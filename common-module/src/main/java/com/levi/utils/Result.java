package com.levi.utils;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private static final String SUCCESS_MESSAGE = "success";
    private static final String FAILED_MESSAGE = "failed";
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(Integer code, String message, T data) {
        return new Result<T>(code, message, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return success(HttpStatus.HTTP_OK, message, data);
    }

    public static <T> Result<T> success(T data) {
        return success(SUCCESS_MESSAGE, data);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> failed(Integer code, String message, T data) {
        return new Result<T>(code, message, data);
    }

    public static <T> Result<T> failed(String message, T data) {
        return failed(HttpStatus.HTTP_INTERNAL_ERROR, message, data);
    }

    public static <T> Result<T> failed(T data) {
        return failed(FAILED_MESSAGE, data);
    }

    public static <T> Result<T> failed() {
        return failed(null);
    }

}
