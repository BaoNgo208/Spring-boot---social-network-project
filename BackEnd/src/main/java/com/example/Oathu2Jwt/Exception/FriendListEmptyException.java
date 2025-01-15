package com.example.Oathu2Jwt.Exception;


import com.example.Oathu2Jwt.Exception.BaseException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class FriendListEmptyException extends BaseException {
    public FriendListEmptyException(String msg) {
        super(msg);
    }
}
