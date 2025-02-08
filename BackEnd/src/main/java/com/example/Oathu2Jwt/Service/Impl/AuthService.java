package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Config.JwtConfig.JwtTokenGenerator;
import com.example.Oathu2Jwt.Exception.UserNotFoundException;
import com.example.Oathu2Jwt.Model.DTO.*;
import com.example.Oathu2Jwt.Model.Entity.*;
import com.example.Oathu2Jwt.Model.Entity.User.UserEntity;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Repository.RefreshTokenRepo;
import com.example.Oathu2Jwt.Repository.UserInfoRepo;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserInfoRepo userInfoRepo;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
    private final Mapper<UserEntity, UserDTO> mapper;


    private void saveUserRefreshToken(UserInfoEntity userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication , HttpServletResponse response) {
        try
        {
            var userInfoEntity = userInfoRepo.findByEmailId(authentication.getName())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User not found hehe"
                    ));
            UserInfoDTO2 userInfoDTO = new UserInfoDTO2(
                    userInfoEntity.getId(),
                    userInfoEntity.getEmailId(),
                    userInfoEntity.getAccName(),
                    mapper.mapTo(userInfoEntity.getEmployee())
            );
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
            saveUserRefreshToken(userInfoEntity,refreshToken);
            creatRefreshTokenCookie(response,refreshToken);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",userInfoEntity.getEmployee().getUserName());
            return  AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(15 * 60)
                    .userName(userInfoEntity.getEmployee().getUserName())
                    .userId(userInfoEntity.getId())
                    .accName(userInfoEntity.getAccName())
                    .userInfo(userInfoDTO)
                    .refreshToken(refreshToken)
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please Try Again");
        }
    }
    public UserInfoEntity getUser(String emailId) {
        return  userInfoRepo.findByEmailId(emailId).get();
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {

        if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);


        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(tokens-> !tokens.isRevoked())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        UserInfoEntity userInfoEntity = refreshTokenEntity.getUser();


        Authentication authentication =  createAuthenticationObject(userInfoEntity);

        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return  AuthResponseDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .userName(userInfoEntity.getEmployee().getUserName())
                .tokenType(TokenType.Bearer)
                .build();
    }

    private static Authentication createAuthenticationObject(UserInfoEntity userInfoEntity) {
        String username = userInfoEntity.getEmailId();
        String password = userInfoEntity.getPassword();
        String roles = userInfoEntity.getRoles();

        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    public AuthResponseDto  registerUser(UserInfoEntity userInfoEntity, HttpServletResponse httpServletResponse) {
        try{
            log.info("[AuthService:registerUser]User Registration Started with :::{}",userInfoEntity.getEmailId());

            Optional<UserInfoEntity> user = userInfoRepo.findByEmailId(userInfoEntity.getEmailId());
            if(user.isPresent()){
                throw new Exception("User Already Exist");
            }

            Authentication authentication = createAuthenticationObject(userInfoEntity);
            System.out.println("user auth: " + authentication.getName());
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            userInfoEntity.getEmployee().setUserInfo(userInfoEntity);
            UserInfoEntity savedUserInfo = userInfoRepo.save(userInfoEntity);

            saveUserRefreshToken(savedUserInfo,refreshToken);

//            creatRefreshTokenCookie(httpServletResponse,refreshToken);

            log.info("[AuthService:registerUser] User:{} Successfully registered",savedUserInfo.getEmployee().getUserName());
            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserInfo.getEmployee().getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
    public Boolean getRefreshTokenState(String header) {
        final String refreshToken = header.substring(7);
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken).get();
         return  refreshTokenEntity.isRevoked();
    }
}
