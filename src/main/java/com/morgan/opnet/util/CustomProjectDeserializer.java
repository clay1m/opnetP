package com.morgan.opnet.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.morgan.opnet.model.Activity;
import com.morgan.opnet.model.ActivityCompressionCost;
import com.morgan.opnet.model.DurationDistribution;
import com.morgan.opnet.model.GeneralizedPrecedenceRelation;
import com.morgan.opnet.model.Project;
import com.morgan.opnet.model.User;
import com.morgan.opnet.model.Activity.ActivityType;
import com.morgan.opnet.model.DurationDistribution.DistributionType;
import com.morgan.opnet.model.GeneralizedPrecedenceRelation.GPRType;

public class CustomProjectDeserializer extends JsonDeserializer<Project> {
	
	private String projectAsJSON;

	@Override
	public Project deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		JsonNode projectNode = jp.getCodec().readTree(jp);
		
		int projectId = (Integer) ((IntNode) projectNode.get("projectId")).numberValue();
		String projectName = projectNode.get("projectName").asText();
		
		
		JsonNode activityArrayNode = projectNode.get("activities");
		List<Activity> activities = new ArrayList<Activity>();
		
    	Map<Integer,Activity> activityMap = new HashMap<Integer,Activity>();
    	//populate activity map on first pass
    	for (JsonNode activityNode : activityArrayNode) {
    		
    		int activityId = (Integer) ((IntNode) activityNode.get("activityId")).numberValue();
			String activityName = activityNode.get("activityName").asText();
			ActivityType activityType = ActivityType.valueOf(activityNode.get("activityType").asText());
			
			Activity activity = new Activity(activityId, activityName, activityType);
			activityMap.put(activityId, activity);
    		
    	}
    	
		
		//create activities and contained sub-objects on second pass
		for (JsonNode activityNode : activityArrayNode) {
			
			int activityId = (Integer) ((IntNode) activityNode.get("activityId")).numberValue();
			Activity activity = activityMap.get(activityId);
			
			JsonNode durationDistributionNode = activityNode.get("durationDistribution");
			int durationDistId = (Integer) ((IntNode) durationDistributionNode.get("durationDistId")).numberValue();
			DistributionType distType = DistributionType.valueOf(durationDistributionNode.get("distType").asText());
			String params = durationDistributionNode.get("params").asText();
			DurationDistribution durationDistribution = new DurationDistribution(durationDistId, distType, params, activity);
			
			Set<GeneralizedPrecedenceRelation> gprPredecessors = new HashSet<GeneralizedPrecedenceRelation>();
			
			JsonNode gprPredecessorsArrayNode = activityNode.get("gprPredecessors");
			for (JsonNode gprPredecessorNode : gprPredecessorsArrayNode) {
				int gprId = (Integer) ((IntNode) gprPredecessorNode.get("gprId")).numberValue();
				String gprName = gprPredecessorNode.get("gprName").asText();
				GPRType gprType = GPRType.valueOf(gprPredecessorNode.get("gprType").asText());
//				double delta = (Double) ((DoubleNode) gprPredecessorNode.get("delta")).numberValue();
				double delta = gprPredecessorNode.get("delta").asDouble();
				int leftActivityId = (Integer) ((IntNode) gprPredecessorNode.get("leftActivity")).numberValue();
				int rightActivityId = (Integer) ((IntNode) gprPredecessorNode.get("rightActivity")).numberValue();
				Activity leftActivity = activityMap.get(leftActivityId);
				Activity rightActivity = activityMap.get(rightActivityId);
				GeneralizedPrecedenceRelation gpr = new GeneralizedPrecedenceRelation(gprId, gprName, gprType, delta, leftActivity, rightActivity);
				gprPredecessors.add(gpr);
			}
			
			Set<GeneralizedPrecedenceRelation> gprSuccessors = new HashSet<GeneralizedPrecedenceRelation>();
			
			JsonNode gprSuccessorsArrayNode = activityNode.get("gprSuccessors");
			for (JsonNode gprSuccessorNode : gprSuccessorsArrayNode) {
				int gprId = (Integer) ((IntNode) gprSuccessorNode.get("gprId")).numberValue();
				String gprName = gprSuccessorNode.get("gprName").asText();
				GPRType gprType = GPRType.valueOf(gprSuccessorNode.get("gprType").asText());
				double delta = gprSuccessorNode.get("delta").asDouble();
//				double delta = (Double) ((DoubleNode) gprSuccessorNode.get("delta")).numberValue();
				int leftActivityId = (Integer) ((IntNode) gprSuccessorNode.get("leftActivity")).numberValue();
				int rightActivityId = (Integer) ((IntNode) gprSuccessorNode.get("rightActivity")).numberValue();
				Activity leftActivity = activityMap.get(leftActivityId);
				Activity rightActivity = activityMap.get(rightActivityId);
				GeneralizedPrecedenceRelation gpr = new GeneralizedPrecedenceRelation(gprId, gprName, gprType, delta, leftActivity, rightActivity);
				gprSuccessors.add(gpr);
			}
			
			Set<ActivityCompressionCost> compressionCosts = new HashSet<ActivityCompressionCost>();
			
			JsonNode compressionCostsArrayNode = activityNode.get("compressionCosts");
			for (JsonNode compressionCostNode : compressionCostsArrayNode) {
				int compressionCostId = (Integer) ((IntNode) compressionCostNode.get("compressionCostId")).numberValue();
//				double durationLB = (Double) ((DoubleNode) compressionCostNode.get("durationLB")).numberValue();
//				double durationUB = (Double) ((DoubleNode) compressionCostNode.get("durationUB")).numberValue();
//				double compressionCost = (Double) ((DoubleNode) compressionCostNode.get("compressionCost")).numberValue();
				double durationLB = compressionCostNode.get("durationLB").asDouble();
				double durationUB = compressionCostNode.get("durationUB").asDouble();
				double compressionCost = compressionCostNode.get("compressionCost").asDouble();
				ActivityCompressionCost cost = new ActivityCompressionCost(compressionCostId, durationLB, durationUB, compressionCost, activity);
				compressionCosts.add(cost);
			}
			
			activity.setDurationDistribution(durationDistribution);
			activity.setGprPredecessors(gprPredecessors);
			activity.setGprSuccessors(gprSuccessors);
			activity.setCompressionCosts(compressionCosts);
			
			activities.add(activity);

		}
		
		Project project = new Project(projectId, projectName, activities, null);
		
		for (Activity a : project.getActivities()) {
			
		}
 
		return project;
        
	}

	public String getProjectAsJSON() {
		return projectAsJSON;
	}

	public void setProjectAsJSON(String projectAsJSON) {
		this.projectAsJSON = projectAsJSON;
	}

}
