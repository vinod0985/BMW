package com.ericsson.swgw.da.actions;

/*###################################################################################
 #  @Name           :   TriggerDownloadNotification.java
 #
 #  @Created        :   Sep Drop 2022
 #
 #  @Description    :   To generated access token and pull sw packages from SW Gateway
 #
 #  @Programmer     :   Eswar D
 #
 #  @Organization   :   HCL
 #
 #  @Release        :   MR2209
 #
 #  @History        :
 #      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
 #      ZDARESW(Eswar)      29-Aug-2022            Download action
 ######################################################################################*/

import io.minio.errors.MinioException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import com.ericsson.swgw.da.bean.Checksum;
import com.ericsson.swgw.da.bean.DMSBean;
import com.ericsson.swgw.da.bean.Products;
import com.ericsson.swgw.da.bean.Webhook;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.dms.DMSService;
import com.ericsson.swgw.da.exception.CustomException;
import com.ericsson.swgw.da.exception.SignatureValidationException;
import com.ericsson.swgw.da.filter.ProductFilter;
import com.ericsson.swgw.da.model.Referencedocument;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.services.AzureTokenService;
import com.ericsson.swgw.da.services.PullProductService;
import com.ericsson.swgw.da.services.PullTicketXMLService;
import com.ericsson.swgw.da.services.Referencedocurls;
import com.ericsson.swgw.da.swgw.bean.SWGReferenceDocs;
import com.ericsson.swgw.da.swgw.bean.SWGReferenceDocs.ENMDocuments.ENMDocument;
import com.ericsson.swgw.da.swgw.bean.SWGReferenceDocs.ReleaseDocs.ReleaseDoc;
import com.ericsson.swgw.da.swgw.bean.TicketXMLGenerator;
import com.ericsson.swgw.da.utils.ChecksumUtils;
import com.ericsson.swgw.da.utils.FileUtils;
import com.ericsson.swgw.da.utils.PullUtils;
import com.ericsson.swgw.da.utils.StringUtils;
import com.ericsson.swgw.da.validations.ChecksumValidation;
import com.ericsson.swgw.da.validations.SignatureValidation;
import com.jcraft.jsch.JSchException;

public class TriggerDownloadNotification {

	private static final String HEADER_FIELDS = "headerFields";
	private static final String FILE = "file";
	private static  Logger logger = LogManager.getLogger(TriggerDownloadNotification.class);
	private static final String JSONFIELD_SHA256 = "sha256";
	private static final String JSONFIELD_MD5 = "md5";
	private static final String currentDirectory = System.getProperty(Constants.USER_DIR);


	public TriggerDownloadNotification() {
		throw new IllegalStateException("Action class");
	}


