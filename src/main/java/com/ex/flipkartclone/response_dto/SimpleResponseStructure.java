package com.ex.flipkartclone.response_dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class SimpleResponseStructure {
	private String message;
	private int status;
	public String getMessage() {
		return message;
	}
	public SimpleResponseStructure setMessage(String message) {
		this.message = message;
		return this;
	}
	public int getStatus() {
		return status;
	}
	public SimpleResponseStructure setStatus(int status) {
		this.status = status;
		return this;
	}
	
	

}
