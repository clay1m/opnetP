package com.morgan.opnet.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morgan.opnet.dao.ProjectDao;
import com.morgan.opnet.model.Project;

@Service("activityNetworkService")
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectDao dao;
	
	@Override
	public Project findById(Integer id) {
		Project project = dao.findById(id);
		return project;
	}

	@Override
	public Project findByName(String name) {
		Project project = dao.findByName(name);
		return project;
	}

	@Override
	public void saveProject(Project project) {
		dao.save(project);
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public void deleteByName(String name) {
		dao.deleteByName(name);
	}

	@Override
	public List<Project> findAllProjects() {
		List<Project> projects = dao.findAllProjects();
		return projects;
	}

	@Override
	public void updateProject(Project project) {
		Project entity = dao.findById(project.getProjectId());
		if(entity!=null){
			entity.setProjectName(project.getProjectName());
			entity.setActivities(project.getActivities());
		}
	}

}
