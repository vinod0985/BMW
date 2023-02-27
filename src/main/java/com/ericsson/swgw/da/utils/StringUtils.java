package com.ericsson.swgw.da.utils;

public class StringUtils {
	public static final String EMPTY_STRING = "";
	
	public static boolean isOnlyWhitespaceOrEmpty(String s) {
	      if (s == null) {
	         return true;
	      } else {
	         for(int i = 0; i < s.length(); ++i) {
	            if (!Character.isWhitespace(s.charAt(i))) {
	               return false;
	            }
	         }

	         return true;
	      }
	   }

}
 