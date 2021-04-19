package com.scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.configuration.RunConfiguration;
import com.console.TestCase;
import com.constants.StringConstants;

public class TestCaseFactory {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void copyTestCaseScripts()
	{
		//String source = StringConstants.TESTCASE_SCRIPTS_SOURCE;
		File srcDir = new File(RunConfiguration.getProjectDir()+StringConstants.TESTCASE_SCRIPTS_SOURCE + StringConstants.ID_SEPARATOR);

		//String destination = "temp/Scripts";
		File destDir = new File(StringConstants.TESTCASE_SCRIPTS_FOLDER + Thread.currentThread().getId());
	
		try {
		    FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
		   // e.printStackTrace();
		}
		
		changeImports();
	}
	
	public static List<File> files;
	
	
	public static void changeImports()
	{
		
        
        files = listf("temp"+StringConstants.ID_SEPARATOR+"Scripts"+Thread.currentThread().getId());
        
        removeAndAdd(files);
	}
	
	public static List<File> listf(String directoryName) {
        //File directory = new File(directoryName);

        List<File> resultList = new ArrayList<File>();

        // get all the files from a directory
        //File[] fList = directory.listFiles();
        
        for(TestCase tc : RunConfiguration.getTestSuiteObj().getTestCases()) {
        	
        	tc.setCaseScriptPath(Thread.currentThread().getId());
        	
        	resultList.add(new File(tc.getCaseScriptPath()));
        }
        	
                
        return resultList;
    }
	
	public static void removeAndAdd(List<File> files)
    {
        for(File file: files)
        {
            List<String> lines = new ArrayList<String>();
            String line = null;
            try {
                File f1 = file;
                FileReader fr = new FileReader(f1);
                BufferedReader br = new BufferedReader(fr);

                line = br.readLine();
                if (line.contains("package"))
                {
                    lines.add(line);
                    lines.add(StringConstants.NEW_LINE);
                    line = br.readLine();
                }

                lines.add(getImports(StringConstants.IMPORTS));
                lines.add("import steps"+Thread.currentThread().getId()+".*"+StringConstants.NEW_LINE);
                lines.add("import internal" + Thread.currentThread().getId()+".GlobalVariable as GlobalVariable");
                                               
                while ((line = br.readLine()) != null) {
                    if (line.contains("import"))
                    {
                        continue;
                    }
                    if(line.contains("@com.kms.katalon")) {
                    	line = line.replace("@com.kms.katalon.core.annotation", "@com.annotation");
                    }
                    
                    lines.add(line);
                    lines.add(StringConstants.NEW_LINE);
                }

                fr.close();
                br.close();
    
                FileWriter fw = new FileWriter(f1);
                BufferedWriter out = new BufferedWriter(fw);
                for(String s : lines)
                     out.write(s);
                out.flush();
                out.close();
            } catch (Exception ex) {
            	LOGGER.severe(ex.getMessage());
            }

        }
    }
	
	public static String getImports(String[] imports)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i<imports.length;i++)
		{
		    sb.append(imports[i]);
		    sb.append(StringConstants.NEW_LINE);
		}

		return sb.toString();
	}
	

}