package com.uplus.OrderService.controller;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.OrderService.dto.CustomerRequestDto;
import com.uplus.OrderService.dto.CustomerResponseDto;
import com.uplus.OrderService.service.OrderService;

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
