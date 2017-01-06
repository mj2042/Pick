package com.bitcamp.pick.dao;

import java.util.List;

import com.bitcamp.pick.domain.ChoiceUser;

public interface ChoiceUserDao {
	public int addChoiceUser(ChoiceUser choiceUser) throws Exception;
	public List<Integer> getUserNoListByChoiceNo(int choiceNo) throws Exception;
}
