package com.ex.flipkartclone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.flipkartclone.request_dto.UserRequest;
import com.ex.flipkartclone.response_dto.UserResponse;
import com.ex.flipkartclone.service.AuthService;
import com.ex.flipkartclone.util.ResponseStructure;

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
}
