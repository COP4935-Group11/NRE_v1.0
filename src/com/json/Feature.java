package com.json;

import java.util.ArrayList;

public class Feature {
	
	protected String name;
	protected ArrayList<Scenario> scenarios = new ArrayList<>();
	
	public void setName(String name) {this.name = name;}
	public String getName() {return this.name;}
	
	public void addScenario(Scenario scene)
	{
		scenarios.add(scene);
	}
	
	public ArrayList<Scenario> getScenarios()
	{
		return scenarios;
	}
	

}
