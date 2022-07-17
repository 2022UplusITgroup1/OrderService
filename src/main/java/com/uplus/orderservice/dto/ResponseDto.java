package com.uplus.orderservice.dto;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class ResponseDto {

    private Integer status;
    private String message;
    private Map<String,Object> data;
    

    public ResponseDto(Integer status,
                            String message,
                            Map<String,Object> data) {
        
        this.status=status;
        this.message=message;
        this.data=data;

    }

}
