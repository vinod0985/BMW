package com.ericsson.swgw.da.dms;

import java.io.IOException;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.ericsson.swgw.da.bean.DMSBean;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.utils.HttpClientUtils;

public class DMSService {
		
	private static  Logger logger = LogManager.getLogger(DMSService.class);
	
	public DMSService() {
		throw new IllegalStateException("Service class");
	}
	

	public static int sendDMSNotification(DMSBean dmsBean,String productNumber ) throws IOException, JSONException {	
		
		if(null == dmsBean){
			return 0;
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constants.EUFT,dmsBean.getEuft()); 
		jsonObj.put(Constants.EUFTGROUPNAME, dmsBean.getEuftGroupName()); 
		jsonObj.put(Constants.TICKET, dmsBean.getTicket()); 
		jsonObj.put(Constants.TICKETHEADER, dmsBean.getTicketHeader());
		jsonObj.put(Constants.RELATIVE_STORAGE_PATH, dmsBean.getRelative_storage_path()); 
		jsonObj.put(Constants.SPINNAKER_POLLING_ENABLED, dmsBean.getSpinnaker_polling_enabled()); 
		 
		
		String sDMSurl = IniConfig.getDMSURL();
		if(!sDMSurl.contains(productNumber)){
			sDMSurl = sDMSurl + "/" + productNumber;
		}
		
		logger.info("sDMSurl "+ sDMSurl);
		logger.info("dmsBean "+ jsonObj.toString());
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		HttpPost createPost = new HttpPost();
		/*if (!authorizationHeader.isEmpty()) {
			createPost.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
		}*/

		String contentType = Constants.APPLICATION_JSON;		
		int statusCode = 0;
		try {
			createPost = HttpClientUtils.createPost(sDMSurl, jsonObj.toString());
			createPost.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

			httpClient = HttpClientUtils.createMinimalHttpClient();
			RequestConfig config = getConfigforTimeout();
			createPost.setConfig(config);
			response = httpClient.execute(createPost);
			logger.info("response "+ response);
			statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode "+ statusCode);
			HttpClientUtils.validateStatusCode(statusCode);
		} catch (Exception e) {		
			e.printStackTrace();
		} finally {
			
			if (null != httpClient) 
			{
				httpClient.close();
			}
			
			if (null != response) 
			{
				response.close();
			}
		}
		return statusCode;
	}

	 private static RequestConfig getConfigforTimeout() {
	        int timeout = 2;
	        RequestConfig config = RequestConfig.custom().
	          setConnectTimeout(timeout * 1000).
	          setConnectionRequestTimeout(timeout * 1000).
	          setSocketTimeout(timeout * 1000).build();
	        return config;
	    }
	 
	
}
