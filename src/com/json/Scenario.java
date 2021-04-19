package com.json;

import java.util.ArrayList;

public class Scenario {
	
	protected String name;
	protected ArrayList<Step> steps = new ArrayList<>();
	
	public void setName(String name) {this.name = name;}
	public String getName() {return this.name;}
	
	public void addStep(Step scene)
	{
		steps.add(scene);
	}
	
	public ArrayList<Step> getSteps()
	{
		return steps;
	}

}
