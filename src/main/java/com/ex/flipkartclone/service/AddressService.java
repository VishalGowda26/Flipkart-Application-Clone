package com.ex.flipkartclone.service;

import org.springframework.http.ResponseEntity;

import com.ex.flipkartclone.request_dto.AddressRequest;
import com.ex.flipkartclone.response_dto.AddressResponse;
import com.ex.flipkartclone.util.ResponseStructure;

public interface AddressService {

	ResponseEntity<ResponseStructure<AddressResponse>> addAddress(AddressRequest addressRequest, int storeId);

	ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(AddressRequest addressRequest, int addressId);

	ResponseEntity<ResponseStructure<AddressResponse>> findAddressById(int addressId);

	ResponseEntity<ResponseStructure<AddressResponse>> findAddressByStore(int storeId);

}
