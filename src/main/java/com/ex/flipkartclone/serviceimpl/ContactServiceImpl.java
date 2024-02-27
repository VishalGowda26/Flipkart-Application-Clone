package com.ex.flipkartclone.serviceimpl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ex.flipkartclone.entity.Address;
import com.ex.flipkartclone.entity.Contact;
import com.ex.flipkartclone.exception.ConstraintViolationException;
import com.ex.flipkartclone.repo.AddressRepo;
import com.ex.flipkartclone.repo.ContactRepo;
import com.ex.flipkartclone.request_dto.ContactRequest;
import com.ex.flipkartclone.service.ContactService;
import com.ex.flipkartclone.util.ResponseStructure;

@Service
public class ContactServiceImpl implements ContactService {

	private AddressRepo addressRepo;
	private ContactRepo contactRepo;
	private ResponseStructure<Contact> structure;
	private ResponseStructure<List<Contact>> lStructure;

	private Contact mapToContact(ContactRequest contactRequest) {
		return Contact.builder().contactNumber(contactRequest.getContactNumber()).name(contactRequest.getName())
				.priority(contactRequest.getPriority()).build();
	}

	/*-------------------------------------------------> Add Contact <-----------------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<Contact>> addContact(ContactRequest contactRequest, int addressId) {
		Address address = addressRepo.findById(addressId).orElseThrow(() -> {
			throw new ConstraintViolationException("Address not found", HttpStatus.NO_CONTENT.value(),
					"Address Id dosen't match");
		});
		if (address.getContacts().size() == 2)
			throw new ConstraintViolationException("Address with contacts already present",
					HttpStatus.ALREADY_REPORTED.value(), "No Address can have more than 2 contacts");
		Contact contact = mapToContact(contactRequest);
		address.getContacts().add(contact);
		addressRepo.save(address);
		contactRepo.save(contact);
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setData(mapToContact(contactRequest));
		structure.setMessage("Contact successfully created");
		return new ResponseEntity<ResponseStructure<Contact>>(structure, HttpStatus.CREATED);
	}

	/*------------------------------------------------> Update Contact <-----------------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<Contact>> updateContact(ContactRequest contactRequest, int addressId) {
		Address address = addressRepo.findById(addressId).orElseThrow(() -> {
			throw new ConstraintViolationException("Address not found", HttpStatus.NO_CONTENT.value(),
					"Address Id dosen't match");
		});
		List<Contact> contacts = address.getContacts();
		Contact contact = null;
		for (Contact contact1 : contacts) {
			if (contact1.getName().equals(contactRequest.getName()))
				contact = contact1;
		}
		contact.setContactNumber(contactRequest.getContactNumber());
		contact.setName(contactRequest.getName());
		contact.setPriority(contactRequest.getPriority());
		contactRepo.save(contact);
		structure.setStatus(HttpStatus.OK.value());
		structure.setData(mapToContact(contactRequest));
		structure.setMessage("Contact successfully created");
		return new ResponseEntity<ResponseStructure<Contact>>(structure, HttpStatus.OK);
	}

	/*-----------------------------------------------> Find Contact By Id <-----------------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<Contact>> findContactById(int contactId) {
		Contact contact = contactRepo.findById(contactId).orElseThrow(() -> {
			throw new ConstraintViolationException("Contact not found", HttpStatus.NO_CONTENT.value(), "");
		});
		structure.setStatus(HttpStatus.FOUND.value());
		structure.setData(contact);
		structure.setMessage("Contact successfully found");
		return new ResponseEntity<ResponseStructure<Contact>>(structure, HttpStatus.FOUND);
	}

	/*----------------------------------------------> Find Contact By Address <-----------------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<List<Contact>>> findContactByAddress(int addressId) {
		Address address = addressRepo.findById(addressId).orElseThrow(() -> {
			throw new ConstraintViolationException("Address not found", HttpStatus.NO_CONTENT.value(), "");
		});
		List<Contact> contacts = address.getContacts();
		lStructure.setStatus(HttpStatus.FOUND.value());
		lStructure.setData(contacts);
		lStructure.setMessage("Contacts successfully found");
		return new ResponseEntity<ResponseStructure<List<Contact>>>(lStructure, HttpStatus.FOUND);
	}

}
