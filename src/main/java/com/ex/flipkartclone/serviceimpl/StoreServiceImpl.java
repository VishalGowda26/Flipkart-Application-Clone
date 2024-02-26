package com.ex.flipkartclone.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ex.flipkartclone.entity.Seller;
import com.ex.flipkartclone.entity.Store;
import com.ex.flipkartclone.exception.ConstraintViolationException;
import com.ex.flipkartclone.repo.SellerRepo;
import com.ex.flipkartclone.repo.StoreRepo;
import com.ex.flipkartclone.request_dto.StoreRequest;
import com.ex.flipkartclone.response_dto.StoreResponse;
import com.ex.flipkartclone.service.StoreService;
import com.ex.flipkartclone.util.ResponseStructure;

public class StoreServiceImpl implements StoreService {
	private ResponseStructure<StoreResponse> structure;
	private SellerRepo sellerRepo;
	private StoreRepo storeRepo;

	private Store mapToStore(StoreRequest storeRequest) {
		return Store.builder().storeName(storeRequest.getStoreName()).about(storeRequest.getAbout()).build();

	}

	private StoreResponse mapToStoreResponse(Store store) {
		return StoreResponse.builder().storeId(store.getStoreId()).storeName(store.getStoreName())
				.about(store.getAbout()).logoLink(store.getLogoLink()).build();
	}

	/*------------------------------------------------------> Create Store <-----------------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<StoreResponse>> createStore(StoreRequest storeRequest, int sellerId) {
		Seller seller = sellerRepo.findById(sellerId).get();
		if (seller.getStore() == null) {
			Store store = mapToStore(storeRequest);
			seller.setStore(store);
			storeRepo.save(store);
			sellerRepo.save(seller);

			return new ResponseEntity<ResponseStructure<StoreResponse>>(structure.setMessage("Store created")
					.setStatus(HttpStatus.CREATED.value()).setData(mapToStoreResponse(store)), HttpStatus.CREATED);
		} else
			throw new ConstraintViolationException("Store not found", HttpStatus.NOT_FOUND.value(),
					"Please check the given ID");
	}

	/*------------------------------------------------------> Fetch Store <-----------------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<StoreResponse>> fetchStore(int storeId) {
		Store store = storeRepo.findById(storeId).orElseThrow(() -> {
			throw new ConstraintViolationException("Store not found", HttpStatus.NOT_FOUND.value(),
					"Please check the given ID");
		});
		return new ResponseEntity<ResponseStructure<StoreResponse>>(
				structure.setMessage("Store details Fetched Successfully").setStatus(HttpStatus.CREATED.value())
						.setData(mapToStoreResponse(store)),
				HttpStatus.CREATED);
	}

	/*------------------------------------------------------> Fetch Store By Seller <-----------------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<StoreResponse>> fetchStoreBySeller(int sellerId) {
		Seller seller = sellerRepo.findById(sellerId).orElseThrow(() -> {
			throw new ConstraintViolationException("User not found", HttpStatus.NO_CONTENT.value(), "");
		});
		structure.setStatus(HttpStatus.OK.value());
		structure.setData(mapToStoreResponse(seller.getStore()));
		structure.setMessage("Store successfully fetched");
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	/*------------------------------------------------------> Update Store <-----------------------------------------------------*/
	@Override
	public ResponseEntity<ResponseStructure<StoreResponse>> updateStore(StoreRequest storeRequest, int storeId) {
		storeRepo.findById(storeId).ifPresent(store -> {
			store.setStoreName(storeRequest.getStoreName());
			store.setAbout(storeRequest.getAbout());
			storeRepo.save(store);
			structure.setStatus(HttpStatus.OK.value());
			structure.setData(mapToStoreResponse(store));
			structure.setMessage("Store successfully fetched");
		});
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

}
