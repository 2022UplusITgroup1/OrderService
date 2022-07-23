package com.uplus.orderservice.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StorageDto {
    @JsonProperty("capability")
    private Integer capability;
}
