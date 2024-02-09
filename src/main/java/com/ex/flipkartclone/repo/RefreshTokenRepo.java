package com.ex.flipkartclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.RefreshToken;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer>{

}
