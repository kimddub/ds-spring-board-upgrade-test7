<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="목록"/>
<%@ include file="../part/head.jspf"%>

<link rel="stylesheet" href="/resource/css/list/list.css">

<script>
	$(function() {
		//최상단 체크박스 클릭
	    $("#checkAll").click(function(){
	        
	        if($("#checkAll").prop("checked")){
	            
	            $("input[name=articles]").prop("checked",true);
	            
	        }else{
	            
	            $("input[name=articles]").prop("checked",false);
	        }
	    })

	});

	function changeListSorting(el) {
		var $el = $(el);
		var cSort = $el.find('span').attr('id');
		
		var newUrl = getNoDomainUrl();
		var sort = 'DESC';
		
		if(cSort == 'DESC') {
			sort = 'ASC';
		}

		newUrl = replaceUrlParam(newUrl, 'sort', sort);
		location.href = newUrl;
	}	

	function deleteArticles() {
		var $form = $('#delete-form');

		if (confirm('정말로 삭제하시겠습니까?') == false) {
			return false;
		}
		
		$form.submit();
	}

</script>

	<section class="article-box">
		<div class="article-menu row">
			<c:if test="${isAdminAuth}">
				<button class="common-btn delete-btn cell" onclick="deleteArticles();">
					<i class="fas fa-trash-alt"></i>
				</button>
			</c:if>
			<c:if test="${isLogined}">
				<button class="common-btn cell" onclick="location.href='./add?${pageInfo}';">
					<i class="fa fa-pencil"></i>
				</button>
			</c:if>
		
			<div class="search-box cell">
				<form id="search-form" action="./list" method="GET">
					<%-- <input type="hidden" name="boardId" value="${param.boardId}"> --%>
				
					<div class="search">
						<select name="searchType" class="searchSelect">
							<option value="all">전체</option>
							<option value="title">제목</option>
							<option value="titleBody">제목+내용</option>
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
			
			<div class="listing-option cell-right">
				<select name="listSize">
					<option value="10">10개씩</option>
					<option value="20">20개씩</option>
				</select>
			</div>
			
			<c:if test="${param.listSize != null && param.listSize != ''}">
				<script>
					$('select[name="listSize"]').val('${param.listSize}');
				</script>
			</c:if>
			
			<script>
				$('select[name="listSize"]').on("textchange",function(el){
					var newUrl = getNoDomainUrl();
					var listSize = $(this).val();

				    newUrl = replaceUrlParam(newUrl, 'listSize', listSize);
					location.href = newUrl;
				});
			</script>
		</div>
		
		<table class="common-table">
			<colgroup>
				<c:if test="${isAdminAuth}">
					<col width="30">
				</c:if>
				<col width="70">
				<col>
				<col width="70">
				<col width="150">
				<col width="200">
				<col width="200">
			</colgroup>
			
			<form id="delete-form" action="../admin/doDeleteArticles?${pageInfo}" method="POST">
				<thead>
					<tr>
						<c:if test="${isAdminAuth}">
							<th><input type="checkbox" id="checkAll"></th>
						</c:if>
						<th>번호</th>
						<th>제목</th>
						<th>첨부</th>
						<th>작성자</th>
						<th id="sort-btn" onclick="changeListSorting(this);">
							작성일
							<c:if test="${param.sort == 'ASC'}">
								<span id="ASC">▲</span>
							</c:if>
							<c:if test="${param.sort == null || param.sort == '' || param.sort == 'DESC'}">
								<span id="DESC">▼</span>
							</c:if>					
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="article" items="${articles}">
						<tr class="article-tr">
							<c:if test="${isAdminAuth}">
								<td><input type="checkbox" name="articles" value="${article.id}"></td> 
							</c:if>
							<td><c:out value="${article.id}"/></td>
							<td class="article-cursor" onclick="location.href='./detail?id=${article.id}&${pageInfo}';">
								<c:out value="${article.title}"/>
								&nbsp; 
								<c:if test="${article.extra.fileCount > 0}">
									<i class="fas fa-paperclip"></i>
								</c:if>
							</td>
							<td>
								<c:if test="${article.extra.fileCount > 0}">
									<c:out value="${article.extra.fileCount}" />
								</c:if>
							</td>
							<td><c:out value="${article.writer}"></c:out></td>
							<td><c:out value="${article.extra.onlyRegDate}"></c:out></td>
						</tr>
					</c:forEach>
					
					<c:if test="${fn:length(articles) < 1}">
						<tr height="300">
							<td colspan="6">게시물이 존재하지 않습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</form>
		</table>
		
	</section>
	
	<nav class="con paging-section">
		<div class="page-box row">
			
			<c:if test="${pageMaker.prev }">
			<div class="cell page-btn">
				<a href="./list?cPage=1&${searchInfo}"><i class="fas fa-angle-double-left"></i></a>
			</div>
			<div class="cell page-btn">
				<a href="./list?cPage=${pageMaker.startPage-1}&${searchInfo}"><i class="fas fa-chevron-left"></i></a>
			</div>
			</c:if>
		
			<ul class="row cell">
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
			</ul>
			
			<c:if test="${pageMaker.next }">
			<div class="cell page-btn">
				<a href="./list?cPage=${pageMaker.endPage+1}&${searchInfo}"><i class="fas fa-chevron-right"></i></a>
			</div>
			<div class="cell page-btn">
				<a href="./list?cPage=${pageMaker.limitPage}&${searchInfo}"><i class="fas fa-angle-double-right"></i></a>
			</div>
			</c:if>
		</div>
	</nav>

<%@ include file="../part/foot.jspf"%>