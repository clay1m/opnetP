package com.morgan.opnet.dao;

import java.util.List;

import com.morgan.opnet.model.GeneralizedPrecedenceRelation;

public interface GeneralizedPrecedenceRelationDao {

	List<GeneralizedPrecedenceRelation> findAll();

	GeneralizedPrecedenceRelation findById(Integer id);
	
	GeneralizedPrecedenceRelation findByName(String name);

	void save(GeneralizedPrecedenceRelation gpr);

	List<GeneralizedPrecedenceRelation> findAllByActivityId(Integer activityId);

	void deleteById(Integer id);

}
