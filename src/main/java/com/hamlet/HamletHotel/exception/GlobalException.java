package com.hamlet.HamletHotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



public class GlobalException extends RuntimeException{
    public GlobalException (String message){
        super(message);
    }

//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
}
