package com.ericsson.swgw.da.xml.parser;

/*###################################################################################
#  @Name           :   Ticket.java
#
#  @Created        :   Oct Drop 2022
#
#  @Description    :   To parse Ticket XML file into pojos to extract checksum values from Box
#
#  @Programmer     :   Eswar D
#
#  @Organization   :   HCL
#
#  @Release        :   MR2210
#
#  @History        :
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   description
#      ZDARESW(Eswar)      21-Sept-2022            To parse Ticket XML file into pojos
######################################################################################*/

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "ticketID", "header", "description", "boxes" })
@XmlRootElement(name = "Ticket")
public class Ticket {
	
	private static  Logger logger = LogManager.getLogger(Ticket.class);
	
	@XmlElement(name = "TicketID", required = false)
	protected String ticketID;
	@XmlElement(name = "Header", required = false)
	protected String header;
	@XmlElement(name = "Description", required = false)
	protected String description;
	@XmlElement(name = "Boxes", required = true)
	protected Ticket.Boxes boxes;

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Ticket.Boxes getBoxes() {
		return boxes;
	}

	public void setBoxes(Ticket.Boxes boxes) {
		this.boxes = boxes;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "box" })
	public static class Boxes {
		@XmlElement(name = "Box", required = false)
		protected List<Ticket.Boxes.Box> box;

		public List<Ticket.Boxes.Box> getBox() {
			if (box == null) {
				box = new ArrayList<>();
			}
			logger.info("=====boxPOJO======= new ArrayList<Box>();======="
							+ box);
			return this.box;
		}

		public void setBox(List<Box> box) {
			this.box = box;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "productNumber", "rState",
				"boxDescription", "fileSize", "productType",
				"functionDesignation", "mD5", "sHA256", "boxFilename",
				"documents" })
		@XmlRootElement(name = "Boxes")
		public static class Box {
			@XmlElement(name = "ProductNumber", required = false)
			private String productNumber;

			@XmlElement(name = "RState", required = false)
			private String rState;

			@XmlElement(name = "Description", required = false)
			private String boxDescription;

			@XmlElement(name = "FileSize", required = false)
			private Integer fileSize;

			@XmlElement(name = "ProductType", required = false)
			private String productType;

			@XmlElement(name = "FunctionDesignation", required = false)
			private String functionDesignation;

			@XmlElement(name = "MD5", required = true)
			private String mD5;

			@XmlElement(name = "SHA256", required = true)
			private String sHA256;

			@XmlElement(name = "Filename", required = false)
			private String boxFilename;

			@XmlElement(name = "Documents", required = false)
			private Documents documents;

			public String getSHA256() {
				return sHA256;
			}

			public void setSHA256(String sHA256) {
				this.sHA256 = sHA256;
			}

			public String getDescription() {
				return boxDescription;
			}

			public void setDescription(String boxDescription) {
				this.boxDescription = boxDescription;
			}

			public String getFunctionDesignation() {
				return functionDesignation;
			}

			public void setFunctionDesignation(String functionDesignation) {
				this.functionDesignation = functionDesignation;
			}

			public Documents getDocuments() {
				return documents;
			}

			public void setDocuments(Documents documents) {
				this.documents = documents;
			}

			public String getProductType() {
				return productType;
			}

			public void setProductType(String productType) {
				this.productType = productType;
			}

			public String getFilename() {
				return boxFilename;
			}

			public void setFilename(String boxFilename) {
				this.boxFilename = boxFilename;
			}

			public String getRState() {
				return rState;
			}

			public void setRState(String rState) {
				this.rState = rState;
			}

			public String getProductNumber() {
				return productNumber;
			}

			public void setProductNumber(String productNumber) {
				this.productNumber = productNumber;
			}

			public Integer getFileSize() {
				return fileSize;
			}

			public void setFileSize(Integer fileSize) {
				this.fileSize = fileSize;
			}

			public String getMD5() {
				return mD5;
			}

			public void setMD5(String mD5) {
				this.mD5 = mD5;
			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "document" })
			public static class Documents {
				@XmlElement(name = "Document", required = false)
				protected List<Ticket.Boxes.Box.Documents> document;

				public List<Ticket.Boxes.Box.Documents> getBox() {
					if (document == null) {
						document = new ArrayList<>();
					}
					logger.info("=====boxPOJO======= new ArrayList<Box>();======="
									+ document);
					return this.document;
				}

				public void setDocument(
						List<Ticket.Boxes.Box.Documents> document) {
					this.document = document;
				}

				@XmlAccessorType(XmlAccessType.FIELD)
				@XmlType(name = "", propOrder = { "documentNumber", "revState",
						"filename", "dataFormat", "languageCode" })
				@XmlRootElement(name = "Documents")
				public static class Document {
					@XmlElement(name = "LanguageCode", required = false)
					protected String languageCode;

					@XmlElement(name = "RevState", required = false)
					protected String revState;

					@XmlElement(name = "DataFormat", required = false)
					protected String dataFormat;

					@XmlElement(name = "Filename", required = false)
					protected String filename;

					@XmlElement(name = "DocumentNumber", required = false)
					protected String documentNumber;

					public String getLanguageCode() {
						return languageCode;
					}

					public void setLanguageCode(String languageCode) {
						this.languageCode = languageCode;
					}

					public String getRevState() {
						return revState;
					}

					public void setRevState(String revState) {
						this.revState = revState;
					}

					public String getDataFormat() {
						return dataFormat;
					}

					public void setDataFormat(String dataFormat) {
						this.dataFormat = dataFormat;
					}

					public String getFilename() {
						return filename;
					}

					public void setFilename(String filename) {
						this.filename = filename;
					}

					public String getDocumentNumber() {
						return documentNumber;
					}

					public void setDocumentNumber(String documentNumber) {
						this.documentNumber = documentNumber;
					}
				}
			}
		}

	}

}
