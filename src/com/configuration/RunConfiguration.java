package com.configuration;

import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.console.TestSuite;
import com.console.TestSuiteCollection;
import com.constants.StringConstants;
import com.ucf.pcte.gold.FailureHandling;

public class RunConfiguration {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
	
	public enum OSType{
	
		WINDOWS,
		LINUX,
			
	}	
	
	private static OSType platform;
	private static String projectDir;
	private static String testSuite;
	private static String profile;
	private static String browser;
	private static String reportDir;
	private static TestSuiteCollection tsCollection;
	private static ExtentReports extent;
	private static ExtentSparkReporter spark;
	private static FailureHandling defaultFailure; 
	private static String reportFile;
	private static String reportRoot;
	
	private static ThreadLocal<TestSuite> testSuiteObj = new ThreadLocal<TestSuite>() {
        @Override
        protected TestSuite initialValue() {
            return new TestSuite();
        }
    };

    
		
	public static OSType getPlatform() {return platform;}
	public static TestSuite getTestSuiteObj() {return testSuiteObj.get();}
	public static String getProfile() {return profile;}
	public static String getTestSuite() {return testSuite;}
	public static String getBrowser() {return browser;}
	public static String getProjectDir() {return projectDir;}
	public static String getReportDir() {return reportDir;}
	public static String getReportFile() {return reportFile;}
	public static String getReportRoot() {return reportRoot;}
	public static FailureHandling getFailureHandler() {return defaultFailure;}
	public static ExtentReports getExtent() {return extent;}
	public static ExtentSparkReporter getSpark() {return spark;}
	public static TestSuiteCollection getTsCollection() {return tsCollection;}
	
	public static void setPlatform(String platform) {
		
		if(platform.toLowerCase().contains("linux")) {
					
		 RunConfiguration.platform = OSType.LINUX;
		}
		else if(platform.toLowerCase().contains("windows")) {
		 RunConfiguration.platform = OSType.WINDOWS;
		}
		else {
			RunConfiguration.platform = OSType.LINUX;
		}
	}
		
	public static void setTestSuiteObj(TestSuite suite) {testSuiteObj.set(suite);}
	public static void setProjectDir(String projectDir) {RunConfiguration.projectDir = projectDir;}	
	public static void setTestSuite(String testSuite) {RunConfiguration.testSuite = testSuite;}
	public static void setProfile(String profile) {RunConfiguration.profile = profile;}
	public static void setBrowser(String browser) {RunConfiguration.browser = browser;}
	public static void setReportDir(String reportDir) {RunConfiguration.reportDir = reportDir;}
	public static void setReportFile(String reportFile) {RunConfiguration.reportFile = reportFile;}
	public static void setExtent(ExtentReports extent) {RunConfiguration.extent = extent;}
	public static void setSpark(ExtentSparkReporter spark) {RunConfiguration.spark = spark;}
	public static void setTsCollection(TestSuiteCollection tsCollection) {RunConfiguration.tsCollection = tsCollection;}
	public static void setReportRoot() {
		RunConfiguration.reportRoot = reportDir + StringConstants.ID_SEPARATOR + java.time.LocalDate.now().format(formatter);
		LOGGER.info("reportRoot set to :" + reportDir + StringConstants.ID_SEPARATOR + java.time.LocalDate.now().format(formatter));
		
	}		
	public static void setFailureHandling(String failure) {
		
		String keyword = null;
			
		if(failure != null) {
			if(failure.toUpperCase().contains("CONTINUE"))
				keyword = "CONTINUE_ON_FAILURE";
			else if(failure.toUpperCase().contains("STOP"))
				keyword = "STOP_ON_FAILURE";
			else
				keyword = "OPTIONAL";
		}else
			keyword = "EMPTY";

		switch(keyword) {
		
		case "CONTINUE_ON_FAILURE" 	: 	defaultFailure = FailureHandling.CONTINUE_ON_FAILURE;
										break;
		case "OPTIONAL" 			: 	defaultFailure = FailureHandling.OPTIONAL;
										break;
		default						:	defaultFailure = FailureHandling.STOP_ON_FAILURE;
										break;		
		}		
		
		LOGGER.info("defaultFailureHandling set to :" + defaultFailure.toString());
		
	}
	
	public static void resetConfigurationData() {
		
		platform = null;
		projectDir = null;
		testSuite = null;
		profile = null;
		browser = null;
		reportDir = null;
		tsCollection = null;
		extent = null;
		spark = null;
		defaultFailure = null; 
		reportFile = null;
		reportRoot = null;
		testSuiteObj.set(null);		
	}
}