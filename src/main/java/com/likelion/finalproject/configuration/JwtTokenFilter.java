package com.likelion.finalproject.configuration;

import com.likelion.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;

//@RequiredArgsConstructor
//@Slf4j
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//    private final UserService userService;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        log.info("authorizationHeader:{}",authorizationHeader);
//    }
//}
