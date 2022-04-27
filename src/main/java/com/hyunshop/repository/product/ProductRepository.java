package com.hyunshop.repository.product;

import com.hyunshop.entity.order.OrderEntity;
import com.hyunshop.entity.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    boolean existsByNameIgnoreCase(String name);

    Optional<ProductEntity> findByIdAndAvailableIsTrue(Integer id);

    boolean existsByNameIgnoreCaseAndIdNotLike(String name, Integer id);

    @Query("select p from ProductEntity p where lower(p.name) like concat('%', lower(:trim), '%') and p.price between :priceMin and :priceMax")
    Page<ProductEntity> findAllByNameAndByMinAndByMax(String trim, Long priceMin, Long priceMax, Pageable pageable);

    @Query("select p from ProductEntity p where p.price between :priceMin and :priceMax")
    Page<ProductEntity> findAllByPriceMinAndPriceMax(Long priceMin, Long priceMax, Pageable pageable);

    @Query("select p from ProductEntity p where lower(p.name) like concat('%', lower(:trim), '%')")
    Page<ProductEntity> findByNameIgnoreCase(String trim, Pageable pageable);

    @Query("select p.name from ProductEntity p " +
            "join OrderDetailEntity o on p.id = o.productId " +
            "where o.orderEntity = ?1")
    List<String> findAllNameProductByOrderEntity(OrderEntity orderEntity);
}
