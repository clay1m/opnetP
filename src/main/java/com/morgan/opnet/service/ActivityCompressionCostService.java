package com.morgan.opnet.service;

import java.util.List;

import com.morgan.opnet.model.ActivityCompressionCost;

public interface ActivityCompressionCostService {
	
	ActivityCompressionCost findById(Integer activityId, Integer costId);
	
	ActivityCompressionCost findByDurationLB(Integer activityId, Double durationLB);
	
	ActivityCompressionCost findByDurationUB(Integer activityId, Double durationUB);

	List<ActivityCompressionCost> findAllByActivityId(Integer activityId);
	
	void saveCompressionCost(ActivityCompressionCost compressionCost);

	void deleteById(Integer activityId, Integer costId);
	
	void deleteAllByActivityId(Integer activityId);

}
