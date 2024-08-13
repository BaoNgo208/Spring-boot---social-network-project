package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.Entity.Comment;
import com.example.Oathu2Jwt.Model.Entity.Post;
import com.example.Oathu2Jwt.Model.Entity.UpdateHistory;
import com.example.Oathu2Jwt.Model.Entity.UserInfoEntity;
import com.example.Oathu2Jwt.Repository.*;
import com.example.Oathu2Jwt.Service.EmployeeService;
import com.example.Oathu2Jwt.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepo postRepo ;
    private final UserInfoRepo userInfoRepo;
    private final CommentRepo commentRepo;
    private final EmployeeService employeeService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public UserInfoEntity getPostOwner(Long postId) {
        return postRepo.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"post not found"))
                .getUser();
    }

    @Override
    public Post getPostById(String id) {
        return postRepo.findById(Long.parseLong(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"post not found"));
    }



    @Override
    @Transactional
    public Post createPost(String username, Post post) {
        UserInfoEntity user = userInfoRepo.findByEmailId(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found ")
        );
        post.setComment(null);
        post.setPostTime(java.sql.Timestamp.valueOf(LocalDateTime.now().format(formatter)));
        post.setUser(user);
        return postRepo.save(post);
    }

    @Override
    public Post updatePost(String postId, Post post) {
        return postRepo.findById(Long.parseLong(postId)).map(existingPost -> {
            Optional.ofNullable(post.getContent()).ifPresent(existingPost::setContent);
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setContent(post.getContent());
            updateHistory.setUpdateTime(java.sql.Timestamp.valueOf(LocalDateTime.now().format(formatter)));
            updateHistory.setPost(existingPost);
            existingPost.getUpdateHistories().add(updateHistory);
            return postRepo.save(existingPost);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"post not found"));
    }

    @Override
    public Post comment(String username, String postId, Comment comment) {
        UserInfoEntity user = userInfoRepo.findByEmailId(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found ")
        );
        Post post = getPostById(postId);
        comment.setCommentTime(java.sql.Timestamp.valueOf(LocalDateTime.now().format(formatter)));
        comment.setUser(user);
        comment.setPost(post);
        commentRepo.save(comment);

        return post;
    }
    @Override
    public Post updateComment(String postId,String commentId,Comment comment) {
        commentRepo.findById(Long.parseLong(commentId)).map(existingComment -> {
            Optional.ofNullable(comment.getContent()).ifPresent(existingComment::setContent);
            UpdateHistory updateHistory = new UpdateHistory();
            updateHistory.setContent(comment.getContent());
            updateHistory.setUpdateTime(java.sql.Timestamp.valueOf(LocalDateTime.now().format(formatter)));
            updateHistory.setComment(existingComment);
            existingComment.getUpdateHistories().add(updateHistory);
            return  commentRepo.save(existingComment);
        });

        return postRepo.findById(Long.parseLong(postId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<Post> getRecommendPosts(String emailId, int page, int size) {
        List<UserInfoEntity> userAndFriends = employeeService.getFriendList(emailId);
        userAndFriends.add(
                userInfoRepo.findByEmailId(emailId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"))
        );

        List<Long> userIds = userAndFriends.stream()
                .map(UserInfoEntity::getId)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        return postRepo.findByUserIdIn(userIds, pageable);
    }

    @Override
    public List<Post> getPostOfUser(String emailId) {
        return postRepo.findByUserId(userInfoRepo.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("Error:Not Found this user"))
                .getId());
    }

}
