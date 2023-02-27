package com.ericsson.swgw.da.services;

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.utils.StringUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * A simple SFTP client using JSCH http://www.jcraft.com/jsch/
 */
public final class DASftpConnection {
	private static  Logger logger = LogManager.getLogger(DASftpConnection.class);
    private static  String hostName;
    private static  int portNo;
    private static  String userName;
    private static  JSch jsch;
    private static  ChannelSftp channel;
    private static  Session session;

    /**
     * @param host     remote hostName
     * @param port     remote port
     * @param username remote username
     */
    public DASftpConnection(String host, int port, String username) {
        hostName = host;
        portNo     = port;
        userName = username;
        jsch     = new JSch();
    }

    /**
     * Use default port 22
     *
     * @param host     remote host
     * @param username username on host
     */
    public DASftpConnection(String host, String username) {
        this(hostName, 22, username);
    }
    
    /**
     * @param loadProperties
     */
   /* public DASftpConnection(LoadProperties sftploadProperties) {
    	loadProperties= sftploadProperties;
    	jsch          = new JSch();
	}*/
    
    /**
     * @param loadProperties
     */
    public DASftpConnection() {
    	jsch          = new JSch();
	}

	public ChannelSftp sftpConnection() throws JSchException {
		try {
			hostName = IniConfig.getCascIP();
			String casPort = IniConfig.getCascPort();
			if(!StringUtils.isOnlyWhitespaceOrEmpty(casPort)){
				portNo = Integer.parseInt(casPort);
			}else{
				portNo = 22;
			}
			
			userName = IniConfig.getCascUsername();
			String password = IniConfig.getCascPassword();
			channel = authPassword(password);
			logger.info(" Connection to CASC was established ");
		} catch (JSchException e) {
			logger.error("Failed to establish connection to CASC");
			logger.error("Check configuration to see if username/password is configured correctly");
			throw new JSchException(e.getMessage());
		}
		return channel;
	}
	
	public ChannelSftp sftp_Connection() throws JSchException {
		try {
			hostName = IniConfig.getDADestIP();
			String DAPort = IniConfig.getDADestPort();
			if(!StringUtils.isOnlyWhitespaceOrEmpty(DAPort)){
				portNo = Integer.parseInt(DAPort);
			}else{
				portNo = 22;
			}
			
			userName = IniConfig.getDADestUsername();
			String password = IniConfig.getDADestPassword();
			channel = authPassword(password);
			System.out.println(" Connection was established Successfully");
		} catch (JSchException e) {
			System.out.println("Failed to establish connection");
			System.out.println("Check configuration to see if username/password is configured correctly");
			throw new JSchException(e.getMessage());
		}
		return channel;
	}

    /**
     * Authenticate with remote using password
     *
     * @param password password of remote
     * @return 
     * @throws JSchException If there is problem with credentials or connection
     */
    public static ChannelSftp authPassword(String password) throws JSchException {
        session = jsch.getSession(userName, hostName, portNo);
        //disable known hosts checking
        //if you want to set knows hosts file You can set with jsch.setKnownHosts("path to known hosts file");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword(password);
        session.connect();
        logger.info("Host connected with port securely");
        return (ChannelSftp) session.openChannel("sftp");
        
    }


    public ChannelSftp authKey(String keyPath, String pass) throws JSchException {
        jsch.addIdentity(keyPath, pass);
        session = jsch.getSession(userName, hostName, portNo);
        //disable known hosts checking
        //if you want to set knows hosts file You can set with jsch.setKnownHosts("path to known hosts file");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        return (ChannelSftp) session.openChannel("sftp");
    }
    
    @SuppressWarnings("unchecked")
    public void listFiles(String remoteDir) throws SftpException, JSchException {
        if (channel == null) {
            throw new IllegalArgumentException("Connection is not available");
        }
        System.out.printf("Listing [%s]...%n", remoteDir);
        channel.cd(remoteDir);
        Vector<ChannelSftp.LsEntry> files = channel.ls(".");
        for (ChannelSftp.LsEntry file : files) {
            String name        = file.getFilename();
            System.out.printf(name);
        }
    }
    
    /**
     * Disconnect from remote
     */
    public void close() {
        if (channel != null) {
            channel.exit();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}
