package com.myRetail.product.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Price {

    BigDecimal value;
    String currencyCode;
}
