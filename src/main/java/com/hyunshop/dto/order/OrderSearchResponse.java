package com.hyunshop.dto.order;

import com.hyunshop.common.constances.StatusEnums;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSearchResponse {
    private Long id;
    private String fullname;
    private String email;
    private String address;
    private StatusEnums status;
    private String statusString;
    private Long orderTime;

}
