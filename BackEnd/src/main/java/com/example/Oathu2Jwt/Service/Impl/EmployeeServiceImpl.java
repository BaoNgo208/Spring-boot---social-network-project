package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.Entity.*;
import com.example.Oathu2Jwt.Model.Entity.User.EmployeeEntity;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Model.Entity.User.UserRelationship;
import com.example.Oathu2Jwt.Repository.*;
import com.example.Oathu2Jwt.Service.EmployeeService;
import com.example.Oathu2Jwt.Util.Graph.Graph;
import com.example.Oathu2Jwt.Util.Graph.Vertex;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import com.example.Oathu2Jwt.Util.Graph.Queue;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final SalaryRepo salaryRepo;
    private final UserInfoRepo userInfoRepo;

    private final UserRelationshipRepo userRelationshipRepo;

    @Override
    public UserInfoEntity getUserByEmail(String email) {
        return  userInfoRepo.findByEmailId(email)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"user not found"));
    }

    @Override
    @Transactional
    public EmployeeEntity updateEmployee(String id, EmployeeEntity employee) {
            Salary salary = salaryRepo.findBySalary(employee.getSalary().getSalary());
            return employeeRepo.findById(Long.parseLong(id)).map(existingEmployee -> {
                Optional.ofNullable(employee.getDateOfBirth()).ifPresent(existingEmployee::setDateOfBirth);
                Optional.ofNullable(employee.getMobileNumber()).ifPresent(existingEmployee::setMobileNumber);
                Optional.ofNullable(employee.getUserName()).ifPresent(existingEmployee::setUserName);
                existingEmployee.setSalary(salary);
                return employeeRepo.save(existingEmployee);
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"user not found"));

    }



    @Override
    public List<UserInfoEntity> getAddFriendRequestList(String email) {
            UserInfoEntity userInfo = userInfoRepo.findByEmailId(email)
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"post not found ")
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


    @Override
    public List<UserInfoEntity> getSearchResult(String username) {
            List<UserInfoEntity> result = userInfoRepo.findByEmployeeUserName(username);
            if(result.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found");
            }
            return result;
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
        int maxCommonFriendsCount = sortedEntries.isEmpty() ? 0 : sortedEntries.get(0).getValue();
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
        for (FriendListAndMutualFriend user : maxCommonFriendsUsers) {
            System.out.println("user with most mutual friends: " + user.getUserInfoEntity().getEmailId());
        }
        System.out.println("count:" + maxCommonFriendsCount);
        System.out.println("list size:"+maxCommonFriendsUsers.size());
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
