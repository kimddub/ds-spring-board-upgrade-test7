package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CKEditorImg {
	private int id;
	private String regDate;
	private String prefix;
	private String originalFileName;
	private String type;
}
