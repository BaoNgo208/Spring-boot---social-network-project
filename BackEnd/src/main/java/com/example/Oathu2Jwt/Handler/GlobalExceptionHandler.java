package com.example.Oathu2Jwt.Handler;


import com.example.Oathu2Jwt.Exception.*;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.AlreadyBoundException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMsg());
    }

    @ExceptionHandler(FriendListEmptyException.class)
    public ResponseEntity<?> handleFriendListEmpty(FriendListEmptyException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", exception.getMessage()));
    }
    @ExceptionHandler(FriendRequestEmptyException.class)
    public ResponseEntity<?> handleFriendRequestEmpty(FriendRequestEmptyException exception) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @ExceptionHandler(FriendRequestAlreadySentException.class)
    public ResponseEntity<?> handleFriendRequestAlreadySent(FriendRequestAlreadySentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(RedisOperationException.class)
    public ResponseEntity<String> handleRedisOperationException(RedisOperationException ex) {
        System.err.println("Redis error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Redis operation failed: " + ex.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormatException(NumberFormatException e) {
        return ResponseEntity.badRequest().body("Invalid userId format: " + e.getMessage());
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<String> handleRedisConnectionFailure(RedisConnectionFailureException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Redis connection error: " + e.getMessage());
    }


    @ExceptionHandler(AlreadyAcceptedFriendRequestException.class)
    public ResponseEntity<?> handleAlreadyAcceptedFriendRequest(AlreadyAcceptedFriendRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }

}
