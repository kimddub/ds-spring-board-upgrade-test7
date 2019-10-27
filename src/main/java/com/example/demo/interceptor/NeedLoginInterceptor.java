package com.example.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("NeedLoginInterceptor")
public class NeedLoginInterceptor implements HandlerInterceptor {

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("location : " + request.getRequestURL());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		if(request.getSession().getAttribute("loginedMemberId") == null) {
			response.getWriter().append("<script>");
			response.getWriter().append("alert('로그인이 필요한 서비스 입니다.');");
			response.getWriter().append("location.replace('/member/login');");
			response.getWriter().append("</script>");
			response.getWriter().close();
			return false;			
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
    }
	
}