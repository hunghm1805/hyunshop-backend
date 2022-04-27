package com.hyunshop.service.order;

import com.hyunshop.common.constances.StatusEnums;
import com.hyunshop.common.data.CurrentAccount;
import com.hyunshop.common.data.PageRes;
import com.hyunshop.dto.order.OrderDto;
import com.hyunshop.dto.order.OrderSearchResponse;
import com.hyunshop.dto.order.OrderSuccessResponse;

public interface OrderService {

    public PageRes<OrderSearchResponse> search(int page,
                                               int pageSize,
                                               String sortProperty,
                                               String sortOrder,
                                               String textSearch,
                                               String dateType);
    OrderSuccessResponse save(CurrentAccount currentAccount, OrderDto orderDto);

    OrderSuccessResponse update(CurrentAccount currentAccount, OrderDto orderDto);

    void updateStatus(CurrentAccount currentAccount, Long orderId, StatusEnums status);

    void delete(CurrentAccount currentAccount, Long orderId);
}
