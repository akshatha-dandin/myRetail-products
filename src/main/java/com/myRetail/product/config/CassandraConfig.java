package com.myRetail.product.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

@Configuration
public class CassandraConfig {

    public @Bean CqlSession session() {
        return CqlSession.builder().withKeyspace("myRetail_price").build();
    }

    public @Bean
    CassandraOperations getCassandraTemplate() {
        return new CassandraTemplate(session());
    }
}
