package com.bitcamp.pick.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bitcamp.pick.dao.VoteDao;
import com.bitcamp.pick.domain.Vote;




@Repository("voteDaoImpl")
public class VoteDaoImpl implements VoteDao{
	
	///Field
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	///Constructor
	public VoteDaoImpl() {
		System.out.println("VoteDaoImpl Default Constructor");
	}

	///Method
	public void addVote(Vote vote) throws Exception {
		System.out.println("VoteDaoImpl-addVote");
		sqlSession.insert("VoteMapper.addVote", vote);
	}

    public Vote getVote(int voteNo)throws Exception {
    	System.out.println("VoteDaoImpl-getVote");
    	return sqlSession.selectOne("VoteMapper.getVote", voteNo);
    }

	@Override
	public List<Vote> getVoteList() throws Exception {
		System.out.println("VoteDaoImpl-getVoteList");
		return sqlSession.selectList("VoteMapper.getVoteList");
	}

	@Override
	public List<Vote> getVoteListByUserNo(int userNo) throws Exception {
		return sqlSession.selectList("VoteMapper.getVoteListByUserNo",userNo);
	}

	@Override
	public List<Vote> search(String word) throws Exception {
		return sqlSession.selectList("VoteMapper.search", word);
	}

	@Override
	public List<Vote> filter(Map<String, Object> filterMap) throws Exception {
		return sqlSession.selectList("VoteMapper.filter", filterMap);
	}

	@Override
	public List<Vote> getMyVoteList(int userNo) throws Exception {
		return sqlSession.selectList("VoteMapper.getMyVoteList",userNo);
	}
	
	
}