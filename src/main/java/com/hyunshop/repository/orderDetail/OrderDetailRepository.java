package com.hyunshop.repository.orderDetail;

import com.hyunshop.entity.order.OrderEntity;
import com.hyunshop.entity.orderDetail.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    @Query(value = "select * from orderdetails  where order_id = :orderEntity", nativeQuery = true)
    List<OrderDetailEntity> findAllByOrderEntity(OrderEntity orderEntity);

    List<OrderDetailEntity> findByOrderIdLike(Long id);

@Query("select od from OrderDetailEntity od where od.orderId = ?1 and od.productId = ?2")
    Optional<OrderDetailEntity> findByOrderIdLikeAndProductIdLike(Long id, Integer integer);
}
