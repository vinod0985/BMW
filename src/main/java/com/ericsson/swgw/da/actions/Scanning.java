package com.ericsson.swgw.da.actions;

/*###################################################################################
#  @Name           :   Scanning.java
#
#  @Created        :   Nov Drop 2022
#
#  @Description    :   To Connect CAS-C and Scan Packages
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2211
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      29-Oct-2022            Scanning action
######################################################################################*/

import io.minio.errors.MinioException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.springframework.stereotype.Component;

import com.ericsson.swgw.da.bean.DMSBean;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.dms.DMSService;
import com.ericsson.swgw.da.exception.CustomException;
import com.ericsson.swgw.da.exception.SignatureValidationException;
import com.ericsson.swgw.da.filter.ProductFilter;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.services.PullTicketXMLService;
import com.ericsson.swgw.da.swgw.bean.TicketXMLGenerator;
import com.ericsson.swgw.da.utils.ChecksumUtils;
import com.ericsson.swgw.da.utils.FileUtils;
import com.ericsson.swgw.da.utils.StringUtils;
import com.ericsson.swgw.da.validations.ChecksumValidation;
import com.ericsson.swgw.da.validations.SignatureValidation;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

@Component
public class Scanning {
	private static ChannelSftp sftpChannel;
	
	 /*public Scanning(ChannelSftp scSftpChannel, LoadProperties scloadProperties) {
		 loadProperties = scloadProperties;
		 sftpChannel= scSftpChannel;
	}*/
	 
	 public Scanning(ChannelSftp scSftpChannel) {
		 sftpChannel= scSftpChannel;
	}
	 
	private static  Logger logger = LogManager.getLogger(Scanning.class);
	private static final String JSONFIELD_SHA256 = "sha256";
	private static final String JSONFIELD_MD5 = "md5";
	
