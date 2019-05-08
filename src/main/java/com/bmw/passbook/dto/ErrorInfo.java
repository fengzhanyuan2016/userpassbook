package com.bmw.passbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo<T> {
    public static final Integer ERROR = -1;
    private Integer code;
    private String messge;
    private String url;
    private T data;
}
