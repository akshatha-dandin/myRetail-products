package com.myRetail.product;

import com.myRetail.product.resource.ProductResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductApplicationTests {

	@Autowired
	ProductResource productResource;

	@Test
	void contextLoads() {
		assertThat(productResource).isNotNull();
	}

}
