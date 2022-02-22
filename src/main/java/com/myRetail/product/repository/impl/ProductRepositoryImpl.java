package com.myRetail.product.repository.impl;

import com.myRetail.product.domain.Price;
import com.myRetail.product.exceptions.BusinessException;
import com.myRetail.product.model.PriceTable;
import com.myRetail.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    CassandraOperations cassandraOperations;

    @Override
    public Price getPrice(BigInteger productId) {
        PriceTable priceRow = cassandraOperations.selectOne(
                Query.query(Criteria.where("product_id").is(String.valueOf(productId))), PriceTable.class);
        if (priceRow == null) {
            throw new BusinessException("Sorry, requested product is not found", "API_ERR_NOT_FOUND",
                    HttpStatus.NOT_FOUND);
        }
        Price price = Price.builder().value(new BigDecimal(priceRow.getPrice()))
                .currencyCode(priceRow.getCurrencyCode()).build();
        return price;
    }

    @Override
    public void upsert(BigInteger productNumber, Price price) {
        PriceTable priceTable = PriceTable.builder().productId(productNumber.toString())
                .price(price.getValue().toString()).currencyCode(price.getCurrencyCode()).build();
        cassandraOperations.insert(priceTable);
    }
}
