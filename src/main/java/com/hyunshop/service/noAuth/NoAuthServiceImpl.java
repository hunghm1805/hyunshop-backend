package com.hyunshop.service.noAuth;

import com.hyunshop.common.constances.RoleEnums;
import com.hyunshop.common.util.ValidateUtils;
import com.hyunshop.dto.noAuth.RegisterDto;
import com.hyunshop.entity.account.AccountEntity;
import com.hyunshop.entity.authority.AuthorityEntity;
import com.hyunshop.entity.role.RoleEntity;
import com.hyunshop.repository.account.AccountRepository;
import com.hyunshop.repository.authority.AuthorityRepository;
import com.hyunshop.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional

public class NoAuthServiceImpl implements NoAuthService {
    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterDto registerDto) {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", registerDto.getUsername());
        data.put("password", registerDto.getPassword());
        data.put("họ và tên", registerDto.getFullname());
        ValidateUtils.validateNullOrBlankString(data);
        ValidateUtils.isEmail(registerDto.getEmail());

        AccountEntity account = accountRepository.findByUsernameAndIsDeletedIsFalse(registerDto.getUsername()).orElse(null);

        if (account != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username này đã được đăng ký với một tài khoản khác, vui lòng sử dụng username khác!");
        } else {
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setIsDeleted(Boolean.FALSE);
            accountEntity.setFullname(registerDto.getFullname());
            accountEntity.setUsername(registerDto.getUsername());
            accountEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            accountEntity.setEmail(registerDto.getEmail());
            accountEntity.setPhoto(registerDto.getPhoto());
            accountEntity.setCreatedTime(new Date().getTime());
            accountRepository.save(accountEntity);

            RoleEntity role = roleRepository.findByName(RoleEnums.ROLE_CUSTOMER);

            AuthorityEntity authority = new AuthorityEntity();
            authority.setAccountEntity(accountEntity);
            authority.setRoleEntity(role);
            authorityRepository.save(authority);
        }


    }
}
