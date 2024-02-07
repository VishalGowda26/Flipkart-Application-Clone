package com.ex.flipkartclone.util;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class ResponseStructure<T> {
	private T data;
	private String message;
	private int status;
	public T getData() {
		return data;
	}
	public ResponseStructure<T> setData(T data) {
		this.data = data;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public ResponseStructure<T> setMessage(String message) {
		this.message = message;
		return this;
	}
	public int getStatus() {
		return status;
	}
	public ResponseStructure<T> setStatus(int status) {
		this.status = status;
		return this;
	}
	
	
	
	
	
	
}
