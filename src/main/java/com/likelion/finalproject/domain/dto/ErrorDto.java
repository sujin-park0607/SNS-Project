package com.likelion.finalproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDto {
    private String errorCode;
    private String message;
}
