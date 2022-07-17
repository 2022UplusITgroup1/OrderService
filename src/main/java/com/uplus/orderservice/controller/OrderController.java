package com.uplus.orderservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.orderservice.dto.CustomerRequestDto;
import com.uplus.orderservice.dto.CustomerResponseDto;
import com.uplus.orderservice.dto.PhoneResponseDto;
import com.uplus.orderservice.dto.PlanResponseDto;
import com.uplus.orderservice.dto.ProductOrderRequestDto;
import com.uplus.orderservice.dto.ProductOrderResponseDto;
import com.uplus.orderservice.dto.ResponseDto;
import com.uplus.orderservice.dto.ResponseMessage;
import com.uplus.orderservice.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping("/order")
    public Long save(@RequestBody CustomerRequestDto requestDto) {

        return orderService.save(requestDto);
    }


    @GetMapping("/customer/{id}")
    public CustomerResponseDto findById (@PathVariable Long id) {

        return orderService.findById(id);
    }

    @GetMapping("/my/{id}")
    public ProductOrderResponseDto findOrderById (@PathVariable Long id) {

        return orderService.findOrderById(id);
    }

    // @GetMapping("/order/my/list")
    // public ProductOrderResponseDto getOrderList(@RequestParam("name") String name, @RequestParam("phone_number") String phoneNumber, @RequestParam("order_number") String orderNumber) {
    //     CustomerResponseDto customerResponseDto=orderService.findCustomerByNameAndPhoneNumber(name, phoneNumber);
        
    //     ProductOrderResponseDto orderResponseDto= orderService.findOrderListByCustomer(customerResponseDto);



    //     return orderResponseDto;
    // }

    @GetMapping("/my")
    public Map<String, Object> getOrderByOrderNumber(@RequestParam("name") String name, @RequestParam("phone_number") String phoneNumber, @RequestParam("order_number") String orderNumber) {
        Map<String, Object> map = new HashMap<>();

        logger.info("name : " + name +
                    " phone_number : " + phoneNumber + " order_number : " + orderNumber);
        
        
        CustomerResponseDto customerResponseDto=orderService.getCustomer(name, phoneNumber);

        if(customerResponseDto==null){
            map.put("status", "204");
            map.put("message", "주문자 이름 또는 전화번호를 찾을 수 없습니다.");
            map.put("data", null);

        }else{

            ProductOrderResponseDto productOrderResponseDto= orderService.getOrder(customerResponseDto, orderNumber);

            if(productOrderResponseDto==null){
                map.put("status", "204");
                map.put("message", "알맞은 주문 번호를 찾을 수 없습니다.");
                map.put("data", null);
            }else{
                map.put("status", "200");
                map.put("message", "주문 조회 성공");
                map.put("data", productOrderResponseDto);
            }
        }
        
        


        return map;
    }

    //상품 주문 결제
    @PostMapping("/payment")
    public ResponseDto payProductOrder(@RequestBody ProductOrderRequestDto productOrderRequestDto) {

        // Map<String, Object> map = new HashMap<>();
        
        //주문한 phone_code, color 로 phone 상세 조회
        logger.info("planCode : " + productOrderRequestDto.getPlanCode() +
                    " phoneCode : " + productOrderRequestDto.getPhoneCode() + " phoneColor : " + productOrderRequestDto.getPhoneColor() + " discountType " + productOrderRequestDto.getDiscountType());

        ResponseDto productResponseDto=orderService.getProductDetail(productOrderRequestDto.getPlanCode(), productOrderRequestDto.getPhoneCode(), productOrderRequestDto.getPhoneColor(), productOrderRequestDto.getDiscountType());
        //주문한 plan_code로 plan 상세 조회


        int phonePrice=(int) ((Map<String, Object>) productResponseDto.getData().get("phone")).get("price");
        int planPrice=(int) ((Map<String, Object>) productResponseDto.getData().get("plan")).get("price");
        int payPeriod=productOrderRequestDto.getPayPeriod();
        int discountType=productOrderRequestDto.getDiscountType();

        int monthPrice=productOrderRequestDto.getMonthPrice();
        

        // logger.info(" phonePrice : " + ((Map<String, Object>) productResponseDto.getData().get("phone")).get("price"));

        //productResponseDto 의
        //단말 가격/할부기간
        // +
        //요금제 가격
        //==month_price 이면 결제.

        if(productResponseDto.getStatus()==200){
            if(orderService.calculatePrice(phonePrice, planPrice, payPeriod, discountType)==monthPrice){

                //결제 실행

                //주문자 정보 입력
                Long customerId=orderService.saveCustomerProductOrder(productOrderRequestDto);
                // if(customerId!=null){// Transaction 성공시
                //     //주문 정보 입력
                //     Long productOrderId=orderService.saveProductOrder(productOrderRequestDto, customerId);
                // }

            }else{
                //DB 가격 정보 상이. 결제 실패

            }
        }else{
            //Product 정보 가져오기 실패
        }

        return productResponseDto;
    }
    

}
