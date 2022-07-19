package com.uplus.orderservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.orderservice.dto.ResponseDto;
import com.uplus.orderservice.dto.ResponseMessage;
import com.uplus.orderservice.dto.StatusCode;
import com.uplus.orderservice.dto.StatusMessage;
import com.uplus.orderservice.dto.request.CustomerRequestDto;
import com.uplus.orderservice.dto.request.OrderRequestDto;
import com.uplus.orderservice.dto.request.ProductOrderRequestDto;
import com.uplus.orderservice.dto.response.CustomerResponseDto;
import com.uplus.orderservice.dto.response.PaymentResponseDto;
import com.uplus.orderservice.dto.response.ProductOrderResponseDto;
import com.uplus.orderservice.entity.Customer;
import com.uplus.orderservice.entity.ProductOrder;
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


    // @PostMapping("/order")
    // public Long save(@RequestBody CustomerRequestDto requestDto) {

    //     return orderService.save(requestDto);
    // }


    // @GetMapping("/customer/{id}")
    // public CustomerResponseDto findById (@PathVariable Long id) {

    //     return orderService.findById(id);
    // }

    // @GetMapping("/my/{id}")
    // public ProductOrderResponseDto findOrderById (@PathVariable Long id) {

    //     return orderService.findOrderById(id);
    // }

    //상품 주문 상세
    @PostMapping("/orderform")
    public Map<String, Object> getOrderForm(@RequestBody OrderRequestDto orderRequestDto) {
        Map<String, Object> map = new HashMap<>();

        // logger.info("name : " + name +
        //             " phone_number : " + phoneNumber + " order_number : " + orderNumber);
        
        
        // CustomerResponseDto customerResponseDto=orderService.getCustomer(name, phoneNumber);

        map.put("orderRequestDto", orderRequestDto);

        


        return map;
    }

    // @GetMapping("/order/my/list")
    // public ProductOrderResponseDto getOrderList(@RequestParam("name") String name, @RequestParam("phone_number") String phoneNumber, @RequestParam("order_number") String orderNumber) {
    //     CustomerResponseDto customerResponseDto=orderService.findCustomerByNameAndPhoneNumber(name, phoneNumber);
        
    //     ProductOrderResponseDto orderResponseDto= orderService.findOrderListByCustomer(customerResponseDto);



    //     return orderResponseDto;
    // }

    @GetMapping("/my")
    public ResponseMessage getOrderByOrderNumber(@RequestParam("name") String name, @RequestParam("phone_number") String phoneNumber, @RequestParam("order_number") String orderNumber) {
        int statusCode=StatusCode.SERVICE_UNAVAILABLE;
        String statusMessage=StatusMessage.NOT_AVAILABLE_SERVICE;
        ProductOrderResponseDto productOrderResponseDto=null;

        logger.info("name : " + name +
                    " phone_number : " + phoneNumber + " order_number : " + orderNumber);
        
        
        Customer customer=orderService.getCustomer(name, phoneNumber);

        if(customer==null){

            statusCode=StatusCode.NO_CONTENT;
            statusMessage=StatusMessage.NOT_FOUND_CUSTOMER;

        }else{

            ProductOrder productOrder= orderService.getOrder(customer, orderNumber);

            if(productOrder==null){

                statusCode=StatusCode.NO_CONTENT;
                statusMessage=StatusMessage.NOT_FOUND_ORDER_NUMBER;
            }else{
                statusCode=StatusCode.OK;
                statusMessage=StatusMessage.SUCCESS_READ_PRODUCT_ORDER;

            }
        }
        

        return ResponseMessage.res(statusCode,statusMessage,productOrderResponseDto);
    }

    //상품 주문 결제
    @PostMapping("/payment")
    public ResponseMessage payProductOrder(@RequestBody OrderRequestDto orderRequestDto) {
        int statusCode=StatusCode.SERVICE_UNAVAILABLE;
        String statusMessage=StatusMessage.NOT_AVAILABLE_SERVICE;
        PaymentResponseDto paymentResponseDto=null;

        
        //주문한 phone_code, color 로 phone 상세 조회
        // logger.info("planCode : " + productOrderRequestDto.getPlanCode() +
        //             " phoneCode : " + productOrderRequestDto.getPhoneCode() + " phoneColor : " + productOrderRequestDto.getPhoneColor() + " discountType " + productOrderRequestDto.getDiscountType());

        ResponseDto productResponseDto=orderService.getProductDetail(orderRequestDto);
        //주문한 plan_code로 plan 상세 조회


        int phonePrice=(int) ((Map<String, Object>) productResponseDto.getData().get("phone")).get("price");
        int planPrice=(int) ((Map<String, Object>) productResponseDto.getData().get("plan")).get("price");
        int payPeriod=orderRequestDto.getPayPeriod();
        int discountType=orderRequestDto.getDiscountType();

        int monthPrice=orderRequestDto.getMonthPrice();
    

        //productResponseDto 의
        //단말 가격/할부기간
        // +
        //요금제 가격
        //==month_price 이면 결제.

        if(productResponseDto.getStatus()==200){
            if(orderService.calculatePrice(phonePrice, planPrice, payPeriod, discountType)==monthPrice){

                //결제 실행

                //주문자 정보 입력
                String orderNumber=orderService.saveCustomerProductOrder(orderRequestDto);
                if(orderNumber!=null){// Transaction 성공시
                    //주문 결제가 완료되었습니다.

                    statusCode=StatusCode.OK;
                    statusMessage=StatusMessage.SUCCESS_PAY_PRODUCT_ORDER;
                    paymentResponseDto=new PaymentResponseDto(orderNumber);

                    //판매량 증가
                    ResponseDto updateSalesResponseDto=orderService.updateProductSales(orderRequestDto);
                    if(updateSalesResponseDto==null){
                        //메세지큐에 담기.. 추후 구현
                    }


                }else{// Transaction 실패시
                    //주문 결제가 실패하였습니다.
                    statusCode=StatusCode.SERVICE_UNAVAILABLE;
                    statusMessage=StatusMessage.FAIL_PRODUCT_ORDER;
                }


            }else{
                //DB 가격 정보 상이. 결제 실패
                //주문 정보가 다릅니다. 주문을 다시 진행해주세요.

                statusCode=StatusCode.OK;
                statusMessage=StatusMessage.NOT_FOUND_PRODUCT_ORDER;
            }
        }else{
            //Product 정보 가져오기 실패
            //"상품 정보를 확인할 수 없습니다."

            statusCode=StatusCode.NOT_FOUND;    
            statusMessage=StatusMessage.NOT_FOUND_PRODUCT_ORDER;
        }

        return ResponseMessage.res(statusCode, statusMessage,paymentResponseDto);
        
    }
    
    
}
