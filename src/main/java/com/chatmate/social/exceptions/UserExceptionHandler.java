package com.chatmate.social.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> handleAnyException(Exception e) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                e.getMessage(),
//                System.currentTimeMillis());
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(EtAuthException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

}
