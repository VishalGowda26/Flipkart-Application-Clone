package com.ex.flipkartclone.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.RefreshToken;
import com.ex.flipkartclone.entity.User;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
	RefreshToken findByToken(String token);

	List<RefreshToken> findByExpirationBefore(LocalDateTime expiration);

	List<RefreshToken> findByUserAndIsBlocked(User user, boolean blocked);

	Optional<RefreshToken> findByUser(User user);
	
	List<RefreshToken> findByUserAndIsBlockedAndTokenNot(User user,boolean blocked, String refreshToken);
}
