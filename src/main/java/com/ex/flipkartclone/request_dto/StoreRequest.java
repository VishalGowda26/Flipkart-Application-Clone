package com.ex.flipkartclone.request_dto;

import lombok.Builder;

@Builder
public class StoreRequest {

	private String storeName;
	private String about;

	public String getStoreName() {
		return storeName;
	}

	public StoreRequest setStoreName(String storeName) {
		this.storeName = storeName;
		return this;
	}

	public String getAbout() {
		return about;
	}

	public StoreRequest setAbout(String about) {
		this.about = about;
		return this;
	}

}