	/**
	 * @param lWebhook
	 *            Received payload stored into beans
	 * @param ldProperties
	 *            to load properties from property file
	 * @throws SignatureValidationException 
	 * @throws CustomException 
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws JAXBException 
	 * @throws JSchException 
	 * @throws MinioException 
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public static void swgwTriggerServices(Webhook webhook) throws SignatureValidationException, CustomException, JSONException, IOException, SQLException, JAXBException, MinioException, JSchException {
		try {
			
			List<String> productBoxNames = webhook.getWHProductNames(webhook);
						
			logger.info("Start DA Pckage products Filter");
			List<String> productsFilterList = ProductFilter.getproductsFilterList();
			logger.info("productsFilterList" +productsFilterList);
			logger.info("END DA Pckage products Filter");
			
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
			
			logger.info("Start DA Pckage Download Flow");
			String bearerToken = AzureTokenService.getswgwAuthenticate();
			
			if (!StringUtils.isOnlyWhitespaceOrEmpty(bearerToken)) {
				logger.info("Bearer token generated. Hence,proceeding further..... ");
				PullTicketXMLService pullTicketXMLService = new PullTicketXMLService(webhook);
				PullProductService pullProductService = new PullProductService(webhook);
				Referencedocurls referencedocurls = new Referencedocurls(webhook) ;
				Referencedocument referencedocument = new Referencedocument(webhook);
				String downloadDir = IniConfig.getSwgwDownloadDir();
				String apiUrl = IniConfig.getSwgwApiURL();
				String logsPath = currentDirectory + Constants.BACK_SLASH + "logs";

				// download ticket-XML in temp path
				String ticketXmlFileName = webhook.getTicket() + Constants.XML_EXT;
				java.io.File ticketXmlFile =  null;
				Map<String, List<String>> responseHeadersTicket =  null;
				List<Products> products = webhook.getProducts();
				String pullTicketxmlUrL = pullTicketXMLService.getPullTicketxmlURL(apiUrl);
				logger.debug("pullTicketxmlUrL ::::"+pullTicketxmlUrL);
				if (!StringUtils.isOnlyWhitespaceOrEmpty(pullTicketxmlUrL)) {
					Map<?, ?> returnMapTicket = pullTicketXMLService.pullTicketxml( logsPath, ticketXmlFileName, pullTicketxmlUrL);
					if(returnMapTicket.containsKey(HEADER_FIELDS)){
						responseHeadersTicket = ((Map<String, List<String>>) returnMapTicket.get(HEADER_FIELDS));
					}
					
					if(returnMapTicket.containsKey(FILE)){
						ticketXmlFile = (File) returnMapTicket.get(FILE);
						System.out.println("pullTicketxml getAbsolutePath() ::::"+ticketXmlFile.getAbsolutePath());
					}
					
					if(ticketXmlFile != null){
						String ticketXmlChecksumMissMatch = checksumValidationTicketXMl(ticketXmlFile, responseHeadersTicket);
						if(!StringUtils.isOnlyWhitespaceOrEmpty(ticketXmlChecksumMissMatch)){
							ticketXmlFile.delete();
							logger.error("Checksum Mismatched for " + ticketXmlFile + " due to "
										+ ticketXmlChecksumMissMatch);
							ChecksumValidation.raiseChecksumException(ticketXmlFile, ticketXmlChecksumMissMatch);
							
							}else{
							logger.info("Checksum Validation success for ticket-xml after downloading into temp "+ ticketXmlFile.getAbsolutePath());
							TicketXMLGenerator  ticketXML = PullTicketXMLService.getTicketXMLGenerator(ticketXmlFile);
							if(ticketXML != null){
							com.ericsson.swgw.da.swgw.bean.TicketXMLGenerator.Boxes boxes = ticketXML.getBoxes();
							List<TicketXMLGenerator.Boxes.Box> box = boxes.getBox();
			 			    List<String> productBoxes = new ArrayList<>();
							List<String> successProductBoxes = new ArrayList<>();
							List<String> failedProductBoxes = new ArrayList<>();
							List<java.io.File> successFiles = new ArrayList<>();
							List<java.io.File> failureFiles = new ArrayList<>();
							List<java.io.File> successTransferedFiles = new ArrayList<>();
							
							for (com.ericsson.swgw.da.swgw.bean.TicketXMLGenerator.Boxes.Box eachBox : box) {
								String productNumber = eachBox.getProductNumber();							
								if((ProductFilter.isVerifyProducts() && !ProductFilter.isDownloadEntireTicket()) && !productsFilterList.contains(productNumber)){
									continue;
								} 
								
								String productVersion = eachBox.getVersion();
								if(StringUtils.isOnlyWhitespaceOrEmpty(productVersion)){
									productVersion = eachBox.getRState();
								}
								productBoxes.add(productNumber+ "-"+productVersion);
								logger.info("Begin of processing the product......."+productNumber+ "-"+productVersion);
								
								SWGReferenceDocs refDocXML =  referencedocurls.getRefdocurls(apiUrl, productNumber, productVersion);
								logger.debug("refDocXML::::::::::::::::"+refDocXML);
								if(refDocXML == null){
									failedProductBoxes.add(productNumber+ "-"+productVersion);
									break;
								}
								//ReleaseDocs  releaseDocs =   Referencedocument.getReleaseDocsBean(refDocXML);
								ReleaseDoc  signatureFile =   Referencedocument.getSignatureFileBean(refDocXML);
								logger.debug("signatureFile::::::::::::::::"+signatureFile);
								if(signatureFile == null){
									failedProductBoxes.add(productNumber+ "-"+productVersion);
									break;
								}
								
								ReleaseDoc  manifestFileBean =   Referencedocument.getManifestFileBean(refDocXML);
								logger.debug("manifestFile::::::::::::::::"+manifestFileBean);
								if(manifestFileBean == null){
									failedProductBoxes.add(productNumber+ "-"+productVersion);
									break;
								}
								logger.debug("downloadDir:::::::::"+downloadDir);
								File manifestFile = Referencedocument.getManifestFile(manifestFileBean,downloadDir);
								logger.debug("manifestFile:::::::::"+manifestFile.getAbsolutePath());
								File signFile = Referencedocument.getsignatureFile(signatureFile,downloadDir);
								logger.debug("signFile:::::::::"+signFile.getAbsolutePath());
								boolean isSignsuccess = false;
								if(signFile.exists() && manifestFile.exists()){
									isSignsuccess = SignatureValidation.boxSignatureValidation(signFile.getAbsolutePath(),manifestFile.getAbsolutePath());
								}else {
									failedProductBoxes.add(productNumber+ "-"+productVersion);
									break;
									//logger.error("signature File Not found in the temporary location ::" + signFile.getAbsolutePath );
								}				
								
								/*Success :: Continue Flow. 
								 *Failed ::  Delete the files (sign file, all Box files for this tickets xml) and
								 *			 stop the flow
								*/
								if(isSignsuccess){
									logger.info("Signature Validation Success");
									successFiles.add(signFile);
									successFiles.add(manifestFile);
									java.io.File pullBoxFile = null;
									String checksumMissMatch = "";
									Map<?, ?> returnMap = pullProductService.pullProduct(downloadDir, productNumber, productVersion);
									Map<String, List<String>> responseHeaders =  new HashMap<String, List<String>>();
									if(returnMap.containsKey(HEADER_FIELDS)){
										responseHeaders = ((Map<String, List<String>>) returnMap.get(HEADER_FIELDS));
									}
									if(returnMap.containsKey(FILE)){
										pullBoxFile = (File) returnMap.get(FILE);
									}
									
									Checksum payloadChecksum = null;
									for(Products product:products){
										if(productNumber.equalsIgnoreCase(product.getNumber()) && productVersion.equalsIgnoreCase(product.getVersion())){
											payloadChecksum = product.getChecksum();
											break;
										}									
									}
									org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject();
									String ticketxmlSHA256 = eachBox.getSHA256();
									if (!StringUtils.isOnlyWhitespaceOrEmpty(ticketxmlSHA256)) {
										json.put(JSONFIELD_SHA256, ticketxmlSHA256);
									}
									String ticketxmlMD5 = eachBox.getMD5();
									if (!StringUtils.isOnlyWhitespaceOrEmpty(ticketxmlMD5)) {
										json.put(JSONFIELD_MD5, ticketxmlMD5);
									}	
									if(null != pullBoxFile){
										checksumMissMatch = ChecksumValidation.parseChecksum(responseHeaders, pullBoxFile,payloadChecksum,json);
										if (!StringUtils.isOnlyWhitespaceOrEmpty(checksumMissMatch)) {
											failureFiles.add(pullBoxFile);
											failureFiles.add(signFile);
											failureFiles.add(manifestFile);
											failedProductBoxes.add(productNumber+ "-"+productVersion);
											break;
										} else {
											logger.info("Checksum Validation Success for "+pullBoxFile);
											successProductBoxes.add(productNumber+ "-"+productVersion);
											successFiles.add(pullBoxFile);
											successFiles.add(signFile);
											successFiles.add(manifestFile);
											//Download SMO/NMS Files for each box
											List<ENMDocument>   enmDocumentList =   Referencedocument.getENMDocuments(refDocXML);
											if(enmDocumentList != null){
												for (ENMDocument enmDocument : enmDocumentList) {
													File fENMDocument = Referencedocument.getENMDocumentFile(enmDocument,downloadDir);
													if(fENMDocument.exists()){
														successFiles.add(fENMDocument);
														//logger.debug("SMO/NMO Document  File  found in the  location ::" + fENMDocument.getAbsolutePath );
													} else {
														failureFiles.add(fENMDocument);
														//logger.error("SMO/NMO Document  File Not found in the  location ::" + fENMDocument.getAbsolutePath );
													}
													
												}
												
											}	
											logger.info("SMO/NMS  Files Completed");
										}
									}else{
										failureFiles.add(signFile);
										failureFiles.add(manifestFile);
										failedProductBoxes.add(productNumber+ "-"+productVersion);
										logger.error("The download of product failed for"+productNumber+ "-"+productVersion);
									}
								} else {
									logger.error("Signature Validation Failed for Sign File:: "+signFile + "&& Manifest File :: "+manifestFile);
									failureFiles.add(signFile);
									failureFiles.add(manifestFile);
									failedProductBoxes.add(productNumber+ "-"+productVersion);
									break;
								}
								logger.info("End of processing the product......."+productNumber+ "-"+productVersion);
							}
							
							//Move Ticket XML from Temp location in to Local Storage
							logger.debug("productBoxes:::::"+productBoxes);
							logger.debug("successProductBoxes:::::"+successProductBoxes);
							logger.debug("failedProductBoxes:::::"+failedProductBoxes);
							if(productBoxes.size() > 0 && (productBoxes.size() == successProductBoxes.size())&& failureFiles.size()== 0){
								logger.info("All Box Validations are success");
								File ticketXmlManifestFile = getTicketXmlManifestFile(responseHeadersTicket,ticketXmlFileName,downloadDir);
								successFiles.add(ticketXmlManifestFile);
								boolean isMoved = FileUtils.moveFileToDestination(logsPath,downloadDir,ticketXmlFileName);
								if(isMoved) {
									File movedTicketXml = new File(downloadDir,ticketXmlFileName);
									String mvXmlChecksumMismatch = checksumValidationTicketXMl(movedTicketXml,responseHeadersTicket);
									if(!StringUtils.isOnlyWhitespaceOrEmpty(mvXmlChecksumMismatch)){
										movedTicketXml.delete();
										logger.error("Checksum Mismatched for " + movedTicketXml + " due to "
													+ mvXmlChecksumMismatch);
										ChecksumValidation.raiseChecksumException(movedTicketXml, mvXmlChecksumMismatch);
									}else{
										logger.info("Checksum Validation success for ticket-xml after moving into DA Store"+ movedTicketXml.getAbsolutePath());
									successFiles.add(movedTicketXml);
									//FileUtils.deleteFile(logsPath,ticketXmlFileName);
									}
								} else {
									logger.error("unable to move file from source to Destination ::" + logsPath,downloadDir,ticketXmlFileName);
								}
								if(FileTransfer.isDownloadOnly()){
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
								
								if(!StringUtils.isOnlyWhitespaceOrEmpty(IniConfig.getDMSURL())){
									for (com.ericsson.swgw.da.swgw.bean.TicketXMLGenerator.Boxes.Box eachBox : box) {
										logger.info("Triggreing DMS for the product "+ eachBox.getProductNumber());
										String productNumber = eachBox.getProductNumber().replace("/", "_").replaceAll("\\s", "");
										DMSBean dmsBean = DMSBean.prepareDMSBean(webhook, eachBox.getProductNumber());							
										int dmsStatusCode = DMSService.sendDMSNotification(dmsBean,eachBox.getProductNumber());
										logger.info("DMS Status Code"+dmsStatusCode);
									}
								}
								
								
							} else if (!ProductFilter.isVerifyProducts()){

								//XML file delete
								//FileUtils.deleteFile(logsPath,ticketXmlFileName);
								
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
								//FileUtils.deleteFile(logsPath,ticketXmlFileName);
								
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
							
						
						} else {
							logger.error("Ticket XML Not found ::" + logsPath + Constants.BACK_SLASH + ticketXmlFileName );
							//FileUtils.deleteFile(logsPath,ticketXmlFileName);
						}
					  }
					}else{
						logger.error("The download of ticket-xml failed. Hence,can't proceed further");
					}
					
					 
				}else {
					logger.error("pullTicketxmlUrL is Empty. ::" + pullTicketxmlUrL);
				}
			}else{
				logger.error("Bearer token failed to generated or it is empty::" +bearerToken );
			}
     		logger.info("End Of DA Download Flow");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} /*catch (JAXBException e1) {
			e1.printStackTrace();
			throw new JAXBException(e1.getMessage());
		}*/ catch (JSONException e2) {
			e2.printStackTrace();
			throw new JSONException(e2.getMessage());
		}
	}


	private static String checksumValidationTicketXMl(java.io.File ticketXmlFile,
			Map<String, List<String>> responseHeadersTicket) {
		String sha256 = ChecksumUtils.calculateChecksum(
					ticketXmlFile, Constants.CHECKSUM_SHA256);
		System.out.println("calculated sha256 for ticket-xml :: "+sha256);
		String md5 = ChecksumUtils.calculateChecksum(
					ticketXmlFile, Constants.CHECKSUM_MD5);
		System.out.println("calculated md5 for ticket-xml :: "+md5);
		String ticketXmlChecksumMissMatch = ChecksumValidation
					.validateTicketXmlChecksumWithResHeader(sha256, md5,responseHeadersTicket);
		return ticketXmlChecksumMissMatch;
	}


	private static File getTicketXmlManifestFile(
			Map<String, List<String>> responseHeadersTicket, String ticketXmlFileName, String downloadDir) throws JSONException, SQLException {
		java.io.File file =  null;
		if(responseHeadersTicket.containsKey(Constants.MANIFEST_SHA256)){
			StringBuilder xmlSHA256 = new StringBuilder(responseHeadersTicket.get(Constants.MANIFEST_SHA256).toString());
			xmlSHA256.deleteCharAt(xmlSHA256.length() - 1);
			xmlSHA256.deleteCharAt(0);
			String headerXmlSHA256Url= xmlSHA256.toString();
			logger.info("Ticket-XML MANIFEST_SHA256 Url .... "+headerXmlSHA256Url);
			StringBuilder txmlSB = new StringBuilder();
			txmlSB.append(ticketXmlFileName);
			txmlSB.append(Constants.MANIFEST_SHA256_EXTN);
			String txmlManifestName = txmlSB.toString();
			Map<String, Object> returnMap = PullUtils.pullMethod(headerXmlSHA256Url,txmlManifestName,downloadDir);
			
			if(returnMap.containsKey("file")){
				file = (File) returnMap.get("file");
			}else {
				file = new java.io.File(downloadDir + Constants.BACK_SLASH+ txmlManifestName);
			}
		}
		return file;
	}
}
