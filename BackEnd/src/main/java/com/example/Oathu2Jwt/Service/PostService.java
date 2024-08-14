package com.example.Oathu2Jwt.Service;

import com.example.Oathu2Jwt.Model.Entity.Comment;
import com.example.Oathu2Jwt.Model.Entity.Post;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    public UserInfoEntity getPostOwner(Long postId);

    public Post getPostById(String id);

    public Post createPost(String id , Post post);
    public Post updatePost(String postId,Post post ) ;
    public Post comment(String username , String  postId, Comment comment);
    public Post updateComment(String postId,String commentId,Comment comment);

    public Page<Post> getRecommendPosts(String emailId, int page, int size);
    public List<Post> getPostOfUser(String emailId);
}
