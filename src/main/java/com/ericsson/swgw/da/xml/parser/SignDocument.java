package com.ericsson.swgw.da.xml.parser;


import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.ericsson.swgw.da.utils.StringUtils;


@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignDocument {

    public SignDocument() {
    }

//    public SignDocument(com.ericsson.swgw.da.xml.parser.SignDocument documentBean) {
//        this.documentNumber = documentBean.getCurrentDocumentNumber();
//        this.documentRevision = documentBean.getCurrentDocumentRevision();
//        this.fileName = documentBean.getDocumentTitle();
//
//        // TODO add format from GASK add languageCode from GASK
//    }

    // Modified for Eridoc integration
    public SignDocument(String documentNumber,
                    String documentRevision,
                    String languageCode,
                    String format,
                    String fileName) {
        super();
        this.documentNumber = documentNumber;
        this.documentRevision = documentRevision;
        this.languageCode = languageCode;
        this.format = format;
        this.fileName = fileName;
    }

    @XmlElement(name = "document-number")
    private String documentNumber;

    @XmlElement(name = "document-revision")
    private String documentRevision;

    @XmlElement(name = "language-code")
    private String languageCode;

    @XmlElement(name = "format")
    private String format;

    @XmlElement(name = "filename")
    private String fileName;

    // Added for Eridoc integration
    @XmlElement(name = "archive", required = false)
    private String archive;

    @XmlTransient()
    private String gaskFileName;

    // Added for Eridoc integration
    @XmlTransient()
    private String eridocFileName;

    @XmlTransient()
    private String seqNo;

    @XmlTransient()
    private String sha256;

    @XmlTransient()
    private FTPFile ftpFile;

    @XmlTransient()
    private File file;

    @XmlTransient()
    private double fileSize;

    @XmlTransient()
    private String packetId;

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getGaskFileName() {
        return gaskFileName;
    }

    public void setGaskFileName(String gaskFileName) {
        this.gaskFileName = gaskFileName;
    }

    public String getEridocFileName() {
        return eridocFileName;
    }

    public void setEridocFileName(String eridocFileName) {
        this.eridocFileName = eridocFileName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /*
     * public String getArchive() { return archive; }
     */
    public String getArchive() {
        return StringUtils.isOnlyWhitespaceOrEmpty(archive) ? StringUtils.EMPTY_STRING : archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDocumentRevision() {
        return documentRevision;
    }

    public void setDocumentRevision(String documentRevision) {
        this.documentRevision = documentRevision;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = String.valueOf(seqNo);
    }

    public FTPFile getFtpFile() {
        return ftpFile;
    }

    public File getFile() {
        return file;
    }

    public void setFtpFile(FTPFile ftpFile) {
        this.ftpFile = ftpFile;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the sha256
     */
    public String getSha256() {
        return sha256;
    }

    /**
     * @param sha256 the sha256 to set
     */
    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }
}

