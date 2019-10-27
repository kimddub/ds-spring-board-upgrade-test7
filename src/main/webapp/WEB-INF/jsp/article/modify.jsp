<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="수정"/>
<c:set var="oldFileCnt" value="${fn:length(articleFiles)}"/>

<%@ include file="../part/head.jspf"%>

<link rel="stylesheet" href="/resource/css/modify/modify.css">

<script src="/resource/ckeditor_standard/ckeditor.js"></script>
<script>
	var maxFilecnt = 3;
	var oldFileCnt = "${oldFileCnt}";
	var additionalFileCnt = maxFilecnt - oldFileCnt;

   CKEDITOR.on('dialogDefinition', function( ev ){
       var dialogName = ev.data.name;
       var dialogDefinition = ev.data.definition;

       switch (dialogName) {
           case 'image': //Image Properties dialog
           //dialogDefinition.removeContents('info');
           dialogDefinition.removeContents('Link');
           dialogDefinition.removeContents('advanced');
           break;
       }
   });
   
	var editorConfig = {
	        filebrowserUploadUrl : "/article/doAddImg", //이미지만 업로드
	   };

   window.onload = function(){
        ck = CKEDITOR.replace("editor", editorConfig);
   };
   
	// --공통--
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

	//--modify--
	
	function deleteFile(id,file){
		var id = id;

		if (confirm('기존 파일을 삭제하시겠습니까?') == false) {
			return false;
		}
		
		$('tr.article-file div.delete-files').append('<input type="hidden" name ="deleteFiles" value="' + id + '">');
		
		var $file = file;
		$file.remove();

		$('tr.article-file div.new-files').prepend('<input type="file" name="newFiles" />');
	}

	$(function() {

		for (var i=0; i<additionalFileCnt; i++) {
			$('tr.article-file div.new-files').append('<input type="file" name="newFiles" />');
		}
		
	});

</script>

	<section class="con common-form-box">
	
		<form onsubmit="checkForm(this); return false;" action="./doModify?${pageInfo}" method="POST" enctype="multipart/form-data">
		
			<input type="hidden" name="id" value="${article.id}">
		
			<table class="common-table common-form">
				<colgroup>
					<col width="150">
					<col>
				</colgroup>
				<tbody>
					<tr>
						<th>제목</th>
						<td>
							<input type="text" name="title" value="${article.title}">
						</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>
							<textarea name="body" id="editor" rows="10" cols="80">
					            <c:out value="${article.body}" escapeXml="true" />
					        </textarea>
						</td>
					</tr>
					<tr class="article-file">
						<th>
							첨부파일
							</br><span style="color:red;">*최대 100MB</span>
						</th>
						<td id="file-section">
							<div id="fileDropBox" class="new-file">
								<input type="file" id="filepond"/>
								<div id="fileDrop">Drag & Drop your files (Word/ Excel/ PowerPoint)</div>
							</div>
						    <div id="fileList"></div>
						    <div id="hiddenFileList"></div>
							
							<div class="old-files">
								<c:forEach var="file" items="${articleFiles}">
									<span class="old-file" onclick="deleteFile(${file.id},this)">
										${file.originalFileName}
										<button type="button" class="common-spc-btn"><i class="fa fa-trash-o"></i></button><br/>
									</span>
								</c:forEach>
							</div>
							
							<div class="delete-files">
<!-- 							<input type="hidden" name ="deleteFileId" value="id"> -->
							</div>
						</td>
						
						<script>

							var uploadFileCount = 0;
							var $dragAndDrop = document.getElementById("filepond");

							function cancelUpload(el,id) {
								var $fileLine = $(el).closest('.fileLine');
								$fileLine.remove();

								var id = id;
								$('#hiddenFileList #' + id ).remove();
							}
							
							$dragAndDrop.addEventListener("change", addFiles, false);

							function addFiles() {
								var fileNum = ++uploadFileCount;
								
								var clone = $('#filepond').clone();
								var uniqueId = 'file' + fileNum;
								clone.attr('id', uniqueId)
							    clone.attr('name', 'files')
								$('#hiddenFileList').append(clone);
								
								var fileValue = clone.val().split("\\");
								var fileName = fileValue[fileValue.length-1];
								
								var $fileLine = `<div class="fileLine">
													` + fileName + `
													<span onclick="cancelUpload(this,'` + uniqueId + `')">
														<button type="button" class="common-spc-btn"><i class="fa fa-trash-o"></i></button><br/>
													</span>
												</div>`
								$('#fileList').append($fileLine);
							}
						</script>
					</tr>
				</tbody>
			
			</table>
			
			<div class="common-form-menu">
				<span class="common-form-menu-btn">
					<input class="common-btn" type="submit" value="수정">
					<input class="common-btn" type="button" value="취소" onclick="history.back();">
					<input class="common-btn" type="button" value="목록" onclick="location.href='./list?${pageInfo}';">
				</span>
			</div>
		
		</form>
	
	</section>

<%@ include file="../part/foot.jspf"%>