package com.ex.flipkartclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.User;

public interface AuthRepo extends JpaRepository<User, Integer> {

}
