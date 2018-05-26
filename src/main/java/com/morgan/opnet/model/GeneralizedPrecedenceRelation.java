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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morgan.opnet.util.CustomActivitySerializer;

@SuppressWarnings("serial")
@Entity
@Table(name="GPR")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@gprId")
public class GeneralizedPrecedenceRelation implements Serializable {
	
	public enum GPRType { FS, FF, SS, SF };
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer gprId;
	
	@NotEmpty
    @Column(name="name", length=100, unique=true, nullable=false)
    private String gprName;
	
	@Column(name="type", length=2, nullable=false)
	@Enumerated(EnumType.STRING)
	private GPRType gprType;
	
	@Column(name="delta", nullable=false)
	private Double delta;
 
//	@JsonBackReference("gpr-leftActivity")
	@JsonSerialize(using = CustomActivitySerializer.class)
    @ManyToOne
    @JoinColumn(name="left_activity_id", nullable=false)
    private Activity leftActivity;
    
//	@JsonBackReference("gpr-rightActivity")
	@JsonSerialize(using = CustomActivitySerializer.class)
    @ManyToOne
    @JoinColumn(name="right_activity_id", nullable=false)
    private Activity rightActivity;
	
	public GeneralizedPrecedenceRelation() {
		
	}
	
	public GeneralizedPrecedenceRelation(Integer gprId, String gprName, GPRType gprType, Double delta,
			Activity leftActivity, Activity rightActivity) {
		this.gprId = gprId;
		this.gprName = gprName;
		this.gprType = gprType;
		this.delta = delta;
		this.leftActivity = leftActivity;
		this.rightActivity = rightActivity;
	}

	public GeneralizedPrecedenceRelation(String gprName, GPRType gprType, Double delta,
			Activity leftActivity, Activity rightActivity) {
		this.gprName = gprName;
		this.gprType = gprType;
		this.delta = delta;
		this.leftActivity = leftActivity;
		this.rightActivity = rightActivity;
	}

	public Integer getGprId() {
		return gprId;
	}

	public void setGprId(Integer gprId) {
		this.gprId = gprId;
	}

	public String getGprName() {
		return gprName;
	}

	public void setGprName(String gprName) {
		this.gprName = gprName;
	}

	public GPRType getGprType() {
		return gprType;
	}

	public void setGprType(GPRType gprType) {
		this.gprType = gprType;
	}

	public Double getDelta() {
		return delta;
	}

	public void setDelta(Double delta) {
		this.delta = delta;
	}

	public Activity getLeftActivity() {
		return leftActivity;
	}

	public void setLeftActivity(Activity leftActivity) {
		this.leftActivity = leftActivity;
	}

	public Activity getRightActivity() {
		return rightActivity;
	}

	public void setRightActivity(Activity rightActivity) {
		this.rightActivity = rightActivity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delta == null) ? 0 : delta.hashCode());
		result = prime * result + ((gprId == null) ? 0 : gprId.hashCode());
		result = prime * result + ((gprName == null) ? 0 : gprName.hashCode());
		result = prime * result + ((gprType == null) ? 0 : gprType.hashCode());
		result = prime * result + ((leftActivity == null) ? 0 : leftActivity.hashCode());
		result = prime * result + ((rightActivity == null) ? 0 : rightActivity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GeneralizedPrecedenceRelation))
			return false;
		GeneralizedPrecedenceRelation other = (GeneralizedPrecedenceRelation) obj;
		if (delta == null) {
			if (other.delta != null)
				return false;
		} else if (!delta.equals(other.delta))
			return false;
		if (gprId == null) {
			if (other.gprId != null)
				return false;
		} else if (!gprId.equals(other.gprId))
			return false;
		if (gprName == null) {
			if (other.gprName != null)
				return false;
		} else if (!gprName.equals(other.gprName))
			return false;
		if (gprType != other.gprType)
			return false;
		if (leftActivity == null) {
			if (other.leftActivity != null)
				return false;
		} else if (!leftActivity.equals(other.leftActivity))
			return false;
		if (rightActivity == null) {
			if (other.rightActivity != null)
				return false;
		} else if (!rightActivity.equals(other.rightActivity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GeneralizedPrecedenceRelation [gprId=" + gprId + ", gprName=" + gprName + ", gprType=" + gprType
				+ ", delta=" + delta + ", leftActivity=" + leftActivity.getActivityName() + ", rightActivity=" + rightActivity.getActivityName() + "]";
	}
	
	
	
}
