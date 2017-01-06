package com.bitcamp.pick.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bitcamp.pick.dao.VoteAuthorityDao;
import com.bitcamp.pick.domain.VoteAuthority;


@Repository("voteAuthorityDaoImpl")
public class VoteAuthorityDaoImpl implements VoteAuthorityDao{

	@Autowired
	@Qualifier("sqlSessionTemplate")
	public SqlSession sqlSession;
 
	public VoteAuthorityDaoImpl() {
		System.out.println("VoteAuthorityDaoImpl Default Constructor");
	}
	public int addVoteAuthority(VoteAuthority voteAuthority){
		System.out.println("VoteAuthorityDaoImpl-addVoteAuthority");
		return sqlSession.insert("VoteAuthorityMapper.addVoteAuthority",voteAuthority);
	}
}
