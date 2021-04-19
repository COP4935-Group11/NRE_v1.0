package com.console;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.xml.XmlParser;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.configuration.RunConfiguration;
import com.constants.StringConstants;
import com.exception.StepFailedException;

public class TestSuiteCollection {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy_HHmmss");
	
	protected static final String SEQUENTIAL_KEYWORD = "SEQUENTIAL";
	protected static final String DESCRIPTION = "description";
	protected static final String NAME = "name";
	protected static final String TAG = "tag";
	protected static final String EXECUTION_MODE = "executionMode";
	protected static final String MAX_CONCURRENT_INSTANCES = "maxConcurrentInstances";
	protected static final String SUITE_RUN_CONFIGURATIONS = "testSuiteRunConfigurations";
	protected static final String SUITE_RUN_CONFIGURATION = "TestSuiteRunConfiguration";
	protected static final String CONFIGURATION = "configuration";
	protected static final String PROFILE_NAME = "profileName";
	protected static final String CONFIGURATION_ID = "runConfigurationId";
	protected static final String RUN_ENABLED = "runEnabled";
	protected static final String TEST_SUITE_ENTITY = "testSuiteEntity";
		
	protected String description;
	protected String name;
	protected String tag;
	protected int executionMode;//0->SEQUENTIAL; 1->PARALLEL
	protected int maxConcurrentInstances;
	protected LinkedHashMap<TestSuite, Boolean> tsCollection;
	protected File path;
	protected String reportFolder;
	protected Boolean isCollection;
	
	
	public String getDescription() {return description;}
	public String getName() {return name;}
	public String getTag() {return tag;}
	public int getExecutionMode() {return executionMode;}
	public LinkedHashMap<TestSuite, Boolean> getCollection() {return tsCollection;}
	public File getPath() {return path;}
	public String getReportFolder() {return reportFolder;}
	
	
	public void setDescription(String string) {description = string;}
	public void setName(String string) {name = string;}
	public void setTag(String string) {tag = string;}
	public void setTestSuites(LinkedHashMap<TestSuite, Boolean> testSuites) {tsCollection = testSuites;}
	public void setPath(File p) {path = p;}
	public void setReportFolder(String report) {reportFolder = report;}
	
	public Boolean isCollection() {	return isCollection;}
	
	public TestSuiteCollection() {
		
		this.description = null;
		this.tag = null;
		this.tsCollection = new LinkedHashMap<>();	
		this.isCollection = Boolean.TRUE;
		this.path = null;
		this.reportFolder = null;

   }
	
	public TestSuiteCollection(File path) {
		
		this();	
		this.path = path;
		
		Node rootNode = null;
		try {
			rootNode = new XmlParser()
			        .parse(path);
			
		} catch (IOException | SAXException | ParserConfigurationException e) {
			
			LOGGER.severe(e.getMessage());
		}
		try {
			NodeList list = (NodeList)rootNode.get(SUITE_RUN_CONFIGURATIONS);
			@SuppressWarnings("unused")	Node allConfigs = (Node) list.get(0);
		}catch(Exception ex){
			
			this.isCollection = Boolean.FALSE;			
		}

   }

	public void collectData()
	{
		// getAllInfo 		
		try
		{
			Node rootNode = new XmlParser()
                    .parse(path);	
			
			this.description = String.valueOf(((NodeList)rootNode.get(DESCRIPTION)).text());
			this.name = String.valueOf(((NodeList)rootNode.get(NAME)).text());
			this.tag = String.valueOf(((NodeList)rootNode.get(TAG)).text());		
			this.executionMode = String.valueOf(((NodeList)rootNode.get(EXECUTION_MODE)).text()).
					compareToIgnoreCase(SEQUENTIAL_KEYWORD)== 0 ? 0 : 1;	
			this.maxConcurrentInstances = Integer.valueOf(((NodeList)rootNode.get(MAX_CONCURRENT_INSTANCES)).text());	
			this.reportFolder = this.name.replace(' ', '_') + StringConstants.UNDERSCORE + java.time.LocalDateTime.now().format(formatter);;
			
			NodeList list = (NodeList)rootNode.get(SUITE_RUN_CONFIGURATIONS);
			 Node allConfigs = (Node) list.get(0);
			 NodeList list2 = (NodeList)allConfigs.get(SUITE_RUN_CONFIGURATION);
			for(int index = 0; index < list2.size(); index++) {
	             Node RunConfig = (Node) list2.get(index);
	             NodeList configList = (NodeList)RunConfig.get(CONFIGURATION);
	             String profile = configList.getAt(PROFILE_NAME).text();
	             String browser = configList.getAt(CONFIGURATION_ID).text();
;
	             
	             boolean isRun = Boolean.parseBoolean(((NodeList)RunConfig.get(RUN_ENABLED)).text());
	             String suitePath = String.valueOf(((NodeList)RunConfig.get(TEST_SUITE_ENTITY)).text());
	             suitePath = RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR + 
	            		 	 suitePath + StringConstants.TEST_SUITES_EXT; 
	             
	             
	             File profilePath = new File(RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR 
							+ StringConstants.PROFILE_FOLDER + StringConstants.ID_SEPARATOR 
							+ profile + StringConstants.PROFILE_EXT);
	             
	             if(profilePath.exists()) {
	             TestSuite ts = new TestSuite();
	             	ts.initSuite(suitePath, profile, browser);
	             	this.tsCollection.put(ts, isRun);
	             	
	             	//System.out.println(ts.getName()+"\t"+ts.getProfile()+"\t"+ts.getBrowser());
	             	//System.out.println(tsCollection.toString());
	             	
	             }
	             else
	            	 throw new StepFailedException("Profile: "+profile+" does not exist!");
    
	             
	         }					
		}catch (Exception e)
		{
			LOGGER.severe(e.getMessage());
			this.tsCollection = null;
			
		}		
		
	}

		
}

