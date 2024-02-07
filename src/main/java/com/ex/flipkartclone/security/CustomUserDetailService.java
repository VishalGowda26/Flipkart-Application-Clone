package com.ex.flipkartclone.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ex.flipkartclone.repo.UserRepo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username).map(user -> new CustomUserDetails(user))
				.orElseThrow(() -> new UsernameNotFoundException("Failed to authenticate the user"));
	}

}
