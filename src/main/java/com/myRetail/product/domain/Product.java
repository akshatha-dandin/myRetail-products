package com.myRetail.product.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class Product {

    BigInteger id;
    String name;
    Price price;

}
