package com.myRetail.product.service.impl;

import com.myRetail.product.client.ProductClient;
import com.myRetail.product.domain.Price;
import com.myRetail.product.domain.Product;
import com.myRetail.product.dto.*;
import com.myRetail.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductClient productClient;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    public void shouldBeAbleToRetrieveProductDetails() {
        BigInteger productId = new BigInteger("13860429");
        Price price = Price.builder()
                .value(new BigDecimal("13.45")).currencyCode("USD").build();
        when(productRepository.getPrice(productId)).thenReturn(price);
        ProductClientResponse productClientResponse = ProductClientResponse.builder().build();
        productClientResponse.setData(ProductData.builder().product(ProductDetails.builder()
                .item(ItemDetails.builder().product_description(ProductDescription.builder()
                        .title("The Big Lebowski (Blu-ray) (Widescreen)").build()).build()).build()).build());
        when(productClient.getProductDetails("13860429")).thenReturn(productClientResponse);

        Product product = productService.getProduct(productId);
        Product expectedProduct = Product.builder().price(price).id(productId)
                .name("The Big Lebowski (Blu-ray) (Widescreen)").build();

        assertEquals(expectedProduct, product);
    }

    @Test
    public void shouldBeAbleToUpsertPrice(){
        BigInteger productId = new BigInteger("13860429");
        Price price = Price.builder()
                .value(new BigDecimal("13.45")).currencyCode("USD").build();
        productService.upsertPrice(productId, price);

        verify(productRepository).upsert(productId, price);
    }

}