package com.ex.flipkartclone.request_dto;

import com.ex.flipkartclone.enums.AddressType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRequest {
	private String streetAddress;
    private String streetAddressAdditional;
    private String country;
    private String state;
    private String city;
    private int pincode;
    private AddressType addressType;
}
