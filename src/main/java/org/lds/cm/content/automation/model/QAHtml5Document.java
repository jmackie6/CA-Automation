package org.lds.cm.content.automation.model;

import java.sql.Timestamp;

public class QAHtml5Document {

    private String citation;
    private String contentType;
    private Timestamp createdDate;
    private Long documentId;
    private String dataAid;
    private Boolean defaultPreviewFlag;
    private String documentXML;
    private String fileId;
    private String fileName;
    private Long folderID;
    private int languageID;
    private Long mediaXmlID;
    private Long modifiedByAppUserID;
    private Timestamp modifiedDate;
    private Long ownerAppUserID;
    private Long oxygenEditAppUserId;
    private Timestamp oxygenEditDate;
    private long statusAppUserID;
    private Timestamp statusChangeDate;
    private Long transformProcessLogID;
    private Boolean validated;
    private String approved;
    private String path;
    private String systemLock;
    private int documentStateID;
    private Timestamp xmlModifiedDate;

    public QAHtml5Document(Long documentId, Timestamp createdDate,
            Long modifiedByAppUserID, Timestamp modifiedDate, int languageID,
            Timestamp oxygenEditDate, Long oxygenEditAppUserId,
            String documentXML, String fileName, Timestamp previewStateDate,
            Long previewStateAppUserID, Timestamp inTranslationStateDate,
            Long inTranslationStateAppUserID,
            Timestamp translationCompleteDate,
            Long translationCompleteAppUserID, Timestamp completeStateDate,
            String citation, String dataAid, String fileId, String contentType,
            Long completeStateAppUserID, Timestamp sourceLockDate,
            Long sourceLockAppUserID, Long ownerAppUserID, Long mediaXmlID,
            Long transformProcessLogID, Boolean defaultPreviewFlag, Boolean validated,
            Long folderID, String status, long statusAppUserID, Timestamp statusChangeDate,
            String approved, String path, String systemLock, int documentStateID, Timestamp xmlModifiedDate) {
        super();
        this.approved = approved;
        this.path = path;
        this.systemLock = systemLock;
        this.xmlModifiedDate = xmlModifiedDate;
        this.citation = citation;
        this.dataAid = dataAid;
        this.fileId = fileId;
        this.contentType = contentType;
        this.documentId = documentId;
        this.createdDate = createdDate;
        this.modifiedByAppUserID = modifiedByAppUserID;
        this.modifiedDate = modifiedDate;
        this.languageID = languageID;
        this.oxygenEditDate = oxygenEditDate;
        this.oxygenEditAppUserId = oxygenEditAppUserId;
        this.documentXML = documentXML;
        this.fileName = fileName;
        this.ownerAppUserID = ownerAppUserID;
        this.mediaXmlID = mediaXmlID;
        this.transformProcessLogID = transformProcessLogID;
        this.defaultPreviewFlag = defaultPreviewFlag;
        this.validated = validated;
        this.folderID = folderID;
        this.statusAppUserID = statusAppUserID;
        this.statusChangeDate = statusChangeDate;
        this.documentStateID = documentStateID;

    }

    public boolean isEqualTo(QAHtml5Document compareTo) {
        boolean temp = (this.approved.equals(compareTo.approved)
                && this.path.equals(compareTo.path)
                && this.systemLock.equals(compareTo.systemLock)
                && this.citation.equals(compareTo.citation)
                && this.dataAid.equals(compareTo.dataAid)
                && this.fileId.equals(compareTo.fileId)
                && this.contentType.equals(compareTo.contentType)
                && this.documentId.equals(compareTo.documentId)
                && this.modifiedByAppUserID.equals(compareTo.modifiedByAppUserID)
                && this.languageID == compareTo.languageID
                && //int
                this.oxygenEditAppUserId.equals(compareTo.oxygenEditAppUserId)
                && this.documentXML.equals(compareTo.documentXML)
                && this.fileName.equals(compareTo.fileName)
                && this.ownerAppUserID.equals(compareTo.ownerAppUserID)
                && this.mediaXmlID.equals(compareTo.mediaXmlID)
                && this.transformProcessLogID.equals(compareTo.transformProcessLogID)
                && this.defaultPreviewFlag.equals(compareTo.defaultPreviewFlag)
                && this.validated.equals(compareTo.validated)
                && this.folderID.equals(compareTo.folderID)
                && this.documentStateID == compareTo.documentStateID
                && this.statusAppUserID == compareTo.statusAppUserID); // long

        if (!temp) {
            return false;
        }

        if (this.xmlModifiedDate != null && compareTo.xmlModifiedDate != null) {
            if (this.xmlModifiedDate.getTime() != compareTo.xmlModifiedDate.getTime()) {
                return false;
            }
        }

        if (this.createdDate != null && compareTo.createdDate != null) {
            if (this.createdDate.getTime() != compareTo.createdDate.getTime()) {
                return false;
            }
        }

        if (this.modifiedDate != null && compareTo.modifiedDate != null) {
            if (this.modifiedDate.getTime() != compareTo.modifiedDate.getTime()) {
                return false;
            }
        }

        if (this.oxygenEditDate != null && compareTo.oxygenEditDate != null) {
            if (this.oxygenEditDate.getTime() != compareTo.oxygenEditDate.getTime()) {
                return false;
            }
        }

        if (this.statusChangeDate != null && compareTo.statusChangeDate != null) {
            if (this.statusChangeDate.getTime() != compareTo.statusChangeDate.getTime()) {
                return false;
            }
        }

        return temp;

    }

