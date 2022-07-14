package com.uplus.orderservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uplus.orderservice.domain.customer.Customer;
import com.uplus.orderservice.domain.customer.CustomerRepository;
import com.uplus.orderservice.domain.productOrder.ProductOrder;
import com.uplus.orderservice.domain.productOrder.ProductOrderRepository;
import com.uplus.orderservice.dto.CustomerRequestDto;
import com.uplus.orderservice.dto.CustomerResponseDto;
import com.uplus.orderservice.dto.ProductOrderResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final CustomerRepository customerRepository;
    private final ProductOrderRepository productOrderRepository;

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
        Customer entity = customerRepository.findByNameAndPhoneNumber(name, phoneNumber);
        // Long customerId=customerEntity.getId();
        // Order orderEntity= orderRepository.findByCustomerId(customerId);

        // OrderResponseDto = new 
 
        return new CustomerResponseDto(entity);
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

            ProductOrder entity = productOrderRepository.findByCustomerIdAndOrderNumber(customerId,orderNumber);
            productOorderResponseDto=new ProductOrderResponseDto(entity);

        }catch(Exception e){
            productOorderResponseDto=null;
        }
        

        
 
        return productOorderResponseDto;
    }

    public ProductOrderResponseDto findOrderByOrderNumber (String orderNumber) {
        ProductOrder entity = productOrderRepository.findByOrderNumber(orderNumber);
        // ProductOrder entity = productOrderRepository.findByOrderNumber(orderNumber);
        ProductOrderResponseDto productOorderResponseDto=new ProductOrderResponseDto(entity);
 
        return productOorderResponseDto;
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
