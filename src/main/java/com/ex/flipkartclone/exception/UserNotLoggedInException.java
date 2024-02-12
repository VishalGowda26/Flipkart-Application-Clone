package com.ex.flipkartclone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
public class UserNotLoggedInException extends RuntimeException {
	private String message;
	private int status;
	private String rootcause;
}
