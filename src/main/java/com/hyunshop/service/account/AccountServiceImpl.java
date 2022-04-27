package com.hyunshop.service.account;

import com.hyunshop.entity.account.AccountEntity;
import com.hyunshop.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService, UserDetailsService {
    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity accountEntity = accountRepository.findByUsername(username);
        if (accountEntity == null) {
            throw new UsernameNotFoundException("Not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        accountEntity.getAuthorityEntities().forEach(authorityEntity -> {
            authorities.add(new SimpleGrantedAuthority(authorityEntity.getRoleEntity().getName().name()));
        });
        return User.builder()
                .username(accountEntity.getUsername())
                .password(accountEntity.getPassword())
                .authorities(authorities)
                .build();
    }

    @Override
    public AccountEntity findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
