package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.ArticleDao;
import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;
import com.example.demo.dto.CKEditorImg;
import com.example.demo.dto.Member;

@Service("ArticleService")
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	ArticleDao articleDao;
	@Value("${custom.uploadDir}")
	private String filePath;
	
	
	@Override
	public List<Article> getList(Map<String, Object> param){
		return articleDao.getList(param);
	}
	
	@Override
	public Article getOne(long id) {
		return articleDao.getOne(id);
	}
	
	@Override
	public Map<String,Object> add(Map<String, Object> param, HttpServletRequest request) {
		
		Map<String,Object> rs = new HashMap<>();
		
		Member member = (Member)request.getAttribute("loginedMember");
		
		param.put("memberId", member.getId());
		param.put("writer", member.getName());
		articleDao.insert(param);
		
		long addedArticlesId = (long)param.get("id");
		
		rs.put("id", addedArticlesId);
		rs.put("alertMsg", addedArticlesId + "번 게시물을 추가했습니다.");
		rs.put("resultCode", "S-1");
		
		return rs;
	}

	private String[] uploadFile(MultipartFile file,String path) {
		if (!file.getOriginalFilename().equals("")) {
			String[] fileName = new String[3];
			
			UUID uid = UUID.randomUUID();
			
			fileName[0] = uid.toString() + "-";
			fileName[1] = file.getOriginalFilename();
			
			int dotIdx = fileName[1].lastIndexOf( "." );
			fileName[2] = fileName[1].substring( dotIdx + 1 );
			
			File target = new File(path, fileName[0] + fileName[1]);
			
			try {
				
				FileOutputStream fos = new FileOutputStream(target);

				fos.write(file.getBytes());
				fos.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return fileName;
			
		} else {
			return null;
		}
		
	}
	
	private List<String[]> uploadFiles(List<MultipartFile> files, String path) {
		List<String[]> rs = new ArrayList<>();
		
		for (MultipartFile file : files) {

			rs.add(uploadFile(file, path));
			
		}
		
		return rs;
	}
	
	@Override
	public void addFiles(List<MultipartFile> files, Object articleId) {
		
		
		List<String[]> filesInfo = uploadFiles(files, filePath);
		
		if ( filesInfo != null && filesInfo.size() > 0 ) {
			
			for (String[] fileInfo : filesInfo) {
				
				// prefix, originalalFileName, articleId, fileType 
				articleDao.insertFiles(fileInfo[0],fileInfo[1],articleId,fileInfo[2]);
			}
		}
	}
	
	@Override
	public Map<String, Object> addCKeditorImg(MultipartFile file) {
		
		Map<String,Object> rs = new HashMap<>();

		// prefix, originalalFileName, fileType 
		String[] fileInfo = uploadFile(file, filePath + "/ckeditor");
		
		if ( fileInfo != null ) {
			CKEditorImg img = new CKEditorImg();
			
			img.setPrefix(fileInfo[0]);
			img.setOriginalFileName(fileInfo[1]);
			img.setType(fileInfo[2]);
			
			articleDao.insertCKEditorImg(img);
			
			//if (img.getId())?
			
		    rs.put("uploaded", 1);
		    rs.put("fileName", fileInfo[1]);
		    rs.put("url", "http://localhost:8017/article/showCKEditorImg?id=" + img.getId());
		    rs.put("message", "업로드 완료");

			return rs;
			
		} else {
			rs.put("uploaded", 0);
			rs.put("message", "업로드 실패");
		}
		
		return rs;
	}
	
	@Override
	public List<ArticleFile> getArticleFiles(long articleId) {
		return articleDao.getArticleFiles(articleId);
	}
	
	@Override
	public ArticleFile getOneFile(long id) {
		return articleDao.getOneFile(id);
	}
	
	@Override
	public CKEditorImg getCKEditorImg(long id) {
		return articleDao.getCKEditorImg(id);
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
		
		try {
			articleDao.updateToDelMode(param);
			articleDao.deleteArticleFiles(param);
			
		} catch(Exception e) {
			rs.put("alertMsg", "게시물을 삭제에 실패했습니다.");
			rs.put("resultCode", "F-1");
			
		}
		
		rs.put("alertMsg", "게시물을 삭제했습니다.");
		rs.put("resultCode", "S-1");
		
		return rs;
	}
	
	public int getTotalCount(Map<String, Object> param) {
		return articleDao.totalCount(param);
	}
}
