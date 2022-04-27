package com.hyunshop.repository.role;

import com.hyunshop.common.constances.RoleEnums;
import com.hyunshop.entity.role.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByName(RoleEnums roleName);
}
