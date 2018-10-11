package org.lds.cm.content.automation.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.model.QAContentPublish;
import org.lds.cm.content.automation.model.QAProcessLog;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.w3c.dom.Document;

public class QAPublishService {
	
	public static void publishFile1(Long doc_id) throws IOException, SQLException {
	
	try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
		// https://publish-test.ldschurch.org/content_automation/ws/v1/qaPublishFile?docId=1542213&ldsAccountId=3431781392997528

		String account_id = "3431781392997528";
		String user_id = "81";
		QALockUnlockService.documentLock(doc_id.toString(), user_id);
		HttpPost httppost = new HttpPost(Constants.epqaPublishFile + doc_id + "&ldsAccountId=" + account_id);

		// Commenting out header information until they put it back in to the code.
		//  httppost.addHeader("client_id", Constants.apiClientId);
		//  httppost.addHeader("client_secret", Constants.apiClientSecret);
		
		
		
		
		HttpEntity entity1 = MultipartEntityBuilder.create().addTextBody("file", doc_id.toString()).build();
		httppost.setEntity(entity1);

		CloseableHttpResponse response = httpClient.execute(httppost);
		System.out.println(response);
			
		}	

	}

	public static QAProcessLog publishFile(long docId) throws SQLException, IOException, ParseException{
		String documentID = String.valueOf(docId);
		String accountID = "3431781392997528";
		String userID = "81";
		boolean notFinished = true;
		QAProcessLog processStatus = null;

		QALockUnlockService.documentLock(documentID, userID);
		String publishString = Constants.epqaPublishFile + documentID + "&accountID=" + accountID;
		final CloseableHttpResponse response = NetUtils.getPost(publishString);
		String logString =  EntityUtils.toString(response.getEntity());
		while(notFinished) {
			processStatus = QAProcessLog.getLogFromID(Long.parseLong(logString));
			if(!processStatus.getProcessStatus().equals("SUCCESSFUL")){
				notFinished = false;
			}
			try {
				Thread.sleep(5000);
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
			System.out.println("In the loop " + logString);
		}


		return processStatus;

	}
	
	public static QAContentPublish fromFileIDLike(String fileIDStart) throws SQLException, IOException {
		String sql = "select c.*, NVL2(c.DOCUMENT_XML, (c.DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob FROM content_publish c join DOCUMENT d ON "
                      + " d.FILE_ID LIKE '" + fileIDStart + "%' AND d.DOCUMENT_ID = c.DOCUMENT_ID";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs).get(0);
	}
	
	public static QAContentPublish fromDocumentID(Long documentID) throws SQLException, IOException {
		final String sql = "SELECT c.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob "
				+ " FROM content_publish c WHERE c.document_id = ? and c.status= 'COMPLETE'";
		ArrayList<String> fillIn = new ArrayList<>();
		fillIn.add(documentID + "");
		ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
		QAContentPublish returningValue = listFromResultSet(rs).get(0);
		rs.close();
		return returningValue;
	}

	public static List<QAContentPublish> fromFolderPath(String folderPath) throws SQLException, IOException {
		final String sql = "SELECT c.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob "
				+ "FROM content_publish c join document_uri_vw v on c.document_id = v.document_id " 
				+ "and v.path =?";
		ArrayList<String> fillIn = new ArrayList<>();
		fillIn.add(folderPath);
		ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
		List<QAContentPublish> contents = listFromResultSet(rs);
		rs.close();
		return contents;
	}

	public static List<QAContentPublish> fromURI(String uri) throws SQLException, IOException {
		final String sql = "SELECT c.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob "
				+ "FROM content_publish c join document_uri_vw v on c.document_id = v.document_id " 
				+ "and v.uri = ?";
		ArrayList<String> fillIn = new ArrayList<>();
		fillIn.add(uri);
		ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
		List<QAContentPublish> contents = listFromResultSet(rs);
		rs.close();
		return contents;
	}

	public static QAContentPublish getContentPublishFromURIandLanguageID(String uri, Long langID ) throws SQLException, IOException {
		final String sql = "SELECT c.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob "
				+ "FROM content_publish c join document_uri_vw v on c.document_id = v.document_id " 
				+ "and v.uri = ? join document d on d.document_id = v.document_id and d.language_id = ?";
		ArrayList<String> fillIn = new ArrayList<>();
		fillIn.add(uri);
		fillIn.add(langID + "");
		ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
		QAContentPublish returningValue = listFromResultSet(rs).get(0);
		rs.close();
		return returningValue;
	} 
	
	public static List<QAContentPublish> getRecentlyPublished(int result) throws SQLException, IOException {
		final String sql = "select * from ( select * from content_publish where status = 'COMPLETE' order by UPDATED_DATE desc ) where ROWNUM <= ?";
		ArrayList<String> fillIn = new ArrayList<>();
		fillIn.add(result + "");
		ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
		List<QAContentPublish> contents = listFromResultSet(rs);
		rs.close();
		return contents;
	} 
	
	private static List<QAContentPublish> listFromResultSet(ResultSet rs) throws SQLException, IOException {
		List<QAContentPublish> documents = new ArrayList<>();
		while (rs.next()) {
			QAContentPublish doc = new QAContentPublish();
			
			doc.setContentPublishID(rs.getLong("CONTENT_PUBLISH_ID"));
			doc.setContentVersion(rs.getLong("content_version"));
			doc.setCreatedDate(rs.getTimestamp("created_date"));
			doc.setDamID(rs.getString("dam_id"));
			doc.setDataAid(rs.getString("data_aid"));
			doc.setDocumentId(rs.getLong("document_id"));
			
			/*Clob temp = rs.getClob("document_xml_clob");
			String clobAsString = JDBCUtils.clobToString(temp);
			doc.setDocumentXML(clobAsString);*/
			
			doc.setPublishedByAppUserID(rs.getLong("published_by_app_user_id"));
			doc.setStatus(rs.getString("status"));
			doc.setUpdatedDate(rs.getTimestamp("updated_date"));
			
			
			documents.add(doc);
		}
		//rs.close();
		return documents;
	}

	public static boolean isPublishedFromDocID(Document currentDoc){
		String docFileId = "";
		String sqlString = "SELECT d.document_id FROM document d inner join content_publish on cp.document_id = d.document_id where d.file_id like '%" + docFileId + "%'";

		return false;
	}

	public static void deleteFromDatabase(String documentID){

	}

}
