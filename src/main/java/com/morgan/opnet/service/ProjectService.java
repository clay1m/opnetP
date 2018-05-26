package com.morgan.opnet.service;

import java.util.List;

import com.morgan.opnet.model.Project;

public interface ProjectService {
	
	Project findById(Integer id);
	
	Project findByName(String name);
	
	void saveProject(Project project);
	
	void updateProject(Project project);
	
	void deleteById(Integer id);
	
	void deleteByName(String name); 
	
	List<Project> findAllProjects();

}
