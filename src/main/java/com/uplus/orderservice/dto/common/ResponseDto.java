package com.uplus.orderservice.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class ResponseDto {

    @JsonProperty("status")
    private int status;

    @JsonProperty("message")
    private String message;

    

}
