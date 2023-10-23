package com.danolog.api.controller;

import com.danolog.api.exception.DanologException;
import com.danolog.api.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DanologException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
          .code("400")
          .message("잘못된 요청입니다.")
          .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ResponseBody
    @ExceptionHandler(DanologException.class)
    public ResponseEntity<ErrorResponse> danologException(DanologException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
          .code(String.valueOf(statusCode))
          .message(e.getMessage())
          .build();

      return ResponseEntity.status(statusCode)
          .body(errorResponse);
    }
}
