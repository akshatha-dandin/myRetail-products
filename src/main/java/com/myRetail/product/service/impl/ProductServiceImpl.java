package com.myRetail.product.service.impl;

import com.myRetail.product.client.ProductClient;
import com.myRetail.product.domain.Price;
import com.myRetail.product.domain.Product;
import com.myRetail.product.dto.ProductClientResponse;
import com.myRetail.product.repository.ProductRepository;
import com.myRetail.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductClient productClient;

    @Override
    public Product getProduct(BigInteger productId) {
        ProductClientResponse productDetails = productClient.getProductDetails(productId.toString());
        Price price = productRepository.getPrice(productId);
        log.info("op=get_product_details, status=OK, " +
                "desc=Name and price retrieved successfully for productId={}", productId);
        Product product = Product.builder().id(productId)
                .name(productDetails.getData().getProduct().getItem().getProduct_description().getTitle())
                .price(Price.builder().value(price.getValue()).currencyCode(price.getCurrencyCode()).build())
                .build();
        return product;
    }

    @Override
    public void upsertPrice(BigInteger productNumber, Price price) {
        productRepository.upsert(productNumber, price);
    }

}
