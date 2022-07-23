package com.uplus.orderservice.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uplus.orderservice.feginclient.ProductServiceClient;
import com.uplus.orderservice.dto.DiscountType;
import com.uplus.orderservice.dto.ResponseMessage;
import com.uplus.orderservice.dto.StatusCode;
import com.uplus.orderservice.dto.StatusMessage;
import com.uplus.orderservice.dto.common.ResponseDto;
import com.uplus.orderservice.dto.request.CustomerRequestDto;
import com.uplus.orderservice.dto.request.OrderRequestDto;
import com.uplus.orderservice.dto.request.ProductOrderRequestDto;
import com.uplus.orderservice.dto.response.CustomerResponseDto;
import com.uplus.orderservice.dto.response.ProductOrderResponseDto;
import com.uplus.orderservice.dto.response.feign.ProductResponseDto;
import com.uplus.orderservice.entity.Customer;
import com.uplus.orderservice.entity.ProductOrder;
import com.uplus.orderservice.repository.CustomerRepository;
import com.uplus.orderservice.repository.ProductOrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final CustomerRepository customerRepository;
    private final ProductOrderRepository productOrderRepository;
    private final ProductServiceClient productServiceClient;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public static String getCurrentDateTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초만 뽑기도 가능
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,
				currentLocale);
		return formatter.format(today);
	}

    public boolean calculatePrice (OrderRequestDto orderRequestDto, ProductResponseDto productResponseDto) {

        //productResponseDto 의
        //단말 가격/할부기간
        // +
        //요금제 가격
        //==month_price 이면 결제.

        int phonePrice = productResponseDto.getData().getPhone().getPrice();
        int planPrice = productResponseDto.getData().getPlan().getPrice();
        int payPeriod = orderRequestDto.getPayPeriod();
        int discountType = orderRequestDto.getDiscountType();

        int requestMonthPrice=orderRequestDto.getMonthPrice();


        if(discountType==DiscountType.PHONE_SUPPORT_FUND){
            phonePrice=(phonePrice-(int) (Math.floor((phonePrice*DiscountType.PHONE_SUPPORT_FUND_RATE/100)/10))*10);
        }else if(discountType==DiscountType.PLAN_SELECTIVE_AGREEMENT_12){
            planPrice=(planPrice-(int) (Math.floor((planPrice*DiscountType.PLAN_SELECTIVE_AGREEMENT_12_RATE/100)/10))*10);
        }else if(discountType==DiscountType.PLAN_SELECTIVE_AGREEMENT_24){
            planPrice=(planPrice-(int) (Math.floor((planPrice*DiscountType.PLAN_SELECTIVE_AGREEMENT_24_RATE/100)/10))*10);
        }

        int responseMonthPrice=(int) Math.floor((phonePrice/payPeriod)/10)*10+planPrice;

        logger.info("calculatePrice : " +responseMonthPrice );

        if(requestMonthPrice==responseMonthPrice)
            return true;
        else
            return false;

    }

    public Customer getCustomer (String name, String phoneNumber) {
        
        //=null 안하면 null값 할당 안됨.
        Customer customer;

        try{
            customer = customerRepository.findByNameAndPhoneNumber(name, phoneNumber);

        }catch(Exception e){
            customer = null;
        }

 
        return customer;
    }

    public ProductOrder getOrder (Customer customer,String orderNumber) {
        ProductOrder productOrder;

        Long customerId = customer.getId();

        try{
            productOrder = productOrderRepository.findByCustomerIdAndOrderNumber(customerId,orderNumber);

        }catch(Exception e){

            productOrder=null;
        }
        
        
        return productOrder;
    }


    public int setStatusCode(Object object){
        int statusCode;

        if(object==null){
            statusCode=StatusCode.NO_CONTENT;
        }else{
            statusCode=StatusCode.OK;
        }

        return statusCode;
    }

    public String setStatusMessage(Object object, String className){
        String statusMessage=StatusMessage.NOT_AVAILABLE_SERVICE;

        // Optional<String> test=Optional.empty();

        // String str=test.get();


        //customer
        if(className==Customer.class.getName()){
            if(object == null){
                //className 에 따라 statusMessage 자동바뀜..
                statusMessage=StatusMessage.NOT_FOUND_CUSTOMER;
    
            }else{
    
                statusMessage=StatusMessage.SUCCESS_READ_CUSTOMER;
    
            }
        }else if(className==ProductOrder.class.getName()){
            if(object == null){
                //className 에 따라 statusMessage 자동바뀜..
                statusMessage=StatusMessage.NOT_FOUND_PRODUCT_ORDER;
    
            }else{
    
                statusMessage=StatusMessage.SUCCESS_READ_PRODUCT_ORDER;
    
            }
        }

        return statusMessage;
    }


    //Feign Product Service API 통신
    //단말 정보 가져오기
    public ProductResponseDto getProductDetail (OrderRequestDto orderRequestDto) {
        String planCode=orderRequestDto.getPlanRequestDto().getCode();
        String phoneCode=orderRequestDto.getPhoneRequestDto().getCode();
        String phoneColor=orderRequestDto.getPhoneRequestDto().getColor();
        int discountType=orderRequestDto.getDiscountType();

        logger.info("planCode : " + planCode +
                    " phoneCode : " + phoneCode + " phoneColor : " + phoneColor+" discountType : "+discountType);

        ProductResponseDto productResponseDto=productServiceClient.getProductDetail(planCode, phoneCode, phoneColor, discountType);

        logger.info("productResponseDto : " + productResponseDto.getStatus()+" "+ productResponseDto.getData());

        return productResponseDto;
    }


    // @Transactional
    // public Long saveCustomer(CustomerRequestDto requestDto) {
        
    //     return customerRepository.save(requestDto.toEntity()).getId();
    // }

    @Transactional
    public Long saveCustomer(ProductOrderRequestDto productOrderRequestDto) {

        String name=productOrderRequestDto.getName();
        String email=productOrderRequestDto.getEmail();
        String address=productOrderRequestDto.getAddress();
        String phoneNumber=productOrderRequestDto.getPhoneNumber();

        CustomerRequestDto customerRequestDto=new CustomerRequestDto(name,email,address,phoneNumber);
        
        return customerRepository.save(customerRequestDto.toEntity()).getId();
    }

    //주문정보저장
    @Transactional
    public String saveCustomerProductOrder(OrderRequestDto orderRequestDto) {
        //비회원 주문이므로 (주문자 정보 insert , 주문 정보 insert) Transaction
        //insert Customer
        String name=orderRequestDto.getName();
        String email=orderRequestDto.getEmail();
        String address=orderRequestDto.getAddress();
        String phoneNumber=orderRequestDto.getPhoneNumber();

        CustomerRequestDto customerRequestDto=new CustomerRequestDto(name,email,address,phoneNumber);
        
        Long customerId=customerRepository.save(customerRequestDto.toEntity()).getId();
        
        //insert ProductOrder
        String phoneCode=orderRequestDto.getPhoneRequestDto().getCode();
        String phoneColor=orderRequestDto.getPhoneRequestDto().getColor();
        String planCode=orderRequestDto.getPlanRequestDto().getCode();
        int payPeriod=orderRequestDto.getPayPeriod();
        int monthPrice=orderRequestDto.getMonthPrice();

        //주문 번호 난수 생성 필요
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int randNum=(int)(rand.nextDouble()*1000000);

        String currentDateTime=getCurrentDateTime();
        String orderNumber=currentDateTime+randNum;

        ProductOrder productOrderEntity=new ProductOrder(customerId,phoneCode,phoneColor,planCode,payPeriod, orderNumber,monthPrice);

        Long productOrderId=productOrderRepository.save(productOrderEntity).getId();
        if(productOrderId==null){
            orderNumber=null;
        }

        return orderNumber;
    }

    //판매량 증가
    public ResponseDto updateProductSales(OrderRequestDto orderRequestDto){
        String phoneCode=orderRequestDto.getPhoneRequestDto().getCode();
        String phoneColor=orderRequestDto.getPhoneRequestDto().getColor();

        ResponseDto updateResultDto=productServiceClient.updateSales(phoneCode, phoneColor);

        return updateResultDto;
    }


}
