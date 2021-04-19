package com.scripts;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

import com.configuration.RunConfiguration;
import com.constants.StringConstants;

public class StepDefinitionsFactory {	
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	protected static ArrayList<File> listOfSteps;
	
	protected static ThreadLocal<LinkedList<File>> listOfScripts = new ThreadLocal<LinkedList<File>>() {
		  @Override
	        protected LinkedList<File> initialValue() {
	            return new LinkedList<File>();
	        }
	};
	
	
	protected static ArrayList<String> imports;


	public static void copyScripts(){
			
		listOfSteps = new ArrayList<>();
		readFolder(new File(RunConfiguration.getProjectDir() + StringConstants.SCRIPTS_SOURCE + StringConstants.ID_SEPARATOR));
		
	}
	
	
	
	public static void compileScripts() {
		
		listOfScripts.set(new LinkedList<>());
		
		for(File f : listOfSteps)
			try {
				listOfScripts.get().add(getScript(f));
			} catch (IOException e1) {

				LOGGER.severe(e1.getMessage());
			}
		
		while(!listOfScripts.get().isEmpty()) {
			//System.out.println(listOfScripts);	//debugging mode
			try {		
				
				RunConfiguration.getTestSuiteObj().addClass(listOfScripts.get().peekFirst());
				listOfScripts.get().removeFirst();				
			}catch(Exception e) {
			
				if(listOfScripts.get().size() > 1) {
					LOGGER.severe(e.getMessage());	
					File tempFile = listOfScripts.get().pollFirst();
					listOfScripts.get().addLast(tempFile);
				}
				else
					LOGGER.severe(e.getMessage());			
			}			 
		}
	}

	protected static File getScript(File rootScript) throws IOException {

		ArrayList<String> fileContent = new ArrayList<String>(Files.readAllLines(Paths.get(rootScript.toURI()), 
				StringConstants.STANDARD_CHARSET));
		ArrayList<String> script = new ArrayList<>();		
		imports = new ArrayList<>(Arrays.asList(StringConstants.IMPORTS));					
		
		Iterator<String> iter = fileContent.iterator();
		while (iter.hasNext()) {
			 String line = iter.next();
			  if (line.contains("package") || line.contains("import")) iter.remove();			  
		}			
			
		script.add(StringConstants.DEFAULT_SCRIPTS_PACKAGE + Thread.currentThread().getId());
		script.addAll(imports);	
		script.add("import steps" + Thread.currentThread().getId() + ".*");
		script.add("import internal" + Thread.currentThread().getId()+".GlobalVariable as GlobalVariable");
		script.addAll(fileContent);
		
		new File(StringConstants.ROOT_DIR + StringConstants.SCRIPTS_FOLDER + Thread.currentThread().getId()).mkdirs();
		Path targetPath = Paths.get(new File(StringConstants.ROOT_DIR + StringConstants.SCRIPTS_FOLDER + Thread.currentThread().getId()
											+ StringConstants.ID_SEPARATOR + rootScript.getName()).getAbsolutePath());
		Files.writeString(targetPath, String.join(StringConstants.NEW_LINE, script), StringConstants.STANDARD_CHARSET);
		return new File(targetPath.toAbsolutePath().toString());
	}
		
	
	protected static void readFolder(File folder) {		
			//create filters for iterating folders and files
			FileFilter folderFilter = new FileFilter() {
	            @Override
	            public boolean accept(File pathname) {
	               return pathname.isDirectory();
	            }
	         };	         
	         FileFilter fileFilter = new FileFilter() {
		            @Override
		            public boolean accept(File pathname) {
		               return pathname.isFile();
		            }
		         };		
		         
		    //Verify if there are any files in the folder
	         File[] lsFiles = folder.listFiles(fileFilter);
	       //Verify if the folder contains nested folders
	         File[] ls = folder.listFiles(folderFilter);
	        	         	         
	         //copy the files first (to handle hooks properly)
	         for(int i = 0; i < lsFiles.length; i++) {
	        	 if(!lsFiles[i].getName().contains(StringConstants.CUSTOM_ATTRIBUTES_FILE))
					listOfSteps.add(lsFiles[i]);
	         }
	         //if there are any containing folder, repeat the recursion
	         if(ls.length > 0)
	        	 readFolder(ls[0]);	         
	}	
	
}
