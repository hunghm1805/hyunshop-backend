package com.hyunshop.seeder;

import com.hyunshop.common.constances.RoleEnums;
import com.hyunshop.common.util.BeanUtils;
import com.hyunshop.entity.account.AccountEntity;
import com.hyunshop.entity.authority.AuthorityEntity;
import com.hyunshop.entity.role.RoleEntity;
import com.hyunshop.repository.account.AccountRepository;
import com.hyunshop.repository.authority.AuthorityRepository;
import com.hyunshop.repository.role.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class UserSeeder implements Seeder{

    @Override
    public void seed() {
        AccountRepository accountRepository = BeanUtils.getBean(AccountRepository.class);
        RoleRepository roleRepository = BeanUtils.getBean(RoleRepository.class);
        AuthorityRepository authorityRepository = BeanUtils.getBean(AuthorityRepository.class);
        PasswordEncoder passwordEncoder = BeanUtils.getBean(BCryptPasswordEncoder.class);

        generate(accountRepository, authorityRepository, roleRepository, passwordEncoder,
                "host", "host123", RoleEnums.ROLE_HOST);

        generate(accountRepository, authorityRepository, roleRepository, passwordEncoder,
                "admin", "admin123", RoleEnums.ROLE_ADMIN);

        generate(accountRepository, authorityRepository, roleRepository, passwordEncoder,
                "customer", "customer123", RoleEnums.ROLE_CUSTOMER);
    }

    private void generate(AccountRepository accountRepository, AuthorityRepository authorityRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                          String username, String rawPassword, RoleEnums roleName) {
        if (!accountRepository.existsByUsernameIgnoreCaseAndIsDeletedIsFalse(username)) {
            AccountEntity accountEntity = AccountEntity.builder()
                    .username(username)
                    .password(passwordEncoder.encode(rawPassword))
                    .email(username.concat("@gmail.com"))
                    .isDeleted(Boolean.FALSE)
                    .build();
            accountRepository.save(accountEntity);

            RoleEntity roleEntity = roleRepository.findByName(RoleEnums.ROLE_ADMIN);
            log.info("ROLE {} founded in the database", roleEntity);

            AuthorityEntity authorityEntity = AuthorityEntity.builder()
                    .accountEntity(accountEntity)
                    .roleEntity(roleRepository.findByName(roleName))
                    .build();
            authorityRepository.save(authorityEntity);
        }
    }
}
