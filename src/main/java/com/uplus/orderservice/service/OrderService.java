package com.uplus.orderservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uplus.orderservice.feginclient.ProductServiceClient;
import com.uplus.orderservice.feginclient.SearchCorrectApiClient;
import com.uplus.orderservice.dto.CustomerRequestDto;
import com.uplus.orderservice.dto.CustomerResponseDto;
import com.uplus.orderservice.dto.ProductOrderResponseDto;
import com.uplus.orderservice.dto.ProductResponseDto;
import com.uplus.orderservice.dto.SearchCorrectResponseDto;
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


    private final SearchCorrectApiClient searchCorrectApiClient;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    // private final RestTemplate restTemplate;


    public SearchCorrectResponseDto getCorrectWord(String query){
        SearchCorrectResponseDto map =searchCorrectApiClient.getCorrectString(query);


        return map;
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

    public CustomerResponseDto findCustomerByNameAndPhoneNumber (String name, String phoneNumber) {
        
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

    public ProductOrderResponseDto findOrderByCustomer (CustomerResponseDto customerResponseDto,String orderNumber) {
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
        // ProductOrder entity = productOrderRepository.findByOrderNumber(orderNumber);
        ProductOrderResponseDto productOrderResponseDto=new ProductOrderResponseDto(entity);
 
        return productOrderResponseDto;
    }


    //Product Service API 통신
    //단말 정보 가져오기
    public ProductResponseDto getProductDetail (String planCode, String phoneCode, String phoneColor, Integer discountType) {

        // String url=String.format("http://", args);
        // try{

        // logger.info("planCode : " + planCode +
        //             " phoneCode : " + phoneCode + " phoneColor : " + phoneColor + " discountType " + discountType);
        // }
        ProductResponseDto productResponseJson=productServiceClient.getProductDetail(planCode, phoneCode, phoneColor, discountType);

        System.out.println(productResponseJson);

        Integer monthPrice;

        // ProductResponseDto productResponseDto=productResponseJson.get("data")



        return productResponseJson;
    }





    // public List<OrderResponseDto> getAllOrderList (String name, String phoneNumber, String orderNumber) {
    //     List<Order> orderList=new ArrayList<>();
    //     List<OrderResponseDto> orderResponseDtoList=new ArrayList<>();

    //     orderList = orderRepository.findByNameAndPhoneNumberAndOrderNumber(name, phoneNumber, orderNumber);

    //     if(orderList.size()!=0){
    //         for(Order entity : orderList){
    //             OrderResponseDto orderResponseDto=new OrderResponseDto(entity);
    //             orderResponseDtoList.add(orderResponseDto);
    //         }
    //     }
        
        

    //     return orderResponseDtoList;
    // }


    

}
