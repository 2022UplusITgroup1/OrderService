package com.uplus.orderservice.controller;

import java.util.HashMap;
import java.util.Map;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.orderservice.dto.ResponseMessage;
import com.uplus.orderservice.dto.StatusCode;
import com.uplus.orderservice.dto.StatusMessage;
import com.uplus.orderservice.dto.common.ResponseDto;
import com.uplus.orderservice.dto.request.OrderRequestDto;
import com.uplus.orderservice.dto.response.PaymentResponseDto;
import com.uplus.orderservice.dto.response.ProductOrderResponseDto;
import com.uplus.orderservice.dto.response.feign.ProductResponseDto;
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


    //상품 주문조회
    @GetMapping("/my")
    public ResponseMessage getOrderByOrderNumber(@RequestParam("name") String name, @RequestParam("phone_number") String phoneNumber, @RequestParam("order_number") String orderNumber) {
        int statusCode=StatusCode.SERVICE_UNAVAILABLE;
        String statusMessage=StatusMessage.NOT_AVAILABLE_SERVICE;
        ProductOrderResponseDto productOrderResponseDto=null;
  
        Customer customer=orderService.getCustomer(name, phoneNumber);

        if(customer==null){

            statusCode=StatusCode.NO_CONTENT;
            statusMessage=StatusMessage.NOT_FOUND_CUSTOMER;

        }else{

            ProductOrder productOrder= orderService.getOrder(customer, orderNumber);
            productOrderResponseDto=new ProductOrderResponseDto(productOrder,name);
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

        //주문한 code로 product 상세 조회
        ProductResponseDto productResponseDto=orderService.getProductDetail(orderRequestDto);



        if(productResponseDto.getStatus()==200){
            
            logger.info("data : " + productResponseDto.getData() );


            if(orderService.calculatePrice(orderRequestDto, productResponseDto)){

                //결제 실행
                //주문, 주문자 정보 저장
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
                    logger.info("month_price : " +orderRequestDto.getMonthPrice() );
                    statusCode=StatusCode.SERVICE_UNAVAILABLE;
                    statusMessage=StatusMessage.FAIL_PRODUCT_ORDER;
                }


            }else{
                //DB 가격 정보 상이. 결제 실패
                //주문 정보가 다릅니다. 주문을 다시 진행해주세요.
                logger.info("month_price : " +orderRequestDto.getMonthPrice()  );
                statusCode=StatusCode.OK;
                statusMessage=StatusMessage.NOT_MATCH_PRODUCT_ORDER_PRICE;
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
