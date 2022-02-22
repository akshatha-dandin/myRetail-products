package com.myRetail.product.repository;

import com.myRetail.product.domain.Price;

import java.math.BigInteger;

public interface ProductRepository {

    Price getPrice(BigInteger productId);

    void upsert(BigInteger productNumber, Price price);
}
