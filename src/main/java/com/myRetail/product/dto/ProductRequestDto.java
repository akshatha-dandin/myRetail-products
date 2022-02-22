package com.myRetail.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Price is mandatory")
    String price;

    @NotBlank(message = "Currency code is mandatory")
    String currency;

}
