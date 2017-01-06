package com.bitcamp.pick.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bitcamp.pick.dao.UserDao;
import com.bitcamp.pick.domain.User;

@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao {

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
 
	public UserDaoImpl() {
		System.out.println(" UserDaoImpl Default Constructor");
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public int addUser(User user) throws Exception {
		System.out.println("UserDaoImpl-addUser");
		return sqlSession.insert("UserMapper.addUser", user);
	}

	@Override
	public User getUserByUserEmail(String userEmail) throws Exception {
		System.out.println("UserDaoImpl-getUserByUserEmail");
		return sqlSession.selectOne("UserMapper.getUserByUserEmail", userEmail);
	}

	@Override
	public User getUserByUserName(String userName) throws Exception {
		System.out.println("UserDaoImpl-getUserByUserName");
		return sqlSession.selectOne("UserMapper.getUserByUserName", userName);
	}

	@Override
	public User getUserByUserNo(int userNo) throws Exception {
		System.out.println("UserDaoImpl-getUserByUserNo");
		return sqlSession.selectOne("UserMapper.getUserByUserNo", userNo);
	}

	@Override
	public void updateUser(User user) throws Exception {
		System.out.println("UserDaoImpl-updateUser");
		sqlSession.update("UserMapper.updateUser",user); 
		
	}

	@Override
	public List<User> getUserList() throws Exception {
		System.out.println("UserDaoImpl-getUserList");
		return sqlSession.selectList("UserMapper.getUserList");
	}

}
