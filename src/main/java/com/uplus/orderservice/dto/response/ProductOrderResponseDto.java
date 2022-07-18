package com.uplus.orderservice.dto.response;

import com.uplus.orderservice.entity.ProductOrder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductOrderResponseDto {

    private String name;
    private String phoneCode;
    private String planCode;
    private String color;
    private String orderNumber;
    private int monthPrice;
    


    public ProductOrderResponseDto(ProductOrder entity) {
        this.phoneCode = entity.getPhoneCode();
        this.color=entity.getPhoneColor();
        this.planCode = entity.getPlanCode();
        this.orderNumber = entity.getOrderNumber();
        this.monthPrice = entity.getMonthPrice();
    }


    public void setName(String name) {
        this.name=name;
    }

}
