package com.ericsson.swgw.da.bean;

public class DMSBean {
	
		private String ticket;
		
		private String euftGroupName;
		
		private String ticketHeader;
		
		private String euft;
		
		private Boolean spinnaker_polling_enabled;		
		
		private String relative_storage_path;
		
		
		
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
		public Boolean getSpinnaker_polling_enabled() {
			return spinnaker_polling_enabled;
		}

		public void setSpinnaker_polling_enabled(Boolean spinnaker_polling_enabled) {
			this.spinnaker_polling_enabled = spinnaker_polling_enabled;
		}

		public String getRelative_storage_path() {
			return relative_storage_path;
		}

		public void setRelative_storage_path(String relative_storage_path) {
			this.relative_storage_path = relative_storage_path;
		}

		public static DMSBean prepareDMSBean(Webhook webhook,String productNumber) {
			DMSBean dmsBean = new DMSBean();
			
			if(null != webhook){
				dmsBean.setEuft(webhook.getEuft());
				dmsBean.setEuftGroupName(webhook.getEuftGroupName());
				dmsBean.setTicket(webhook.getTicket());
				dmsBean.setTicketHeader(webhook.getTicketHeader());
				dmsBean.setSpinnaker_polling_enabled(true);
				dmsBean.setRelative_storage_path(productNumber+"/"+"dummy_test_product_"+webhook.getTicket());
			}
			return dmsBean;
		}

		
}
