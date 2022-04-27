package com.hyunshop.dto.order;

import com.hyunshop.common.constances.StatusEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderDto {
    private Long id;
    private String address;
    private List<ProductOrder> productOrders;
    private StatusEnums status;

}
