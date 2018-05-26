package com.morgan.opnet.dao;

import java.util.List;

import com.morgan.opnet.model.Project;

public interface ProjectDao {
	
	Project findById(Integer id);
	
	Project findByName(String name);
	
	void save(Project project);
	
	void deleteById(Integer id);
	
	void deleteByName(String name); 
	
	List<Project> findAllProjects();

}
