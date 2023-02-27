package com.ericsson.swgw.da.utils;

/*###################################################################################
 #  @Name           :   PullUtils.java
 #
 #  @Created        :   Oct Drop 2022
 #
 #  @Description    :   To pull packages from SW Gateway.
 #
 #  @Programmer     :   Eswar D
 #
 #  @Organization   :   HCL
 #
 #  @Release        :   MR2210
 #
 #  @History        :
 #      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
 #      ZDARESW(Eswar)      20-Sept-2022           created pull util method to pull packages from SW gateway.
 ######################################################################################*/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.services.AzureTokenService;

public class PullUtils {
	
	private static  Logger logger = LogManager.getLogger(PullUtils.class);

	private PullUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * @param pullURL
	 * @param fileName
	 * @param filePath
	 * @param loadProperties
	 * @return headerFields
	 * @throws JSONException 
	 * @throws SQLException 
	 */
	public static Map<String, Object> pullMethod(String pullURL,
			String fileName, String filePath) throws JSONException, SQLException {
		Map<String, List<String>> headerFields = new HashMap<String, List<String>>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//logger.debug("File storage path......." + filePath);
		//logger.debug("Downloaded file name......." + fileName);

		logger.debug("Start Time for the pullMethod process :::" + new Date().toString());
		
		java.net.URL url;
		FileOutputStream fos = null;

		try {
			String sbearerToken = AzureTokenService.getswgwAuthenticate();
			if(StringUtils.isOnlyWhitespaceOrEmpty(sbearerToken)){
				//logger.error("bearer token is empty cant initate pull service towards SW Gateway");
				return returnMap;
			}
			String auth = String.format(Constants.BEARER, sbearerToken);
			url = new java.net.URL(pullURL);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setReadTimeout(600000);
			httpURLConnection.setConnectTimeout(600000);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty(HttpHeaders.AUTHORIZATION, auth);
			//httpURLConnection.setRequestProperty(HttpHeaders.PRAGMA, Constants.PRAGMA_HEADER_KEY_VALUE);
			httpURLConnection.setRequestProperty(HttpHeaders.USER_AGENT, Constants.APPLICATION);
			 if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				 InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
				 headerFields = httpURLConnection.getHeaderFields();
					if(fileName.contains("SMO") || fileName.contains("NMS")){
						fileName = getFileNameFromResponse(headerFields);
					}			
					java.io.File file = new java.io.File(filePath + Constants.BACK_SLASH+ fileName);
					System.out.println("file::::"+file.getAbsolutePath());
					
					fos = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int n = 0;
					int byteCount = 0;
					while (-1 != (n = in.read(buf))) {
						if (byteCount == 2048) {
							byteCount = 0;
						}
						byteCount++;
						fos.write(buf, 0, n);
					}
					in.close();
				
				returnMap.put("file", file);
				returnMap.put("fileName", fileName);
				returnMap.put("headerFields", headerFields);
			 }else{
				 InputStream errorStream = httpURLConnection.getErrorStream();
			        if (errorStream != null) {
			            BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
			            String line;
			            while ((line = reader.readLine()) != null) {
			            	logger.error("Unable to download packages from SW Gateway::::" + "Server returned HTTP " + httpURLConnection.getResponseCode()
							        + " " + httpURLConnection.getResponseMessage()+line);
			            }
			           
			            reader.close();
			            errorStream.close();
			        }
			        return returnMap;  
			    }
		} catch (IOException e) {
			logger.error("Unable to download packages from SW Gateway::::"+e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (null != fos) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("headerFields:::::::::::::"+headerFields);
		System.out.println("END Time for the pullMethod process :::" + new Date().toString());
		return returnMap;

	}

	/**
	 * @param fileName
	 * @param headerFields
	 * @return
	 */
	private static String getFileNameFromResponse(Map<String, List<String>> headerFields) {
		String fileName = "";
		if(headerFields.containsKey("fileName")){
			StringBuilder sbfileName = new StringBuilder(headerFields.get("fileName").toString());
			sbfileName.deleteCharAt(sbfileName.length() - 1);
			sbfileName.deleteCharAt(0);
			fileName = sbfileName.toString();
			fileName = fileName.replace("/", "_").replaceAll("\\s", "");
		}
		return fileName;
	}

	/**
	 * @param trustAnchorFileUrl
	 * @param fileName
	 * @param currentDirectory
	 * @throws IOException
	 */
	public static void pullAnchorFile(String trustAnchorFileUrl,
			String fileName, String currentDirectory) throws IOException {
		java.io.File file = new java.io.File(currentDirectory
				+ Constants.BACK_SLASH + fileName);
		java.net.URL url;
		FileOutputStream fos = null;
		try {
			url = new java.net.URL(trustAnchorFileUrl);
			URLConnection openConnection = url.openConnection();
			openConnection.setReadTimeout(600000);
			openConnection.setConnectTimeout(600000);
			InputStream in = new BufferedInputStream(
					openConnection.getInputStream());
			fos = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int n = 0;
			int byteCount = 0;
			while (-1 != (n = in.read(buf))) {
				if (byteCount == 2048) {
					byteCount = 0;
				}
				byteCount++;
				fos.write(buf, 0, n);
			}
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				fos.close();
			}
		}

	}	
}
