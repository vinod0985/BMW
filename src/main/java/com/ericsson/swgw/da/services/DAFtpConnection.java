package com.ericsson.swgw.da.services;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DAFtpConnection {
	private static  Logger logger = LogManager.getLogger(DAFtpConnection.class);
	private static FTPClient ftpClient;
	private static FTPFileFilter filter;
	

	public boolean  setConnection(String hostName,int port,String userName,String password) throws IOException {
		try {
			ftpClient = new FTPClient();
            ftpClient.connect(hostName, port);
            ftpClient.enterLocalPassiveMode(); 
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.error("Failed to establish connection to CASC :" +replyCode);
                close();
                return false;
            }else{
            	logger.info("Connection to CASC was established: " + replyCode);
            }
        } catch (IOException ex) {
        	logger.error("Oops! Something wrong happened :"+ex.getMessage());
            ex.printStackTrace();
        }
		return login(userName,password);
	}


	private boolean login(String userName, String password) throws IOException {
		boolean success = ftpClient.login(userName, password);
        showServerReply(ftpClient);
        String replyMessage = ftpClient.getReplyString();
        if (!success) {
        	logger.error("Could not login to the CASC : " + replyMessage);
            logger.error("Check configuration to see if username/password is configured correctly");
        } else {
        	logger.info("Logged into the CASC successfully : " + replyMessage);
        }
		return success;
	}


	private static void showServerReply(FTPClient ftpClient) {
	    String[] replies = ftpClient.getReplyStrings();
	    if (replies != null && replies.length > 0) {
	        for (String aReply : replies) {
	            logger.info("SERVER: " + aReply);
	        }
	    }
	}
	
	private void close() throws IOException {
		ftpClient.disconnect();
    }
	
	public static  FTPFile[] listFiles(String pathName, FTPFileFilter filter2) throws IOException{
		return ftpClient.listFiles(pathName, filter);
	}
	
	

}
