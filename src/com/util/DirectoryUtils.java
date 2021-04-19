package com.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class DirectoryUtils {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
		public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) {
	    File sourceDirectory = new File(sourceDirectoryLocation);
	    File destinationDirectory = new File(destinationDirectoryLocation);
	    try {
			FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
		} catch (IOException e) {

			LOGGER.severe(e.getMessage());
		}
	}
	
	public static void deleteDirectory(String dir) throws IOException{
		
			LOGGER.info("Deleting dir : " + dir);
			FileUtils.deleteDirectory(new File(dir));		
	}
}
