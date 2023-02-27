package com.ericsson.swgw.da.utils;

/*###################################################################################
#  @Name           :   HttpClientUtils.java
#
#  @Created        :   Sep Drop 2022
#
#  @Description    :   To create http methods
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2209
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      29-Aug-2022            Use of http methods
######################################################################################*/

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HttpClientUtils {
	
	private static  Logger logger = LogManager.getLogger(HttpClientUtils.class);

	private HttpClientUtils() {
	}

	public static HttpGet createGet(String url) throws ClientProtocolException,
			IOException {
		HttpGet httpget = new HttpGet(url);

		return httpget;
	}

	public static HttpPost createPost(String postUrl, String content)
			throws ClientProtocolException, IOException {
		HttpPost httppost = new HttpPost(postUrl);
		EntityBuilder builder = EntityBuilder.create();
		builder.setText(content);
		HttpEntity entity = builder.build();
		httppost.setEntity(entity);
		return httppost;
	}

	public static HttpPost createPost(String postUrl, File file)
			throws ClientProtocolException, IOException {
		HttpPost httppost = new HttpPost(postUrl);
		EntityBuilder builder = EntityBuilder.create();
		builder.setFile(file);
		HttpEntity entity = builder.build();
		httppost.setEntity(entity);
		return httppost;
	}

	public static CloseableHttpClient createDefaultHttpClient()
			throws ClientProtocolException, IOException {
		return HttpClients.createDefault();
	}

	public static CloseableHttpClient createMinimalHttpClient()
			throws ClientProtocolException, IOException {
		logger.info("About to create closable http clinet with minimal");
		return HttpClients.createMinimal();
	}

	public static boolean isStatusCodeOk(int statusCode) {
		if (statusCode < 200 || statusCode > 206) {
			return false;
		} else {
			return true;
		}

	}
	
	 public static void validateStatusCode(int status) throws Exception {
	        if (status < 200 || status > 206) {
	            /*System.out.println("Could not execute the http request due to status code '" + status
	                    + "' is returned from other system");*/
	            throw new Exception("Could not execute the http request due to status code '"
	                    + status + "' is returned from other system");
	        }


	   }



}
