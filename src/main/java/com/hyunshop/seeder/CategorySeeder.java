package com.hyunshop.seeder;

import com.hyunshop.common.constances.CategoryEnums;
import com.hyunshop.common.util.BeanUtils;
import com.hyunshop.entity.category.CategoryEntity;
import com.hyunshop.repository.category.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CategorySeeder implements Seeder{
    @Override
    public void seed() {
        CategoryRepository categoryRepository = BeanUtils.getBean(CategoryRepository.class);
        CategoryEntity[] categoryEntitiesArr = new CategoryEntity[]{
                new CategoryEntity(CategoryEnums.CLOCK),
                new CategoryEntity(CategoryEnums.PHONE),
                new CategoryEntity(CategoryEnums.HAT),
                new CategoryEntity(CategoryEnums.CAMERA),
                new CategoryEntity(CategoryEnums.MOTOBIKE),
                new CategoryEntity(CategoryEnums.COMPUTER),
                new CategoryEntity(CategoryEnums.RING),
                new CategoryEntity(CategoryEnums.FAN),
                new CategoryEntity(CategoryEnums.SUITCASE)
        };

        Set<CategoryEnums> categoryNames = categoryRepository.findAll()
                .stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toSet());

        List<CategoryEntity> categoryEntities = new ArrayList<>();

        for (CategoryEntity category : categoryEntitiesArr) {
            if (!categoryNames.contains(category.getName())) {
                categoryEntities.add(category);
            }
        }
        categoryRepository.saveAll(categoryEntities);
    }
}
