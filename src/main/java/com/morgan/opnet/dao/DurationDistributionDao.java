package com.morgan.opnet.dao;

import com.morgan.opnet.model.DurationDistribution;

public interface DurationDistributionDao {
	
	DurationDistribution findByActivityId(Integer activityId);
	
	void save(DurationDistribution durationDistribution);

	void deleteByActivityId(Integer activityId);
}