    public QAHtml5Document() {
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Long getModifiedByAppUserID() {
        return modifiedByAppUserID;
    }

    public void setModifiedByAppUserID(Long modifiedByAppUserID) {
        this.modifiedByAppUserID = modifiedByAppUserID;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getLanguageID() {
        return languageID;
    }

    public void setLanguageID(int languageID) {
        this.languageID = languageID;
    }

    public Timestamp getOxygenEditDate() {
        return oxygenEditDate;
    }

    public void setOxygenEditDate(Timestamp oxygenEditDate) {
        this.oxygenEditDate = oxygenEditDate;
    }

    public Long getOxygenEditAppUserId() {
        return oxygenEditAppUserId;
    }

    public void setOxygenEditAppUserId(Long oxygenEditAppUserId) {
        this.oxygenEditAppUserId = oxygenEditAppUserId;
    }

    public String getDocumentXML() {
        String documentXMLWithDeclaration = documentXML;
        if (!documentXMLWithDeclaration.startsWith("<!DOCTYPE html>")) {
            documentXMLWithDeclaration = "<!DOCTYPE html>" + documentXML;
        }
        return documentXMLWithDeclaration;
    }

    public void setDocumentXML(String documentXML) {
        this.documentXML = documentXML;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public String getDataAid() {
        return dataAid;
    }

    public void setDataAid(String dataAid) {
        this.dataAid = dataAid;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getOwnerAppUserID() {
        return ownerAppUserID;
    }

    public void setOwnerAppUserID(Long ownerAppUserID) {
        this.ownerAppUserID = ownerAppUserID;
    }

    public Long getMediaXmlID() {
        return mediaXmlID;
    }

    public void setMediaXmlID(Long mediaXmlID) {
        this.mediaXmlID = mediaXmlID;
    }

    public Long getTransformProcessLogID() {
        return transformProcessLogID;
    }

    public void setTransformProcessLogID(Long processLogID) {
        this.transformProcessLogID = processLogID;
    }

    public Boolean getDefaultPreviewFlag() {
        return defaultPreviewFlag;
    }

    public void setDefaultPreviewFlag(Boolean defaultPreviewFlag) {
        this.defaultPreviewFlag = defaultPreviewFlag;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public Long getFolderID() {
        return folderID;
    }

    public void setFolderID(Long folderID) {
        this.folderID = folderID;
    }

    public Long getStatusAppUserID() {
        return statusAppUserID;
    }

    public void setStatusAppUserID(long statusAppUserID) {
        this.statusAppUserID = statusAppUserID;
    }

    public Timestamp getStatusChangeDate() {
        return statusChangeDate;
    }

    public void setStatusChangeDate(Timestamp statusChangeDate) {
        this.statusChangeDate = statusChangeDate;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSystemLock() {
        return systemLock;
    }

    public void setSystemLock(String systemLock) {
        this.systemLock = systemLock;
    }

    public int getDocumentStateID() {
        return documentStateID;
    }

    public void setDocumentStateID(int documentStateID) {
        this.documentStateID = documentStateID;
    }

    public Timestamp getXmlModifiedDate() {  return xmlModifiedDate;    }

    public void setXmlModifiedDate(Timestamp xmlModifiedDate) {
        this.xmlModifiedDate = xmlModifiedDate;
    }

    public boolean compareStrings(String first, String second) {
        if (first == null && second == null) {
            return true;
        }
        if (first == null) {
            first = "";
        }
        if (second == null) {
            second = "";
        }
        return first.equals(second);
    }
}
