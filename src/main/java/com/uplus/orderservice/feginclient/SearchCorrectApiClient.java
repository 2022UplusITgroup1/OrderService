package com.uplus.orderservice.feginclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.uplus.orderservice.dto.SearchCorrectResponseDto;

import feign.Headers;


@FeignClient(name="searchcorrect", url = "https://openapi.naver.com/v1/search/errata.json", configuration = FeignClientConfiguration.class)
public interface SearchCorrectApiClient {

    @GetMapping
    SearchCorrectResponseDto getCorrectString(@RequestParam(value = "query") String query);
}
