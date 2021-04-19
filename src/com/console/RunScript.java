package com.console;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.annotation.SetUp;
import com.annotation.SetupTestCase;
import com.annotation.TearDown;
import com.annotation.TearDownTestCase;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.configuration.RunConfiguration;
import com.console.TestCase.Variable;
import com.constants.StringConstants;
import com.json.Feature;
import com.json.Scenario;
import com.json.Step;
import com.scripts.FeaturesFactory;
import com.ucf.pcte.gold.FailureHandling;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;


public class RunScript {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	protected static final String FEATURE_LABEL = "FEATURE";
	protected static final String SCENARIO_LABEL = "SCENARIO";
	protected static final String STEP_SKIPPED_MESSAGE = "SKIPPED : A previous step has failed!";
	protected static final String SKIPPED_MESSAGE = "SKIPPED : Suite stopped on failure!";
	
	protected static Map<String, ArrayList<String>> tagsMap = FeaturesFactory.getTags();
	protected static GroovyClassLoader gcl = null;
	protected static ThreadLocal<File> suiteScript = new ThreadLocal<File>() {
		  @Override
	        protected File initialValue() {
	            return new File((StringConstants.SUITE_FOLDER + Thread.currentThread().getId() + (RunConfiguration.getTestSuiteObj().getPath().
						substring(RunConfiguration.getTestSuiteObj().getPath().lastIndexOf(StringConstants.ID_SEPARATOR)))).
						replace(StringConstants.TEST_SUITES_EXT, StringConstants.GROOVY_EXT));
	        }
	};
	
    public static int beforeSuite() {
	 	 	 
    	int fail = 0;
		
		gcl = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
		Boolean skipped = true;		
	 	
		try {
	
					
			Class<?> clazz = gcl.parseClass(suiteScript.get());
			GroovyShell groovyShell = new GroovyShell(gcl);
			
			Method m = clazz.getMethod("setUp", (Class<?>[]) null);
			
			if(m.getAnnotation(SetUp.class) != null) {
				skipped = m.getAnnotation(SetUp.class).skipped();
				
			//System.out.println("skipped = "+ skipped);	//debugging mode			
	 
				if(!skipped) { 
					//System.out.println("TestSuite SetUp->");
					RunConfiguration.getTestSuiteObj().setCurrentNode(RunConfiguration.getTestSuiteObj().getSuiteCase().createNode("Suite setup").
							assignCategory(StringConstants.HOOKS_REPORT_TAG));
					try {
						groovyShell.parse(suiteScript.get()).invokeMethod("setUp", null);
						RunConfiguration.getTestSuiteObj().getCurrentNode().pass(StringConstants.PASS_LOG);
					}catch(Exception e) { 
						LOGGER.severe(e.getMessage());
						RunConfiguration.getTestSuiteObj().getCurrentNode().fail(e.getLocalizedMessage());
						fail = -1;
					}					
				}
			}

		}catch(Exception e) { 
			LOGGER.severe(e.getMessage());
			fail = -1;
			//Logger.failTestSuite(e.toString());
		}
		   
		RunConfiguration.getExtent().flush();
		return fail;
    }
	

	 public static void afterSuite(int control) {
		 	 	
		 Boolean skipped = true;		

		 try {

			 Class<?> clazz = gcl.parseClass(suiteScript.get());
				GroovyShell groovyShell = new GroovyShell(gcl);
			 
			 Method m = clazz.getMethod("tearDown", (Class<?>[]) null);
			
			 if(m.getAnnotation(TearDown.class) != null) {
				 skipped = m.getAnnotation(TearDown.class).skipped();
		
				 //System.out.println("skipped = "+ skipped);	//debugging mode			

				 if(!skipped) {
					 //System.out.println("TestSuite TearDown->");
					 RunConfiguration.getTestSuiteObj().setCurrentNode(RunConfiguration.getTestSuiteObj()
							 .getSuiteCase().createNode("Suite TearDown").assignCategory("Hooks"));
					 

						if(control == -1) {
							RunConfiguration.getTestSuiteObj().getCurrentNode().skip(SKIPPED_MESSAGE);
							
						}else {
							
							try {
								groovyShell.parse(suiteScript.get()).invokeMethod("tearDown", null);
								RunConfiguration.getTestSuiteObj().getCurrentNode().pass("PASSED");
							}catch(Exception e) { 
									LOGGER.severe(e.getMessage());
									RunConfiguration.getTestSuiteObj().getCurrentNode().fail(e.getLocalizedMessage());
									}
					 
							RunConfiguration.getExtent().flush();
						}
				 }
				 RunConfiguration.getTestSuiteObj().getParentClassLoader().clearCache();
			 }

		 }catch(Exception e) { // do nothing
			 LOGGER.severe(e.getMessage());
			//fail = -1;
		 }

	}


