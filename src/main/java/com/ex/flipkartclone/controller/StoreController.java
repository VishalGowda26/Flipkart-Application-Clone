package com.ex.flipkartclone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.flipkartclone.request_dto.StoreRequest;
import com.ex.flipkartclone.response_dto.StoreResponse;
import com.ex.flipkartclone.service.StoreService;
import com.ex.flipkartclone.util.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class StoreController {
	private StoreService storeService;

	@PostMapping("/stores/{sellerId}")
	public ResponseEntity<ResponseStructure<StoreResponse>> createStore(@RequestBody StoreRequest storeRequest,
			@PathVariable int sellerId) {
		return storeService.createStore(storeRequest, sellerId);
	}

	@GetMapping("/stores/{storeId}")
	public ResponseEntity<ResponseStructure<StoreResponse>> fetchStoreByStoreId(@PathVariable int storeId) {
		return storeService.fetchStore(storeId);
	}

	@GetMapping("/sellers/{sellerId}/stores")
	public ResponseEntity<ResponseStructure<StoreResponse>> fetchStoreBySeller(@PathVariable int sellerId) {
		return storeService.fetchStoreBySeller(sellerId);
	}

	@PutMapping("/stores/{storeId}")
	public ResponseEntity<ResponseStructure<StoreResponse>> updateStore(@RequestBody StoreRequest storeRequest,
			@PathVariable int storeId) {
		return storeService.updateStore(storeRequest, storeId);
	}

}
