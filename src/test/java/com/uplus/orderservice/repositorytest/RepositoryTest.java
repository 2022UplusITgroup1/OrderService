package com.uplus.orderservice.repositorytest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.netflix.discovery.converters.Auto;
import com.uplus.orderservice.entity.Customer;
import com.uplus.orderservice.entity.ProductOrder;
import com.uplus.orderservice.repository.CustomerRepository;
import com.uplus.orderservice.repository.ProductOrderRepository;

@SpringBootTest
public class RepositoryTest {
    // Customer findByNameAndPhoneNumber (String name, String phoneNumber);
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @DisplayName("고유한 orderNumber 반환 테스트")
    @Test
    public void findOrderNumberTest(){
        //given : 테스트에 필요한 데이터를 세팅
        Set<String> checkSet=new HashSet<>();


        //when : 테스트가 필요한 코드 실행
        // Customer c=customerRepository.findByNameAndPhoneNumber(name, phoneNumber);
        List<ProductOrder> productOrderList=productOrderRepository.findAll();
        int productOrderNums= productOrderList.size();
        for(int i=0;i<productOrderNums;i++){
            ProductOrder productOrder=productOrderList.get(i);
            String orderNumber=productOrder.getOrderNumber();

            checkSet.add(orderNumber);

        }



        //then : 결과를 대입하여 success, fail 확인
        System.out.println("productOrderNums : "+productOrderNums+" checkSet.size() : "+checkSet.size());
        // assert equal 
        // success 하면 orderNumber는 distinct
        assertEquals(productOrderNums, checkSet.size(), "productOrderNums : "+productOrderNums+" checkSet.size() : "+checkSet.size());
        

    }




}
