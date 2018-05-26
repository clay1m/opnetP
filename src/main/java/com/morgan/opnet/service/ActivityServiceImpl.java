package com.morgan.opnet.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morgan.opnet.dao.ActivityDao;
import com.morgan.opnet.model.Activity;

@Service("activityService")
@Transactional
public class ActivityServiceImpl implements ActivityService {
	
	@Autowired
	ActivityDao dao;
	
	@Override
	public Activity findById(Integer id) {
		Activity activity = dao.findById(id);
		return activity;
	}

	@Override
	public void saveActivity(Activity activity) {
		dao.save(activity);
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public void deleteByName(String name) {
		dao.deleteByName(name);
	}

	@Override
	public List<Activity> findAllActivities() {
		List<Activity> activities = dao.findAllActivities();
		return activities;
	}

	@Override
	public void updateActivity(Activity activity) {
		Activity entity = dao.findById(activity.getActivityId());
		if(entity!=null){
			entity.setActivityName(activity.getActivityName());
			entity.setActivityType(activity.getActivityType());
			entity.setCompressionCosts(activity.getCompressionCosts());
			entity.setGprPredecessors(activity.getGprPredecessors());
			entity.setGprSuccessors(activity.getGprSuccessors());
		}
	}

}
