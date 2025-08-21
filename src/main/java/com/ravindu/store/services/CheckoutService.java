package com.ravindu.store.services;

import com.ravindu.store.dtos.CheckOutRequest;
import com.ravindu.store.dtos.CheckOutResponse;
import com.ravindu.store.entities.Order;
import com.ravindu.store.entities.OrderStatus;
import com.ravindu.store.exceptions.CartEmptyException;
import com.ravindu.store.exceptions.CartNotFoundException;
import com.ravindu.store.exceptions.PaymentException;
import com.ravindu.store.repositories.CartRepository;
import com.ravindu.store.repositories.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;



    @Transactional
    public CheckOutResponse checkout(CheckOutRequest request) {
        var cart=cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart==null){
           throw new CartNotFoundException();
        }

        if (cart.isEmpty()){
            throw new CartEmptyException();
        }


        var order= Order.fromCart(cart,authService.getCurrentUser());
        orderRepository.save(order);

        try{
            var session= paymentGateway.createCheckOutSession(order);
            cartService.clearCart(cart.getId());
            return new CheckOutResponse(order.getId(),session.getCheckOutUrl());
        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }

    }

    public void handelWebhookEvent(WebhookRequest request){
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order=orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
    }

}
