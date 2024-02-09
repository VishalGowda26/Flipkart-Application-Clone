package com.ex.flipkartclone.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieManager {

	@Value("${myapp.domain}")
	private String domain;

	/*-------------------------------> Method to configure cookie <------------------------------------*/

	public Cookie configureCookie(Cookie cookie, int expirationInSecounds) {
		cookie.setDomain(domain);
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(expirationInSecounds);
		return cookie;
	}
	
	/*-------------------------------> Method to remove cookie from browser <------------------------------------*/
	
	public Cookie invalidate(Cookie cookie) {
		cookie.setPath("/");
		cookie.setMaxAge(0);
		return cookie;
	}
}
