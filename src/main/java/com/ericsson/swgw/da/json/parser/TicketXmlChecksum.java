package com.ericsson.swgw.da.json.parser;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketXmlChecksum {
	
	@JsonProperty("sha256")
	protected String sHA256;

	@JsonProperty("md5")
	protected String mD5;
	
	public String getsHA256() {
		return sHA256;
	}

	public void setsHA256(String sHA256) {
		this.sHA256 = sHA256;
	}

	public String getmD5() {
		return mD5;
	}

	public void setmD5(String mD5) {
		this.mD5 = mD5;
	}
}
