package com.example.demo.dto;

import java.util.Map;

import groovy.json.StringEscapeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 롬복 어노테이션
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
	private int id;
	private String regDate;
	private String modDate;
	private String title;
	private String body;
	private int boardId;
	private String writer;
	private int delState;
	private Map<String, Object> extra;

	public String getBodyForPrint() {
		
		  return body.replaceAll("&", "&amp;")
				    .replaceAll("<", "&lt;")
				    .replaceAll(">", "&gt;")
				    .replaceAll("\\\\\"", "&quot;")
				    .replaceAll("'", "&#x27;")
				    .replaceAll("/", "&#x2F;")
				    .replaceAll("\\n", "<br/>");

	}
}
