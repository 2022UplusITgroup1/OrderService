package com.uplus.orderservice.dto.feign;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ProductDto {


    @JsonProperty("phone")
    private PhoneDto phone;

    @JsonProperty("plan")
    private PlanDto plan;

    // @Builder
    // public ProductResponseDto(ProductOrder entity) {
    //     this.status
    // }

}
