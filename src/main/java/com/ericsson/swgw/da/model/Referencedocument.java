package com.ericsson.swgw.da.model;

/*###################################################################################
#  @Name           :   Referencedocument.java
#
#  @Created        :   
#
#  @Description    :   
#
#  @Programmer     :  
#
#  @Organization   :   
#
#  @History        :
#
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   Description
#
#
####################################################################################*/


import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import com.ericsson.swgw.da.bean.Webhook;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.controller.Listener;
import com.ericsson.swgw.da.swgw.bean.SWGReferenceDocs;
import com.ericsson.swgw.da.swgw.bean.SWGReferenceDocs.ENMDocuments;
import com.ericsson.swgw.da.swgw.bean.SWGReferenceDocs.ENMDocuments.ENMDocument;
import com.ericsson.swgw.da.swgw.bean.SWGReferenceDocs.ReleaseDocs;
import com.ericsson.swgw.da.swgw.bean.SWGReferenceDocs.ReleaseDocs.ReleaseDoc;
import com.ericsson.swgw.da.utils.PullUtils;
import com.ericsson.swgw.da.utils.StringUtils;


public class Referencedocument {
	
	private static  Logger logger = LogManager.getLogger(Referencedocument.class);
	public static Webhook webhook;
	
	public Referencedocument(Webhook lWebhook) {
		webhook = lWebhook;
	}

	public static ReleaseDoc getSignatureFileBean(SWGReferenceDocs refDocXML) {
		
		if(refDocXML != null){
			ReleaseDocs  releaseDocs=  refDocXML.getReleaseDocs();
			List<ReleaseDoc> listReleaseDocs = releaseDocs.getReleaseDoc();
			for (ReleaseDoc releaseDoc : listReleaseDocs) {
				String strDocTitle = releaseDoc.getDocumentTitle();
				logger.debug("strDocTitle:::::::::::::"+strDocTitle);
				if(Constants.SIGN_File.equalsIgnoreCase(strDocTitle)){
					return releaseDoc;
				}
			}
		}		
		return null;
	}

	public static ReleaseDocs getReleaseDocsBean(SWGReferenceDocs refDocXML) {
		
		if(refDocXML != null){
			return refDocXML.getReleaseDocs();
		}
		
		return null;
	}

	public static File getsignatureFile(ReleaseDoc signatureFile, String downloadDir) throws JSONException, SQLException {
		String strSignDocumentNumber = signatureFile.getDocumentNumber().replace("/", "_").replaceAll("\\s", "");
		String strURL = signatureFile.getURL();

		logger.debug("strSignDocumentNumber::::::::::::::"+strSignDocumentNumber);
		logger.debug("strURL::::::::::::::"+strURL);
		if(!StringUtils.isOnlyWhitespaceOrEmpty(strURL)){
			System.out.println("downloadDir::::::::::::::"+strSignDocumentNumber);

			Map returnMap = PullUtils.pullMethod(strURL, strSignDocumentNumber, downloadDir);
			System.out.println("downloadDir::::::::::::::"+downloadDir);
			java.io.File file =  null;
			if(returnMap.containsKey("file")){
				file = (File) returnMap.get("file");
			}else {
				file = new java.io.File(downloadDir + Constants.BACK_SLASH+ strSignDocumentNumber);
			}
			return file;
		}
		
		
		return null;
	}

	public static ReleaseDoc getManifestFileBean(SWGReferenceDocs refDocXML) {

		if(refDocXML != null){
			ReleaseDocs  releaseDocs=  refDocXML.getReleaseDocs();
			List<ReleaseDoc> listReleaseDocs = releaseDocs.getReleaseDoc();
			for (ReleaseDoc releaseDoc : listReleaseDocs) {
				String strDocTitle = releaseDoc.getDocumentTitle();
				System.out.println("strDocTitle:::::::::::::"+strDocTitle);
				if(Constants.MANIFEST_File.equalsIgnoreCase(strDocTitle)){
					return releaseDoc;
				}
			}
		}		
		return null;
	}

	public static File getManifestFile(ReleaseDoc  manifestFileBean , String downloadDir) throws JSONException, SQLException {
		
		String strManifestDocNo = manifestFileBean.getDocumentNumber().replace("/", "_").replaceAll("\\s", "");
		String strURL = manifestFileBean.getURL();

		System.out.println("strManifestDocNo::::::::::::::"+strManifestDocNo);
		System.out.println("strURL::::::::::::::"+strURL);
		if(!StringUtils.isOnlyWhitespaceOrEmpty(strURL)){			
			System.out.println("downloadDir::::::::::::::"+strManifestDocNo);
				
			Map returnMap = PullUtils.pullMethod(strURL, strManifestDocNo, downloadDir);
			System.out.println("downloadDir::::::::::::::"+downloadDir);
			java.io.File file =  null;
			if(returnMap.containsKey("file")){
				file = (File) returnMap.get("file");
			}else {
				file = new java.io.File(downloadDir + Constants.BACK_SLASH+ strManifestDocNo);
			}
			return file;
		}
		
		return null;
	}

	public static List<ENMDocument> getENMDocuments(SWGReferenceDocs refDocXML) {
		
		if(refDocXML != null){
			ENMDocuments enmDocuments = refDocXML.ENMDocuments();	
			if(enmDocuments != null){
				return enmDocuments.getENMDocument();
			}			
		}		
		return null;
	}

	public static File getENMDocumentFile(ENMDocument enmDocument,String downloadDir) throws JSONException, SQLException {
			
		String strENMDocNo = enmDocument.getDocumentNumber().replace("/", "_").replaceAll("\\s", "");
		String strURL = enmDocument.getURL();

		System.out.println("strManifestDocNo::::::::::::::"+strENMDocNo);
		System.out.println("strURL::::::::::::::"+strURL);
		if(!StringUtils.isOnlyWhitespaceOrEmpty(strURL)){			
			Map returnMap = PullUtils.pullMethod(strURL, strENMDocNo, downloadDir);
			System.out.println("downloadDir::::::::::::::"+downloadDir);
			java.io.File file =  null;
			if(returnMap.containsKey("file")){
				file = (File) returnMap.get("file");
			}else {
				file = new java.io.File(downloadDir + Constants.BACK_SLASH+ strENMDocNo);
			}			
			return file;
		}
		
		return null;
	}

}
