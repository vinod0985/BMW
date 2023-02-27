package com.ericsson.swgw.da.validations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ericsson.swgw.da.bean.Checksum;
import com.ericsson.swgw.da.bean.Products;
import com.ericsson.swgw.da.bean.Webhook;
import com.ericsson.swgw.da.constants.Constants;
import com.ericsson.swgw.da.utils.StringUtils;


/*###################################################################################
#  @Name           :   PayloadValidation.java
#
#  @Created        :   Sep Drop 2022
#
#  @Description    :   To validate all the Payload fields.
#
#  @Programmer     :   Rajni Kumari
#
#  @Organization   :   HCL
#
#  @Release        :   MR2209
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZUAMJRK(Rajni)      2-Sept-2022            Created Payload validation to validate all the mandatory fields.
######################################################################################*/


public class PayloadValidation {
	
	private static  Logger logger = LogManager.getLogger(PayloadValidation.class);
	
	@SuppressWarnings("unchecked")
	public static String validatePayload(Map<String, Object> payload, Webhook webhook) {
		String message = "";

		if(payload.containsKey(Constants.TICKET) && !(Constants.NULL).equals(((String) payload.get(Constants.TICKET))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) payload.get(Constants.TICKET))){
			webhook.setTicket((String) payload.get(Constants.TICKET));
		} else {
			return Constants.ERROR_TICKET;
		}

