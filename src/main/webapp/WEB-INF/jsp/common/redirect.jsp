<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${cPage == null || cPage == '' }">
<c:set var="cPage" value="${param.cPage}"/>
</c:if>

<c:set var="pageInfo" value="searchType=${param.searchType}&searchKey=${param.searchKey}&cPage=${cPage}"/> <!-- boardId 추가 -->
<c:set var="searchInfo" value="searchType=${param.searchType}&searchKey=${param.searchKey}"/> <!-- boardId 추가 -->
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>redirect page..</title>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script>
	var msg = "${alertMsg}";
	var redirectUrl = "${redirectUrl}";  //+ "&" + "searchType=${param.searchType}&searchKey=${param.searchKey}";
	var historyBack = "${historyBack}";
	
	if (msg.trim().length != 0) {
		alert(msg);
	}
	
	if (historyBack == "true") {
		history.back();
	}

	if (redirectUrl.length) {

		location.replace(redirectUrl);
	}

</script>

</head>
<body>



</body>
</html>