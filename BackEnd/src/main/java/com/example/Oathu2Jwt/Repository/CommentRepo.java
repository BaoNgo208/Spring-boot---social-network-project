package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment,Long> {
    public Optional<Comment> findByPostId(Long id);
}
