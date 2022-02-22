package com.myRetail.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PriceDto {

    BigDecimal value;
    String currency_code;
}
