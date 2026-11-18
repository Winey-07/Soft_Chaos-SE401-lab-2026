package edu.itc.shop_rest.api;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import edu.itc.shop_rest.api.dto.ProductDto;
import edu.itc.shop_rest.api.dto.StockChange;
import edu.itc.shop_rest.api.filter.Compress;
import edu.itc.shop_rest.api.params.ProductFilter;
import edu.itc.shop_rest.domain.*;

@Path("/products")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProductResource {

    private final ProductRepository repo;

    @Inject
    public ProductResource(ProductRepository repo) {
        this.repo = repo;
    }

    @GET
    @Compress
    public List<ProductDto> list(@BeanParam @Valid ProductFilter f) {
        if (f.minPrice != null && !"EUR".equals(f.minPrice.currency())) {
            throw new BadRequestException("minPrice currency must be EUR");
        }
        if (f.maxPrice != null && !"EUR".equals(f.maxPrice.currency())) {
            throw new BadRequestException("maxPrice currency must be EUR");
        }

        Comparator<Product> comparator = switch (f.sortField()) {
            case "name" -> Comparator.comparing(Product::name, String.CASE_INSENSITIVE_ORDER);
            case "price" -> Comparator.comparing(Product::price);
            case "stock" -> Comparator.comparingInt(Product::stock);
            default -> throw new BadRequestException("Unknown sort field: " + f.sortField());
        };
        if (f.descending()) {
            comparator = comparator.reversed();
        }

        return repo.findAll().stream()
                .filter(p -> f.q == null || f.q.isBlank()
                        || p.name().toLowerCase().contains(f.q.toLowerCase()))
                .filter(p -> f.minPrice == null || p.price().compareTo(f.minPrice.amount()) >= 0)
                .filter(p -> f.maxPrice == null || p.price().compareTo(f.maxPrice.amount()) <= 0)
                .filter(p -> !f.inStock || p.stock() > 0)
                .sorted(comparator)
                .map(ProductDto::from)
                .toList();
    }

    @GET
    @Path("{id: \\d+}")
    public ProductDto getById(@PathParam("id") long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product " + id + " not found"));
        return ProductDto.from(product);
    }

    @POST
    public Response create(@Valid @NotNull ProductDto dto, @Context UriInfo uriInfo) {
        repo.findBySku(dto.getSku())
                .ifPresent(p -> {
                    throw new DuplicateSkuException(dto.getSku());
                });
        Product saved = repo.save(dto.toProduct(0, 0));
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(Long.toString(saved.id()))
                .build();
        return Response.created(location)
                .entity(ProductDto.from(saved))
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public ProductDto update(@PathParam("id") long id, @Valid @NotNull ProductDto dto) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product " + id + " not found"));
        Product updated = new Product(
                id,
                dto.getName(),
                dto.getSku(),
                dto.getPrice(),
                dto.getStock(),
                existing.version()
        );
        return ProductDto.from(repo.save(updated));
    }

    @DELETE
    @Path("{id: \\d+}")
    public void delete(@PathParam("id") long id) {
        if (!repo.delete(id)) {
            throw new NotFoundException("Product " + id + " not found");
        }
    }

    @PATCH
    @Path("{id: \\d+}/stock")
    @Consumes(MediaType.APPLICATION_JSON)
    public ProductDto changeStock(@PathParam("id") long id, @Valid @NotNull StockChange change) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product " + id + " not found"));

        int newStock = existing.stock() + change.delta();
        if (newStock < 0) {
            throw new InsufficientStockException();
        }

        Product updated = new Product(
                id,
                existing.name(),
                existing.sku(),
                existing.price(),
                newStock,
                existing.version()
        );
        return ProductDto.from(repo.save(updated));
    }
}