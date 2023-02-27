package com.ericsson.swgw.da.swgw.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder =
    { "releaseDocs", "enmDocuments", "description" })
@XmlRootElement(name = "ReferenceDocs")
public class SWGReferenceDocs {


    @XmlElement(name = "ReleaseDocs", required = true)
    protected SWGReferenceDocs.ReleaseDocs releaseDocs;

    @XmlElement(name = "Meta-InformationFile", required = true)
    protected SWGReferenceDocs.ENMDocuments enmDocuments;

    @XmlElement(name = "Description", required = true)
    protected String description;

    /**
     * Gets the value of the releaseDocs property.
     *
     * @return possible object is {@link SWGReferenceDocs.ReleaseDocs }
     *
     */
    public SWGReferenceDocs.ReleaseDocs getReleaseDocs() {
        return releaseDocs;
    }

    /**
     * Sets the value of the releaseDocs property.
     *
     * @param value allowed object is {@link SWGReferenceDocs.ReleaseDocs }
     *
     */
    public void setReleaseDocs(SWGReferenceDocs.ReleaseDocs value) {
        this.releaseDocs = value;
    }

    /**
     * Gets the value of the enmDocuments property.
     *
     * @return possible object is {@link SWGReferenceDocs.ENMDocuments }
     *
     */
    public SWGReferenceDocs.ENMDocuments ENMDocuments() {
        return enmDocuments;
    }

    /**
     * Sets the value of the enmDocuments property.
     *
     * @param value allowed object is {@link SWGReferenceDocs.ENMDocuments }
     *
     */
    public void setENMDocuments(SWGReferenceDocs.ENMDocuments value) {
        this.enmDocuments = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * <p>
     * Java class for anonymous complex type.
     *
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ENMDocument" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="DocumentNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="DocumentRevision" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="DocumentTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder =
        { "enmDocument" })
    public static class ENMDocuments {

        // Demand 91177-Change ENM doc to Meta Information File - MR1910
        @XmlElement(name = "MetaInformationFile", required = true)
        protected List<SWGReferenceDocs.ENMDocuments.ENMDocument> enmDocument;

        /**
         * Gets the value of the enmDocument property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the enmDocument property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         *
         * <pre>
         * getENMDocument().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SWGReferenceDocs.ENMDocuments.ENMDocument }
         *
         *
         */
        public List<SWGReferenceDocs.ENMDocuments.ENMDocument> getENMDocument() {
            if (enmDocument == null) {
                enmDocument = new ArrayList<SWGReferenceDocs.ENMDocuments.ENMDocument>();
            }
            return this.enmDocument;
        }

        public void setENMDocument(List<SWGReferenceDocs.ENMDocuments.ENMDocument> enmDocument) {

            this.enmDocument = enmDocument;
        }

        /**
         * <p>
         * Java class for anonymous complex type.
         *
         * <p>
         * The following schema fragment specifies the expected content
         * contained within this class.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="DocumentNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="DocumentRevision" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="DocumentTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder =
            { "documentNumber", "documentRevision", "documentTitle", "url" })
        public static class ENMDocument {

            @XmlElement(name = "DocumentNumber", required = true)
            protected String documentNumber;

            @XmlElement(name = "DocumentRevision", required = true)
            protected String documentRevision;

            @XmlElement(name = "DocumentTitle", required = true)
            protected String documentTitle;

            @XmlElement(name = "URL", required = true)
            protected String url;

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
             * Gets the value of the documentRevision property.
             *
             * @return possible object is {@link String }
             *
             */
            public String getDocumentRevision() {
                return documentRevision;
            }

            /**
             * Sets the value of the documentRevision property.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setDocumentRevision(String value) {
                this.documentRevision = value;
            }

            /**
             * Gets the value of the documentTitle property.
             *
             * @return possible object is {@link String }
             *
             */
            public String getDocumentTitle() {
                return documentTitle;
            }

            /**
             * Sets the value of the documentTitle property.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setDocumentTitle(String value) {
                this.documentTitle = value;
            }

            /**
             * Gets the value of the url property.
             *
             * @return possible object is {@link String }
             *
             */
            public String getURL() {
                return url;
            }

            /**
             * Sets the value of the url property.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setURL(String value) {
                this.url = value;
            }

        }

    }

    /**
     * <p>
     * Java class for anonymous complex type.
     *
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ReleaseDoc" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="DocumentNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="DocumentRevision" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="DocumentTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder =
        { "releaseDoc" })
    public static class ReleaseDocs {

        @XmlElement(name = "ReleaseDoc", required = true)
        protected List<SWGReferenceDocs.ReleaseDocs.ReleaseDoc> releaseDoc;

        /**
         * Gets the value of the releaseDoc property.
         *
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the releaseDoc property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         *
         * <pre>
         * getReleaseDoc().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SWGReferenceDocs.ReleaseDocs.ReleaseDoc }
         *
         *
         */
        public List<SWGReferenceDocs.ReleaseDocs.ReleaseDoc> getReleaseDoc() {
            if (releaseDoc == null) {
                releaseDoc = new ArrayList<SWGReferenceDocs.ReleaseDocs.ReleaseDoc>();
            }
            return this.releaseDoc;
        }

        public void setReleaseDoc(List<SWGReferenceDocs.ReleaseDocs.ReleaseDoc> releaseDoc) {

            this.releaseDoc = releaseDoc;
        }

        /**
         * <p>
         * Java class for anonymous complex type.
         *
         * <p>
         * The following schema fragment specifies the expected content
         * contained within this class.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="DocumentNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="DocumentRevision" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="DocumentTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder =
            { "documentNumber", "documentRevision", "documentTitle", "url" })
        public static class ReleaseDoc {

            @XmlElement(name = "DocumentNumber", required = true)
            protected String documentNumber;

            @XmlElement(name = "DocumentRevision", required = true)
            protected String documentRevision;

            @XmlElement(name = "DocumentTitle", required = true)
            protected String documentTitle;

            @XmlElement(name = "URL", required = true)
            protected String url;

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
             * Gets the value of the documentRevision property.
             *
             * @return possible object is {@link String }
             *
             */
            public String getDocumentRevision() {
                return documentRevision;
            }

            /**
             * Sets the value of the documentRevision property.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setDocumentRevision(String value) {
                this.documentRevision = value;
            }

            /**
             * Gets the value of the documentTitle property.
             *
             * @return possible object is {@link String }
             *
             */
            public String getDocumentTitle() {
                return documentTitle;
            }

            /**
             * Sets the value of the documentTitle property.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setDocumentTitle(String value) {
                this.documentTitle = value;
            }

            /**
             * Gets the value of the url property.
             *
             * @return possible object is {@link String }
             *
             */
            public String getURL() {
                return url;
            }

            /**
             * Sets the value of the url property.
             *
             * @param value allowed object is {@link String }
             *
             */
            public void setURL(String value) {
                this.url = value;
            }

        }

    }
}
