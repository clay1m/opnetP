/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.morgan.opnet.swig.activitynet;

public class arcData {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected arcData(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(arcData obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        activity_networkJNI.delete_arcData(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setId(int value) {
    activity_networkJNI.arcData_id_set(swigCPtr, this, value);
  }

  public int getId() {
    return activity_networkJNI.arcData_id_get(swigCPtr, this);
  }

  public void setType(ActivityType value) {
    activity_networkJNI.arcData_type_set(swigCPtr, this, value.swigValue());
  }

  public ActivityType getType() {
    return ActivityType.swigToEnum(activity_networkJNI.arcData_type_get(swigCPtr, this));
  }

  public void setGpr(GPRType value) {
    activity_networkJNI.arcData_gpr_set(swigCPtr, this, value.swigValue());
  }

  public GPRType getGpr() {
    return GPRType.swigToEnum(activity_networkJNI.arcData_gpr_get(swigCPtr, this));
  }

  public void setName(SWIGTYPE_p_std__string value) {
    activity_networkJNI.arcData_name_set(swigCPtr, this, SWIGTYPE_p_std__string.getCPtr(value));
  }

  public SWIGTYPE_p_std__string getName() {
    return new SWIGTYPE_p_std__string(activity_networkJNI.arcData_name_get(swigCPtr, this), true);
  }

  public void setDurations(SWIGTYPE_p_arma__vec value) {
    activity_networkJNI.arcData_durations_set(swigCPtr, this, SWIGTYPE_p_arma__vec.getCPtr(value));
  }

  public SWIGTYPE_p_arma__vec getDurations() {
    return new SWIGTYPE_p_arma__vec(activity_networkJNI.arcData_durations_get(swigCPtr, this), true);
  }

  public void setCurrentDuration(double value) {
    activity_networkJNI.arcData_currentDuration_set(swigCPtr, this, value);
  }

  public double getCurrentDuration() {
    return activity_networkJNI.arcData_currentDuration_get(swigCPtr, this);
  }

  public void setCurrentInterval(int value) {
    activity_networkJNI.arcData_currentInterval_set(swigCPtr, this, value);
  }

  public int getCurrentInterval() {
    return activity_networkJNI.arcData_currentInterval_get(swigCPtr, this);
  }

  public void setCostRates(SWIGTYPE_p_arma__vec value) {
    activity_networkJNI.arcData_costRates_set(swigCPtr, this, SWIGTYPE_p_arma__vec.getCPtr(value));
  }

  public SWIGTYPE_p_arma__vec getCostRates() {
    return new SWIGTYPE_p_arma__vec(activity_networkJNI.arcData_costRates_get(swigCPtr, this), true);
  }

  public void setCurrentCostRate(double value) {
    activity_networkJNI.arcData_currentCostRate_set(swigCPtr, this, value);
  }

  public double getCurrentCostRate() {
    return activity_networkJNI.arcData_currentCostRate_get(swigCPtr, this);
  }

  public void setCostBreakPoints(SWIGTYPE_p_arma__vec value) {
    activity_networkJNI.arcData_costBreakPoints_set(swigCPtr, this, SWIGTYPE_p_arma__vec.getCPtr(value));
  }

  public SWIGTYPE_p_arma__vec getCostBreakPoints() {
    return new SWIGTYPE_p_arma__vec(activity_networkJNI.arcData_costBreakPoints_get(swigCPtr, this), true);
  }

  public void setCritical(boolean value) {
    activity_networkJNI.arcData_critical_set(swigCPtr, this, value);
  }

  public boolean getCritical() {
    return activity_networkJNI.arcData_critical_get(swigCPtr, this);
  }

  public void setTotalFloat(double value) {
    activity_networkJNI.arcData_totalFloat_set(swigCPtr, this, value);
  }

  public double getTotalFloat() {
    return activity_networkJNI.arcData_totalFloat_get(swigCPtr, this);
  }

  public void setFlow(double value) {
    activity_networkJNI.arcData_flow_set(swigCPtr, this, value);
  }

  public double getFlow() {
    return activity_networkJNI.arcData_flow_get(swigCPtr, this);
  }

  public void setGprActivityA(int value) {
    activity_networkJNI.arcData_gprActivityA_set(swigCPtr, this, value);
  }

  public int getGprActivityA() {
    return activity_networkJNI.arcData_gprActivityA_get(swigCPtr, this);
  }

  public void setGprActivityB(int value) {
    activity_networkJNI.arcData_gprActivityB_set(swigCPtr, this, value);
  }

  public int getGprActivityB() {
    return activity_networkJNI.arcData_gprActivityB_get(swigCPtr, this);
  }

  public void setGprDelta(double value) {
    activity_networkJNI.arcData_gprDelta_set(swigCPtr, this, value);
  }

  public double getGprDelta() {
    return activity_networkJNI.arcData_gprDelta_get(swigCPtr, this);
  }

  public void setTotalIntervals(int value) {
    activity_networkJNI.arcData_totalIntervals_set(swigCPtr, this, value);
  }

  public int getTotalIntervals() {
    return activity_networkJNI.arcData_totalIntervals_get(swigCPtr, this);
  }

  public arcData() {
    this(activity_networkJNI.new_arcData(), true);
  }

}
