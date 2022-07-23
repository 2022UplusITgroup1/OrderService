package com.uplus.orderservice.dto.response.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uplus.orderservice.dto.common.PhoneDto;
import com.uplus.orderservice.dto.common.PlanDto;
import com.uplus.orderservice.dto.common.ResponseDto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ProductResponseDto extends ResponseDto{

    @JsonProperty("data")
    private DataResponseDto data;

    @Data
    public static class DataResponseDto{

        @JsonProperty("phone")
        private PhoneDto phone;

        @JsonProperty("plan")
        private PlanDto plan;
    }

    // @Builder
    // public ProductResponseDto(ProductOrder entity) {
    //     this.status
    // }

}
