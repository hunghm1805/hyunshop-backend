package com.hyunshop.seeder;

import com.hyunshop.common.util.BeanUtils;
import com.hyunshop.entity.product.ProductEntity;
import com.hyunshop.repository.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
public class ProductSeeder implements Seeder{


    @Override
    public void seed() {
        ProductRepository productRepository = BeanUtils.getBean(ProductRepository.class);
        ProductEntity[] productEntitiesArr = new ProductEntity[]{
                new ProductEntity("Máy tính xyz", "image", 500000.0,true, 6),
                new ProductEntity("Camera 123", "image", 2500000.0,true, 4),
                new ProductEntity("Đồng hồ abc", "image", 4500000.0,true, 1),
        };


        Set<String> productNames = productRepository.findAll()
                .stream()
                .map(ProductEntity :: getName)
                .collect(Collectors.toSet());
        List<ProductEntity> productEntities = new ArrayList<>();

        for (ProductEntity productEntity : productEntitiesArr) {
            if(!productNames.contains(productEntity.getName())){
                productEntities.add(productEntity);
            }
        }
        productRepository.saveAll(productEntities);
    }
}


