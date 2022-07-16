package com.uplus.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductOrderRequestDto {
    private String name;
    private String email;
    private String address;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("phone_code")
    private String phoneCode;

    @JsonProperty("phone_color")
    private String phoneColor;

    @JsonProperty("plan_code")
    private String planCode;

    @JsonProperty("discount_type")
    private int discountType;

    @JsonProperty("month_price")
    private String monthPrice;

    @Builder
    public ProductOrderRequestDto(String name, 
                        String email, 
                        String address, 
                        String phoneNumber,
                        String phoneCode,
                        String phoneColor,
                        String planCode,
                        int discountType,
                        String monthPrice) {

        this.name=name;
        this.email=email;
        this.address=address;
        this.phoneNumber=phoneNumber;
        this.phoneCode=phoneCode;
        this.phoneColor=phoneColor;
        this.planCode=planCode;
        this.discountType=discountType;
        this.monthPrice=monthPrice;

    }
}
