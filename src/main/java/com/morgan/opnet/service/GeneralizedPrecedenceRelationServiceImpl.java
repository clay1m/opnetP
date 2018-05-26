package com.morgan.opnet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morgan.opnet.dao.GeneralizedPrecedenceRelationDao;
import com.morgan.opnet.model.GeneralizedPrecedenceRelation;

@Service("gprService")
@Transactional
public class GeneralizedPrecedenceRelationServiceImpl implements GeneralizedPrecedenceRelationService {

	@Autowired
	GeneralizedPrecedenceRelationDao dao;
	
	@Override
	public List<GeneralizedPrecedenceRelation> findAll() {
		List<GeneralizedPrecedenceRelation> gprs = dao.findAll();
		return gprs;
	}

	@Override
	public GeneralizedPrecedenceRelation findById(Integer id) {
		GeneralizedPrecedenceRelation gpr = dao.findById(id);
		return gpr;
	}

	@Override
	public void saveGPR(GeneralizedPrecedenceRelation gpr) {
		dao.save(gpr);
	}

	@Override
	public List<GeneralizedPrecedenceRelation> findAllByActivityId(Integer activityId) {
		List<GeneralizedPrecedenceRelation> gprs = dao.findAllByActivityId(activityId);
		return gprs;
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

}
