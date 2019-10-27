package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.Member;

@Mapper
public interface MemberDao {
	public List<Member> selectMember(Map<String, Object> param);
	
	public Member selectOneMember(Map<String, Object> param);

	public void insertMember(Map<String, Object> param);


}
