package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.Entity.TokenType;
import com.example.Oathu2Jwt.Repository.RefreshTokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {
    private final RefreshTokenRepo refreshTokenRepo;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader  = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!authHeader.startsWith(TokenType.Bearer.name())) {
            return;
        }
        final String refreshToken = authHeader.substring(7);
        System.out.println("refresh token nhan duoc tu header chung :" + refreshToken );
        var storedRefreshToken = refreshTokenRepo.findByRefreshToken(refreshToken)
                .map(token -> {
                    token.setRevoked(true);
                    refreshTokenRepo.save(token);
                    System.out.println("ton tai trong db");
                    return token;
                }).orElse(null);

    }
}
