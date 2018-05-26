package com.morgan.opnet.service;

import java.util.List;

import com.morgan.opnet.model.User;

public interface UserService {
	
	User findById(Integer id);
	
	User findWithProjectsById(Integer id);
	
	User findBySSO(String sso);
	
	void saveUser(User user);
	
	void updateUser(User user);
	
	void deleteUserBySSO(String sso);

	List<User> findAllUsers(); 
	
	boolean isUserSSOUnique(Integer id, String sso);

}