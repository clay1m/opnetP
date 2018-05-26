package com.morgan.opnet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.distribution.EnumeratedRealDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Splitter;
import com.google.common.primitives.Doubles;
import com.morgan.opnet.model.DurationDistribution.DistributionType;

@SuppressWarnings("serial")
@Entity
@Table(name="ACTIVITY")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@activityId")
public class Activity implements Serializable {
	
	public enum ActivityType { REG, DUMMY };
	     
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer activityId;
     
    @NotEmpty
    @Column(name="name", length=100, unique=true, nullable=false)
    private String activityName;
    
	@Column(name="type", length=5, nullable=false)
	@Enumerated(EnumType.STRING)
	private ActivityType activityType;
	
	
//	@JoinColumn(name = "durationDist_id")
//	@JsonManagedReference("activity-durationDist")
    @OneToOne(mappedBy="activity", cascade = CascadeType.ALL)
	private DurationDistribution durationDistribution;

//	@JsonBackReference("activity-project")
	@ManyToOne(optional = false)
	@JoinColumn(name = "project_id")
	private Project project;
    
//	@JsonManagedReference("rightActivity-gpr")
    @OneToMany(mappedBy="rightActivity", cascade = CascadeType.ALL)
    private Set<GeneralizedPrecedenceRelation> gprPredecessors = new HashSet<GeneralizedPrecedenceRelation>();
    
//	@JsonManagedReference("leftActivity-gpr")
    @OneToMany(mappedBy="leftActivity", cascade = CascadeType.ALL)
    private Set<GeneralizedPrecedenceRelation> gprSuccessors = new HashSet<GeneralizedPrecedenceRelation>();
    
//	@JsonManagedReference("activity-compressionCost")
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private Set<ActivityCompressionCost> compressionCosts = new HashSet<ActivityCompressionCost>();
	
	@Transient
	private List<Double> durations = new ArrayList<Double>();
	
	@Transient
	private int sampleSize = 5;

	public Activity() {
    }
	
	public Activity(Integer activityId, String activityName, ActivityType activityType) {
		this.activityId = activityId;
		this.activityName = activityName;
		this.activityType = activityType;
	}

	public Activity(String activityName, ActivityType activityType, Project project) {
		this.activityName = activityName;
		this.activityType = activityType;
		this.project = project;
	}
	
	public Activity(String activityName, ActivityType activityType, DurationDistribution durationDistribution, Project project) {
		this.activityName = activityName;
		this.activityType = activityType;
		this.project = project;
		durationDistribution.setActivity(this);
		this.durationDistribution = durationDistribution;
		this.durations = sampleDurations();
	}


	public Activity(Integer activityId, String activityName, ActivityType activityType,
			DurationDistribution durationDistribution, Project project,
			Set<GeneralizedPrecedenceRelation> gprPredecessors, Set<GeneralizedPrecedenceRelation> gprSuccessors,
			Set<ActivityCompressionCost> compressionCosts) {
		this.activityId = activityId;
		this.activityName = activityName;
		this.activityType = activityType;
		durationDistribution.setActivity(this);
		this.durationDistribution = durationDistribution;
		this.durations = sampleDurations();
		this.project = project;
		for (GeneralizedPrecedenceRelation gpr : gprPredecessors) {
			gpr.setRightActivity(this);
		}
		this.gprPredecessors = gprPredecessors;
		for (GeneralizedPrecedenceRelation gpr : gprSuccessors) {
			gpr.setLeftActivity(this);
		}
		this.gprSuccessors = gprSuccessors;
		for (ActivityCompressionCost cost : compressionCosts) {
			cost.setActivity(this);
		}
		this.compressionCosts = compressionCosts;
	}

	
	
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
	
	public DurationDistribution getDurationDistribution() {
		return durationDistribution;
	}

	public void setDurationDistribution(DurationDistribution durationDistribution) {
		durationDistribution.setActivity(this);
		this.durationDistribution = durationDistribution;
		this.durations = sampleDurations();
	}

	public Set<GeneralizedPrecedenceRelation> getGprPredecessors() {
		return gprPredecessors;
	}

	public void setGprPredecessors(Set<GeneralizedPrecedenceRelation> gprPredecessors) {
		for (GeneralizedPrecedenceRelation gpr : gprPredecessors) {
			gpr.setRightActivity(this);
		}
		this.gprPredecessors = gprPredecessors;
	}
	
	public void setGprPredecessorsWithArray(GeneralizedPrecedenceRelation[] gprPredecessors) {
		for (GeneralizedPrecedenceRelation gpr : gprPredecessors) {
			gpr.setRightActivity(this);
		}
		Set<GeneralizedPrecedenceRelation> gprSet = new HashSet<GeneralizedPrecedenceRelation>(Arrays.asList(gprPredecessors));
		this.gprPredecessors = gprSet;
	}
	
	public void addGprPredecessor(GeneralizedPrecedenceRelation gpr) {
		gpr.setRightActivity(this);
		this.gprPredecessors.add(gpr);
	}

	public Set<GeneralizedPrecedenceRelation> getGprSuccessors() {
		return gprSuccessors;
	}

	public void setGprSuccessors(Set<GeneralizedPrecedenceRelation> gprSuccessors) {
		for (GeneralizedPrecedenceRelation gpr : gprSuccessors) {
			gpr.setLeftActivity(this);
		}
		this.gprSuccessors = gprSuccessors;
	}
	
	public void setGprSuccessorsWithArray(GeneralizedPrecedenceRelation[] gprSuccessors) {
		for (GeneralizedPrecedenceRelation gpr : gprSuccessors) {
			gpr.setLeftActivity(this);
		}
		Set<GeneralizedPrecedenceRelation> gprSet = new HashSet<GeneralizedPrecedenceRelation>(Arrays.asList(gprSuccessors));
		this.gprSuccessors = gprSet;
	}
	
