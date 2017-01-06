package com.bitcamp.pick.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.SystemPropertyUtils;

import com.bitcamp.pick.domain.Comment;
import com.bitcamp.pick.dao.CommentDao;


@Repository("commentDaoImpl")
public class CommentDaoImpl implements CommentDao {
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	public SqlSession sqlSession;
	
	public CommentDaoImpl() {
		System.out.println("CommentDaoImpl Default Constructor");
	}
	@Override
	public int addComment(Comment comment) throws Exception{
		System.out.println("CommentDaoImpl-addComment");
		return sqlSession.insert("CommentMapper.addComment",comment);
	}
	@Override
	public Comment getComment(int commentNo) throws Exception{
		System.out.println("CommentDaoImpl-getComment");
		return sqlSession.selectOne("CommentMapper.getComment",commentNo);
	}
	public List<Comment> getCommentListByVoteNo(int voteNo) throws Exception{
		System.out.println("CommentDaoImpl-getCommentListByVoteNo");
		return sqlSession.selectList("CommentMapper.getCommentListByVoteNo",voteNo);
	}
	@Override
	public int deleteCommnet(int commentNo) throws Exception {
		System.out.println("CommentDaoImpl-deleteCommnet");
		return sqlSession.delete("CommentMapper.deleteComment", commentNo);
	}
	
	
}
