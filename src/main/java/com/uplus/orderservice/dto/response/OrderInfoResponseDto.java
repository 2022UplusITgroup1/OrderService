package com.uplus.orderservice.dto.response;

import com.uplus.orderservice.dto.ProductOrderDto;
import com.uplus.orderservice.dto.feign.ProductDto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
public class OrderInfoResponseDto {
    
    private ProductOrderDto productOrder;
    private ProductDto product;


    
    public OrderInfoResponseDto(ProductOrderDto productOrder, ProductDto product) {
        this.productOrder=productOrder;
        this.product=product;
    }

    public OrderInfoResponseDto(ProductOrderDto productOrder) {
        this.productOrder=productOrder;

    }

}
