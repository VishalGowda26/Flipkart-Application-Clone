package com.ex.flipkartclone.service;

import org.springframework.http.ResponseEntity;

import com.ex.flipkartclone.request_dto.StoreRequest;
import com.ex.flipkartclone.response_dto.StoreResponse;
import com.ex.flipkartclone.util.ResponseStructure;

public interface StoreService {

	ResponseEntity<ResponseStructure<StoreResponse>> createStore(StoreRequest storeRequest, int sellerId);

	ResponseEntity<ResponseStructure<StoreResponse>> fetchStore(int storeId);

	ResponseEntity<ResponseStructure<StoreResponse>> fetchStoreBySeller(int sellerId);

	ResponseEntity<ResponseStructure<StoreResponse>> updateStore(StoreRequest storeRequest, int storeId);

}
