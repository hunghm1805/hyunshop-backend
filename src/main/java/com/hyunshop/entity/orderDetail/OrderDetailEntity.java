package com.hyunshop.entity.orderDetail;

import java.io.Serializable;

import javax.persistence.*;

import com.hyunshop.entity.order.OrderEntity;
import com.hyunshop.entity.product.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orderdetails")
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double price;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductEntity productEntity;

    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private OrderEntity orderEntity;

    @Column( name = "order_id")
    private Long orderId;
}
