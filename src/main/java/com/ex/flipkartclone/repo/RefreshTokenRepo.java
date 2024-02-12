package com.ex.flipkartclone.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.RefreshToken;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
	RefreshToken findByToken(String token);

	List<RefreshToken> findByExpirationBefore(LocalDateTime expiration);
}
