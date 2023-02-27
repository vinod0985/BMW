package com.ericsson.swgw.da.services;

/*###################################################################################
#  @Name           :   Referencedocurls.java
#
#  @Created        :   
#
#  @Description    :   
#
#  @Programmer     :  
#
#  @Organization   :   
#
#  @History        :
#
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   Description
#
#
####################################################################################*/

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import com.ericsson.swgw.da.bean.Webhook;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.swgw.bean.SWGReferenceDocs;
import com.ericsson.swgw.da.utils.PullUtils;
import com.ericsson.swgw.da.utils.StringUtils;



public class Referencedocurls {
	
	private static  Logger logger = LogManager.getLogger(Referencedocurls.class);
	public static Webhook webhook;
	private static final String currentDirectory = System.getProperty(Constants.USER_DIR);
	
	public Referencedocurls(Webhook lWebhook) {
		webhook = lWebhook;
	}
	
	
	/**
	 * @param sbearerToken
	 * @param string
	 * @param filePath
	 * @throws SQLException 
	 * @throws JSONException 
	 * @throws JAXBException 
	 */

	public SWGReferenceDocs getRefdocurls(String apiUrl, String productNumber,String productVersion) throws JSONException, SQLException, JAXBException{
		logger.debug("productNumber ::::"+productNumber);
		logger.debug("productVersion ::::"+productVersion);
		String refDocURL = getRefDocURL(apiUrl, productNumber, productVersion);
		logger.debug("refDocURL:::::::::"+refDocURL);
		if (!StringUtils.isOnlyWhitespaceOrEmpty(refDocURL)) {
			String sFileName = getTempFileName();
			String sLogsPath = getLogsPath();			
			
			Map<String, Object> returnMap = PullUtils.pullMethod(refDocURL,sFileName ,sLogsPath);
			System.out.println("downloadDir::::::::::::::"+sLogsPath);
			java.io.File file =  null;
			if(returnMap.containsKey("file")){
				file = (File) returnMap.get("file");
			}else {
				file = new java.io.File(sLogsPath + Constants.BACK_SLASH+ sFileName);
			}
			
			System.out.println("getRefdocurls getAbsolutePath() ::::"+file.getAbsolutePath());
			if(file.exists()){
				SWGReferenceDocs refDocXML = null;
				try{
					refDocXML = collectInputAndSaveAsXMLObject(file);
				}catch (Exception e) {
					e.printStackTrace();
				} finally {
					file.delete();
				}
				
				return refDocXML;
			}else {
				return null;
				//logger.error("Ticket XML Not found in the temporary location ::" + logsPath + Constants.BACK_SLASH + fileName );
			}	
		}				
		return null;	
	}
	
	private SWGReferenceDocs collectInputAndSaveAsXMLObject(File file) throws JAXBException {
			JAXBContext jaxbContext1 = JAXBContext.newInstance(SWGReferenceDocs.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			SWGReferenceDocs refDocXML = (SWGReferenceDocs) jaxbUnmarshaller.unmarshal(file);
			return refDocXML;
	}


	public String getRefDocURL(String apiUrl,String productNumber,String productVersion) {
		
		
		StringBuilder refDocBuilder = new StringBuilder();
		refDocBuilder.append(apiUrl);
		refDocBuilder.append(Constants.REF_DOC_URL);
		refDocBuilder.append(Constants.AMPERSAND);
		refDocBuilder.append(Constants.PRODUCT_NO_EQUAL_TO);
		refDocBuilder.append(productNumber);
		refDocBuilder.append(Constants.AMPERSAND);
		refDocBuilder.append(Constants.RSTATE_NO_EQUAL_TO);
		refDocBuilder.append(productVersion);
		refDocBuilder.append(Constants.AMPERSAND);
		refDocBuilder.append(Constants.EUFT_NUMBER_EQUAL_TO);
		refDocBuilder.append( webhook.getEuft());
		refDocBuilder.append(Constants.AMPERSAND);
		refDocBuilder.append(Constants.TICKET_ID_EQUAL_TO);
		refDocBuilder.append(webhook.getTicket());

		return refDocBuilder.toString();
	}
	
	private static String getTempFileName() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Long lTimeStamp = timestamp.getTime();
		
		StringBuilder sb = new StringBuilder();
		sb.append("ReferenceDoc");
		sb.append("_");		
		sb.append(lTimeStamp.toString());
		sb.append(Constants.XML_EXT);
		
		logger.debug("TempFileName::::::::::::::"+sb.toString());
		return sb.toString();
	}

	public static String getLogsPath(){
		return currentDirectory + Constants.BACK_SLASH + "logs";
		
	}
}
