package com.ravindu.store.repositories;

import com.ravindu.store.entities.Order;
import com.ravindu.store.entities.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o FROM Order o WHERE o.customer=:customer")
    List<Order> getAllByCustomer(@Param("customer") User customer);

    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o FROM Order o WHERE o.id=:orderId")
    Optional<Order> getOrderByIdWithItems(@Param("orderId") Long orderId);
}