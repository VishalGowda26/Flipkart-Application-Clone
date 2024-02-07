package com.ex.flipkartclone.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@AllArgsConstructor
@Getter
@Setter
public class ConstraintViolationException extends RuntimeException {
	private String message;
	private int status;
	private String rootcause;
}
