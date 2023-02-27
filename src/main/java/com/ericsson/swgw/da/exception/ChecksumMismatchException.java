package com.ericsson.swgw.da.exception;

/*###################################################################################
#  @Name           :   ChecksumMismatchException.java
#
#  @Created        :   Oct Drop 2022
#
#  @Description    :   To throw checksum mismatch exception message .
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2210
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      21-Sept-2022              throw custom checksum mismatch exception message
######################################################################################*/

import java.io.File;

public class ChecksumMismatchException extends Exception {

	private static final String DEFAULT_ERROR_MSG = "Checksum Mismatched for %s:%s";
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param messsage
	 * @param pullBoxFile
	 */
	public ChecksumMismatchException(String messsage,File pullBoxFile) {
		super(String.format(DEFAULT_ERROR_MSG, pullBoxFile, messsage));
	}

}
