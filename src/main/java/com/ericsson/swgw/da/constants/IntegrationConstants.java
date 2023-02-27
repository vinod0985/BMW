package com.ericsson.swgw.da.constants;

public class IntegrationConstants {
	
	public  IntegrationConstants() {
	    throw new IllegalStateException("Constant class");
	  }

	 public static final String VAULT_SERVER_PATH = "https://eric-sec-key-management:8200";
	 public static final String TOKEN = "s.E3BRk50CvwMf7nWA0of9qsQB";
	 
	//path The Vault key value to which to write (e.g. secret/hello)
	 public static final String  PATH = "secret/my-app";
	 
	//The Vault key names
	 public static final String  CLIENT_KEY = "client_key";
	 public static final String  SECRET_KEY = "secret_key";
}
