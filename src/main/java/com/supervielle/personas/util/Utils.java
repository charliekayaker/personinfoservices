package com.supervielle.personas.util;

import java.io.IOException;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	
	public static String getRequestData(HttpServletRequest request) throws IOException {	
		return  request.getReader().lines().collect(Collectors.joining());
	}

}
