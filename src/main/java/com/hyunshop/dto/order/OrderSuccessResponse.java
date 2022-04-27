package com.hyunshop.dto.order;

import com.hyunshop.common.constances.StatusEnums;
import com.hyunshop.entity.account.AccountEntity;
import com.hyunshop.entity.order.OrderEntity;
import com.hyunshop.entity.orderDetail.OrderDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSuccessResponse {

    private Long id;
    private String address;
    private StatusEnums status;
    private String username;
    private List<ProductNameAndQuantityDto> products;
    private Long createdTime;

    public OrderSuccessResponse(OrderEntity order, List<String> productNames, List<OrderDetailEntity> orderDetailEntities, AccountEntity account) {
        this.address = order.getAddress();
        this.username = account.getUsername();
        this.id = order.getId();
        this.status = order.getStatus();
        this.createdTime = order.getCreatedTime();
        List<ProductNameAndQuantityDto> productNameAndQuantityDtoList = new ArrayList<>();

        for (int i = 0; i < productNames.size(); i++) {
            ProductNameAndQuantityDto productNameAndQuantityDto = ProductNameAndQuantityDto.builder()
                    .name(productNames.get(i))
                    .quantity(orderDetailEntities.get(i).getQuantity())
                    .price(orderDetailEntities.get(i).getPrice())
                    .build();
            productNameAndQuantityDtoList.add(productNameAndQuantityDto);
        }

        this.products = productNameAndQuantityDtoList;
    }
}
