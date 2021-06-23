package com.supervielle.personas.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jdk.internal.org.jline.utils.Log;

public class ReflectionHelper {

	public static String getObjectAsPlainText(Object obj) {
		
		if(obj ==null) {
			return "NULL OBJECT";
		}
		
		String className = obj.getClass().getName();
		StringBuilder sb = null;
		
		try {
			
		Class<?> clazz = Class.forName(className);

		Field[] fields = clazz.getDeclaredFields();
		Method m;
		sb = new StringBuilder();

		Class<?> clazzField;
		String nameField;
		String methodName;

		
			int count = 0;
			
			System.out.println("\t\t\t\t" + className+"\n");
			
			for (Field field : fields) {

				clazzField = field.getType();
				nameField = field.getName();
				
				if(nameField.equals("serialVersionUID")) {
					continue;
				}
				sb.append("Field"+ count +" Name : " + nameField);
				sb.append(" Type : " + clazzField.getName());
				
				methodName = "get" + Character.toUpperCase(nameField.charAt(0))
						+ nameField.substring(1, nameField.length());
				
					m = clazz.getMethod(methodName);
				
					sb.append(" Value : " + m.invoke(obj) + " \n");
					count++;				
			}
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassNotFoundException ex) {		
			if(!(ex instanceof NoSuchMethodException))
				ex.printStackTrace();
		} 
		
		return sb.toString();
	}


}
