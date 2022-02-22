package com.myRetail.product.repository.impl;

import com.myRetail.product.domain.Price;
import com.myRetail.product.exceptions.BusinessException;
import com.myRetail.product.model.PriceTable;
import com.myRetail.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;

@Repository
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    CassandraOperations cassandraOperations;

    @Value("${product.api.errorcode.API_ERR_NOT_FOUND}")
    private String errorCode;

    @Value("${product.api.errormessage.API_ERR_NOT_FOUND}")
    private String errorMessage;

    @Override
    public Price getPrice(BigInteger productId) {
        log.debug("op=get_product_details, status=OK, " +
                "desc=Getting price from db for productId={}", productId);
        PriceTable priceRow = cassandraOperations.selectOne(
                Query.query(Criteria.where("product_id").is(String.valueOf(productId))), PriceTable.class);
        if (priceRow == null) {
            log.error("op=get_product_details, status=KO, " +
                    "desc=Price record not found in db for productId={}", productId);
            throw new BusinessException(errorMessage, errorCode, HttpStatus.NOT_FOUND);
        }
        Price price = Price.builder().value(new BigDecimal(priceRow.getPrice()))
                .currencyCode(priceRow.getCurrencyCode()).build();
        log.debug("op=get_product_details, status=OK, " +
                "desc=Price retrieved from db for productId={}", productId);
        return price;
    }

    @Override
    public void upsert(BigInteger productNumber, Price price) {
        log.debug("op=upsert_price, status=OK, " +
                "desc=Updating price in db for productId={}", productNumber.toString());
        PriceTable priceTable = PriceTable.builder().productId(productNumber.toString())
                .price(price.getValue().toString()).currencyCode(price.getCurrencyCode()).build();
        cassandraOperations.insert(priceTable);
        log.info("op=upsert_price, status=OK, " +
                "desc=Price updated successfully for productId={}, price={}", productNumber, price.getValue());
    }
}