	public static int beforeTest() {
		
		int fail = 0;
		Boolean skipped = true;		

		try {

			Class<?> clazz = gcl.parseClass(suiteScript.get());
			GroovyShell groovyShell = new GroovyShell(gcl);
			
			Method m = clazz.getMethod("setupTestCase", (Class<?>[])  null);
			
			if(m.getAnnotation(SetupTestCase.class) != null) {
				skipped = m.getAnnotation(SetupTestCase.class).skipped();
	
			 //System.out.println("skipped = "+ skipped);	//debugging mode			

				if(!skipped) {
					//System.out.println("setupTestCase->");
					RunConfiguration.getTestSuiteObj().setCurrentNode(RunConfiguration.getTestSuiteObj()
							.getTestCase().createNode("TestCase SetUp").assignCategory("Hooks"));
					
					try {
						groovyShell.parse(suiteScript.get()).invokeMethod("setupTestCase", null);
						RunConfiguration.getTestSuiteObj().getCurrentNode().pass("PASSED");
					}
					catch(Exception e)	{ 
						LOGGER.severe(e.getMessage());
						RunConfiguration.getTestSuiteObj().getCurrentNode().fail(e.getLocalizedMessage());
						fail = -1;
					}
					
					RunConfiguration.getExtent().flush();
				}
		 }

		}catch(Exception e)	{ // do nothing 
			LOGGER.severe(e.getMessage());
		}
		
		
		return fail;
	}
	 				

	public static int afterTest(int control) {
	
		int fail = 0;
		Boolean skipped = true;		

		try {

			Class<?> clazz = gcl.parseClass(suiteScript.get());
			GroovyShell groovyShell = new GroovyShell(gcl);
			
			Method m = clazz.getMethod("tearDownTestCase", (Class<?>[])  null);
			if(m.getAnnotation(TearDownTestCase.class) != null) {
				skipped = m.getAnnotation(TearDownTestCase.class).skipped();
	
			 //System.out.println("skipped = "+ skipped);	//debugging mode			

				if(!skipped) {
					//System.out.println("tearDownTestCase->");
					RunConfiguration.getTestSuiteObj().setCurrentNode(RunConfiguration.getTestSuiteObj()
							.getTestCase().createNode("TestCase TearDown").assignCategory(StringConstants.HOOKS_REPORT_TAG));
					
					if(control == -1) {
						RunConfiguration.getTestSuiteObj().getCurrentNode().skip(STEP_SKIPPED_MESSAGE);
						
					}else {
					
						try {
							groovyShell.parse(suiteScript.get()).invokeMethod("tearDownTestCase", null);
							RunConfiguration.getTestSuiteObj().getCurrentNode().pass(StringConstants.PASS_LOG);
						}catch(Exception e){ 
							LOGGER.severe(e.getMessage());
							RunConfiguration.getTestSuiteObj().getCurrentNode().fail(e.getLocalizedMessage());
							fail = -1;
						}
					
						RunConfiguration.getExtent().flush();
				
					}
				}
			}
		}catch(Exception e){
			
			// do nothing
			LOGGER.severe(e.getMessage());
		}
		
		
		return fail;
	}
		
