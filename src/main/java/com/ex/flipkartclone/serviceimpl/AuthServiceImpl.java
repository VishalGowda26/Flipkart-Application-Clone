package com.ex.flipkartclone.serviceimpl;

import java.util.Date;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ex.flipkartclone.cache.CacheStore;
import com.ex.flipkartclone.entity.Customer;
import com.ex.flipkartclone.entity.Seller;
import com.ex.flipkartclone.entity.User;
import com.ex.flipkartclone.exception.ConstraintViolationException;
import com.ex.flipkartclone.repo.CustomerRepo;
import com.ex.flipkartclone.repo.SellerRepo;
import com.ex.flipkartclone.repo.UserRepo;
import com.ex.flipkartclone.request_dto.OtpModel;
import com.ex.flipkartclone.request_dto.UserRequest;
import com.ex.flipkartclone.response_dto.UserResponse;
import com.ex.flipkartclone.service.AuthService;
import com.ex.flipkartclone.util.MessageStructure;
import com.ex.flipkartclone.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

	private UserRepo userRepo;
	private SellerRepo sellerRepo;
	private CustomerRepo customerRepo;
	private ResponseStructure<UserResponse> structure;
	private CacheStore<String> otpCacheStore;
	private CacheStore<User> userCacheStore;
	private ResponseStructure<User> userStructure;
	private JavaMailSender javaMailSender;

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

//	/*------------------------------> Method to saveUser <--------------------------------------------*/
//
//	public User saveUser(UserRequest userRequest) {
//		User user = null;
//		switch (userRequest.getUserrole()) {
//		case CUSTOMER -> {
//			user = customerRepo.save((Customer) mapToUser(userRequest));
//		}
//		case SELLER -> {
//			user = sellerRepo.save((Seller) mapToUser(userRequest));
//		}
//		default -> throw new ConstraintViolationException("User with given role dosen't Exists",
//				HttpStatus.BAD_REQUEST.value(), "No such role available");
//
//		}
//		return user;
//	}

	/*------------------------------> Register User <--------------------------------------------*/

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> register(UserRequest userrequest) {
		if (userRepo.existsByEmail(userrequest.getEmail()))
			throw new RuntimeException("User Already exists with given email");
		String OTP = generateOTP();
		User user = mapToUser(userrequest);
		userCacheStore.add(userrequest.getEmail(), user);
		otpCacheStore.add(userrequest.getEmail(), OTP);
		try {
			sendOtpToMail(user, OTP);
		} catch (Exception e) {
			log.error("Given email address doesn't exist pls verify");
		}

		return new ResponseEntity<ResponseStructure<UserResponse>>(structure.setData(mapToUserResponse(user))
				.setMessage("Please verify through OTP sent on mail id").setStatus(HttpStatus.ACCEPTED.value()),
				HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<ResponseStructure<User>> verifyOTP(OtpModel otpModel) {
		User user = userCacheStore.get(otpModel.getEmail());
		String otp = otpCacheStore.get(otpModel.getEmail());
		if (otp == null)
			throw new RuntimeException("OTP Expired");
		if (user == null)
			throw new RuntimeException("Registration Session Expired");
		if (otp.equals(otpModel.getOtp())) {
			user.setEmailVerified(true);
			userRepo.save(user);
		} else
			throw new ConstraintViolationException("Entered password is incorrect", HttpStatus.BAD_REQUEST.value(),
					"Pls check the entered password");

		try {
			sendRegistrationMail(user);
		} catch (Exception e) {
			log.error("Given email address doesn't exist pls verify");
		}
		return new ResponseEntity<ResponseStructure<User>>(
				userStructure.setData(user).setMessage("Registration Successful").setStatus(HttpStatus.CREATED.value()),
				HttpStatus.CREATED);
	}

	/*---------------------------------------> Generate OTP <--------------------------------------------*/

	private String generateOTP() {
		return String.valueOf(new Random().nextInt(100000, 999999));
	}

	/*-------------------------------------> ToSend Mail for Users <-------------------------------------*/

	@Async
	private void sendMail(MessageStructure message) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setTo(message.getTo());
		helper.setSubject(message.getSubject());
		helper.setSentDate(message.getSentDate());
		helper.setText(message.getText(), true);
		javaMailSender.send(mimeMessage);
	}

	/*-------------------------------------> ToSend OTP To Mail <-------------------------------------*/

	public void sendOtpToMail(User user, String otp) throws MessagingException {
		sendMail(MessageStructure.builder().to(user.getEmail()).subject("Complete your Registration To Flipkart")
				.sentDate(new Date())
				.text("hey, " + user.getUsername() + " Good to see you intrested in flipkart "
						+ " complete your registration using OTP <br>" + "<h1>" + otp + "</h1> <br>" + "<br><br>"
						+ " with best regards <br> " + " Flipkart ")
				.build());
	}

	/*-------------------------------------> ToSend login Confirmation <-------------------------------------*/
	public void sendRegistrationMail(User user) throws MessagingException {
		sendMail(MessageStructure.builder().to(user.getEmail()).subject("Complete your Registration To Flipkart")
				.sentDate(new Date()).text("Welcome, " + user.getUsername() + " to Flipkart "
						+ " Your Registration process is complete <br>" + " Continue Shopping on Flipkart <br> ")
				.build());
	}

}