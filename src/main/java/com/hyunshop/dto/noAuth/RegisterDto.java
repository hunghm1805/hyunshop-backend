package com.hyunshop.dto.noAuth;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String photo;

}
