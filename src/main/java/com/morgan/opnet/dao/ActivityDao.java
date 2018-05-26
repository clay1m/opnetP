package com.morgan.opnet.dao;

import java.util.List;

import com.morgan.opnet.model.Activity;

public interface ActivityDao {
	
	Activity findById(Integer id);
	
	Activity findByName(String name);
	
	void save(Activity activity);
	
	void deleteById(Integer id);
	
	void deleteByName(String name); 
	
	List<Activity> findAllActivities();

}
