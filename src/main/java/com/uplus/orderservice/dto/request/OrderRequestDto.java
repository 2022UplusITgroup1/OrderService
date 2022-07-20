package com.uplus.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequestDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("discountType")
    private int discountType;

    @JsonProperty("payPeriod")
    private int payPeriod;

    @JsonProperty("monthPrice")
    private int monthPrice;

    @JsonProperty("phone")
    private PhoneRequestDto phoneRequestDto;

    @JsonProperty("plan")
    private PlanRequestDto planRequestDto;

    @Data
    public static class PhoneRequestDto{

        @JsonProperty("code")
        private String code;

        @JsonProperty("name")
        private String name;

        @JsonProperty("storage")
        private int storage;

        @JsonProperty("color")
        private String color;

        @JsonProperty("price")
        private int price;
        
    }

    @Data
    public static class PlanRequestDto{

        @JsonProperty("code")
        private String code;

        @JsonProperty("name")
        private String name;

        @JsonProperty("price")
        private int price;
        
    }

    @Builder
    public OrderRequestDto(String name, 
                        String email, 
                        String address, 
                        String phoneNumber,
                        int discountType,
                        int payPeriod,
                        int monthPrice,
                        PhoneRequestDto phoneRequestDto,
                        PlanRequestDto planRequestDto) {

        this.name=name;
        this.email=email;
        this.address=address;
        this.phoneNumber=phoneNumber;
        this.discountType=discountType;
        this.payPeriod=payPeriod;
        this.monthPrice=monthPrice;
        this.phoneRequestDto=phoneRequestDto;
        this.planRequestDto=planRequestDto;

    }

}
