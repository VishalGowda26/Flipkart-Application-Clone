package com.ex.flipkartclone.request_dto;

import com.ex.flipkartclone.enums.Priority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ContactRequest {

    private String name;
    private long contactNumber;
    private Priority priority;

}

