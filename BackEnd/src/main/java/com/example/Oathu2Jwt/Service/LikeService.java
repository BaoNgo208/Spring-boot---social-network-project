package com.example.Oathu2Jwt.Service;

import com.example.Oathu2Jwt.Model.Entity.LikeEntity;

public interface LikeService {
    public void createLike(String userEmail,String postId);
    public void removeLike(String userEmail ,String postId);
}
