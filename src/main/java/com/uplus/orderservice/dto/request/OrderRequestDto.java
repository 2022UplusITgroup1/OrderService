package com.uplus.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uplus.orderservice.dto.feign.PhoneDto;
import com.uplus.orderservice.dto.feign.PlanDto;

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

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("discountType")
    private int discountType;

    @JsonProperty("payPeriod")
    private int payPeriod;

    @JsonProperty("monthPrice")
    private int monthPrice;

    @JsonProperty("phone")
    private PhoneDto phoneDto;

    @JsonProperty("plan")
    private PlanDto planDto;


    @Builder
    public OrderRequestDto(String name, 
                        String email, 
                        String address, 
                        String phoneNumber,
                        int discountType,
                        int payPeriod,
                        int monthPrice,
                        PhoneDto phoneDto,
                        PlanDto planDto) {

        this.name=name;
        this.email=email;
        this.address=address;
        this.phoneNumber=phoneNumber;
        this.discountType=discountType;
        this.payPeriod=payPeriod;
        this.monthPrice=monthPrice;
        this.phoneDto=phoneDto;
        this.planDto=planDto;

    }

}
