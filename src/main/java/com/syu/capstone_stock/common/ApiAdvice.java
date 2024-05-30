package com.syu.capstone_stock.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
public class ApiAdvice {
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<?> handleIllegalArgumentException(ApiException e){
        return ResponseEntity.status(e.getHttpStatus()).body(ApiResponse.createFail(e.getHttpStatus().value(), e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex){
        ArrayList<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            String errorMsgFormat = fieldName + errorMessage;
            errors.add(errorMsgFormat);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createFail(HttpStatus.BAD_REQUEST.value(), String.join(", ", errors)));
    }
}
