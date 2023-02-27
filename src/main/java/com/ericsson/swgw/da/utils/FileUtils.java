package com.ericsson.swgw.da.utils;

/*###################################################################################
 #  @Name           :   FileUtils.java
 #
 #  @Created        :   Sep Drop 2022
 #
 #  @Description    :   To store webhook payload content into a file
 #
 #  @Programmer     :   Eswar D
 #
 #  @Organization   :   HCL
 #
 #  @Release        :   MR2209
 #
 #  @History        :
 #      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
 #      ZDARESW(Eswar)      29-Aug-2022            store webhook content into a file
 ######################################################################################*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.MediaType;

import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class FileUtils {
	
	private static  Logger logger = LogManager.getLogger(FileUtils.class);
	/*private static final String SIGNATURE_FILES = "SignatureFiles";
	private static final int BUFFER_SIZE = 4096;*/
	private static InputStream inputStream;
	private static FileOutputStream outputStream;
	private static String currentDirectory = System
			.getProperty(Constants.USER_DIR);
	private static String logsPath = currentDirectory + "/logs";
	private static String testResourcePath = currentDirectory
			+ "/test-resources";
	private static String configPath = currentDirectory + "/configurations";
	
	
//	static{
//		System.out.println("currentDirectory...................."+currentDirectory);
//		if(StringUtils.isOnlyWhitespaceOrEmpty(currentDirectory)){
//			currentDirectory = "/data/distribution_agent";
//		}else{
//			File file = new File(currentDirectory);
//			if(!file.isDirectory()){
//				currentDirectory = "/data/distribution_agent";
//			}
//		}
//		System.out.println("currentDirectory after verification...................."+currentDirectory);
//	}

	/**
	 * @param payload
	 * @param contentType
	 * @param storeMultipleFile
	 * @throws Exception
	 */
	public static void savePayload(
			Map<String, Object> payload, MediaType contentType,
			String storeMultipleFile) throws Exception {
		//		if (storeMultipleFile.equalsIgnoreCase("TRUE")) {
		String ticketId ="";
		if(payload.containsKey(Constants.TICKET) && !(Constants.NULL).equals((payload.get(Constants.TICKET))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) payload.get(Constants.TICKET))){
			ticketId = (String) payload.get(Constants.TICKET);
		}
			multipleFile(payload, contentType,ticketId);

//		} else {
//			singleFile(payload,ticketId);
//		}

	}

