package com.example.demo.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;
import com.example.demo.dto.CKEditorImg;

public interface ArticleService {

	public List<Article> getList(Map<String, Object> param);

	public Article getOne(long id);

	public Map<String,Object> add(Map<String, Object> param, HttpServletRequest request);

	public void addFiles(List<MultipartFile> files, Object articleId);

	public Map<String, Object> modify(Map<String, Object> param);

	public Map<String, Object> delete(Map<String, Object> param);

	public int getTotalCount(Map<String, Object> param);

	public List<ArticleFile> getArticleFiles(long articleId);

	public ArticleFile getOneFile(long id);

	public void deleteFiles(String[] deleteFiles);

	public Map<String, Object> addCKeditorImg(MultipartFile file);

	public CKEditorImg getCKEditorImg(long id);

}
