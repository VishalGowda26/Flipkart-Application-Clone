package com.ex.flipkartclone.service;

import org.springframework.http.ResponseEntity;

import com.ex.flipkartclone.request_dto.UserRequest;
import com.ex.flipkartclone.response_dto.UserResponse;
import com.ex.flipkartclone.util.ResponseStructure;

public interface AuthService {
	public ResponseEntity<ResponseStructure<UserResponse>> register(UserRequest userrequest);
}
