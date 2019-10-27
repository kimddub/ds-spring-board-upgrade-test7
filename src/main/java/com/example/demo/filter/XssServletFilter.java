package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jline.internal.Log;

public class XssServletFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
        Log.info(
          "Starting a transaction for req : {}", 
          req.getRequestURI());
  
        chain.doFilter(request, response);
        
        Log.info(
          "Committing a transaction for req : {}", 
          req.getRequestURI());
		
	}
	
	

}
