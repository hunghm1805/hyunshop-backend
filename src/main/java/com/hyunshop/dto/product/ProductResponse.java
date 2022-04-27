package com.hyunshop.dto.product;

import com.hyunshop.common.constances.CategoryEnums;
import com.hyunshop.entity.product.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Integer id;
    private String name;
    private String image;
    private Double price;
    @Enumerated(EnumType.STRING)
    private CategoryEnums categoryName;

    public ProductResponse(ProductEntity product) {
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.categoryName = product.getCategoryEntity().getName();
    }
}
