package com.morgan.opnet.service;

import com.morgan.opnet.model.DurationDistribution;

public interface DurationDistributionService {
	
	DurationDistribution findByActivityId(Integer activityId);
	
	void saveDurationDist(DurationDistribution durationDistribution);

	void deleteByActivityId(Integer activityId);

}
