package com.hyunshop.entity.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyunshop.common.constances.RoleEnums;
import com.hyunshop.entity.authority.AuthorityEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class RoleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private RoleEnums name;

    @JsonIgnore
    @OneToMany(mappedBy = "roleEntity", fetch = FetchType.EAGER)
    List<AuthorityEntity> authorities;

    public RoleEntity(RoleEnums name) {
        this.name = name;
    }
}
