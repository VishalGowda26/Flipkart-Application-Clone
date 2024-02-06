package com.ex.flipkartclone.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.ex.flipkartclone.entity.Seller;
import com.ex.flipkartclone.entity.User;
import com.ex.flipkartclone.enums.UserRole;
import com.ex.flipkartclone.repo.AuthRepo;
import com.ex.flipkartclone.requestdto.UserRequest;
import com.ex.flipkartclone.responsedto.UserResponse;
import com.ex.flipkartclone.service.AuthService;
import com.ex.flipkartclone.util.ResponseStructure;

public class AuthServiceImpl implements AuthService {

	@Autowired
	AuthRepo authRepo;

	public <T extends User> UserRequest mapToUser(UserRequest userRequest) {
		return userRequest.builder().email(userRequest.getEmail())
				.password(userRequest.getPassword())
				.userrole(userRequest.getUserrole())
				.build();
	
		}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userrequest) {
		Seller seller = new Seller();
		if (seller.equals(UserRole.SELLER) && seller != null) {
			authRepo.save(seller);
			seller.setDeleted(false);
			seller.setEmailVerified(false);
		}
		return null;
	}

}
