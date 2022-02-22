package com.myRetail.product.client;

import com.myRetail.product.dto.*;
import com.myRetail.product.exceptions.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductClientTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ProductClient productClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productClient, "getProductsUrl",
                "http://localhost:9080/api/v1/demo?tcin=");
        ReflectionTestUtils.setField(productClient, "headerName", "token");
    }

    @Test
    public void shouldBeAbleToRetrieveProductName() {
        ResponseEntity<ProductClientResponse> mockResponse = mock(ResponseEntity.class);
        doReturn(getMockProductApiResponse()).when(mockResponse).getBody();
        doReturn(mockResponse).when(restTemplate).exchange(any(String.class),
                eq(HttpMethod.GET), any(),
                any(ParameterizedTypeReference.class));
        ProductClientResponse productDetails = productClient.getProductDetails("13860428");

        assertEquals("some title", productDetails.getData().getProduct().getItem().getProduct_description().getTitle());

    }

    @Test
    public void shouldThrowApplicationErrorOnFailedRequest() {
        doThrow(HttpClientErrorException.class).when(restTemplate).exchange(any(String.class),
                eq(HttpMethod.GET), any(),
                any(ParameterizedTypeReference.class));
        try {
            productClient.getProductDetails("13860428");
            fail("Exception must be thrown");
        } catch (ApplicationException ex) {
            assertEquals("500 INTERNAL_SERVER_ERROR", ex.getResponseStatusCode());
            assertEquals("Error while retrieving product name", ex.getErrorMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getHttpStatus());
        }

    }

    @Test
    public void shouldThrowApplicationErrorWhenCallIsNotSuccessful() {
        doThrow(RestClientException.class).when(restTemplate).exchange(any(String.class),
                eq(HttpMethod.GET), any(),
                any(ParameterizedTypeReference.class));

        try {
            productClient.getProductDetails("13860428");
            fail("Exception must be thrown");
        } catch (ApplicationException ex) {
            assertEquals("500 INTERNAL_SERVER_ERROR", ex.getResponseStatusCode());
            assertEquals("Error connecting to service for retrieving product name", ex.getErrorMessage());
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getHttpStatus());
        }


    }

    private Object getMockProductApiResponse() {
        return ProductClientResponse.builder().data(ProductData.builder()
                .product(ProductDetails.builder()
                        .item(ItemDetails.builder()
                                .product_description(ProductDescription.builder().title("some title").build())
                                .build()).build()).build()).build();
    }

}