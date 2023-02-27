package com.ericsson.swgw.da.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ericsson.swgw.da.actions.TriggerDownloadNotification;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.constants.IniConstants;
import com.ericsson.swgw.da.properties.IniConfig;
import com.ericsson.swgw.da.utils.StringUtils;

public class ProductFilter {
	private static  Logger logger = LogManager.getLogger(ProductFilter.class);

	public static Boolean isVerifyProducts(){
		Boolean bReturn = false;
		String sVerifyProducts =   IniConfig.getDAVerifyProducts();
		if (!StringUtils.isOnlyWhitespaceOrEmpty(sVerifyProducts) && Constants.TRUE.equalsIgnoreCase(sVerifyProducts)) {
			logger.debug("verify_products is True hence procced for products Filter");
			bReturn = true;
			
		} else {
			logger.debug("verify_products is False hence products Filter Not Required");
		}		
		return bReturn;	
	}
	
	public static Boolean isDownloadEntireTicket(){
		Boolean bReturn = false;
		String sDownloadEntireTicket =   IniConfig.getDADownloadEntireTicket();
		if (!StringUtils.isOnlyWhitespaceOrEmpty(sDownloadEntireTicket) && Constants.TRUE.equalsIgnoreCase(sDownloadEntireTicket)) {
			logger.debug("verify_products is True hence procced for products Filter");
			bReturn = true;
			
		} else {
			logger.debug("verify_products is False hence products Filter Not Required");
		}		
		return bReturn;	
	}
	
	
	public static List<String>  getproductsFilterList (){
		List<String> productsFilterList = new ArrayList<String>();
		if(isVerifyProducts() || isDownloadEntireTicket()){
			List liProducts = IniConfig.getDefaultAttProducts();
			for (Object object : liProducts) {
				String sProductSectionName = (String) object;				
				Map mpProductMap = IniConfig.getProductMap(sProductSectionName.trim());									
				String sSWProductNo =  (String) mpProductMap.get(IniConstants.INI_PRODUCTS_SW_PRODUCT_NO);				
				productsFilterList.add(sSWProductNo);
			}
		}		
		return productsFilterList;
	}
}
