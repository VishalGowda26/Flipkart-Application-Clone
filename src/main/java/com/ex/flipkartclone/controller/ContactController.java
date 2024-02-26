package com.ex.flipkartclone.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.flipkartclone.entity.Contact;
import com.ex.flipkartclone.request_dto.ContactRequest;
import com.ex.flipkartclone.service.ContactService;
import com.ex.flipkartclone.util.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(allowCredentials = "true",origins = "http://localhost:5173/")
@AllArgsConstructor
public class ContactController {

    private ContactService contactService;

    @PostMapping("contacts/{addressId}/address")
    public ResponseEntity<ResponseStructure<Contact>> addContact(@RequestBody ContactRequest contactRequest,@PathVariable int addressId){
        return contactService.addContact(contactRequest,addressId);
    }

    @PutMapping("contacts/{addressId}/address")
    public ResponseEntity<ResponseStructure<Contact>> updateContact(@RequestBody ContactRequest contactRequest,@PathVariable int addressId){
        return contactService.updateContact(contactRequest,addressId);
    }

    @GetMapping("contacts/{contactId}")
    public ResponseEntity<ResponseStructure<Contact>> findContactById(@RequestBody ContactRequest contactRequest,@PathVariable int contactId){
        return contactService.findContactById(contactId);
    }

    @GetMapping("contacts/{addressId}/address")
    public ResponseEntity<ResponseStructure<List<Contact>>> findContactByAddress(@RequestBody ContactRequest contactRequest,@PathVariable int addressId){
        return contactService.findContactByAddress(addressId);
    }

}