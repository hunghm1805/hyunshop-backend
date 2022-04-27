package com.hyunshop.common.util;

import com.hyunshop.common.data.PageReq;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;
import java.util.Optional;

public class PaginationUtils {

    public static Pageable getPageable(PageReq pageReq) {
        pageReq = getPageReq(pageReq, "createdTime");
        return PageRequest.of(
                pageReq.getPage(),
                pageReq.getPageSize(),
                Sort.by(getOrder(pageReq, pageReq.getSortProperty())));
    }

    public static Pageable getPageable(PageReq pageReq, String customSortProperty) {
        pageReq = getPageReq(pageReq, customSortProperty);
        return PageRequest.of(
                pageReq.getPage(),
                pageReq.getPageSize(),
                Sort.by(getOrder(pageReq, customSortProperty)));
    }

    public static Pageable getPageable(PageReq pageReq, Integer customPageSize, String customSortProperty) {
        pageReq = getPageReq(pageReq, customPageSize, customSortProperty);
        return PageRequest.of(
                pageReq.getPage(),
                pageReq.getPageSize(),
                Sort.by(getOrder(pageReq, customSortProperty)));
    }

    private static Sort.Order getOrder(PageReq pageReq, String customSortProperty) {
        Sort.Direction direction =
                Objects.equals("desc", pageReq.getSortOrder()) ?
                        Sort.Direction.DESC :
                        Sort.Direction.ASC;
        if (Objects.equals("createdTime", customSortProperty)) {
            return new Sort.Order(direction, customSortProperty);
        }
        return new Sort.Order(direction, customSortProperty).ignoreCase();

    }

    public static PageReq getPageReq(PageReq pageReq, String customSortProperty) {
        Integer pageSize = Optional.ofNullable(pageReq.getPageSize()).orElse(10);
        String sortOrder = Optional.ofNullable(pageReq.getSortOrder()).orElse("desc");
        return getPageReq(pageReq, pageSize, customSortProperty, sortOrder);
    }

    public static PageReq getPageReq(PageReq pageReq, Integer customPageSize, String customSortProperty) {
        String sortOrder = Optional.ofNullable(pageReq.getSortOrder()).orElse("desc");
        return getPageReq(pageReq, customPageSize, customSortProperty, sortOrder);
    }

    public static PageReq getPageReq(PageReq pageReq, Integer customPageSize, String customSortProperty, String customSortOrder) {
        Integer page = Optional.ofNullable(pageReq.getPage()).orElse(0);
        Integer pageSize = Optional.ofNullable(pageReq.getPageSize()).orElse(customPageSize);
        String sortProperty = Optional.ofNullable(pageReq.getSortProperty()).orElse(customSortProperty);
        String sortOrder = Optional.ofNullable(pageReq.getSortOrder()).orElse(customSortOrder);
        return new PageReq(page, pageSize, sortProperty, sortOrder);
    }
}