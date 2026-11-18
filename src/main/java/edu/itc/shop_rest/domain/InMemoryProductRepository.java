package edu.itc.shop_rest.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// import org.glassfish.jaxb.core.v2.model.core.ID;

public class InMemoryProductRepository implements ProductRepository {

    // store products using their IDs
    private final ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();
    // Why not use a normal HashMap?
    // Because multiple HTTP requests may access the repository simultaneously. ConcurrentHashMap is designed for concurrent access.

    // This generates unique IDs.
    private final AtomicLong ids = new AtomicLong(0);
    // Why AtomicLong?
    // requests might create products at the same time. AtomicLong makes ID generation safe between threads.

    public InMemoryProductRepository() {
       
        save(new Product(0, "Kettle", "KT-01", new BigDecimal("24.90"), 12, 0));

        save(new Product(0, "Mug", "MG-01", new BigDecimal("6.50"), 40, 0));

        save(new Product(0, "Tea", "TE-01", new BigDecimal("4.20"), 0, 0));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(products.values());
    }

    @Override
    public Optional<Product> findById(long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return products.values().stream()
                .filter(p -> p.sku().equals(sku))
                .findFirst();
    }

    @Override
    public Product save(Product product) {
        long id = product.id() == 0
                ? ids.incrementAndGet()
                : product.id();

        int version = product.id() == 0
                ? 1
                : product.version() + 1;

        Product saved = new Product(
                id,
                product.name(),
                product.sku(),
                product.price(),
                product.stock(),
                version
        );
         // save()
        // This puts the product into the map.
        // When the product is new, it receives an ID and version 1. 
        // When an existing product is saved, its version increases.
        products.put(id, saved);
        return saved;
    }

    @Override
    public boolean delete(long id) {
        return products.remove(id) != null;
        
    }
}