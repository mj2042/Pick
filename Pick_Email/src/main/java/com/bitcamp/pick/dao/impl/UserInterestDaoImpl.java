package com.bitcamp.pick.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bitcamp.pick.dao.UserInterestDao;

@Repository("userInterestDaoImpl")
public class UserInterestDaoImpl implements UserInterestDao {
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	public SqlSession sqlSession;
 
	public UserInterestDaoImpl() {
		System.out.println("UserInterestDaoImpl Default Constructor");
	}

	
	/*도메인으로 변경예정*/
	@Override
	public int addUserInterest(int userNo, int interestNo) throws Exception {
		System.out.println("UserInterestDaoImpl addUserInterest");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userNo", new Integer(userNo));
		map.put("interestNo", new Integer(interestNo));

		return sqlSession.insert("UserInterestMapper.addUserInterest", map);
	}

	@Override
	public int deleteUserInterest(int userNo) throws Exception {
		System.out.println("UserInterestDaoImpl deleteUserInterest");
		return sqlSession.delete("UserInterestMapper.deleteUserInterest",userNo);
	}
}
