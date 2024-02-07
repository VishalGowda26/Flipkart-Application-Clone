package com.ex.flipkartclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.Seller;

public interface SellerRepo extends JpaRepository<Seller, Integer>{

}
