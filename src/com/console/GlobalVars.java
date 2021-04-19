package com.console;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import groovy.lang.GroovyShell;
import groovy.util.Node;
import groovy.util.NodeList;
import groovy.xml.XmlParser;

import com.configuration.RunConfiguration;
import com.constants.StringConstants;

public class GlobalVars{
	
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static ThreadLocal<Map<String, Object>> selectedVariables = new ThreadLocal<Map<String, Object>>(){
		@Override
        protected Map<String,Object> initialValue() {
            return new HashMap<String,Object>();
        }
	};
	
	public static Map<String, Object> getGlobalVars(String profileName) {
				
		try {
            selectedVariables.set(new HashMap<>()); 
                        
            Node rootNode = new XmlParser()
                    .parse(new File(RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR 
        					+StringConstants.PROFILE_FOLDER + StringConstants.ID_SEPARATOR 
        					+profileName + StringConstants.PROFILE_EXT));
            NodeList variableNodes = (NodeList) rootNode.get("GlobalVariableEntity");
            for (int index = 0; index < variableNodes.size(); index++) {
                Node globalVariableNode = (Node) variableNodes.get(index);
                String variableName = ((Node) ((NodeList) globalVariableNode.get("name")).get(0)).text();
                String defaultValue = ((Node) ((NodeList) globalVariableNode.get("initValue")).get(0)).text();
                try {
                	selectedVariables.get().put(variableName, getObject(defaultValue));
                } catch (Exception e) {
                    LOGGER.severe(e.getMessage());
                }
            }            
            //System.out.println(selectedVariables.get());
            return selectedVariables.get();
        } catch (Exception ex) {
            System.out.println("Could not create global variable.");
            LOGGER.severe(ex.getMessage());
            return Collections.emptyMap();
        }  
    }
	
	protected static Object getObject(String defaultValue)
	{
		ImportCustomizer importCustomizer = new ImportCustomizer();

        importCustomizer.addStaticImport("com.ucf.pcte.TestDataFinder", "findTestData");
		 
		CompilerConfiguration configuration = new CompilerConfiguration();
		configuration.addCompilationCustomizers(importCustomizer);
		String script = defaultValue;
		GroovyShell groovyShell = new GroovyShell(RunConfiguration.getTestSuiteObj().parentClassLoader, configuration);
		Object output = null;
		
		try {
			output = groovyShell.evaluate(script);
		}catch(Exception e)
		{
			LOGGER.severe(e.getMessage());
			RunConfiguration.getTestSuiteObj().currentNode.fail(e.getLocalizedMessage());
		
		}
		
		
		return output;
	}

}