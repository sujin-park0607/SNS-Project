package com.likelion.finalproject.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicServiceTest {
    BasicService basicService = new BasicService();

    @Test
    @DisplayName("자릿수 합 잘 구하는지")
    void sumOfDigit() {
        assertEquals(21, basicService.sum(687));
        assertEquals(22, basicService.sum(787));
        assertEquals(0, basicService.sum(0));
        assertEquals(5, basicService.sum(11111));
    }

}