package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Page;
import com.example.demo.PageMaker;
import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;
import com.example.demo.service.ArticleService;

import groovy.util.logging.Slf4j;

@Slf4j
@Controller
public class ArticleController {
	@Autowired
	ArticleService articleService;
	@Autowired
	ResourceLoader resourceLoader;
	@Value("${custom.uploadDir}")
	private String filePath;
	
	// =====기능=====
	public List<Article> getList(Map<String, Object> param) {
		return articleService.getList(param);
	}	
	
	public Article getOne(int id) {
		return articleService.getOne(id);
	}	
	
	public boolean isNotAccessArticle(int id) {
		Article article = getOne(id);
		
		if (article == null || article.getDelState() == 1) {
			return true;
		} 
		
		return false;
	}
	
	public String getArticleAccessMsg(int id) {
		Article article = getOne(id);
		
		String alertMsg = "";
		
		
		if (article == null) {

			alertMsg = "해당 게시물이 존재하지 않습니다.";
			
		} else if (article.getDelState() == 1) {
			
			alertMsg = "삭제된 게시물입니다.";
			
		} // 글에 대한 권한이 있는지 확인 추가
			
		return alertMsg;
	}	
	// =====기능=====
	
	@RequestMapping("article/list")
	public String showList(Page page, Model model,@RequestParam Map<String,Object> param, HttpServletRequest request) {
						
		// 세상에 cPage라는 변수로 들어오는 값이
		// page 객체 안의 cPage 필드에도 반영된다
		// 그래서 0이 셋팅과정 없이 입력되는 것 방지
		if (page.getcPage() < 1) {
			page.setcPage(1);
		}
		
		// 나중엔 URL에 계속 들어있으므로 문장이 "" 인지 비교
		if (param.containsKey("searchType") && param.containsKey("searchKey")) {
			param.put("searchMode", "true");
		}
		
		PageMaker pageMaker = new PageMaker(page, articleService.getTotalCount(param));
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("cPage", page.getcPage());
		
		param.put("prevPageArticles", (long)page.getPrevPageArticles());
		param.put("perPageArticles", (long)page.getPerPageArticles());
		
		List<Article> articles = getList(param);
		model.addAttribute("articles", articles);
		
		return "article/list";
	}	
		
	@RequestMapping("article/add")
	public String showAdd() {
		return "article/add";
	}
	
	@RequestMapping("article/doAdd")
	public String doAdd(@RequestParam Map<String,Object> param, 
			@RequestParam(value="files") List<MultipartFile> files,
			Model model) {
		
		Map<String,Object> rs = articleService.add(param);
		articleService.addFiles(files, param.get("id"));
		
		String resultCode = (String) rs.get("resultCode");
		
		if (resultCode.startsWith("S-")) {

			model.addAttribute("alertMsg", rs.get("alertMsg"));
			model.addAttribute("redirectUrl", "./list?cPage=1"); // 페이징/검색 조건 유지
		} else {
			
			model.addAttribute("alertMsg", "게시물 작성에 실패했습니다.");
			model.addAttribute("historyBack", true);
		}
		
		return "common/redirect";
	}
	
	@RequestMapping("article/detail")
	public String showDetail(@RequestParam Map<String,Object> param, Model model) {
		
		String strId = (String)param.get("id");
		int id = Integer.parseInt(strId);
		
		// 접근 불가능한 게시물의 메시지 타입 읽어옴
		if (isNotAccessArticle(id)) {
			
			model.addAttribute("alertMsg", getArticleAccessMsg(id));
			model.addAttribute("historyBack", "true");
			
			return "common/redirect";
		}
		
	
		Article article = getOne(id);
		List<ArticleFile> articleFiles = articleService.getArticleFiles(id);
		List<ArticleFile> articleImgFiles = articleService.getArticleImgFiles(id);
				
		model.addAttribute("article", article);
		model.addAttribute("articleFiles", articleFiles);
		model.addAttribute("articleImgFiles", articleImgFiles);
		
		return "article/detail";
	}
	
