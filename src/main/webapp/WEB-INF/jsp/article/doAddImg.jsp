<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<script src="/resource/ckeditor_standard/ckeditor.js"></script>
<!-- <script src="//cdn.ckeditor.com/4.13.0/standard/ckeditor.js"></script> -->

<script type = "text/javascript">
	alert();
    window.parent.CKEDITOR.tools.callFunction('${CKEditorFuncNum}','${filePath}', '업로드완료');
</script>

<!-- {"filename" : "[파일이름]", "uploaded" : 1, "url":"[파일path]"} -->
