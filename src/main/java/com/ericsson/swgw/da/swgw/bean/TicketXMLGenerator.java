package com.ericsson.swgw.da.swgw.bean;

/*###################################################################################
#  @Name           :   TicketXMLGenerator.java
#
#  @Created        :   
#
#  @Description    :   XML Generator template to Ticket XML Preparation
#
#  @Programmer     :  
#
#  @Organization   :   
#
#  @History        :
#
#      Xsignum/Esignum :   Date(DD-MM-YYYY)    :   Description
#
#
####################################################################################*/

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
{ "ticketID", "header", "description", "boxes" })
@XmlRootElement(name = "Ticket")
public class TicketXMLGenerator {
	
	@XmlElement(name = "TicketID", required = true)
    protected String ticketID;

    @XmlElement(name = "Header", required = false)
    protected String header;

    @XmlElement(name = "Description", required = false)
    protected String description;

    @XmlElement(name = "Boxes", required = false)
    protected TicketXMLGenerator.Boxes boxes;

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String value) {
        this.ticketID = value;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String value) {
        this.header = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public TicketXMLGenerator.Boxes getBoxes() {
        return boxes;
    }

    public void setBoxes(TicketXMLGenerator.Boxes value) {
        this.boxes = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder =
    { "box" })
    public static class Boxes {

        @XmlElement(name = "Box")
        private List<TicketXMLGenerator.Boxes.Box> box;

        public List<TicketXMLGenerator.Boxes.Box> getBox() {
            if (box == null) {
                box = new ArrayList<TicketXMLGenerator.Boxes.Box>();
            }
            return this.box;
        }

        public void setBox(List<TicketXMLGenerator.Boxes.Box> box) {
            this.box = box;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder =
        { "productNumber",
          "version",
          "rState",
          "typeOfProduct",
          "description",
          "fileSize",
          "productType",
          "functionDesignation",
          "md5",
          "sha256",
          "filename",
        "documents" })
        public static class Box {

            @XmlElement(name = "ProductNumber", required = false)
            protected String productNumber;

            // MR-2004 : Demand 95591 : To Replace R-state with Version
            @XmlElement(name = "Version", required = false)
            protected String version;

            @XmlElement(name = "RState", required = false)
            protected String rState;
            
            @XmlElement(name = "TypeOfProduct", required = false)
            protected String typeOfProduct;

            @XmlElement(name = "ProductType", required = false)
            protected String productType;
            
            @XmlElement(name = "Description", required = false)
            protected String description;

            @XmlElement(name = "FileSize", required = false)
            protected String fileSize;

            @XmlElement(name = "FunctionDesignation", required = false)
            protected String functionDesignation;

            @XmlElement(name = "MD5", required = false)
            protected String md5;

            @XmlElement(name = "SHA256", required = false)
            protected String sha256;

            @XmlElement(name = "Filename", required = false)
            protected String filename;

            @XmlElement(name = "Documents", required = false)
            protected TicketXMLGenerator.Boxes.Box.Documents documents;

            public String getProductNumber() {
                return productNumber;
            }

            public void setProductNumber(String value) {
                this.productNumber = value;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String value) {
                this.version = value;
            }

            public String getRState() {
                return rState;
            }

            public void setRState(String value) {
                this.rState = value;
            }

            public String getTypeOfProduct() {
                return typeOfProduct;
            }

            public void setTypeOfProduct(String typeOfProduct) {
                this.typeOfProduct = typeOfProduct;
            }
            
            public String getProductType() {
                return productType;
            }

            public void setProductType(String ProductType) {
                this.productType = ProductType;
            }
            
            public String getDescription() {
                return description;
            }

            public void setDescription(String value) {
                this.description = value;
            }

            public String getFileSize() {
                return fileSize;
            }

            public void setFileSize(String value) {
                this.fileSize = value;
            }

            public String getFunctionDesignation() {
                return functionDesignation;
            }

            public void setFunctionDesignation(String value) {
                this.functionDesignation = value;
            }

            /**
             * Gets the value of the md5 property.
             *
             * @return possible object is {@link String }
             *
             */
            public String getMD5() {
                return md5;
            }

            /**
             * Sets the value of the md5 property.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setMD5(String value) {
                this.md5 = value;
            }

            /**
             * Gets the value of the md5 property.
             *
             * @return possible object is {@link String }
             *
             */
            public String getSHA256() {
                return sha256;
            }

            /**
             * Sets the value of the md5 property.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setSHA256(String value) {
                this.sha256 = value;
            }

            /**
             * Gets the value of the filename property.
             *
             * @return possible object is {@link String }
             *
             */
            public String getFilename() {
                return filename;
            }

            /**
             * Sets the value of the filename property.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setFilename(String value) {
                this.filename = value;
            }

            /**
             * Gets the value of the documents property.
             *
             * @return possible object is
             *         {@link TicketXMLGenerator.Boxes.Box.Documents }
             *
             */
            public TicketXMLGenerator.Boxes.Box.Documents getDocuments() {
                return documents;
            }

            /**
             * Sets the value of the documents property.
             *
             * @param value allowed object is
             *        {@link TicketXMLGenerator.Boxes.Box.Documents }
             *
             */
            public void setDocuments(TicketXMLGenerator.Boxes.Box.Documents value) {
                this.documents = value;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder =
            { "document" })
            public static class Documents {

                @XmlElement(name = "Document")
                private List<TicketXMLGenerator.Boxes.Box.Documents.Document> document;

                public List<TicketXMLGenerator.Boxes.Box.Documents.Document> getDocument() {
                    if (document == null) {
                        document = new ArrayList<TicketXMLGenerator.Boxes.Box.Documents.Document>();
                    }
                    return this.document;
                }

                public void setDocument(List<TicketXMLGenerator.Boxes.Box.Documents.Document> document) {
                    this.document = document;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder =
                { "documentNumber", "revState", "filename", "dataFormat", "languageCode" })
                public static class Document {

                    @XmlElement(name = "DocumentNumber", required = false)
                    protected String documentNumber;

                    @XmlElement(name = "RevState", required = false)
                    protected String revState;

                    @XmlElement(name = "Filename", required = false)
                    protected String filename;

                    @XmlElement(name = "DataFormat", required = false)
                    protected String dataFormat;

                    @XmlElement(name = "LanguageCode", required = false)
                    protected String languageCode;

                    /**
                     * Gets the value of the documentNumber property.
                     *
                     * @return possible object is {@link String }
                     *
                     */
                    public String getDocumentNumber() {
                        return documentNumber;
                    }

                    /**
                     * Sets the value of the documentNumber property.
                     *
                     * @param value allowed object is {@link String }
                     *
                     */
                    public void setDocumentNumber(String value) {
                        this.documentNumber = value;
                    }

                    /**
                     * Gets the value of the revState property.
                     *
                     * @return possible object is {@link String }
                     *
                     */
                    public String getRevState() {
                        return revState;
                    }

                    /**
                     * Sets the value of the revState property.
                     *
                     * @param value allowed object is {@link String }
                     *
                     */
                    public void setRevState(String value) {
                        this.revState = value;
                    }

                    /**
                     * Gets the value of the filename property.
                     *
                     * @return possible object is {@link String }
                     *
                     */
                    public String getFilename() {
                        return filename;
                    }

                    /**
                     * Sets the value of the filename property.
                     *
                     * @param value allowed object is {@link String }
                     *
                     */
                    public void setFilename(String value) {
                        this.filename = value;
                    }

                    /**
                     * Gets the value of the dataFormat property.
                     *
                     * @return possible object is {@link String }
                     *
                     */
                    public String getDataFormat() {
                        return dataFormat;
                    }

                    /**
                     * Sets the value of the dataFormat property.
                     *
                     * @param value allowed object is {@link String }
                     *
                     */
                    public void setDataFormat(String value) {
                        this.dataFormat = value;
                    }

                    /**
                     * Gets the value of the languageCode property.
                     *
                     * @return possible object is {@link String }
                     *
                     */
                    public String getLanguageCode() {
                        return languageCode;
                    }

                    /**
                     * Sets the value of the languageCode property.
                     *
                     * @param value allowed object is {@link String }
                     *
                     */
                    public void setLanguageCode(String value) {
                        this.languageCode = value;
                    }
                }
            }
        }
    }
}
