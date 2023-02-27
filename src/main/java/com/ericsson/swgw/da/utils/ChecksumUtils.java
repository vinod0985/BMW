package com.ericsson.swgw.da.utils;

/*###################################################################################
#  @Name           :   ChecksumUtils.java
#
#  @Created        :   Oct Drop 2022
#
#  @Description    :   Checksum Util methods
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2210
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      21-Sept-2022            To verify checksums of the downloaded files
######################################################################################*/

import java.io.File;

//import jonelo.jacksum.JacksumAPI;
//import jonelo.jacksum.algorithm.AbstractChecksum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

public class ChecksumUtils {
	
	private static  Logger logger = LogManager.getLogger(ChecksumUtils.class);
			
	private ChecksumUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static String calculateChecksum(byte[] bytes, String checksumType) {
		String returnValue = "";
		try {
			
			  AbstractChecksum checksumInstance = JacksumAPI
			  .getChecksumInstance(checksumType);
			  checksumInstance.setEncoding(AbstractChecksum.HEX);
			  checksumInstance.update(bytes);
			  
			  String formattedValue = checksumInstance.getFormattedValue();
			  checksumInstance.reset(); returnValue = padString(formattedValue, "0", -8);
			 
		} catch (Exception e) {
			logger.error("Could not calculate checksum!" + e.getMessage());
		}
		return returnValue;
	}
	
	public static String calculateChecksum(File file, String checksumType) {
		String returnValue = "";
		try {
			
			  AbstractChecksum checksumInstance = JacksumAPI
			  .getChecksumInstance(checksumType);
			  checksumInstance.setEncoding(AbstractChecksum.HEX);
			  checksumInstance.readFile(file.getCanonicalPath()); String formattedValue =
			  checksumInstance.getFormattedValue(); returnValue = padString(formattedValue,
			  "0", -8);
			 
		} catch (Exception e) {
			logger.error("Could not calculate checksum!" + e.getMessage());
		}
		return returnValue;
	}
	
	private static String padString(String str, String pad, int len) {
		StringBuilder strb = new StringBuilder();

		for (int i = 0; i < Math.abs(len) - str.length(); i++) {
			strb.append(pad);
		}

		return len < 0 ? strb.toString() + str : str + strb.toString();
	}

}
