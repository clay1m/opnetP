package com.morgan.opnet.service;

import java.util.List;

import com.morgan.opnet.model.GeneralizedPrecedenceRelation;

public interface GeneralizedPrecedenceRelationService {
	
	List<GeneralizedPrecedenceRelation> findAll();

	GeneralizedPrecedenceRelation findById(Integer id);

	void saveGPR(GeneralizedPrecedenceRelation gpr);

	List<GeneralizedPrecedenceRelation> findAllByActivityId(Integer activityId);

	void deleteById(Integer id);

}
