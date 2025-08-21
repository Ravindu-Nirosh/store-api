package com.ravindu.store.services;

import com.ravindu.store.dtos.OrderDto;
import com.ravindu.store.exceptions.OrderNotFoundException;
import com.ravindu.store.mappers.OrderMapper;
import com.ravindu.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getOrders(){
        var user=authService.getCurrentUser();
        var orders=orderRepository.getAllByCustomer(user);

        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order= orderRepository.getOrderByIdWithItems(orderId)
                .orElseThrow(OrderNotFoundException::new);

        var user=authService.getCurrentUser();
        if (!order.getCustomer().getId().equals(user.getId())){
            throw new AccessDeniedException("you do not have access to this order");
        }

        return orderMapper.toDto(order);
    }
}
