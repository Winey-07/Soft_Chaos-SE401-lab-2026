package edu.itc.shop_rest.domain;

import java.math.BigDecimal;

public record Money(BigDecimal amount, String currency) {
    public static Money parse(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Cannot parse Money from blank input");
        }
        String[] parts = raw.trim().split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Cannot parse Money from '" + raw + "'");
        }
        try {
            return new Money(new BigDecimal(parts[0]), parts[1].toUpperCase());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse Money from '" + raw + "'", e);
        }
    }

    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency;
    }
}