package com.ex.flipkartclone.service;

import org.springframework.http.ResponseEntity;

import com.ex.flipkartclone.entity.User;
import com.ex.flipkartclone.request_dto.OtpModel;
import com.ex.flipkartclone.request_dto.UserRequest;
import com.ex.flipkartclone.response_dto.UserResponse;
import com.ex.flipkartclone.util.ResponseStructure;

public interface AuthService {
	public ResponseEntity<ResponseStructure<UserResponse>> register(UserRequest userrequest);

	public ResponseEntity<ResponseStructure<User>> verifyOTP(OtpModel otpModel);

}
