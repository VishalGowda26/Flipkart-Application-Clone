		package com.ex.flipkartclone.serviceimpl;
		
		import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ex.flipkartclone.cache.CacheStore;
import com.ex.flipkartclone.entity.AccessToken;
import com.ex.flipkartclone.entity.Customer;
import com.ex.flipkartclone.entity.RefreshToken;
import com.ex.flipkartclone.entity.Seller;
import com.ex.flipkartclone.entity.User;
import com.ex.flipkartclone.exception.ConstraintViolationException;
import com.ex.flipkartclone.exception.UserNotFoundException;
import com.ex.flipkartclone.exception.UserNotLoggedInException;
import com.ex.flipkartclone.repo.AccessTokenRepo;
import com.ex.flipkartclone.repo.CustomerRepo;
import com.ex.flipkartclone.repo.RefreshTokenRepo;
import com.ex.flipkartclone.repo.SellerRepo;
import com.ex.flipkartclone.repo.UserRepo;
import com.ex.flipkartclone.request_dto.AuthRequest;
import com.ex.flipkartclone.request_dto.OtpModel;
import com.ex.flipkartclone.request_dto.UserRequest;
import com.ex.flipkartclone.response_dto.AuthResponse;
import com.ex.flipkartclone.response_dto.SimpleResponseStructure;
import com.ex.flipkartclone.response_dto.UserResponse;
import com.ex.flipkartclone.security.JwtService;
import com.ex.flipkartclone.service.AuthService;
import com.ex.flipkartclone.util.CookieManager;
import com.ex.flipkartclone.util.MessageStructure;
import com.ex.flipkartclone.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
		
		@Service
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
			private PasswordEncoder passwordEncoder;
			private AuthenticationManager authenticationManager;
			private ResponseStructure<AuthResponse> authStructure;
			private ResponseStructure<String> stringStructure;
			private SimpleResponseStructure simpleStructure;
			private CookieManager cookieManager;
			private JwtService jwtService;
			private AccessTokenRepo accessTokenRepo;
			private RefreshTokenRepo refreshTokenRepo;
			@Value("${myapp.access.expiry}")
			private int accessExpiryInSeconds;
			@Value("${myapp.refresh.expiry}")
			private int refreshExpiryInSeconds;
		
			public AuthServiceImpl(UserRepo userRepo, SellerRepo sellerRepo, CustomerRepo customerRepo,
					ResponseStructure<UserResponse> structure, CacheStore<String> otpCacheStore,
					CacheStore<User> userCacheStore, ResponseStructure<User> userStructure, JavaMailSender javaMailSender,
					PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
					ResponseStructure<AuthResponse> authStructure, ResponseStructure<String> stringStructure,
					SimpleResponseStructure simpleStructure, CookieManager cookieManager, JwtService jwtService,
					AccessTokenRepo accessTokenRepo, RefreshTokenRepo refreshTokenRepo) {
				super();
				this.userRepo = userRepo;
				this.sellerRepo = sellerRepo;
				this.customerRepo = customerRepo;
				this.structure = structure;
				this.otpCacheStore = otpCacheStore;
				this.userCacheStore = userCacheStore;
				this.userStructure = userStructure;
				this.javaMailSender = javaMailSender;
				this.passwordEncoder = passwordEncoder;
				this.authenticationManager = authenticationManager;
				this.authStructure = authStructure;
				this.stringStructure = stringStructure;
				this.simpleStructure = simpleStructure;
				this.cookieManager = cookieManager;
				this.jwtService = jwtService;
				this.accessTokenRepo = accessTokenRepo;
				this.refreshTokenRepo = refreshTokenRepo;
				this.accessExpiryInSeconds = accessExpiryInSeconds;
				this.refreshExpiryInSeconds = refreshExpiryInSeconds;
			}
		
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
				user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
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
		
			@Override
			public ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest,
					HttpServletResponse servletResponse) {
				String username = authRequest.getEmail().split("@")[0];
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
						authRequest.getPassword());
				Authentication authentication = authenticationManager.authenticate(token);
				if (!authentication.isAuthenticated())
					throw new UserNotFoundException("Failed to Authenticate the user", HttpStatus.CONFLICT.value(),
							"User details not found");
				else {
					// generating the cookies and authResponse & returning to client
					return userRepo.findByUsername(username).map(user -> {
						grantAccess(servletResponse, user);
						return ResponseEntity.ok(authStructure.setStatus(HttpStatus.OK.value()).setMessage("Login Successful")
								.setData(AuthResponse.builder().userId(user.getUserId()).username(username)
										.role(user.getUserrole().name()).isAuthenticated(true)
										.accessExpiration(LocalDateTime.now().plusSeconds(accessExpiryInSeconds))
										.refreshExpiration(LocalDateTime.now().plusSeconds(refreshExpiryInSeconds)).build()));
					}).get();
				}
		
			}
		
			private void grantAccess(HttpServletResponse response, User user) {
				// generating access & refresh tokens
				String accessToken = jwtService.generateAccessToken(user.getUsername());
				String refreshToken = jwtService.generateRefreshToken(user.getUsername());
		
				// adding access & refresh token cookies to response
				Cookie atCookie = cookieManager.configureCookie(new Cookie("at", accessToken), accessExpiryInSeconds);
				response.addCookie(atCookie);
				Cookie rtCookie = cookieManager.configureCookie(new Cookie("rt", refreshToken), refreshExpiryInSeconds);
				response.addCookie(rtCookie);
		
				// saving the access & refresh cookie into database
				accessTokenRepo.save(AccessToken.builder().user(user).token(accessToken).isBlocked(false)
						.expiration(LocalDateTime.now().plusSeconds(accessExpiryInSeconds)).build());
				refreshTokenRepo.save(RefreshToken.builder().user(user).token(refreshToken)
						.expiration(LocalDateTime.now().plusSeconds(refreshExpiryInSeconds)).isBlocked(false).build());
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
		
			/*----------------------------------> ToSend login Confirmation <-----------------------------------*/
			public void sendRegistrationMail(User user) throws MessagingException {
				sendMail(MessageStructure.builder().to(user.getEmail()).subject("Login Successfull").sentDate(new Date())
						.text("Welcome, " + user.getUsername() + " to Flipkart " + " Your Registration process is complete <br>"
								+ " Continue Shopping on Flipkart <br> ")
						.build());
			}
		
			public void blockAccessTokens(List<AccessToken> accessTokens) {
				accessTokens.forEach(at -> {
					at.setBlocked(true);
					accessTokenRepo.save(at);
				});
			}
		
			public void blockRefreshTokens(List<RefreshToken> refeshToken) {
				refeshToken.forEach(rt -> {
					rt.setBlocked(true);
					refreshTokenRepo.save(rt);
				});
			}
		
			/*-------------------------------------------> User Logout <---------------------------------------------*/
		
			@Override
			public ResponseEntity<SimpleResponseStructure> logout(HttpServletResponse servletResponse, String accessToken,
					String refeshToken) {
				if (accessToken == null && refeshToken == null)
					throw new UserNotLoggedInException("User with given details not found", HttpStatus.BAD_REQUEST.value(),
							"No such user found");
				AccessToken at = accessTokenRepo.findByToken(accessToken);
				at.setBlocked(true);
				accessTokenRepo.save(at);
				RefreshToken rt = refreshTokenRepo.findByToken(refeshToken);
				rt.setBlocked(true);
				refreshTokenRepo.save(rt);
				servletResponse.addCookie(cookieManager.invalidate(new Cookie("at", "")));
				servletResponse.addCookie(cookieManager.invalidate(new Cookie("rt", "")));
				simpleStructure.setMessage("Logout Successful");
				simpleStructure.setStatus(HttpStatus.GONE.value());
				return new ResponseEntity<SimpleResponseStructure>(simpleStructure, HttpStatus.GONE);
			}
		
			/*-------------------------------------------> User Logout <---------------------------------------------*/
		
		//	@Override
		//	public ResponseEntity<ResponseStructure<String>> logout(HttpServletRequest servletRequest,
		//			HttpServletResponse servletResponse) {
		//		String rt = "";
		//		String at = "";
		//		Cookie[] cookies = servletRequest.getCookies();
		//		for (Cookie cookie : cookies) {
		//			if (cookie.getName().equals("at")) {
		//				at = cookie.getValue();
		//				AccessToken accessToken = accessTokenRepo.findByToken(at);
		//				accessToken.setBlocked(true);
		//				accessTokenRepo.save(accessToken);
		//			}
		//			if (cookie.getName().equals("rt")) {
		//				rt = cookie.getValue();
		//				RefreshToken refreshToken = refreshTokenRepo.findByToken(rt);
		//				refreshToken.setBlocked(true);
		//				refreshTokenRepo.save(refreshToken);
		//			}
		//			servletResponse.addCookie(cookieManager.invalidate(new Cookie(at,"")));
		//			servletResponse.addCookie(cookieManager.invalidate(new Cookie(rt,"")));
		//		}
		//		return new ResponseEntity<ResponseStructure<String>>(stringStructure.setStatus(HttpStatus.GONE.value())
		//				.setMessage("User Logged Out")
		//				.setData("Wish to See you Again"),HttpStatus.GONE);
		//	}
		
			/*---------------------------------> Revoke All Device Access <--------------------------------*/
			@Override
			public ResponseEntity<SimpleResponseStructure> revokeAllDeviceAccess(String accessToken, String refreshToken) {
				String username = SecurityContextHolder.getContext().getAuthentication().getName();
				userRepo.findByUsername(username).ifPresent(user -> {
					System.out.println("user");
					accessTokenRepo.findByUserAndIsBlocked(user, false).forEach(access -> {
						access.setBlocked(true);
						accessTokenRepo.save(access);
					});
					refreshTokenRepo.findByUserAndIsBlocked(user, false).forEach(refresh -> {
						refresh.setBlocked(true);
						refreshTokenRepo.save(refresh);
					});
				});
				return new ResponseEntity<SimpleResponseStructure>(simpleStructure.setStatus(HttpStatus.OK.value())
						.setMessage("All access revoked and logged out from all devices"), HttpStatus.OK);
			}
		
			/*---------------------------------> Revoke Other Device Access <--------------------------------*/
			@Override
			public ResponseEntity<SimpleResponseStructure> revokeOtherDeviceAccess(String accessToken, String refreshToken) {
				String username = SecurityContextHolder.getContext().getAuthentication().getName();
				userRepo.findByUsername(username).ifPresent(user -> {
					blockAccessTokens(accessTokenRepo.findByUserAndIsBlockedAndTokenNot(user, false, accessToken));
					blockRefreshTokens(refreshTokenRepo.findByUserAndIsBlockedAndTokenNot(user, false, refreshToken));
				});
				return new ResponseEntity<SimpleResponseStructure>(simpleStructure
						.setMessage("All other devices logged out successfully").setStatus(HttpStatus.OK.value()),
						HttpStatus.OK);
			}
		
			/*---------------------------------> Refresh Login <--------------------------------*/
		
			@Override
			public ResponseEntity<SimpleResponseStructure> refreshLogin(HttpServletResponse servletResponse, String accessToken,
					String refreshToken) {
				String username = SecurityContextHolder.getContext().getAuthentication().getName();
				userRepo.findByUsername(username).ifPresent(user -> {
					if (accessToken == null) {
						grantAccess(servletResponse, user);
					} else
						blockAccessTokens(accessTokenRepo.findByUserAndIsBlockedAndTokenNot(user, false, accessToken));
					if (refreshToken == null)
						throw new UserNotLoggedInException("User not logged-In", HttpStatus.BAD_REQUEST.value(), "");
					else {
						blockRefreshTokens(refreshTokenRepo.findByUserAndIsBlockedAndTokenNot(user, false, refreshToken));
						grantAccess(servletResponse, user);
					}
				});
		
				return new ResponseEntity<SimpleResponseStructure>(
						simpleStructure.setMessage("Tokens Refreshed").setStatus(HttpStatus.OK.value()), HttpStatus.OK);
			}
		
		}