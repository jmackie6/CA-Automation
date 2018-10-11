package org.lds.cm.content.automation.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.lds.cm.content.automation.util.JDBCUtils;

public class QADocumentDetail {
	
	private String document_ID;
	private String content_type;
	private int data_aid;
   

	public String getDocumentID() {
		return document_ID;
	}
	public void setDocumentID(String document_ID) {
		this.document_ID = document_ID;
	}
	public String getContentType() {
		return content_type;	
		//return languageCode;
	}

	//Not sure why an int is passed in?
	public void setContentType(int contenttype) {
		this.content_type = contenttype + "";
	}
	public String getData_AID() {
		return String.format("%03d", data_aid);	
	}
	public void setDataAID(int dataaid) {
		this.data_aid= dataaid;
	}
	
    private static final Map<Integer, QADocumentDetail> CONTENT_CENTRAL_ID_MAP = new HashMap<>();
    private static final Map<String,QADocumentDetail>DATA_AID_CODE_MAP = new HashMap<>();
    static {
        try {
            ResultSet rs = JDBCUtils.getResultSet("select * from document");
            while(rs.next()) {
                String documentID = rs.getString("document_ID");
                int contentCentralID = rs.getInt("content_type");
                QADocumentDetail doc = new QADocumentDetail();
               DATA_AID_CODE_MAP.put(documentID, doc);
                CONTENT_CENTRAL_ID_MAP.put(contentCentralID, doc);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to fetch languages from the database: " + e.getMessage());
        }
    }

    public static QADocumentDetail fromContentCentralID(int id) {
        return CONTENT_CENTRAL_ID_MAP.get(id);
    }

	

}