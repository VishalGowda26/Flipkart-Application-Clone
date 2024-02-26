package com.ex.flipkartclone.response_dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreResponse {
	private int storeId;
	private String storeName;
	private String logoLink;
	private String about;
}
