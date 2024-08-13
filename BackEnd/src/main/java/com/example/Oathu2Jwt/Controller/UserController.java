package com.example.Oathu2Jwt.Controller;


import com.example.Oathu2Jwt.Model.DTO.*;
import com.example.Oathu2Jwt.Model.Entity.*;
import com.example.Oathu2Jwt.Service.EmployeeService;
import com.example.Oathu2Jwt.Service.PostService;
import com.example.Oathu2Jwt.Service.WorkPointService;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/employee")
public class UserController {
    private final Mapper<EmployeeEntity,EmployeeDTO> mapper;
    private final Mapper<Post,PostDTO> postMapper;
    private final Mapper<WorkPoint,WorkPointDTO> workPointMapper;
    private final Mapper<UserInfoEntity,UserInfoDTO> userInfoMapper;
    private final Mapper<FriendListAndMutualFriend,FriendListAndMutualFriendDTO> friendAndMutualFriendMapper ;


    private final EmployeeService employeeService;
    private final WorkPointService workPointService;
    private final PostService postService;

    @PatchMapping("/update/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee( @PathVariable("id") String id , @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(mapper.mapTo(employeeService.updateEmployee(id,mapper.mapFrom(employeeDTO))));
    }
    @PostMapping("/checkIn")
    public ResponseEntity<WorkPointDTO> checkIn(Principal principal) {
        return ResponseEntity.ok(workPointMapper.mapTo(workPointService.checkWorkPoint(principal.getName())));
    }



    @PostMapping("/addFriend/{username}")
    public ResponseEntity<?> addFriend(@PathVariable("username") String username ,Principal principal) {
         return ResponseEntity.ok(employeeService.addFriend(principal.getName(),username));
    }
    @DeleteMapping("/delete/friend-request/{userId}")
    public ResponseEntity<?> deleteFriendRequest(@PathVariable("userId") String userId ,Principal principal) {
        return ResponseEntity.ok(employeeService.deleteFriendRequest(principal.getName(),userId));
    }
    @GetMapping("/get/friend-requests")
    public List<UserInfoDTO> getFriendRequests(Principal principal) {
            List<UserInfoEntity>userInfoEntities =employeeService.getAddFriendRequestList(principal.getName());
            return userInfoEntities.stream().map(userInfoMapper::mapTo).collect(Collectors.toList());
    }

    @PostMapping("/accept-friend/request/{userId}")
    public List<FriendListAndMutualFriendDTO> acceptFriendRequest(@PathVariable("userId") String userId,Principal principal) {
        return employeeService.acceptFriendRequest(principal.getName(),Long.parseLong(userId))
                .stream().map(friendAndMutualFriendMapper::mapTo).collect(Collectors.toList());
    }
    @GetMapping("/get/profile")
    public List<PostDTO> getProfilePost(@RequestParam(required = false) String emailId ,Principal principal) {
        if (emailId == null) {
            emailId = principal.getName();
        }
        return postService
                .getPostOfUser(emailId)
                .stream().map(postMapper::mapTo)
                .collect(Collectors.toList());
    }
    @GetMapping("/get/friendList")
    public List<UserInfoDTO> getFriendList(Principal principal) {
        return employeeService.getFriendList(principal.getName() ).stream().map(userInfoMapper::mapTo).collect(Collectors.toList());
    }
    @GetMapping("/get/friendListAndMutualFriend")
    public List<FriendListAndMutualFriendDTO> getFriendListAndMutualFriend(@RequestParam(required = false) String emailId ,Principal principal) {
        if(emailId != null) {
            return employeeService.getFriendListAndMutualFriend(emailId).stream()
                    .map(friendAndMutualFriendMapper::mapTo).collect(Collectors.toList());
        }
        return employeeService.getFriendListAndMutualFriend(principal.getName()).stream()
                .map(friendAndMutualFriendMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/get/searchResult/{userName}")
    public List<UserInfoDTO> getSearchResult(@PathVariable("userName") String userName) {
        List<UserInfoEntity> searchResults = employeeService.getSearchResult(userName);
        return searchResults.stream().map(userInfoMapper::mapTo).collect(Collectors.toList());
    }
    @GetMapping("/get/getRecommendedFriend")
    public List<FriendListAndMutualFriendDTO> createSocialGraph(Principal principal) {
        List<FriendListAndMutualFriend> users = employeeService.createSocialGraph(principal.getName());
        return users.stream().map(friendAndMutualFriendMapper::mapTo).collect(Collectors.toList());
    }



}
