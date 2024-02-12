package com.ex.flipkartclone.service;

import org.springframework.http.ResponseEntity;

import com.ex.flipkartclone.entity.User;
import com.ex.flipkartclone.request_dto.AuthRequest;
import com.ex.flipkartclone.request_dto.OtpModel;
import com.ex.flipkartclone.request_dto.UserRequest;
import com.ex.flipkartclone.response_dto.AuthResponse;
import com.ex.flipkartclone.response_dto.SimpleResponseStructure;
import com.ex.flipkartclone.response_dto.UserResponse;
import com.ex.flipkartclone.util.ResponseStructure;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	public ResponseEntity<ResponseStructure<UserResponse>> register(UserRequest userrequest);

	public ResponseEntity<ResponseStructure<User>> verifyOTP(OtpModel otpModel);

	public ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest,
			HttpServletResponse servletResponse);

	public ResponseEntity<ResponseStructure<SimpleResponseStructure>> logout(HttpServletResponse servletResponse,
			String accessToken, String refeshToken);


	public ResponseEntity<ResponseStructure<SimpleResponseStructure>> revokeOtherDeviceAccess(String accessToken,
			String refreshToken);

	public ResponseEntity<ResponseStructure<SimpleResponseStructure>> revokeAllDeviceAccess(String accessToken,
			String refreshToken);

//	public ResponseEntity<ResponseStructure<String>> logout(HttpServletRequest servletRequest,
//			HttpServletResponse servletResponse);

}
