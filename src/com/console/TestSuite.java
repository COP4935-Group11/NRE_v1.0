package com.console;

import groovy.lang.GroovyClassLoader;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.xml.XmlParser;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Set;
import java.util.logging.Logger;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import com.aventstack.extentreports.ExtentTest;
import com.configuration.RunConfiguration;
import com.constants.StringConstants;

public class TestSuite {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	protected static final String DESCRIPTION = "description";
	protected static final String NAME = "name";
	protected static final String TAG = "tag";
	protected static final String IS_RERUN = "isRerun";
	protected static final String MAIL_RECIPIENT = "mailRecipient";
	protected static final String NUMBER_OF_RERUN = "numberOfRerun";
	protected static final String PAGE_LOAD_TIMEOUT = "pageLoadTimeout";
	protected static final String PAGE_LOAD_TIMEOUT_DEFAULT = "pageLoadTimeoutDefault";
	protected static final String RERUN_FAILED_TESTCASES_ONLY = "rerunFailedTestCasesOnly";
	protected static final String TEST_SUITE_GUID = "testSuiteGuid";
	protected static final String TEST_CASE_LINK = "testCaseLink";
	protected static final String TEST_CASE_ID = "testCaseId";
	
	protected String description;
	protected String name;
	protected String tag;
	protected Boolean isReRun;
	protected String mailRecipient;
	protected int numberOfReRun;
	protected int pageLoadTimeOut;
	protected Boolean pageLoadTimeOutDefault;
	protected Boolean reRunTestCasesOnly;
	protected String testSuiteGuid;
	protected ArrayList<TestCase> TestCases;
	protected String profile;
	protected String browser;
	protected String tsPath;
	protected String reportFolder;
	protected GroovyClassLoader parentClassLoader;
	protected CompilerConfiguration configuration;
	protected ExtentTest suiteCase;
	protected ExtentTest testCase;
	protected ExtentTest currentNode;
	protected TestCase currentTestCase;
	protected Boolean failed;
	protected Boolean stopped;
	
	
	public String getDescription() {return description;}
	public String getName() {return name;}
	public String getTag() {return tag;}
	public Boolean getIsReRun() {return isReRun;}
	public String getMailRecipient() {return mailRecipient;}
	public int getNumberOfReRun() {return numberOfReRun;}
	public int getPageLoadTimeOut() {return pageLoadTimeOut;}
	public Boolean getPageLoadTimeOutDefault() {return pageLoadTimeOutDefault;}
	public Boolean getReRunTestCasesOnly() {return reRunTestCasesOnly;}
	public String getTestSuiteGuid() {return testSuiteGuid;}
	public ArrayList<TestCase> getTestCases() {return TestCases;}
	public GroovyClassLoader getParentClassLoader() {return parentClassLoader;}
	public CompilerConfiguration getCompilerConfiguration() {return configuration;}
	public String getProfile() {return profile;}
	public String getBrowser() {return browser;}
	public String getPath() {return tsPath;}
	public String getReportFolder() {return reportFolder;}
	public ExtentTest getSuiteCase() {return suiteCase;}
	public ExtentTest getTestCase() {return testCase;}
	public ExtentTest getCurrentNode() {return currentNode;}
	public TestCase getCurrentTestCase() {return currentTestCase;}
	public Boolean isFailed() {return failed;}
	public Boolean isStopped() {return stopped;}
	
	public void setDescription(String string) {description = string;}
	public void setName(String string) {name = string;}
	public void setTag(String string) {tag = string;}
	public void setIsReRun(Boolean bool) {isReRun = bool;}
	public void setMailRecipient(String string) {mailRecipient = string;}
	public void setNumberOfReRun(int i) {numberOfReRun = i;}
	public void setPageLoadTimeOut(int i) {pageLoadTimeOut = i;}
	public void setPageLoadTimeOutDefault(Boolean bool) {pageLoadTimeOutDefault = bool;}
	public void setReRunTestCasesOnly(Boolean bool) {	reRunTestCasesOnly = bool;}
	public void setTestSuiteGuid(String string) {testSuiteGuid = string;}
	public void setTestCases(ArrayList<TestCase> testCases) {TestCases = testCases;}
	public void setParentClassLoader(GroovyClassLoader gcl) {parentClassLoader = gcl;}
	public void setCompilerConfiguration(CompilerConfiguration cc) {configuration = cc;}
	public void setProfile(String pr) {profile = pr;}
	public void setBrowser(String br) {browser = br;}	
	public void setTsPath(String path) {tsPath = path;}	
	public void setReportFolder(String report) {reportFolder = report;}
	public void setSuiteCase(ExtentTest suiteCase) {this.suiteCase = suiteCase;}	
	public void setTestCase(ExtentTest testCase) {this.testCase = testCase;}
	public void setCurrentNode(ExtentTest currentNode) {this.currentNode = currentNode;}
	public void setCurrentTestcase(TestCase currentTestCase) {this.currentTestCase = currentTestCase;}
	public void setFailed(Boolean failed) {this.failed = failed;}
	public void setStopped(Boolean stopped) {this.stopped = stopped;}
	
