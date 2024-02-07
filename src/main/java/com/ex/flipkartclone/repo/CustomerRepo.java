package com.ex.flipkartclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Integer>{

}
