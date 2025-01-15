package com.example.Oathu2Jwt.Exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class AlreadyAcceptedFriendRequestException extends BaseException{
    public AlreadyAcceptedFriendRequestException(String msg) {
        super(msg);
    }
}
