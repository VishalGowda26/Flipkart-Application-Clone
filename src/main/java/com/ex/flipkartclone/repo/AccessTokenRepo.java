package com.ex.flipkartclone.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.AccessToken;
import com.ex.flipkartclone.entity.User;

public interface AccessTokenRepo extends JpaRepository<AccessToken, Long> {
	AccessToken findByToken(String token);

	List<AccessToken> findByExpirationBefore(LocalDateTime dateTime);

	Optional<AccessToken> findByTokenAndIsBlocked(String token, boolean isBlocked);

	Optional<AccessToken> findByUserAndIsBlocked(User user, boolean blocked);

	List<AccessToken> findByUserAndIsBlockedAndTokenNot(User user,boolean blocked, String accessToken);
	
}
