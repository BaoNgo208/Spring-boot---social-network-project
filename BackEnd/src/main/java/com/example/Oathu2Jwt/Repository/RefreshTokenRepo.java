package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.server.WebFilter;

import java.util.Optional;

public interface RefreshTokenRepo  extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
