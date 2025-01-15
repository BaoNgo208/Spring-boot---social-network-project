package com.example.Oathu2Jwt.Exception;

import com.example.Oathu2Jwt.Exception.BaseException;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}