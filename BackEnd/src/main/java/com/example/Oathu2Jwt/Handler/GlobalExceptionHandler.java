package com.example.Oathu2Jwt.Handler;


import com.example.Oathu2Jwt.Exception.*;
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
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An unexpected error occurred"));
    }

    @ExceptionHandler(FriendRequestAlreadySentException.class)
    public ResponseEntity<?> handleFriendRequestAlreadySent(FriendRequestAlreadySentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(AlreadyAcceptedFriendRequestException.class)
    public ResponseEntity<?> handleAlreadyAcceptedFriendRequest(AlreadyAcceptedFriendRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }

}
