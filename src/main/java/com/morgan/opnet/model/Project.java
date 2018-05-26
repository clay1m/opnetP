package com.morgan.opnet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
@Table(name="PROJECT")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@projectId")
public class Project implements Serializable {
	
	@Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer projectId;
     
    @NotEmpty
    @Column(name="name", length=50, unique=true, nullable=false)
    private String projectName;
    
//    @JsonManagedReference("project-activity")
    @OneToMany(mappedBy="project", cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<Activity>();
    
    @JsonIgnore
    @ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	public Project() {
	}
	
	public Project(String projectName) {
		this.projectName = projectName;
	}

	public Project(String projectName, List<Activity> activities) {
		this.projectName = projectName;
		for (Activity activity : activities) {
			activity.setProject(this);
		}
		this.activities = activities;
	}
	
	public Project(Integer projectId, String projectName, List<Activity> activities, User user) {
		super();
		this.projectId = projectId;
		this.projectName = projectName;
		for (Activity activity : activities) {
			activity.setProject(this);
		}
		this.activities = activities;
		this.user = user;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		for (Activity a : activities) {
			a.setProject(this);
		}
		this.activities = activities;
	}
	
	public void setActivitiesWithArray(Activity[] activities) {
		for (Activity a : activities) {
			a.setProject(this);
		}
		List<Activity> activityList = new ArrayList<Activity>(Arrays.asList(activities));
		this.activities = activityList;
	}
	
	public void addActivity(Activity activity) {
		activity.setProject(this);
		this.activities.add(activity);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activities == null) ? 0 : activities.hashCode());
		result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
		result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Project))
			return false;
		Project other = (Project) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (projectId == null) {
			if (other.projectId != null)
				return false;
		} else if (!projectId.equals(other.projectId))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [projectId=" + projectId + ", projectName=" + projectName + ", activities=" + activities
				+ ", user=" + user.getSsoId() + "]";
	}
	
	
    
    
    
}
