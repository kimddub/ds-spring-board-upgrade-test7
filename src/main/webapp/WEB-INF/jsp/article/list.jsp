<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="목록"/>
<%@ include file="../part/head.jspf"%><link rel="stylesheet" href="/css/list/list.css">

	<section class="con article-box">
		<div class="article-menu row">
		
			<div class="search-box cell">
			
				<form action="./list" method="GET">
				
					<%-- <input type="hidden" name="boardId" value="${param.boardId}"> --%>
				
					
					<div class="search">
						
						<select name="searchType" class="searchSelect">
							<option value="title">제목</option>
							<option value="body">내용</option>
							<option value="writer">작성자</option>
						</select>
					
						<input type="text" name="searchKey" class="searchTerm" placeholder="검색어를 입력해보세요!"/>
						<button type="submit" class="searchButton">
							<i class="fa fa-search"></i>
						</button>
				   </div>
					
					<c:if test="${param.searchType != null && param.searchType != '' }">
						<script>
							$('select[name="searchType"]').val('${param.searchType}');
							$('select[name="searchType"]').css('background-color','#F0F8FF');
						</script>
					</c:if>
					
					<c:if test="${param.searchKey != null && param.searchKey != '' }">
						<script>
							$('input[name="searchKey"]').attr("placeholder",'"${param.searchKey}" 검색 결과');
							$('input[name="searchKey"]').css('background-color','#F0F8FF');
						</script>
					</c:if>
				</form>
				
			</div>
			<button class="common-btn cell-right" onclick="location.href='./add?${pageInfo}';">
				<i class="fa fa-pencil"></i>
			</button>
			
		</div>
		
		<table class="common-table">
			<colgroup>
				<col width="70">
				<col>
				<col width="150">
				<col width="230">
				<col width="230">
			</colgroup>
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>작성자</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
					<c:forEach var="article" items="${articles}">
						<tr class="accessible" onclick="location.href='./detail?id=${article.id}&${pageInfo}';"> 
							<td><c:out value="${article.id}"></c:out></td>
							<td><c:out value="${article.title}"></c:out></td>
							<td><c:out value="${article.writer}"></c:out></td>
							<td><c:out value="${article.extra.onlyRegDate}"></c:out></td>
						</tr>
					</c:forEach>
				
				<c:if test="${param.cPage > pageMaker.limitPage}">
					<tr height="300">
						<td colspan="4">게시물이 존재하지 않습니다.</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		
	</section>
	
	<nav class="con paging-section">
		<div class="page-box">
		
			<ul class="row">
				
				<li class="cell page-btn">
					<c:if test="${pageMaker.prev }">
					<a href="./list?cPage=${cPage-1}&${searchInfo}"><i class="fas fa-angle-double-left"></i></a>
					</c:if>
				</li>
			
				<c:forEach begin="${pageMaker.startPage}" end="${pageMaker.endPage}" var="idx">
					<c:if test="${idx == cPage}">
						<li class="cell current-page" >
							<a href="./list?cPage=${idx}&${searchInfo}">${idx}</a>
						</li>
					</c:if>
					
					<c:if test="${idx != cPage}">
						<li class="cell page-index">
							<a href="./list?cPage=${idx}&${searchInfo}">${idx}</a>
						</li>
					</c:if>
				</c:forEach>
			
				<li class="cell page-btn">
					<c:if test="${pageMaker.next }"><a href="./list?cPage=${cPage+1}&${searchInfo}"><i class="fas fa-angle-double-right"></i></a></c:if>
				</li>
			
			</ul>
		</div>
	</nav>

<%@ include file="../part/foot.jspf"%>