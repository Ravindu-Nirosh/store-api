package com.ravindu.store.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(){
        super("order not found");
    }
}
