package com.hyunshop.service.order;

import com.hyunshop.common.constances.StatusEnums;
import com.hyunshop.common.data.CurrentAccount;
import com.hyunshop.common.data.PageReq;
import com.hyunshop.common.data.PageRes;
import com.hyunshop.common.data.Pages;
import com.hyunshop.common.util.ValidateUtils;
import com.hyunshop.dto.order.OrderDto;
import com.hyunshop.dto.order.OrderSearchResponse;
import com.hyunshop.dto.order.OrderSuccessResponse;
import com.hyunshop.dto.order.ProductOrder;
import com.hyunshop.entity.account.AccountEntity;
import com.hyunshop.entity.order.OrderEntity;
import com.hyunshop.entity.orderDetail.OrderDetailEntity;
import com.hyunshop.entity.product.ProductEntity;
import com.hyunshop.repository.account.AccountRepository;
import com.hyunshop.repository.order.OrderRepository;
import com.hyunshop.repository.orderDetail.OrderDetailRepository;
import com.hyunshop.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final EntityManager entityManager;

    private final AccountRepository accountRepository;

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final ProductRepository productRepository;

    @Override
    public PageRes<OrderSearchResponse> search(int page, int pageSize, String sortProperty, String sortOrder, String textSearch, String dateType) {
        PageReq pageReq = new PageReq(page, pageSize, sortProperty, sortOrder);
        List<OrderSearchResponse> orderSearchResponseList = new ArrayList<>();
        String nativeSql = "SELECT o.id, a.fullname, a.email, o.address, o.status, o.created_time " +
                "FROM orders o " +
                "         LEFT JOIN accounts a ON a.username = o.username " +
                "WHERE 1 = 1 ";

        if (textSearch != null && textSearch.trim().length() > 0) {

            nativeSql += " AND LOWER(a.fullname) LIKE CONCAT('%','" + textSearch.trim() + "','%')";
        }

        nativeSql += " order by o.created_time desc";


        Query query = entityManager.createNativeQuery(nativeSql).setFirstResult((page) * pageSize).setMaxResults(pageSize);

        List<Object[]> objects = query.getResultList();

        String countSql = nativeSql.replace(nativeSql.substring(0, nativeSql.indexOf("FROM") - 1), "select count(o.id)");

        Query queryTotal = entityManager.createNativeQuery(countSql);
        BigInteger totalElements = (BigInteger) queryTotal.getSingleResult();


        if (!objects.isEmpty()) {
            for (Object[] object : objects) {
                BigInteger orderIdBigInteger = Objects.isNull(object[0]) ? null : (BigInteger) object[0];
                Long orderId = orderIdBigInteger.longValue();
                String fullname = Objects.isNull(object[1]) ? null : object[1].toString();
                String email = Objects.isNull(object[2]) ? null : object[2].toString();
                String address = Objects.isNull(object[3]) ? null : object[3].toString();
                String statusString = Objects.isNull(object[4]) ? null : object[4].toString();
                StatusEnums status = StatusEnums.valueOf(statusString);
                BigInteger orderTimeBigInteger = Objects.isNull(object[5]) ? null : (BigInteger) object[5];
                Long orderTime = orderTimeBigInteger.longValue();

                OrderSearchResponse orderSearchResponse = OrderSearchResponse.builder()
                        .id(orderId)
                        .fullname(fullname)
                        .email(email)
                        .address(address)
                        .status(status)
                        .orderTime(orderTime)
                        .statusString(getOrderStatusStr(status))
                        .build();
                orderSearchResponseList.add(orderSearchResponse);
            }

            if (Objects.equals("today", dateType) && !orderSearchResponseList.isEmpty()) {
                List<OrderSearchResponse> orderSearchResponses = getOrderSearchResponsesByToday(orderSearchResponseList);
                Pages pages = getPages(orderSearchResponses.size(), pageReq);
                boolean hasNext = pageReq.getPage() < pages.getTotalPages();
                boolean hasPrevious = pageReq.getPage() > 1;
                return new PageRes<>(orderSearchResponses, pages.getTotalPages(), pages.getTotalElements(), hasNext, hasPrevious);
            }

            if ((Objects.equals("week", dateType)) && !orderSearchResponseList.isEmpty()) {
                List<OrderSearchResponse> orderSearchResponses = getOrderSearchResponsesByWeekNow(orderSearchResponseList);
                Pages pages = getPages(orderSearchResponses.size(), pageReq);
                boolean hasNext = pageReq.getPage() < pages.getTotalPages();
                boolean hasPrevious = pageReq.getPage() > 1;
                return new PageRes<>(orderSearchResponses, pages.getTotalPages(), pages.getTotalElements(), hasNext, hasPrevious);
            }

            if ((Objects.equals("month", dateType)) && !orderSearchResponseList.isEmpty()) {
                List<OrderSearchResponse> orderSearchResponses = getOrderSearchResponsesByMonthNow(orderSearchResponseList);
                Pages pages = getPages(orderSearchResponses.size(), pageReq);
                boolean hasNext = pageReq.getPage() < pages.getTotalPages();
                boolean hasPrevious = pageReq.getPage() > 1;
                return new PageRes<>(orderSearchResponseList, pages.getTotalPages(), pages.getTotalElements(), hasNext, hasPrevious);
            }

            Pages pages = getPages(Integer.parseInt(String.valueOf(totalElements)), pageReq);
            boolean hasNext = pageReq.getPage() < pages.getTotalPages();
            boolean hasPrevious = pageReq.getPage() > 1;
            return new PageRes<>(orderSearchResponseList, pages.getTotalPages(), pages.getTotalElements(), hasNext, hasPrevious);
        }
        return new PageRes<>(new ArrayList<>(), 0, 0, false);
    }

    private String getOrderStatusStr(StatusEnums status) {
        switch (status) {
            case PENDING:
                return "Đang chờ xử lý";
            case CANCELED:
                return "Đã Hủy";
            case ACCEPT:
                return "Đã chấp nhận";
            case IN_DELIVERY:
                return "Đang vận chuyển";
            case FINISH:
                return "Đã hoàn thành";
            default:
                return "";
        }
    }

    @Override
    public OrderSuccessResponse save(CurrentAccount currentAccount, OrderDto orderDto) {
        validateOrderDto(orderDto);

        AccountEntity account;
        OrderEntity order;
        if (currentAccount != null) {
            account = accountRepository.findByUsernameAndIsDeletedIsFalse(currentAccount.getUsername()).orElse(null);
            order = getOrder(account, orderDto);
            List<String> productNames = getProductEntities(order, orderDto.getProductOrders())
                    .stream().map(ProductEntity::getName).collect(Collectors.toList());

            List<OrderDetailEntity> orderDetailEntities = getOderDetailEntities(order);
            return new OrderSuccessResponse(order, productNames, orderDetailEntities, account);
        }
        return new OrderSuccessResponse();
    }

    @Override
    public OrderSuccessResponse update(CurrentAccount currentAccount, OrderDto orderDto) {
        isUpdateOrder(orderDto);

        AccountEntity account = accountRepository.findByUsernameAndIsDeletedIsFalse(currentAccount.getUsername()).orElse(null);
        OrderEntity orderEntity = orderRepository.findById(orderDto.getId()).orElse(null);

        List<String> productNames = null;
        List<OrderDetailEntity> orderDetailEntities = new ArrayList<>();

        if (isUpdateStatus(orderEntity, orderDto.getStatus())) {
            orderEntity.setStatus(orderDto.getStatus());
            orderEntity.setUpdatedTime(new Date().getTime());
            orderEntity.setUpdatedBy(account.getUsername());
            orderRepository.save(orderEntity);
            productNames = productRepository.findAllNameProductByOrderEntity(orderEntity);
            orderDetailEntities = getOderDetailEntities(orderEntity);
        }

        if (isUpdateOrderProducts(orderEntity, orderDto)) {
            orderDetailEntities = getOderDetailEntities(orderEntity);
            if (!orderDetailEntities.isEmpty()) {
                orderDetailEntities.forEach(orderDetailEntity -> {
                    orderDetailEntity.setOrderEntity(null);
                });
            }
            orderEntity.setOrderDetails(null);
            orderDetailRepository.deleteAll(orderDetailEntities);

            orderDetailEntities = getOderDetailEntities(orderEntity);

            if (orderDetailEntities.size() == 0) {
                productNames = getProductEntities(orderEntity, orderDto.getProductOrders()).stream().map(ProductEntity::getName).collect(Collectors.toList());
                orderDetailEntities = getOderDetailEntities(orderEntity);
            }
        }
        return new OrderSuccessResponse(orderEntity, productNames, orderDetailEntities, account);
    }

    @Override
    public void updateStatus(CurrentAccount currentAccount, Long orderId, StatusEnums status) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found!");
        });

        orderEntity.setStatus(status);
        orderEntity.setUpdatedTime(new Date().getTime());
        orderEntity.setUpdatedBy(currentAccount.getUsername());
        orderRepository.save(orderEntity);
    }

    @Override
    public void delete(CurrentAccount currentAccount, Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found!");
        });
        orderEntity.setIsDeleted(Boolean.TRUE);
        orderEntity.setUpdatedTime(new Date().getTime());
        orderEntity.setUpdatedBy(currentAccount.getUsername());
        orderRepository.save(orderEntity);
    }

    private void validateOrderDto(OrderDto orderDto) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("địa chỉ", orderDto.getAddress());
        if (orderDto.getProductOrders() == null || orderDto.getProductOrders().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cần chọn ít nhất 1 sản phẩm!");
        }
        ValidateUtils.validateNullOrBlank(map);
    }

    private OrderEntity getOrder(AccountEntity account, OrderDto orderDto) {
        OrderEntity orderEntity = OrderEntity.builder()
                .address(orderDto.getAddress())
                .status(StatusEnums.PENDING)
                .username(account.getUsername())
                .isDeleted(Boolean.FALSE)
                .build();
        orderEntity.setCreatedTime(new Date().getTime());
        return orderRepository.save(orderEntity);
    }

    private List<ProductEntity> getProductEntities(OrderEntity orderEntity, List<ProductOrder> productOrders) {
        List<ProductEntity> productEntities = new ArrayList<>();
        if (!productOrders.isEmpty()) {
            productOrders.forEach(productOrder -> {
                ProductEntity productEntity = productRepository.findByIdAndAvailableIsTrue(productOrder.getProductId()).orElse(null);
                if (productEntity != null) {
                    OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                    orderDetailEntity.setOrderId(orderEntity.getId());
                    orderDetailEntity.setOrderEntity(orderEntity);
                    orderDetailEntity.setPrice(productEntity.getPrice());
                    orderDetailEntity.setQuantity(productOrder.getQuantity());
                    orderDetailEntity.setProductId(productEntity.getId());
                    orderDetailEntity.setProductEntity(productEntity);
                    orderDetailRepository.save(orderDetailEntity);
                    productEntities.add(productEntity);
                }
            });
        }
        return productEntities.stream().distinct().collect(Collectors.toList());
    }


    private List<ProductEntity> getProductEntitiesForUpdate(OrderEntity orderEntity) {
        List<ProductEntity> productEntities = new ArrayList<>();


        return productEntities.stream().distinct().collect(Collectors.toList());
    }

    private List<OrderDetailEntity> getOderDetailEntities(OrderEntity orderEntity) {
        List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findAllByOrderEntity(orderEntity);
        return orderDetailEntities.stream().distinct().collect(Collectors.toList());
    }

    private void isUpdateOrder(OrderDto orderDto) {
        if (orderDto.getId() == null
                && orderDto.getProductOrders() == null && orderDto.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Dữ liệu truyền lên không thấy dữ liệu nào liên quan tới cập nhật giỏ hàng!");
        }
    }

    private Boolean isUpdateStatus(OrderEntity orderEntity, StatusEnums status) {
        if (status != null && status != orderEntity.getStatus()) {
            return true;
        }
        return false;
    }

    private Boolean isUpdateOrderProducts(OrderEntity orderEntity, OrderDto orderDto) {
        if (orderEntity.getStatus().equals(StatusEnums.CANCELED) || orderEntity.getStatus().equals(StatusEnums.REJECT) || orderEntity.getStatus().equals(StatusEnums.FINISH)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không thể cập nhật vì lịch khám này đã kết thúc hoặc bị hủy!");
        }

        List<Integer> productIdsChange = new ArrayList<>();
        orderDto.getProductOrders().forEach(productOrder -> {
            productIdsChange.add(productOrder.getProductId());
        });

        List<Integer> productIds = orderDetailRepository.findAllByOrderEntity(orderEntity)
                .stream().map(OrderDetailEntity::getProductId).collect(Collectors.toList());

        if (productIdsChange.size() != productIds.size() || productIdsChange.isEmpty()) {
            return true;
        } else {
            for (int i = 0; i < productIdsChange.size(); i++) {
                OrderDetailEntity orderDetailEntity = orderDetailRepository.findByOrderIdLikeAndProductIdLike(orderEntity.getId(), productIdsChange.get(i)).orElse(null);
                if (orderDetailEntity == null) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<OrderSearchResponse> getOrderSearchResponsesByToday(List<OrderSearchResponse> orderSearchResponses) {
        if (orderSearchResponses != null) {
            List<Long> orderTimes = orderSearchResponses.stream().map(OrderSearchResponse::getOrderTime).collect(Collectors.toList());
            if (orderTimes != null) {
                LocalDate today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int day = today.getDayOfMonth();
                List<OrderSearchResponse> orderSearchResponseList = new ArrayList<>();

                for (int i = 0; i < orderTimes.size(); i++) {
                    Date date = new Date(orderTimes.get(i));
                    LocalDate localDateOfT = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int dayOfMonth = localDateOfT.getDayOfMonth();
                    if (dayOfMonth == day) {
                        orderSearchResponseList.add(orderSearchResponses.get(i));
                    }
                }
                if (!orderSearchResponseList.isEmpty()) {
                    return orderSearchResponseList;
                }
            }
        }
        return new ArrayList<>();
    }

    private List<OrderSearchResponse> getOrderSearchResponsesByWeekNow(List<OrderSearchResponse> orderSearchResponses) {
        if (orderSearchResponses != null) {
            List<Long> orderTimes = orderSearchResponses.stream().map(OrderSearchResponse::getOrderTime).collect(Collectors.toList());
            if (orderTimes != null) {
                LocalDate dateNow = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int weekOfYearNow = dateNow.get(WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear());
                List<OrderSearchResponse> orderSearchResponseList = new ArrayList<>();

                for (int i = 0; i < orderTimes.size(); i++) {
                    Date date = new Date(orderTimes.get(i));
                    LocalDate localDateOfT = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int weekOfYear = localDateOfT.get(WeekFields.of(DayOfWeek.MONDAY, 7).weekOfYear());
                    if (weekOfYear == weekOfYearNow) {
                        orderSearchResponseList.add(orderSearchResponses.get(i));
                    }
                }
                if (!orderSearchResponseList.isEmpty()) {
                    return orderSearchResponseList;
                }
            }
        }
        return new ArrayList<>();
    }

    private List<OrderSearchResponse> getOrderSearchResponsesByMonthNow(List<OrderSearchResponse> orderSearchResponses) {
        if (orderSearchResponses != null) {
            List<Long> orderTimes = orderSearchResponses.stream().map(OrderSearchResponse::getOrderTime).collect(Collectors.toList());
            if (orderTimes != null) {
                LocalDate dateNow = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int monthNow = dateNow.getMonthValue();
                List<OrderSearchResponse> orderSearchResponseList = new ArrayList<>();
                for (int i = 0; i < orderTimes.size(); i++) {
                    Date date = new Date(orderTimes.get(i));
                    LocalDate localDateOfT = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    int month = localDateOfT.getMonthValue();
                    if (month == monthNow) {
                        orderSearchResponseList.add(orderSearchResponses.get(i));
                    }
                }
                if (!orderSearchResponseList.isEmpty()) {
                    return orderSearchResponseList;
                }
            }
        }
        return new ArrayList<>();
    }

    private Pages getPages(int listSize, PageReq pageReq) {
        Long totalElements = Long.valueOf(listSize);
        int totalPages = 0;
        if (totalElements > 0) {
            totalPages = (int) (totalElements % pageReq.getPageSize() == 0 ?
                    totalElements / pageReq.getPageSize() :
                    totalElements / pageReq.getPageSize() + 1);
        }
        Pages pages = new Pages(totalElements, totalPages, pageReq.getPageSize(), pageReq.getPage());
        return pages;
    }


}
