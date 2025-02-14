package com.quickpolls.controller;

import com.quickpolls.dto.response.ResponseBody;
import com.quickpolls.exception.AuthException;
import com.quickpolls.exception.NicknameAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NicknameAlreadyInUseException.class)
    public ResponseEntity<ResponseBody> handleUserAlreadyExistsException(NicknameAlreadyInUseException ex) {
        ResponseBody errorResponse = new ResponseBody(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody> handleGenericException(Exception ex) {
        ResponseBody responseBody = new ResponseBody(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ResponseBody> handleAuthException(AuthException e) {
        ResponseBody responseBody = new ResponseBody(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, String>> handleMissingRequestHeader(
            MissingRequestHeaderException ex) {

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", "Missing header: " + ex.getHeaderName());

        return ResponseEntity.badRequest().body(errorResponse);
    }

}

