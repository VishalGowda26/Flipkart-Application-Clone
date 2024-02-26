package com.ex.flipkartclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.Address;


public interface AddressRepo extends JpaRepository<Address, Integer>{

}
