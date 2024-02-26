package com.ex.flipkartclone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ex.flipkartclone.request_dto.AddressRequest;
import com.ex.flipkartclone.response_dto.AddressResponse;
import com.ex.flipkartclone.service.AddressService;
import com.ex.flipkartclone.util.ResponseStructure;

import lombok.AllArgsConstructor;


@RestController
//@CrossOrigin(allowCredentials = "true",origins = "http://localhost:5173/")
//@RequestMapping("/api/v1")
@AllArgsConstructor
public class AddressController {

	private AddressService addressService;

    @PostMapping("addresses/{storeId}")
    public ResponseEntity<ResponseStructure<AddressResponse>> addAddress(@RequestBody AddressRequest addressRequest,@PathVariable int storeId){
        return addressService.addAddress(addressRequest,storeId);
    }

    @PostMapping("addresses/{addressId}")
    public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(@RequestBody AddressRequest addressRequest,@PathVariable int addressId){
        return addressService.updateAddress(addressRequest,addressId);
    }

    @GetMapping("addresses/{addressId}")
    public ResponseEntity<ResponseStructure<AddressResponse>> findAddressById(@PathVariable int addressId){
        return addressService.findAddressById(addressId);
    }

    @GetMapping("addresses/{storeId}")
    public ResponseEntity<ResponseStructure<AddressResponse>> findAddressByStore(@PathVariable int storeId){
        return addressService.findAddressByStore(storeId);
    }
}
