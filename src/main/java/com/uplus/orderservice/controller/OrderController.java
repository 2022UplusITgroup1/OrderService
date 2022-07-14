package com.uplus.orderservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.orderservice.dto.CustomerRequestDto;
import com.uplus.orderservice.dto.CustomerResponseDto;
import com.uplus.orderservice.dto.ProductOrderResponseDto;
import com.uplus.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public Long save(@RequestBody CustomerRequestDto requestDto) {

        return orderService.save(requestDto);
    }


    @GetMapping("/order/customer/{id}")
    public CustomerResponseDto findById (@PathVariable Long id) {

        return orderService.findById(id);
    }

    @GetMapping("/order/my/{id}")
    public ProductOrderResponseDto findOrderById (@PathVariable Long id) {

        return orderService.findOrderById(id);
    }

    // @GetMapping("/order/my/list")
    // public ProductOrderResponseDto getOrderList(@RequestParam("name") String name, @RequestParam("phone_number") String phoneNumber, @RequestParam("order_number") String orderNumber) {
    //     CustomerResponseDto customerResponseDto=orderService.findCustomerByNameAndPhoneNumber(name, phoneNumber);
        
    //     ProductOrderResponseDto orderResponseDto= orderService.findOrderListByCustomer(customerResponseDto);



    //     return orderResponseDto;
    // }

    @GetMapping("/order/my")
    public Map<String, Object> getOrderByOrderNumber(@RequestParam("name") String name, @RequestParam("phone_number") String phoneNumber, @RequestParam("order_number") String orderNumber) {
        Map<String, Object> map = new HashMap<>();
        
        CustomerResponseDto customerResponseDto=orderService.findCustomerByNameAndPhoneNumber(name, phoneNumber);
        
        ProductOrderResponseDto productOrderResponseDto= orderService.findOrderByCustomer(customerResponseDto, orderNumber);

        if(productOrderResponseDto==null){
            map.put("status", "204");
            map.put("message", "알맞은 결과를 찾을 수 없습니다.");
            map.put("data", null);
        }else{
            map.put("status", "200");
            map.put("message", "주문 조회 성공");
            map.put("data", productOrderResponseDto);
        }


        return map;
    }

    //상품 주문 결제
    // @PostMapping("/order/payment")
    // public Map<String, Object> payProductOrder(@RequestParam("name") String name, 
    //                                                 @RequestParam("email") String email, 
    //                                                 @RequestParam("address") String address,
    //                                                 @RequestParam("phone_number") String phoneNumber, 
    //                                                 @RequestParam("phone_id") String phoneId,
    //                                                 @RequestParam("plan_id") String planId, 
    //                                                 @RequestParam("month_price") String monthPrice) {

    //     Map<String, Object> map = new HashMap<>();
        
    //     CustomerResponseDto customerResponseDto=orderService.findCustomerByNameAndPhoneNumber(name, phoneNumber);
        
    //     ProductOrderResponseDto productOrderResponseDto= orderService.findOrderByCustomer(customerResponseDto, orderNumber);

    //     if(productOrderResponseDto==null){
    //         map.put("status", "204");
    //         map.put("message", "알맞은 결과를 찾을 수 없습니다.");
    //         map.put("data", null);
    //     }else{
    //         productOrderResponseDto.setName(name);
    //         map.put("status", "200");
    //         map.put("message", "주문 조회 성공");
    //         map.put("data", productOrderResponseDto);
    //     }


    //     return map;
    // }


}
