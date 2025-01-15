package com.example.Oathu2Jwt.Exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)

public class FriendRequestAlreadySentException extends BaseException{
    public FriendRequestAlreadySentException(String msg) {
        super(msg);
    }
}
