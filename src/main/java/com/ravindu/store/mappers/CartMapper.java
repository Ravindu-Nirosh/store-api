package com.ravindu.store.mappers;

import com.ravindu.store.dtos.CartDto;
import com.ravindu.store.dtos.CartItemDto;
import com.ravindu.store.entities.Cart;
import com.ravindu.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice",expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice" ,expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);


}
