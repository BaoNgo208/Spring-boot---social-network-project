package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Exception.User.UserNotFoundException;
import com.example.Oathu2Jwt.Model.DTO.SearchedUserInfoDto;
import com.example.Oathu2Jwt.Model.DTO.UserDTO;
import com.example.Oathu2Jwt.Model.Entity.*;
import com.example.Oathu2Jwt.Model.Entity.User.UserEntity;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Model.Entity.User.UserRelationship;
import com.example.Oathu2Jwt.Repository.*;
import com.example.Oathu2Jwt.Service.UserService;
import com.example.Oathu2Jwt.Util.Graph.Graph;
import com.example.Oathu2Jwt.Util.Graph.Vertex;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.stream.Collectors;

import com.example.Oathu2Jwt.Util.Graph.Queue;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final SalaryRepo salaryRepo;
    private final UserInfoRepo userInfoRepo;
    @Qualifier("jdkRedisTemplate")
    private final RedisTemplate<String,Object> redisTemplate;
    private final UserRelationshipRepo userRelationshipRepo;
    private final Mapper<UserEntity, UserDTO> userDTOMapper;

    public UserServiceImpl(UserRepo userRepo, SalaryRepo salaryRepo, UserInfoRepo userInfoRepo,
                           UserRelationshipRepo userRelationshipRepo,
                           @Qualifier("jdkRedisTemplate") RedisTemplate<String, Object> redisTemplate,
                           Mapper<UserEntity,UserDTO> userDTOMapper) {
        this.userRepo = userRepo;
        this.salaryRepo = salaryRepo;
        this.userInfoRepo = userInfoRepo;
        this.userRelationshipRepo = userRelationshipRepo;
        this.redisTemplate = redisTemplate;
        this.userDTOMapper = userDTOMapper;
    }
    @Override
    public UserInfoEntity getUserByEmail(String email) {
        return  userInfoRepo.findByEmailId(email)
                .orElseThrow(()-> new UserNotFoundException("user not found"));
    }

    @Override
    @Transactional
    public UserEntity updateEmployee(String id, UserEntity user) {
            Salary salary = salaryRepo.findBySalary(user.getSalary().getSalary());
            return userRepo.findById(Long.parseLong(id)).map(existingEmployee -> {
                Optional.ofNullable(user.getDateOfBirth()).ifPresent(existingEmployee::setDateOfBirth);
                Optional.ofNullable(user.getMobileNumber()).ifPresent(existingEmployee::setMobileNumber);
                Optional.ofNullable(user.getUserName()).ifPresent(existingEmployee::setUserName);
                existingEmployee.setSalary(salary);
                return userRepo.save(existingEmployee);
            }).orElseThrow(() -> new UserNotFoundException("User not found"));

    }


    @Override
    public List<UserInfoEntity> getAddFriendRequestList(String email) {
            UserInfoEntity userInfo = userInfoRepo.findByEmailId(email)
                    .orElseThrow(
                            () -> new UserNotFoundException("user not found ")
                    );
            List<UserRelationship> userRelationshipList = userRelationshipRepo.findByAddFriendRequest(userInfo.getId());
            if(userRelationshipList.isEmpty()) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            List<UserInfoEntity> addFriendRequests= new ArrayList<>();
            for(UserRelationship userRelationship : userRelationshipList) {
                if(Objects.equals(userRelationship.getUserFirstId().getId(), userInfo.getId()) ) {
                    addFriendRequests.add(userRelationship.getUserSecondId());
                }
                else {
                    addFriendRequests.add(userRelationship.getUserFirstId());
                }
            }
            return addFriendRequests;
    }

    public Boolean checkFriendRequestState(UserInfoEntity user1,UserInfoEntity user2) {
        UserRelationship userRelationship ;
        if(user1.getId() < user2.getId()) {
            userRelationship = userRelationshipRepo.findByUserFirstId_IdAndUserSecondId_Id(user1.getId(), user2.getId());
            if ( userRelationship == null )  return true;
            if(userRelationship.getType() == Type.PENDING_FIRST_SECOND) return false;
            return userRelationshipRepo.findByUserFirstId_IdAndUserSecondId_Id(user1.getId(), user2.getId()).getType() != Type.FRIENDS;
        }
        else {
            userRelationship = userRelationshipRepo.findByUserFirstId_IdAndUserSecondId_Id(user2.getId(), user1.getId());
            if ( userRelationship == null )  return true;
            if(userRelationship.getType() == Type.PENDING_SECOND_FIRST) return false;
            return userRelationshipRepo.findByUserFirstId_IdAndUserSecondId_Id(user2.getId(), user1.getId()).getType() != Type.FRIENDS;
        }
    }
    @Override
    public String addFriend(String user,String accName) {

        UserInfoEntity pendingFirst = userInfoRepo.findByEmailId(user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found")
        );
        UserInfoEntity pendingSecond= userInfoRepo.findByAccName(accName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found")
        );
        UserRelationship userRelationship = new UserRelationship();
        if(!checkFriendRequestState(pendingFirst, pendingSecond)   ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"request is already sent");
        }
        else {
            if(pendingFirst.getId() > pendingSecond.getId()) {
                userRelationship.setUserFirstId(pendingSecond);
                userRelationship.setUserSecondId(pendingFirst);
                System.out.println("lon hon");
                userRelationship.setType(Type.PENDING_SECOND_FIRST);
            }
            else {
                userRelationship.setUserFirstId(pendingFirst);
                userRelationship.setUserSecondId(pendingSecond);
                userRelationship.setType(Type.PENDING_FIRST_SECOND);
            }
            userRelationshipRepo.save(userRelationship);
            return "request is sent";
        }

    }


    public List<FriendListAndMutualFriend> readFromRedis(String key) {
        Object redisValue = redisTemplate.opsForValue().get(key);

        if (redisValue == null) {
            return Collections.emptyList();
        }

        // Ép kiểu giá trị đọc từ Redis
        if (redisValue instanceof List) {
            return (List<FriendListAndMutualFriend>) redisValue;
        }

        throw new IllegalStateException("Dữ liệu trong Redis không đúng định dạng!");
    }

    @Override
    @Cacheable(value = "friends" , key = "#emailId")
    public List<FriendListAndMutualFriend> getFriendListAndMutualFriend(String emailId) {
        List<UserInfoEntity> friendOfUser =getFriendList(emailId);
        List<FriendListAndMutualFriend> friendListAndMutualFriends = new ArrayList<>();
        for(UserInfoEntity userFriend : friendOfUser) {
            FriendListAndMutualFriend userWithUserMutualFriend= calculateCommonFriendsCount(friendOfUser,userFriend);
            friendListAndMutualFriends.add(new FriendListAndMutualFriend(userWithUserMutualFriend.getMutualFriend()
                    ,userFriend,userWithUserMutualFriend.getMututalFriendList()));
        }
        return friendListAndMutualFriends;
    }

    @Override
    public Page<SearchedUserInfoDto> getSearchResult(String email, String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserInfoEntity> result = userInfoRepo.findByEmployeeUserName(username, pageable);

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found");
        }

        String redisKey = "friends::" + email;
        System.out.println("email:" + redisKey);

        Object redisValue = redisTemplate.opsForValue().get(redisKey);


        List<FriendListAndMutualFriend> redisList = new ArrayList<>();
        if (redisValue != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                String jsonValue = objectMapper.writeValueAsString(redisValue);
                redisList = objectMapper.readValue(jsonValue,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, FriendListAndMutualFriend.class));
            } catch (Exception e) {
                System.err.println("Lỗi khi deserialize dữ liệu từ Redis: " + e.getMessage());
            }
        }
        List<FriendListAndMutualFriend> finalRedisList = redisList;
        List<SearchedUserInfoDto> searchedUserInfoList = result.getContent().stream()
                .map(user -> {
                    FriendListAndMutualFriend friendData = finalRedisList.stream()
                            .filter(friend -> friend.getUserInfoEntity().getAccName().equals(user.getAccName()))
                            .findFirst()
                            .orElse(null);

                    boolean existsInRedis = friendData != null;
                    int mutualFriendCount = friendData != null ? friendData.getMutualFriend() : 0;

                    return new SearchedUserInfoDto(
                            user.getEmailId(),
                            user.getAccName(),
                            userDTOMapper.mapTo(user.getEmployee()),
                            mutualFriendCount,
                            existsInRedis || user.getEmailId().equals(email)
                    );
                })
                .collect(Collectors.toList());

        return new PageImpl<>(searchedUserInfoList, pageable, result.getTotalElements());
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "friends", key = "#emailId"),
    },
            put = {
                    @CachePut(value = "friends", key = "#emailId"),
            })
    public List<FriendListAndMutualFriend>  acceptFriendRequest(String emailId, Long userSecondId) {
        UserInfoEntity userInfo = userInfoRepo.findByEmailId(emailId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found"));
        UserRelationship userRelationship;
        if(userInfo.getId() > userSecondId) {
             userRelationship = userRelationshipRepo.findByUserFirstId_IdAndUserSecondId_Id(userSecondId,userInfo.getId());
        }
        else {
            userRelationship = userRelationshipRepo.findByUserFirstId_IdAndUserSecondId_Id(userInfo.getId(),userSecondId);
            if(userRelationship.getType() == Type.FRIENDS) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ALREADY ACCEPTED");
            }
            userRelationship.setType(Type.FRIENDS);
        }
        userRelationshipRepo.save(userRelationship);
        return getFriendListAndMutualFriend(emailId);
    }

    @Override
    public String deleteFriendRequest(String user1EmailId,String user2Id) {
        UserInfoEntity user1 = userInfoRepo.findByEmailId(user1EmailId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found"));
        UserInfoEntity user2 = userInfoRepo.findById(Long.valueOf(user2Id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found"));
        UserRelationship userRelationship;
        if(user1.getId() < user2.getId()) {
            userRelationship= userRelationshipRepo.
                    findByUserFirstId_IdAndUserSecondId_Id(user1.getId(),user2.getId());
        }
        else {
            userRelationship = userRelationshipRepo.
                    findByUserFirstId_IdAndUserSecondId_Id(user2.getId(),user1.getId());
        }
        userRelationshipRepo.delete(userRelationship);
        return "deleted request";
    }

    @Override
    public List<UserInfoEntity> getFriendList(String emailId) {
        UserInfoEntity user = userInfoRepo.findByEmailId(emailId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found")
        );
        List<UserInfoEntity> friendList = new ArrayList<>();
        List<UserRelationship> userRelationships = userRelationshipRepo.getAllFriendOfUser(user.getId());
        for(UserRelationship userRelationship : userRelationships) {
            if(Objects.equals(userRelationship.getUserFirstId().getId(), user.getId())) {
                friendList.add(userRelationship.getUserSecondId());
            }
            else {
                friendList.add(userRelationship.getUserFirstId());
            }
        }
        return friendList;
    }




    private FriendListAndMutualFriend calculateCommonFriendsCount(List<UserInfoEntity> friendsOfUser, UserInfoEntity user) {
        int commonFriendsCount = 0;
        List<UserInfoEntity> mutualFriendList = new ArrayList<>();
        for (UserInfoEntity userFriend : friendsOfUser) {
            List<UserInfoEntity> userFriendFriends = getFriendList(userFriend.getEmailId());
            if (userFriendFriends.contains(user)) {
                mutualFriendList.add(userFriend);
                commonFriendsCount++;
            }
        }
        return new FriendListAndMutualFriend(commonFriendsCount,user,mutualFriendList);
    }

    private void processFriend(Vertex current, Graph graph, Queue queue, Set<String> visited,
                               List<UserInfoEntity> friendsOfUser, Map<UserInfoEntity, Integer> commonFriendsCountMap) {
        List<UserInfoEntity> friends = getFriendList(current.getData().getEmailId());
        if (friends == null) return;
        for (UserInfoEntity friend : friends) {
            if (!visited.contains(friend.getEmailId())) {
                Vertex friendVertex = new Vertex(friend);
                graph.addVertex(friend);
                graph.addEdge(current, friendVertex, null);
                queue.enqueue(friendVertex);
                visited.add(friend.getEmailId());

                if (!friendsOfUser.contains(friend)) {
                    int commonFriendsCount = calculateCommonFriendsCount(friendsOfUser, friend).getMutualFriend();
                    commonFriendsCountMap.put(friend, commonFriendsCountMap.getOrDefault(friend, 0) + commonFriendsCount);
                }
            }
        }
    }


    private List<FriendListAndMutualFriend> findMaxCommonFriendsUsers(String emailId,Map<UserInfoEntity, Integer> commonFriendsCountMap) {
        List<Map.Entry<UserInfoEntity, Integer>> sortedEntries = new ArrayList<>(commonFriendsCountMap.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        List<FriendListAndMutualFriend> maxCommonFriendsUsers = new ArrayList<>();
        int currentCount = 0;

        for (Map.Entry<UserInfoEntity, Integer> entry : sortedEntries) {
            if (currentCount < 8) {
                List<UserInfoEntity> friendOfUser = getFriendList(emailId);
                FriendListAndMutualFriend userWithUserMutualFriend= calculateCommonFriendsCount(friendOfUser,entry.getKey());
                maxCommonFriendsUsers.add(new FriendListAndMutualFriend(userWithUserMutualFriend.getMutualFriend()
                        ,entry.getKey(),userWithUserMutualFriend.getMututalFriendList()));
                currentCount++;
            } else {
                break;
            }
        }
        return maxCommonFriendsUsers;
    }

    @Override
    public List<FriendListAndMutualFriend> createSocialGraph(String emailId) {
        UserInfoEntity user = userInfoRepo.findByEmailId(emailId).orElseThrow(() -> new RuntimeException("Error:Not Found this user"));
        Vertex start = new Vertex(user);
        Graph graph = new Graph(false, false);
        Queue queue = new Queue();
        Set<String> visited = new HashSet<>();
        queue.enqueue(start);
        visited.add(start.getData().getEmailId());
        graph.addVertex(start.getData());

        List<UserInfoEntity> friendsOfUser = getFriendList(user.getEmailId());

        Map<UserInfoEntity, Integer> commonFriendsCountMap = new HashMap<>();
        while (!queue.isEmpty()) {
            Vertex current = queue.dequeue();
            processFriend(current, graph, queue, visited, friendsOfUser, commonFriendsCountMap);
        }
        return findMaxCommonFriendsUsers(emailId,commonFriendsCountMap);
    }



}
