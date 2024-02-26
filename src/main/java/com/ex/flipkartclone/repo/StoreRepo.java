package com.ex.flipkartclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.Store;

public interface StoreRepo extends JpaRepository<Store, Integer> {
	boolean existsByStoreName(String storeName);

}
