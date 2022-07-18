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
import com.uplus.orderservice.dto.ResponseDto;
import com.uplus.orderservice.dto.ResponseMessage;
import com.uplus.orderservice.dto.request.CustomerRequestDto;
import com.uplus.orderservice.dto.request.ProductOrderRequestDto;
import com.uplus.orderservice.dto.response.CustomerResponseDto;
import com.uplus.orderservice.dto.response.ProductOrderResponseDto;
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
    // private final RestTemplate restTemplate;
    
    public static String getCurrentDateTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초만 뽑기도 가능
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,
				currentLocale);
		return formatter.format(today);
	}

    @Transactional
    public Long save(CustomerRequestDto requestDto) {
        
        return customerRepository.save(requestDto.toEntity()).getId();
    }

    public CustomerResponseDto findById (Long id) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문자가 없습니다. id="+ id));
 
        return new CustomerResponseDto(entity);
    }

    public CustomerResponseDto getCustomer (String name, String phoneNumber) {
        
        //=null 안하면 null값 할당 안됨.
        CustomerResponseDto customerResponseDto;

        try{
            Customer customerEntity = customerRepository.findByNameAndPhoneNumber(name, phoneNumber);
            customerResponseDto = new CustomerResponseDto(customerEntity);

        }catch(Exception e){
            customerResponseDto = null;
        }

 
        return customerResponseDto;
    }


    public ProductOrderResponseDto findOrderById (Long id) {
        // Long customerId = customerResponseDto.getId();
        // Order entity = orderRepository.findByCustomerId(customerId);
        ProductOrder entity = productOrderRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 주문자가 없습니다. id="+ id));

        ProductOrderResponseDto orderResponseDto=new ProductOrderResponseDto(entity);
 
        return orderResponseDto;
    }

    public ProductOrderResponseDto findOrderListByCustomer ( String name, String orderNumber) {
        // Long customerId = customerResponseDto.getId();
        // ProductOrder entity = productOrderRepository.findByCustomerId(customerId);
        ProductOrder entity = productOrderRepository.findByOrderNumber(orderNumber);
        ProductOrderResponseDto productOorderResponseDto=new ProductOrderResponseDto(entity);
        productOorderResponseDto.setName(name);
 
        return productOorderResponseDto;
    }


    public ProductOrderResponseDto getOrder (CustomerResponseDto customerResponseDto,String orderNumber) {
        ProductOrderResponseDto productOorderResponseDto;

        Long customerId = customerResponseDto.getId();

        try{

            ProductOrder productOrderEntity = productOrderRepository.findByCustomerIdAndOrderNumber(customerId,orderNumber);
            productOorderResponseDto=new ProductOrderResponseDto(productOrderEntity);
            productOorderResponseDto.setName(customerResponseDto.getName());

        }catch(Exception e){
            productOorderResponseDto=null;
        }
        

        
 
        return productOorderResponseDto;
    }

    public ProductOrderResponseDto findOrderByOrderNumber (String orderNumber) {
        ProductOrder entity = productOrderRepository.findByOrderNumber(orderNumber);

        ProductOrderResponseDto productOrderResponseDto=new ProductOrderResponseDto(entity);
 
        return productOrderResponseDto;
    }


    //Feign Product Service API 통신
    //단말 정보 가져오기
    public ResponseDto getProductDetail (String planCode, String phoneCode, String phoneColor, Integer discountType) {

        ResponseDto productResponseDto=productServiceClient.getProductDetail(planCode, phoneCode, phoneColor, discountType);


        return productResponseDto;
    }

    public int calculatePrice (int phonePrice, int planPrice, int payPeriod, int discountType) {


        if(discountType==DiscountType.PHONE_SUPPORT_FUND){
            phonePrice=(phonePrice-(int)Math.ceil(phonePrice*DiscountType.PHONE_SUPPORT_FUND_RATE/100));
        }else if(discountType==DiscountType.PLAN_SELECTIVE_AGREEMENT_12){
            planPrice=(planPrice-(int)Math.ceil(planPrice*DiscountType.PLAN_SELECTIVE_AGREEMENT_12_RATE/100));
        }else if(discountType==DiscountType.PLAN_SELECTIVE_AGREEMENT_24){
            planPrice=(planPrice-(int)Math.ceil(planPrice*DiscountType.PLAN_SELECTIVE_AGREEMENT_24_RATE/100));
        }

        int monthPrice=(phonePrice/payPeriod)+planPrice;


        return monthPrice;
    }


    @Transactional
    public Long saveCustomer(CustomerRequestDto requestDto) {
        
        return customerRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long saveCustomer(ProductOrderRequestDto productOrderRequestDto) {

        String name=productOrderRequestDto.getName();
        String email=productOrderRequestDto.getEmail();
        String address=productOrderRequestDto.getAddress();
        String phoneNumber=productOrderRequestDto.getPhoneNumber();

        CustomerRequestDto customerRequestDto=new CustomerRequestDto(name,email,address,phoneNumber);
        
        return customerRepository.save(customerRequestDto.toEntity()).getId();
    }

    @Transactional
    public Long saveProductOrder(ProductOrderRequestDto productOrderRequestDto, Long customerId) {

        String phoneCode=productOrderRequestDto.getPhoneCode();
        String phoneColor=productOrderRequestDto.getPhoneColor();
        String planCode=productOrderRequestDto.getPlanCode();
        int monthPrice=productOrderRequestDto.getMonthPrice();


        String orderNumber="1234";

        ProductOrder productOrderEntity=new ProductOrder(customerId,phoneCode,phoneColor,planCode,orderNumber,monthPrice);

        return productOrderRepository.save(productOrderEntity).getId();
    }

    @Transactional
    public Long saveCustomerProductOrder(ProductOrderRequestDto productOrderRequestDto) {
        //비회원 주문이므로 (주문자 정보 insert , 주문 정보 insert) Transaction
        //insert Customer
        String name=productOrderRequestDto.getName();
        String email=productOrderRequestDto.getEmail();
        String address=productOrderRequestDto.getAddress();
        String phoneNumber=productOrderRequestDto.getPhoneNumber();

        CustomerRequestDto customerRequestDto=new CustomerRequestDto(name,email,address,phoneNumber);
        
        Long customerId=customerRepository.save(customerRequestDto.toEntity()).getId();
        
        //insert ProductOrder
        String phoneCode=productOrderRequestDto.getPhoneCode();
        String phoneColor=productOrderRequestDto.getPhoneColor();
        String planCode=productOrderRequestDto.getPlanCode();
        int monthPrice=productOrderRequestDto.getMonthPrice();

        //주문 번호 난수 생성 필요
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int randNum=(int)(rand.nextDouble()*1000000);

        String currentDateTime=getCurrentDateTime();
        String orderNumber=currentDateTime+randNum;

        ProductOrder productOrderEntity=new ProductOrder(customerId,phoneCode,phoneColor,planCode,orderNumber,monthPrice);

        return productOrderRepository.save(productOrderEntity).getId();
    }



}
