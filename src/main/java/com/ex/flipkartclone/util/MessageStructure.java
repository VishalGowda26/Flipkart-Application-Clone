package com.ex.flipkartclone.util;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageStructure {
	private String to;
	private String subject;
	private Date sentDate;
	private String text;

}
