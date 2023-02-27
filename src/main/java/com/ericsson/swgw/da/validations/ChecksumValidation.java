package com.ericsson.swgw.da.validations;

/*###################################################################################
#  @Name           :   ChecksumValidation.java
#
#  @Created        :   Oct Drop 2022
#
#  @Description    :   To validate checksum for the packages download from SW Gateway
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2210
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      21-Sept-2022            validate checksum for the packages download from SW Gateway
######################################################################################*/

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.ericsson.swgw.da.bean.Checksum;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.exception.ChecksumMismatchException;
import com.ericsson.swgw.da.utils.ChecksumUtils;
import com.ericsson.swgw.da.utils.StringUtils;

public class ChecksumValidation {
	private static  Logger logger = LogManager.getLogger(ChecksumValidation.class);
	private ChecksumValidation() {
	    throw new IllegalStateException(" Validation Class ");
	  }
	
	/**
	 * @param allHeaders  Response Headers received from pull product 
	 * @param pullBoxFile    path where files are downloaded
	 * @param checksum     Received product from webhook payload
	 * @param json 
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static String parseChecksum(Map<String, List<String>> allHeaders,
			File pullBoxFile, Checksum checksum, JSONObject json)
			throws JSONException {
		String checksumMissMatchTicketXml = "";
		String checksumMissMatchResponseHeader = "";
		String checksumMissMatch = "";
		String checksumMissMatchpayload = "";
		String sha256 = ChecksumUtils.calculateChecksum(pullBoxFile,
				Constants.CHECKSUM_SHA256);
		String md5 = ChecksumUtils.calculateChecksum(pullBoxFile,
				Constants.CHECKSUM_MD5);
		String crc32 = ChecksumUtils.calculateChecksum(pullBoxFile,
				Constants.CHECKSUM_CRC32);
		logger.debug("calculateBoxChecksum :: sha256 : " + sha256);
		logger.debug("calculateBoxChecksum :: md5 : " + md5);
		logger.debug("calculateBoxChecksum :: crc32 : " + crc32);
		checksumMissMatchResponseHeader = validateBoxChecksumWithResHeader(
				sha256, md5, crc32, allHeaders);
		checksumMissMatch = checksumMissMatchResponseHeader;

		if (StringUtils
				.isOnlyWhitespaceOrEmpty(checksumMissMatchResponseHeader)) {
			checksumMissMatchTicketXml = validateBoxChecksumWithTicketXmlFile(
					sha256, md5, json);
			checksumMissMatch = checksumMissMatchTicketXml;
		}

		if (StringUtils.isOnlyWhitespaceOrEmpty(checksumMissMatchTicketXml)
				&& null != checksum) {
			checksumMissMatchpayload = validateBoxChecksumWithPayload(sha256,
					md5, checksum);
			checksumMissMatch = checksumMissMatchpayload;
		}
		if (!StringUtils.isOnlyWhitespaceOrEmpty(checksumMissMatch)) {
			pullBoxFile.delete();
			logger.error("Checksum Mismatched for " + pullBoxFile + " due to "
					+ checksumMissMatch);
			raiseChecksumException(pullBoxFile, checksumMissMatch);

		} else {
			logger.info("Checksum Matched with " + pullBoxFile);
		}
		return checksumMissMatch;
	}

	private static String validateBoxChecksumWithPayload(String sha256,
			String md5, Checksum checksum) {
		String payloadMD5 = checksum.getMD5();
		logger.debug("payloadChecksum :: md5 :"+payloadMD5);
		String payloadSHA256 = checksum.getSHA256();
		logger.debug("payloadChecksum :: sha256 :"+payloadSHA256);
		String strChecksumMissMatchPayload = "";
		 if(!sha256.equalsIgnoreCase(payloadSHA256) ){
			 strChecksumMissMatchPayload = "payloadSHA256";
            logger.debug("Checksum Miss Match for :: payloadSHA256" + payloadSHA256);
        } else if(!md5.equalsIgnoreCase(payloadMD5)){
        	strChecksumMissMatchPayload = "payloadMD5";
            logger.debug("Checksum Miss Match for :: payloadMD5 " + payloadMD5);
        }
		 
			return strChecksumMissMatchPayload;
		
	}

	public static String validateBoxChecksumWithTicketXmlFile(String sha256, String md5, JSONObject json) throws JSONException {
		String ticketXmlsHA256 = json.getString(Constants.CHECKSUM_SHA256);
		logger.debug("ticketXmlChecksum :: sha256 :"+ticketXmlsHA256);
		String ticketXmlmD5 = json.getString(Constants.CHECKSUM_MD5);
		logger.debug("ticketXmlChecksum :: md5 :"+ticketXmlmD5);
		String strChecksumMissMatchTicketXml = "";
        if (!sha256.equalsIgnoreCase(ticketXmlsHA256) ) {            
            strChecksumMissMatchTicketXml = "ticketXmlsHA256";
             logger.debug("Checksum Miss Match for :: ticketXmlsHA256" + ticketXmlsHA256);
        } else if (!md5.equalsIgnoreCase(ticketXmlmD5)){
            strChecksumMissMatchTicketXml = "headerMD5";
            logger.debug("Checksum Miss Match for :: ticketXmlmD5" + ticketXmlmD5);
        }
        
		return strChecksumMissMatchTicketXml;
		
	}

	/**
	 * @param sha256      Calculated sha256 value for downloaded product
	 * @param md5         Calculated sha256 value for downloaded product
	 * @param crc32       Calculated sha256 value for downloaded product
	 * @param allHeaders  Response Headers received from pull product
	 * @param filePath    Path where files are downloaded
	 */
	public static String validateBoxChecksumWithResHeader(String sha256, String md5,
			String crc32, Map<String, List<String>> allHeaders) {
		String headerSHA256 ="";
		String headerMD5 = "";
		String headerCRC32 = "";
		
		if(allHeaders.containsKey(Constants.CHECKSUM_SHA256)){
			StringBuilder sbSHA256 = new StringBuilder(allHeaders.get(Constants.CHECKSUM_SHA256).toString());
			sbSHA256.deleteCharAt(sbSHA256.length() - 1);
			sbSHA256.deleteCharAt(0);
			headerSHA256 = sbSHA256.toString();
			logger.debug("pullHeadersChecksum :: sha256 :"+headerSHA256);
		}  
		if(allHeaders.containsKey(Constants.CHECKSUM_CRC32)){
			StringBuilder sbCRC32 = new StringBuilder(allHeaders.get(Constants.CHECKSUM_CRC32).toString());
			sbCRC32.deleteCharAt(sbCRC32.length() - 1);
			sbCRC32.deleteCharAt(0);
			headerCRC32 = sbCRC32.toString();
			logger.debug("pullHeadersChecksum :: crc32 :"+headerCRC32);
		}  
		if(allHeaders.containsKey(Constants.CHECKSUM_MD5)){
			StringBuilder sbMD5 = new StringBuilder(allHeaders.get(Constants.CHECKSUM_MD5).toString());
			sbMD5.deleteCharAt(sbMD5.length() - 1);
			sbMD5.delete(0,6);
			headerMD5 = sbMD5.toString();
			logger.debug("pullHeadersChecksum :: md5 :"+headerMD5);
		}
		
		String strChecksumMissMatch = "";
        if (!sha256.equalsIgnoreCase(headerSHA256) ) {            
            strChecksumMissMatch = "headerSHA256";
            logger.debug("Checksum Miss Match for  :: ResponseHeaderSHA256" + headerSHA256);
        } else if (!md5.equalsIgnoreCase(headerMD5)){
            strChecksumMissMatch = "headerMD5";
            logger.debug("Checksum Miss Match for :: ResponseHeaderMD5" + headerMD5);
        } 
        else if(!crc32.equalsIgnoreCase(headerCRC32)){
            strChecksumMissMatch = "headerCRC32";
            logger.debug("Checksum Miss Match for :: ResponseHeaderCRC32" + headerCRC32);
        } 
		return strChecksumMissMatch;
	}

