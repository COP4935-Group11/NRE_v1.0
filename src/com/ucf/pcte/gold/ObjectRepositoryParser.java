package com.ucf.pcte.gold;

import java.util.*;
import java.util.logging.Logger;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ObjectRepositoryParser
{

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
  private static int webElementXpathsCount = -1;
  private static boolean recordName = false;
  public static ArrayList<webElementXpaths> web = new ArrayList<webElementXpaths>();

  public static void print(NodeList nodeList)
  {
    for (int i = 0; i < nodeList.getLength(); i++)
    {
    	Node temp = nodeList.item(i);
    	if (temp.getNodeType() == Node.ELEMENT_NODE)
    	{

        String s = temp.getNodeName();
        //Adds to array list to create the Xpaths list, there can be multiple xpath elements and this handles it
        switch(s)
        {
          case "webElementXpaths":
            webElementXpaths newWeb = new webElementXpaths();
            web.add(newWeb);
            webElementXpathsCount++;
            recordName = true;
            break;
          case "isSelected":
            if (recordName == false)
            {
              break;
            }
            web.get(webElementXpathsCount).isSelected = Boolean.parseBoolean(temp.getTextContent());
            break;
          case "matchCondition":
            if (recordName == false)
            {
              break;
            }
            web.get(webElementXpathsCount).matchCondition = temp.getTextContent();
            break;
          case "name":
            if (recordName == false)
            {
              break;
            }
            web.get(webElementXpathsCount).name = temp.getTextContent();
            break;
          case "type":
            if (recordName == false)
            {
              break;
            }
            web.get(webElementXpathsCount).type = temp.getTextContent();
            break;
          case "value":
            if (recordName == false)
            {
              break;
            }
            web.get(webElementXpathsCount).value = temp.getTextContent();
            break;
        }
        //System.out.println("\nNode Name = " + temp.getNodeName() + " [OPEN]");
        //System.out.println("Node Value = " + temp.getTextContent());

        if (temp.hasChildNodes())
        {
          print(temp.getChildNodes());
        }
        //System.out.println("Node Name = " + temp.getNodeName() + " [CLOSE]");
      }
    }
  }

  //call this function to print and return a list of the strings of the xpath values
  public static String getXpath (String s)
  {

    String Xpath = "";
    try
    {
      //File file = new File("Sample.xml");
      File file = new File(s);

      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

      Document doc = db.parse(file);

      //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

      if (doc.hasChildNodes())
      {
        print(doc.getChildNodes());
      }


      for (webElementXpaths w : web)
      {
        if (w.isSelected)
        {
          Xpath = w.value;
        }
      }

      /*for (webElementXpaths w : web)
      {
        System.out.println(w.isSelected);
        System.out.println(w.matchCondition);
        System.out.println(w.name);
        System.out.println(w.type);
        System.out.println(w.value);
      }*/

    }
    catch (Exception e)
    {
      LOGGER.severe(e.getMessage());
    }
    //Comment this section out if you dont want it printing
//    System.out.println(Xpath);

    return Xpath;
  }

  public ObjectRepositoryParser()
  {
    // empty boy
  }
  /*public static void main (String [] args)
  {
    try
    {
      //File file = new File("Sample.xml");
      File file = new File("a_Grafana_SignOut.rs");

      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

      Document doc = db.parse(file);

      System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

      if (doc.hasChildNodes())
      {
        print(doc.getChildNodes());
      }

      for (webElementXpaths w : web)
      {
        System.out.println(w.isSelected);
        System.out.println(w.matchCondition);
        System.out.println(w.name);
        System.out.println(w.type);
        System.out.println(w.value);
      }

    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
    }
  }*/

}
