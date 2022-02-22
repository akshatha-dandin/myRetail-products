package com.myRetail.product.repository.impl;

import com.myRetail.product.domain.Price;
import com.myRetail.product.exceptions.BusinessException;
import com.myRetail.product.model.PriceTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryImplTest {

    @Mock
    CassandraOperations cassandraTemplate;

    @InjectMocks
    ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productRepository, "errorCode", "API_ERR_NOT_FOUND");
        ReflectionTestUtils.setField(productRepository, "errorMessage",
                "Sorry, requested product is not found");
    }

    @Test
    public void shouldBeAbleToRetrievePrice() {
        PriceTable priceTable = PriceTable.builder().productId("13860428").price("13.45").currencyCode("USD").build();
        when(cassandraTemplate.selectOne(any(Query.class), eq(PriceTable.class))).thenReturn(priceTable);

        Price expectedPrice = Price.builder().value(new BigDecimal("13.45")).currencyCode("USD").build();
        Price price = productRepository.getPrice(new BigInteger("13860429"));
        assertEquals(expectedPrice, price);
    }

    @Test
    public void shouldReturnNotFoundIfRequestedProductIsNotFound() {
        when(cassandraTemplate.selectOne(any(Query.class), eq(PriceTable.class))).thenReturn(null);
        try {
            productRepository.getPrice(new BigInteger("13860429"));
        } catch (BusinessException ex) {
            assertEquals("API_ERR_NOT_FOUND", ex.getResponseStatusCode());
            assertEquals("Sorry, requested product is not found", ex.getErrorMessage());
            assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        }
    }

    @Test
    public void shouldBeAbleToUpsertPrice() {
        PriceTable priceTable = PriceTable.builder().productId("13860428").price("13.45").currencyCode("USD").build();
        Price price = Price.builder().value(new BigDecimal("13.45")).currencyCode("USD").build();

        productRepository.upsert(new BigInteger("13860428"), price);
        verify(cassandraTemplate).insert(priceTable);
    }

}