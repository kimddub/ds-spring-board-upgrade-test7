package com.example.demo.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.ArticleDao;
import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;

@Service("ArticleService")
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	ArticleDao articleDao;
	@Value("${custom.uploadDir}")
	private String filePath;
	
	private List<String[]> uploadFiles(List<MultipartFile> files) {
		List<String[]> rs = new ArrayList<>();
		
		for (MultipartFile file : files) {
			
			if (!file.getOriginalFilename().equals("")) {
				String[] fileName = new String[3];
				
				UUID uid = UUID.randomUUID();
				
				fileName[0] = uid.toString() + "-";
				fileName[1] = file.getOriginalFilename();
				
				int dotIdx = fileName[1].lastIndexOf( "." );
				fileName[2] = fileName[1].substring( dotIdx + 1 );
				
				File target = new File(filePath, fileName[0] + fileName[1]);
				
				try {
					
					FileOutputStream fos = new FileOutputStream(target);

					fos.write(file.getBytes());
					fos.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				rs.add(fileName);
			}
			
		}
		
		return rs;
	}
	
	@Override
	public List<Article> getList(Map<String, Object> param){
		return articleDao.getList(param);
	}
	
	@Override
	public Article getOne(long id) {
		return articleDao.getOne(id);
	}
	
	@Override
	public Map<String,Object> add(Map<String, Object> param) {
		
		Map<String,Object> rs = new HashMap<>();
		articleDao.insert(param);
		
		long addedArticlesId = (long)param.get("id");
		
		rs.put("id", addedArticlesId);
		rs.put("alertMsg", addedArticlesId + "번 게시물을 추가했습니다.");
		rs.put("resultCode", "S-1");
		
		return rs;
	}
	
	@Override
	public void addFiles(List<MultipartFile> files, Object articleId) {
		
		List<String[]> filesInfo = uploadFiles(files);
		
		if ( filesInfo != null && filesInfo.size() > 0 ) {
			
			for (String[] fileInfo : filesInfo) {

				// prefix, originalalFileName, articleId, fileType 
				articleDao.insertFiles(fileInfo[0],fileInfo[1],articleId,fileInfo[2]);
			}
		}
	}
	
	@Override
	public List<ArticleFile> getArticleFiles(long articleId) {
		return articleDao.getArticleFiles(articleId);
	}
	
	@Override
	public List<ArticleFile> getArticleImgFiles(long articleId) {
		
		return articleDao.getArticleImgFiles(articleId);
	}
	
	@Override
	public ArticleFile getOneFile(long id) {
		return articleDao.getOneFile(id);
	}
	
	@Override
	public void deleteFiles(String[] deleteFiles) {
		articleDao.deleteFile(deleteFiles);
	}
	
	@Override	
	public Map<String, Object> modify(Map<String, Object> param) {
		Map<String,Object> rs = new HashMap<>();
		
		articleDao.update(param);
		
		rs.put("alertMsg", "게시물을 성공적으로 수정했습니다.");
		rs.put("resultCode", "S-1");
		
		return rs;
	}
	
	@Override
	public Map<String, Object> delete(Map<String, Object> param) {
		Map<String,Object> rs = new HashMap<>();
		
		articleDao.updateToDelMode(param);
		articleDao.deleteArticleFiles(param);
		
		rs.put("alertMsg", "게시물을 삭제했습니다.");
		rs.put("resultCode", "S-1");
		
		return rs;
	}
	
	public int getTotalCount(Map<String, Object> param) {
		return articleDao.totalCount(param);
	}
}
