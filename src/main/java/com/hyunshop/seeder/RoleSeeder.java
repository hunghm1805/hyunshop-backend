package com.hyunshop.seeder;


import com.hyunshop.common.constances.RoleEnums;
import com.hyunshop.common.util.BeanUtils;
import com.hyunshop.entity.role.RoleEntity;
import com.hyunshop.repository.role.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class RoleSeeder implements Seeder {
    @Override
    public void seed() {
        RoleRepository roleRepository = BeanUtils.getBean(RoleRepository.class);
        RoleEntity[] roleEntitiesArr = new RoleEntity[]{
                new RoleEntity(RoleEnums.ROLE_HOST),
                new RoleEntity(RoleEnums.ROLE_CUSTOMER),
                new RoleEntity(RoleEnums.ROLE_ADMIN),
        };

        Set<RoleEnums> roleNames = roleRepository.findAll()
                .stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());

        List<RoleEntity> roleEntities = new ArrayList<>();

        for (RoleEntity role : roleEntitiesArr) {
            if (!roleNames.contains(role.getName())) {
                roleEntities.add(role);
            }
        }
        roleRepository.saveAll(roleEntities);
    }
}
