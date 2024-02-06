package com.ex.flipkartclone.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ResponseStructure<T> {
	private T data;
	private String message;
	private int status;
}