	public static int beforeCase()
	{
		int fail = 0;
		File script = new File(RunConfiguration.getTestSuiteObj().getCurrentTestCase().getCaseScriptPath());
		
		GroovyShell groovyShell = new GroovyShell(gcl);
		Boolean setupSkipped = true;	

		List<Variable> Variables = RunConfiguration.getTestSuiteObj().getCurrentTestCase().getCaseScriptVariables();
		
		for(Variable var : Variables) 
			groovyShell.setProperty(var.name, var.defaultValue);
		
		
		Class<?> clazz = null;
						
		try {
			
				clazz = gcl.parseClass(script);
				
				try {
					Method m = clazz.getMethod("setup", (Class<?>[]) null);
					
					if(m.getAnnotation(SetUp.class) != null) {
							setupSkipped = m.getAnnotation(SetUp.class).skipped();
				
						 //System.out.println("skipped = "+ skipped);	//debugging mode			
	
							if(!setupSkipped) { 
								//System.out.println("setup->");
								RunConfiguration.getTestSuiteObj().setCurrentNode(RunConfiguration.getTestSuiteObj()
										.getTestCase().createNode("setup()").assignCategory(StringConstants.HOOKS_REPORT_TAG));
								
								try {
									groovyShell.parse(script).invokeMethod("setup", null);
									RunConfiguration.getTestSuiteObj().getCurrentNode().pass(StringConstants.PASS_LOG);
								}catch(Exception e){ 
									LOGGER.severe(e.getMessage());
									RunConfiguration.getTestSuiteObj().getCurrentNode().fail(e.getLocalizedMessage());
									fail = -1;
								}
								
								RunConfiguration.getExtent().flush();
							}
					}
				}catch(Exception e)
				{
					// do nothing
				}
		}catch(Exception e)
		{
			// do nothinng?
			LOGGER.severe(e.getMessage());
		}
		
		
		return fail;		
	}
	
	public static int afterCase(int control)
	{
		int fail = 0;
		File script = new File(RunConfiguration.getTestSuiteObj().getCurrentTestCase().getCaseScriptPath());
		
		GroovyShell groovyShell = new GroovyShell(gcl);
		Boolean teardownSkipped = true;
		
		List<Variable> Variables = RunConfiguration.getTestSuiteObj().getCurrentTestCase().getCaseScriptVariables();
		
		for(Variable var : Variables) 
			groovyShell.setProperty(var.name, var.defaultValue);
		
		Class<?> clazz = null;
		try {
			clazz = gcl.parseClass(script);

			try {
				Method m = clazz.getMethod("teardown", (Class<?>[]) null);
				
				if(m.getAnnotation(TearDown.class) != null) {
						teardownSkipped = m.getAnnotation(TearDown.class).skipped();
			
					 //System.out.println("skipped = "+ skipped);	//debugging mode			
	
						if(!teardownSkipped) { 
							//System.out.println("teardown->");
							RunConfiguration.getTestSuiteObj().setCurrentNode(RunConfiguration.getTestSuiteObj()
									.getTestCase().createNode("teardown()").assignCategory(StringConstants.HOOKS_REPORT_TAG));
							
							if(control == -1) {
								RunConfiguration.getTestSuiteObj().getCurrentNode().skip(STEP_SKIPPED_MESSAGE);
								
							}else {
															
								try {
									groovyShell.parse(script).invokeMethod("teardown", null);
									RunConfiguration.getTestSuiteObj().getCurrentNode().pass(StringConstants.PASS_LOG);
								}catch(Exception e) {
									LOGGER.severe(e.getMessage());
									RunConfiguration.getTestSuiteObj().getCurrentNode().fail(e.getLocalizedMessage());
									fail = -1;
								}
							
								RunConfiguration.getExtent().flush();
							}
						}
					}
				}catch(Exception e)	{ 
					// do nothing: we good teardown just isnt present!	
				}
		}catch(Exception e)
		{
			//do nothing?
		}
		
		
		return fail;
	}
	
