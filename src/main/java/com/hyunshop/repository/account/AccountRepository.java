package com.hyunshop.repository.account;

import com.hyunshop.entity.account.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    boolean existsByUsernameIgnoreCaseAndIsDeletedIsFalse(String username);

    @Query("select a from AccountEntity a where a.username = :username and a.isDeleted = false")
    AccountEntity findByUsername(@Param("username") String username);

    Optional<AccountEntity> findByUsernameAndIsDeletedIsFalse(String username);

}
