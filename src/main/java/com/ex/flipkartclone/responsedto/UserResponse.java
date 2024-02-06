package com.ex.flipkartclone.responsedto;

import com.ex.flipkartclone.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

	private int userId;
	private String username;
	private String email;
	private UserRole userrole;
	boolean isEmailVerified;
	boolean isDeleted;
}
