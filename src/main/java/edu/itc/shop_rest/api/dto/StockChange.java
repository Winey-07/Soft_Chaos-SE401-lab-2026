package edu.itc.shop_rest.api.dto;

public record StockChange(int delta) {
    
}
// Here, delta means the amount of change.
//  -5 means remove five items.
// +10 means add ten items.
