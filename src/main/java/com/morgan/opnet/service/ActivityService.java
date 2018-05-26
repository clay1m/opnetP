package com.morgan.opnet.service;

import java.util.List;

import com.morgan.opnet.model.Activity;

public interface ActivityService {
	
	Activity findById(Integer id);
	
	void saveActivity(Activity activity);
	
	void updateActivity(Activity activity);
	
	void deleteById(Integer id);
	
	void deleteByName(String name); 
	
	List<Activity> findAllActivities();

}
