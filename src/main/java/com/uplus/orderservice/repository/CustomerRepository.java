package com.uplus.orderservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.uplus.orderservice.entity.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Customer findByNameAndPhoneNumber (String name, String phoneNumber);
}
