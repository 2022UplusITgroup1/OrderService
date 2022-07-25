package com.uplus.orderservice.dto.feign;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StorageDto {
    @JsonProperty("capability")
    private Integer capability;
}
