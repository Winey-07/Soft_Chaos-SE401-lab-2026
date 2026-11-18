package edu.itc.shop_rest.api.filter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import jakarta.ws.rs.NameBinding;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Compress {
}