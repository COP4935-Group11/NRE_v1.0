package com.scripts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.configuration.RunConfiguration;
import com.constants.StringConstants;
import com.util.DirectoryUtils;

public class KeywordsFactory {
	
	static final String CSVDATA_IMPORT = "com.ucf.pcte.CSVData";
	protected static String packageName = null;
	
	public static String getPackageName(){return packageName;}

	public static void createKeywordFiles()
	{		
		List<File> files = new ArrayList<File>();
		readFiles(RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR + StringConstants.KEYWORDS_FOLDER, files);
		
		for(File f : files) {
			try {
				compileGroovyScript(f.getName(), getScript(f));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	    if(files.size() > 0)
			DirectoryUtils.copyDirectory(packageName, StringConstants.BIN_FOLDER+StringConstants.ID_SEPARATOR+packageName);
	}	

	public static void compileGroovyScript(final String className, final String script) {


	}
	
	static String getScript(File rootScript) throws IOException {

		ArrayList<String> fileContent = new ArrayList<String>(Files.readAllLines(Paths.get(rootScript.toURI()), 
				StringConstants.STANDARD_CHARSET));
		ArrayList<String> script = new ArrayList<>();		
						
		
		Iterator<String> iter = fileContent.iterator();
		while (iter.hasNext()) {
			 String line = iter.next();
			 if(line.contains("package")) { 
				 packageName = line.substring(line.indexOf(" ")+1, line.indexOf('.'));
				 script.add(line); iter.remove();
			 }
			 if (line.contains("import")) {
				 if(line.contains("com"))				 
					 iter.remove();
				 else {
					 script.add(line);
					 iter.remove();
				 }
			 }
		}			
			
		script.addAll(new ArrayList<>(Arrays.asList(StringConstants.KEYWORD_IMPORTS)));
		script.addAll(fileContent);
						
		return String.join(StringConstants.NEW_LINE, script);
	}
	
	public static void readFiles(String directoryName, List<File> files) {
	    File directory = new File(directoryName);

	    // Get all files from a directory.
	    File[] fList = directory.listFiles();
	    if(fList != null)
	        for (File file : fList) {      
	            if (file.isFile()) {
	                files.add(file);
	            } else if (file.isDirectory()) {
	                readFiles(file.getAbsolutePath(), files);
	            }
	        }
	}
}
