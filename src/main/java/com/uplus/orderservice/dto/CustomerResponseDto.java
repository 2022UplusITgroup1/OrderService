package com.uplus.orderservice.dto;

import com.uplus.orderservice.domain.customer.Customer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomerResponseDto {
    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    public CustomerResponseDto(Customer entity) {
        this.name=entity.getName();
        this.email=entity.getEmail();
        this.address=entity.getAddress();
        this.phoneNumber=entity.getPhoneNumber();
    }
}
