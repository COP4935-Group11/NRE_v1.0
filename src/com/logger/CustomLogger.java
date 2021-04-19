package com.logger;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


import com.constants.StringConstants;

public class CustomLogger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static private FileHandler fileHTML;
    static private Formatter formatterHTML;

    static public void setup() throws IOException {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MMddyyyy_HHmmss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String currentTime = java.time.LocalDateTime.now().format(timeFormatter);
        String logFolder = StringConstants.LOGS_DIR + StringConstants.ID_SEPARATOR + java.time.LocalDate.now().format(dateFormatter) 
        					+ StringConstants.ID_SEPARATOR + currentTime;
        if(!(new File(logFolder).exists())) 
        	new File(logFolder).mkdirs();        	
        fileTxt = new FileHandler(logFolder + StringConstants.ID_SEPARATOR + "log" + StringConstants.UNDERSCORE 
        							+ currentTime + ".txt");
        fileHTML = new FileHandler(logFolder + StringConstants.ID_SEPARATOR + "log" + StringConstants.UNDERSCORE 
        							+ currentTime + ".html");
       
        	
        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

        // create an HTML formatter
        formatterHTML = new MyHtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);
    }
}
