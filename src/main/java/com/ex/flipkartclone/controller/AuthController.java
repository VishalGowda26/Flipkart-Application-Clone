package com.ex.flipkartclone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ex.flipkartclone.requestdto.UserRequest;
import com.ex.flipkartclone.responsedto.UserResponse;
import com.ex.flipkartclone.serviceimpl.AuthServiceImpl;
import com.ex.flipkartclone.util.ResponseStructure;

public class AuthController {

	@Autowired
	private AuthServiceImpl authServiceImpl;

	@PostMapping(path = "/users")
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(@RequestBody UserRequest userrequest) {
		return authServiceImpl.registerUser(userrequest);

	}
}
