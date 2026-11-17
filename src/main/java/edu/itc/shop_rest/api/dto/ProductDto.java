
package edu.itc.shop_rest.api.dto;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import edu.itc.shop_rest.domain.Product;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductDto {

    private long id;
    private String name;
    private String sku;
    private BigDecimal price;
    private int stock;

    // Required no-argument constructor
    // This creates an empty DTO object.
    
    public ProductDto() {
    }

    // Constructor
    public ProductDto(
            long id,
            String name,
            String sku,
            BigDecimal price,
            int stock) {

        this.id = id;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.stock = stock;
    }

    // Convert Product into ProductDto
    public static ProductDto from(Product product) {

        return new ProductDto(
                product.id(),
                product.name(),
                product.sku(),
                product.price(),
                product.stock()
        );
    }

    // Convert ProductDto back into Product
    public Product toProduct(long id, int version) {

        return new Product(
                id,
                name,
                sku,
                price,
                stock,
                version
        );
    }

    // Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
