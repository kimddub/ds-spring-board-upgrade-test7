<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="내용"/>


<c:if test="${isAdminAuth == true || article.memberId == loginedMemberId}">
	<c:set var="isAccessible" value="true"/>
</c:if>

<%@ include file="../part/head.jspf"%>
<link rel="stylesheet" href="/resource/css/detail/detail.css">

<script>
	function confirmArticleDelete(id) {
		
		if (confirm('정말로 삭제하시겠습니까?') == false) {
			return false;
		}

		location.href='./doDelete?id=' + id + '&${pageInfo}';
	}
</script>
	
	<section class="con common-form-box">
		
		<table class="common-table common-form">
			<colgroup>
				<col width="150">
				<col>
			</colgroup>
			<tbody>
				<tr>
					<th>제목</th>
					<td colspan="5">
						<c:out value="${article.title}" escapeXml="true"></c:out>
					</td>
				</tr>
				<tr>
					<th>작성자</th>
					<td>
						<c:out value="${article.writer}" escapeXml="true"></c:out>
					</td>
					<th>등록일</th>
					<td>
					${article.regDate}</td>
					<th>수정일</th>
					<td>${article.modDate}</td>
				</tr>
				<tr>
					<th>내용</th>
					<td colspan="5">
						<div class="article-body">
							${article.body}
						</div>
						<c:forEach var="imgFile" items="${articleImgFiles}">
							<div class="img-box">
								<img src="./showImg?id=${imgFile.id}" alt="${imgFile.originalFileName}">
							</div>
						</c:forEach>
					</td>
				</tr>
				<tr>
					<th>첨부파일</th>
					<td colspan="5">
						
						<c:forEach var="file" items="${articleFiles}">
							<span class="download-file" onclick="location.href='./';">
								<a href="./downloadFile?id=${file.id}">
									<i class="fa fa-file"></i>
									${file.originalFileName}
								</a>
							</span><br/>
						</c:forEach>
					</td>
				</tr>
			</tbody>
		
		</table>
		
		<div class="common-form-menu">
			<span class="common-form-menu-btn">
				<input class="common-btn" type="button" value="목록" onclick="location.href='./list?${pageInfo}';">
				
				<c:if test="${isAccessible}">
					<input class="common-btn" type="button" value="수정" onclick="location.href='./modify?id=${article.id}&${pageInfo}';">
					<input class="common-btn" type="button" value="삭제" onclick="confirmArticleDelete(${article.id});">
				</c:if>
			</span>
		</div>
	
	</section> 


<%@ include file="../part/foot.jspf"%>