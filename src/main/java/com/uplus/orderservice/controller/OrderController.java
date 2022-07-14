package com.uplus.orderservice.controller;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.orderservice.dto.CustomerRequestDto;
import com.uplus.orderservice.dto.CustomerResponseDto;
import com.uplus.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public Long save(@RequestBody CustomerRequestDto requestDto) {

        return orderService.save(requestDto);
    }


    @GetMapping("/order/{id}")
    public CustomerResponseDto findById (@PathVariable Long id) {

        return orderService.findById(id);
    }

}
