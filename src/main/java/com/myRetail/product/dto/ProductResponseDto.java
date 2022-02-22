package com.myRetail.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;

@Getter
@Builder
public class ProductResponseDto {

    BigInteger id;
    String name;
    PriceDto current_price;

}
