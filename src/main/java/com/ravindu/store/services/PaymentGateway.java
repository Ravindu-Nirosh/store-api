package com.ravindu.store.services;

import com.ravindu.store.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckOutSession createCheckOutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
