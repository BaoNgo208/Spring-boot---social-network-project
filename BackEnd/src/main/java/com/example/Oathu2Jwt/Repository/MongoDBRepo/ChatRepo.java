package com.example.Oathu2Jwt.Repository.MongoDBRepo;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Chat.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepo extends MongoRepository<Chat,String> {

    @Query("{ 'participants' : { $all : ?0 } }")
    Optional<Chat> findByParticipantsContainingIgnoreOrder(List<Integer> participants);

    @Query("{ 'participants' : { $in : [?0] } }")
    Page<Chat> findByUserId(Integer userId, Pageable pageable);
}
