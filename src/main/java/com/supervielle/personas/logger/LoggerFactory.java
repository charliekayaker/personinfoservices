package com.supervielle.personas.logger;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


public class LoggerFactory {
	
	
	   public static Logger getLogger(@SuppressWarnings("rawtypes") Class clazz) {
	    	 
	    	Logger logger = Logger.getLogger(clazz);
	    	logger.setAdditivity(false);
	    	PatternLayout layout = new PatternLayout();
	        
	    	String conversionPattern = "%-7p %d [%t] %c %x - %m%n";
	        layout.setConversionPattern(conversionPattern);  
	        
	        ConsoleAppender consoleAppender = new ConsoleAppender();
	        consoleAppender.setLayout(layout);
	        consoleAppender.activateOptions();
	    	
	    	BasicConfigurator.configure();
	    	logger.addAppender(consoleAppender);
	    	return logger;
	    }
	   
	   
	 
}
