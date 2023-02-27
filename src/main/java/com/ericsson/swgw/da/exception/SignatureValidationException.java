package com.ericsson.swgw.da.exception;

/*###################################################################################
#  @Name           :   SignatureValidationException.java
#
#  @Created        :   Oct Drop 2022
#
#  @Description    :   To throw signature exception message .
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2210
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      23-Sept-2022              throw custom signature validation exception message
######################################################################################*/

public class SignatureValidationException extends Exception {
	private static final String DEFAULT_ERROR_MSG = "Signature Validation failed for :%s %s";
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param messsage
	 * @param filePath
	 */
	public SignatureValidationException(String messsage,String filePath) {
		super(String.format(DEFAULT_ERROR_MSG, messsage,filePath ));
	}
	
	 public SignatureValidationException(String messsage) {
	            super(messsage);
	 }
	
}
