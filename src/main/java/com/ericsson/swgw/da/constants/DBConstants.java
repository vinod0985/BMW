package com.ericsson.swgw.da.constants;

/**
 * 
 *
 */
public class DBConstants {
	
	public DBConstants() {
	    throw new IllegalStateException("Constant class");
	  }
	
	public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	
	public static final String URL = "jdbc:derby:data";
	
	public static final String USER = "DA";
	
	public static final String PASS = "secret";
	
	public static final String CREATE_TRUE = "create=true";
	

}

