package com.console;

import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.configuration.RunConfiguration;
import com.constants.StringConstants;
import com.driver.DriverFactory;
import com.scripts.*;
import com.util.DirectoryUtils;


public class RunConsole {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	protected static String SUITE_LABEL = "SUITE";
	protected static String[] paths = { StringConstants.ROOT_DIR + StringConstants.COMPILED_STEPS_FOLDER,
			StringConstants.ROOT_DIR + StringConstants.BIN_FOLDER};
	protected static Boolean isCollection;
	protected static Boolean loading;
	protected static Thread loadingThread;
	protected static long startLoadingTime;
	protected static ThreadLocal<Map<String,Object>> GlobalVariables = new ThreadLocal<Map<String, Object>>(){
		@Override
        protected Map<String,Object> initialValue() {
            return new HashMap<String,Object>();
        }
	};			

	
	public static Map<String,Object> getGlobalVariables(){return GlobalVariables.get();}
	
	public static void run() {
		
		loading = Boolean.FALSE;
		isCollection = Boolean.FALSE;
		
		try {
			startloading(StringConstants.PARSING_PHASE_MESSAGE);
							
		} catch (IOException | InterruptedException e) {
			LOGGER.severe(e.getMessage());
		}
		
		RunConfiguration.setReportRoot();
		
		File path = new File(RunConfiguration.getTestSuite());		
		
		RunConfiguration.setTsCollection(new TestSuiteCollection(path));
		
				
		if(RunConfiguration.getTsCollection().isCollection()) {
			isCollection = Boolean.TRUE;
			RunConfiguration.getTsCollection().collectData();
			
			//System.out.println(RunConfiguration.tsCollection.getCollection().toString());
			
			runCollection(RunConfiguration.getTsCollection().getCollection());
		
		}else {
		
		TestSuite testSuite = new TestSuite();		
		testSuite.initSuite(path.getPath(), RunConfiguration.getProfile(), RunConfiguration.getBrowser());
		RunConfiguration.setTestSuiteObj(testSuite);
		
		RunConfiguration.setExtent(new ExtentReports());
		RunConfiguration.getExtent().setAnalysisStrategy(AnalysisStrategy.SUITE);
		RunConfiguration.setReportFile(RunConfiguration.getReportRoot() + StringConstants.ID_SEPARATOR + 
				StringConstants.ID_SEPARATOR + testSuite.getReportFolder() + StringConstants.ID_SEPARATOR + 
				testSuite.getReportFolder() + StringConstants.HTML_EXT);
		RunConfiguration.setSpark(new ExtentSparkReporter(RunConfiguration.getReportFile()));
				
		File file = new File(StringConstants.EXTENT_XML_CONFIG_FILE);
		try {
			RunConfiguration.getSpark().loadXMLConfig(file);
		} catch (IOException e) {

			LOGGER.severe(e.getMessage());
		}
		
		RunConfiguration.getExtent().attachReporter(RunConfiguration.getSpark());
		RunConfiguration.getExtent().setSystemInfo(StringConstants.OS, RunConfiguration.getPlatform().toString());
		RunConfiguration.getTestSuiteObj().suiteCase = RunConfiguration.getExtent().
				createTest(SUITE_LABEL + StringConstants.COLON + RunConfiguration.getTestSuiteObj().getName()).
				assignCategory(StringConstants.TEST_SUITES_REPORT_TAG).assignAuthor(RunConfiguration.getTestSuiteObj().
						getProfile() + StringConstants.HYPHEN + RunConfiguration.getTestSuiteObj().getBrowser());
		RunConfiguration.getExtent().flush();
				
		stopLoading();
		console(testSuite);
		
		cleanProject();	
		
	}
	}
			
