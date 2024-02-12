package com.ex.flipkartclone.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ex.flipkartclone.entity.AccessToken;
import com.ex.flipkartclone.exception.ConstraintViolationException;
import com.ex.flipkartclone.exception.UserNotLoggedInException;
import com.ex.flipkartclone.repo.AccessTokenRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private AccessTokenRepo accessTokenRepo;
	private JwtService jwtService;
	private CustomUserDetailService customUserDetails;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String at = null;
		String rt = null;
		Cookie[] cookies = request.getCookies();
		log.info("In Jwt filter");
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("at"))
					at = cookie.getValue();
				if (cookie.getName().equals("rt"))
					rt = cookie.getValue();
			}
			log.info("Trying Authenticating the token.....");
			String username = null;
			if (at != null && rt != null) {
				Optional<AccessToken> accesstoken = accessTokenRepo.findByTokenAndIsBlocked(at, false);
				if (accesstoken == null)
					throw new UserNotLoggedInException("Token is blocked", HttpStatus.BAD_REQUEST.value(),
							"User not logged in");
				else {
					log.info("Authenticating the token.....");
					username = jwtService.extractUsername(at);
					if (username == null)
						throw new ConstraintViolationException("Failed to Authenticate", HttpStatus.BAD_REQUEST.value(),
								"");
					UserDetails userDetails = customUserDetails.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							username, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					log.info("Authenticated Succesfully");
				}
				
			}
		}
		filterChain.doFilter(request, response);
	}

}
