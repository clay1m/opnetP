package com.morgan.opnet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morgan.opnet.dao.DurationDistributionDao;
import com.morgan.opnet.dao.GeneralizedPrecedenceRelationDao;
import com.morgan.opnet.model.DurationDistribution;

@Service("durationDistributionService")
@Transactional
public class DurationDistributionServiceImpl implements DurationDistributionService {
	
	@Autowired
	DurationDistributionDao dao;

	@Override
	public DurationDistribution findByActivityId(Integer activityId) {
		DurationDistribution dist = dao.findByActivityId(activityId);
		return dist;
	}

	@Override
	public void saveDurationDist(DurationDistribution durationDistribution) {
		dao.save(durationDistribution);
	}

	@Override
	public void deleteByActivityId(Integer activityId) {
		dao.deleteByActivityId(activityId);
	}

}
