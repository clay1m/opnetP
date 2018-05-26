package com.morgan.opnet.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.morgan.opnet.model.DurationDistribution;

@Repository("durationDistributionDao")
public class DurationDistributionDaoImpl extends AbstractDao<Integer, DurationDistribution> implements DurationDistributionDao {

	
	public DurationDistribution findByActivityId(Integer activityId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("activityId", activityId));
		DurationDistribution dist  = (DurationDistribution) crit.uniqueResult();
		return dist;
	}

	public void save(DurationDistribution durationDistribution) {
		persist(durationDistribution);
	}

	public void deleteByActivityId(Integer activityId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("activityId", activityId));
		DurationDistribution dist  = (DurationDistribution) crit.uniqueResult();
		delete(dist);
	}

}
