package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.Member;
import com.example.demo.util.SHA256Util;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	MemberDao memberDao;

	public Member getMemberByMemberId(int memberId) {
		return memberDao.selectOneMember(Maps.of("where__id", "true","memberId",memberId));
	}
	
	public Member getMemberByLoginId(String loginId) {
		return memberDao.selectOneMember(Maps.of("where__loginId", "true","loginId",loginId));
	}

	public Map<String, Object> join(Map<String, Object> param) {
		
		Map<String,Object> rs = new HashMap<>();
		
		String loginId = (String)param.get("loginId");
		
		Member member = getMember(Maps.of(
				"where__loginId", "true",
				"loginId",loginId));
		
		if (member != null) {
			
			rs.put("resultCode", "F-1");
			rs.put("alertMsg", "이미 존재하는 ID 입니다.");
		} else {
			String loginPw = (String)param.get("loginPw");
			
			// DB에 넣을 비밀번호 단방향 SHA-256 으로 SALT 이용해 암호화 하기
	        String salt = SHA256Util.generateSalt();
	        String shaLoginPw = SHA256Util.getEncrypt(loginPw, salt);
	        
	        param.put("encryptingSalt", salt);
	        param.put("encryptedLoginPw", shaLoginPw);
			
			// 회원 DB 추가
			try {
				insertMember(param);

				rs.put("resultCode", "S-1");
				rs.put("alertMsg", "성공적으로 회원가입 되었습니다.");
				
			} catch(Exception e) {
				
				rs.put("resultCode", "F-2");
				rs.put("alertMsg", "오류: 회원가입을 할 수 없습니다.");
			}
		}
		
		return rs;
	}
	
	// 로그인시 해당 ID, PW 회원 체크
	public Map<String, Object> login(Map<String, Object> param, HttpServletRequest request) {
		Map<String,Object> rs = new HashMap<>();
		
		// 해당 ID의 회원 가져옴
		String loginId = (String)param.get("loginId");
		Member existingMember = getMemberByLoginId(loginId);
		
		if (existingMember == null) {
			rs.put("resultCode", "F-1");
			rs.put("alertMsg", "해당 정보로 로그인 할 수 없습니다. ID 와 PW 를 확인하세요.");
			
			return rs;
		}
		
		// 회원의 솔트 정보 가져옴
        String salt = existingMember.getEncryptingSalt();

        // 해당 ID의 회원 솔트 정보로 로그인 유저 비밀번호 암호화
        String loginPw = (String)param.get("loginPw");
        String shaLoginPw = SHA256Util.getEncrypt(loginPw, salt);
        
        // 해당 ID의 회원 정보와 로그인 유저의 ID, 암호화 PW 비교
		Member member = getMember(Maps.of(
				"where__loginIdAndLoginPw","true",
				"loginId",loginId,
				"encryptedLoginPw",shaLoginPw));
		
		if (member != null) {

			// 로그인
			try {
				
				HttpSession session = request.getSession();
				session.setAttribute("loginedMemberId", member.getId());
				
				rs.put("resultCode", "S-1");
				rs.put("alertMsg", member.getName() + " 님 환영합니다.");
				
			} catch(Exception e) {
				
				rs.put("resultCode", "F-2");
				rs.put("alertMsg", "오류: 로그인을 할 수 없습니다.");
			}
		} else {

			rs.put("resultCode", "F-1");
			rs.put("alertMsg", "해당 정보로 로그인 할 수 없습니다. ID 와 PW 를 확인하세요.");
		}
		
		return rs;
		
	}
	
	public Map<String, Object> logout(HttpServletRequest request) {
		
		Map<String,Object> rs = new HashMap<>();
		
		try {
			
			HttpSession session = request.getSession();
			session.removeAttribute("loginedMemberId");
			//session.getMaxInactiveInterval();
			
			rs.put("resultCode", "S-1");
			rs.put("alertMsg", "로그아웃 되었습니다.");
			
		} catch(Exception e) {
			
			rs.put("resultCode", "F-2");
			rs.put("alertMsg", "오류: 로그아웃이 성공적으로 완료되지 않았습니다.");
		}
		
		return rs;
	}
	
	private void insertMember(Map<String, Object> param) {
		memberDao.insertMember(param);
	}
	
	// 이용할 때 where 옵션 변수와 조건 파라미터를 Maps 로 생성해서 변수 줄 것!
	private Member getMember(Map<String,Object> param) {

		// id, loginId, loginPw 를 통해 단 한명의 회원을 가져올 때만 사용
		// where__id
		// where__loginId
		// where__loginIdAndLoginPw
		return memberDao.selectOneMember(param);
	}
	
}
