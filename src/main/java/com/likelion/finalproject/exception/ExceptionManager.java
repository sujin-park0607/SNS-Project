package com.likelion.finalproject.exception;

import com.likelion.finalproject.domain.dto.ErrorDto;
import com.likelion.finalproject.domain.entity.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> joinExceptionHandler(AppException e){
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(new ErrorDto(e.getErrorCode().name(), e.getMessage())));
    }
}
