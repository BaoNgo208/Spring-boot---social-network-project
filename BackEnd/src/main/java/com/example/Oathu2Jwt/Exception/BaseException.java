package com.example.Oathu2Jwt.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {
    private final String msg;

    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
    }
}