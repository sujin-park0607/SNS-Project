package com.likelion.finalproject.service;

import org.springframework.stereotype.Service;

@Service
public class BasicService {

    public Integer sum(Integer number) {
        Integer result = 0;
        while(number > 0){
            result += number % 10;
            number /= 10;
        }
        return result;
    }
}
