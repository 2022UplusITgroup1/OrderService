package com.uplus.orderservice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PaymentResponseDto {
    
    private String orderNumer;

    public PaymentResponseDto(String orderNumber) {
        this.orderNumer=orderNumber;

    }
}
