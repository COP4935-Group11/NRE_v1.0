package com.ucf.pcte;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import com.configuration.RunConfiguration;
import com.constants.StringConstants;
import com.exception.StepFailedException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;


public class TestData {
	
	protected String type = null;
	protected String path = null;
	protected String seperator = null;
	protected boolean isInternal = true;
	protected boolean contHeader = true;
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public TestData(String path, String type, Boolean contHead, Boolean isInternal, String seperator)
	{
		this.path = path;
		this.type = type;
		this.contHeader = contHead;
		this.isInternal = isInternal;
		this.seperator = seperator;
	}
	
	public Object getValue(int column, int row)
	{
		Object value = null;
		
		if(isInternal == true)
		{
			path = RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR + path;
		}
		
		switch (type)
		{
			case "CSV":
				return getCSVData(column, row);
				
			default:
				break;
		}
		
		
		return value;
		
	}
	
	public Object getValue(String column, int row)
	{
		Object value = null;
		
		if(isInternal == true)
		{
			path = RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR + path;
		}
		
		switch (type)
		{
			case "CSV":
				return getCSVData(column, row);
				
			default:
				break;
		}
		
		
		return value;
		
	}
	
	public Object getCSVData(int column, int row)
	{
		
		 try { 
		        FileReader filereader = new FileReader(path); 
		  
		        CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
		   
		        CSVReader csvReader = new CSVReaderBuilder(filereader) 
		                                  .withCSVParser(parser) 
		                                  .build(); 
		  
		        List<String[]> allData = csvReader.readAll();
		        
		        return allData.get(row)[column - 1];
		        
		 }catch(Exception e)
		 {
			 LOGGER.severe(e.getMessage());
			 throw new StepFailedException(e.getLocalizedMessage());
			
		 }
		
	}
	
	public Object getCSVData(String column, int row)
	{
		
		 try { 
		        FileReader filereader = new FileReader(path); 
		  
		        CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
		   
		        CSVReader csvReader = new CSVReaderBuilder(filereader) 
		                                  .withCSVParser(parser) 
		                                  .build(); 
		  
		        List<String[]> allData = csvReader.readAll();

		        return allData.get(row)[getColumnIndex(column,allData)];
		        
		 }catch(Exception e)
		 {
			 LOGGER.severe(e.getMessage());
			 throw new StepFailedException(e.getLocalizedMessage());
			
		 }
		
	}
	
	public int getColumnIndex(String column, List<String[]> allData)
	{
		for(int i = 0; i < allData.size(); i++)
		{
			if(allData.get(0)[i].equals(column)) 
			{
				return i;
			}
		}		
		
		return 0;
	}
	
	
	public int getRowNumbers()
	{
		FileReader filereader = null;
		try {
			filereader = new FileReader(path);
		} catch (FileNotFoundException e) {
			
			LOGGER.severe(e.getMessage());
			throw new StepFailedException(e.getLocalizedMessage());
		} 
		  
        CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
   
        CSVReader csvReader = new CSVReaderBuilder(filereader) 
                                  .withCSVParser(parser) 
                                  .build(); 
        List<String[]> allData = null;
        try {
			allData = csvReader.readAll();
		} catch (IOException | CsvException e) {
			
			LOGGER.severe(e.getMessage());
			throw new StepFailedException(e.getLocalizedMessage());
		}
        
		return allData.size();
	}

}