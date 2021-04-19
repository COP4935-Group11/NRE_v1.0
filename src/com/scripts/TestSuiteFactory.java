package com.scripts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.configuration.RunConfiguration;
import com.constants.StringConstants;


public class TestSuiteFactory {
	 
	protected static ArrayList<String> imports;
	
	public static void copySuite() throws IOException{
		
		String testSuite = RunConfiguration.getTestSuiteObj().getPath().replace(StringConstants.TEST_SUITES_EXT, StringConstants.GROOVY_EXT);
		File testSuiteFile = new File(testSuite);		
		RunConfiguration.getTestSuiteObj().setTsPath(getScript(testSuiteFile).getAbsolutePath().replace(StringConstants.ROOT_DIR, ""));
		//System.out.println(RunConfiguration.getTestSuiteObj().getPath());
	}	
	
	protected static File getScript(File rootScript) throws IOException {

		ArrayList<String> fileContent = new ArrayList<String>(Files.readAllLines(Paths.get(rootScript.toURI()), 
				StringConstants.STANDARD_CHARSET));
		ArrayList<String> script = new ArrayList<>();		
		imports = new ArrayList<>(Arrays.asList(StringConstants.SUITE_IMPORTS));					
		
		Iterator<String> iter = fileContent.iterator();
		while (iter.hasNext()) {
			 String line = iter.next();
			  if (line.contains("package") || line.contains("import")) iter.remove();			  
		}			
			
		script.add("package suite" + Thread.currentThread().getId());
		script.addAll(imports);
		script.add("import steps" + Thread.currentThread().getId() + ".*");
		script.add("import internal" + Thread.currentThread().getId()+".GlobalVariable as GlobalVariable");
		script.addAll(fileContent);
		
		new File(StringConstants.ROOT_DIR + StringConstants.SUITE_FOLDER + Thread.currentThread().getId()).mkdirs();
		Path targetPath = Paths.get(new File(StringConstants.ROOT_DIR + StringConstants.SUITE_FOLDER + Thread.currentThread().getId()
											+ StringConstants.ID_SEPARATOR + rootScript.getName()
											.replace(StringConstants.GROOVY_EXT, "") + StringConstants.GROOVY_EXT).getAbsolutePath());
		Files.writeString(targetPath, String.join(StringConstants.NEW_LINE, script), StringConstants.STANDARD_CHARSET);
		return new File(targetPath.toAbsolutePath().toString());
	}

}