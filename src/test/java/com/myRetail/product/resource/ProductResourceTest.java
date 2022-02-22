package com.myRetail.product.resource;

import com.myRetail.product.domain.Price;
import com.myRetail.product.domain.Product;
import com.myRetail.product.dto.ProductRequestDto;
import com.myRetail.product.exceptions.BusinessException;
import com.myRetail.product.exceptions.ProductControllerAdvice;
import com.myRetail.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductResourceTest {

    private MockMvc mockMvc;

    @Mock
    ProductService productService;

    @InjectMocks
    ProductResource productResource;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(productResource)
                .addPlaceholderValue("service.contextPath", "/api/v1/products")
                .setControllerAdvice(new ProductControllerAdvice())
                .build();
    }

    @Test
    public void shouldBeAbleToGetProductDetails() throws Exception {
        BigInteger id = new BigInteger("13860428");
        Product product = Product.builder().id(id).name("someName").price(
                Price.builder().value(new BigDecimal("13.45")).currencyCode("USD").build()).build();
        when(productService.getProduct(id)).thenReturn(product);
        mockMvc.perform(get("/api/v1/products/13860428"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(
                        "{\"id\":13860428,\"name\":\"someName\",\"" +
                        "current_price\":{\"value\":13.45,\"currency_code\":\"USD\"}}"));
    }

    @Test
    public void shouldThrowBadRequestForInvalidInput() throws Exception {
        mockMvc.perform(get("/api/v1/products/ssddd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[{\"errorCode\":\"API_ERR_INVALID_PRODUCTID\"" +
                        ",\"errorMessage\":\"Please enter a valid productId\"}]"));
    }

    @Test
    public void shouldThrowNotFoundExceptionForInvalidProductId() throws Exception {
        BigInteger id = new BigInteger("13860428");
        doThrow(new BusinessException("Sorry, requested product is not found", "API_ERR_NOT_FOUND",
                HttpStatus.NOT_FOUND)).when(productService).getProduct(id);
        mockMvc.perform(get("/api/v1/products/13860428"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("[{\"errorCode\":\"API_ERR_NOT_FOUND\"" +
                        ",\"errorMessage\":\"Sorry, requested product is not found\"}]"));
    }

    @Test
    public void shouldBeAbleToUpdateProductDetails() throws Exception {
        BigInteger id = new BigInteger("13860428");
        ProductRequestDto productRequestDto = ProductRequestDto.builder().price("13.45").currency("USD").build();
        mockMvc.perform(put("/api/v1/products/13860428")
                        .header("Content-type","application/json")
                        .content("{\"price\":\"14.99\",\"currency\":\"USD\"}"))
                .andExpect(status().is2xxSuccessful());
        verify(productService).upsertPrice(id, Price.builder().value(new BigDecimal("14.99"))
                .currencyCode("USD").build());
    }

    @Test
    public void shouldThrowBadRequestForInvalidProductIdOnUpdate() throws Exception {
        mockMvc.perform(put("/api/v1/products/ssddd")
                .header("Content-type","application/json")
                .content("{\"price\":\"14.99\",\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[{\"errorCode\":\"API_ERR_INVALID_PRODUCTID\"" +
                        ",\"errorMessage\":\"Please enter a valid productId\"}]"));
    }

    @Test
    public void shouldThrowBadRequestForEmptyPriceOnUpdate() throws Exception {
        mockMvc.perform(put("/api/v1/products/13860428")
                        .header("Content-type","application/json")
                        .content("{\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[{\"errorCode\":\"API_ERR_INVALID_INPUT\"," +
                        "\"errorMessage\":\"Price is mandatory\"}]"));
    }

    @Test
    public void shouldThrowBadRequestForEmptyCurrencyOnUpdate() throws Exception {
        mockMvc.perform(put("/api/v1/products/13860428")
                        .header("Content-type","application/json")
                        .content("{\"price\":\"14.99\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[{\"errorCode\":\"API_ERR_INVALID_INPUT\"," +
                        "\"errorMessage\":\"Currency code is mandatory\"}]"));
    }

    @Test
    public void shouldThrowBadRequestForInvalidInputOnUpdate() throws Exception {
        mockMvc.perform(put("/api/v1/products/13860428")
                        .header("Content-type","application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().contains("API_ERR_INVALID_INPUT");
    }

    @Test
    public void shouldThrowBadRequestForInvalidPriceOnUpdate() throws Exception {
        mockMvc.perform(put("/api/v1/products/13860428")
                        .header("Content-type","application/json")
                        .content("{\"price\":\"sss\",\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[{\"errorCode\":\"API_ERR_INVALID_PRICE\"," +
                        "\"errorMessage\":\"Please enter a valid price\"}]"));
    }
}