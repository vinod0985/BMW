package com.ericsson.swgw.da.properties;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.springframework.core.io.ClassPathResource;

import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.constants.IniConstants;

public class IniConfig {

	private static String currentDirectory = System
			.getProperty(Constants.USER_DIR);
	private static String configPath = currentDirectory + "/configurations";

	private static String iniFilePath;
	private static Ini ini = null;

	private static String clientKey;
	private static String secretKey;
	
	/*private static String DAServerPort = IniConstants.INI_DA_DESTINATION_PORT;
	private static String DADestDir = IniConstants.INI_DA_DESTINATION_DIR;
	private static String DADestPort = IniConstants.INI_DA_DESTINATION_PORT;
	private static String DADestIP = IniConstants.INI_DA_DESTINATION_IP;
	private static String DAStorageType = IniConstants.INI_DA_TYPE_OF_STORAGE;
	private static String DAVerifyProducts = IniConstants.INI_DA_VERIFY_PRODUCTS;
	private static String DADownloadOnly = IniConstants.INI_DA_DOWNLOAD_ONLY;
	private static String DADownloadEntireTicket = IniConstants.INI_DA_DOWNLOAD_ENTIRE_TICKET;
	private static String DARetryToken = IniConstants.INI_DA_RETRY_TOKEN;
	private static String DAStoreMultipleFile = IniConstants.INI_DA_STORE_MULTIPLE_FILE;
	private static String DADestUsername = IniConstants.INI_DA_DESTINATION_USERNAME;
	private static String DADestPassword = IniConstants.INI_DA_DESTINATION_PASSWORD;
	private static String DAAccessKey = IniConstants.INI_DA_ACCESSKEY;
	private static String DAScretKey = IniConstants.INI_DA_SECRETKEY;*/
	
	public IniConfig(){
			try {
				iniFilePath = configPath + Constants.BACK_SLASH
						+ Constants.TEMPLATE_FILE;
				System.out.println("iniFilePath................"+iniFilePath);
				
				/*ClassPathResource cp = new ClassPathResource(iniFilePath);
				File f = null;

			    if (cp.exists()) {
			        f = cp.getFile();
			        System.out.println("iniFilePath......f ...getAbsolutePath......."+f.getAbsolutePath());
			    	ini = new Ini(f);
			    }*/
				
				File fileToParse = new File(iniFilePath);
				System.out.println("isFile................"+fileToParse.isFile());
				if (fileToParse.isFile()) {
					ini = new Ini(fileToParse);
				}
			} catch (Exception e) {
				System.out.println("Initialization failed");
				e.printStackTrace();
			}
	}	

	public static Ini getIniConfig() {
		return ini;
	}

	public static Map getSwgwConfigMap() {
		return (null != ini) ? ini.get(IniConstants.INI_SWGW) : new Ini();
	}

	public static Map getDefaultAttConfigMap() {
		return (null != ini) ? ini.get(IniConstants.INI_DEFAULTATTRIBUTES)
				: new HashMap();
	}

	public static Map getCascConfigMap() {
		return (null != ini) ? ini.get(IniConstants.INI_CASC) : new HashMap();
	}

	public static Map getSpinnakerConfigMap() {
		return (null != ini) ? ini.get(IniConstants.INI_SPINNAKER)
				: new HashMap();
	}

	public static Map getDAConfigMap() {
		return (null != ini) ? ini.get(IniConstants.INI_DISTRIBUTIONAGENT)
				: new HashMap();
	}

	public static Map getProductMap(String ProductSectionName) {
		return (null != ini) ? ini.get(ProductSectionName) : new HashMap();
	}

	public static List getDefaultAttProducts() {
		List<String> productsList = new ArrayList<String>();
		Map mDefaultAttt = (null != ini) ? ini.get(IniConstants.INI_DEFAULTATTRIBUTES) : null;
		if (null != mDefaultAttt) {
			String products = (String) mDefaultAttt.get(IniConstants.INI_PRODUCTS);
			productsList = new ArrayList<String>(Arrays.asList(products.split(Constants.COMMA)));
		}
		return productsList;
	}

	public static String getSWProductNo(Map productMap) {
		return (null != productMap.get(IniConstants.INI_PRODUCTS_SW_PRODUCT_NO)) ? (String) (productMap
				.get(IniConstants.INI_PRODUCTS_SW_PRODUCT_NO))
				: Constants.EMPTY;
	}

	public static String getReleasNoteNo(Map productMap) {
		return (null != productMap
				.get(IniConstants.INI_PRODUCTS_RELEASE_NOTE_NO)) ? (String) (productMap
				.get(IniConstants.INI_PRODUCTS_RELEASE_NOTE_NO))
				: Constants.EMPTY;
	}

	public static String getProductHeader(Map productMap) {
		return (null != productMap
				.get(IniConstants.INI_PRODUCTS_PRODUCT_HEADER)) ? (String) (productMap
				.get(IniConstants.INI_PRODUCTS_PRODUCT_HEADER))
				: Constants.EMPTY;
	}

