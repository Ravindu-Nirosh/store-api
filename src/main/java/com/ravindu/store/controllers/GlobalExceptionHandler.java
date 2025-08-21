package com.ravindu.store.controllers;

import com.ravindu.store.dtos.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnreadableMessage(){
        return ResponseEntity.badRequest().body(
               new ErrorDto("invalid request body")
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationErrors(MethodArgumentNotValidException exception){
        var errors = new HashMap<String,String>();
        exception.getBindingResult().getFieldErrors().forEach(e->errors.put(e.getField(),e.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
}