	@SuppressWarnings("unchecked")
    public static List<String> ticketXmlListFiles(ChannelSftp channel, String remoteDir, long daScanInterval) throws SftpException, JSchException {
        java.util.Date currentDate = new java.util.Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(daScanInterval));
        logger.info(" Current TimeUnit during scan period :::: "+currentDate);
        channel.cd(remoteDir);
        Vector<ChannelSftp.LsEntry> files = channel.ls(".");
        List<String> listTicketXMLS = new ArrayList<>();
        for(int i=0; i<files.size();i++){
        	ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) files.get(i);
            if(entry.getFilename().startsWith("Ticket_T-") && entry.getFilename().endsWith(".xml")){
            	String str = entry.getFilename();
                //logger.info("TicketXml Name ::::::::::: "+str);
            	SftpATTRS attrs = entry.getAttrs();
            	int modificationTime = attrs.getMTime();
            	java.util.Date modificationDate = new java.util.Date(modificationTime * 1000L);
                if(modificationDate.after(currentDate) || modificationDate.equals(currentDate)){
                	String remoteTicketxmlName = entry.getFilename();
                	listTicketXMLS.add(remoteTicketxmlName);
                }
            }
        }
        
        return listTicketXMLS;
    }

	public static List<String> transferFiles(ChannelSftp sftpChannel, List<String> listFiles, String sourceDir, String destinationDir) throws JSchException, SftpException {
		if(listFiles.isEmpty()){
			return Collections.emptyList();
		}else{
			sftpChannel.cd(sourceDir);
			logger.info("Transfer of  files to local Folder is  Started....");
			List<String> destFiles = new ArrayList<>();
			for (String remoteFileName : listFiles) {
				String destFileName="";
				StringBuilder sbDestFile = new StringBuilder();
				sbDestFile.append(destinationDir);
				sbDestFile.append(Constants.BACK_SLASH);
				sbDestFile.append(remoteFileName.replace("/", "_").replaceAll("\\s+$", ""));
				destFileName=sbDestFile.toString();
				sftpChannel.get(remoteFileName, destFileName);
				destFiles.add(remoteFileName);
			}
			logger.info("Transfer of  files to local Folder is  completed....");
			return destFiles;
		}
		
	}

	@SuppressWarnings("unchecked")
	public static List<String> boxlistFiles(ChannelSftp sftpChannel,
			String productNumber, String productVersion, String sourceDir) throws SftpException {
		sftpChannel.cd(sourceDir);
		Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
        List<String> refDoclist = new ArrayList<>();
        for(int i=0; i<files.size();i++){
        	ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) files.get(i);
            if(entry.getFilename().contains(productNumber+"-"+productVersion) && (entry.getFilename().endsWith(".xml")||entry.getFilename().endsWith(".zip")||entry.getFilename().endsWith(".sig")|| entry.getFilename().endsWith(".sha256"))){
            	refDoclist.add(entry.getFilename());
            }
        }
		return refDoclist;
	}
	
	/**
	 * @param sftpChannel
	 * @param downloadDir 
	 * @param remoteDir 
	 * @param loadProperties
	 * @throws Exception
	 * @throws JSONException
	 * @throws SignatureValidationException 
	 * @throws IOException 
	 * @throws MinioException 
	 * @throws CustomException 
	 */
	public void swgwScanOperation() throws JSONException, SignatureValidationException, IOException, MinioException, CustomException {
		
		List<String> productBoxNames = new ArrayList<String>();
		logger.info("Start DA Pckage products Filter");
		List<String> productsFilterList = ProductFilter.getproductsFilterList();
		logger.info("Products Filter List ::: " +productsFilterList);
		logger.info("END DA Pckage products Filter");
		String remoteDir = IniConfig.getCascDownloadDir();
		String downloadDir = IniConfig.getSwgwDownloadDir();
		String currentDirectory = System
				.getProperty(Constants.USER_DIR);
		String logsPath = currentDirectory + "/logs";
		String scanInterval = IniConfig.getCascDASscanInterval();
		long daScanInterval = Long.parseLong(scanInterval);
		try {
			List<String> ticketlistFiles = Scanning.ticketXmlListFiles(sftpChannel,remoteDir,daScanInterval);
			List<String> transferTicketXml = Scanning.transferFiles(sftpChannel,ticketlistFiles,remoteDir,logsPath);
			if(transferTicketXml.size()!= 0){
				for(String ticketfileName : transferTicketXml){
					File ticketXMlfile = new File(logsPath,ticketfileName);
					TicketXMLGenerator  ticketXML = PullTicketXMLService.getTicketXMLGenerator(ticketXMlfile);
					if(ticketXML != null){
						com.ericsson.swgw.da.swgw.bean.TicketXMLGenerator.Boxes boxes = ticketXML.getBoxes();
						List<TicketXMLGenerator.Boxes.Box> box = boxes.getBox();
						List<String> productBoxes = new ArrayList<>();
						List<String> successProductBoxes = new ArrayList<>();
						List<String> failedProductBoxes = new ArrayList<>();
						List<java.io.File> successFiles = new ArrayList<>();
						List<java.io.File> failureFiles = new ArrayList<>();
						List<java.io.File> successTransferedFiles = new ArrayList<>();
						checkDownloadEntireTicket(productBoxNames, productsFilterList, box);
						for (com.ericsson.swgw.da.swgw.bean.TicketXMLGenerator.Boxes.Box eachBox : box) {
							String productNumber = eachBox.getProductNumber();
							if((ProductFilter.isVerifyProducts() && !ProductFilter.isDownloadEntireTicket()) && !productsFilterList.contains(productNumber)){
								continue;
							} 
							String productVersion = eachBox.getVersion();
							if(StringUtils.isOnlyWhitespaceOrEmpty(productVersion)){
								productVersion = eachBox.getRState();
							}
							productBoxes.add(productNumber+ "-" +productVersion);
							logger.info("Begin of processing the product......."+productNumber+ "-"+productVersion);
							String destDir = IniConfig.getSwgwDownloadDir();
							List<String> boxlistFiles = Scanning.boxlistFiles(sftpChannel,productNumber,productVersion,remoteDir);
							List<String> transferBoxFilesList = Scanning.transferFiles(sftpChannel,boxlistFiles,remoteDir,destDir);
							File sigFile = null;
							File manifestFile = null;
							File boxFile = null;
							File nmsDocFile=null;
							for(String transferBoxNames : transferBoxFilesList){
								if(transferBoxNames.contains(productNumber+ "-" +productVersion) && transferBoxNames.endsWith(".sig")){
									 sigFile = new File(downloadDir,transferBoxNames);
								}else if(transferBoxNames.contains(productNumber+ "-" +productVersion)&& transferBoxNames.endsWith(".sha256")){
									 manifestFile = new File(downloadDir,transferBoxNames);
								}else if(transferBoxNames.contains(productNumber+ "-" +productVersion)&&transferBoxNames.endsWith(".zip")){
									 boxFile = new File(downloadDir,transferBoxNames);
								}else if(transferBoxNames.contains(productNumber+ "-" +productVersion)&&transferBoxNames.endsWith(".xml")){
									 nmsDocFile = new File(downloadDir,transferBoxNames);
								}else{
									logger.error("-----------files are not found-------------------");
								}
								
							}
								boolean isSignsuccess = false;
								if(null != sigFile  &&  null != manifestFile){
									isSignsuccess = SignatureValidation.boxSignatureValidation(sigFile.getAbsolutePath(),manifestFile.getAbsolutePath());
								}else{
									logger.error("Either sigFile or manifestFile are null hence cant proceed to further...signature validation");
									failureFiles.add(boxFile);
									failureFiles.add(sigFile);
									failureFiles.add(manifestFile);
									failureFiles.add(nmsDocFile);
									failedProductBoxes.add(productNumber+ "-"+productVersion);
									break;
								}
								
								if(isSignsuccess){
									logger.info("Signature Validation success for Sign File:: "+sigFile + "&& Manifest File :: "+manifestFile);
									successFiles.add(sigFile);
									successFiles.add(manifestFile);
									String checksumMissMatch = "";
									org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject();
									String ticketxmlSHA256 = eachBox.getSHA256();
									if (!StringUtils.isOnlyWhitespaceOrEmpty(ticketxmlSHA256)) {
										json.put(JSONFIELD_SHA256, ticketxmlSHA256);
									}
									String ticketxmlMD5 = eachBox.getMD5();
									if (!StringUtils.isOnlyWhitespaceOrEmpty(ticketxmlMD5)) {
										json.put(JSONFIELD_MD5, ticketxmlMD5);
									}
									if(null != boxFile){
										String sha256 = ChecksumUtils.calculateChecksum(boxFile,
												Constants.CHECKSUM_SHA256);
									String md5 = ChecksumUtils.calculateChecksum(boxFile,
												Constants.CHECKSUM_MD5);
									checksumMissMatch = ChecksumValidation.validateBoxChecksumWithTicketXmlFile(sha256, md5, json);
									if (!StringUtils.isOnlyWhitespaceOrEmpty(checksumMissMatch)){
										logger.error("Checksum Validation failed for "+ boxFile +":"+checksumMissMatch );
										failureFiles.add(boxFile);
										failureFiles.add(sigFile);
										failureFiles.add(manifestFile);
										failureFiles.add(nmsDocFile);
										failedProductBoxes.add(productBoxes+ "-"+productVersion);
										break;
										}else{
											logger.info("Checksum Validation Success for "+boxFile);
											successProductBoxes.add(productNumber+ "-"+productVersion);
											successFiles.add(boxFile);
											successFiles.add(sigFile);
											successFiles.add(manifestFile);
											if(null != nmsDocFile){
												successFiles.add(nmsDocFile);
											}else{
												failureFiles.add(nmsDocFile);
											}
											logger.info("SMO/NMS  Files Completed");
										}
									}else{
										failureFiles.add(sigFile);
										failureFiles.add(manifestFile);
										failedProductBoxes.add(productBoxes+ "-"+productVersion);
										logger.error("The transfer of product failed for"+productBoxes+ "-"+productVersion);
										}
									}else{
										logger.error("Signature Validation Failed for Sign File:: "+sigFile + "&& Manifest File :: "+manifestFile);
										failureFiles.add(sigFile);
										failureFiles.add(manifestFile);
										failureFiles.add(boxFile);
										failureFiles.add(nmsDocFile);
										failedProductBoxes.add(productNumber+ "-"+productVersion);
										break;
									}
								logger.info("End of processing the product......."+productNumber+ "-"+productVersion);
							}
			
						if(productBoxes.size() > 0 && (productBoxes.size() == successProductBoxes.size())&& failureFiles.size()== 0){
							logger.info("All Box Validations are success");
							String strTicketFileName= ticketXML.getTicketID() + Constants.XML_EXT;
							  File transferTicketManifestFile = transferTicketManifestFile(remoteDir, downloadDir, ticketXML,
									strTicketFileName);
							boolean isMoved = FileUtils.moveFileToDestination(logsPath,downloadDir,ticketXMlfile.getName(),strTicketFileName);
							if(isMoved) {
								successFiles.add(transferTicketManifestFile);
								successFiles.add(new File(downloadDir,strTicketFileName));
								FileUtils.deleteFile(logsPath,ticketXMlfile.getName());
							} else {
								logger.error("unable to move file from source to Destination ::" + logsPath,downloadDir,ticketXMlfile.getName());
							}
							if(Boolean.TRUE.equals(FileTransfer.isDownloadOnly())){
								logger.info("Begin of file transfer.........");
								successTransferedFiles = FileTransfer.transferFiles(successFiles);
                                logger.info("End of file transfer.........");
                                for(File transferedFile : successTransferedFiles ){
									if (transferedFile.exists()){
										transferedFile.delete();
									}
									logger.info("Cleaning the local DA Store after sucessfull File Transfer");
								}
                            }
							
						} else if (!ProductFilter.isVerifyProducts()){

							//XML file delete
							FileUtils.deleteFile(logsPath,ticketXMlfile.getName());
							
							//cleaning success Boxes with Sig & Manifest Files and SMO & NMO Files if any Box Checksum validation fails 
								for(File successList : successFiles ){
									if (null != successList && successList.exists()){
										logger.info("successFiles::::"+successList.getName());
										successList.delete();
									}
								}
								
							//cleaning Sig & Manifest Files and SMO & NMO Files
								for(File failureList : failureFiles ){
									if (null != failureList && failureList.exists()){
										logger.info("failureFiles::::"+failureList.getName());
										failureList.delete();
									}
								}
							
						}else {
							//XML file delete
							FileUtils.deleteFile(logsPath,ticketXMlfile.getName());
							
							//cleaning success Boxes with Sig & Manifest Files and SMO & NMO Files if any Box Checksum validation fails 
							for(File successList : successFiles ){
								if (null != successList && successList.exists()){
									logger.info("successFiles::::"+successList.getName());
									successList.delete();
								}
							}
							
						//cleaning Sig & Manifest Files and SMO & NMO Files
							for(File failureList : failureFiles ){
								if (null != failureList && failureList.exists()){
									logger.info("failureFiles::::"+failureList.getName());
									failureList.delete();
								}
							}
						}
						
					}else {
						logger.error("Ticket XML Not found ::" + logsPath + Constants.BACK_SLASH + ticketXMlfile.getName() );
						FileUtils.deleteFile(logsPath,ticketXMlfile.getName());
					}	
				}
			}else{
				logger.info("There are no new files arrived into the directory");
			}
			logger.info("......................./End of DA Run");
		} catch (SftpException | JSchException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void checkDownloadEntireTicket(List<String> productBoxNames,
			List<String> productsFilterList,
			List<TicketXMLGenerator.Boxes.Box> box) throws CustomException {
		for (com.ericsson.swgw.da.swgw.bean.TicketXMLGenerator.Boxes.Box eachBox : box) {
			String productNumber = eachBox.getProductNumber();
			productBoxNames.add(productNumber);
		}
		Boolean isDownloadEntireTicket = false;
		if(Boolean.TRUE.equals(ProductFilter.isDownloadEntireTicket())){
			List<String> collectProducts = productsFilterList.stream()
		       .filter(productBoxNames::contains)
		       .collect(Collectors
		                    .toList());
			if(collectProducts.isEmpty()){
				throw new CustomException("Download Entire Ticket is enabled and Product Filter didn't matched" );
			}
		}
	}

	private File transferTicketManifestFile(String remoteDir, String downloadDir,
			TicketXMLGenerator ticketXML, String strTicketFileName)
			throws SftpException {
		String destFileName="";
		sftpChannel.cd(remoteDir);
		  @SuppressWarnings("unchecked")
		Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(".");
		  for(int i=0; i<files.size();i++){
			  ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) files.get(i);
			  if(entry.getFilename().startsWith(ticketXML.getTicketID()) && entry.getFilename().endsWith(Constants.MANIFEST_SHA256_EXTN)){
				  String remoteTktXmlManifestFileName = entry.getFilename();
				  StringBuilder sbDestFile = new StringBuilder();
				  sbDestFile.append(downloadDir);
				  sbDestFile.append(Constants.BACK_SLASH);
				  sbDestFile.append(strTicketFileName);
				  sbDestFile.append(Constants.MANIFEST_SHA256_EXTN);
				  destFileName=sbDestFile.toString();
				  sftpChannel.get(remoteTktXmlManifestFileName, destFileName);
				  logger.info("Transfer of Ticket-Xml Manifest File......"+destFileName);
			  }
		}
		return new File(destFileName);
	}
}
