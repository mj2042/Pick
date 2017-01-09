package com.bitcamp.pick.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bitcamp.pick.dao.ChoiceDao;
import com.bitcamp.pick.dao.ChoiceUserDao;
import com.bitcamp.pick.domain.Choice;
import com.bitcamp.pick.domain.ChoiceUser;
import com.bitcamp.pick.service.ChoiceService;

@Service("choiceServiceImpl")
public class ChoiceServiceImpl implements ChoiceService {

	/// Field
	@Autowired
	@Qualifier("choiceDaoImpl")
	private ChoiceDao choiceDao;

	@Autowired
	@Qualifier("choiceUserDaoImpl")
	private ChoiceUserDao choiceUserDao;

	/// Constructor
	public ChoiceServiceImpl() {
		System.out.println("ChoiceServiceImpl Default Constructor");
	}

	/// Method

	public void addChoice(Choice choice) throws Exception {
		System.out.println("ChoiceServiceImpl-addChoice");
		choiceDao.addChoice(choice);
	}

	public List<Choice> getChoiceListByVoteNo(int voteNo) throws Exception {
		System.out.println("ChoiceServiceImpl-getChoiceListByVoteNo");
		return choiceDao.getChoiceListByVoteNo(voteNo);
	}

	public Choice getChoiceByChoiceNo(int choiceNo) throws Exception {
		System.out.println("ChoiceServiceImpl-getChoiceByChoiceNo");
		return choiceDao.getChoiceByChoiceNo(choiceNo);
	}

	public void updateChoiceCount(Choice choice, int userNo) throws Exception {
		System.out.println("ChoiceServiceImpl-updateChoiceCount");
		ChoiceUser choiceUser = new ChoiceUser();

		choiceUser.setChoiceNo(choice.getChoiceNo());
		choiceUser.setUserNo(userNo);

		choiceUserDao.addChoiceUser(choiceUser);

		choiceDao.updateChoiceCount(choice);
	}

	@Override
	public List<Integer> getUserNoListByChoiceNo(int choiceNo) throws Exception {
		System.out.println("ChoiceServiceImpl-getUserNoListByChoiceNo");
		return choiceUserDao.getUserNoListByChoiceNo(choiceNo);
	}

}