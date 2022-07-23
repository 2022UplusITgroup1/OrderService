package com.uplus.orderservice.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class PhoneDto {
    @JsonProperty("storage")
    private StorageDto storage;

    @JsonProperty("brand")
    private BrandDto brand;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("imgThumbnail")
    private String imgThumbnail;

    @JsonProperty("networkSupport")
    private String networkSupport;

    @JsonProperty("discountType")
    private Integer discountType;

    @JsonProperty("color")
    private String color;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("sales")
    private Integer sales;

    @JsonProperty("createTime")
    private LocalDateTime createTime;
}
