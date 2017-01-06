package com.bitcamp.pick.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bitcamp.pick.dao.InterestDao;
import com.bitcamp.pick.domain.Interest;

@Repository("interestDaoImpl")
public class InterestDaoImpl implements InterestDao {

	@Autowired
	@Qualifier("sqlSessionTemplate")
	public SqlSession sqlSession;
 
	public InterestDaoImpl() {
		System.out.println("InterestDaoImpl Default Constructor");
	}

	@Override
	public int addInterest(Interest interest) throws Exception {
		System.out.println("InterestDaoImpl-addInterest");
		return sqlSession.insert("InterestMapper.addInterest", interest);
	}

	@Override
	public Interest getInterestByContent(String content) throws Exception {
		System.out.println("InterestDaoImpl - getInterestByContent");
		return sqlSession.selectOne("InterestMapper.getInterestByContent", content);
	}

	@Override
	public Interest getInterestByInterestNo(int interestNo) throws Exception {
		System.out.println("InterestDaoImpl - getInterestByInterestNo");
		return sqlSession.selectOne("InterestMapper.getInterestByInterestNo", interestNo);
	}

	@Override
	public List<Interest> getInterestList() throws Exception {
		System.out.println("InterestDaoImpl - getInterestList");
		return sqlSession.selectList("InterestMapper.getInterestList");
	}

}
