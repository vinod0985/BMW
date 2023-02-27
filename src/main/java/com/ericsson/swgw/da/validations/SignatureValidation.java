package com.ericsson.swgw.da.validations;

/*###################################################################################
#  @Name           :   SignatureValidation.java
#
#  @Created        :   Oct Drop 2022
#
#  @Description    :   To validate Signature files with trust_anchor key for  packages download from SW Gateway
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2210
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      21-Sept-2022            validate signature files form the packages download from SW Gateway
######################################################################################*/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.exception.SignatureValidationException;
import com.ericsson.swgw.da.utils.StringUtils;

public class SignatureValidation {
	private static final String SPACE = " ";

	private static  Logger logger = LogManager.getLogger(SignatureValidation.class);
	private SignatureValidation() {
	    throw new IllegalStateException("Validation class");
	  }
	private static String currentDirectory = System
			.getProperty(Constants.USER_DIR);
	public static String validateOpenSSL() {
		String strResult = "";
		String strcommandErrorResult = "";
		try {

			boolean isWindows = System.getProperty("os.name").toLowerCase()
					.startsWith("windows");
			//String command = "";
			String[] command = new String[4];
			if (isWindows) {
				command[0] = "cmd.exe";
				command[1] = "/c";
				command[2] = "openssl version";
				command[3] = ";";
			} else {
				command[0] = "sh";
				command[1] = "-c";
				command[2] = "openssl version";
				command[3] = ";";
				//command = "openssl version";
			}

			Process process;
			// String command = "help PATH";
			// String command = "java -version";
			Runtime r = Runtime.getRuntime();
			logger.info("before validate OpenSSL  exec :::"+Arrays.toString(command));
			process = r.exec(command);
			logger.debug("process :::"+process);
			BufferedReader commandResult = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			BufferedReader commandError = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));
			
			strResult = commandResult.readLine();
			
			logger.info("commandResult readLine()   ::::"
					+ strResult);
			
			while ((strResult = commandResult.readLine()) != null) {
				try {
					logger.debug("strResult  ::::" + strResult);
				} catch (Exception ex) {
					// Not required: Kept to skip the row
				}
			}

			strcommandErrorResult = commandError.readLine();
			logger.info("commandError readLine   ::::"
					+ strcommandErrorResult);
			
			while ((strcommandErrorResult = commandError.readLine()) != null) {
				try {
					logger.error("strcommandErrorResult  ::::"
							+ strcommandErrorResult);
				} catch (Exception ex) {
					// Not required: Kept to skip the row
				}
			}
			process.destroy();
			if (process.isAlive()) {
				process.destroyForcibly();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strcommandErrorResult;
	}
	
	public static boolean boxSignatureValidation(String signatureFile, String manifestFile) throws SignatureValidationException, IOException {
		boolean isSignsuccess = false;
		String validateOpenSSL = validateOpenSSL();
		logger.debug("==========validateOpenSSL==========="+validateOpenSSL);
		if(!StringUtils.isOnlyWhitespaceOrEmpty(validateOpenSSL)){
			throw new SignatureValidationException("couldn't find openssl installed in the DA execution server");
		}else{
			File anchorfile = new File(currentDirectory + Constants.BACK_SLASH +Constants.TRUST_ANCHOR_FILE);
			signatureFile = "\"" +signatureFile + "\"";
			manifestFile = "\"" +manifestFile + "\"";
			String sAnchorFile = "\"" +anchorfile.getAbsolutePath() + "\"";
			StringBuilder sigBuilder = new StringBuilder();
			sigBuilder.append("openssl");
			sigBuilder.append(SPACE);
			sigBuilder.append("cms");
			sigBuilder.append(SPACE);
			sigBuilder.append("-verify");
			sigBuilder.append(SPACE);
			sigBuilder.append("-in");
			sigBuilder.append(SPACE);
			sigBuilder.append(signatureFile);
			sigBuilder.append(SPACE);
			sigBuilder.append("-binary");
			sigBuilder.append(SPACE);
			sigBuilder.append("-inform");
			sigBuilder.append(SPACE);
			sigBuilder.append("der");
			sigBuilder.append(SPACE);
			sigBuilder.append("-content");
			sigBuilder.append(SPACE);
			sigBuilder.append(manifestFile);
			sigBuilder.append(SPACE);
			sigBuilder.append("-CAfile");
			sigBuilder.append(SPACE);
			sigBuilder.append(sAnchorFile);
			String sigCommand = sigBuilder.toString();
			logger.debug("===============Signature command========="+sigCommand);
			boolean isWindows = System.getProperty("os.name").toLowerCase()
					.startsWith("windows");
			 String[] command = new String[4];
			//String command = "";
			if (isWindows) {
				command[0] = "cmd.exe";
				command[1] = "/c";
			} else {
				command[0] = "sh";
				command[1] = "-c";
				//command = sigCommand;
			}
			command[2] = sigCommand;
			command[3] = ";";
			System.out.println("command :::"+Arrays.toString(command));
			Process process;
			Runtime r = Runtime.getRuntime();
			System.out.println("before exec :::");
			process = r.exec(command);

			BufferedReader commandResult = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			BufferedReader commandError = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));

			logger.debug("commandResult readLine()   ::::"
					+ commandResult.readLine());
			String strResult="";
			while ((strResult = commandResult.readLine()) != null) {
				try {
					logger.debug("strResult  ::::" + strResult);
				} catch (Exception ex) {
					// Not required: Kept to skip the row
				}
			}

			logger.debug("commandError readLine   ::::"
					+ commandError.readLine());
			if(StringUtils.isOnlyWhitespaceOrEmpty(commandError.readLine())){
				isSignsuccess = true;
			}
			String strcommandErrorResult;
			while ((strcommandErrorResult = commandError.readLine()) != null) {
				try {
					logger.error("strcommandErrorResult  ::::"
							+ strcommandErrorResult);
				} catch (Exception ex) {
					// Not required: Kept to skip the row
				}
			}
			process.destroy();
			if (process.isAlive()) {
				process.destroyForcibly();
			}
		}
		return isSignsuccess;
	}

}
