package com.ericsson.swgw.da.actions;

/*###################################################################################
#  @Name           :   Checksum.java
#
#  @Created        :   Nov Drop 2022
#
#  @Description    :   To transfer files to destination storage 
#
#  @Programmer     :   Santosh K
#
#  @Organization   :   HCL
#
#  @Release        :   MR2211
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      3-Nov-2022            created for file transfer to destination storage 
######################################################################################*/

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.services.DASftpConnection;
import com.ericsson.swgw.da.utils.StringUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FileTransfer {

	public static String bucketName = "my-bucketname";
	public static String ACCESS_KEY = IniConfig.getDAAccessKey();
	public static String SECRET_KEY = IniConfig.getDASecretKey();

	private static MinioClient minioClient;

	private static ChannelSftp sftpChannel;

	private static  Logger logger = LogManager.getLogger(FileTransfer.class);

	private FileTransfer(ChannelSftp scSftpChannel) {
		sftpChannel= scSftpChannel;

	}
	private static final String FALSE = "false";

	public static Boolean isDownloadOnly(){
		Boolean bReturn = false;
		String sDownloadOnly = IniConfig.getDADownloadOnly();
		logger.info("sDownloadOnly is "+ sDownloadOnly);
		if (!StringUtils.isOnlyWhitespaceOrEmpty(sDownloadOnly) && FALSE.equalsIgnoreCase(sDownloadOnly)) {
			logger.info("Download only is not specified in DA configurations. Hence proceed for file transfer");
			bReturn = true;
		} else {
			logger.info("Download only was specified in DA configurations");
		}        
		return bReturn;    
	}

	public static List<File> transferFiles(List<File> successFiles) throws MinioException , JSchException{
		List<File> transferedFiles = new ArrayList<>();
		if(successFiles.isEmpty()){
			return Collections.emptyList();
		}
		String daStorageType = IniConfig.getDAStorageType();
		String daDestIP = IniConfig.getDADestIP();
		if(daStorageType.equalsIgnoreCase("minio")){
			try{
				minioClient =
						MinioClient.builder()
						.endpoint(daDestIP)
						.credentials(ACCESS_KEY, SECRET_KEY)
						.build();
				boolean found = 
						minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
				if (found) {
					minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
					logger.info(bucketName +" exists");
					String readAndWritePolicy = "json:{}";
					minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(readAndWritePolicy).build());
					minioFileTransfer(successFiles);

				} else {
					logger.info(bucketName +" does not exist");
				}
			}catch (MinioException e) {
				logger.info("Error occurred: " + e);
				logger.info("HTTP trace: " + e.httpTrace());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			DASftpConnection dasftp = new DASftpConnection();
			Session session = null;
			String sourceDir = IniConfig.getSwgwDownloadDir();
			String remoteDir = IniConfig.getDADestDir();
			try {
				sftpChannel = dasftp.sftp_Connection();	
				if(sftpChannel!=null){
					transferedFiles = sftpFileTransfer(sftpChannel,successFiles,sourceDir,remoteDir);
					session = sftpChannel.getSession();
				}
			} catch (JSchException e) {
				e.printStackTrace();
			}finally{
				if (sftpChannel != null) {
					sftpChannel.exit();
				}
				if (session != null && session.isConnected()) {
					session.disconnect();
				}
			}
		}
		return transferedFiles;
	}
	public static void minioFileTransfer(List<File> files) throws Exception{
		for(File file : files){
			String fileName = file.getName();
			minioClient.uploadObject(
					UploadObjectArgs.builder()
					.bucket(bucketName)
					.object(fileName)
					.filename(fileName)
					.build());
		}
	}

	public static List<File> sftpFileTransfer(ChannelSftp sftpChannel, List<File> files, String sourceDir, String destDir) throws JSchException  {
		List<File> successFiles = new ArrayList<>();
		//sftpChannel.cd(destDir);
		sftpChannel.connect();
		for(File localFile :files ){
			logger.info("Storing the file into remote Directory "+ localFile.getAbsolutePath());
			logger.info("destDir : " +destDir);
			try{
				sftpChannel.put(localFile.getAbsolutePath(), destDir + Constants.BACK_SLASH);
				successFiles.add(localFile);
			} catch (SftpException e) {
				e.printStackTrace();
			}
			logger.info("Stored the file into remote Directory "+ localFile.getAbsolutePath());
		}
		sftpChannel.exit();
		return successFiles;
	}



}
