package com.ex.flipkartclone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.flipkartclone.entity.User;
import com.ex.flipkartclone.request_dto.AuthRequest;
import com.ex.flipkartclone.request_dto.OtpModel;
import com.ex.flipkartclone.request_dto.UserRequest;
import com.ex.flipkartclone.response_dto.AuthResponse;
import com.ex.flipkartclone.response_dto.SimpleResponseStructure;
import com.ex.flipkartclone.response_dto.UserResponse;
import com.ex.flipkartclone.service.AuthService;
import com.ex.flipkartclone.util.ResponseStructure;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthController {

	private AuthService authService;

	@PostMapping(path = "/users")
	public ResponseEntity<ResponseStructure<UserResponse>> register(@RequestBody UserRequest userrequest) {
		return authService.register(userrequest);

	}

	@PostMapping(path = "/verify-otp")
	public ResponseEntity<ResponseStructure<User>> verifyOTP(@RequestBody OtpModel otpModel) {
		return authService.verifyOTP(otpModel);

	}

	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody AuthRequest authRequest,
			HttpServletResponse servletResponse) {
		return authService.login(authRequest, servletResponse);
	}

//	@PostMapping("/logout")
//	public ResponseEntity<ResponseStructure<String>> logout(HttpServletRequest servletRequest,HttpServletResponse servletResponse){
//		return authService.logout(servletRequest,servletResponse);
//		
//	}

	@PreAuthorize(value = "hasAuthority('SELLER') or hasAuthority('CUSTOMER')")
	@PostMapping("/logout")
	public ResponseEntity<ResponseStructure<SimpleResponseStructure>> logout(HttpServletResponse servletResponse,
			@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "rt", required = false) String refeshToken) {
		return authService.logout(servletResponse, accessToken, refeshToken);
	}

	@PreAuthorize(value = "hasAuthority('SELLER') or hasAuthority('CUSTOMER')")
	@PostMapping("/revoke-all")
	public ResponseEntity<ResponseStructure<SimpleResponseStructure>> revokeAllDeviceAccess(
			@CookieValue(name = "rt", required = false) String refreshToken,
			@CookieValue(name = "at", required = false) String accessToken) {
		return authService.revokeAllDeviceAccess(accessToken, refreshToken);
	}
	
	@PreAuthorize(value = "hasAuthority('SELLER') or hasAuthority('CUSTOMER')")
	@PostMapping("/revoke-other")
	public ResponseEntity<ResponseStructure<SimpleResponseStructure>> revokeOtherDeviceAccess(
			@CookieValue(name = "rt", required = false) String refreshToken,
			@CookieValue(name = "at", required = false) String accessToken) {
		return authService.revokeOtherDeviceAccess(accessToken, refreshToken);
	}


}
