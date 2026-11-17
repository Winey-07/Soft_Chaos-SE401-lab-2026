package edu.itc.shop_rest.api;

import java.net.URI;
import java.util.List;
import edu.itc.shop_rest.api.dto.ProductDto;
import edu.itc.shop_rest.api.dto.StockChange;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response;
import edu.itc.shop_rest.domain.DuplicateSkuException;
import edu.itc.shop_rest.domain.InsufficientStockException;
import edu.itc.shop_rest.domain.Product;
import edu.itc.shop_rest.domain.ProductRepository;

@Path("/products")
// @Produces: What format your API can return.
@Produces({
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_XML
})
// @Consumes: What format your API can receive.
@Consumes({
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_XML
})
public class ProductResource {

    private final ProductRepository repo;

    @Inject
    public ProductResource(ProductRepository repo) {
        this.repo = repo;
    }

    @GET
    public List<ProductDto> list() {

    return repo.findAll()
            .stream()
            // It takes each Product and converts it into a ProductDto.
            .map(product -> ProductDto.from(product))
            .toList();
}

    @GET
    @Path("{id: \\d+}")
    // Every product response should now use the same public data structure.
    public ProductDto getById(@PathParam("id") long id) {

        Product product = repo.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Product " + id + " not found"
                        )
                );

        return ProductDto.from(product);
}
    @POST 
    public Response create(
        ProductDto dto,
        @Context UriInfo uriInfo) {
    
    repo.findBySku(dto.getSku())
        .ifPresent(p-> {
            throw new DuplicateSkuException(dto.getSku());
        });
    Product saved = repo.save(dto.toProduct(0, 0));
    
    // Why use UriInfo?
    // Because the API should generate its own Location header instead of hardcoding a URL.
    URI location = uriInfo.getAbsolutePathBuilder()
                          .path(Long.toString(saved.id()))
                          .build();
    
    
    return Response.created(location)
        .entity(ProductDto.from(saved))
        .build();
    }

    @PUT 
    @Path("{id: \\d+}")
    public ProductDto update(
        @PathParam("id") long id,
        ProductDto dto) {

    Product existing = repo.findById(id)
            .orElseThrow(() -> 
                new NotFoundException("Product not found")
        );
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
    public void delete(@PathParam("id") long id){
        if(!repo.delete(id)){
            throw new NotFoundException("Product not found");
        }
    }
    // Why does this return 204?

    // A successful deletion doesn't need to return a product body.

    // JAX-RS returns 204 No Content for this void method.

    // If the product doesn't exist, it returns 404.
    
    @PATCH
    @Path("{id: \\d+}/stock")
    @Consumes(MediaType.APPLICATION_JSON)
    public ProductDto changeStock(
            @PathParam("id") long id,
            StockChange change) {

        Product existing = repo.findById(id)
                .orElseThrow(() ->
                    new NotFoundException("Product not found")
                );

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
