package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.filter.XssServletFilter;
//import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	@Qualifier("BaseInterceptor")
	private HandlerInterceptor baseInterceptor;
	
	@Autowired
	@Qualifier("NeedLoginInterceptor")
	private HandlerInterceptor needLoginInterceptor;

	@Autowired
	@Qualifier("NeedLogoutInterceptor")
	private HandlerInterceptor needLogoutInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(baseInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**");
		
		registry.addInterceptor(needLoginInterceptor).addPathPatterns("/**")
		.excludePathPatterns("/", "/home/main")
		.excludePathPatterns("/article/list", "/article/detail")
		.excludePathPatterns("/article/downloadFile", "/article/showImg")
		.excludePathPatterns("/article/showCKEditorImg","/article/getAllReplies")
		.excludePathPatterns("/member/logout", "/member/join", "/member/doJoin")
		.excludePathPatterns("/member/login", "/member/doLogin","/member/checkExistedDuplId")
		.excludePathPatterns("/resource/**", "/common/**");
		
		// needLogout
		registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/member/login").addPathPatterns("/member/doLogin")
				.addPathPatterns("/member/join").addPathPatterns("/member/doJoin");

		// 이렇게만 해도 아래처럼 작동됨
		// registry.addInterceptor(specialInterceptor).addPathPatterns("/**");
	}

}

/*
@Bean
public FilterRegistrationBean getFilterRegistrationBean() {
  FilterRegistrationBean registrationBean = new FilterRegistrationBean(new XssEscapeServletFilter());
  registrationBean.setOrder(Integer.MIN_VALUE);
  registrationBean.addUrlPatterns("/*");
  return registrationBean;
}

@Bean
public FilterRegistrationBean<XssServletFilter> loggingFilter(){
  FilterRegistrationBean<XssServletFilter> registrationBean 
    = new FilterRegistrationBean<>();
       
  registrationBean.setFilter(new XssServletFilter());
  registrationBean.addUrlPatterns("/article/*");
       
  return registrationBean;    
}
*/