package com.ericsson.swgw.da.bean;

/*###################################################################################
#  @Name           :   Checksum.java
#
#  @Created        :   Sep Drop 2022
#
#  @Description    :   To store checksum values from payload 
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2209
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      29-Aug-2022            created checksum model object
######################################################################################*/

public class Checksum {

	private String sha256;
	private String md5;

	public String getSHA256() {
		return sha256;
	}

	public void setSHA256(String sHA256) {
		sha256 = sHA256;
	}

	public String getMD5() {
		return md5;
	}

	public void setMD5(String mD5) {
		md5 = mD5;
	}

}
