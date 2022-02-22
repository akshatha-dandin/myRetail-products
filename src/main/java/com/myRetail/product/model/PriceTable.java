package com.myRetail.product.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "price")
@Data
@Builder
public class PriceTable {

    @PrimaryKeyColumn(name = "product_id", type = PrimaryKeyType.PARTITIONED)
    private final String productId;

    @Column("price")
    private final String price;

    @Column("currency_code")
    private final String currencyCode;

}
