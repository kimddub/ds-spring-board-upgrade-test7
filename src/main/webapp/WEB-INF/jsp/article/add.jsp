<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="작성"/>
<%@ include file="../part/head.jspf"%>

<script>

	function checkForm(form) {
	
		var $form = form;

		$form.title.value = $form.title.value.trim();
		if ($form.title.value.length == 0) {
			
			alert("제목을 입력하세요");
			$form.title.focus();

			return false;
		}

		$form.body.value = $form.body.value.trim();
		if ($form.body.value.length == 0) {
			
			alert("내용을 입력하세요");
			$form.body.focus();
			
			return false;
		}

		$form.writer.value = $form.writer.value.trim();
		if ($form.writer.value.length == 0) {
			
			alert("작성자를 입력하세요");
			$form.writer.focus();

			return false;
		}

		$form.submit();
	}

</script>

	<section class="con common-form-box">
	
		<form onsubmit="checkForm(this); return false;" action="./doAdd" method="POST" enctype="multipart/form-data">
		
			<table class="common-table common-form">
				<colgroup>
					<col width="150">
					<col>
				</colgroup>
				<tbody>
					<tr>
						<th>제목</th>
						<td>
							<input type="text" name="title" placeholder="제목을 입력하세요">
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>
							<textarea name="body" placeholder="내용을 입력하세요"></textarea>
						</td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>
							<input type="text" name="writer" placeholder="별명을 입력하세요">
						</td>
					</tr>
					<tr>
						<th>첨부파일</th>
						<td>
							<input type="file" name="files" />
							<input type="file" name="files" />
							<input type="file" name="files" />
						</td>
					</tr>
				</tbody>
			
			</table>
			
			<div class="common-form-menu">
				<span class="common-form-menu-btn">
					<input class="common-btn" type="submit" value="작성">
					<input class="common-btn" type="button" value="목록" onclick="location.href='./list?${pageInfo}';">
				</span>
			</div>
		
		</form>
	
	</section>

<%@ include file="../part/foot.jspf"%>