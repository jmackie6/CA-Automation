package org.lds.cm.content.automation.model;

import org.lds.cm.content.automation.enums.ContentType;
import org.lds.cm.content.automation.enums.ContentTypeFull;

	

public class QADocInfo {
	//TODO - add lists for audio URLs, video URLs, text (pdf) URLs
	
	private String language;
	private int languageId;
	private String URI;
	private String fileId;
	private String aID;//data-aid at the document level.
	private ContentTypeFull contentTypeFull;
	
	
	private String xmlLanguage;
	private String xmlFileId;
	private String xmlVersion;
	private ContentType xmlContentType;
	private String xmlDataContentType;
	private String xmlCitation;
	
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getLanguageId() {
		return languageId;
	}
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public String getXmlLanguage() {
		return xmlLanguage;
	}
	public void setXmlLanguage(String xmlLanguage) {
		this.xmlLanguage = xmlLanguage;
	}
	public String getXmlFileId() {
		return xmlFileId;
	}
	public void setXmlFileId(String xmlFileId) {
		this.xmlFileId = xmlFileId;
	}
	public String getXmlVersion() {
		return xmlVersion;
	}
	public Long getVersionAsLong() {
		try {
			return Long.parseLong(xmlVersion);
		} catch (NumberFormatException e){
			return null;
		}
	}
	
	public void setXmlVersion(String xmlVersion) {
		this.xmlVersion = xmlVersion;
	}
	public String getaID() {
		return aID;
	}
	public void setaID(String aID) {
		this.aID = aID;
	}
	public ContentType getXmlContentType() {
		return xmlContentType;
	}
	public Long getContentTypeId() {
		return Long.valueOf(xmlContentType.getEnumID());
	}
	public void setXmlContentType(ContentType xmlContentType) {
		this.xmlContentType = xmlContentType;
	}
	public String getXmlDataContentType() {
		return xmlDataContentType;
	}
	public void setXmlDataContentType(String xmlDataContentType) {
		this.xmlDataContentType = xmlDataContentType;
	}
	public String getXmlCitation() {
		return xmlCitation;
	}
	public void setCitation(String xmlCitation) {
		this.xmlCitation = xmlCitation;
	}
	
	public ContentTypeFull getContentTypeFull() {
		return this.contentTypeFull;
	}
	
	public void setContentTypeFull(ContentTypeFull type) {
		this.contentTypeFull = type;
	}
}



