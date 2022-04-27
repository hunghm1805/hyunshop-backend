package com.hyunshop.entity.authority;

import com.hyunshop.entity.account.AccountEntity;
import com.hyunshop.entity.role.RoleEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authorities", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username", "role_id"})
})
public class AuthorityEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "username")
    private AccountEntity accountEntity;
    @ManyToOne(fetch = FetchType.EAGER)  @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;
}
