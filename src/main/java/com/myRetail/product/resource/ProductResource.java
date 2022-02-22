package com.myRetail.product.resource;

import com.myRetail.product.domain.Price;
import com.myRetail.product.domain.Product;
import com.myRetail.product.dto.ProductRequestDto;
import com.myRetail.product.dto.ProductResponseDto;
import com.myRetail.product.dto.mapper.ProductResponseMapper;
import com.myRetail.product.exceptions.BusinessException;
import com.myRetail.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;

@RestController
@RequestMapping("${service.contextPath}")
@Slf4j
public class ProductResource {

    @Autowired
    ProductService productService;

    @Value("${product.api.errorcode.API_ERR_INVALID_PRICE}")
    private String invalidPriceErrorCode;

    @Value("${product.api.errormessage.API_ERR_INVALID_PRICE}")
    private String invalidPriceErrorMessage;

    @Value("${product.api.errorcode.API_ERR_INVALID_PRODUCTID}")
    private String invalidProductErrorCode;

    @Value("${product.api.errormessage.API_ERR_INVALID_PRODUCTID}")
    private String invalidProductErrorMessage;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable String productId) {
        log.debug("op=get_product_details, status=OK, " +
                "desc=Request received to get product details for productId={}", productId);
        BigInteger productNumber = validateProductId(productId);
        Product product = productService.getProduct(productNumber);
        ProductResponseDto productResponseDto = ProductResponseMapper.map(product);
        return new ResponseEntity(productResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity addOrUpdateProduct(@PathVariable String productId,
                                             @RequestBody @Valid ProductRequestDto productRequestDto) {
        log.debug("op=upsert_price, status=OK, " +
                "desc=Request received to update price for productId={}", productId);
        BigInteger productNumber = validateProductId(productId);
        BigDecimal value = validatePrice(productRequestDto);

        Price price = Price.builder().value(value)
                .currencyCode(productRequestDto.getCurrency()).build();
        productService.upsertPrice(productNumber, price);
        return new ResponseEntity(HttpStatus.OK);
    }

    private BigDecimal validatePrice(ProductRequestDto productRequestDto) {
        BigDecimal value;
        try {
            value = new BigDecimal(productRequestDto.getPrice()).setScale(2);
        } catch (NumberFormatException ex) {
            log.debug("op=upsert_price, status=OK, desc=Invalid price in request {}", productRequestDto.getPrice());
            throw new BusinessException(invalidPriceErrorMessage, invalidPriceErrorCode, HttpStatus.BAD_REQUEST);
        }
        return value;
    }

    private BigInteger validateProductId(String productId) {
        BigInteger productNumber;
        try {
            productNumber = new BigInteger(productId);
        } catch (NumberFormatException ex) {
            log.error("op=get_product_details, status=OK, desc=Invalid productId in request {}", productId);
            throw new BusinessException(invalidProductErrorMessage, invalidProductErrorCode, HttpStatus.BAD_REQUEST);
        }
        return productNumber;
    }
}
