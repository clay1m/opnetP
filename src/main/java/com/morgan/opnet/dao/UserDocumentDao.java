package com.morgan.opnet.dao;

import java.util.List;

import com.morgan.opnet.model.UserDocument;

public interface UserDocumentDao {

	List<UserDocument> findAll();
	
	UserDocument findById(Integer id);
	
	void save(UserDocument document);
	
	List<UserDocument> findAllByUserId(Integer userId);
	
	void deleteById(Integer id);
}
