package com.hyunshop.common.data;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CurrentAccount {
    private String username;
    private String email;
    private String name;
    private List<String> roles;
    private String createdBy;
}
