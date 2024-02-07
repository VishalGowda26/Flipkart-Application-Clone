package com.ex.flipkartclone.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
	boolean existsByEmail(String email);

	Optional<User> findByUsername(String username);
}
