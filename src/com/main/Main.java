package com.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
//import java.util.Scanner;
import java.util.logging.Logger;

import com.configuration.RunConfiguration;
import com.configuration.RunConfiguration.OSType;
import com.console.RunConsole;
import com.constants.StringConstants;
import com.logger.CustomLogger;


public class Main {	
	

    protected static HashMap<Variables, String> variablesMap;
    public static String[] errorList;
    private static File suitePath;
    public File getSuitePath() {return suitePath;}

	
	protected static enum Variables{
		PROJECT_VAR,
		TEST_SUITE_VAR,
		PROFILE_VAR,
		BROWSER_VAR,
		REPORT_VAR,
		FAILURE_VAR
	}
	
	 private static final String HEAD = StringConstants.CYAN_TEXT+ "NRE>" + StringConstants.ANSI_RESET;
	 private static final String QUIT_COMMAND = "quit";
	 private static final String CLEAR_COMMAND = "clear";
	 private static final String HELP_COMMAND = "help";
	 private static final String PROJECT = "projectPath";
	 private static final String TEST_SUITE = "testSuitePath";
	 private static final String PROFILE = "executionProfile";
	 private static final String BROWSER = "browserType";
	 private static final String REPORT = "reportFolder";
	 private static final String FAILURE = "failureHandling";
	 
	 //project_123 pcte.us.to
	 //static final String COMMAND = "-projectPath=/home/richard89/git/project123/PCTE_Tests -testSuitePath=Test Suites/9001-9500 Smoke/S9001 All Smoke Tests -browserType=Chrome -executionProfile=Authentication -failureHandling=stop";
     //static final String COMMAND = "-projectPath=/home/richard89/Documents/new/Sebastian-2-master2/Sebastian-2-master/PCTE-Similar1 -testSuitePath=Smoke -browserType=Chrome(headless) -failureHandling=continue";
	 //static final String COMMAND = "-projectPath=/home/richard89/Downloads/scripts/PCTE Tests -testSuitePath=Test Suites/9001-9500 Smoke/S9001 All Smoke Tests -browserType=Chrome -executionProfile=RCS01 QA01";
	 static final String COMMAND = "-projectPath=/home/richard89/git/project123/PCTE_Tests -testSuitePath=Test Suites/9001-9500 Smoke/C9001 All Smoke Tests Across All Browsers -failureHandling=continue";
	 		
	 private static final String LOGO = StringConstants.CYAN_TEXT + "\n"
			 				  + "******************WELCOME TO******************\n"
     						  + "##         ##   ############    ############# \n"
     						  + "###        ##   ##         ##   ##            \n"
     						  + "## ##      ##   ##         ##   ##            \n"
     						  + "##  ##     ##   ##        ##    ##            \n"
     						  + "##   ##    ##   ##########      ###########   \n"
     						  + "##    ##   ##   ##       ##     ##            \n"
     						  + "##     ##  ##   ##        ##    ##            \n"
     						  + "##      ## ##   ##         ##   ##            \n"
     						  + "##       ####   ##          ##  ############# \n"
     						  + "********The new runtime engine for PCTE*******\n"
     						  + StringConstants.ANSI_RESET;
	 