	public static int runScript()
	{
		int fail = 0;
		File script = new File(RunConfiguration.getTestSuiteObj().getCurrentTestCase().getCaseScriptPath());
		
		GroovyShell groovyShell = new GroovyShell(gcl);

		List<Variable> Variables = RunConfiguration.getTestSuiteObj().getCurrentTestCase().getCaseScriptVariables();
		
		for(Variable var : Variables) 
			groovyShell.setProperty(var.name, var.defaultValue);
		
			
		RunConfiguration.getTestSuiteObj().setCurrentNode(RunConfiguration.getTestSuiteObj().getTestCase());
			try
			{
				
				groovyShell.evaluate(script);
				RunConfiguration.getExtent().flush();
				
			}catch(Exception e)
			{
				LOGGER.severe(e.getMessage());
				fail = -1;
			}
				
				if(RunConfiguration.getTestSuiteObj().getCurrentTestCase().hasCucumber()) {
					try {
						RunConfiguration.getExtent().setGherkinDialect(StringConstants.GHERKIN_LANGUAGE);
					
										
					ExtentTest cucumberNode = RunConfiguration.getTestSuiteObj().getCurrentNode();
				
					RunConfiguration.getTestSuiteObj().getCurrentTestCase().getScenarios();
				
					for(Feature feature : RunConfiguration.getTestSuiteObj().getCurrentTestCase().getCucumberFeatures())
					{
						ExtentTest featureNode = cucumberNode.createNode(new GherkinKeyword(FEATURE_LABEL),FEATURE_LABEL + 
								StringConstants.COLON + feature.getName()).assignCategory(StringConstants.FEATURES_REPORT_TAG);
						tagsMap.get(feature.getName()).forEach((tag) -> featureNode.assignCategory(tag));
						for(Scenario scenario : feature.getScenarios())
						{
							ExtentTest scenarioNode = featureNode.createNode(new GherkinKeyword(SCENARIO_LABEL), SCENARIO_LABEL +
									StringConstants.COLON + scenario.getName()).assignCategory(StringConstants.SCENARIOS_REPORT_TAG);
							tagsMap.get(feature.getName()).forEach((tag) -> scenarioNode.assignCategory(tag));
							for(Step steps : scenario.getSteps())
							{
								ExtentTest stepNode;
								if(steps.getResult().equals("passed"))
								{
									stepNode  = scenarioNode.createNode(new GherkinKeyword(steps.getType()), steps.getType() + StringConstants.COLON + steps.getName()).
												assignCategory(StringConstants.STEPS_REPORT_TAG).pass(StringConstants.PASS_LOG);
									tagsMap.get(feature.getName()).forEach((tag) -> stepNode.assignCategory(tag));
									
								}else if(steps.getResult().equals("failed"))
								{
										if(RunConfiguration.getFailureHandler().equals(FailureHandling.OPTIONAL)) {
											stepNode = scenarioNode.createNode(new GherkinKeyword(steps.getType()), steps.getType() + StringConstants.COLON + steps.getName()).
												assignCategory(StringConstants.STEPS_REPORT_TAG).warning(steps.getFailure());
											tagsMap.get(feature.getName()).forEach((tag) -> stepNode.assignCategory(tag));
										}else {
											stepNode = scenarioNode.createNode(new GherkinKeyword(steps.getType()), steps.getType() + StringConstants.COLON + steps.getName()).
													assignCategory(StringConstants.STEPS_REPORT_TAG).fail(steps.getFailure());
											tagsMap.get(feature.getName()).forEach((tag) -> stepNode.assignCategory(tag));
										}
										fail = -1;
								}	
								else {
									scenarioNode.createNode(new GherkinKeyword(steps.getType()), steps.getType() + StringConstants.COLON + steps.getName()).
									assignCategory(StringConstants.STEPS_REPORT_TAG).skip(STEP_SKIPPED_MESSAGE);
								}
							}						
						}
					}
				} catch (UnsupportedEncodingException | ClassNotFoundException e1) {
					
					LOGGER.severe(e1.getMessage());
				};
					
			}					
				RunConfiguration.getExtent().flush();

		return fail;
	}		
	
}