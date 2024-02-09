package com.ex.flipkartclone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@AllArgsConstructor
@Getter
@Setter
public class UserNotFoundException extends RuntimeException {
	private String message;
	private int status;
	private String rootcause;

}
