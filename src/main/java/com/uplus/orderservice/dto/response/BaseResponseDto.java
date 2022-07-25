package com.uplus.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class BaseResponseDto<T> {

    @JsonProperty("status")
    private int status;

    @JsonProperty("message")
    private String message;

    
    // public <T> ResponseDto (int status, final String message, T data) {
    //     this.status=status;
    //     this.message=message;
    //     this.data=(T) data;

    // }

}
