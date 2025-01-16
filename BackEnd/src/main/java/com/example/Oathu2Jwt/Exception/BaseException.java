package com.example.Oathu2Jwt.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {
    private final String msg;
    private final String errorCode;
    public BaseException(String errorCode,String msg) {
        super(msg);
        this.msg = msg;
        this.errorCode = errorCode;
    }
    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
        this.errorCode = "UNKNOWN";
    }


    public BaseException(String msg, Throwable cause, String errorCode) {
        super(msg, cause);
        this.msg = msg;
        this.errorCode = errorCode;
    }


}