	@SuppressWarnings({ "unused"})
	protected static void console(TestSuite ts) {
			
			//System.out.println(ts.getName());
								
		try {
			
			long start, finish, absStart, absFinish;
			
			if(isCollection && RunConfiguration.getTsCollection().getExecutionMode() == 1) {
				RunConfiguration.setTestSuiteObj(ts);
				RunConfiguration.getTestSuiteObj().suiteCase = RunConfiguration.getExtent().
						createTest(SUITE_LABEL + StringConstants.COLON + RunConfiguration.getTestSuiteObj().getName()).
						assignCategory(StringConstants.TEST_SUITES_REPORT_TAG).assignAuthor(RunConfiguration.getTestSuiteObj().
								getProfile() + StringConstants.HYPHEN +	RunConfiguration.getTestSuiteObj().getBrowser());

			}
			
			if(!isCollection || RunConfiguration.getTsCollection().getExecutionMode() == 0) {
											
				try {
					startloading(StringConstants.SETTING_SUITE_MESSAGE + StringConstants.COLON + ts.getName());								
				}catch (IOException | InterruptedException e) {

					LOGGER.severe(e.getMessage());
				}
			}
			
			if(!isCollection) {
				
				start = System.currentTimeMillis();
				FeaturesFactory.copyFeatures();
				finish = System.currentTimeMillis();
				//System.out.println("FeaturesFactory took "+(finish-start) +"ms");
			
				start = System.currentTimeMillis();
				StepDefinitionsFactory.copyScripts();
				finish = System.currentTimeMillis();
				//System.out.println("StepDefinitionsFactory took "+(finish-start) +"ms");
			}			
			
			absStart = System.currentTimeMillis();
			GlobalVariables.set(GlobalVars.getGlobalVars(ts.getProfile()));
			//GlobalVariables.get().forEach((key, value) -> System.out.println(key + ":" + value + "class:" + value.getClass().getName()));
			start = System.currentTimeMillis();
			GlobalVariableFactory.createGlobalVarFile(GlobalVariables.get());			
			finish = System.currentTimeMillis();
			//System.out.println("GlobalVarFactory took "+(finish-start) +"ms");
					
			//KeywordsFactory.createKeywordFiles();
											
			start = System.currentTimeMillis();
			StepDefinitionsFactory.compileScripts();
			finish = System.currentTimeMillis();
			//System.out.println("StepDefinitionsFactory took "+(finish-start) +"ms");
				
			start = System.currentTimeMillis();
			TestSuiteFactory.copySuite();
			finish = System.currentTimeMillis();
	
			start = System.currentTimeMillis();
			TestCaseFactory.copyTestCaseScripts();
			finish = System.currentTimeMillis();
			//System.out.println("TestCaseFactory took "+(finish-start) +"ms");
			
			
			absFinish= System.currentTimeMillis();
			//System.out.println("Parsing process took "+(absFinish-absStart) +"ms");
				
			setupContextClassLoader(paths ,RunConfiguration.getTestSuiteObj().getParentClassLoader());
			
			
			if(loading) 				
				stopLoading();
			
			
			Execute.run();
			
			DriverFactory.cleanAllDrivers();
			
			if(isCollection)
				cleanSuite();	
			
			
		} catch (Exception e) {			
			LOGGER.severe(e.getMessage());
		}		
	}	
	
