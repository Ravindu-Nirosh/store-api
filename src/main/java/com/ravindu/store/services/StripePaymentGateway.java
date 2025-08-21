package com.ravindu.store.services;

import com.ravindu.store.entities.Order;
import com.ravindu.store.entities.OrderStatus;
import com.ravindu.store.exceptions.PaymentException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway{
    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckOutSession createCheckOutSession(Order order) {
        //create a checkout session
        try {
            var buildr= SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl+"/checkout-success?orderId="+order.getId())
                    .setCancelUrl(websiteUrl+"/checkout-cancel.html")
                    .putMetadata("order_id",order.getId().toString());

            order.getItems().forEach(item->{
                var lineItem=  SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build()
                                        )
                                        .build()
                        ).build();
                buildr.addLineItem(lineItem);
            });

            var session= Session.create(buildr.build());
            return new CheckOutSession(session.getUrl());
        } catch (StripeException ex){
            System.out.println(ex.getMessage());

            throw new PaymentException();
        }
}

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            var payload =request.getPayload();
            var headers =request.getHeaders().get("stripe-signature");
            var event= Webhook.constructEvent(payload,headers,webhookSecretKey);

            switch (event.getType()){
                case "payment_intent.succeeded" -> {
                    //Update order status (PAID)
                    return Optional.of(new PaymentResult(extractOrderId(event),OrderStatus.PAID));
                }
                case "payment_intent.payment_failed" -> {
                    //Update order status (FAILED)
                    return Optional.of(new PaymentResult(extractOrderId(event),OrderStatus.FAILED));
                }
                default -> {
                    return Optional.empty();
                }
            }

        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid Signature");
        }
    }

    private Long extractOrderId(Event event){
        var stripObject=event.getDataObjectDeserializer().getObject().orElseThrow(
                ()->new PaymentException("Can not Deserialize Stripe event ,Please Check Stripe SDK Version")
        );

        var paymentIntent = (PaymentIntent) stripObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

}
