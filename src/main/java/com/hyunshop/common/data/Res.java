package com.hyunshop.common.data;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class Res<T> {
    private T data;
    private String message;
    private HttpStatus status;
    private Long timeStamp = new Date().getTime();

    public Res(T data, String message, HttpStatus status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public Res(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
