package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;
import com.example.demo.dto.CKEditorImg;

@Mapper
public interface ArticleDao {
	public void insert(Map<String, Object> param);

	public List<Article> getList(Map<String, Object> param);

	public Article getOne(long id);

	public void update(Map<String, Object> param);

	public void updateToDelMode(Map<String, Object> param);

	public int totalCount(Map<String, Object> param);

	public void insertFiles(String prefix, String originalFileName, Object articleId, String type);

	public List<ArticleFile> getArticleFiles(long articleId);

	public ArticleFile getOneFile(long id);

	// 꼭 이해하기!!!!!!!!!!!!!!!!
	public void deleteFile(@Param("deleteFiles") String[] deleteFiles);

	public void deleteArticleFiles(Map<String, Object> param);

	public CKEditorImg getCKEditorImg(long id);

	public void insertCKEditorImg(CKEditorImg img);

}