	 private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
     
	 	 
	 protected static void initializeVariables() {
		 	
		 	variablesMap = new HashMap<>();
		 	errorList = null;
		    suitePath = null;
		 
		 	variablesMap.put(Variables.PROJECT_VAR, null);
			variablesMap.put(Variables.TEST_SUITE_VAR, null);
			variablesMap.put(Variables.PROFILE_VAR, null);
			variablesMap.put(Variables.BROWSER_VAR, null);
			variablesMap.put(Variables.REPORT_VAR, null);
			variablesMap.put(Variables.FAILURE_VAR, null);			
			
	 }
 
	 
	public static void main(String[] args) throws IOException {
		
		long absStart = 0, absFinish = 0;
		
		System.out.println(LOGO);
		
		
		RunConfiguration.setPlatform(System.getProperty("os.name"));
		RunConfiguration.setProjectDir(System.getProperty("user.home"));
		
		try {	
			CustomLogger.setup();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		LOGGER.setLevel(Level.INFO);
		LOGGER.info("Program Started!");
		
		if(args.length > 0) {
			
			
			if(args.length == 1 && args[0].equalsIgnoreCase(HELP_COMMAND)) {
				LOGGER.info("Displaying Help...");
					displayHelp();
				LOGGER.info("Closing Program...");
				System.exit(0);
			}
			
			else if(validateInput(args) > 0)
				;
			else {
			
				initializeVariables();
				processInput(args);				
			
				if(validateVariables() > 0)
					;
				else {
	
					Thread executionThread = new Thread() {						
						@Override
						public void run(){
							RunConsole.run();
						}						
					};
		
					absStart = System.currentTimeMillis();
					executionThread.start();
			
					try {
						executionThread.join();
					} catch (InterruptedException e) {
			
						LOGGER.severe(e.getMessage());
					}
		
					RunConfiguration.resetConfigurationData();
					System.gc();
					absFinish = System.currentTimeMillis();
					System.out.println(StringConstants.ANSI_RESET);
					System.out.print(StringConstants.WHITE_BACKGROUND + StringConstants.BLACK_BOLD_BRIGHT + "COMPLETED in ");
					System.out.print(StringConstants.BLUE_BACKGROUND + StringConstants.WHITE_BOLD_BRIGHT + "(" 
								+ StringConstants.YELLOW_TEXT + getElapsedTime(Long.valueOf(absFinish - absStart)) 
								+ StringConstants.WHITE_BOLD_BRIGHT + ")");
					System.out.println(StringConstants.ANSI_RESET);
				
					LOGGER.info("COMPLETED in "+ getElapsedTime(Long.valueOf(absFinish - absStart)));
									
				}
				LOGGER.info("Closing Program...");
				System.exit(0);
			}
		}
		else {
		
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			Boolean quit = Boolean.FALSE;
			
		
			while(!quit) {
						
				in = new BufferedReader(new InputStreamReader(System.in)); 
				initializeVariables();
							
					System.out.print(HEAD);
			
					String line = in.readLine().trim();
						
					if(line.equalsIgnoreCase(QUIT_COMMAND))
						quit = Boolean.TRUE;
					else if(line.equalsIgnoreCase(CLEAR_COMMAND)){
						LOGGER.info("Clearing Screen...");
						clearScreen();
					}
					else if(line.equalsIgnoreCase(HELP_COMMAND)){
						LOGGER.info("Displaying Help...");
						displayHelp();
					}
					else {
					
						if(line.compareTo("123") == 0)
							processInput(COMMAND);				
						//System.out.println(variables);
				
						else 
							processInput(line);	
				
				
						if(validateVariables() > 0)
							;
						else {
					
							Thread executionThread = new Thread() {						
								@Override
								public void run(){
									RunConsole.run();
								}						
							};
						
							absStart = System.currentTimeMillis();
							executionThread.start();
							
							try {
								executionThread.join();
							} catch (InterruptedException e) {
							
								LOGGER.severe(e.getMessage());
							}
						
							RunConfiguration.resetConfigurationData();
							System.gc();
							absFinish = System.currentTimeMillis();
							System.out.println(StringConstants.ANSI_RESET);
							System.out.print(StringConstants.WHITE_BACKGROUND + StringConstants.BLACK_BOLD_BRIGHT + "COMPLETED in ");
							System.out.print(StringConstants.BLUE_BACKGROUND + StringConstants.WHITE_BOLD_BRIGHT + "(" 
									+ StringConstants.YELLOW_TEXT +getElapsedTime(Long.valueOf(absFinish - absStart)) 
									+ StringConstants.WHITE_BOLD_BRIGHT + ")");
							System.out.println(StringConstants.ANSI_RESET);
							
							LOGGER.info("COMPLETED in "+ getElapsedTime(Long.valueOf(absFinish - absStart)));
						}	
					}
				}

			in.close();
			LOGGER.info("Closing Program...");
			System.exit(0);
		}
	}
	
	protected static void processInput(String line) {
		
		if(RunConfiguration.getPlatform().equals(OSType.WINDOWS)) {
			line = line.replace(File.separator, StringConstants.ID_SEPARATOR); 
			line = line.replace("/", StringConstants.ID_SEPARATOR);  
    	
		}
    
		else if(RunConfiguration.getPlatform().equals(OSType.LINUX)){
			line = line.replace(File.separator, StringConstants.ID_SEPARATOR); 
			line = line.replace("\\", StringConstants.ID_SEPARATOR);  
		}	
	
			String[] arrOfStr = line.split(" -", 15);
			
			processInput(arrOfStr);
	}
	
	protected static void processInput(String[] arrOfStr) {
			
		variablesMap.clear();
	
		try{
		        
			for(int i = 0; i < arrOfStr.length; i++)
        			arrOfStr[i].trim();
			
        
        for(int i = 0; i < arrOfStr.length; i++) {
        	if(arrOfStr[i].contains(PROJECT)) {
        		
        		
        		String project = arrOfStr[i].replace("projectPath=", "").replace("\"", "").trim();
        		if(project.startsWith("-"))
        			project = project.substring(1);
        		if(project.contains(".prj"))
        			project = project.substring(0, project.lastIndexOf(StringConstants.ID_SEPARATOR, project.length()));
        		
        		
        		//if(!project.contains(System.getProperty("user.home")))
        			//project = System.getProperty("user.home").concat(StringConstants.ID_SEPARATOR).concat(project);
        		
        		variablesMap.put(Variables.PROJECT_VAR, project);
        		
           		        		
        	}else if(arrOfStr[i].contains(PROFILE)) {
        		
        		String profile = arrOfStr[i].replace("executionProfile=","").replace("\"", "").trim();
        		if(profile.startsWith("-"))
        			profile = profile.substring(1);
        		variablesMap.put(Variables.PROFILE_VAR, profile);       		
        		      		
        	}
        	else if(arrOfStr[i].contains(BROWSER)) {
        		
        		String browser = arrOfStr[i].replace("browserType=", "").replace("\"", "").trim();
        		if(browser.startsWith("-"))
        			browser = browser.substring(1);
        		variablesMap.put(Variables.BROWSER_VAR, browser);
        		        	    		
        	}
        	else if(arrOfStr[i].contains(TEST_SUITE)) {
        		
        		String testSuite = arrOfStr[i].replace("testSuitePath=", "").replace("\"", "")
        				.concat(StringConstants.TEST_SUITES_EXT).trim();
        		if(testSuite.startsWith("-"))
        			testSuite = testSuite.substring(1);
        		if(!testSuite.contains(StringConstants.DEFAULT_TEST_SUITES_FOLDER))
        			testSuite = StringConstants.DEFAULT_TEST_SUITES_FOLDER.concat
        						(StringConstants.ID_SEPARATOR).concat(testSuite);
        		        			
        		variablesMap.put(Variables.TEST_SUITE_VAR, testSuite);
        		     		
        	}
          	else if(arrOfStr[i].contains(REPORT)) {
        		
        		String report = arrOfStr[i].replace("reportFolder=", "").replace("\"", "").trim();
        		if(report.startsWith("-"))
        			report = report.substring(1);
        		variablesMap.put(Variables.REPORT_VAR, report);
        		     		
        	}else if(arrOfStr[i].contains(FAILURE)) {
    		
        		String failure = arrOfStr[i].replace("failureHandling=", "").replace("\"", "").trim();
        		if(failure.startsWith("-"))
        			failure = failure.substring(1);
        		variablesMap.put(Variables.FAILURE_VAR, failure);
    		     		
        	}
                
        }
        
		}catch(Exception e) {LOGGER.severe(e.getMessage());}
	}
		

	
	
	protected static void clearScreen(){
	    
	    try {
	        if (RunConfiguration.getPlatform().equals(OSType.WINDOWS))
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        else {
	            System.out.print(StringConstants.CLEAR_SCAPE_CHAIN);  
	        	System.out.flush();  	        	
	        	//Runtime.getRuntime().exec("clear");
	        }
	    } catch (IOException | InterruptedException ex) {LOGGER.severe(ex.getMessage());}
	}

	protected static void displayHelp(){
	   
	    try {
	        
	    	InputStream input = new BufferedInputStream(new FileInputStream(StringConstants.README_FILE));
	    	byte[] buffer = new byte[8192];

	    	try {
	    		
	    		System.out.println(StringConstants.BLUE_TEXT);
	    	    for (int length = 0; (length = input.read(buffer)) != -1;) 
	    	    	System.out.write(buffer, 0, length);
	    	   	System.out.println(StringConstants.ANSI_RESET);
	    	} finally {
	    	    input.close();
	    	}
	    	
	    	
	    } catch (Exception ex) {LOGGER.severe(ex.getMessage());}
	}
	
	
	protected static int validateInput(String[] args) {		
		return 0;
	}
	

	protected static int validateVariables() {
		
		int flag = 0;
		
		//validate PROJECT DIR
		if(variablesMap.get(Variables.PROJECT_VAR) != null){
		
			//System.out.println(variables.get(Variables.PROJECT_VAR));
			File projDirectory = new File(variablesMap.get(Variables.PROJECT_VAR));
			if(projDirectory.exists()) {
				RunConfiguration.setProjectDir(variablesMap.get(Variables.PROJECT_VAR));
			
			}else {
				System.out.println("ERROR: Project path does not exist!");
				LOGGER.severe("ERROR: Invalid project path!");
				flag++;
			}
			//-------------------------------------------------------------------------
			
			//validate PROFILE
			if(variablesMap.get(Variables.PROFILE_VAR) == null)
				variablesMap.put(Variables.PROFILE_VAR, StringConstants.DEFAULT_PROFILE);
			
			File profileFile = new File(RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR 
					+ StringConstants.PROFILE_FOLDER + StringConstants.ID_SEPARATOR 
					+ variablesMap.get(Variables.PROFILE_VAR) + StringConstants.PROFILE_EXT);
			if(profileFile.isFile()) {
				RunConfiguration.setProfile(variablesMap.get(Variables.PROFILE_VAR));
				
			}else {
				System.out.println("ERROR: Profile '"+ variablesMap.get(Variables.PROFILE_VAR) + "' does not exist!");
				LOGGER.severe("ERROR: Invalid profile!");
				flag++;
			}
			//-------------------------------------------------------------------------

			
			//validate BROWSER
			if(variablesMap.get(Variables.BROWSER_VAR) == null)
				variablesMap.put(Variables.BROWSER_VAR, StringConstants.DEFAULT_BROWSER);
			RunConfiguration.setBrowser(variablesMap.get(Variables.BROWSER_VAR));
			//-------------------------------------------------------------------------

			
			//validate REPORT DIR---------------------------------------------------
			if(variablesMap.get(Variables.REPORT_VAR) == null)
				/*** This is the final directory ***/
				//variablesMap.put(Variables.REPORT_VAR, RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR 
				//		+ StringConstants.DEFAULT_REPORT_DIR);
				variablesMap.put(Variables.REPORT_VAR, StringConstants.DEFAULT_REPORT_DIR);
		
			new File(variablesMap.get(Variables.REPORT_VAR)).mkdirs();
			
				RunConfiguration.setReportDir(variablesMap.get(Variables.REPORT_VAR));
			//-------------------------------------------------------------------------
			
				
			//validate FAILURE HANDLING---------------------------------------------------
				RunConfiguration.setFailureHandling(variablesMap.get(Variables.FAILURE_VAR));
			//-------------------------------------------------------------------------	
				
				
			//validate TEST SUITE---------------------------------------------------
			if(variablesMap.get(Variables.TEST_SUITE_VAR) != null) {
				
				variablesMap.put(Variables.TEST_SUITE_VAR, RunConfiguration.getProjectDir().
						concat(StringConstants.ID_SEPARATOR.concat(variablesMap.get(Variables.TEST_SUITE_VAR))));
				
				RunConfiguration.setTestSuite(variablesMap.get(Variables.TEST_SUITE_VAR));
				
			}else {
				System.out.println("ERROR: No test suite was provided!");
				LOGGER.severe("ERROR: Invalid TestSuite!");
				flag++;
			}
			//-------------------------------------------------------------------------

		}else{
			System.out.println("ERROR: No project path was provided!");
			LOGGER.severe("ERROR: Invalid project path!");
			flag++;
		}
		//-------------------------------------------------------------------------

			return flag;	
	}
	
	private static String getElapsedTime(long elapsed) {
		
		long secs = Long.valueOf(0), mins = Long.valueOf(0), hours = Long.valueOf(0);
				
		if(Long.valueOf(elapsed).compareTo(Long.valueOf(0)) > 0) {
			
			secs = (long)elapsed / Long.valueOf(1000);
									
			if(Long.valueOf(secs).compareTo(Long.valueOf(60)) > 0) {
				mins = (long)secs / Long.valueOf(60);
				secs = (long)secs % Long.valueOf(60);
				
				if(Long.valueOf(mins).compareTo(Long.valueOf(60)) > 0) {
					hours = (long)mins / Long.valueOf(60);
					mins = (long)mins % Long.valueOf(60);	
					
					return	String.valueOf(hours) + "h " + String.valueOf(mins) + "m " + String.valueOf(secs) + "s";
							 
				}
				else
					return	String.valueOf(mins) + "m " + String.valueOf(secs) + "s";			
			}
			else
				return String.valueOf(secs) + "s";		
		}
		else
			return "0s";
	}
	
	
}
