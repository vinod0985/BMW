package com.ericsson.swgw.da.exception;

/*###################################################################################
#  @Name           :   CustomException.java
#
#  @Created        :   Sep Drop 2022
#
#  @Description    :   To throw custom exception message .
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2209
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      4-Sept-2022              
######################################################################################*/

public class CustomException extends Exception {

	private static final String DEFAULT_ERROR_MSG = "path to where the packages from SWGW shall be downloaded to locally on DA execution server is balnk or not specified correct path";
	private static final long serialVersionUID = 1L;

	public CustomException() {
		super(DEFAULT_ERROR_MSG);
	}

	/**
	 * @param messsage
	 */
	public CustomException(String messsage) {
		
		super( messsage);

	}

}
