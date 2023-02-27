package com.ericsson.swgw.da.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import com.ericsson.swgw.da.services.PullTicketXMLService;

public class TestINIFileRead {

	public static void main(String[] args) {
		File fileToParse = new File("C:\\projects\\swgwda\\logs\\T-144582.xml");
		try {
			/*
			 * Ini ini = new Ini(fileToParse); String swgwConnectTimeOut= ini.get("swgw",
			 * "connect_timeout");
			 * System.out.println("swgwConnectTimeOut:::::::::"+swgwConnectTimeOut);
			 * 
			 * Map mSwgwConnectTimeOut= ini.get("swgw");
			 * System.out.println("mSwgwConnectTimeOut:::::::::"+mSwgwConnectTimeOut);
			 * 
			 * String retries = (String) mSwgwConnectTimeOut.get("retries");
			 * System.out.println("retries:::::::::"+retries);
			 */
			PullTicketXMLService.collectInputAndSaveAsXMLObject(fileToParse);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	/*public static Map<String, Map<String, String>> parseIniFile(File fileToParse)
			  throws IOException {
			    Ini ini = new Ini(fileToParse);
			    return ini.entrySet().stream()
			      .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
			}*/

}
