package com.driver;

import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;

import com.configuration.RunConfiguration;
import com.console.RunConsole;
import com.constants.StringConstants;
import com.exception.StepFailedException;


public class DriverFactory {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static ThreadLocal<DriverStack> localDriverStack = new ThreadLocal<DriverStack>() {
        @Override
        protected DriverStack initialValue() {
            return new DriverStack();
        }
    };
		
	    //return a new driver from the map
	    public static WebDriver getWebDriver(){
	            	
	    	if(!localDriverStack.get().getStack().isEmpty())
	    		return localDriverStack.get().getStack().peek().driver;
	    	else { 
	    		openWebDriver();
	    		return localDriverStack.get().getStack().peek().driver;
	    	}
	    	
	    }
	    
	    
	    
	    public static void openWebDriver() {
	    		    	
	    try {

	    	System.out.print(StringConstants.PURPLE_TEXT);
	    	if(localDriverStack.get().getStack().isEmpty())
	    		localDriverStack.get().push(new Driver(RunConfiguration.getTestSuiteObj().getBrowser()));
	    	System.out.print(StringConstants.ANSI_RESET);
	    }catch(Exception e){
	    	LOGGER.severe(e.getMessage());
	    }
	   		   		
	    }
	    	    
	    public static void closeWebDriver() {
	    	
	    	if(localDriverStack.get().getStack().peek() != null)
	    		localDriverStack.get().quit();
	    	else {
	    		LOGGER.severe("No open driver was found!");
	    		throw new StepFailedException("No open driver was found!");
	    	}
	    }
	    
	    
		public static void cleanAllDrivers() {
	    	
			try {
				System.out.println(StringConstants.ANSI_RESET);
				RunConsole.progressiveMessage("Cleaning drivers...");
			} catch (InterruptedException e) {
					LOGGER.severe(e.getMessage());
			}
				while(!localDriverStack.get().getStack().isEmpty()) 
					localDriverStack.get().quit();
				
				localDriverStack.get().reset();		
				localDriverStack.remove();
				System.out.println(StringConstants.GREEN_BOLD_BRIGHT+ StringConstants.GREEN_UNDERLINED + "DONE" 
									+ StringConstants.ANSI_RESET);
				
				LOGGER.info("Cleaned drivers successfully!");
	    }	
	    		
}