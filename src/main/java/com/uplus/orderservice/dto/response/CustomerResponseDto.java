package com.uplus.orderservice.dto.response;

import com.uplus.orderservice.entity.Customer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomerResponseDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    public CustomerResponseDto(Customer entity) {
        this.id=entity.getId();
        this.name=entity.getName();
        this.email=entity.getEmail();
        this.address=entity.getAddress();
        this.phoneNumber=entity.getPhoneNumber();
    }
}
