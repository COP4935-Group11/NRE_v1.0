package com.console;

import com.configuration.RunConfiguration;
import com.constants.StringConstants;
import com.ucf.pcte.gold.FailureHandling;


public class Execute {
	
	private static final String SKIPPED_MESSAGE = "SKIPPED : Suite stopped on failure!";
	private static final String TEST_CASE_LABEL = "TEST CASE";
		
	private static FailureHandling failure = RunConfiguration.getFailureHandler();
	

	public static void run() throws Exception
	{		
		RunConfiguration.getTestSuiteObj().setFailed(RunScript.beforeSuite() == -1);
		RunConfiguration.getTestSuiteObj().setStopped(RunConfiguration.getTestSuiteObj().isFailed() && failure == FailureHandling.STOP_ON_FAILURE);
		
	
		for(TestCase tc : RunConfiguration.getTestSuiteObj().getTestCases()) {
				
			RunConfiguration.getTestSuiteObj().setTestCase(RunConfiguration.getTestSuiteObj().getSuiteCase().
					createNode(TEST_CASE_LABEL + StringConstants.COLON + tc.getName()).assignCategory(StringConstants.TEST_CASES_REPORT_TAG).
					assignAuthor(RunConfiguration.getTestSuiteObj().getProfile() + StringConstants.HYPHEN + 
							RunConfiguration.getTestSuiteObj().getBrowser()));
			RunConfiguration.getTestSuiteObj().setCurrentNode(RunConfiguration.getTestSuiteObj().getTestCase());
				if(RunConfiguration.getTestSuiteObj().isStopped())
					RunConfiguration.getTestSuiteObj().getTestCase().skip(SKIPPED_MESSAGE);
				else {
					RunConfiguration.getTestSuiteObj().setCurrentTestcase(tc);
					
					RunConfiguration.getTestSuiteObj().getCurrentTestCase().setFailed(RunScript.beforeTest() == -1);
					RunConfiguration.getTestSuiteObj().getCurrentTestCase().setStopped(RunConfiguration.getTestSuiteObj().getCurrentTestCase().isFailed() && failure == FailureHandling.STOP_ON_FAILURE);
					if(RunConfiguration.getTestSuiteObj().getCurrentTestCase().isStopped())
						RunConfiguration.getTestSuiteObj().setStopped(Boolean.TRUE);
					
					if(!RunConfiguration.getTestSuiteObj().getCurrentTestCase().isStopped()){	
						RunConfiguration.getTestSuiteObj().getCurrentTestCase().setFailed(RunScript.beforeCase() == -1);
						RunConfiguration.getTestSuiteObj().getCurrentTestCase().setStopped(RunConfiguration.getTestSuiteObj().getCurrentTestCase().isFailed() && failure == FailureHandling.STOP_ON_FAILURE);
						if(RunConfiguration.getTestSuiteObj().getCurrentTestCase().isStopped())
							RunConfiguration.getTestSuiteObj().setStopped(Boolean.TRUE);
						
						if(!RunConfiguration.getTestSuiteObj().getCurrentTestCase().isStopped()){	
							RunConfiguration.getTestSuiteObj().getCurrentTestCase().setFailed(RunScript.runScript() == -1);
							RunConfiguration.getTestSuiteObj().getCurrentTestCase().setStopped(RunConfiguration.getTestSuiteObj().getCurrentTestCase().isFailed() && failure == FailureHandling.STOP_ON_FAILURE);
							if(RunConfiguration.getTestSuiteObj().getCurrentTestCase().isStopped())
								RunConfiguration.getTestSuiteObj().setStopped(Boolean.TRUE);	
							
							if(!RunConfiguration.getTestSuiteObj().getCurrentTestCase().isStopped()){
								RunConfiguration.getTestSuiteObj().getCurrentTestCase().setFailed(RunScript.afterCase(0) == -1);
								RunConfiguration.getTestSuiteObj().getCurrentTestCase().setStopped(RunConfiguration.getTestSuiteObj().getCurrentTestCase().isFailed() && failure == FailureHandling.STOP_ON_FAILURE);
								if(RunConfiguration.getTestSuiteObj().getCurrentTestCase().isStopped())
									RunConfiguration.getTestSuiteObj().setStopped(Boolean.TRUE);	
								
								if(!RunConfiguration.getTestSuiteObj().getCurrentTestCase().isStopped()){
									RunConfiguration.getTestSuiteObj().getCurrentTestCase().setFailed(RunScript.afterTest(0) == -1);
									RunConfiguration.getTestSuiteObj().getCurrentTestCase().setStopped(RunConfiguration.getTestSuiteObj().getCurrentTestCase().isFailed() && failure == FailureHandling.STOP_ON_FAILURE);
																
								}else 
									RunScript.afterTest(-1);							
							}else {
									RunScript.afterCase(-1);
									RunScript.afterTest(-1);
							}
						}else { 
								RunScript.afterCase(-1);
								RunScript.afterTest(-1);
						}							
					}
					else 
						RunScript.afterTest(-1);				
				}	
				
			RunConfiguration.getExtent().flush();
		}
		
		RunConfiguration.getTestSuiteObj().setCurrentNode(RunConfiguration.getTestSuiteObj().getSuiteCase());
		
		if(!RunConfiguration.getTestSuiteObj().isStopped() && !RunConfiguration.getTestSuiteObj().isFailed())
			RunScript.afterSuite(0);				
		else
			RunScript.afterSuite(-1);
		
		RunConfiguration.getExtent().flush();
	}
	
}