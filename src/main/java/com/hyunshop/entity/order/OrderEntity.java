package com.hyunshop.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hyunshop.common.constances.StatusEnums;
import com.hyunshop.entity.BaseInfoEntity;
import com.hyunshop.entity.account.AccountEntity;
import com.hyunshop.entity.orderDetail.OrderDetailEntity;
import lombok.*;


import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends BaseInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    @Enumerated(EnumType.STRING)
    private StatusEnums status;
    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "username")
    private String username;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private AccountEntity accountEntity;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderEntity")
    private List<OrderDetailEntity> orderDetails;
}
