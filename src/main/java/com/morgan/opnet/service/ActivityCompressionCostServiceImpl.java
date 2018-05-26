package com.morgan.opnet.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morgan.opnet.dao.ActivityCompressionCostDao;
import com.morgan.opnet.model.ActivityCompressionCost;

@Service("compressionCostService")
@Transactional
public class ActivityCompressionCostServiceImpl implements ActivityCompressionCostService {
	
	@Autowired
	ActivityCompressionCostDao dao;

	@Override
	public ActivityCompressionCost findById(Integer activityId, Integer costId) {
		ActivityCompressionCost cost = dao.findById(activityId, costId);
		return cost;
	}

	@Override
	public ActivityCompressionCost findByDurationLB(Integer activityId, Double durationLB) {
		ActivityCompressionCost cost = dao.findByDurationLB(activityId, durationLB);
		return cost;
	}

	@Override
	public ActivityCompressionCost findByDurationUB(Integer activityId, Double durationUB) {
		ActivityCompressionCost cost = dao.findByDurationUB(activityId, durationUB);
		return cost;
	}

	@Override
	public List<ActivityCompressionCost> findAllByActivityId(Integer activityId) {
		List<ActivityCompressionCost> costs = dao.findAllByActivityId(activityId);
		return costs;
	}

	@Override
	public void saveCompressionCost(ActivityCompressionCost compressionCost) {
		dao.save(compressionCost);
	}

	@Override
	public void deleteById(Integer activityId, Integer costId) {
		dao.deleteById(activityId, costId);
	}

	@Override
	public void deleteAllByActivityId(Integer activityId) {
		dao.deleteAllByActivityId(activityId);
	}

}
