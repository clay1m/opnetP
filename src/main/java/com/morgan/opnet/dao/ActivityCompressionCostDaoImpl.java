package com.morgan.opnet.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.morgan.opnet.model.ActivityCompressionCost;

@Repository("compressionCostDao")
public class ActivityCompressionCostDaoImpl extends AbstractDao<Integer, ActivityCompressionCost> implements ActivityCompressionCostDao {

	public void save(ActivityCompressionCost cost) {
		persist(cost);
	}

	@SuppressWarnings("unchecked")
	public List<ActivityCompressionCost> findAllByActivityId(Integer activityId) {
		Criteria crit = createEntityCriteria();
		Criteria costCriteria = crit.createCriteria("activity");
		costCriteria.add(Restrictions.eq("activityId", activityId));
		List<ActivityCompressionCost> costs = (List<ActivityCompressionCost>) crit.list();
		return costs;
	}

	public ActivityCompressionCost findById(Integer activityId, Integer costId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("compressionCostId", costId));
		Criteria costCriteria = crit.createCriteria("activity");
		costCriteria.add(Restrictions.eq("activityId", activityId));
		ActivityCompressionCost cost  = (ActivityCompressionCost) crit.uniqueResult();
		return cost;
	}

	public ActivityCompressionCost findByDurationLB(Integer activityId, Double durationLB) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("durationLB", durationLB));
		Criteria costCriteria = crit.createCriteria("activity");
		costCriteria.add(Restrictions.eq("activityId", activityId));
		ActivityCompressionCost cost  = (ActivityCompressionCost) crit.uniqueResult();
		return cost;
	}

	public ActivityCompressionCost findByDurationUB(Integer activityId, Double durationUB) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("durationUB", durationUB));
		Criteria costCriteria = crit.createCriteria("activity");
		costCriteria.add(Restrictions.eq("activityId", activityId));
		ActivityCompressionCost cost  = (ActivityCompressionCost) crit.uniqueResult();
		return cost;
	}

	public void deleteById(Integer activityId, Integer costId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("compressionCostId", costId));
		Criteria costCriteria = crit.createCriteria("activity");
		costCriteria.add(Restrictions.eq("activityId", activityId));
		ActivityCompressionCost cost  = (ActivityCompressionCost) crit.uniqueResult();
		delete(cost);
	}

	
	@SuppressWarnings("unchecked")
	public void deleteAllByActivityId(Integer activityId) {
		Criteria crit = createEntityCriteria();
		Criteria costCriteria = crit.createCriteria("activity");
		costCriteria.add(Restrictions.eq("activityId", activityId));
		List<ActivityCompressionCost> costs = (List<ActivityCompressionCost>) crit.list();
		deleteList(costs);
	}

	
}
