package com.ex.flipkartclone.service;

import org.springframework.http.ResponseEntity;

import com.ex.flipkartclone.requestdto.UserRequest;
import com.ex.flipkartclone.responsedto.UserResponse;
import com.ex.flipkartclone.util.ResponseStructure;

public interface AuthService {
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userrequest);
}
