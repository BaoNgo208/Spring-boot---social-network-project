    package com.example.Oathu2Jwt.Repository.MongoDBRepo;

    import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ArchivedChatMessages;
    import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.mongodb.repository.Query;
    import org.springframework.data.mongodb.repository.MongoRepository;

    public interface ArchivedChatMessagesRepo extends MongoRepository<ArchivedChatMessages,String> {
        @Query("{ $or: [ { senderId: ?0, receiverId: ?1 }, { senderId: ?1, receiverId: ?0 } ] }")
        Page<ArchivedChatMessages> findMessagesBetweenUsers(
                Integer senderId,
                Integer receiverId,
                Pageable pageable);
    }
