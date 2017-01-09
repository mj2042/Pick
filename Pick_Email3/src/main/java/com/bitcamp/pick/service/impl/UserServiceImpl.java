package com.bitcamp.pick.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bitcamp.pick.dao.InterestDao;
import com.bitcamp.pick.dao.UserDao;
import com.bitcamp.pick.dao.UserInterestDao;
import com.bitcamp.pick.domain.Interest;
import com.bitcamp.pick.domain.User;
import com.bitcamp.pick.service.UserService;


@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	
	@Autowired
	@Qualifier("userDaoImpl")
	private UserDao userDao;
	
	 
	
	@Autowired
	@Qualifier("interestDaoImpl")
	private InterestDao interestDao;
	
	
	@Autowired
	@Qualifier("userInterestDaoImpl")
	private UserInterestDao userInterestDao;

	
	
	
	
	public UserServiceImpl(){
		System.out.println(" UserServiceImpl Default Constructor");
	}
	@Override
	public Map<String,Object> loginUser(User user) throws Exception {
		System.out.println(" UserServiceImpl-loginUser");
		
		Map<String,Object> loginCheckMap = new HashMap<String,Object>();
		
		User dbUser=userDao.getUserByUserEmail(user.getUserEmail());
		
		if(dbUser==null){    //계정이  없을 경우 
			loginCheckMap.put("loginCheck", "idNotFound");
		}else if(!dbUser.getUserPassword().equals(user.getUserPassword())){ //계정은 있으나 비밀번호가 틀린 경우 
			loginCheckMap.put("loginCheck", "passwordError");
		}else{  //로그인에 성공하였을 경우 
			
			loginCheckMap.put("user",dbUser);
			loginCheckMap.put("loginCheck", "success");
		}
	
		return loginCheckMap;
	
	}
	@Override
	public int addUser(User user) throws Exception {
		System.out.println(" UserServiceImpl-addUser");
		
		userDao.addUser(user);//userNo get하기위해 먼저 add
		User dbUser = userDao.getUserByUserEmail(user.getUserEmail());
		
		List<Interest> userInterest = user.getInterestList();
		
		for(Interest interest : userInterest){
			userInterestDao.addUserInterest(dbUser.getUserNo(), interest.getInterestNo());
		}
		
		
		return 1;
	}

	@Override
	public User getUserByUserEmail(String userEmail) throws Exception {
		System.out.println(" UserServiceImpl-getUserByUserEmail");
		return userDao.getUserByUserEmail(userEmail);
	}
	@Override
	public User getUserByUserNo(int userNo) throws Exception {
		System.out.println(" UserServiceImpl-getUserByUserNo");
		return userDao.getUserByUserNo(userNo);
	}
	
	@Override
	public User getUserByUserName(String userName) throws Exception {
		System.out.println(" UserServiceImpl-getUserByUserName");
		return userDao.getUserByUserName(userName);
	}

	
	@Override
	public void updateUser(User user) throws Exception {
		System.out.println(" UserServiceImpl-updateUser");
		List<Interest> userInterest = user.getInterestList();
		
		userInterestDao.deleteUserInterest(user.getUserNo());		
		
		for(Interest interest : userInterest){
			userInterestDao.addUserInterest(user.getUserNo(), interest.getInterestNo());
		}
		
		userDao.updateUser(user);
	}
	@Override
	public List<User> getUserList() throws Exception {
		System.out.println(" UserServiceImpl-getUserList");
		return userDao.getUserList();
	}

	

}
