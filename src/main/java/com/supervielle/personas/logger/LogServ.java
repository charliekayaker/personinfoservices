package com.supervielle.personas.logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.supervielle.personas.util.ReflectionHelper;
import com.supervielle.personas.util.Utils;

public class LogServ {
	
	private static LogServ INSTANCE = null;	
	
	private LogServ() {
		
	}
	
	public static LogServ getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new LogServ();
		}
		return INSTANCE;
	}
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException  { // Es singletón, no se puede clonar.		
		throw new CloneNotSupportedException();		
	}
	

	/**
	 * this method log request for <b>POSTService</b>
	 * 
	 * @author charlie_alfon
	 * */
	public void LogPRequest(HttpServletRequest request, Logger logger) {
		if (request != null)
			try {
				logger.info("Request : " + Utils.getRequestData(request));
			} catch (IOException e) {
				logger.error(e);
			}
	}
	
	/**
	 * this method log request for <b>GETService</b>
	 * it's log only the endpoint path with a mediation begin.
	 * @author charlie_alfon
	 * */

	public void LogGRequest(String request, Logger logger) {
		if (request != null)
			logger.info("Request GETEndPoint : " + request);

	}
	
	/**
	 * This method log response and mediation time in ms. 
	 * 
	 * @author charlie_alfon
	 * */

	public void serviceLogResponse(Object response, long mediationTime, Logger logger) {
		if (response != null) {
			logger.info("Response : ");
			if (response instanceof ArrayList) {

				for (int n = 0, size = ((List<?>) response).size(); n < size; n++) {
					logger.info(ReflectionHelper.getObjectAsPlainText(((List<?>) response).get(n)));
				}
			}else if(response instanceof String) {
				logger.info(response);
			} else {

				logger.info(ReflectionHelper.getObjectAsPlainText(response));
			}
		}
			logger.info("Medation time : " + mediationTime + " ms.");
	}
	

}
