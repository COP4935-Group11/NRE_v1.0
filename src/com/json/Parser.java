package com.json;
 
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.configuration.RunConfiguration;
 
public class Parser 
{
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	protected static Feature feature;
    @SuppressWarnings("unchecked")
    public static void startJson(String path) 
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try {
        	FileReader reader = new FileReader(path);
       
            //Read JSON file
            Object obj = jsonParser.parse(reader);
                  
            JSONArray objectList = (JSONArray) obj;
//            System.out.println(objectList);
             

            feature = new Feature();

            objectList.forEach( o -> parseObject( (JSONObject) o ) );

        }catch(Exception e) {
        	LOGGER.severe(e.getMessage());
        }

    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static void parseObject(JSONObject object) 
    {
//    	System.out.println("--------------------JSON---------------------------");
        // getting address
        Map lines = ((Map<?, ?>)object);
          
        // iterating address Map
        Iterator<Map.Entry> itr1 = lines.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();
            if(pair.getKey().toString().equalsIgnoreCase("elements")) {
            	
            	
            	JSONParser parser = new JSONParser();  
            	Object obj = null;
        		try {
        			obj = parser.parse(object.get("elements").toString());
        			
        		} catch (ParseException e) {
        			
        			LOGGER.severe(e.getMessage());
        		}                
                JSONArray objectList = (JSONArray) obj; 
                objectList.forEach( o -> parseElements( (JSONObject) o ) );
		            
            }
            else
            {
//           	System.out.println(pair.getKey() + " : " + pair.getValue());
            	if(pair.getKey().toString().equals("name"))
            	{
//            		System.out.println("Scenario:" + pair.getValue());
            		feature.setName(pair.getValue().toString());
            	}
            }
        }
        
//        System.out.println("-----------------------------------------------");
        
//        for(Scenario scenario : feature.getScenarios())
//        {
//        	System.out.println("Scenario name: " + scenario.name);
//        	for(Step step: scenario.getSteps())
//        	{
//        		System.out.println("Steps:");
//        		System.out.println(step.type);
//        		System.out.println(step.name);
//        		System.out.println(step.result);
//        	}
//        }
        RunConfiguration.getTestSuiteObj().getCurrentTestCase().getCucumberFeatures().add(feature);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static void parseElements(JSONObject object)
    {
    	Scenario scenario = new Scenario();
//    	System.out.println("\t--------------------ELEMENTS---------------------------");
        // getting address
        Map lines = ((Map<?, ?>)object);
          
        // iterating address Map
        Iterator<Map.Entry> itr1 = lines.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();
            if(pair.getKey().toString().equalsIgnoreCase("steps")) {
            	
            	JSONParser parser = new JSONParser();  
            	Object obj = null;
        		try {
        			obj = parser.parse(object.get("steps").toString());
        		} catch (ParseException e) {

        			LOGGER.severe(e.getMessage());
        		}                
                JSONArray objectList = (JSONArray) obj; 
                objectList.forEach( o -> parseSteps( (JSONObject) o , scenario) );
		            
            }
            else
            {
//            	System.out.println("\t"+pair.getKey() + " : " + pair.getValue());
            	if(pair.getKey().toString().equals("name"))
            	{
//            		System.out.println("Scenario:" + pair.getValue());
            		scenario.name = pair.getValue().toString();
            	}
            }
        }
        
       feature.addScenario(scenario);
        
//        System.out.println("\t-----------------------------------------------");	   	   	
                    	    

    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static void parseSteps(JSONObject object, Scenario scenario)
    {
    	Step step = new Step();
//    	System.out.println("\t\t--------------------STEPS---------------------------");
        // getting address
        Map lines = ((Map<?, ?>)object);
          
        // iterating address Map
        Iterator<Map.Entry> itr1 = lines.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();
            if(pair.getKey().toString().equalsIgnoreCase("result")) {
            	
            	parseStepsResult((Map<?, ?>)pair.getValue(), step);
            	
            }
            else if(pair.getKey().toString().equalsIgnoreCase("match")){
            	
            	parseStepsMatch((Map<?, ?>)pair.getValue());
            }
            else
            {
//            	1.out.println("\t\t"+pair.getKey() + " : " + pair.getValue());
            	
            	if(pair.getKey().toString().equals("name"))
            	{
//            		System.out.println("Step Name:" + pair.getValue());
            		step.name = pair.getValue().toString();
            	}
            	if(pair.getKey().toString().equals("keyword"))
            	{
//            		System.out.println("Type:" + pair.getValue());
            		step.type = pair.getValue().toString();
            	}
            }
        }
        
        scenario.addStep(step);
        
//        System.out.println("\t\t-----------------------------------------------");	   	   	
                    	    

    }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
		private static void parseStepsResult(Map result, Step step) {
        	        	
//        	System.out.println("\t\t\t---------Result----------");
        	
        	Iterator<Map.Entry> itr = result.entrySet().iterator();
        	
        	while (itr.hasNext()) {
                Map.Entry resultPair = (Entry) itr.next();
            if(resultPair.getKey().toString().equals("status"))
    		{
            	step.result = resultPair.getValue().toString();
            	
    		}else if(resultPair.getKey().toString().equals("error_message"))
    		{
    			step.failure = resultPair.getValue().toString();
    		}
//        	System.out.println("\t\t\t"+resultPair.getKey() + " : " + resultPair.getValue());
        	}
        	
//        	System.out.println("\t\t\t-------------------------");
        }
    
        @SuppressWarnings({ "rawtypes", "unchecked" })
		private static void parseStepsMatch(Map match) {
        	        	
//        	System.out.println("\t\t\t---------Match----------");
        	
        	Iterator<Map.Entry> itr = match.entrySet().iterator();
        	
        	while (itr.hasNext()) {
                @SuppressWarnings("unused")
				Map.Entry matchPair = (Entry) itr.next();
//        	System.out.println("\t\t\t"+matchPair.getKey() + " : " + matchPair.getValue());
        	}
        	
//        	System.out.println("\t\t\t-------------------------");
        }
    
}