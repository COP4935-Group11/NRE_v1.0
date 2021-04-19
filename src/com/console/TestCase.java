package com.console;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.xml.XmlParser;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.configuration.RunConfiguration;
import com.configuration.RunConfiguration.OSType;
import com.constants.StringConstants;

import com.json.Feature;
import com.json.Parser;

public class TestCase {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	protected static final String DESCRIPTION = "description";
	protected static final String NAME = "name";
	protected static final String TAG = "tag";
	protected static final String COMMENT = "comment";
	protected static final String ID = "id";
	protected static final String MASKED = "masked";
	protected static final String TEST_CASE_GUID = "testCaseGuid";
	protected static final String TEST_CASE_VARIABLE = "variable";
	protected static final String VARIABLE_DEFAULT_VALUE = "defaultValue";
	
	public class Variable{
		
		String name;
		Object defaultValue;
	    String description;
	    String id;
	    Boolean masked;
	    
	}
	
	protected String description;
	protected String name;
	protected String tag;
	protected String comment;
	protected String testCaseGuid;
	protected List<Variable> caseScriptVariables;	
	protected String casePath;
	protected String caseScriptPath;
	protected ArrayList<Feature> cucumberFeatures;
	protected ArrayList<String> cucumberFiles;
	protected Boolean failed;
	protected Boolean stopped;
	protected boolean cucumber;
	
	
	public String getDescription() {return description;}
	public String getName() {return name;}
	public String getTag() {return tag;}
	public String getComment() {return comment;}
	public String getTestCaseGuid() {return testCaseGuid;}
	public List<Variable> getCaseScriptVariables() {return caseScriptVariables;}
	public String getCasePath() {return casePath;}
	public String getCaseScriptPath() {return caseScriptPath;}
	public ArrayList<Feature> getCucumberFeatures(){return cucumberFeatures;}
	public ArrayList<String> getCucumberFiles(){return cucumberFiles;}
	public Boolean hasCucumber() {return cucumber;}
	public Boolean isFailed() {return failed;}
	public Boolean isStopped() {return stopped;}
		
	public void setDescription(String description) {this.description = description;}
	public void setName(String name) {this.name = name;}
	public void setTag(String tag) {this.tag = tag;}
	public void setComment(String comment) {this.comment = comment;}
	public void setTestCaseGuid(String testCaseGuid) {this.testCaseGuid = testCaseGuid;}
	public void setCaseScriptVariables(List<Variable> caseScriptVariables) {this.caseScriptVariables = caseScriptVariables;}
	public void setCucumber() {cucumber = Boolean.TRUE;}
	public void setFailed(Boolean failed) {this.failed = failed;}
	public void setStopped(Boolean stopped) {this.stopped = stopped;}
	
	
	
	public void setCasePath(String path) {	
		
		String cpath = path.replace(RunConfiguration.getProjectDir()+StringConstants.ID_SEPARATOR, "").replace("Test Cases", "Scripts");
		
		if(RunConfiguration.getPlatform().equals(OSType.WINDOWS)) {
			cpath = cpath.replace("/", StringConstants.ID_SEPARATOR);
		}		
			
		this.casePath = "temp" + StringConstants.ID_SEPARATOR + cpath;
		//System.out.println(casePath);
	}
	
	public void setCaseScriptPath(long id) {	
				
		//System.out.println(casePath);
		String f = new File(this.casePath.replace(StringConstants.TEST_CASES_EXT, "").replace("Scripts", "Scripts"+Thread.currentThread().getId())).list()[0];
		this.caseScriptPath = this.casePath.replace(StringConstants.TEST_CASES_EXT, "").replace("Scripts", "Scripts"+Thread.currentThread().getId()) + StringConstants.ID_SEPARATOR + f;	
		//System.out.println(caseScriptPath);
	}

	public TestCase() {
		this.caseScriptVariables = new ArrayList<>();
		this.cucumber = Boolean.FALSE;
		this.failed = Boolean.FALSE;
		this.cucumberFeatures = new ArrayList<>();
		this.cucumberFiles = new ArrayList<>();
	}
	
	public TestCase(String path) {
		
		this();
		
		setCasePath(path);
		
		 //System.out.println(tcPath);
		try {
            Node rootNode = new XmlParser()
                    .parse(new File(path));
            
            this.description = String.valueOf(((NodeList)rootNode.get(DESCRIPTION)).text());
            this.name = String.valueOf(((NodeList)rootNode.get(NAME)).text());
            this.tag = String.valueOf(((NodeList)rootNode.get(TAG)).text());
            this.comment = String.valueOf(((NodeList)rootNode.get(COMMENT)).text());
            this.testCaseGuid = String.valueOf(((NodeList)rootNode.get(TEST_CASE_GUID)).text());
                                                          
            NodeList variableNodes = (NodeList) rootNode.get(TEST_CASE_VARIABLE);
            for (int index = 0; index < variableNodes.size(); index++) {
                Node globalVariableNode = (Node) variableNodes.get(index);
                Variable var =  new Variable();
                
               var.description = ((Node) ((NodeList) globalVariableNode.get(DESCRIPTION)).get(0)).text();
               var.name = ((Node) ((NodeList) globalVariableNode.get(NAME)).get(0)).text();
               var.id = ((Node) ((NodeList) globalVariableNode.get(ID)).get(0)).text();
               var.defaultValue = (Object)((Node) ((NodeList) globalVariableNode.get(VARIABLE_DEFAULT_VALUE)).get(0)).text();
               var.masked = Boolean.parseBoolean(((Node) ((NodeList) globalVariableNode.get(DESCRIPTION)).get(0)).text());
              
               this.caseScriptVariables.add(var);
            }            
                       
        } catch (Exception ex) {
            System.out.println("Could not get Script Variables.");
            LOGGER.severe(ex.getMessage());
        }  
		
	}	
	
	public void getScenarios()
	{
		for(String file : cucumberFiles)
		{
			Parser.startJson(file);
		}
		
		
	}

}