//	private static void singleFile(Map<String, Object> payload)
//			throws JsonProcessingException, IOException {
//		String fileName = "storePayload.out";
//		String jsonStr = new ObjectMapper().writeValueAsString(payload);
//		File file = new File(logsPath, fileName);
//		if (!file.exists()) {
//			file.createNewFile();
//		}
//		BufferedWriter bf = null;
//		try {
//			bf = new BufferedWriter(new FileWriter(file));
//			bf.write(jsonStr);
//			bf.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if(null != bf){
//				bf.close();
//			}
//			
//		}
//	}

	private static void multipleFile(Map<String, Object> payload,
			MediaType contentType, String ticketId) throws Exception {
		BufferedWriter bfWriter = null;
		StringBuilder sb = new StringBuilder();
		sb.append("Webhook_");
		sb.append(ticketId);
		sb.append("_");
		String format = contentType.toString();
		try {
			if ((format.equalsIgnoreCase(Constants.APPLICATION_JSON))) {
				String fileName = sb.toString();
				String jsonStr = new ObjectMapper()
						.writeValueAsString(payload);
				File file = File.createTempFile(fileName,
						Constants.JSON_EXT, new File(logsPath));
					bfWriter = new BufferedWriter(new FileWriter(file));
					bfWriter.write(jsonStr);
					bfWriter.flush();
				

			} else if (format.equalsIgnoreCase(Constants.TEXT_XML) || format.equalsIgnoreCase(Constants.APPLICATION_XML) ) {
				String fileName = sb.toString();
				String xmlStr = new XmlMapper()
						.writeValueAsString(payload);
				File file = File.createTempFile(fileName,
						Constants.XML_EXT, new File(logsPath));
			
					bfWriter = new BufferedWriter(new FileWriter(file));
					bfWriter.write(xmlStr);
					bfWriter.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException();
		} finally {
			try {
				if (bfWriter != null) {
					bfWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createTemplateFile() {
		String fileName = Constants.TEMPLATE_FILE;
		logger.info("Current dir & congig path......."+configPath);
		File file = new File(configPath);
		if (!file.exists()) {
			file.mkdir();
		}
		logger.info("Current dir & congig path......."+file.getAbsolutePath());
		File template = new File(configPath, fileName);
		logger.info("template.ini path ......."+template.getAbsolutePath());
		if (!template.exists()) {
			try {
				template.createNewFile();
				logger.info("template.ini path ......."+template.getAbsolutePath());
				inputStream = FileUtils.class.getClassLoader()
						.getResourceAsStream(fileName);
				outputStream = new FileOutputStream(configPath
						+ Constants.BACK_SLASH + fileName);
				int n;
				while ((n = inputStream.read()) != -1) {
					outputStream.write(n);
				}
			}

			catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
					if (null != outputStream) {
						outputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		logger.info("template.ini path ......."+template.getAbsolutePath());
	}

	public static void createSSLTemplate() {
		String fileName = Constants.ADDITIONAL_PROPERTIES;
		File file = new File(configPath);
		if (!file.exists()) {
			file.mkdir();
		}
		File templateSSL = new File(configPath, fileName);
		if (!templateSSL.exists()) {
			try {
				templateSSL.createNewFile();
				inputStream = FileUtils.class.getClassLoader()
						.getResourceAsStream(fileName);
				outputStream = new FileOutputStream(configPath
						+ Constants.BACK_SLASH + fileName);
				int n;
				while ((n = inputStream.read()) != -1) {
					outputStream.write(n);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
					if (null != outputStream) {
						outputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public static void createlogs() {
		File file = new File(logsPath);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public static void createTestResorces() {
		String fileName = Constants.READ_SECRETS;
		File file = new File(testResourcePath);
		if (!file.exists()) {
			file.mkdir();
		}
		File templateSecrets = new File(testResourcePath, fileName);
		if (!templateSecrets.exists()) {
			try {
				templateSecrets.createNewFile();
				inputStream = FileUtils.class.getClassLoader()
						.getResourceAsStream(fileName);
				outputStream = new FileOutputStream(testResourcePath
						+ Constants.BACK_SLASH + fileName);
				int n;
				while ((n = inputStream.read()) != -1) {
					outputStream.write(n);
				}
			}catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
					if (null != outputStream) {
						outputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void createAnchorFile() throws IOException {
		String fileName = Constants.TRUST_ANCHOR_FILE;
		File file = new File(currentDirectory,fileName);
		if (!file.exists()) {
			PullUtils.pullAnchorFile(Constants.TRUST_ANCHOR_FILE_URL, fileName, currentDirectory);
		}	
	}
	
	/*public static synchronized File unzip(String zipFilePath,
			String destDirectory, String extractFolderName) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}

		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(
				zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();

		// create Sub directory
		File subdir = new File(destDirectory + File.separator
				+ extractFolderName);
		subdir.mkdir();

		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator
					+ extractFolderName + File.separator + entry.getName();

			if (entry.getName().contains(
					SIGNATURE_FILES)) {
				File dir = new File(destDirectory + File.separator
						+ extractFolderName + File.separator
						+ SIGNATURE_FILES);
				dir.mkdir();
			}

			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
		return subdir;
	}*/

	/* private static synchronized void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
	        byte[] bytesIn = new byte[BUFFER_SIZE];
	        int read = 0;
	        while ((read = zipIn.read(bytesIn)) != -1) {
	            bos.write(bytesIn, 0, read);
	        }
	        bos.close();
	    }*/
	 
	 public static synchronized File renameDir(String dirpath) {
	        String newDirName = "SignatureFiles_old";
	        File destDir = new File(dirpath);
	        if (destDir.exists() && destDir.isDirectory()) {
	            File newDir = new File(destDir.getParent() + File.separator + newDirName);
	            destDir.renameTo(newDir);
	        }
	        return destDir;
	    }
	 
	 public static boolean removeTemporaryFile(File file, boolean deleteDirectory) {
	        if (deleteDirectory) {
	            File fileDirectory = file.getParentFile();
	            if (fileDirectory != null && fileDirectory.isDirectory()) {
	                return file.delete() && fileDirectory.delete();
	            }
	        }
	        return file.delete();
	    }
	 
	 public static org.codehaus.jettison.json.JSONObject readFromFileforJSON_data_append(File file) throws IOException {
	        org.codehaus.jettison.json.JSONObject json = null;
	            BufferedReader reader;
	            reader = new BufferedReader(new FileReader(file));
	            try {
	                StringBuilder builder = new StringBuilder();
	                String str;
	                while ((str = reader.readLine()) != null) {
	                    builder.append(str);
	                }
	                reader.close();
	                json = new org.codehaus.jettison.json.JSONObject(builder.toString());
	            } catch (JSONException e) {
	                logger.error(" Unable to read from file: " + e.getMessage());
	                e.printStackTrace();
	            }finally{
	                reader.close();
	            }
	            return json;
	}

	public static boolean moveFileToDestination(String sourceDir,String destinationDir, String fileName) {
		File tempFile = new File (sourceDir + Constants.BACK_SLASH+ fileName);
		
		if (tempFile.exists()){
			boolean isMoved = tempFile.renameTo(new File(destinationDir + Constants.BACK_SLASH+ fileName));
			return isMoved;
		}
		return false;
	}
	
	public static boolean moveFileToDestination(String sourceDir,String destinationDir, String sourFileName, String destFileName) {
		File tempFile = new File (sourceDir + Constants.BACK_SLASH+ sourFileName);
		System.out.println("sourceDir getAbsolutePath::::"+tempFile.getAbsolutePath());
		if (tempFile.exists()){
			System.out.println("destinationDir getAbsolutePath:::"+destinationDir + Constants.BACK_SLASH+ destFileName);

			boolean isMoved = tempFile.renameTo(new File(destinationDir + Constants.BACK_SLASH+ destFileName));
			System.out.println("isMoved::::"+isMoved);

			return isMoved;
		}
		return false;
	}

	public static void deleteFile(String path, String fileName) {
		File file = new File (path + Constants.BACK_SLASH+ fileName);
		
		if (file.exists()){
			file.delete();
		}
	}
}
