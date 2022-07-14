package com.uplus.orderservice.domain.productOrder;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long>{

    ProductOrder findByCustomerId(Long customerId);

    ProductOrder findByOrderNumber(String orderNumber);

    ProductOrder findByCustomerIdAndOrderNumber(Long CustomerId,String orderNumber);
}

