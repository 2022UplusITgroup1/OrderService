package com.uplus.orderservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uplus.orderservice.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Customer findByNameAndPhoneNumber (String name, String phoneNumber);
}
