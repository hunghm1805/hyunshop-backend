package com.hyunshop.service.noAuth;

import com.hyunshop.dto.noAuth.RegisterDto;

public interface NoAuthService {
    void register(RegisterDto registerDto);
}
