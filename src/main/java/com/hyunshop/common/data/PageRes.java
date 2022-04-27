package com.hyunshop.common.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageRes <T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;

    public PageRes() {
    }

    public PageRes(List<T> data, int totalPages, long totalElements, boolean hasNext) {
        this.data = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.hasNext = hasNext;
    }

    public PageRes(boolean isEmpty) {
        this.data = new ArrayList<>();
        this.totalPages = 0;
        this.totalElements = 0;
        this.hasNext = false;
    }

    public PageRes(List<T> data, int totalPages, long totalElements, boolean hasNext, boolean hasPrevious) {
        this.data = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }
}
