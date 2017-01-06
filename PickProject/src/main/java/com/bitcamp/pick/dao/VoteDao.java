package com.bitcamp.pick.dao;

import java.util.List;
import java.util.Map;

import com.bitcamp.pick.domain.Vote;

public interface VoteDao {
	

	public void addVote(Vote vote)throws Exception ;
	public Vote getVote(int voteNo)throws Exception ; 
    public List<Vote> getVoteList() throws Exception;
    public List<Vote> getVoteListByUserNo(int userNo) throws Exception;
    public List<Vote> getMyVoteList(int userNo) throws Exception;
    public List<Vote> search(String word) throws Exception;
    public List<Vote> filter(Map<String,Object> filterMap) throws Exception;
	
}