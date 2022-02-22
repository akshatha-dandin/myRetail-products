package com.myRetail.product.resource;

import com.myRetail.product.domain.Price;
import com.myRetail.product.domain.Product;
import com.myRetail.product.dto.ProductRequestDto;
import com.myRetail.product.dto.ProductResponseDto;
import com.myRetail.product.dto.mapper.ProductResponseMapper;
import com.myRetail.product.exceptions.BusinessException;
import com.myRetail.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;

@RestController
@RequestMapping("${service.contextPath}")
public class ProductResource {

    @Autowired
    ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable String productId) {
        BigInteger productNumber = validateProductId(productId);
        Product product = productService.getProduct(productNumber);
        ProductResponseDto productResponseDto = ProductResponseMapper.map(product);
        return new ResponseEntity(productResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity addOrUpdateProduct(@PathVariable String productId,
                                             @RequestBody @Valid ProductRequestDto productRequestDto) {
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
            throw new BusinessException("Please enter a valid price", "API_ERR_INVALID_PRICE",
                    HttpStatus.BAD_REQUEST);
        }
        return value;
    }

    private BigInteger validateProductId(String productId) {
        BigInteger productNumber;
        try {
            productNumber = new BigInteger(productId);
        } catch (NumberFormatException ex) {
            throw new BusinessException("Please enter a valid productId", "API_ERR_INVALID_PRODUCTID",
                    HttpStatus.BAD_REQUEST);
        }
        return productNumber;
    }
}
