package com.myRetail.product.dto.mapper;

import com.myRetail.product.domain.Price;
import com.myRetail.product.domain.Product;
import com.myRetail.product.dto.PriceDto;
import com.myRetail.product.dto.ProductResponseDto;

public class ProductResponseMapper {
    public static ProductResponseDto map(Product product) {
        Price price = product.getPrice();
        return ProductResponseDto.builder().id(product.getId()).name(product.getName())
                .current_price(PriceDto.builder().value(price.getValue())
                        .currency_code(price.getCurrencyCode()).build()).build();
    }
}
