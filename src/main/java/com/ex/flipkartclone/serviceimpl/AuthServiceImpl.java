package com.ex.flipkartclone.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ex.flipkartclone.entity.Customer;
import com.ex.flipkartclone.entity.Seller;
import com.ex.flipkartclone.entity.User;
import com.ex.flipkartclone.exception.ConstraintViolationException;
import com.ex.flipkartclone.repo.CustomerRepo;
import com.ex.flipkartclone.repo.SellerRepo;
import com.ex.flipkartclone.repo.UserRepo;
import com.ex.flipkartclone.request_dto.UserRequest;
import com.ex.flipkartclone.response_dto.UserResponse;
import com.ex.flipkartclone.service.AuthService;
import com.ex.flipkartclone.util.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

	private UserRepo userRepo;
	private SellerRepo sellerRepo;
	private CustomerRepo customerRepo;
	private ResponseStructure<UserResponse> structure;

	/*------------------------------> Map To UserRequest <--------------------------------------------*/

	public <T extends User> T mapToUser(UserRequest userRequest) {
		User user = null;
		switch (userRequest.getUserrole()) {
		case CUSTOMER -> {
			user = new Customer();
		}
		case SELLER -> {
			user = new Seller();
		}
		default -> throw new ConstraintViolationException(null, 0, null);
		}
		user.setUsername(userRequest.getEmail().split("@")[0]);
		user.setPassword(userRequest.getPassword());
		user.setEmail(userRequest.getEmail());
		user.setUserrole(userRequest.getUserrole());
		user.setDeleted(false);
		user.setEmailVerified(false);
		return (T) user;
	}

	/*------------------------------> Map To UserResponse <--------------------------------------------*/

	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder().email(user.getEmail()).userId(user.getUserId()).username(user.getUsername())
				.userrole(user.getUserrole()).isDeleted(user.isDeleted()).isEmailVerified(user.isEmailVerified())
				.build();
	}

	/*------------------------------> Register User <--------------------------------------------*/

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> register(UserRequest userrequest) {
		User user = userRepo.findByUsername(userrequest.getEmail().split("@")[0]).map(u -> {
			if (u.isEmailVerified())
				throw new ConstraintViolationException("User with specified email already Exists",
						HttpStatus.ALREADY_REPORTED.value(), "Email Should be Unique");
			else {
				// send an email to client with OTP
			}
			return u;
		}).orElseGet(() -> saveUser(userrequest));
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure.setData(mapToUserResponse(user))
				.setMessage("Please verify through OTP sent on mail Id").setStatus(HttpStatus.ACCEPTED.value()),
				HttpStatus.ACCEPTED);
	}

	/*------------------------------> Method to saveUser <--------------------------------------------*/

	public User saveUser(UserRequest userRequest) {
		User user = null;
		switch (userRequest.getUserrole()) {
		case CUSTOMER -> {
			user = customerRepo.save((Customer) mapToUser(userRequest));
		}
		case SELLER -> {
			user = sellerRepo.save((Seller) mapToUser(userRequest));
		}
		default -> throw new ConstraintViolationException("User with given role dosen't Exists",
				HttpStatus.BAD_REQUEST.value(), "No such role available");

		}
		return user;
	}

}