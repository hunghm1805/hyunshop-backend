package com.hyunshop.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyunshop.entity.BaseInfoEntity;
import com.hyunshop.entity.authority.AuthorityEntity;
import com.hyunshop.entity.order.OrderEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity extends BaseInfoEntity {
    @Id
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String photo;
    @Column(name = "is_deleted",
            nullable = false,
            columnDefinition = "boolean default false")
    private Boolean isDeleted = Boolean.FALSE;

    @JsonIgnore
    @OneToMany(mappedBy = "accountEntity")
    private List<OrderEntity> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "accountEntity")
    private List<AuthorityEntity> authorities;

    @OneToMany(mappedBy = "accountEntity")
    private List<AuthorityEntity> authorityEntities;


}
