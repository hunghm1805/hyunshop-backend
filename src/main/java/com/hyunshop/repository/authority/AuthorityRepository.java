package com.hyunshop.repository.authority;

import com.hyunshop.entity.authority.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Integer> {
}
