package com.ravindu.store.dtos;

import lombok.Data;

@Data
public class CheckOutResponse {
    private Long orderId;
    private String checkoutUrl;


    public CheckOutResponse(Long orderId, String url){
        this.orderId=orderId;
        this.checkoutUrl=url;
    }
}
