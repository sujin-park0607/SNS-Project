package com.likelion.finalproject.configuration;

import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.service.UserService;
import com.likelion.finalproject.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Value("${jwt.token.secret}")
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorizationHeader:{}",authorizationHeader);

        if(authorizationHeader == null){
            log.info("not exist token");
            request.setAttribute("exception",ErrorCode.INVALID_PERMISSION.name());
            filterChain.doFilter(request, response);
            return;
        }

        if(!authorizationHeader.startsWith("Bearer ")){
            log.info("not exist Bearer");
            request.setAttribute("exception", ErrorCode.INVALID_TOKEN.name());
            filterChain.doFilter(request, response);
            return;
        }

        final String token;
        //token분리
        try{
            token = authorizationHeader.split(" ")[1];

            //token만료 체크
            if(JwtTokenUtil.isExpired(token, secretKey)){
                request.setAttribute("exception",ErrorCode.INVALID_TOKEN.name());
                filterChain.doFilter(request,response);
                return;
            };

            //userName 꺼내기
            String userName = JwtTokenUtil.getUserName(token, secretKey);
            log.info("userName:{}", userName);
            //userDetail가져오기
            User user = userService.getUserByUserName(userName);
            log.info("userRole:{}", user.getRole());

            //권한여부경정
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority(user.getRole().name())));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);

        }catch(Exception e){
            request.setAttribute("exception",ErrorCode.INVALID_TOKEN.name());
            filterChain.doFilter(request,response);
        }

    }
}

