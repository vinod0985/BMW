package com.ericsson.swgw.da.bean;

/*###################################################################################
#  @Name           :   Webhook.java
#
#  @Created        :   Sep Drop 2022
#
#  @Description    :   To store webhook payload 
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2209
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      29-Aug-2022            created webhook model object
######################################################################################*/

import java.util.ArrayList;
import java.util.List;

public class Webhook {
	private String ticket;
	
	private String euftGroupName;
	
	private String ticketHeader;
	
	private String euft;
	
	private List<Products> products;
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getEuftGroupName() {
		return euftGroupName;
	}

	public void setEuftGroupName(String euftGroupName) {
		this.euftGroupName = euftGroupName;
	}

	public String getTicketHeader() {
		return ticketHeader;
	}

	public void setTicketHeader(String ticketHeader) {
		this.ticketHeader = ticketHeader;
	}

	public String getEuft() {
		return euft;
	}

	public void setEuft(String euft) {
		this.euft = euft;
	}

	public List<Products> getProducts() {
		return products;
	}

	public void setProducts(List<Products> products) {
		this.products = products;
	}
	
	public List<String> getWHProductNames(Webhook webhook) {
		List<String> productsFilterList = new ArrayList<String>();
		List<Products> products = webhook.getProducts();
		
		for (Products product : products) {
			productsFilterList.add(product.getNumber());
		}		
		
		return productsFilterList;
	}

}
