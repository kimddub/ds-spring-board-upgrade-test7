package com.example.demo.dto;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	private int id;
	private String regDate;
	private String loginId;
	private String encryptedLoginPw;
	private String encryptingSalt;
	private String name;
	private int authCode;
	private boolean withdrawal;
}
