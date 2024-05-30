package com.syu.capstone_stock.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException{
    private final HttpStatus httpStatus;

    public ApiException(String errorMsg, HttpStatus httpStatus){
        super(errorMsg);
        this.httpStatus = httpStatus;
    }
}
