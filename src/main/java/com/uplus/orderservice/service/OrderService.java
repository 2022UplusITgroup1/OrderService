package com.uplus.orderservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uplus.orderservice.domain.customer.Customer;
import com.uplus.orderservice.domain.customer.CustomerRepository;
import com.uplus.orderservice.dto.CustomerRequestDto;
import com.uplus.orderservice.dto.CustomerResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final CustomerRepository customerRepository;

    @Transactional
    public Long save(CustomerRequestDto requestDto) {
        
        return customerRepository.save(requestDto.toEntity()).getId();
    }

    public CustomerResponseDto findById (Long id) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문자가 없습니다. id="+ id));
 
        return new CustomerResponseDto(entity);
    }
}