	public static String getProductWebhook(Map productMap) {
		return (null != productMap.get(IniConstants.INI_PRODUCTS_WEBHOOK)) ? (String) (productMap
				.get(IniConstants.INI_PRODUCTS_WEBHOOK)) : Constants.EMPTY;
	}

	public static String getCascIP() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_CASC) : null)) ? (ini
				.get(IniConstants.INI_CASC)).get(IniConstants.INI_CASC_IP)
				: Constants.EMPTY;
	}

	public static String getCascUsername() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_CASC) : null)) ? (ini
				.get(IniConstants.INI_CASC))
				.get(IniConstants.INI_CASC_USERNAME) : Constants.EMPTY;
	}

	public static String getCascPassword() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_CASC) : null)) ? (ini
				.get(IniConstants.INI_CASC))
				.get(IniConstants.INI_CASC_PASSWORD) : Constants.EMPTY;
	}

	public static String getCascDownloadDir() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_CASC) : null)) ? (ini
				.get(IniConstants.INI_CASC))
				.get(IniConstants.INI_CASC_DOWNLOAD_DIR) : Constants.EMPTY;
	}

	public static String getCascTimeout() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_CASC) : null)) ? (ini
				.get(IniConstants.INI_CASC)).get(IniConstants.INI_CASC_TIMEOUT)
				: Constants.EMPTY;
	}

	public static String getCascPort() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_CASC) : null)) ? (ini
				.get(IniConstants.INI_CASC)).get(IniConstants.INI_CASC_PORT)
				: Constants.EMPTY;
	}
	
	public static String getCascDASscanInterval() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_CASC) : null)) ? (ini
				.get(IniConstants.INI_CASC)).get(IniConstants.INI_CASC_DA_SCAN_INTERVAL)
				: Constants.EMPTY;
	}

	public static String getSpinnakerIP() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SPINNAKER)
				: null)) ? (ini.get(IniConstants.INI_SPINNAKER))
				.get(IniConstants.INI_SPINNAKER_IP) : Constants.EMPTY;
	}

	public static String getSpinnakerPort() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SPINNAKER)
				: null)) ? (ini.get(IniConstants.INI_SPINNAKER))
				.get(IniConstants.INI_SPINNAKER_PORT) : Constants.EMPTY;
	}

	public static String getSpinnakerURL() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SPINNAKER)
				: null)) ? (ini.get(IniConstants.INI_SPINNAKER))
				.get(IniConstants.INI_SPINNAKER_URL) : Constants.EMPTY;
	}

	public static String getSpinnakerUsername() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SPINNAKER)
				: null)) ? (ini.get(IniConstants.INI_SPINNAKER))
				.get(IniConstants.INI_SPINNAKER_USERNAME) : Constants.EMPTY;
	}

	public static String getSpinnakerPassword() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SPINNAKER)
				: null)) ? (ini.get(IniConstants.INI_SPINNAKER))
				.get(IniConstants.INI_SPINNAKER_PASSWORD) : Constants.EMPTY;
	}

	public static String getSpinnakerWebhookService() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SPINNAKER)
				: null)) ? (ini.get(IniConstants.INI_SPINNAKER))
				.get(IniConstants.INI_SPINNAKER_WEBHOOK_SERVICE)
				: Constants.EMPTY;
	}

	public static String getSwgwConnectTimeout() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SWGW) : null)) ? (ini
				.get(IniConstants.INI_SWGW))
				.get(IniConstants.INI_SWGW_CONNECT_TIMEOUT) : Constants.EMPTY;
	}

	public static String getSwgwReadTimeout() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SWGW) : null)) ? (ini
				.get(IniConstants.INI_SWGW))
				.get(IniConstants.INI_SWGW_READ_TIMEOUT) : Constants.EMPTY;
	}

	public static String getSwgwRetries() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SWGW) : null)) ? (ini
				.get(IniConstants.INI_SWGW)).get(IniConstants.INI_SWGW_RETRIES)
				: Constants.EMPTY;
	}

	public static String getSwgwBaseURL() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SWGW) : null)) ? (ini
				.get(IniConstants.INI_SWGW))
				.get(IniConstants.INI_SWGW_BASE_URL) : Constants.EMPTY;
	}

	public static String getSwgwApiURL() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SWGW) : null)) ? (ini
				.get(IniConstants.INI_SWGW)).get(IniConstants.INI_SWGW_API_URL)
				: Constants.EMPTY;
	}

	public static String getSwgwClientKey() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SWGW) : null)) ? (ini
				.get(IniConstants.INI_SWGW))
				.get(IniConstants.INI_SWGW_CLIENT_KEY) : Constants.EMPTY;
	}

	public static String getSwgwSecretKey() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SWGW) : null)) ? (ini
				.get(IniConstants.INI_SWGW))
				.get(IniConstants.INI_SWGW_SECRET_KEY) : Constants.EMPTY;
	}

	public static String getSwgwDownloadDir() {
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SWGW) : null)) ? (ini
				.get(IniConstants.INI_SWGW))
				.get(IniConstants.INI_SWGW_DOWNLOAD_DIR) : Constants.EMPTY;
	}

	public static String getSwgwEUFT() {
		
		return (null != ((null != ini) ? ini.get(IniConstants.INI_SWGW) : null)) ? (ini
				.get(IniConstants.INI_SWGW)).get(IniConstants.INI_SWGW_EUFT)
				: Constants.EMPTY;
	}

	public String getDAServerPort() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_SERVER_PORT) : Constants.EMPTY;
	}

	public static String getDADestDir() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_DESTINATION_DIR) : Constants.EMPTY;
	}

	public static String getDADestIP() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_DESTINATION_IP) : Constants.EMPTY;
	}

	public static String getDADestPort() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_DESTINATION_PORT) : Constants.EMPTY;
	}

	public static String getDAStorageType() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_TYPE_OF_STORAGE) : Constants.EMPTY;
	}

	public static String getDAVerifyProducts() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_VERIFY_PRODUCTS) : Constants.EMPTY;
	}

	public static String getDADownloadOnly() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_DOWNLOAD_ONLY) : Constants.EMPTY;
	}

	public static String getDADownloadEntireTicket() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_DOWNLOAD_ENTIRE_TICKET)
				: Constants.EMPTY;
	}

	public static String getDARetryToken() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_RETRY_TOKEN) : Constants.EMPTY;
	}

	public static String getDAStoreMultipleFile() {
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_STORE_MULTIPLE_FILE) : Constants.EMPTY;
	}

	public static String getDAAccessKey(){
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_ACCESSKEY) : Constants.EMPTY;
	}
	
	public static String getDASecretKey(){
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_SECRETKEY) : Constants.EMPTY;
	}
	
	public static String getDADestUsername(){
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_DESTINATION_USERNAME) : Constants.EMPTY;
	}
	
	public static String getDADestPassword(){
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT) : null)) ? (ini
				.get(IniConstants.INI_DISTRIBUTIONAGENT))
				.get(IniConstants.INI_DA_DESTINATION_PASSWORD) : Constants.EMPTY;
	}
	
	public static String getClientKey() {
		return clientKey;
	}
	
	public static String getSecretKey() {
		return secretKey;
	}
	
	public static String getCurrentDirectory() {
		return currentDirectory;
	}

	/*public static String getDAServerPort() {
		return DAServerPort;
	}

	public static String getDADestDir() {
		return DADestDir;
	}

	public static String getDADestPort() {
		return DADestPort;
	}

	public static String getDADestIP() {
		return DADestIP;
	}

	public static String getDAStorageType() {
		return DAStorageType;
	}

	public static String getDAVerifyProducts() {
		return DAVerifyProducts;
	}

	public static String getDADownloadOnly() {
		return DADownloadOnly;
	}

	public static String getDADownloadEntireTicket() {
		return DADownloadEntireTicket;
	}

	public static String getDARetryToken() {
		return DARetryToken;
	}

	public static String getDAStoreMultipleFile() {
		return DAStoreMultipleFile;
	}

	public static String getDADestUsername() {
		return DADestUsername;
	}

	public static String getDADestPassword() {
		return DADestPassword;
	}

	public static String getDAAccessKey() {
		return DAAccessKey;
	}

	public static String getDAScretKey() {
		return DAScretKey;
	}*/

	public static void setSwgwClientKey(String sClientKey) {
		clientKey = (null != sClientKey) ? sClientKey.trim() : Constants.EMPTY;
		
		if(null != sClientKey && null != ini){
			Section section = ini.get(IniConstants.INI_SWGW);
			section.add(IniConstants.INI_SWGW_CLIENT_KEY, sClientKey);			
			ini.add(IniConstants.INI_SWGW, section);
		}
	}

	public static void setSwgwSecretKey(String sSecretKey) {
		secretKey = (null != sSecretKey) ? sSecretKey.trim() : Constants.EMPTY;
		
		if(null != sSecretKey && null != ini){
			Section section = ini.get(IniConstants.INI_SWGW);
			section.add(IniConstants.INI_SWGW_SECRET_KEY, sSecretKey);			
			ini.add(IniConstants.INI_SWGW, section);
		}
	}
	
	public static String getDMSURL(){
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DMS) : null)) ? (ini
				.get(IniConstants.INI_DMS))
				.get(IniConstants.INI_DMS_URL) : Constants.EMPTY;
	}
	
	public static String getDMSUsername(){
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DMS) : null)) ? (ini
				.get(IniConstants.INI_DMS))
				.get(IniConstants.INI_DMS_USERNAME) : Constants.EMPTY;
	}
	
	public static String getDMSPassword(){
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DMS) : null)) ? (ini
				.get(IniConstants.INI_DMS))
				.get(IniConstants.INI_DMS_PASSWORD) : Constants.EMPTY;
	}
	
	
	public static String getDMSTypeOfAPI(){
		return (null != ((null != ini) ? ini
				.get(IniConstants.INI_DMS) : null)) ? (ini
				.get(IniConstants.INI_DMS))
				.get(IniConstants.INI_DMS_TYPEOFAPI) : Constants.EMPTY;
	}


}
