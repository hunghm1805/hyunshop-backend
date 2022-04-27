package com.hyunshop.entity.product;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.hyunshop.entity.BaseInfoEntity;
import com.hyunshop.entity.category.CategoryEntity;
import com.hyunshop.entity.orderDetail.OrderDetailEntity;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductEntity extends BaseInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String image;

    private Double price;

    private Boolean available = Boolean.TRUE;

    @Column(name = "category_id")
    private Integer categoryId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity categoryEntity;
    @JsonIgnore
    @OneToMany(mappedBy = "productEntity")
    private List<OrderDetailEntity> orderDetails;

    public ProductEntity(String name, String image, Double price, Boolean available, Integer categoryId){
        this.name = name;
        this.image = image;
        this.price = price;
        this.available = available;
        this.categoryId = categoryId;
    }


}
