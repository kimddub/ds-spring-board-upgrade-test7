package com.example.demo.controller;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.MemberService;
import com.example.demo.util.SHA256Util;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	MemberService memberService;
	
	/*
		join controller: RSA 공개키, 개인키 생성
		join view jsp: RSA공개키로 사용자 비밀번호 암호화
		doJoin controller: RSA 개인키로 사용자 암호화 비밀번호 복호화
						   SHA 단방향 암호화 후 DB 저장/보관
	*/
	@RequestMapping("/join")
	public String showJoin(Model model, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024);
		KeyPair keyPair = generator.genKeyPair();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		HttpSession session = request.getSession();
		// 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
		session.setAttribute("rsaPrivateKey", privateKey);
 
		RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		String publicKeyModulus = publicSpec.getModulus().toString(16);
		String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
 
		if (publicKeyModulus != "" && publicKeyExponent != "") {
			model.addAttribute("publicKeyModulus", publicKeyModulus);  //로그인 폼에 값을 셋팅하기위해서
			model.addAttribute("publicKeyExponent", publicKeyExponent);   //로그인 폼에 값을 셋팅하기위해서
		}
		
		return "member/join";
	}
		
	@RequestMapping("/login")
	public String showLogin(Model model, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024);
		KeyPair keyPair = generator.genKeyPair();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(20*60); // 20분 세션 유지
		
		// 세션에 공개키의 문자열을 키로하여 개인키를 저장한다.
		session.setAttribute("rsaPrivateKey", privateKey);
 
		RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
		String publicKeyModulus = publicSpec.getModulus().toString(16);
		String publicKeyExponent = publicSpec.getPublicExponent().toString(16);
 
		if (publicKeyModulus != "" && publicKeyExponent != "") {
			model.addAttribute("publicKeyModulus", publicKeyModulus);  //로그인 폼에 값을 셋팅하기위해서
			model.addAttribute("publicKeyExponent", publicKeyExponent);   //로그인 폼에 값을 셋팅하기위해서
		}
		
		return "member/login";
	}
	
	@RequestMapping("/checkExistedDuplId")
	@ResponseBody
	public boolean checkExistedDuplId(String loginId) {
		
		if (memberService.getMemberByLoginId(loginId) != null) {
			return true;
		}
		
		return false;
	}
	
	@RequestMapping("/doJoin")
	public String doJoin(Model model,@RequestParam Map<String,Object> param, HttpServletRequest request) {

        String rsaLoginPw = (String)param.get("rsaLoginPw");
		String loginPw = null;
		
        HttpSession session = request.getSession();
        PrivateKey privateKey = (PrivateKey)session.getAttribute("rsaPrivateKey");
        session.removeAttribute("rsaPrivateKey"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.
        
        if (privateKey == null) {
            throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
        }
        
        try {
        	
        	// 클라이언트에서 주고받기 위한 비밀번호 양방향 암호를 복호화
        	loginPw = decryptRsa(privateKey, rsaLoginPw);
            
        } catch (Exception ex) {
        	throw new RuntimeException("암호를 복호화 할 수 없습니다.");
        }
        
        param.put("loginPw", loginPw);
        
        // 회원정보 DB와 일치비교
		Map<String, Object> rs = memberService.join(param);
		
		String resultCode = (String)rs.get("resultCode");
		
		if (resultCode.startsWith("F")) {

			model.addAttribute("alertMsg", (String)rs.get("alertMsg"));
			model.addAttribute("historyBack","true");
		} else {
		
			model.addAttribute("alertMsg", (String)rs.get("alertMsg"));
			model.addAttribute("redirectUrl", "../member/login");
			
			// 로그인에 성공했을 땐 키 새로 생성
	        session.removeAttribute("rsaPrivateKey"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.
		}
		
		return "common/redirect";
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(Model model,@RequestParam Map<String,Object> param, HttpServletRequest request) {

        String rsaLoginPw = (String)param.get("rsaLoginPw");
		String loginPw = null;

        HttpSession session = request.getSession();
        PrivateKey privateKey = (PrivateKey) session.getAttribute("rsaPrivateKey");

        if (privateKey == null) {
            throw new RuntimeException("암호화 비밀키 정보를 찾을 수 없습니다.");
        }
        
        try {
        	
        	// 클라이언트에서 주고받기 위한 비밀번호 양방향 암호를 복호화
        	loginPw = decryptRsa(privateKey, rsaLoginPw);
            
        } catch (Exception ex) {
        	throw new RuntimeException("암호를 복호화 할 수 없습니다.");
        }
        
        param.put("loginPw", loginPw);
        
        // 회원정보 DB insert
		Map<String, Object> rs = memberService.login(param, request);
		
		String resultCode = (String)rs.get("resultCode");
		
		if (resultCode.startsWith("F")) {

			model.addAttribute("alertMsg", (String)rs.get("alertMsg"));
			model.addAttribute("historyBack","true");
		} else {
		
			model.addAttribute("alertMsg", (String)rs.get("alertMsg"));
			model.addAttribute("redirectUrl", "../article/list");
			
			// 로그인에 성공했을 땐 키 새로 생성
	        session.removeAttribute("rsaPrivateKey"); // 키의 재사용을 막는다. 항상 새로운 키를 받도록 강제.
		}
		
		return "common/redirect";
	}
	
	@RequestMapping("/doLogout")
	public String doLogout(Model model, HttpServletRequest request) {
		
		Map<String, Object> rs = memberService.logout(request);
		
		String resultCode = (String)rs.get("resultCode");
		
		if (resultCode.startsWith("F")) {

			model.addAttribute("alertMsg", (String)rs.get("alertMsg"));
			model.addAttribute("historyBack","true");
		}
		
		model.addAttribute("alertMsg", (String)rs.get("alertMsg"));
		model.addAttribute("redirectUrl", "../member/login");
		
		return "common/redirect";
	}

	
	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        //System.out.println("will decrypt : " + securedValue);
       
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }

    // 16진 문자열 byte 배열로 변환
	private static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
}
