package com.hyunshop.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pages {
    private Long totalElements;
    private int totalPages;
    private int pageSize;
    private int page;

    public Pages() {
        this.totalElements = (long) 0;
    }

    public Pages(Long totalElements, Integer pageSize, Integer page) {
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.page = page;
        this.totalPages = (int) (totalElements % pageSize == 0 ? totalElements / pageSize : totalElements / pageSize + 1);
    }
}
