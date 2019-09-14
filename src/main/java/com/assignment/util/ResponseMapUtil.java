package com.assignment.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class ResponseMapUtil {
	public static <T> T getUserNotAvailableError(){
	    Map<String, Object> map = new LinkedHashMap<>();    
	    map.put("result_code", 501);
	    map.put("result", "User Not Available"); 
	    return (T) map;
	  }  
	  public static <T> T getSuccessResult(){
	    Map<String, Object> map = new LinkedHashMap<>();    
	    map.put("result_code", 0);
	    map.put("result", "success"); 
	    return (T) map;
	  }  
	  public static <T> T getSuccessResult(Object obj){
	    Map<String, Object> map = new LinkedHashMap<>();    
	    map.put("result_code", 0);
	    map.put("result", "success");
	    map.put("value", obj);
	    return (T) map;
	  }
	public static <T> T getNoUserAvailableError() {
		Map<String, Object> map = new LinkedHashMap<>();    
	    map.put("result_code", 502);
	    map.put("result", "No User Available"); 
	    return (T) map;
	}
	public static <T> T getFailureResult(Object obj) {
		Map<String, Object> map = new LinkedHashMap<>();    
	    map.put("error_code",503);
	    map.put("error_msg", obj); 
	    return (T) map;
	}
	
}
