package com.ravindu.store.controllers;

import com.ravindu.store.dtos.ErrorDto;
import com.ravindu.store.dtos.OrderDto;
import com.ravindu.store.exceptions.OrderNotFoundException;
import com.ravindu.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getOrders(){
        return orderService.getOrders();
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable("orderId") Long orderId){
        return orderService.getOrder(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Void> handleException(Exception e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(Exception e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(e.getMessage()));
    }

}