	public TestSuite() {
		
		this.description = null;
		this.tag = null;
		this.isReRun = false;
		this.mailRecipient = null;
		this.numberOfReRun = 0;
		this.pageLoadTimeOut = 0;
		this.pageLoadTimeOutDefault = false;
		this.reRunTestCasesOnly = false;
		this.testSuiteGuid = null;
		this.TestCases = new ArrayList<>();
		this.configuration = new CompilerConfiguration();
		this.configuration.setClasspath(StringConstants.ROOT_DIR);
		this.configuration.setTargetDirectory(StringConstants.BIN_FOLDER);
		this.parentClassLoader = new GroovyClassLoader(TestSuite.class.getClassLoader(),this.configuration);
		this.profile = null;
		this.browser = null;
		this.reportFolder = null;
		this.failed = Boolean.FALSE;
		this.stopped = Boolean.FALSE;

   }

	public TestCase getTestCase(String testCaseName){
		
		for(int i = 0; i < this.TestCases.size(); i++)		
			if(this.TestCases.get(i).getCaseScriptPath().equalsIgnoreCase(testCaseName))
				return this.TestCases.get(i);
		
		return null;		
	}
	
	public void initSuite(String suite, String profile, String browser) {
			
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy_HHmmss");
		
		setTsPath(suite);
		
		try
		{
			File suiteFile= new File(suite);
			Node rootNode = new XmlParser()
                    .parse(suiteFile);	
			
			this.description = String.valueOf(((NodeList)rootNode.get(DESCRIPTION)).text());
			this.name = String.valueOf(((NodeList)rootNode.get(NAME)).text());
			this.tag = String.valueOf(((NodeList)rootNode.get(TAG)).text());
			this.isReRun = Boolean.parseBoolean(((NodeList)rootNode.get(IS_RERUN)).text());
			this.mailRecipient = String.valueOf(((NodeList)rootNode.get(MAIL_RECIPIENT)).text());
			this.numberOfReRun = Integer.valueOf(((NodeList)rootNode.get(NUMBER_OF_RERUN)).text());
			this.pageLoadTimeOut = Integer.valueOf(((NodeList)rootNode.get(PAGE_LOAD_TIMEOUT)).text());
			this.pageLoadTimeOutDefault = Boolean.parseBoolean(((NodeList)rootNode.get(PAGE_LOAD_TIMEOUT_DEFAULT)).text());
			this.reRunTestCasesOnly = Boolean.parseBoolean(((NodeList)rootNode.get(RERUN_FAILED_TESTCASES_ONLY)).text());
			this.profile = profile;
			this.browser = browser;
			this.reportFolder = this.name.replace(' ', '_') + StringConstants.UNDERSCORE + java.time.LocalDateTime.now().format(formatter);
			
			
			
			NodeList list = (NodeList)rootNode.get(TEST_CASE_LINK);
			for (int index = 0; index < list.size(); index++) {
	             Node tc = (Node) list.get(index);
	             String testcase = ((Node) ((NodeList) tc.get(TEST_CASE_ID)).get(0)).text();
	             String tcPath = RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR
							+ testcase + StringConstants.TEST_CASES_EXT;
	             TestCase test = new TestCase(tcPath);
	             this.TestCases.add(test);
	         }					
		}catch (Exception e)
		{
			LOGGER.severe(e.getMessage());
		}		
	}	
	
	public void addClass(File f) throws CompilationFailedException, IOException, ClassNotFoundException {
		
		//System.out.println(f.getName());
		this.parentClassLoader.loadClass(this.parentClassLoader.parseClass(f).getName());

	}
	
	/*public Class<?> addReturnClass(File f) throws CompilationFailedException, IOException, ClassNotFoundException {
		Class<?> clazz = parentClassLoader.parseClass(f);
		parentClassLoader.loadClass(clazz.getName());
		
		return clazz;		
	}*/

}

