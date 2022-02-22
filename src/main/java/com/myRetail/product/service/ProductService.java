package com.myRetail.product.service;

import com.myRetail.product.domain.Price;
import com.myRetail.product.domain.Product;

import java.math.BigInteger;

public interface ProductService {

    Product getProduct(BigInteger productId);

    void upsertPrice(BigInteger productNumber, Price price);
}
