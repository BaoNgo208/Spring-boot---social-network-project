package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.User.UserRelationship;
import com.example.Oathu2Jwt.Model.Entity.User.UserRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRelationshipRepo extends JpaRepository<UserRelationship, UserRelationshipId> {
    @Query("SELECT ur FROM UserRelationship ur " +
            "WHERE ((ur.userSecondId.Id = :userId AND ur.type = 'pending_first_second') " +
            "OR (ur.userFirstId.Id = :userId AND ur.type = 'pending_second_first'))")
    public List<UserRelationship> findByAddFriendRequest(@Param("userId") Long userId);
    public UserRelationship findByUserFirstId_IdAndUserSecondId_Id(Long userFirstId, Long userSecondId);
  @Query("SELECT ur FROM UserRelationship ur " +
          "WHERE (ur.userFirstId.Id = :givenId OR ur.userSecondId.Id = :givenId) " +
          "AND ur.type = 'FRIENDS'")
    public List<UserRelationship> getAllFriendOfUser(@Param("givenId") Long givenId);
}
