package com.ex.flipkartclone.request_dto;

import com.ex.flipkartclone.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRequest {

	private String email;
	private String password;
	private UserRole userrole;
}
