package com.morgan.opnet.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.morgan.opnet.model.Activity;

@Repository("activityDao")
public class ActivityDaoImpl extends AbstractDao<Integer, Activity> implements ActivityDao {

	public Activity findById(Integer id) {
		Activity activity = getByKey(id);
		return activity;
	}

	public void save(Activity activity) {
		persist(activity);
	}

	public void deleteById(Integer id) {
		Activity activity = getByKey(id);
		delete(activity);
	}

	public void deleteByName(String name) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("activityName", name));
		Activity activity = (Activity) crit.uniqueResult();
		delete(activity);
	}

	@SuppressWarnings("unchecked")
	public List<Activity> findAllActivities() {
		Criteria criteria = createEntityCriteria();
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<Activity> activities = (List<Activity>) criteria.list();
		return activities;
	}

	@Override
	public Activity findByName(String name) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("activityName", name));
		Activity activity = (Activity) crit.uniqueResult();
		return activity;
	}

}
