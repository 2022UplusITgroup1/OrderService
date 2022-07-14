package com.uplus.orderservice.domain.customer;


import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Customer findByNameAndPhoneNumber (String name, String phoneNumber);
}
