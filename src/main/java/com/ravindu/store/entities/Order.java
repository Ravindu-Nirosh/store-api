package com.ravindu.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at",insertable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @JoinColumn(name = "customer_id")
    @ManyToOne
    private User customer;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<OrderItem> items= new LinkedHashSet<>();

    public static Order fromCart(Cart cart,User customer){
        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(customer);

        cart.getItems().forEach(cartItem ->{
            var orderItem = new OrderItem(order,cartItem.getProduct(),cartItem.getQuantity());
            order.items.add(orderItem);
        });

        return order;
    }
}
