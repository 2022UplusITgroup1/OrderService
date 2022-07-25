package com.uplus.orderservice.dto;

import com.uplus.orderservice.entity.Customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomerDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;

    public CustomerDto(Customer entity) {
        this.id=entity.getId();
        this.name=entity.getName();
        this.email=entity.getEmail();
        this.address=entity.getAddress();
        this.phoneNumber=entity.getPhoneNumber();
    }

    @Builder
    public CustomerDto(String name, String email, String address, String phoneNumber) {
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
