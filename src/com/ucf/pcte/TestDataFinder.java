package com.ucf.pcte;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.configuration.RunConfiguration;
import com.constants.StringConstants;
import com.exception.StepFailedException;

//import Reporter.Logger;

public class TestDataFinder {
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@SuppressWarnings("unused")
	public static  TestData findTestData(String fileName)
	{
		String type = null;
		String path = null;
		String seperator = null;
		boolean isInternal = true;
		boolean contHeader = true;
		
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(RunConfiguration.getProjectDir() + StringConstants.ID_SEPARATOR
												+ StringConstants.DATA_FILES_SOURCE + StringConstants.ID_SEPARATOR + fileName + StringConstants.DATA_FILES_EXT));
			document.getDocumentElement().normalize();
			Element root = document.getDocumentElement();
			NodeList nList = document.getElementsByTagName("DataFileEntity");
			
			for (int temp = 0; temp < nList.getLength(); temp++)
			{
			 Node node = nList.item(temp);
			 if (node.getNodeType() == Node.ELEMENT_NODE)
			 {
				Element eElement = (Element) node;
			    type = eElement.getElementsByTagName("driver").item(0).getTextContent();
			    path = eElement.getElementsByTagName("dataSourceUrl").item(0).getTextContent();
			    seperator = eElement.getElementsByTagName("csvSeperator").item(0).getTextContent();
			    isInternal = Boolean.parseBoolean(eElement.getElementsByTagName("isInternalPath").item(0).getTextContent());
			    contHeader = Boolean.parseBoolean(eElement.getElementsByTagName("containsHeaders").item(0).getTextContent());
			 }
			}
      
			
		}catch (Exception e)
		{
			LOGGER.severe(e.getMessage());
			throw new StepFailedException(e.getLocalizedMessage());
		}
		
		
		return new TestData(path, type, contHeader, isInternal, seperator);
	}
	
}