package com.morgan.opnet.dao;

import java.util.List;

import com.morgan.opnet.model.User;


public interface UserDao {

	User findById(Integer id);
	
	User findWithProjectsById(Integer id);
	
	User findBySSO(String sso);
	
	void save(User user);
	
	void deleteBySSO(String sso);
	
	List<User> findAllUsers();

}

