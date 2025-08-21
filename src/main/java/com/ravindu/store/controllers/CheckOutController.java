package com.ravindu.store.controllers;

import com.ravindu.store.dtos.CheckOutRequest;
import com.ravindu.store.dtos.CheckOutResponse;
import com.ravindu.store.dtos.ErrorDto;
import com.ravindu.store.entities.OrderStatus;
import com.ravindu.store.exceptions.CartEmptyException;
import com.ravindu.store.exceptions.CartNotFoundException;
import com.ravindu.store.exceptions.PaymentException;
import com.ravindu.store.repositories.OrderRepository;
import com.ravindu.store.services.CheckoutService;
import com.ravindu.store.services.WebhookRequest;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckOutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;


    @PostMapping
    public CheckOutResponse checkout(@Valid @RequestBody CheckOutRequest request){
            return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handleWebhook(
            @RequestHeader Map<String,String> headers,
            @RequestBody String payload
    ){
        checkoutService.handelWebhookEvent(new WebhookRequest(headers,payload));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handelException(Exception e){
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }


    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("error creating checkout session"));
    }
}
