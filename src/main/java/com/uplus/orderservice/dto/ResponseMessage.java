package com.uplus.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

////////////////////////////////////
// Create Date: 2022.07.14        //
// Create By: MYSEO              //
///////////////////////////////////
@Data
@AllArgsConstructor
@Builder
public class ResponseMessage<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ResponseMessage<T> res(final int status, final String message) {
        return res(status, message, null);
    }

    public static <T> ResponseMessage<T> res(final int status, final String message, final T t) {
        return ResponseMessage.<T>builder()
                .status(status)
                .message(message)
                .data(t)
                .build();
    }
}
