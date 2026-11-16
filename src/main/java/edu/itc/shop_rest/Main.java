package edu.itc.shop_rest;

import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
// import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import edu.itc.shop_rest.domain.InMemoryProductRepository;
import edu.itc.shop_rest.domain.ProductRepository;

public class Main {
    // This defines your API's base address.
    public static final URI BASE = URI.create("http://localhost:8080/api/");

    /** The application: scanned resources/providers + one shared repository. */
    public static ResourceConfig config() {
        // This tells Jersey to scan your API package for REST resources and providers.
        return new ResourceConfig()
                .packages("edu.itc.shop_rest.api")
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        // This creates one repository instance and registers it for dependency injection.
                        bind(new InMemoryProductRepository()).to(ProductRepository.class);
                        // we need it because ProductResource needs a repository. 
                        // Jersey uses this configuration to provide the repository automatically.
                    }
                });
    }

    public static void main(String[] args) throws Exception {
        // The server is started here:
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE, config()); // This starts Grizzly at port 8080.
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
        System.out.println("shop-rest listening on " + BASE);
        Thread.currentThread().join();
    }
}