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
import com.uplus.orderservice.dto.CustomerDto;
import com.uplus.orderservice.dto.ProductOrderDto;
import com.uplus.orderservice.dto.feign.PhoneDto;
import com.uplus.orderservice.dto.feign.ProductDto;
import com.uplus.orderservice.dto.request.OrderRequestDto;
import com.uplus.orderservice.dto.response.ResponseMessage;
import com.uplus.orderservice.entity.Customer;
import com.uplus.orderservice.entity.ProductOrder;
import com.uplus.orderservice.repository.CustomerRepository;
import com.uplus.orderservice.repository.ProductOrderRepository;
import com.uplus.orderservice.service.data.DiscountType;

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
    

    public boolean isAvailableResponse(ResponseMessage responseDto){


        if(responseDto.getStatus()==200){
            return true;
        }

        return false;
    }


    public static String getCurrentDateTime() {
		Date today = new Date();
		Locale currentLocale = new Locale("KOREAN", "KOREA");
		String pattern = "yyyyMMddHHmmss"; //hhmmss로 시간,분,초만 뽑기도 가능
		SimpleDateFormat formatter = new SimpleDateFormat(pattern,
				currentLocale);
		return formatter.format(today);
	}

    public static int calcInstallmentFee(int phonePrice, Double rate) {
        rate = rate * 0.01;
      
        int r_money = (int) (Math.floor(Math.floor((Double)(phonePrice * rate)) / 12)/10)*10;
      
        return r_money; //할부이자

    }

    public boolean isValidPrice (OrderRequestDto orderRequestDto, ResponseMessage<ProductDto> productResponseDto) {

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
        int responseMonthPrice;
        int monthInstallmentFee=0;
        int totalInstallmentFee=0;
        int monthPhonePrice=(int) Math.floor((phonePrice/payPeriod)/10)*10;
        
        double rate=5.9;
        if(payPeriod>=12){
            monthInstallmentFee=calcInstallmentFee(phonePrice,rate);
            totalInstallmentFee=monthInstallmentFee*(payPeriod/2);
        }
        responseMonthPrice=monthPhonePrice+monthInstallmentFee;

        

        logger.info("calculatePrice : " +responseMonthPrice );

        // return responseMonthPrice;
        if(requestMonthPrice==responseMonthPrice)
            return true;
        else
            return false;

    }

    public CustomerDto getCustomer (String name, String phoneNumber) {
        
        //=null 안하면 null값 할당 안됨.
        CustomerDto customerDto;

        try{
            Customer customer = customerRepository.findByNameAndPhoneNumber(name, phoneNumber);
            customerDto = new CustomerDto(customer);

        }catch(Exception e){
            customerDto = null;
        }

 
        return customerDto;
    }

    public ProductOrderDto getOrder (CustomerDto customerDto,String orderNumber) {

        
        ProductOrderDto productOrderDto;

        if(customerDto==null){
            productOrderDto=null;
        }else{
            Long customerId = customerDto.getId();

            try{
                ProductOrder productOrder = productOrderRepository.findByCustomerIdAndOrderNumber(customerId,orderNumber);
                productOrderDto=new ProductOrderDto(productOrder,customerDto.getName());

            }catch(Exception e){

                productOrderDto=null;
            }
        }

        
        
        
        return productOrderDto;
    }



    //Feign Product Service API 통신
    //단말 정보 가져오기
    public ResponseMessage<ProductDto> getProductDetail (String planCode, String phoneCode, String phoneColor, int discountType) {

        ResponseMessage<ProductDto> productResponseDto;

        logger.info("planCode : " + planCode +
                    " phoneCode : " + phoneCode + " phoneColor : " + phoneColor+" discountType : "+discountType);
        try{
            productResponseDto=productServiceClient.getProductDetail(planCode, phoneCode, phoneColor, discountType);

        }catch(Exception e){
            // e.printStackTrace();
            productResponseDto=null;
        }
        

        return productResponseDto;
    }



    //주문정보저장
    @Transactional
    public ProductOrderDto saveCustomerProductOrder(OrderRequestDto orderRequestDto) {
        //비회원 주문이므로 (주문자 정보 insert , 주문 정보 insert) Transaction
        //insert Customer
        String name=orderRequestDto.getName();
        String email=orderRequestDto.getEmail();
        String address=orderRequestDto.getAddress();
        String phoneNumber=orderRequestDto.getPhoneNumber();

        CustomerDto customerDto=new CustomerDto(name,email,address,phoneNumber);
        
        Long customerId=customerRepository.save(customerDto.toEntity()).getId();
        
        //insert ProductOrder
        String phoneCode=orderRequestDto.getPhoneDto().getCode();
        String phoneColor=orderRequestDto.getPhoneDto().getColor();
        String planCode=orderRequestDto.getPlanDto().getCode();
        int discountType=orderRequestDto.getDiscountType();
        int payPeriod=orderRequestDto.getPayPeriod();
        int monthPrice=orderRequestDto.getMonthPrice();

        //주문 번호 난수 생성 필요
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        int randNum=(int)(rand.nextDouble()*1000000);
        String randNumString = String.format("%06d", randNum);

        String currentDateTime=getCurrentDateTime();
        String orderNumber=currentDateTime+randNumString;

        ProductOrder productOrderEntity=new ProductOrder(customerId,phoneCode,phoneColor,planCode, discountType, payPeriod, orderNumber,monthPrice);

        ProductOrderDto productOrderDto=new ProductOrderDto(productOrderRepository.save(productOrderEntity));

        return productOrderDto;
    }


    //판매량 증가 
    //Feign API 호출
    public ResponseMessage<PhoneDto> updateProductSales(ProductOrderDto productOrderDto){
        String phoneCode=productOrderDto.getPhoneCode();
        String phoneColor=productOrderDto.getColor();

        ResponseMessage<PhoneDto> updatePhoneSalesDto=productServiceClient.updateSales(phoneCode, phoneColor);

        return updatePhoneSalesDto;
    }


}
