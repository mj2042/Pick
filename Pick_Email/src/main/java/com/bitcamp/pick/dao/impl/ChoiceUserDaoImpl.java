package com.bitcamp.pick.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bitcamp.pick.dao.ChoiceUserDao;
import com.bitcamp.pick.domain.ChoiceUser;


@Repository("choiceUserDaoImpl")
public class ChoiceUserDaoImpl implements ChoiceUserDao {

	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	public SqlSession sqlSession;
	
	///Constructor
	public ChoiceUserDaoImpl() {
			System.out.println("ChoiceUserImpl Default Constructor");
	}
	
	
	@Override
	public int addChoiceUser(ChoiceUser choiceUser) throws Exception {
		System.out.println("ChoiceUserDaoImpl-addChoiceUser");
		return sqlSession.insert("ChoiceUserMapper.addChoiceUser",choiceUser);
	}


	@Override
	public List<Integer> getUserNoListByChoiceNo(int choiceNo) throws Exception {
		System.out.println("ChoiceUserDaoImpl-getUserNoListByChoiceNo");
		return sqlSession.selectList("ChoiceUserMapper.getUserNoListByChoiceNo", choiceNo);
	}

}
