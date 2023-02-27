/*package com.ericsson.swgw.da.properties;

###################################################################################
#  @Name           :   LoadProperties.java
#
#  @Created        :   Sep Drop 2022
#
#  @Description    :   To load properties from  configurations file  
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2209
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      29-Aug-2022            created for load properties from configuration file
######################################################################################

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.utils.StringUtils;

public class LoadProperties {
	private static String currentDirectory = System
			.getProperty(Constants.USER_DIR);
	private static String configPath = currentDirectory + "/configurations";

	@Value("${download_dir}")
	private String downloadDir;

	@Value("${base_url}")
	private String baseUrl;

	@Value("${api_url}")
	private String apiUrl;

	private String clientKey;

	private String secretKey;

	@Value("${retry_token}")
	private String retryToken;

	@Value("${store_multiple_file}")
	private String storeMultipleFile;
	
	@Value("${server_port}")
	private String serverPort;
	
	@Value("${euft}")
	private String euft;
	
	@Value("${da_scan_interval}")
	private String daScanInterval;
	
	@Value("cas_ip")
	private String casIP;

	@Value("cas_username")
	private String casUsername;
	
	@Value("cas_password")
	private String casPassword;
	
	@Value("cas_download_dir")
	private String casdownloadDir;
	
	@Value("cas_timeout")
	private String casTimeout;
	
	@Value("cas_port")
	private String casPort; 
	
	
	public LoadProperties() {
		initialize();
	}

	private void initialize() {
		InputStream inputStream = null;
		Properties prop = new Properties();
		String propFileName = Constants.TEMPLATE_FILE;
	
	try {
		File file = new File(configPath + Constants.BACK_SLASH+ propFileName);
		if (!file.exists()) {
			return;
		}			
		inputStream = new FileInputStream(file.getAbsolutePath());
		prop.load(inputStream);
		inputStream.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	if (null == downloadDir) {
		downloadDir = prop.getProperty("download_dir");
	}
	if (null == baseUrl) {
		baseUrl = prop.getProperty("base_url");
	}

	if (null == apiUrl) {
		apiUrl = prop.getProperty("api_url");
	}
	
	if (null == storeMultipleFile) {
		storeMultipleFile = prop.getProperty("store_multiple_file");
	}
	
	if (null == retryToken) {
		retryToken = prop.getProperty("retry_token");
	}
	
	if (null == serverPort) {
		serverPort = prop.getProperty("server_port");
	}
	
	if (null == euft) {
		euft = prop.getProperty("euft");
	}
	
	if (null == daScanInterval) {
		daScanInterval = prop.getProperty("da_scan_interval");
	}
	
	if (null == casIP) {
		casIP = prop.getProperty("cas_ip");
	}
	
	if (null == casUsername) {
		casUsername = prop.getProperty("cas_username");
	}
	
	if (null == casPassword) {
		casPassword = prop.getProperty("cas_password");
	}
	
	if (null == casdownloadDir) {
		casdownloadDir = prop.getProperty("cas_download_dir");
	}
	
	if (null == casIP) {
		casTimeout = prop.getProperty("cas_timeout");
	}
	
	if (null == casIP) {
		casTimeout = prop.getProperty("cas_port");
	}
	
	setDownloadDir(downloadDir);
	setBaseUrl(baseUrl);
	setApiUrl(apiUrl);
	setStoreMultipleFile(storeMultipleFile);
	setRetryToken(retryToken);
	setServerPort(serverPort);
	setEuft(euft);
	setDaScanInterval(daScanInterval);
	setCasIP(casIP);
	setCasUsername(casUsername);
	setCasPassword(casPassword);
	setCasdownloadDir(casdownloadDir);
	setCasTimeout(casTimeout);
	setCasPort(casPort);
	}

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(downloadDir)){
		this.downloadDir = downloadDir.trim();
		}
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(baseUrl)){
		this.baseUrl = baseUrl.trim();
		}
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(apiUrl)){
		this.apiUrl = apiUrl.trim();
		}
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(clientKey)){
		this.clientKey = clientKey.trim();
		}
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(secretKey)){
		this.secretKey = secretKey.trim();
		}
	}

	public String getRetryToken() {
		return retryToken;
	}

	public void setRetryToken(String retryToken2) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(retryToken2)){
		this.retryToken = retryToken2.trim();
		}
	}

	public String getStoreMultipleFile() {
		return storeMultipleFile;
	}

	public void setStoreMultipleFile(String storeMultipleFile) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(storeMultipleFile)){
		this.storeMultipleFile = storeMultipleFile.trim();
		}
	}
	
	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(serverPort)){
		this.serverPort = serverPort.trim();
		}
	}
	
	public String getEuft() {
		return euft;
	}

	public void setEuft(String euft) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(euft)){
		this.euft = euft.trim();
		}
	}
	
	public String getDaScanInterval() {
		return daScanInterval;
	}

	public void setDaScanInterval(String daScanInterval) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(daScanInterval)){
		this.daScanInterval = daScanInterval.trim();
		}
	}
	
	public String getCasIP() {
		return casIP;
	}

	public void setCasIP(String casIP) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(casIP)){
		this.casIP = casIP.trim();
		}
	}

	public String getCasUsername() {
		return casUsername;
	}

	public void setCasUsername(String casUsername) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(casUsername)){
		this.casUsername = casUsername.trim();
		}
	}

	public String getCasPassword() {
		return casPassword;
	}

	public void setCasPassword(String casPassword) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(casPassword)){
		this.casPassword = casPassword.trim();
		}
	}

	public String getCasdownloadDir() {
		return casdownloadDir;
	}

	public void setCasdownloadDir(String casdownloadDir) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(casdownloadDir)){
		this.casdownloadDir = casdownloadDir.trim();
		}
	}

	public String getCasTimeout() {
		return casTimeout;
	}

	public void setCasTimeout(String casTimeout) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(casTimeout)){
			this.casTimeout = casTimeout.trim();
		}
	}

	public String getCasPort() {
		return casPort;
	}

	public void setCasPort(String casPort) {
		if(!StringUtils.isOnlyWhitespaceOrEmpty(casPort)){
			this.casPort = casPort.trim();
		}
	}

}
*/