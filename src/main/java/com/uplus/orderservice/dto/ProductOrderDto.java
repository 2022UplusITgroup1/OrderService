package com.uplus.orderservice.dto;

import com.uplus.orderservice.dto.response.BaseResponseDto;
import com.uplus.orderservice.entity.ProductOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductOrderDto {

    private String name;
    private String phoneCode;
    private String planCode;
    private String color;
    private int discountType;
    private int payPeriod;
    private String orderNumber;
    private int monthPrice;
    


    public ProductOrderDto(ProductOrder entity) {
        this.phoneCode = entity.getPhoneCode();
        this.color=entity.getPhoneColor();
        this.planCode = entity.getPlanCode();
        this.discountType=entity.getDiscountType();
        this.payPeriod=entity.getPayPeriod();
        this.orderNumber = entity.getOrderNumber();
        this.monthPrice = entity.getMonthPrice();
    }

    public ProductOrderDto(ProductOrder entity, String name) {
        this.name=name;
        this.phoneCode = entity.getPhoneCode();
        this.color=entity.getPhoneColor();
        this.planCode = entity.getPlanCode();
        this.discountType=entity.getDiscountType();
        this.payPeriod=entity.getPayPeriod();
        this.orderNumber = entity.getOrderNumber();
        this.monthPrice = entity.getMonthPrice();
    }



}
