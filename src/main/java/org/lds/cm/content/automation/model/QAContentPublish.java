package org.lds.cm.content.automation.model;

import oracle.xdb.XMLType;
import org.lds.cm.content.automation.enums.PublishStatus;
import org.lds.cm.content.automation.util.JDBCUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class QAContentPublish {
	private Long contentPublishID; 
	private Long contentVersion; 
	private String damID;
	private String status;
	private Long publishedByAppUserID;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private Long documentId;
	private String documentXML;
	private String dataAid;
	
	
	public Long getContentPublishID() {
		return contentPublishID;
	}

	public void setContentPublishID(Long contentPublishID) {
		this.contentPublishID = contentPublishID;
	}

	public Long getContentVersion() {
		return contentVersion;
	}

	public void setContentVersion(Long contentVersion) {
		this.contentVersion = contentVersion;
	}

	public String getDamID() {
		return damID;
	}

	public void setDamID(String damID) {
		this.damID = damID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getPublishedByAppUserID() {
		return publishedByAppUserID;
	}

	public void setPublishedByAppUserID(Long publishedByAppUserID) {
		this.publishedByAppUserID = publishedByAppUserID;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public void setDataAid(String dataAid) {
		this.dataAid = dataAid;
	}

	public String getDocumentXML() {
		return documentXML;
	}
	
	public void setDocumentXML(String documentXML) {
		this.documentXML = documentXML;
	}
	
	public String getDataAid() {
		return dataAid;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(128);
		builder.append(QAContentPublish.class.getSimpleName());
		builder.append("(id=").append(this.getDocumentId());
		return builder.toString();
	}

	public boolean isEmpty() {
		return false;
	}

	public boolean isPublished() {
		PublishStatus publishStatus = PublishStatus.fromString(status);

		if(publishStatus == PublishStatus.COMPLETE){
			return true;
		}

		return false;
	}

	public static QAContentPublish fromDBWithDocumentID(String documentID) throws SQLException {
		final QAContentPublish returnObj = new QAContentPublish();
		boolean getRow = false;
		String getContentPublishFromDBsql = "SELECT "
											+ "CP.CONTENT_PUBLISH_ID "
											+ ", CP.CONTENT_VERSION "
                                            + ", CP.DAM_ID "
                                            + ", CP.STATUS "
                                            + ", CP.PUBLISHED_BY_APP_USER_ID "
                                            + ", CP.CREATED_DATE "
                                            + ", CP.UPDATED_DATE "
                                            + ", CP.DOCUMENT_ID "
                                            + ", CP.DOCUMENT_XML "
                                            + ", CP.DATA_AID "
											+ " FROM CONTENT_PUBLISH CP "
                                            + " WHERE CP.DOCUMENT_ID = " + documentID;
		final ResultSet rs = JDBCUtils.getResultSet(getContentPublishFromDBsql);


		if(rs.next()){
			returnObj.setContentPublishID(rs.getBigDecimal("CONTENT_PUBLISH_ID").longValue());
			returnObj.setContentVersion(rs.getBigDecimal("CONTENT_VERSION").longValue());
			returnObj.setDamID(rs.getString("DAM_ID"));
			returnObj.setStatus(rs.getString("STATUS"));
			returnObj.setPublishedByAppUserID(rs.getLong("PUBLISHED_BY_APP_USER_ID"));
			returnObj.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
			returnObj.setUpdatedDate(rs.getTimestamp("UPDATED_DATE"));
			returnObj.setDocumentId(rs.getLong("DOCUMENT_ID"));
			XMLType tempXML = (XMLType)rs.getObject("DOCUMENT_XML");
			returnObj.setDocumentXML(tempXML.toString());
			returnObj.setDataAid(rs.getString("DATA_AID"));
			getRow = true;
		}

		return getRow == true ? returnObj : null;
	}


}
