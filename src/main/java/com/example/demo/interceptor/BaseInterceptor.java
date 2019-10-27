package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.dto.Member;
import com.example.demo.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("BaseInterceptor")
public class BaseInterceptor implements HandlerInterceptor {
	@Autowired
	MemberService memberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//request.getSession().removeAttribute("loginedMemberId");
		
		log.info("PreHandler is executed");

		if (request.getSession().getAttribute("loginedMemberId") == null) {

			request.setAttribute("isLogined", false);
			request.setAttribute("loginedMemberId", 0);
			request.setAttribute("loginedMemberLoginId", "");
			request.setAttribute("isAdminAuth", false);
			request.setAttribute("loginedMember", null);

		} else {
			int loginedMemberId = (int) request.getSession().getAttribute("loginedMemberId");
			Member member = memberService.getMemberByMemberId(loginedMemberId);
			
			if (member == null) {
				request.getSession().removeAttribute("loginedMemberId");
			
				log.info("logined member is not exists");
			}

			request.setAttribute("isLogined", true);
			request.setAttribute("loginedMemberId", loginedMemberId);
			request.setAttribute("loginedMemberLoginId", member.getLoginId());
			request.setAttribute("loginedMember", member);
			
			if ( member.getAuthCode() == 0 ) {
				request.setAttribute("isAdminAuth", true);
			} else {
				request.setAttribute("isAdminAuth", false);
			}

		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
