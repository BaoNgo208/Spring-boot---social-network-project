package com.example.Oathu2Jwt.Exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class FriendRequestEmptyException extends BaseException{
    public FriendRequestEmptyException(String msg) {
        super(msg);
    }
}
