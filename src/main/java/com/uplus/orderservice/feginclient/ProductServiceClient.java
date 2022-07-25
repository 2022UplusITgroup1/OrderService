package com.uplus.orderservice.feginclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uplus.orderservice.dto.feign.PhoneDto;
import com.uplus.orderservice.dto.feign.ProductDto;
import com.uplus.orderservice.dto.response.ResponseMessage;

@FeignClient("productservice")
public interface ProductServiceClient {

    @GetMapping("/product/detail")
    ResponseMessage<ProductDto> getProductDetail(@RequestParam(value = "pl_code") String planCode,
                                        @RequestParam(value = "ph_code") String phoneCode,
                                        @RequestParam(value = "color") String color,
                                        @RequestParam(value = "dc_type") Integer discountType);


    @PutMapping("/product/sales/{code}/{color}")
    ResponseMessage<PhoneDto> updateSales(@PathVariable("code") final String phoneCode,
                                        @PathVariable("color") final String phoneColor);
                                        
}
