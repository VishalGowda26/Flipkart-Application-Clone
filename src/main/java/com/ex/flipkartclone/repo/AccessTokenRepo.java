package com.ex.flipkartclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.AccessToken;

public interface AccessTokenRepo extends JpaRepository<AccessToken, Integer> {

}
