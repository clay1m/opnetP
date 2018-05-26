package com.morgan.opnet.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morgan.opnet.util.CustomActivitySerializer;

@SuppressWarnings("serial")
@Entity
@Table(name="COMP_COST")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@costId")
public class ActivityCompressionCost implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer compressionCostId;
	
	@Column(name="duration_lb", nullable=false)
	private Double durationLB;
	
	@Column(name="duration_ub", nullable=false)
	private Double durationUB;
	
	@Column(name="compression_cost", nullable=false)
	private Double compressionCost;
	
//	@JsonBackReference("compressionCost-activity")
	@JsonSerialize(using = CustomActivitySerializer.class)
	@ManyToOne(optional = false)
	@JoinColumn(name = "activity_id")
	private Activity activity;
	
	public ActivityCompressionCost() {
		
	}
	
	public ActivityCompressionCost(Double durationLB, Double durationUB, Double compressionCost) {
		this.durationLB = durationLB;
		this.durationUB = durationUB;
		this.compressionCost = compressionCost;
		
		if (this.durationUB < this.durationLB) {
			this.durationUB = this.durationLB;
			this.durationLB = durationUB;
		}
	}

	public ActivityCompressionCost(Integer compressionCostId, Double durationLB, Double durationUB,
			Double compressionCost, Activity activity) {
		super();
		this.compressionCostId = compressionCostId;
		this.durationLB = durationLB;
		this.durationUB = durationUB;
		this.compressionCost = compressionCost;
		this.activity = activity;
	}

	public ActivityCompressionCost(Double durationLB, Double durationUB, Double compressionCost, Activity activity) {
		this.durationLB = durationLB;
		this.durationUB = durationUB;
		this.compressionCost = compressionCost;
		this.activity = activity;
		
		if (this.durationUB < this.durationLB) {
			this.durationUB = this.durationLB;
			this.durationLB = durationUB;
		}
	}

	
	public Integer getCompressionCostId() {
		return compressionCostId;
	}

	public void setCompressionCostId(Integer compressionCostId) {
		this.compressionCostId = compressionCostId;
	}

	public Double getDurationLB() {
		return durationLB;
	}

	public void setDurationLB(Double durationLB) {
		this.durationLB = durationLB;
	}

	public Double getDurationUB() {
		return durationUB;
	}

	public void setDurationUB(Double durationUB) {
		this.durationUB = durationUB;
	}

	public Double getCompressionCost() {
		return compressionCost;
	}

	public void setCompressionCost(Double compressionCost) {
		this.compressionCost = compressionCost;
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
		result = prime * result + ((compressionCost == null) ? 0 : compressionCost.hashCode());
		result = prime * result + ((compressionCostId == null) ? 0 : compressionCostId.hashCode());
		result = prime * result + ((durationLB == null) ? 0 : durationLB.hashCode());
		result = prime * result + ((durationUB == null) ? 0 : durationUB.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ActivityCompressionCost))
			return false;
		ActivityCompressionCost other = (ActivityCompressionCost) obj;
		if (activity == null) {
			if (other.activity != null)
				return false;
		} else if (!activity.equals(other.activity))
			return false;
		if (compressionCost == null) {
			if (other.compressionCost != null)
				return false;
		} else if (!compressionCost.equals(other.compressionCost))
			return false;
		if (compressionCostId == null) {
			if (other.compressionCostId != null)
				return false;
		} else if (!compressionCostId.equals(other.compressionCostId))
			return false;
		if (durationLB == null) {
			if (other.durationLB != null)
				return false;
		} else if (!durationLB.equals(other.durationLB))
			return false;
		if (durationUB == null) {
			if (other.durationUB != null)
				return false;
		} else if (!durationUB.equals(other.durationUB))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ActivityCompressionCost [compressionCostId=" + compressionCostId + ", durationLB=" + durationLB
				+ ", durationUB=" + durationUB + ", compressionCost=" + compressionCost + ", activity=" + activity.getActivityName()
				+ "]";
	}

}