	public void addGprSuccessor(GeneralizedPrecedenceRelation gpr) {
		gpr.setLeftActivity(this);
		this.gprSuccessors.add(gpr);
	}

	public Set<ActivityCompressionCost> getCompressionCosts() {
		return compressionCosts;
	}

	public void setCompressionCosts(Set<ActivityCompressionCost> compressionCosts) {
		for (ActivityCompressionCost cost : compressionCosts) {
			cost.setActivity(this);
		}
		this.compressionCosts = compressionCosts;
	}
	
	public void addCompressionCost(ActivityCompressionCost cost) {
		cost.setActivity(this);
		this.compressionCosts.add(cost);
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	public List<Double> getDurations() {
		return durations;
	}

	public void setDurations(List<Double> durations) {
		this.durations = durations;
	}
	
	public int getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}
	
	private List<Double> sampleDurations() {
		
		String params = durationDistribution.getParams();
		List<String> strParamList = Splitter.on(',').splitToList(params);
//		strParamList.stream().map(Double::parseDouble);
		List<Double> dblParamList = new ArrayList<Double>();
		for (String s : strParamList) {
			dblParamList.add(Double.parseDouble(s));
		}
		
//    	IntStream.range(0, strParamList.size())
//        .mapToObj(strParamList::get)
//        .forEach(dblParamList::add);
		
		DistributionType type = durationDistribution.getDistType();
		RandomGenerator rng = new JDKRandomGenerator();
		
		switch (type) {
        case Single:
        	setDurations(dblParamList);
        	return dblParamList;
		case Discrete:
        	if (dblParamList.size() % 2 != 0) {
        		//must have even number of params
        	}
        	
        	//split params into list of values and associated probs
        	List<Double> valList = new ArrayList<Double>();
        	IntStream.range(0, dblParamList.size())
            .filter(n -> n % 2 == 0)
            .mapToObj(dblParamList::get)
            .forEach(valList::add);
        	
        	List<Double> probList = new ArrayList<Double>();
        	IntStream.range(0, dblParamList.size())
            .filter(n -> n % 2 != 0)
            .mapToObj(dblParamList::get)
            .forEach(probList::add);
        	
        	if (probList.stream().mapToDouble(w -> w).sum() != 1) {
        		//throw error for invalid params (probs must sum to 1)
        	}
        	
			EnumeratedRealDistribution discreteDist = new EnumeratedRealDistribution(rng,
					valList.stream().mapToDouble(w -> w).toArray(), probList.stream().mapToDouble(w -> w).toArray());
			
			return Doubles.asList(discreteDist.sample(sampleSize));

		case Uniform:
        	if (dblParamList.size() != 2) {
        		//throw error for invalid num of params
        	}
        	double lb = dblParamList.get(0).doubleValue();
        	double ub = dblParamList.get(1).doubleValue();
        	
        	if (lb >= ub) {
        		//throw error for invalid param vals
        	}
        	
        	UniformRealDistribution unifDist = new UniformRealDistribution(rng, lb, ub);
        	return Doubles.asList(unifDist.sample(sampleSize));
		case Triangular:
        	if (dblParamList.size() != 3) {
        		//throw error for invalid params
        	}
        	double min = dblParamList.get(0).doubleValue();
        	double mode = dblParamList.get(1).doubleValue();
        	double max = dblParamList.get(2).doubleValue();
        	
        	if (min >= mode || mode >= max || min >= max) {
        		//throw error for invalid param vals
        	}
        	
        	TriangularDistribution trigDist = new TriangularDistribution(rng, min, mode, max);
        	return Doubles.asList(trigDist.sample(sampleSize));
		case Exponential:
        	if (dblParamList.size() != 1) {
        		//throw error for invalid params
        	}
        	double mean = dblParamList.get(0).doubleValue();
        	
        	ExponentialDistribution expoDist = new ExponentialDistribution(rng, mean);
        	return Doubles.asList(expoDist.sample(sampleSize));
        }
		return null;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getActivityId() == null) ? 0 : getActivityId().hashCode());
		result = prime * result + ((getActivityName() == null) ? 0 : getActivityName().hashCode());
		result = prime * result + ((getActivityType() == null) ? 0 : getActivityType().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Activity))
			return false;
		Activity other = (Activity) obj;
		if (getActivityId() == null) {
			if (other.getActivityId() != null)
				return false;
		} else if (!getActivityId().equals(other.getActivityId()))
			return false;
		if (getActivityName() == null) {
			if (other.getActivityName() != null)
				return false;
		} else if (!getActivityName().equals(other.getActivityName()))
			return false;
		if (getActivityType() != other.getActivityType())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Activity [activityId=" + activityId + ", activityName=" + activityName + ", activityType="
				+ activityType + ", durationDistribution=" + durationDistribution + ", project=" + project.getProjectName()
				+ ", gprPredecessors=" + gprPredecessors + ", gprSuccessors=" + gprSuccessors + ", compressionCosts="
				+ compressionCosts + ", durations=" + durations + ", sampleSize=" + sampleSize + "]";
	}
	
	
	
	

//	@Override
//	public String toString() {
//		return "Activity [activityId=" + activityId + ", activityName=" + activityName + ", activityType="
//				+ activityType + ", project=" + project.getProjectName() + ", gprPredecessors=" + gprPredecessors + ", gprSuccessors="
//				+ gprSuccessors + ", compressionCosts=" + compressionCosts + "]";
//	}

	

}
