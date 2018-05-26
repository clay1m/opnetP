package com.morgan.opnet.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.morgan.opnet.model.GeneralizedPrecedenceRelation;

@Repository("gprDao")
public class GeneralizedPrecedenceRelationDaoImpl extends AbstractDao<Integer, GeneralizedPrecedenceRelation> implements GeneralizedPrecedenceRelationDao {

	@SuppressWarnings("unchecked")
	public List<GeneralizedPrecedenceRelation> findAll() {
		Criteria criteria = createEntityCriteria();
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<GeneralizedPrecedenceRelation> gprs = (List<GeneralizedPrecedenceRelation>) criteria.list();
		return gprs;
	}

	public GeneralizedPrecedenceRelation findById(Integer id) {
		GeneralizedPrecedenceRelation gpr = getByKey(id);
		return gpr;
	}

	public void save(GeneralizedPrecedenceRelation gpr) {
		persist(gpr);
	}

	@SuppressWarnings("unchecked")
	public List<GeneralizedPrecedenceRelation> findAllByActivityId(Integer activityId) {
		Criteria crit = createEntityCriteria();
	    crit.add(Restrictions.disjunction()
	        .add(Restrictions.eq("leftActivityId", activityId))
	        .add(Restrictions.eq("rightActivityId", activityId))
	    );
	    List<GeneralizedPrecedenceRelation> gprs = (List<GeneralizedPrecedenceRelation>) crit.list();
		return gprs;
	}

	public void deleteById(Integer id) {
		// TODO need to impose FK contraints in db and/or hibernate
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("gprId", id));
		GeneralizedPrecedenceRelation gpr  = (GeneralizedPrecedenceRelation) crit.uniqueResult();
		delete(gpr);
	}

	@Override
	public GeneralizedPrecedenceRelation findByName(String name) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("gprName", name));
		GeneralizedPrecedenceRelation gpr = (GeneralizedPrecedenceRelation) crit.uniqueResult();
		return gpr;
	}

}
