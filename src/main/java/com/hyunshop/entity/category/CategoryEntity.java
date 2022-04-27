package com.hyunshop.entity.category;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.hyunshop.common.constances.CategoryEnums;
import com.hyunshop.entity.product.ProductEntity;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class CategoryEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private CategoryEnums name;

    @JsonIgnore
    @OneToMany(mappedBy = "categoryEntity")
    private List<ProductEntity> products;

    public CategoryEntity(CategoryEnums name) {
        this.name = name;
    }
}
