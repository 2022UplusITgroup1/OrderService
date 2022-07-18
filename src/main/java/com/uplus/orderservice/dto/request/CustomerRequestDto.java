package com.uplus.orderservice.dto.request;

import com.uplus.orderservice.entity.Customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CustomerRequestDto {

    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    @Builder
    public CustomerRequestDto(String name, String email, String address, String phoneNumber) {
        this.name=name;
        this.email=email;
        this.address=address;
        this.phoneNumber=phoneNumber;
    }

    public Customer toEntity() {
        return Customer.builder()
               .name(name)
               .email(email)
               .address(address)
               .phoneNumber(phoneNumber)
               .build();
    }
    
}
