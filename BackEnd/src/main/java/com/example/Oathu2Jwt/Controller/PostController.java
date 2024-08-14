package com.example.Oathu2Jwt.Controller;


import com.example.Oathu2Jwt.Model.DTO.*;
import com.example.Oathu2Jwt.Model.Entity.*;
import com.example.Oathu2Jwt.Model.Entity.Notification.CommentNotification;
import com.example.Oathu2Jwt.Model.Entity.Notification.Notification;
import com.example.Oathu2Jwt.Model.Entity.Notification.NotificationType;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Service.EmployeeService;
import com.example.Oathu2Jwt.Service.LikeService;
import com.example.Oathu2Jwt.Service.PostService;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/post")
public class PostController {
    private final Mapper<Post,PostDTO> postMapper;
    private final Mapper<Comment, CommentDTO> commentMapper;


    private final EmployeeService employeeService;
    private final LikeService likeService;
    private final PostService postService;

    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(Principal principal,
                                              @RequestBody PostDTO postDTO ) {
        Post post = postMapper.mapFrom(postDTO);
        System.out.println("principal name:" + principal.getName());
        Post createdPost = postService.createPost(principal.getName(),post);
        return new ResponseEntity<PostDTO>(postMapper.mapTo(createdPost), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")

    public ResponseEntity<PostDTO> getPost(@PathVariable("id") String id ) {
        return ResponseEntity.ok(postMapper.mapTo(postService.getPostById(id)));
    }


    @PatchMapping("/update/{id}")

    public ResponseEntity<PostDTO> updatePost(@PathVariable("id") String id ,@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postMapper.mapTo(postService.updatePost(id,postMapper.mapFrom(postDTO))));
    }

    @PatchMapping("/update/{id}/comment/{commentId}")

    public ResponseEntity<PostDTO> updateComment(@PathVariable("id") String id ,
                                                 @PathVariable("commentId") String commentId,
                                                 @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(postMapper.mapTo(postService.updateComment(id,commentId,commentMapper.mapFrom(commentDTO))));
    }

    @GetMapping("/get/recommend/post")

    public ResponseEntity<Page<PostDTO>> getRecommendPosts(
            Principal principal,
            @RequestParam int page,
            @RequestParam int size) {
        Page<Post> posts = postService.getRecommendPosts(principal.getName(), page, size);
        Page<PostDTO> postDTOs = posts.map(postMapper::mapTo);
        return ResponseEntity.ok(postDTOs);
    }


    @PostMapping("/comment/{id}")
    public ResponseEntity<PostDTO> comment(Principal principal,
                                           @PathVariable("id") String id,
                                           @RequestBody CommentDTO commentDTO) {
        Comment comment = commentMapper.mapFrom(commentDTO);

        Post commentedPost = postService.comment(principal.getName(), id, comment);

        UserInfoEntity user = employeeService.getUserByEmail(principal.getName());

        CommentNotification newNotification = new CommentNotification(
                user.getId(),
                user.getEmployee().getUserName(),
                postService.getPostOwner(commentedPost.getId()).getId(),
                NotificationType.COMMENT,
                commentedPost.getId(),
                commentMapper.mapTo(comment)
        );

        messagingTemplate.convertAndSend("/notifications/user", newNotification);

        return new ResponseEntity<>(postMapper.mapTo(commentedPost), HttpStatus.OK);
    }


    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable("postId") String postId,Principal principal)
    {
        try {
            Post post = postService.getPostById(postId);
            UserInfoEntity user = employeeService.getUserByEmail(principal.getName());
            Notification newNotification = new Notification(
                    user.getId(),
                    user.getEmployee().getUserName(),
                    postService.getPostOwner(post.getId()).getId(),
                    NotificationType.LIKE
            );
            likeService.createLike(principal.getName(),postId);

            messagingTemplate.convertAndSend("/notifications/user", newNotification);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/cancelLike/{postId}")
    public ResponseEntity<?> cancelLikePost(@PathVariable("postId") String postId,Principal principal)
    {
        try {
            likeService.removeLike(principal.getName(),postId);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




}
