package com.ravindu.store.mappers;

import com.ravindu.store.dtos.OrderDto;
import com.ravindu.store.entities.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
