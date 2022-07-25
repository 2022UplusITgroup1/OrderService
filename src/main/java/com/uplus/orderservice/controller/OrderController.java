package com.uplus.orderservice.controller;

import java.util.HashMap;
import java.util.Map;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.orderservice.dto.CustomerDto;
import com.uplus.orderservice.dto.ProductOrderDto;
import com.uplus.orderservice.dto.feign.PhoneDto;
import com.uplus.orderservice.dto.feign.ProductDto;
import com.uplus.orderservice.dto.request.OrderRequestDto;
import com.uplus.orderservice.dto.response.OrderInfoResponseDto;
import com.uplus.orderservice.dto.response.ResponseMessage;
import com.uplus.orderservice.entity.Customer;
import com.uplus.orderservice.entity.ProductOrder;
import com.uplus.orderservice.service.OrderService;

import com.uplus.orderservice.dto.response.StatusCode;
import com.uplus.orderservice.dto.response.StatusMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    //상품 주문조회
    @GetMapping("/my")
    public ResponseMessage getOrderInfo(@RequestParam("name") String name, @RequestParam("phone_number") String phoneNumber, @RequestParam("order_number") String orderNumber) {
  
        CustomerDto customerDto=orderService.getCustomer(name, phoneNumber);

        if(customerDto==null)
            return ResponseMessage.res(StatusCode.NO_CONTENT,StatusMessage.NOT_FOUND_CUSTOMER,customerDto);
        
        ProductOrderDto productOrderDto= orderService.getOrder(customerDto, orderNumber);

        if(productOrderDto==null)
            return ResponseMessage.res(StatusCode.NO_CONTENT,StatusMessage.NOT_FOUND_ORDER_NUMBER,productOrderDto);

        ResponseMessage<ProductDto> productResponseDto=orderService.getProductDetail(productOrderDto.getPlanCode(), productOrderDto.getPhoneCode(), productOrderDto.getColor(), productOrderDto.getDiscountType());

        if(productResponseDto==null)
            return ResponseMessage.res(StatusCode.PARTIAL_CONTENT,StatusMessage.NOT_FOUND_PRODUCT,new OrderInfoResponseDto(productOrderDto));

        return ResponseMessage.res(StatusCode.OK,StatusMessage.SUCCESS_READ_PRODUCT_ORDER,new OrderInfoResponseDto(productOrderDto, productResponseDto.getData()));
    }




    //상품 주문 결제
    @PostMapping("/payment")
    public ResponseMessage payProductOrder(@RequestBody OrderRequestDto orderRequestDto) {

        //주문한 code로 product 상세 조회
        ResponseMessage<ProductDto> productResponseDto=orderService.getProductDetail(orderRequestDto.getPlanDto().getCode(), orderRequestDto.getPhoneDto().getCode(),orderRequestDto.getPhoneDto().getColor(),orderRequestDto.getDiscountType());

        if(orderService.isAvailableResponse(productResponseDto)){
            if(orderService.isValidPrice(orderRequestDto, productResponseDto)){
                //결제 실행
                //주문, 주문자 정보 저장
                ProductOrderDto productOrderDto=orderService.saveCustomerProductOrder(orderRequestDto);
                //판매량 증가
                ResponseMessage<PhoneDto> updateSalesResponseDto=orderService.updateProductSales(productOrderDto);

                if(orderService.isAvailableResponse(updateSalesResponseDto)==false){
                    //메세지큐에 담기.. 추후 구현
                }

                return ResponseMessage.res(StatusCode.OK,StatusMessage.SUCCESS_PAY_PRODUCT_ORDER, productOrderDto.getOrderNumber());

                
            }else{
                return ResponseMessage.res(StatusCode.OK,StatusMessage.NOT_MATCH_PRODUCT_ORDER_PRICE);
            }
        }else{
            return ResponseMessage.res(StatusCode.NOT_FOUND,StatusMessage.NOT_FOUND_PRODUCT);
        }
        
    }
    
    
}
