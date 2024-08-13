package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepo extends JpaRepository<Post,Long> {
    public List<Post> findByUserId(Long id);
    Page<Post> findByUserIdIn(List<Long> userIds, Pageable pageable);

}
