package com.ericsson.swgw.da.services;

/*###################################################################################
#  @Name           :   PullTicketXMLService.java
#
#  @Created        :   Oct Drop 2022
#
#  @Description    :   To pull ticket XML from SW Gateway
#
#  @Programmer     :   Rajni Kumari
#
#  @Organization   :   HCL
#
#  @Release        :   MR2210
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZUAMJRK(Rajni)      21-sept-2022            Download action
######################################################################################*/

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import com.ericsson.swgw.da.bean.Webhook;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.swgw.bean.TicketXMLGenerator;
import com.ericsson.swgw.da.utils.PullUtils;
import com.ericsson.swgw.da.utils.StringUtils;

public class PullTicketXMLService {

	private static  Logger logger = LogManager.getLogger(PullTicketXMLService.class);
	public static Webhook webhook;
	public static String bearerToken = "";
	//private static  LoadProperties loadProperties;
	private static  IniConfig iniConfig;

	public PullTicketXMLService(Webhook lWebhook) {
		webhook = lWebhook;		
	}
	
	public PullTicketXMLService(Webhook lWebhook, IniConfig liniConfig) {
		webhook = lWebhook;
		iniConfig = liniConfig;
		
	}

	/**
	 * @param sbearerToken
	 * @param string
	 * @param filePath
	 * @throws JSONException 
	 * @throws SQLException 
	 */

	public void pullTicketxml(String filePath,String apiUrl) throws JSONException, SQLException {
		
		
			String fileName = webhook.getTicket() + Constants.XML_EXT;

			String pullTicketURL = getPullTicketxmlURL(apiUrl);

			if (!StringUtils.isOnlyWhitespaceOrEmpty(pullTicketURL)) {
				PullUtils.pullMethod(pullTicketURL, fileName, filePath);
			}
		
	}
	
	/**
	 * @param sbearerToken
	 * @param string
	 * @param filePath
	 * @throws JSONException 
	 * @throws SQLException 
	 */

	public Map<String, Object> pullTicketxml(String filePath,String fileName,String pullTicketURL) throws JSONException, SQLException {
		System.out.println("pullTicketURL ::::"+pullTicketURL);
		if (!StringUtils.isOnlyWhitespaceOrEmpty(pullTicketURL)) {			
			Map<String, Object> returnMap = PullUtils.pullMethod(pullTicketURL, fileName, filePath);
			return returnMap;			
		}		
		return null;	
	}
	
	public static TicketXMLGenerator getTicketXMLGenerator(File file){
		
		if(file != null && file.exists()){
			TicketXMLGenerator ticketXML = collectInputAndSaveAsXMLObject(file);
			return ticketXML;
		}else {
			return null;
			//logger.error("Ticket XML Not found in the temporary location ::" + logsPath + Constants.BACK_SLASH + fileName );
		}	
	}

	public String getPullTicketxmlURL(String apiUrl) {
		
		StringBuilder ticketXmlBuilder = new StringBuilder();
		ticketXmlBuilder.append(apiUrl);
		ticketXmlBuilder.append(Constants.TICKET_XML_URL);
		ticketXmlBuilder.append(Constants.AMPERSAND);
		ticketXmlBuilder.append(Constants.EUFT_EQUAL_TO);
		ticketXmlBuilder.append( webhook.getEuft());
		ticketXmlBuilder.append(Constants.AMPERSAND);
		ticketXmlBuilder.append(Constants.TICKET_EQUAL_TO);
		ticketXmlBuilder.append(webhook.getTicket());

		return ticketXmlBuilder.toString();
		
	}
	
	
	public static TicketXMLGenerator collectInputAndSaveAsXMLObject(File file){
		try{
			JAXBContext jaxbContext1 = JAXBContext.newInstance(TicketXMLGenerator.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext1.createUnmarshaller();
			TicketXMLGenerator ticketXML = (TicketXMLGenerator) jaxbUnmarshaller.unmarshal(file);
			return ticketXML;
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("::::::::::::::::::::::::" + e.getMessage());
		}
		
		return null;		
	}
}