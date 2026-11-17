package edu.itc.shop_rest.domain;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

public class InsufficientStockException extends ClientErrorException {
    public InsufficientStockException() {
        super("Insfficient stock", Response.Status.CONFLICT);
    }
    
}
