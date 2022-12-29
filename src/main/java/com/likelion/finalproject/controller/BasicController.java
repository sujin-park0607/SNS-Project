package com.likelion.finalproject.controller;

import com.likelion.finalproject.service.BasicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
@RequiredArgsConstructor
public class BasicController {

    private final BasicService basicService;

    @GetMapping(produces = "application/json; charset=utf8")
    public String hello() {
        return "박수진";
    }

    @GetMapping(value = "/{number}")
    public Integer sumOfDigit(@PathVariable Integer number) {
        Integer sum = basicService.sum(number);
        return sum;
    }
}
