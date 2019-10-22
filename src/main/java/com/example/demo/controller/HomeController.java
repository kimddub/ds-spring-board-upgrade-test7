package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@RequestMapping("")
	public String showMain() {
		return "home/main";
	}
	
	@RequestMapping("home/main")
	public String showMain2() {
		return "home/main";
	}
	
}
