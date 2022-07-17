package com.uplus.orderservice.feginclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uplus.orderservice.dto.ResponseDto;
import com.uplus.orderservice.dto.ResponseMessage;

@FeignClient("productservice")
public interface ProductServiceClient {

    @GetMapping("/product/detail")
    ResponseDto getProductDetail(@RequestParam(value = "pl_code") String planCode,
                                        @RequestParam(value = "ph_code") String phoneCode,
                                        @RequestParam(value = "color") String color,
                                        @RequestParam(value = "dc_type") Integer discountType);
                                        
}
