package edu.itc.shop_rest.api.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import edu.itc.shop_rest.domain.Product;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductDto {

    @XmlAttribute
    private long id;

    @NotBlank
    @Size(max = 80)
    private String name;

    @NotNull
    @Pattern(regexp = "[A-Z]{2}-\\d{2}")
    private String sku;

    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @PositiveOrZero
    private int stock;

    public ProductDto() {
    }

    public ProductDto(long id, String name, String sku, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.stock = stock;
    }

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.id(),
                product.name(),
                product.sku(),
                product.price(),
                product.stock()
        );
    }

    public Product toProduct(long id, int version) {
        return new Product(id, name, sku, price, stock, version);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}