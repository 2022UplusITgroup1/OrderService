package com.uplus.orderservice.dto;

import com.uplus.orderservice.entity.ProductOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductOrderResponseDto {

    private String name;
    private Long phoneId;
    private Long planId;
    private String orderNumber;
    private int monthPrice;
    


    public ProductOrderResponseDto(ProductOrder entity) {
        this.phoneId = entity.getPhoneId();
        this.planId = entity.getPlanId();
        this.orderNumber = entity.getOrderNumber();
        this.monthPrice = entity.getMonthPrice();
    }


    public void setName(String name) {
        this.name=name;
    }

}
