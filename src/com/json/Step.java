package com.json;

public class Step {
	protected String type;
	protected String name;
	protected String result;
	protected String failure;
		
	public String getType() {return type;}
	public String getName() {return name;}
	public String getResult() {return result;}
	public String getFailure() {return failure;}
	
	public void setType(String type) {this.type = type;}	
	public void setName(String name) {this.name = name;}
	public void setResult(String result) {this.result = result;}
	public void setFailure(String failure) {this.failure = failure;}
}
