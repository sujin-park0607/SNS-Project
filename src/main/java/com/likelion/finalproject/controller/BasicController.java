package com.likelion.finalproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class BasicController {

    @GetMapping("/happy_new_year")
    public String hello(){
        return "happy_new_year";
    }
}
