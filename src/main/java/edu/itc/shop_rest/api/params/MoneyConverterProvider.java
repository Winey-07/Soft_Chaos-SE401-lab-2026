package edu.itc.shop_rest.api.params;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import edu.itc.shop_rest.domain.Money;

@Provider
public class MoneyConverterProvider implements ParamConverterProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType != Money.class) {
            return null;
        }
        return (ParamConverter<T>) new ParamConverter<Money>() {
            @Override
            public Money fromString(String value) {
                return value == null ? null : Money.parse(value);
            }

            @Override
            public String toString(Money value) {
                return value == null ? null : value.toString();
            }
        };
    }
}