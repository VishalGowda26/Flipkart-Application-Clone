package com.ex.flipkartclone.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ex.flipkartclone.entity.Address;
import com.ex.flipkartclone.entity.Store;
import com.ex.flipkartclone.exception.ConstraintViolationException;
import com.ex.flipkartclone.exception.StoreNotFoundException;
import com.ex.flipkartclone.repo.AddressRepo;
import com.ex.flipkartclone.repo.StoreRepo;
import com.ex.flipkartclone.request_dto.AddressRequest;
import com.ex.flipkartclone.response_dto.AddressResponse;
import com.ex.flipkartclone.service.AddressService;
import com.ex.flipkartclone.util.ResponseStructure;

@Service
public class AddressServiceImpl implements AddressService {
	private AddressRepo addressRepo;
	private StoreRepo storeRepo;
	private ResponseStructure<AddressResponse> structure;

	public Address mapToAddress(AddressRequest addressRequest) {
		return Address.builder().addressType(addressRequest.getAddressType())
				.streetAddress(addressRequest.getStreetAddress())
				.streetAddressAdditional(addressRequest.getStreetAddressAdditional()).city(addressRequest.getCity())
				.country(addressRequest.getCountry()).pincode(addressRequest.getPincode())
				.state(addressRequest.getState()).build();
	}

	public AddressResponse mapToAddressResponse(Address address) {
		return AddressResponse.builder().addressId(address.getAddressId()).addressType(address.getAddressType())
				.streetAddress(address.getStreetAddress()).streetAddressAdditional(address.getStreetAddressAdditional())
				.city(address.getCity()).country(address.getCountry()).pincode(address.getPincode())
				.state(address.getState()).build();
	}

	/*-----------------------------------------------> Add Address <-------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<AddressResponse>> addAddress(AddressRequest addressRequest, int storeId) {
		Store store = storeRepo.findById(storeId).orElseThrow(() -> {
			throw new StoreNotFoundException("Store not found", HttpStatus.NO_CONTENT.value(),
					"Store Id dosen't match");
		});
		Address address = mapToAddress(addressRequest);
		store.setAddress(address);
		storeRepo.save(store);
		addressRepo.save(address);
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setData(mapToAddressResponse(address));
		structure.setMessage("Address successfully created");
		return new ResponseEntity<ResponseStructure<AddressResponse>>(structure, HttpStatus.CREATED);
	}

	/*-----------------------------------------> Update Address <-------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(AddressRequest addressRequest,
			int addressId) {
		Address address = addressRepo.findById(addressId).orElseThrow(() -> {
			throw new ConstraintViolationException("Address not found", HttpStatus.NO_CONTENT.value(),
					"Address Id dosen't match");
		});
		address.setAddressType(addressRequest.getAddressType());
		address.setStreetAddress(addressRequest.getStreetAddress());
		address.setCity(addressRequest.getCity());
		address.setCountry(addressRequest.getCountry());
		address.setState(addressRequest.getState());
		address.setPincode(addressRequest.getPincode());
		address.setStreetAddressAdditional(addressRequest.getStreetAddressAdditional());
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setData(mapToAddressResponse(address));
		structure.setMessage("Address successfully updated");
		return new ResponseEntity<ResponseStructure<AddressResponse>>(structure, HttpStatus.CREATED);
	}

	/*-----------------------------------------> Find Address by ID <-------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<AddressResponse>> findAddressById(int addressId) {
		Address address = addressRepo.findById(addressId).orElseThrow(() -> {
			throw new ConstraintViolationException("Address not found", HttpStatus.NO_CONTENT.value(),
					"Address Id dosen't match");
		});
		structure.setStatus(HttpStatus.FOUND.value());
		structure.setData(mapToAddressResponse(address));
		structure.setMessage("Address successfully updated");
		return new ResponseEntity<ResponseStructure<AddressResponse>>(structure, HttpStatus.FOUND);
	}

	/*--------------------------------> Find Address by Store <-------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<AddressResponse>> findAddressByStore(int storeId) {
		storeRepo.findById(storeId).orElseThrow(() -> {
			throw new StoreNotFoundException("Store not found", HttpStatus.NO_CONTENT.value(), "");
		});
		return null;
	}

}
