package com.syu.capstone_stock.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(boolean success, int status, T data, String message) {

    public static <T> ApiResponse<T> createSuccess(T data) {
        return new ApiResponse<>(true, HttpStatus.OK.value(), data, null);
    }

    public static <T> ApiResponse<T> createSuccess() {
        return new ApiResponse<>(true, HttpStatus.OK.value(), null, null);
    }

    public static <T> ApiResponse<T> createFail(int status, String message) {
        return new ApiResponse<>(false, status, null, message);
    }
}
