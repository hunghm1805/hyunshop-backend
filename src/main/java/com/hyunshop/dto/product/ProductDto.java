package com.hyunshop.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private Integer id;
    private String name;
    private String price;
    private String image;
    private String categoryId;
}
