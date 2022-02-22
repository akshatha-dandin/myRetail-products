package com.myRetail.product.service.impl;

import com.myRetail.product.domain.Price;
import com.myRetail.product.domain.Product;
import com.myRetail.product.repository.ProductRepository;
import com.myRetail.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product getProduct(BigInteger productId) {
        Price price = productRepository.getPrice(productId);
        Product product = Product.builder().id(productId)
                .name("The Big Lebowski (Blu-ray) (Widescreen)")
                .price(Price.builder().value(price.getValue()).currencyCode(price.getCurrencyCode()).build())
                .build();
        return product;
    }

    @Override
    public void upsertPrice(BigInteger productNumber, Price price) {
        productRepository.upsert(productNumber, price);
    }

}
