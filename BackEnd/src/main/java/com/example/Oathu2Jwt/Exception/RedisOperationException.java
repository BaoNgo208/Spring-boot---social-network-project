package com.example.Oathu2Jwt.Exception;


import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class RedisOperationException extends BaseException{
    public RedisOperationException(String msg) {
        super(msg, "REDIS_OPERATION_ERROR");
    }

    public RedisOperationException(String msg,Throwable cause) {
        super(msg, cause, "REDIS_OPERATION_ERROR");
    }
}
