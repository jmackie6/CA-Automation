package org.lds.cm.content.automation.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.lds.cm.content.automation.model.QAContentPublish;
import org.lds.cm.content.automation.model.QAProcessLog;
import org.lds.cm.content.automation.service.QAPublishService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lds.cm.content.automation.util.NetUtils.httpGetJSONWithParams;

public class QADocumentUtils {

    public static String getDocumentID(String filePath) throws Exception{
        Document currentDoc = XMLUtils.getDocumentFromFilePath(filePath);
        String docIDXPath = "//lds:title[@type='file']";
        return XPathUtils.getStringValue(docIDXPath, currentDoc, true);
    }

    public static String getDocumentID(Document currentDoc) throws Exception{
        String docIDXPath = "//lds:title[@type='file']";
        return XPathUtils.getStringValue(docIDXPath, currentDoc, true);

    }

    public static String getDocumentIdHTMLString(String xmlString) throws Exception{
        Document currentDoc = XMLUtils.getDocumentFromStringHTML(xmlString);
        return getDocumentID(currentDoc);
    }

    public static String getDocumentTableID(String filePath) throws Exception{
        String tableId = getDocumentIdHTMLString(filePath);
        String docSQL = "SELECT d.DOCUMENT_ID FROM DOCUMENT d WHERE d.FILE_ID LIKE '%" + tableId + "%'";
        ResultSet rs = JDBCUtils.getResultSet(docSQL);
        if(rs.next()){
            return rs.getString("DOCUMENT_ID");
        }

        return null;
    }

    public static boolean hasDataAidInTable(String fileId) throws SQLException {
        String sqlQuery = "SELECT DATA_AID FROM DOCUMENT d WHERE d.FILE_ID LIKE '%" + fileId +"%'";
        ResultSet rs = JDBCUtils.getResultSet(sqlQuery);
        return rs.getString("DATA_AID") != null;
    }

    public static boolean isFileLocked(String fileId) throws SQLException{
        String sqlQuery = "SELECT STATUS FROM DOCUMENT d WHERE d.FILE_ID LIKE '%" + fileId + "%'";
        ResultSet rs = JDBCUtils.getResultSet(sqlQuery);
        return rs.getString("STATUS").equals("LOCKED");
    }

    public static boolean publishIfNotPublished(String filePath) throws Exception {
        String userID = JDBCUtils.getTestUserID();
		String tableDocId = QADocumentUtils.getDocumentTableID(filePath);
		final QAContentPublish publishObj = QAContentPublish.fromDBWithDocumentID(filePath);
        QAProcessLog processLog = null;
		if(publishObj != null && !publishObj.isPublished()){
			processLog = QAPublishService.publishFile(Long.valueOf(tableDocId));
		}

		return processLog.wasSuccessfullyPublished();


    }

    public static String getUriFromDocument(String filePath) throws Exception{
        Document currentDoc = getDocFromFilePath(filePath);
        String xPathGetURL = "/*/@data-uri";
        return XPathUtils.getStringValue(xPathGetURL, currentDoc, false);


    }

    private static Document getDocFromFilePath(String filePath) throws Exception{
        return XMLUtils.getDocumentFromFilePath(filePath);
    }

    public static JSONObject getDocumentIdFromUriAndLang(String languageID, String uri) throws Exception{ String documentIdUrl = Constants.epGetDocumentIdFromLanguageIdAndUri;
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("languageId", languageID);
        paramMap.put("uri", uri);
        JSONObject returnObject = httpGetJSONWithParams(documentIdUrl, paramMap);
        return returnObject;
    }

}
