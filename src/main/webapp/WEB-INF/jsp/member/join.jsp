<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.example.demo.dto.Article"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="pageTitle" value="회원가입"/>
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
		var timer;
		
		// 연속으로 함수가 호출될 때 2초에 1번만 실행되도록 함
		$('.write-form input[name="loginId"]').on("textchange", function() {

			var $input = $(this);
			var loginId = $input.val().trim();

			if (timer) {
			    clearTimeout(timer);
			 }
			
			timer = setTimeout(function() {
// 				alert(checkExistedDuplId(loginId));

				$.post(
						'../member/checkExistedDuplId',
						{"loginId" : loginId},
						function(data) {
							
							var existDuplId = data;

							if (existDuplId) {
								$input.closest('div').removeClass('available');
								$input.closest('div').addClass('warning');
							} else {
								$input.closest('div').removeClass('warning');
								$input.closest('div').addClass('available');
							}

						},
						'json'
					).done(function(jqXHR) {
					});
				
			}, 300);
			
		}); 

		// 연속으로 함수가 호출될 때 2초에 1번만 실행되도록 함
		$('.write-form input[name="checkLoginPw"]').on("textchange", function() {

			var $checkPwInput = $(this);
			var $pwInput = $('.write-form input[name="loginPw"]');
			var checkPw = $checkPwInput.val().trim();
			var pw = $pwInput.val().trim();

			if (timer) {
			    clearTimeout(timer);
			 }
			
			timer = setTimeout(function() {

				if (checkPw != pw) {
					$checkPwInput.closest('div').removeClass('available');
					$checkPwInput.closest('div').addClass('warning');
				} else {
					$checkPwInput.closest('div').removeClass('warning');
					$checkPwInput.closest('div').addClass('available');
				}
				
			}, 500);
			
		}); 
	});

	function checkExistedDuplId(loginId) {
		var existDuplId = false;
		
		$.post(
			'../member/checkExistedDuplId',
			{"loginId" : loginId},
			function(data) {
				alert();
				existDuplId = data;

			},
			'html'
		).done(function(jqXHR) {

			return "Test";
		});

//			.done(function(jqXHR) {
//		    alert("second success" );
//		})
//		.fail(function(jqXHR) {
//		    alert("error" );
//		})
//		.always(function(jqXHR) {
//		    alert("finished" );
//		});
		
	}

	function formCheck(form) {

		var name = $(form).find('input[name="name"]').val().trim();
		if (name.length == 0) {
			alert("이름을 입력해주세요");

			return false;
		}

		var $loginIdInput = $(form).find('input[name="loginId"]');
		var loginId = $loginIdInput.val().trim();
		
		if (loginId.length == 0 ) {
			alert("ID를 입력해주세요");

			return false;
		} 

		if ($loginIdInput.closest('div').hasClass('warning')) {
			alert('중복된 ID가 존재합니다. 다른 ID를 사용해주세요');

			return false;
		}

		var loginPw = $(form).find('input[name="loginPw"]').val().trim();
		if (loginPw.length == 0) {
			alert("PW를 입력해주세요");

			return false;
		}

		var checkLoginPw = $(form).find('input[name="checkLoginPw"]').val().trim();
		if (checkLoginPw.length == 0) {
			alert("PW 재확인을 입력해주세요");

			return false;
		}

		if (loginPw != checkLoginPw) {
			alert("PW가 일치하지 않습니다.");

			return false;
		}


		if ("${publicKeyModulus}" == '' && "${publicKeyExponent}" == '') {
			alert("오류: 암호화 문제");

			return false;
		}

		var encryptedLoginPw = getEncryptedLoginPw(loginPw,"${publicKeyModulus}","${publicKeyExponent}");

		$('#encryptedLoginPw').val(encryptedLoginPw);
		$('input[name="loginPw"]').prop('disabled', true);
		$('input[name="checkLoginPw"]').prop('disabled', true);
		
		if (confirm("위 정보로 가입하시겠습니까?") == false) {
			return false;
		}
		
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
		<form onsubmit="formCheck(this); return false;" action="../member/doJoin" method="POST">
			<input type="hidden" name="rsaLoginPw" id="encryptedLoginPw"/>
			<div class="write-form">
				<div>
					<label>이름</label>
					<input type="text" name="name" placeholder="최대 10자" maxlength="10">
				</div>
				<div>
					<label>ID</label>
					<input type="text" name="loginId" placeholder="최대 10자" maxlength="10">
					<span class="msg available-msg">사용가능</span>
					<span class="msg warning-msg">중복된 ID</span>
				</div>
				<div>
					<label>PW</label>
					<input type="password" name="loginPw" placeholder="최소 4자 ~ 10자" minlength="4" maxlength="10">
				</div>
				<div>
					<label>PW 확인</label>
					<input type="password" name="checkLoginPw" placeholder="최소 4자 ~ 10자" minlength="4" maxlength="10">
					<span class="msg available-msg">일치</span>
					<span class="msg warning-msg">불일치</span>
				</div>
			</div>
			
			<div>
				<input type="radio" name="authCode" value="1" checked/> 회원
				<input type="radio" name="authCode" value="0"/> 관리자
			</div>
			
			<div class="common-form-btn"> 
				<input class="common-btn" onclick="history.back();" type="button" value="취소">
				<input class="common-btn" type="submit" value="가입">
			</div>
		</form>
	</div>


<%@ include file="../part/foot.jspf"%>