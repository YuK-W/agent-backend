package com.yuke.agentbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> CommonResponse<T> error(Integer code, String message) {
        return CommonResponse.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> CommonResponse<T> error(String message) {
        return error(500, message);
    }
}