package com.myRetail.product.client;

import com.myRetail.product.dto.ProductClientResponse;
import com.myRetail.product.exceptions.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ProductClient {

    public static final String TCIN = "tcin";
    public static final String KEY = "key";
    public static final String ERROR_WHILE_RETRIEVING_PRODUCT_NAME = "Error while retrieving product name";
    public static final String ERROR_CONNECTING_TO_SERVICE_FOR_RETRIEVING_PRODUCT_NAME =
            "Error connecting to service for retrieving product name";

    @Autowired
    @Qualifier("productRestTemplate")
    private RestTemplate productRestTemplate;

    @Value("${product.rest.getProductsUrl}")
    private String getProductsUrl;

    @Value("${product.rest.headerName}")
    private String headerName;


    public ProductClientResponse getProductDetails(String productId) {
        log.debug("op=get_product_details, status=OK, desc=Getting product name from aggregation API");
        ResponseEntity<ProductClientResponse> response;
        try {
            Map<String, String> urlParams = new HashMap<>();
            urlParams.put(TCIN, productId);
            urlParams.put(KEY, "someKey");
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getProductsUrl);
            String urlString = builder.buildAndExpand(urlParams).toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.set(headerName, "someToken");
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            log.info("op=get_product_details, status=OK, " +
                    "desc=Invoking aggregations API with url {} to get product name", urlString);
            response = productRestTemplate.exchange(urlString, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<>() {
                    });
            ProductClientResponse productApiResponse = response.getBody();
            log.info("op=get_product_details, status=OK, desc=Product name retrieved successfully.");
            return productApiResponse;
        } catch (HttpClientErrorException ex) {
            log.error("op=get_product_details, status=KO, " +
                    "desc=Client error exception while trying to retrieve product name", ex);
            throw new ApplicationException(ERROR_WHILE_RETRIEVING_PRODUCT_NAME);
        } catch (RestClientException ex) {
            log.error("op=get_product_details, status=KO, " +
                    "desc=Exception while trying to retrieve product name", ex);
            throw new ApplicationException(ERROR_CONNECTING_TO_SERVICE_FOR_RETRIEVING_PRODUCT_NAME);
        }
    }
}
