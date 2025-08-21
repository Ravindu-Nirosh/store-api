package com.ravindu.store.exceptions;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(){
        super("cart not found");
    }
}
