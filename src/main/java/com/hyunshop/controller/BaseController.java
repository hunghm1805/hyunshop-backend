package com.hyunshop.controller;

import com.hyunshop.common.data.CurrentAccount;
import com.hyunshop.entity.account.AccountEntity;
import com.hyunshop.service.account.AccountService;
import com.hyunshop.service.authority.AuthorityService;
import com.hyunshop.service.noAuth.NoAuthService;
import com.hyunshop.service.order.OrderService;
import com.hyunshop.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseController {

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected NoAuthService noAuthService;

    @Autowired
    protected AuthorityService authorityService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected OrderService orderService;
    protected <T> T checkNotNull(T reference) {
        return checkNotNull(reference);
    }

    <T> T checkNotNull(T reference, String message) throws Exception {
        if (reference == null) {
            throw new Exception(message);
        }
        return reference;
    }

    <T> T checkNotNull(Optional<T> reference) throws Exception {
        return checkNotNull(reference, "Requested item wasn't found!");
    }

    <T> T checkNotNull(Optional<T> reference, String notFoundMessage) throws Exception {
        if (reference.isPresent()) {
            return reference.get();
        } else {
            throw new Exception(notFoundMessage);
        }
    }

    protected CurrentAccount getCurrentAccount() {
        CurrentAccount currentAccount = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object o = authentication.getPrincipal();
        String username;
        if (o instanceof UserDetails) ((UserDetails) o).getUsername();
        username = o.toString();
        AccountEntity accountEntity = accountService.findByUsername(username);
        if (accountEntity != null) {
            String name = null;
            List<String> roles = authentication
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            currentAccount = CurrentAccount.builder()
                    .username(accountEntity.getUsername())
                    .email(accountEntity.getEmail())
                    .name(name)
                    .createdBy(accountEntity.getCreatedBy())
                    .roles(roles)
                    .build();
        }
        return currentAccount;
    }
}

