package com.ericsson.swgw.da.services;

/*###################################################################################
 #  @Name           :   AzureTokenService.java
 #
 #  @Created        :   Sept Drop 2022
 #
 #  @Description    :   Azure Token geneeration service
 #
 #  @Programmer     :   Eswar D
 #
 #  @Organization   :   HCL
 #
 #  @Release        :   MR2209
 #
 #  @History        :
 #      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
 #      ZDARESW(Eswar)      21-Sept-2022              Created utils method to authenticate client and secret key
 ######################################################################################*/

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jettison.json.JSONException;

import com.bettercloud.vault.VaultException;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.database.DBUtils;
import com.ericsson.swgw.da.integrations.kms_vault_hvac_integration;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.utils.HttpClientUtils;
import com.ericsson.swgw.da.utils.StringUtils;

public class AzureTokenService {
	
	private static  Logger logger = LogManager.getLogger(AzureTokenService.class);

	private AzureTokenService() {
		throw new IllegalStateException("Service class");
	}
	

	@SuppressWarnings("unchecked")
	public static String getswgwAuthenticate() throws IOException, JSONException {
		String bearerToken = Constants.EMPTY;
		IniConfig iniConfig= new IniConfig();
		String baseUrl = IniConfig.getSwgwBaseURL();
		String clientKey = IniConfig.getSwgwClientKey();
		String secretKey = IniConfig.getSwgwSecretKey();
		
		// Read clientKey & secretKey from KMS Vault
		if(StringUtils.isOnlyWhitespaceOrEmpty(clientKey)|| StringUtils.isOnlyWhitespaceOrEmpty(secretKey)){
			try {
				clientKey = kms_vault_hvac_integration.getClientKey();
				secretKey =  kms_vault_hvac_integration.getSecretKey();
			} catch (VaultException e) {
				logger.error("clientKey,secretKey are not found in kms vault");
			}
		}
		
		// Read clientKey & secretKey from Database
		if(StringUtils.isOnlyWhitespaceOrEmpty(clientKey)|| StringUtils.isOnlyWhitespaceOrEmpty(secretKey)){
			String wEuft = IniConfig.getSwgwEUFT();
			Map<String, String> configureSecrects;
			try {
				configureSecrects = DBUtils.getSecrects(wEuft);
				clientKey = configureSecrects.get(Constants.CLIENT_KEY);
				secretKey = configureSecrects.get(Constants.SECRET_KEY);
			} catch (SQLException e) {
				logger.error("clientKey,secretKey are not found in Data Base");
			}
		}
		
		if(StringUtils.isOnlyWhitespaceOrEmpty(baseUrl)|| StringUtils.isOnlyWhitespaceOrEmpty(clientKey)|| StringUtils.isOnlyWhitespaceOrEmpty(secretKey)){
			logger.debug("Base URl :: "+baseUrl);
			logger.error("Either values of baseUrl,clientKey,secretKey are empty,Please update and retry the operation");
			return bearerToken;
		}
		
		bearerToken = executeHttpPost(baseUrl, clientKey,secretKey);
		
		return bearerToken;
	}

	/**
	 * @param baseUrl
	 * @param clientKey
	 * @param secretKey
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	private static String executeHttpPost(String baseUrl,
			String clientKey, String secretKey) throws IOException,
			JSONException {
		String bearerToken = "";
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		try {
			String content = String.format(Constants.AUTH_CONTENT, clientKey);
			httpClient = HttpClientUtils.createDefaultHttpClient();
			HttpPost createPost = HttpClientUtils.createPost(baseUrl, content);
			createPost.setHeader(Constants.CONTENT_TYPE,
					Constants.APPLICATION_URL_ENCODED);

			String auth = clientKey + Constants.COLON + secretKey;
			String basicAuth = Constants.BASIC_AUTH;
			String basicAuthHeader = String.format(basicAuth,
					new String(Base64.encodeBase64(auth.getBytes())));

			createPost.setHeader(HttpHeaders.AUTHORIZATION, basicAuthHeader);

			response = httpClient.execute(createPost);

			if (HttpClientUtils.isStatusCodeOk(response.getStatusLine()
					.getStatusCode())) {
				String responseString = EntityUtils.toString(response
						.getEntity());
				logger.debug("Status Code is Ok :::::" + responseString);
				org.codehaus.jettison.json.JSONObject jsonResponse = new org.codehaus.jettison.json.JSONObject(
						responseString);
				bearerToken = (String) jsonResponse.get(Constants.ACCESS_TOKEN);
			} else {
				String responseString = EntityUtils.toString(response
						.getEntity());
				logger.error("Status Code is NOT Ok::" + responseString);
			}
		} 
		catch (IOException  ioEx) {
			logger.error("Failed To Generate Bearer Token..");
			logger.error(ioEx.getMessage());
			throw new IOException("Failed To Generate Bearer Token");
		} 
		catch (JSONException e) {
			e.printStackTrace();
			throw new JSONException(e.getMessage());
		} finally {
			if (null != httpClient) {
				httpClient.close();
			}
			
			if (null != response) {
				response.close();
			}
		}
		return bearerToken;
	}

}
