<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="로그인"/>
<%@ include file="../part/head.jspf"%>

<script type="text/javascript" src="/resource/js/rsa/jsbn.js"></script>
<script type="text/javascript" src="/resource/js/rsa/rsa.js"></script>
<script type="text/javascript" src="/resource/js/rsa/prng4.js"></script>
<script type="text/javascript" src="/resource/js/rsa/rng.js"></script>

<script>
// 	var key = "${publicKeyModulus}";
// 	alert(key.length);
// 	alert("${publicKeyExponent}");

	$(function() {

		
// 		var timer;
		
// 		// 연속으로 함수가 호출될 때 2초에 1번만 실행되도록 함
// 		$('.write-form input[name="loginId"]').on("textchange", function() {

// 			var value = $(this).val().trim();

// 			if (timer) {
// 			    clearTimeout(timer);
// 			 }
			
// 			timer = setTimeout(function() {
// 				alert('name 실시간 중복 체크');
// 			}, 2000);
			
// 		}); 
	});

	function formCheck(form) {

		var loginId = $(form).find('input[name="loginId"]').val().trim();
		if (loginId.length == 0) {
			alert("ID를 입력해주세요");

			return false;
		}

		var loginPw = $(form).find('input[name="loginPw"]').val().trim();
		if (loginPw.length == 0) {
			alert("PW를 입력해주세요");

			return false;
		}


		if ("${publicKeyModulus}" == '' && "${publicKeyExponent}" == '') {
			alert("오류: 암호화 문제");

			return false;
		}

		var encryptedLoginPw = getEncryptedLoginPw(loginPw,"${publicKeyModulus}","${publicKeyExponent}");

		$('#encryptedLoginPw').val(encryptedLoginPw);
		form.submit();
	}

	function getEncryptedLoginPw(loginPw, rsaPublicKeyModulus, rsaPpublicKeyExponent) {
		var rsa = new RSAKey();
	    rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);

	    // 사용자ID와 비밀번호를 RSA로 암호화한다.
	    var encryptedLoginPw = rsa.encrypt(loginPw);

	    return encryptedLoginPw;
	}
 
</script>

	<div class="common-form-box">
		<form onsubmit="formCheck(this); return false;" action="../member/doLogin" method="POST">
			<input type="hidden" name="rsaLoginPw" id="encryptedLoginPw"/>
			<div class="write-form">
				<div>
					<label>ID</label>
					<input type="text" name="loginId" placeholder="최대 10자" maxlength="10">
				</div>
				<div>
					<label>PW</label>
					<input type="password" name="loginPw" placeholder="최소 4자 ~ 10자" minlength="4" maxlength="10">
				</div>
			</div>
			
			<div class="common-form-btn"> 
				<input class="common-btn" onclick="history.back();" type="button" value="취소">
				<input class="common-btn" type="submit" value="로그인">
			</div>
		</form>
	</div>


<%@ include file="../part/foot.jspf"%>