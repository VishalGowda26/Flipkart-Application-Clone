package com.ex.flipkartclone.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.flipkartclone.entity.Contact;

public interface ContactRepo extends JpaRepository<Contact, Integer>{

}