	/**
	 * @param pullBoxFile
	 * @param strChecksumMissMatch
	 */
	public static void raiseChecksumException(File pullBoxFile, String strChecksumMissMatch) {
		try {
			throw new ChecksumMismatchException(strChecksumMissMatch,pullBoxFile);
		} catch (ChecksumMismatchException e1) {
			e1.printStackTrace();
		}
	}

	public static String validateTicketXmlChecksumWithResHeader(String sha256,
			String md5, Map<String, List<String>> responseHeadersTicket) {

		String headerSHA256 ="";
		String headerMD5 = "";
		
		if(responseHeadersTicket.containsKey(Constants.CHECKSUM_SHA256)){
			StringBuilder sbSHA256 = new StringBuilder(responseHeadersTicket.get(Constants.CHECKSUM_SHA256).toString());
			sbSHA256.deleteCharAt(sbSHA256.length() - 1);
			sbSHA256.deleteCharAt(0);
			headerSHA256 = sbSHA256.toString();
			System.out.println("pullHeadersChecksum :: sha256 :"+headerSHA256);
		} 
		
		if(responseHeadersTicket.containsKey(Constants.CHECKSUM_MD5)){
			StringBuilder sbMD5 = new StringBuilder(responseHeadersTicket.get(Constants.CHECKSUM_MD5).toString());
			sbMD5.deleteCharAt(sbMD5.length() - 1);
			sbMD5.deleteCharAt(0);
			headerMD5 = sbMD5.toString();
			System.out.println("pullHeadersChecksum :: md5 :"+headerMD5);
		}
		
		String strChecksumMissMatch = "";
        if (!sha256.equalsIgnoreCase(headerSHA256) ) {            
            strChecksumMissMatch = "headerSHA256";
            logger.debug("Checksum Miss Match for  :: ResponseHeaderSHA256" + headerSHA256);
        } else if (!md5.equalsIgnoreCase(headerMD5)){
            strChecksumMissMatch = "headerMD5";
            logger.debug("Checksum Miss Match for :: ResponseHeaderMD5" + headerMD5);
        } 
        
		return strChecksumMissMatch;
	
	}
}