	@RequestMapping("article/showImg")
	public ResponseEntity<Resource> showImg(long id, HttpServletRequest request) throws IOException{
		// ResponseEntity 이용 방법1
		ArticleFile articleFile = articleService.getOneFile(id);
		
		File target = new File(filePath, articleFile.getPrefix()+articleFile.getOriginalFileName());		
		Resource resource = null;
		String mimeType = null;	
		HttpHeaders header = new HttpHeaders();

		if(target.exists()) {
			
			resource = new UrlResource(target.toURI());
			mimeType = Files.probeContentType(Paths.get(resource.getFilename()));
			
			if(mimeType == null) {
				mimeType = "application/octet-stream";
			}		
			
			header.setContentType(MediaType.parseMediaType(mimeType));
		}
		
		
		// resource, multiValueMap, httpStatus
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	@RequestMapping("article/downloadFile")
	public ResponseEntity<Resource> downloadFile(long id) throws IOException {
		// ResponseEntity 이용 방법2
		ArticleFile articleFile = articleService.getOneFile(id);
		
		File target = new File(filePath, articleFile.getPrefix()+articleFile.getOriginalFileName());		
		Resource resource = null;
		String mimeType = null;	
		
		if(target.exists()) {
			
			resource = new UrlResource(target.toURI());
			mimeType = Files.probeContentType(Paths.get(resource.getFilename()));
			
			if(mimeType == null) {
				mimeType = "application/octet-stream";
			}		
			
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(mimeType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() +"\"")
					.body(resource);
		}
		
		return null;
		
		// resource 말고 byte나 file 객체를 자원으로 이용하는 방법도 있음
		// httpservletresponse 이용하는 방법도 있음
		
//		File target = new File(filePath, articleFile.getPrefix()+articleFile.getOriginalFileName());		
//		Resource rs = null;
//		String mimeType = null;	
	}
	
	@RequestMapping("article/modify")
	public String showModify(@RequestParam Map<String,Object> param, Model model) {
		
		String strId = (String)param.get("id");
		int id = Integer.parseInt(strId);
		
		// 접근 불가능한 게시물의 메시지 타입 읽어옴
		if (isNotAccessArticle(id)) {
			
			model.addAttribute("alertMsg", getArticleAccessMsg(id));
			model.addAttribute("historyBack", "true");
			
			return "common/redirect";
		}
	
		Article article = getOne(id);
		List<ArticleFile> articleFiles = articleService.getArticleFiles(id);
				
		model.addAttribute("article", article);
		model.addAttribute("articleFiles", articleFiles);
		
		return "article/modify";
	}
	
	@RequestMapping("article/doModify")
	public String doModify(@RequestParam Map<String,Object> param, 
			@RequestParam(value="newFiles") List<MultipartFile> newFiles,
			@RequestParam(value="deleteFiles", required=false) String[] deleteFiles,
			Model model, HttpServletRequest request) {
		
		String strId = (String)param.get("id");
		int id = Integer.parseInt(strId);
		
		// 접근 불가능한 게시물의 메시지 타입 읽어옴
		if (isNotAccessArticle(id)) {
			
			model.addAttribute("alertMsg", getArticleAccessMsg(id));
			model.addAttribute("historyBack", "true");
			
			return "common/redirect";
		}
		
		Map<String,Object> rs = articleService.modify(param);
		
		System.out.println("새로 추가하는 파일 : " + newFiles.size());
		
		if (newFiles.size() != 0) {
			articleService.addFiles(newFiles, id);
		}
		
		if (deleteFiles != null) {
			articleService.deleteFiles(deleteFiles);
		}
		
		String resultCode = (String) rs.get("resultCode");
		String redirectUrl = "/article/detail?id=" + id; // 나머진 jsp에 파라미터 유지된 채로 받을 수 있다.
				
		if (resultCode.startsWith("S-")) {

			model.addAttribute("alertMsg", rs.get("alertMsg"));
			model.addAttribute("redirectUrl", redirectUrl); // 페이징/검색 조건 유지
		} else {
			
			model.addAttribute("alertMsg", "게시물 수정에 실패했습니다.");
			model.addAttribute("historyBack", true);
		}
		
		return "common/redirect";
		
		// JSP로 안보내고 컨트롤러로 바로 보낼 때 URL 인코딩 작업 후 요청해야 한다. (redirect)
		// JSP로 보내면 파라미터 계속 유지되고 있으므로 직접 작성하지 않아도 (O), 인코딩도 (O)
		// 1. redirectUrl += "&" + request.getQueryString(); // 인코딩 된 상태
			// System.out.println(redirectUrl);
			//./detail?id=141&searchType=title&searchKey=%EC%A0%9C&cPage=1
			// URLDecoder.decode(request.getQueryString(), "UTF-8"); 하면 한글로 디코딩 된 상태로 가져옴
		// 2. redirectUrl + "&searchType=" + URLDecoder.decode(param.get("searchType"),"UTF-8"); // + ...
	}
	
	@RequestMapping("article/doDelete")
	public String doDelete(@RequestParam Map<String,Object> param, Model model, HttpServletRequest request) {
		String strId = (String)param.get("id");
		int id = Integer.parseInt(strId);
		
		// 접근 불가능한 게시물의 메시지 타입 읽어옴
		if (isNotAccessArticle(id)) {
			
			model.addAttribute("alertMsg", getArticleAccessMsg(id));
			model.addAttribute("historyBack", "true");
			
			return "common/redirect";
		}
		
		Map<String,Object> rs = articleService.delete(param);
		
		String resultCode = (String) rs.get("resultCode");
		
		String redirectUrl = "./list?";
		
		try {
			redirectUrl += "&" + URLDecoder.decode(request.getQueryString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
//				+ "&searchType=" + param.get("searchType")
//				+ "&searchKey=" + param.get("searchKey");
		
		if (resultCode.startsWith("S-")) {

			model.addAttribute("alertMsg", rs.get("alertMsg"));
			model.addAttribute("redirectUrl", redirectUrl); // 페이징/검색 조건 유지
		} else {
			
			model.addAttribute("alertMsg", "게시물 수정에 실패했습니다.");
			model.addAttribute("historyBack", true);
		}
		
		return "common/redirect";
	}
}
