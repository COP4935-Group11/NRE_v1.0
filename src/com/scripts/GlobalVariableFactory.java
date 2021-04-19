package com.scripts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.groovy.control.CompilationFailedException;

import com.configuration.RunConfiguration;
import com.constants.StringConstants;

public class GlobalVariableFactory {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void createGlobalVarFile(Map<String,Object> GlobalVariables)
	{		
		ArrayList<String> fileContent = new ArrayList<>();
		
		fileContent.add("package internal"+Thread.currentThread().getId());
		fileContent.add("import com.console.RunConsole");
		fileContent.add("public class GlobalVariable {");
		
		GlobalVariables.forEach((key, value) -> 
				fileContent.add("public static Object " + key + " = " + "RunConsole.getGlobalVariables().get('" + key +"')"));
				fileContent.add("}");
		
		compileGroovyScript(String.join(StringConstants.NEW_LINE, fileContent));
		
	}	

	public static void compileGroovyScript(final String script) {
		try {
		 FileWriter myWriter = new FileWriter(StringConstants.ROOT_DIR + "GlobalVariable" + Thread.currentThread().getId() + StringConstants.GROOVY_EXT);
	      myWriter.write(String.join(StringConstants.NEW_LINE, script));
	      myWriter.flush();
	      myWriter.close();
	      				
	      File GlobalVarFile = new File(StringConstants.ROOT_DIR + "GlobalVariable" + Thread.currentThread().getId() + StringConstants.GROOVY_EXT);
		
	      RunConfiguration.getTestSuiteObj().addClass(GlobalVarFile);
	      GlobalVarFile.delete();
	      
			//RunConsole.getTestSuite().getParentClassLoader();
			//ClassLoader.getSystemClassLoader().loadClass(clz.getName());
			//for(Field ff : clz.getDeclaredFields())
			//System.out.println(ff.getName());
			
		} catch (CompilationFailedException | ClassNotFoundException | IOException e1) {

			LOGGER.severe(e1.getMessage());
		}


	}
	
}