		if(payload.containsKey(Constants.EUFTGROUPNAME) && !(Constants.NULL).equals(((String) payload.get(Constants.EUFTGROUPNAME))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) payload.get(Constants.EUFTGROUPNAME))){
			webhook.setEuftGroupName((String) payload.get(Constants.EUFTGROUPNAME));
		} else {
			return Constants.ERROR_EUFT_GROUPNAME;
		}

		if(payload.containsKey(Constants.TICKETHEADER) && !(Constants.NULL).equals(((String) payload.get(Constants.TICKETHEADER))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) payload.get(Constants.TICKETHEADER))){
			webhook.setTicketHeader((String) payload.get(Constants.TICKETHEADER));
		} else {
			return Constants.ERROR_TICKET_HEADER;
		}

		if(payload.containsKey(Constants.EUFT) && !(Constants.NULL).equals(((String) payload.get(Constants.EUFT))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) payload.get(Constants.EUFT))){
			webhook.setEuft((String) payload.get(Constants.EUFT));
		} else {
			return Constants.ERROR_EUFT_NO;
		}

		if(payload.containsKey(Constants.PRODUCTS)){

			//List payloadProductsList = (List) payload.get(Constants.PRODUCTS);
			List payloadProductsList = new ArrayList();
			LinkedHashMap payloadProductsMap = new LinkedHashMap();
			Object payloadProductsObject =  payload.get(Constants.PRODUCTS);

			if (payloadProductsObject instanceof List){
				payloadProductsList = (List) payloadProductsObject;

				logger.debug("productsList::::::::"+payloadProductsList);
				List<Products> productList  = new ArrayList<Products>();

				for (Object product: payloadProductsList){
					Map<String, Object> mapProduct = (Map<String, Object>) product;
					logger.debug("mapProduct::::::::"+mapProduct);

					Products products = new  Products();

					if(mapProduct.containsKey(Constants.NUMBER) && !(Constants.NULL).equals(((String) mapProduct.get(Constants.NUMBER))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapProduct.get(Constants.NUMBER))){
						products.setNumber((String) mapProduct.get(Constants.NUMBER));
					} else {
						return Constants.ERROR_PRODUCT_NO;
					}

					if(mapProduct.containsKey(Constants.VERSION) && !(Constants.NULL).equals(((String) mapProduct.get(Constants.VERSION))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapProduct.get(Constants.VERSION))){
						products.setVersion((String) mapProduct.get(Constants.VERSION));
					} else {
						return Constants.ERROR_PRODUCT_VERSION;
					}

					if(mapProduct.containsKey(Constants.FUNCTIONDESIGNATION) && !(Constants.NULL).equals(((String) mapProduct.get(Constants.FUNCTIONDESIGNATION))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapProduct.get(Constants.FUNCTIONDESIGNATION))){
						products.setFunctionDesignation((String) mapProduct.get(Constants.FUNCTIONDESIGNATION));
					} else {
						return Constants.ERROR_FUNCTION_DESIGNATION;
					}

					if(mapProduct.containsKey(Constants.EXTERNALACCESS) && !(Constants.NULL).equals(((String) mapProduct.get(Constants.EXTERNALACCESS))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapProduct.get(Constants.EXTERNALACCESS))){
						products.setExternalAccess((String) mapProduct.get(Constants.EXTERNALACCESS));
					} else {
						return Constants.ERROR_EXTERNAL_ACCESS;
					}

					if(mapProduct.containsKey(Constants.CHECKSUM)){
						Checksum checksum = new Checksum();
						Map<String, Object> mapChecksum = (Map<String, Object>) mapProduct.get(Constants.CHECKSUM);

						if(mapChecksum.containsKey(Constants.SHA256) && !(Constants.NULL).equals(((String) mapChecksum.get(Constants.SHA256))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapChecksum.get(Constants.SHA256))){
							checksum.setSHA256((String) mapChecksum.get(Constants.SHA256));
						} else {
							return Constants.ERROR_SHA256;
						}

						if(mapChecksum.containsKey(Constants.MD5) && !(Constants.NULL).equals(((String) mapChecksum.get(Constants.MD5))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapChecksum.get(Constants.MD5))){
							checksum.setMD5((String) mapChecksum.get(Constants.MD5));
						} else {
							return Constants.ERROR_MD5;
						}

						products.setChecksum(checksum);
					} else {
						return Constants.ERROR_CHECKSUM;
					}

					productList.add(products);
				} 	

				webhook.setProducts(productList);

			}else if ( payloadProductsObject instanceof LinkedHashMap ){
				
				payloadProductsMap = (LinkedHashMap<?, ?>) payloadProductsObject;
				logger.debug("payloadProductsMap::::"+payloadProductsMap);
				List<Products> productList  = new ArrayList<Products>();
				Set<?> entrySet = payloadProductsMap.entrySet();
				Iterator<?> it = entrySet.iterator();
				while(it.hasNext())  
				{  
					Map.Entry<String, Object> product = (Entry<String, Object>) it.next();
					logger.debug(it);
					Map<String, Object> mapProduct =  (Map<String, Object>) product.getValue();  				
					Products products = new  Products();

					if(mapProduct.containsKey(Constants.NUMBER) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapProduct.get(Constants.NUMBER))){
						products.setNumber((String) mapProduct.get(Constants.NUMBER));
					} else {
						return Constants.ERROR_PRODUCT_NO;
					}

					if(mapProduct.containsKey(Constants.VERSION) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapProduct.get(Constants.VERSION))){
						products.setVersion((String) mapProduct.get(Constants.VERSION));
					} else {
						return Constants.ERROR_PRODUCT_VERSION;
					}

					if(mapProduct.containsKey(Constants.FUNCTIONDESIGNATION) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapProduct.get(Constants.FUNCTIONDESIGNATION))){
						products.setFunctionDesignation((String) mapProduct.get(Constants.FUNCTIONDESIGNATION));
					} else {
						return Constants.ERROR_FUNCTION_DESIGNATION;
					}

					if(mapProduct.containsKey(Constants.EXTERNALACCESS) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapProduct.get(Constants.EXTERNALACCESS))){
						products.setExternalAccess((String) mapProduct.get(Constants.EXTERNALACCESS));
					} else {
						return Constants.ERROR_EXTERNAL_ACCESS;
					}

					if(mapProduct.containsKey(Constants.CHECKSUM)){
						Checksum checksum = new Checksum();
						Map<String, Object> mapChecksum = (Map<String, Object>) mapProduct.get(Constants.CHECKSUM);

						if(mapChecksum.containsKey(Constants.SHA256) && !(Constants.NULL).equals(((String) mapChecksum.get(Constants.SHA256))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapChecksum.get(Constants.SHA256))){
							checksum.setSHA256((String) mapChecksum.get(Constants.SHA256));
						} else {
							return Constants.ERROR_SHA256;
						}

						if(mapChecksum.containsKey(Constants.MD5) && !(Constants.NULL).equals(((String) mapChecksum.get(Constants.MD5))) && !StringUtils.isOnlyWhitespaceOrEmpty((String) mapChecksum.get(Constants.MD5))){
							checksum.setMD5((String) mapChecksum.get(Constants.MD5));
						} else {
							return Constants.ERROR_MD5;
						}

						products.setChecksum(checksum);
					} else {
						return Constants.ERROR_CHECKSUM;
					}

					productList.add(products);
				} 	

				webhook.setProducts(productList);

			}
		} else {
			return Constants.ERROR_NO_PRODCUTS;
		}

		return message;
	}


}
