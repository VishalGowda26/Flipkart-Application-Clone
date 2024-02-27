package com.ex.flipkartclone.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ex.flipkartclone.entity.Contact;
import com.ex.flipkartclone.request_dto.ContactRequest;
import com.ex.flipkartclone.util.ResponseStructure;

public interface ContactService {

	ResponseEntity<ResponseStructure<Contact>> addContact(ContactRequest contactRequest, int addressId);

	ResponseEntity<ResponseStructure<Contact>> updateContact(ContactRequest contactRequest, int addressId);

	ResponseEntity<ResponseStructure<Contact>> findContactById(int contactId);

	ResponseEntity<ResponseStructure<List<Contact>>> findContactByAddress(int addressId);

}
