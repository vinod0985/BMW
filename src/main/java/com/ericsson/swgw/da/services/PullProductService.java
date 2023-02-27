package com.ericsson.swgw.da.services;

/*###################################################################################
#  @Name           :   PullProductService.java
#
#  @Created        :   Sep Drop 2022
#
#  @Description    :   To  pull SW packages from SW Gateway
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2209
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      29-Aug-2022            Download action
######################################################################################*/

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import com.ericsson.swgw.da.bean.Checksum;
import com.ericsson.swgw.da.bean.Webhook;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.utils.PullUtils;
import com.ericsson.swgw.da.utils.StringUtils;

public class PullProductService {
	
	private static  Logger logger = LogManager.getLogger(PullProductService.class);

	private static Webhook webhook ;
	
	
	public PullProductService (Webhook lWebhook){
		webhook = lWebhook;
	}
	
	/**
	 * @param sbearerToken
	 * @param string2 
	 * @param filePath 
	 * @param productVersion 
	 * @param productNumber 
	 * @param checksum 
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public void pullProduct(String filePath, String apiUrl, String productNumber, String productVersion, Checksum checksum) throws Exception {
		String fileName = productNumber + "-" + productVersion +".zip";
		String pullURL = getPullProductURL(productNumber, productVersion, apiUrl);
		
		if (!StringUtils.isOnlyWhitespaceOrEmpty(pullURL)) {
					
			@SuppressWarnings("rawtypes")
			Map returnMap = PullUtils.pullMethod(pullURL, fileName, filePath);
			logger.debug("downloadDir::::::::::::::"+filePath);
			java.io.File file =  null;
			if(returnMap.containsKey("file")){
				file = (File) returnMap.get("file");
			}else {
				file = new java.io.File(filePath + Constants.BACK_SLASH+ fileName);
			}
			
			if(returnMap.containsKey("headerFields")){
				Map<String, List<String>> responseHeaders = (Map<String, List<String>>) returnMap.get("headerFields");
			}
		}
	}

	/**
	 * @param product
	 * @param apiUrl 
	 * @return
	 */
	private static String getPullProductURL(String productNumber, String productVersion, String apiUrl) {
		
		
		StringBuilder pullProductBuilder = new StringBuilder();
		pullProductBuilder.append(apiUrl);
		pullProductBuilder.append(Constants.PULL_PRODUCT_URL);
		pullProductBuilder.append(Constants.PRODUCT_NO_EQUAL_TO);
		pullProductBuilder.append(productNumber);
		pullProductBuilder.append(Constants.AMPERSAND);
		pullProductBuilder.append(Constants.RSTATE_NO_EQUAL_TO);
		pullProductBuilder.append(productVersion);
		pullProductBuilder.append(Constants.AMPERSAND);
		pullProductBuilder.append(Constants.TICKET_ID_EQUAL_TO);
		pullProductBuilder.append( webhook.getTicket());
		pullProductBuilder.append(Constants.AMPERSAND);
		pullProductBuilder.append(Constants.EUFT_NO_EQUAL_TO);
		pullProductBuilder.append(webhook.getEuft());

		return pullProductBuilder.toString();				
		
	}

	
	public Map<String, Object> pullProduct(String filePath, String productNumber, String productVersion) throws  JSONException, SQLException {
		String boxName = "";
		StringBuilder sbProdFileName = new StringBuilder();
		sbProdFileName.append(productNumber);
		sbProdFileName.append("-");
		sbProdFileName.append(productVersion);
		sbProdFileName.append(".zip");
		String fileName = sbProdFileName.toString();
		boxName = fileName.replace("/", "_").replaceAll("\\s", "");
		String pullURL = getPullProductURL(productNumber, productVersion, IniConfig.getSwgwApiURL());
		
		if (!StringUtils.isOnlyWhitespaceOrEmpty(pullURL)) {
			logger.debug("pull Product ::::::"+pullURL);
			Map<String, Object> returnMap = PullUtils.pullMethod(pullURL, boxName, filePath);
			return returnMap;
		}
		return null;
	}
	
}
