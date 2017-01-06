package com.bitcamp.pick.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bitcamp.pick.dao.ChoiceDao;
import com.bitcamp.pick.dao.VoteAuthorityDao;
import com.bitcamp.pick.dao.VoteDao;
import com.bitcamp.pick.domain.Choice;
import com.bitcamp.pick.domain.Vote;
import com.bitcamp.pick.domain.VoteAuthority;
import com.bitcamp.pick.service.VoteService;





@Service("voteServiceImpl")
public class VoteServiceImpl implements VoteService{
	
	///Field
	@Autowired
	@Qualifier("voteDaoImpl")
	private VoteDao voteDao;
	
	
	@Autowired
	@Qualifier("voteAuthorityDaoImpl")
	private VoteAuthorityDao voteAuthorityDao;
	
	
	
	@Autowired
	@Qualifier("choiceDaoImpl")
	private ChoiceDao choiceDao;
	

	///Constructor
	public VoteServiceImpl() {
		System.out.println(" VoteServiceImpl Default Constructor");
	}

	///Method
	public void addVote(Vote vote) throws Exception {
		System.out.println("VoteServiceImpl-addVote");
		 
	      voteDao.addVote(vote);
	     
	      VoteAuthority voteAuthority = vote.getVoteAuthority();
	      voteAuthority.setVoteNo(vote.getVoteNo());
	      voteAuthorityDao.addVoteAuthority(voteAuthority);
	}
	
	public Vote getVote(int voteNo) throws Exception {
		System.out.println("VoteServiceImpl-getVote");
		Vote vote = voteDao.getVote(voteNo);
		List<Choice> choiceList = choiceDao.getChoiceListByVoteNo(voteNo);
		vote.setChoiceList(choiceList);
		
		
		return vote;
	}

	@Override
	public List<Vote> getVoteList() throws Exception {
		System.out.println("VoteServiceImpl-getVoteList");
		return voteDao.getVoteList();
	}

	@Override
	public List<Vote> getVoteListByUserNo(int userNo) throws Exception {
		System.out.println("VoteServiceImpl-getVoteListByUserNo");
		return voteDao.getVoteListByUserNo(userNo);
	}

	@Override
	public List<Vote> search(String word) throws Exception {
		System.out.println("VoteServiceImpl-search");
		return voteDao.search(word);
	}

	@Override
	public List<Vote> filter(Map<String, Object> filterMap) throws Exception {
		System.out.println("VoteServiceImpl-filter");
		return voteDao.filter(filterMap);
	}

	@Override
	public List<Vote> getMyVoteList(int userNo) throws Exception {
		System.out.println("VoteServiceImpl-getMyVoteList");
		return voteDao.getMyVoteList(userNo);
	}
	
	




	


}