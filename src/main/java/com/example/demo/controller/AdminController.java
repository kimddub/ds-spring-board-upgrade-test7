package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.ArticleService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	ArticleService articleService;

	@RequestMapping("/doDeleteArticles")
	public String doDeleteArticles(String[] articles,@RequestParam Map<String,Object> param, Model model, HttpServletRequest request) {
	
		int failedToDeleteArticles = 0;
		
		for (String articleId:articles) {
			
			param.put("id", articleId);

			Map<String,Object> rs = articleService.delete(param);
			
			if (((String)rs.get("resultCode")).startsWith("F")) {
				failedToDeleteArticles++;
			}
		}
		
		String redirectUrl = "../article/list?";
		
		if (request.getQueryString() != null) {
			try {
				redirectUrl += "&" + URLDecoder.decode(request.getQueryString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}
		
		model.addAttribute("alertMsg", failedToDeleteArticles + "건의 글삭제에 실패했습니다.");
		model.addAttribute("redirectUrl", redirectUrl); // 페이징/검색 조건 유지
		
		return "common/redirect";
	}
	
}