	protected static void cleanProject() {
			
		try {
			System.out.println(StringConstants.ANSI_RESET);
			progressiveMessage("Deleting Temporary Files:");
		
			if(!isCollection) {
				progressiveMessage("\n\t./"+StringConstants.TEMP_FOLDER);
				try {
					DirectoryUtils.deleteDirectory(StringConstants.TEMP_FOLDER);
					System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
										+StringConstants.ANSI_RESET);
				} catch (IOException e) {
					System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" +
							StringConstants.ANSI_RESET);
					LOGGER.severe(e.getMessage());
				}
				
				progressiveMessage("\n\t./"+StringConstants.CUCUMBER_DIR);
				try {
					DirectoryUtils.deleteDirectory(StringConstants.CUCUMBER_DIR);
					System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
										+StringConstants.ANSI_RESET);
				} catch (IOException e) {
					System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" +
							StringConstants.ANSI_RESET);
					LOGGER.severe(e.getMessage());
				}				
			}
			progressiveMessage("\n\t./"+StringConstants.COMPILED_STEPS_FOLDER + Thread.currentThread().getId());
			try {	
				DirectoryUtils.deleteDirectory(StringConstants.COMPILED_STEPS_FOLDER + Thread.currentThread().getId());
					System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
										+StringConstants.ANSI_RESET);
			} catch (IOException e) {
				System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" +
						StringConstants.ANSI_RESET);
				LOGGER.severe(e.getMessage());
			}
				
			progressiveMessage("\n\t./"+StringConstants.COMPILED_INTERNAL_FOLDER + Thread.currentThread().getId());
			try {
				DirectoryUtils.deleteDirectory(StringConstants.COMPILED_INTERNAL_FOLDER + Thread.currentThread().getId());
				System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
									+StringConstants.ANSI_RESET);
			} catch (IOException e) {
				System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" + 
						StringConstants.ANSI_RESET);
				LOGGER.severe(e.getMessage());
			}
		
			if(KeywordsFactory.getPackageName() != null) {
				try {
					DirectoryUtils.deleteDirectory(StringConstants.BIN_FOLDER + StringConstants.ID_SEPARATOR
										   + KeywordsFactory.getPackageName());
				
				DirectoryUtils.deleteDirectory(KeywordsFactory.getPackageName());
				} catch (IOException e) {
					
					LOGGER.severe(e.getMessage());
				}
	
			}	
			System.out.println();
		} catch (InterruptedException e) {
			
			LOGGER.severe(e.getMessage());
		}
		
		GroovyClassLoader.getSystemClassLoader().clearAssertionStatus();
		Thread.currentThread().getContextClassLoader().clearAssertionStatus();
		RunConfiguration.getTestSuiteObj().parentClassLoader.clearCache();
		try {
			RunConfiguration.getTestSuiteObj().parentClassLoader.close();
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
			
	}	
	
	
	protected static void cleanSuite() {
		
		System.out.println(StringConstants.ANSI_RESET);
		System.out.println("Deleting Temporary Files:");

			//deleting scripts folder
			System.out.println("\t./" + StringConstants.SCRIPTS_FOLDER + Thread.currentThread().getId());
			try {
				DirectoryUtils.deleteDirectory(StringConstants.SCRIPTS_FOLDER + Thread.currentThread().getId());
				System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
									+StringConstants.ANSI_RESET);
			} catch (IOException e) {
				System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" +
						StringConstants.ANSI_RESET);
				LOGGER.severe(e.getMessage());
			}
			
			//deleting suite folder
			System.out.println("\t./" + StringConstants.SUITE_FOLDER + Thread.currentThread().getId());
			try {
				DirectoryUtils.deleteDirectory(StringConstants.SUITE_FOLDER + Thread.currentThread().getId());
				System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
									+StringConstants.ANSI_RESET);
			} catch (IOException e) {
				System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" +
						StringConstants.ANSI_RESET);
				LOGGER.severe(e.getMessage());
			}
			
			//deleting Scripts folder
			System.out.println("\t./" + StringConstants.TESTCASE_SCRIPTS_FOLDER + Thread.currentThread().getId());
			try {
				DirectoryUtils.deleteDirectory(StringConstants.TESTCASE_SCRIPTS_FOLDER + Thread.currentThread().getId());
				System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
									+StringConstants.ANSI_RESET);
			} catch (IOException e) {
				System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" +
						StringConstants.ANSI_RESET);
				LOGGER.severe(e.getMessage());
			}
			
		
			System.out.println("\t./"+StringConstants.COMPILED_STEPS_FOLDER + Thread.currentThread().getId());
		try {	
			DirectoryUtils.deleteDirectory(StringConstants.COMPILED_STEPS_FOLDER + Thread.currentThread().getId());
				System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
									+StringConstants.ANSI_RESET);
		} catch (IOException e) {
			System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" +
					StringConstants.ANSI_RESET);
			LOGGER.severe(e.getMessage());
		}
			
		System.out.println("\t./"+StringConstants.COMPILED_INTERNAL_FOLDER + Thread.currentThread().getId());
		try {
			DirectoryUtils.deleteDirectory(StringConstants.COMPILED_INTERNAL_FOLDER + Thread.currentThread().getId());
			System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
								+StringConstants.ANSI_RESET);
		} catch (IOException e) {
			System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" + 
					StringConstants.ANSI_RESET);
			LOGGER.severe(e.getMessage());
		}

		if(KeywordsFactory.getPackageName() != null) {
			try {
				DirectoryUtils.deleteDirectory(StringConstants.BIN_FOLDER + StringConstants.ID_SEPARATOR
									   + KeywordsFactory.getPackageName());
			
			DirectoryUtils.deleteDirectory(KeywordsFactory.getPackageName());
			} catch (IOException e) {
				
				LOGGER.severe(e.getMessage());
			}

		}	
		System.out.println();
		
		GroovyClassLoader.getSystemClassLoader().clearAssertionStatus();
		Thread.currentThread().getContextClassLoader().clearAssertionStatus();
		RunConfiguration.getTestSuiteObj().parentClassLoader.clearCache();
		try {
			RunConfiguration.getTestSuiteObj().parentClassLoader.close();
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
		}
			
	}	

	
	protected static void runCollection(LinkedHashMap<TestSuite, Boolean> collection)
	{		
		
		@SuppressWarnings("unused")
		long start, finish;
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal;
		ArrayList<MyThread> threads = new ArrayList<>();
		ArrayList<TestSuite> suitesToExecute = new ArrayList<>();
		
		RunConfiguration.setExtent(new ExtentReports());
		RunConfiguration.getExtent().setAnalysisStrategy(AnalysisStrategy.SUITE);
		RunConfiguration.setReportFile(RunConfiguration.getReportRoot() + StringConstants.ID_SEPARATOR + 
				StringConstants.ID_SEPARATOR + RunConfiguration.getTsCollection().getReportFolder() + StringConstants.ID_SEPARATOR + 
				RunConfiguration.getTsCollection().getReportFolder() + StringConstants.HTML_EXT);
		RunConfiguration.setSpark(new ExtentSparkReporter(RunConfiguration.getReportFile()));
		File file = new File(StringConstants.EXTENT_XML_CONFIG_FILE);
		try {
			RunConfiguration.getSpark().loadXMLConfig(file);
		}catch(Exception e)
		{
			LOGGER.severe(e.getMessage());
		}
		RunConfiguration.getExtent().attachReporter(RunConfiguration.getSpark());
		RunConfiguration.getExtent().setSystemInfo(StringConstants.OS, RunConfiguration.getPlatform().toString());
		
		if(RunConfiguration.getTsCollection().getExecutionMode() == 0) 			
			RunConfiguration.getExtent().setSystemInfo(StringConstants.RUNNING_MODE, StringConstants.SEQUENTIAL_KEYWORD);
		else
			RunConfiguration.getExtent().setSystemInfo(StringConstants.RUNNING_MODE, StringConstants.PARALLEL_KEYWORD);
		
		try {
			start = System.currentTimeMillis();
			FeaturesFactory.copyFeatures();
			finish = System.currentTimeMillis();
			//System.out.println("FeaturesFactory took "+(finish-start) +"ms");

			start = System.currentTimeMillis();
			StepDefinitionsFactory.copyScripts();
			finish = System.currentTimeMillis();
			//System.out.println("StepDefinitionsFactory took "+(finish-start) +"ms");
			
			
		}catch(IllegalAccessException | InstantiationException | IOException e){
			LOGGER.severe(e.getMessage());
		}
		
		
			
		for (Map.Entry<TestSuite,Boolean> entry : collection.entrySet()) {
            
			//TestSuite ts = entry.getKey();			
			//System.out.println(ts.getProfile()+"\t"+ts.getBrowser());
			//System.gc();
			if(entry.getValue()) {
				suitesToExecute.add(entry.getKey());
			}
		}
		
		doneSignal = new CountDownLatch(suitesToExecute.size());
		
		for(TestSuite suite : suitesToExecute) {
						
				RunConfiguration.setTestSuiteObj(suite);
				
				//RUNNING MODE = SEQUENTIAL
				if(RunConfiguration.getTsCollection().getExecutionMode() == 0) {
					
								
					RunConfiguration.setTestSuiteObj(suite);
					RunConfiguration.getTestSuiteObj().suiteCase = RunConfiguration.getExtent().
							createTest(SUITE_LABEL + StringConstants.COLON + RunConfiguration.getTestSuiteObj().getName()).
							assignCategory(StringConstants.TEST_SUITES_REPORT_TAG).assignAuthor(RunConfiguration.getTestSuiteObj().
									getProfile() + StringConstants.HYPHEN + RunConfiguration.getTestSuiteObj().getBrowser());
					RunConfiguration.getExtent().flush();
					
					stopLoading();
					console(RunConfiguration.getTestSuiteObj());
													
					//RUNNING MODE = PARALLEL
				}else {
				
					threads.add(new MyThread(startSignal, doneSignal, suite));
				}
			}
		
			
			if(!threads.isEmpty()) {
				try {
										
					startSignal.countDown(); 
					doneSignal.await();	
					
										
				} catch (InterruptedException e) {
					LOGGER.severe(e.getMessage());
				}
				
			}
			
			try {
			System.out.println(StringConstants.ANSI_RESET);
			progressiveMessage("Deleting Temporary Files:");
		
			progressiveMessage("\n\t./"+StringConstants.TEMP_FOLDER);
				try {
					DirectoryUtils.deleteDirectory(StringConstants.TEMP_FOLDER);
					System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
										+StringConstants.ANSI_RESET);
				} catch (IOException e) {
					System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" +
							StringConstants.ANSI_RESET);
					LOGGER.severe(e.getMessage());
				}
			
			progressiveMessage("\n\t./"+StringConstants.CUCUMBER_DIR);
				try {
					DirectoryUtils.deleteDirectory(StringConstants.CUCUMBER_DIR);
					System.out.print(" " +StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "DONE"
										+StringConstants.ANSI_RESET);
				} catch (IOException e) {
					System.out.print(" " +StringConstants.RED_BOLD_BRIGHT + "ERROR: The file could not be deleted!" +
							StringConstants.ANSI_RESET);
					LOGGER.severe(e.getMessage());
				}
				
			}catch(Exception ex) {LOGGER.severe(ex.getMessage());}		
					
	}
	
    @SuppressWarnings("unchecked")
    protected static void setupContextClassLoader(String[] paths, GroovyClassLoader gcl) throws IOException {
        AccessController.doPrivileged(new DoSetContextAction(Thread.currentThread(), 
        		new GroovyScriptEngine(paths, gcl).getGroovyClassLoader()));
    }
    
	public static synchronized void startloading(String message) throws IOException, InterruptedException {
		
	    startLoadingTime = System.currentTimeMillis();
	    loading = Boolean.TRUE;
	    System.out.print(StringConstants.WHITE_BACKGROUND + StringConstants.BLACK_TEXT);
	    progressiveMessage(message);
	    
	    loadingThread = new Thread() {
	        @Override
	        public void run() {
	            try {
	            	while(true) {
	                    System.out.write(".".getBytes());
	                    Thread.sleep(200);
	            	}         		
	               
	            } catch (IOException | InterruptedException e) {
	            	LOGGER.severe(e.getMessage());
	            }
	        }
	    };
	    
	    loadingThread.start();
	    
	}
	
	@SuppressWarnings("deprecation")
	public static synchronized void stopLoading() {
		
		
		loading = Boolean.FALSE;
   	    try {
			System.out.write((StringConstants.ANSI_RESET + StringConstants.GREEN_BOLD_BRIGHT + 
					StringConstants.BLUE_BACKGROUND +"OK").getBytes());
			long elapsedLoadingTime = System.currentTimeMillis() - startLoadingTime;
			System.out.write((StringConstants.BLUE_BACKGROUND + StringConstants.WHITE_TEXT +
					" ("+String.valueOf(elapsedLoadingTime)+"ms)" + StringConstants.ANSI_RESET + "\r\n").getBytes());
		} catch (IOException e) {
			
			LOGGER.severe(e.getMessage());
		}   			
   	    loadingThread.stop();
	}
		
	
	public static synchronized void progressiveMessage(String message) throws InterruptedException {
			progressiveMessage(message, 30);
		}
	
	public static synchronized void progressiveMessage(String message, int speed) throws InterruptedException {
		
		for(int i = 0; i < message.length(); i++) {
			System.out.print(message.charAt(i));
			Thread.sleep(speed);
		}
		
	}
	
	
}
