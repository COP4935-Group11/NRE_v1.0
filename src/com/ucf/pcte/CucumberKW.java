package com.ucf.pcte;

import cucumber.api.cli.Main;

import com.constants.StringConstants;

import java.util.logging.Logger;

import com.configuration.RunConfiguration;
import com.console.RunConsole;

public class CucumberKW {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);	
	
	public static void runFeatureFile(String relativeFilePath)
	{
		
		relativeFilePath = StringConstants.FEATURES_FOLDER.concat(relativeFilePath
				.substring(relativeFilePath.lastIndexOf('/'), relativeFilePath.length()));
		
		//System.out.println(relativeFilePath);
		System.out.println(StringConstants.BLUE_BOLD);
		try {
			RunConsole.progressiveMessage("Starting Cucumber keyword runFeatureFile: " 
					+ StringConstants.PURPLE_BOLD + StringConstants.PURPLE_UNDERLINED + relativeFilePath + StringConstants.ANSI_RESET
					+ StringConstants.BLUE_BOLD + "\nand extracting report to folder: " 
					+ StringConstants.PURPLE_BOLD + StringConstants.PURPLE_UNDERLINED + RunConfiguration.getReportRoot() + StringConstants.ID_SEPARATOR + "...", 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(StringConstants.ANSI_RESET);
		
		LOGGER.info("Starting Cucumber keyword runFeatureFile: " + relativeFilePath + " and extracting report to folder: " 
					+ RunConfiguration.getReportRoot() + StringConstants.ID_SEPARATOR + "...");
		
		long currentTime = System.currentTimeMillis();
			
		String[] argv = new String[]{
				relativeFilePath,
				"--glue",
				"steps"+Thread.currentThread().getId(),
				"--strict",
                "-p",
                "pretty",
                "-p",
           		"json:"+ "Cucumber" + StringConstants.ID_SEPARATOR + String.valueOf(currentTime) + StringConstants.JSON_EXT
                };
		
		RunConfiguration.getTestSuiteObj().getCurrentTestCase().setCucumber();
		RunConfiguration.getTestSuiteObj().getCurrentTestCase().getCucumberFiles().add("Cucumber" + StringConstants.ID_SEPARATOR + String.valueOf(currentTime) + StringConstants.JSON_EXT);
	

		boolean runSuccess = Main.run(argv, Thread.currentThread().getContextClassLoader()) == 0;
	
		try {
			
			System.out.print(StringConstants.BLUE_BOLD);
			RunConsole.progressiveMessage("Feature file: "+ relativeFilePath + " ", 15);
			
			if (runSuccess) {				
				System.out.print(StringConstants.GREEN_BOLD_BRIGHT + StringConstants.GREEN_UNDERLINED + "PASSED" + StringConstants.ANSI_RESET + "\n");
				LOGGER.finest("Feature file: "+ relativeFilePath + " PASSED");
			} else {
				System.out.print(StringConstants.RED_BOLD_BRIGHT + StringConstants.RED_UNDERLINED + "FAILED" + StringConstants.ANSI_RESET + "\n");
				LOGGER.severe("Feature file: "+ relativeFilePath + " FAILED");
			}		
			
		} catch (InterruptedException e) {LOGGER.severe(e.getMessage());}
	}
	
}
