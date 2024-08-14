package com.example.Oathu2Jwt.Controller;

import com.example.Oathu2Jwt.Model.DTO.UserInfoDTO;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Service.Impl.AuthService;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final Mapper<UserInfoEntity,UserInfoDTO> mapper;
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(Authentication authentication, HttpServletResponse response ){
        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication,response));
    }

    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }
    @GetMapping("/get/refresh-token")
    public Boolean getRefreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        return authService.getRefreshTokenState(refreshToken);
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserInfoDTO userInfoDTO,
                                        HttpServletResponse httpServletResponse){

        log.info("[AuthController:registerUser]Signup Process Started for user:{}",userInfoDTO.getEmployee().getUserName());
        return ResponseEntity.ok(authService.registerUser(mapper.mapFrom(userInfoDTO) ,httpServletResponse));
    }
}
