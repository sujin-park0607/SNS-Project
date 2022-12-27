package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.entity.Response;
import com.likelion.finalproject.domain.dto.*;
import com.likelion.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        UserDto user = userService.join(request);
        return Response.success(new UserJoinResponse(user.getUserId(), user.getUserName()));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request.getUserName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

}
