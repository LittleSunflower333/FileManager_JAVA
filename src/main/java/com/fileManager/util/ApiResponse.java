package com.fileManager.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private String message;
    private T data;
    private int statusCode;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("操作成功", data, 200);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null, 400);
    }
}
