package com.ericsson.swgw.da.bean;


/*###################################################################################
#  @Name           :   Products.java
#
#  @Created        :   Sep Drop 2022
#
#  @Description    :   To store products from payload request
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2209
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      29-Aug-2022            created products model object
######################################################################################*/

public class Products {

	private String number;

	private String version;

	private Checksum checksum;

	private String functionDesignation;

	private String externalAccess;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Checksum getChecksum() {
		return checksum;
	}

	public void setChecksum(Checksum checksum) {
		this.checksum = checksum;
	}

	public String getFunctionDesignation() {
		return functionDesignation;
	}

	public void setFunctionDesignation(String functionDesignation) {
		this.functionDesignation = functionDesignation;
	}

	public String getExternalAccess() {
		return externalAccess;
	}

	public void setExternalAccess(String externalAccess) {
		this.externalAccess = externalAccess;
	}
}
