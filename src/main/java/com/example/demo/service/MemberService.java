package com.example.demo.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.dto.Member;

public interface MemberService {
	
	public Member getMemberByMemberId(int memberId);
	
	public Member getMemberByLoginId(String loginId);

	public Map<String, Object> join(Map<String, Object> param);

	public Map<String, Object> login(Map<String, Object> param, HttpServletRequest request);

	public Map<String, Object> logout(HttpServletRequest request);

}
