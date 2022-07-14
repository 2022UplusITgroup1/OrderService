package com.uplus.orderservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uplus.orderservice.entity.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long>{

    ProductOrder findByCustomerId(Long customerId);

    ProductOrder findByOrderNumber(String orderNumber);

    ProductOrder findByCustomerIdAndOrderNumber(Long CustomerId,String orderNumber);
}

