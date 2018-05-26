package com.morgan.opnet.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.morgan.opnet.model.Project;

@Repository("activityNetworkDao")
public class ProjectDaoImpl extends AbstractDao<Integer, Project> implements ProjectDao {
	
	@Override
	public Project findById(Integer id) {
		Project project = getByKey(id);
		return project;
	}

	@Override
	public Project findByName(String name) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("projectName", name));
		Project project = (Project) crit.uniqueResult();
		return project;
	}

	@Override
	public void save(Project project) {
		persist(project);
	}

	@Override
	public void deleteById(Integer id) {
		Project project = getByKey(id);
		delete(project);
	}

	@Override
	public void deleteByName(String name) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("projectName", name));
		Project project = (Project) crit.uniqueResult();
		delete(project);
	}

	@SuppressWarnings("unchecked")
	public List<Project> findAllProjects() {
		Criteria criteria = createEntityCriteria();
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<Project> projects = (List<Project>) criteria.list();
		return projects;
	}

}
