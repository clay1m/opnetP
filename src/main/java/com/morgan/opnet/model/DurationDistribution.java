package com.morgan.opnet.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morgan.opnet.util.CustomActivitySerializer;

@SuppressWarnings("serial")
@Entity
@Table(name="DURATION_DIST")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@durationDistId")
public class DurationDistribution implements Serializable {
	
	public enum DistributionType { Single, Discrete, Uniform, Triangular, Exponential };
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer durationDistId;
	
	@Column(name="type", length=10, nullable=false)
	@Enumerated(EnumType.STRING)
	private DistributionType distType;
	
	//implement variable length array of distribution params as string eg: "1,2,3"
	@Column(name="params", length=200, nullable=false)
	private String params; 
	
//	@JsonBackReference("compressionCost-activity")
	@JsonSerialize(using = CustomActivitySerializer.class)
//	@JsonDeserialize()
	@OneToOne(optional = false)
	@JoinColumn(name = "activity_id")
	private Activity activity;
	
	public DurationDistribution() {
		
	}
	
	public DurationDistribution(DistributionType distType, String params) {
		this.distType = distType;
		this.params = params;
	}

	public DurationDistribution(DistributionType distType, String params, Activity activity) {
		this.distType = distType;
		this.params = params;
		this.activity = activity;
	}
	
	public DurationDistribution(Integer durationDistId, DistributionType distType, String params, Activity activity) {
		super();
		this.durationDistId = durationDistId;
		this.distType = distType;
		this.params = params;
		this.activity = activity;
	}

	public Integer getDurationDistId() {
		return durationDistId;
	}

	public void setDurationDistId(Integer durationDistId) {
		this.durationDistId = durationDistId;
	}

	public DistributionType getDistType() {
		return distType;
	}

	public void setDistType(DistributionType distType) {
		this.distType = distType;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activity == null) ? 0 : activity.hashCode());
		result = prime * result + ((distType == null) ? 0 : distType.hashCode());
		result = prime * result + ((durationDistId == null) ? 0 : durationDistId.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DurationDistribution))
			return false;
		DurationDistribution other = (DurationDistribution) obj;
		if (activity == null) {
			if (other.activity != null)
				return false;
		} else if (!activity.equals(other.activity))
			return false;
		if (distType != other.distType)
			return false;
		if (durationDistId == null) {
			if (other.durationDistId != null)
				return false;
		} else if (!durationDistId.equals(other.durationDistId))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DurationDistribution [durationDistId=" + durationDistId + ", distType=" + distType + ", params="
				+ params + ", activity=" + activity.getActivityName() +  "]";
	}
	
	
	
	

}
