package com.example.Oathu2Jwt.Controller;


import com.example.Oathu2Jwt.Model.DTO.*;
import com.example.Oathu2Jwt.Model.Entity.*;
import com.example.Oathu2Jwt.Model.Entity.User.UserEntity;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Service.PostService;
import com.example.Oathu2Jwt.Service.UserService;
import com.example.Oathu2Jwt.Service.WorkPointService;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/employee")
public class UserController {
    private final Mapper<UserEntity, UserDTO> mapper;
    private final Mapper<WorkPoint,WorkPointDTO> workPointMapper;
    private final Mapper<UserInfoEntity,UserInfoDTO> userInfoMapper;
    private final Mapper<FriendListAndMutualFriend,FriendListAndMutualFriendDTO> friendAndMutualFriendMapper ;


    private final UserService userService;
    private final WorkPointService workPointService;

    @PatchMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateEmployee(@PathVariable("id") String id , @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(mapper.mapTo(userService.updateEmployee(id,mapper.mapFrom(userDTO))));
    }
    @PostMapping("/checkIn")
    public ResponseEntity<WorkPointDTO> checkIn(Principal principal) {
        return ResponseEntity.ok(workPointMapper.mapTo(workPointService.checkWorkPoint(principal.getName())));
    }

    @PostMapping("/addFriend/{username}")
    public ResponseEntity<?> addFriend(@PathVariable("username") String username ,Principal principal) {
         return ResponseEntity.ok(userService.addFriend(principal.getName(),username));
    }
    @DeleteMapping("/delete/friend-request/{userId}")
    public ResponseEntity<?> deleteFriendRequest(@PathVariable("userId") String userId ,Principal principal) {
        return ResponseEntity.ok(userService.deleteFriendRequest(principal.getName(),userId));
    }
    @GetMapping("/get/friend-requests")
    public ResponseEntity<?> getFriendRequests(Principal principal) {
            List<UserInfoEntity>userInfoEntities =userService.getAddFriendRequestList(principal.getName());
            return ResponseEntity.ok(userInfoEntities.stream().map(userInfoMapper::mapTo).collect(Collectors.toList()));
    }

    @PostMapping("/accept-friend/request/{userId}")
    public List<FriendListAndMutualFriendDTO> acceptFriendRequest(@PathVariable("userId") String userId,Principal principal) {
        return userService.acceptFriendRequest(principal.getName(),Long.parseLong(userId))
                .stream().map(friendAndMutualFriendMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/get/friendList")
    public ResponseEntity<?> getFriendList(Principal principal) {
            List<UserInfoDTO> friendList = userService.getFriendList(principal.getName())
                    .stream()
                    .map(userInfoMapper::mapTo)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(friendList);

    }

    @GetMapping("/get/friendListAndMutualFriend")
    public List<FriendListAndMutualFriendDTO> getFriendListAndMutualFriend(@RequestParam(required = false) String emailId ,Principal principal) {
        if(emailId != null) {
            return userService.getFriendListAndMutualFriend(emailId).stream()
                    .map(friendAndMutualFriendMapper::mapTo).collect(Collectors.toList());
        }
        return userService.getFriendListAndMutualFriend(principal.getName()).stream()
                .map(friendAndMutualFriendMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/get/searchResult")
    public ResponseEntity<Page<SearchedUserInfoDto>> getSearchResult(
            Principal principal,
            @RequestParam String userName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<SearchedUserInfoDto> searchResults = userService.getSearchResult(principal.getName(), userName, page, size);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/get/getRecommendedFriend")
    public List<FriendListAndMutualFriendDTO> createSocialGraph(Principal principal) {
        List<FriendListAndMutualFriend> users = userService.createSocialGraph(principal.getName());
        return users.stream().map(friendAndMutualFriendMapper::mapTo).collect(Collectors.toList());
    }



}
