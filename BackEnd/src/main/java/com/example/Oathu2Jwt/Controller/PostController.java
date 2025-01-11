package com.example.Oathu2Jwt.Controller;


import com.example.Oathu2Jwt.Model.DTO.*;
import com.example.Oathu2Jwt.Model.Entity.*;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Notification.CommentNotification;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Notification.Notification;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Notification.NotificationType;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Service.*;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/post")
public class PostController {
    private final Mapper<Post,PostDTO> postMapper;
    private final Mapper<Comment, CommentDTO> commentMapper;


    private final UserService userService;
    private final LikeService likeService;
    private final PostService postService;
    private final NotificationService notificationService;
    private final MinIOService minIOService;
    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> createPost(
            Principal principal,
            @RequestParam("post") String postJson,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            PostDTO postDTO = new ObjectMapper().readValue(postJson, PostDTO.class);

            Post post = postMapper.mapFrom(postDTO);

            Post createdPost = postService.createPost(principal.getName(), post);

            if (file != null && !file.isEmpty()) {
                String fileUrl = minIOService.upLoadFile(file, "mybucket");
                createdPost.setImageUrl(fileUrl);
                postService.savePost(createdPost);
            }

            return new ResponseEntity<>(postMapper.mapTo(createdPost), HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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



    //add postId attribute
    @PostMapping("/comment/{id}")
    public ResponseEntity<PostDTO> comment(Principal principal,
                                           @PathVariable("id") String id,
                                           @RequestBody CommentDTO commentDTO) {
        Comment comment = commentMapper.mapFrom(commentDTO);

        Post commentedPost = postService.comment(principal.getName(), id, comment);

        UserInfoEntity user = userService.getUserByEmail(principal.getName());

        CommentNotification newNotification = new CommentNotification(
                user.getId(),
                user.getEmployee().getUserName(),
                postService.getPostOwner(commentedPost.getId()).getId(),
                new Date(),
                NotificationType.COMMENT,
                commentedPost.getId(),
                commentMapper.mapTo(comment)
        );

        notificationService.saveNotification(newNotification);
        messagingTemplate.convertAndSend("/notifications/user", newNotification);
        return new ResponseEntity<>(postMapper.mapTo(commentedPost), HttpStatus.OK);
    }


    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable("postId") String postId,Principal principal)
    {
        try {
            Post post = postService.getPostById(postId);
            UserInfoEntity user = userService.getUserByEmail(principal.getName());
            Notification newNotification = Notification.builder()
                    .senderId(user.getId())
                    .senderName(user.getEmployee().getUserName())
                    .receiverId(postService.getPostOwner(post.getId()).getId())
                    .createdAt(new Date())
                    .type(NotificationType.LIKE)
                    .postId(Long.parseLong(postId))
                    .build();

            likeService.createLike(principal.getName(),postId);
            notificationService.saveNotification(newNotification);
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

    @GetMapping("/get/recommend/post")

    public ResponseEntity<Page<PostDTO>> getRecommendPosts(
            Principal principal,
            @RequestParam int page,
            @RequestParam int size) {
        Page<Post> posts = postService.getRecommendPosts(principal.getName(), page, size);
        Page<PostDTO> postDTOs = posts.map(postMapper::mapTo);
        postService.saveUserPostToRedisCache(principal.getName());
        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/get/profile")
    public ResponseEntity<Page<PostDTO>> getProfilePost(
            @RequestParam(required = false) String emailId ,
            Principal principal,
            @RequestParam int page,
            @RequestParam int size
    ) {
        if (emailId == null) {
            emailId = principal.getName();
        }
        Page<PostDTO> postDTOS = postService.getPostOfUser(emailId,page,size).map(postMapper::mapTo);
        return ResponseEntity.ok(postDTOS) ;
    }


    @GetMapping("/get/posts")
    public ResponseEntity<Page<PostDTO>> getPostOfUsersByUserName(
            @RequestParam String username,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<PostDTO> postDTOS = postService.getPostsByUserName(username,page,size).map(postMapper::mapTo);
        return ResponseEntity.ok(postDTOS);
    }

}
