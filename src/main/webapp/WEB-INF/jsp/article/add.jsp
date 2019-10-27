<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="작성"/>
<%@ include file="../part/head.jspf"%>

<link rel="stylesheet" href="/resource/css/add/add.css">

<script src="/resource/ckeditor_standard/ckeditor.js"></script>

<script>
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

		var $file = $("input[name='files']");
		
	    if( $file.val() != "" ){
		    
		    var acceptedExtArr = '${acceptedMutiFile}';
    		var ext = $file.val().split('.').pop().toLowerCase();

    	    if($.inArray(ext, acceptedExtArr.split(',')) == -1) {
        	    // doc,docx,xls,xlsx,ppt,pptx
    	    	// xlsx, xlsm, xlsb, xltx, xml, xlam -> excel..?
    	    	//['msword','vnd.ms-excel','vnd.ms-powerpoint'] -> MIMTYPE
    		 	alert('지원하지 않는 형식의 첨부파일이 있습니다.');

    			return false;
    	      }

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
							<textarea name="body" id="editor" rows="10" cols="80">
					            This is my textarea to be replaced with CKEditor.
					        </textarea>
						</td>
					</tr>
					<tr>
						<th>첨부파일</th>
						<td id="file-section">
							<div id="fileDropBox">
								<input type="file" id="filepond"/>
								<div id="fileDrop">Drag & Drop your files</div>
							</div>
						    <div id="fileList"></div>
						    <div id="hiddenFileList"></div>
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
					<input class="common-btn" type="submit" value="작성">
					<input class="common-btn" type="button" value="목록" onclick="location.href='./list?${pageInfo}';">
				</span>
			</div>
		
		</form>
	
	</section>

<%@ include file="../part/foot.jspf"%>