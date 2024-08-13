package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.Entity.LikeEntity;
import com.example.Oathu2Jwt.Model.Entity.Post;
import com.example.Oathu2Jwt.Repository.LikeRepo;
import com.example.Oathu2Jwt.Repository.PostRepo;
import com.example.Oathu2Jwt.Repository.UserInfoRepo;
import com.example.Oathu2Jwt.Service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class LikeServiceImp implements LikeService {
    private final LikeRepo likeRepository;
    private final PostRepo postRepo;
    private final UserInfoRepo userInfoRepo;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public void createLike(String userEmail,String postId) {
        likeRepository.save(new LikeEntity(
                null,
                userInfoRepo.findByEmailId(userEmail)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found")),
                postRepo.findById(Long.parseLong(postId))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found")),
                java.sql.Timestamp.valueOf(LocalDateTime.now().format(formatter))
        ));
    }

    @Override
    public void removeLike(String userEmail,String postId) {
        LikeEntity existedLike = likeRepository.findByPostIdAndUserInfoId(
                Long.parseLong(postId),
                userInfoRepo.findByEmailId(userEmail)
                        .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found"))
                        .getId()
                );
        System.out.println("existed like:" + existedLike.getId());
        likeRepository.delete(existedLike);
    }
}
