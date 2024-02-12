package com.ex.flipkartclone.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.AccessToken;

public interface AccessTokenRepo extends JpaRepository<AccessToken, Long> {
	AccessToken findByToken(String token);
	
	List<AccessToken> findByExpirationBefore(LocalDateTime dateTime);
 
}
