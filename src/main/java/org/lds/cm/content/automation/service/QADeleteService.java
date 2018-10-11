package org.lds.cm.content.automation.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.lds.cm.content.automation.util.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.testng.annotations.Test;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import org.w3c.dom.Document;


public class QADeleteService {
	
	//can use to test deleteFromAnnotationsAndDB method
	@Test
	public static void runDeleteFromAnnotationsAndDB() {
		try {
			deleteDocumentsFromMarkLogic("92905_000");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//Delete from docmaps if data aid exists and then delete from DB using deleteFromDB method. 
	public static void deleteFromAnnotationsMLAndDB(String fileId) throws SQLException {
		
		String newFileId = fileId.substring(0, fileId.lastIndexOf("_")) + "%";
		String mlFileId = fileId.substring(0, fileId.lastIndexOf("_"));
		newFileId = newFileId.trim();
		String getDataAids = "SELECT * FROM DOCUMENT WHERE FILE_ID like '" + newFileId + "' and DATA_AID IS NOT NULL";
		ResultSet rsDataAids = JDBCUtils.getResultSet(getDataAids);
		while(rsDataAids.next()) { 
			QADocMapService.deleteSingleDocmap(rsDataAids.getString("DATA_AID"));
		}

		deleteFromDbByFileIdSingleDocument(newFileId);
		deleteUriMappingFiles(mlFileId);
		deleteDocumentsFromMarkLogic(mlFileId);
		System.out.println("Deleted From DB");

		if(rsDataAids!= null) rsDataAids.close();
	}

	//Deletes document with fileID from database this will delete it from everywhere in the DB
	//must pass in a partial file_id for this bulk function to work so example would be 99265_059
	public static void bulkDeleteFromDbByFileId(String fileId) throws SQLException {
		String getDocuments = "SELECT DOCUMENT_ID FROM DOCUMENT WHERE FILE_ID LIKE '" + fileId + "%'";
		if (!JDBCUtils.getResultSet(getDocuments).isBeforeFirst()) {
			//		String verifyDocumentsAreDeleted = "SELECT DOCUMENT_ID FROM DOCUMENT WHERE PATH LIKE '" + path + "%' AND LANGUAGE_ID = '" + langId + "'";
			System.out.println("No results returned for fileID: " + fileId);
//			Assert.fail();
		}
		else {
			String deleteFromDocument = "BEGIN FOR docNum IN (SELECT DOCUMENT_ID FROM DOCUMENT WHERE FILE_ID LIKE '" + fileId + "%')"
					+ "LOOP DELETE_DOCUMENT(docNum.DOCUMENT_ID); END LOOP; END;";
			JDBCUtils.getResultSet(deleteFromDocument);

			if (!JDBCUtils.getResultSet(getDocuments).isBeforeFirst()) {

				System.out.println("Documents with this " + fileId + " have been deleted from the database.");
			}
			else {
				System.out.println("Documents did not get deleted");
			}
		}
	}
	
	//Deletes document with fileID from database this will delete it from everywhere in the DB
	//Must pass in the complete file_id of the single
	public static void deleteFromDbByFileIdSingleDocument(String fileId) throws SQLException {
		System.out.println("deleting documents by fileId");
		String getDocuments = "SELECT DOCUMENT_ID FROM DOCUMENT WHERE FILE_ID LIKE '" + fileId + "'";

		if (!JDBCUtils.getResultSet(getDocuments).isBeforeFirst()) {
			System.out.println("No results returned for fileID: " + fileId);
//			Assert.fail();
		}
		else {
			String deleteFromDocument = "BEGIN FOR docNum IN (SELECT DOCUMENT_ID FROM DOCUMENT WHERE FILE_ID LIKE '" + fileId + "')"
					+ "LOOP DELETE_DOCUMENT(docNum.DOCUMENT_ID); END LOOP; END;";
			JDBCUtils.getResultSet(deleteFromDocument);

			if (!JDBCUtils.getResultSet(getDocuments).isBeforeFirst()) {

				System.out.println("Document with this " + fileId + " has been deleted from the database.");
			}
			else {
				System.out.println("Document did not get deleted");
			}
		}
	}

	//This function will delete a single document from DB when you pass in a filename, path, and langId like 0 or 2 or 300
	public static void deleteSingleDocumentFromDbByPathLangIdAndFilename(String filename, String path, String langId) throws SQLException {
		System.out.println("deleting documents by URI, langID, and Filename");
		String getDocuments = "SELECT DOCUMENT_ID FROM DOCUMENT WHERE FILE_NAME = '" + filename + "' AND PATH = '" + path + "' AND LANGUAGE_ID = '" + langId + "'";

		if (!JDBCUtils.getResultSet(getDocuments).isBeforeFirst()) {
			System.out.println("No results returned for path: " + path + " and langID: " + langId + "and filename: " + filename);
			Assert.fail();
		}
		else {
			String deleteFromDocument = "BEGIN FOR docNum IN (SELECT DOCUMENT_ID FROM DOCUMENT WHERE FILE_NAME = '" + filename + "' AND PATH = '" + path + "' AND LANGUAGE_ID = '" + langId + "')"
					+ "LOOP DELETE_DOCUMENT(docNum.DOCUMENT_ID); END LOOP; END;";
			JDBCUtils.getResultSet(deleteFromDocument);

			if (!JDBCUtils.getResultSet(getDocuments).isBeforeFirst()) {

				System.out.println("Document with filename:" + filename + ", Path: " + path + ", and langID: " + langId + "has been deleted");
			}
			else {
				System.out.println("Document did not get deleted");
			}
		}
	}

	//This function will bulk delete documents from DB when you pass in a path/URI and langId like 0 or 2 or 300
	public static void bulkDeleteFromDbByLangIdAndPath(String path, String langId) throws SQLException {
		System.out.println("\ndeleting documents by URI and langID\n");
		String getDocuments = "SELECT DOCUMENT_ID FROM DOCUMENT WHERE PATH LIKE '" + path + "%' AND LANGUAGE_ID = '" + langId + "'";
		if (!JDBCUtils.getResultSet(getDocuments).isBeforeFirst()) {
			System.out.println("No results returned for path: " + path + " and langID: " + langId);
			Assert.fail();
		}
		else {
			String deleteFromDocument = "BEGIN FOR docNum IN (SELECT DOCUMENT_ID FROM DOCUMENT WHERE PATH LIKE '" + path + "%' AND LANGUAGE_ID = '" + langId + "')"
					+ "LOOP DELETE_DOCUMENT(docNum.DOCUMENT_ID); END LOOP; END;";
			JDBCUtils.getResultSet(deleteFromDocument);

			if (!JDBCUtils.getResultSet(getDocuments).isBeforeFirst()) {

				System.out.println("Documents with Path: " + path + " and langID: " + langId + " have been deleted");
			}
			else {
				System.out.println("Documents did not get deleted");
			}
		}
	}


	//Will delete all docmaps corresponding to data aids listed in CSV at /content_automation-qa/src/main/java/org/lds/cm/content/automation/service/Documents/DeleteDoc.txt
	@Test
	public static void deleteDocMapsFromTextDoc() {
		String path = "./src/main/java/org/lds/cm/content/automation/service/Documents/DeleteDoc.txt";  
		ArrayList<String> filesToDelete = new ArrayList<>();
		
		try {
			FileInputStream file = new FileInputStream(path);
			Scanner scanner = new Scanner(file);
			String listItem;
			
			while(scanner.hasNextLine()) {
				listItem = scanner.nextLine();
				listItem = listItem.trim();
				
     			if(StringUtils.isNotBlank(listItem) && NumberUtils.isNumber(listItem)) {
					filesToDelete.add(listItem);
				}
			}
		
			scanner.close();
			file.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n");
		System.out.println("Test From Document");
		QADocMapService.bulkDeleteDocmap(filesToDelete);
	}
	

	//You can use this to delete docmaps within a certain range of data aids by changing the start and end ids
	public static void deleteDocMapsFromDBRange() throws SQLException {
		
		ArrayList<String> filesToDelete = new ArrayList<>();

		Connection conn = null;
		try {
			String startID = "1163224";
			String endID  = "1163228";
			
			conn = DriverManager.getConnection(Constants.dbUrl, Constants.dbUsername, Constants.dbPassword);
			Statement stmt = conn.createStatement();
			
			String query = "select document_id from content_publish where document_id between " + "'" + startID + "'" + " and " + "'" + endID + "'" + "";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String documentId = rs.getString("document_id");
				filesToDelete.add(documentId);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally
		{
			if(conn != null)
				conn.close();
		}
		
		System.out.println("\n");
		System.out.println("Test From DB");
		QADocMapService.bulkDeleteDocmap(filesToDelete);
	}
	
	
	//Will delete all docmaps corresponding to data aids listed in CSV at /content_automation-qa/src/main/java/org/lds/cm/content/automation/service/Documents/DeleteCSV.csv
	public static void deleteDocMapsFromCSV() {
		String path = "./src/main/java/org/lds/cm/content/automation/service/Documents/DeleteCSV.csv";
		ArrayList<String> filesToDelete = new ArrayList<>();
		
		
		try {
			FileInputStream file = new FileInputStream(path);
			Scanner scanner = new Scanner(file);
			String listItem; 

			
			while(scanner.hasNextLine()) {
				listItem = scanner.nextLine();
				listItem = listItem.trim();
				
				if(StringUtils.isNotBlank(listItem) && NumberUtils.isNumber(listItem)) {
					
					filesToDelete.add(listItem);
				}
			}
		
			scanner.close();
			file.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n");
		System.out.println("Test From CSV");
		QADocMapService.bulkDeleteDocmap(filesToDelete);
	}
	
    public static void deleteUriMappingFiles(String fileID) {
      //  for (String file : fileID) {
            String path = "/preview/content-automation/uri-mapping/" + fileID + "_000-uris.xml";
            deleteFileFromMarkLogic(path);
            Assert.assertFalse(MarkLogicUtils.docExists(path));
        //}
    }

    public static void deleteDocumentsFromMarkLogic(String fileID) throws SQLException {
        List<String> paths = new ArrayList<>();
        Connection conn = DriverManager.getConnection(Constants.dbUrl, Constants.dbUsername, Constants.dbPassword);
        Statement stmt = conn.createStatement();
       // for (String file : fileIds) {
            String sql = "SELECT path, file_name FROM document WHERE file_id LIKE '" + fileID + "%'";
            ResultSet resultSet = stmt.executeQuery(sql);
            Assert.assertFalse(null == resultSet);
            try {
                while (resultSet.next()) paths.add(resultSet.getString("path") + "/" + resultSet.getString("file_name"));
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
            }
        //}

        for (String path : paths) System.out.println(path); //deleteFileFromMarkLogic("/published/content-automation/content/eng" + path);
    }
	
    
    public static void deleteFileFromMarkLogic(String path) {
		getDatabaseClient().newXMLDocumentManager().delete(path);
	}

    	public static boolean docExists(String path) {
		return getDatabaseClient().newXMLDocumentManager().exists(path) != null;

    	}

    	
    	private static DatabaseClient databaseClient = null;
    	
    	private static DatabaseClient getDatabaseClient() {
		if (null == databaseClient) {
			databaseClient = DatabaseClientFactory.newClient("l14231.ldschurch.org", 9000, "Delivery", "ca-rest-user", "content911", DatabaseClientFactory.Authentication.DIGEST);
		}

			return databaseClient;
    	}


	public static String getLangAbbreviation(String lang_code) {
		//System.out.println("\nFinding Language Abbreviation for Language Code: " + lang_code);
		List<String> abbreviation = new ArrayList<>();

		try {
			ResultSet rs = JDBCUtils.getResultSet("SELECT ISO_LANG_CD_PART2T FROM LANGUAGE WHERE LANGUAGE_CODE = " + lang_code);
			while(rs.next()) {
				abbreviation.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return abbreviation.get(0);
	}

	public static String getLangName(String lang_code) {
		List<String> lang_name = new ArrayList<>();

		try {
			ResultSet rs = JDBCUtils.getResultSet("SELECT LANG_NAME FROM LANGUAGE WHERE LANGUAGE_CODE = " + lang_code);
			while (rs.next()) {
				lang_name.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lang_name.get(0);
	}

	private static String getBasicAuthEncoding(String username, String password) {
		String credStr = username + ":" + password;
		String encoded = Base64.encodeBase64String(credStr.getBytes());
		return encoded;
	}

	public static void bulkDeleteDocmapByUriAndLang(String file_id) {

		String ep = null;
		String uri = null;
		String lang = null;

		try {
			ResultSet rs = JDBCUtils.getResultSet("SELECT PATH, LANGUAGE_ID FROM DOCUMENT WHERE FILE_ID LIKE " + "'" + file_id + "%'");

			while(rs.next()) {
				uri = rs.getString(1);
				lang = getLangAbbreviation(rs.getString(2));

				ep = Constants.epDeleteDocmapByUriAndLanguage + uri + "*" + "&locale=" + lang;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

			CloseableHttpClient client = HttpClients.createDefault();
			HttpDelete httpDelete = new HttpDelete(ep);

			String basicAuth = getBasicAuthEncoding("contentManager",
					"manageTheContent");
			httpDelete.addHeader("Authorization", "Basic " + basicAuth);
			CloseableHttpResponse response = null;

			try {
				response = client.execute(httpDelete);
				System.out.println("\nDeleting documents for the following: ");
				System.out.println("\nURI: " + uri);
				System.out.println("\nLanguage: " + lang);
				System.out.println("\nResults:");
				System.out.println("\nStatus Code: " + response.getStatusLine().getStatusCode());
				String responseBody = EntityUtils.toString(response.getEntity());
				String docs_num = StringUtils.substringBetween(responseBody, "<numberOfDocs>", "</numberOfDocs>");
				System.out.println("\nDocmap Deleted: " + docs_num + "\n");
			}
			catch (ClientProtocolException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	}


	public static void deleteSingleDocmapByUriAndLang(String file_id) {

		System.out.println("\nFinding Path, File Name, and Language ID associated with File ID: " + file_id);
		List<String> uri = new ArrayList<>();

		try {
			ResultSet rs = JDBCUtils.getResultSet("SELECT PATH, FILE_NAME, LANGUAGE_ID FROM DOCUMENT WHERE FILE_ID = " + "'" + file_id + "'");
			while(rs.next()) {
				uri.add(rs.getString(1));
				uri.add(rs.getString(2));
				uri.add(rs.getString(3));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}

		String stripped_file_name = uri.get(1).replace(".html", "");
		System.out.println("\nConcatenating Path, File Name, and Language Code for the Endpoint URL");
		System.out.println("\nCalling Delete Docmap Endpoint");
		String ep = Constants.epDeleteDocmapByUriAndLanguage + uri.get(0) + "/" + stripped_file_name + "&locale=" + getLangAbbreviation(uri.get(2));
		CloseableHttpClient client = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(ep);

		String basicAuth = getBasicAuthEncoding("contentManager",
				"manageTheContent");
		httpDelete.addHeader("Authorization", "Basic " + basicAuth);
		CloseableHttpResponse response = null;

		try {
			response = client.execute(httpDelete);
			System.out.println("\nResults: ");
			System.out.println("\nStatus Code: " + response.getStatusLine().getStatusCode());
			String responseBody = EntityUtils.toString(response.getEntity());
			String docs_num = StringUtils.substringBetween(responseBody, "<numberOfDocs>", "</numberOfDocs>");
			System.out.println("\nDocmap Deleted: " + docs_num + "\n");
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int bulkDeleteDocmapsByFileId(String file_id) throws Exception{

			ResultSet rs = JDBCUtils.getResultSet("SELECT DATA_AID FROM DOCUMENT WHERE FILE_ID LIKE " + "'%" + file_id + "%'");
			ResultSetMetaData md = rs.getMetaData();
			int size = 0;
			while(rs.next()) {

				String url = Constants.epDeleteDocmap;
				Map<String, String> parameters = new HashMap<>();
				parameters.put("docId", rs.getString("DATA_AID"));
				Document result = NetUtils.httpDeleteRequestWithParams(url, parameters);
				size += Integer.parseInt(XPathUtils.getStringValue("//numberOfDocs", result, false));

			}
			return size;
	}

	public static boolean deleteSingleDocmapByFileId(String file_id) throws Exception{

		String sqlQuery = "SELECT DATA_AID FROM DOCUMENT d WHERE d.FILE_ID LIKE '%" + file_id + "%'";
    	ResultSet rs = JDBCUtils.getResultSet(sqlQuery);
    	String httpQuery = Constants.epDeleteDocmap;
    	Map<String, String> parameters = new HashMap<>();
    	if(rs.next()){
			parameters.put("docId", rs.getString("DATA_AID"));
		}

    	Document currentDoc =  NetUtils.httpDeleteRequestWithParams(httpQuery, parameters);
    	return Integer.parseInt(XPathUtils.getStringValue("//numberOfDocs", currentDoc, false)) > 0;

	}

	public static boolean deleteDocMaps(String uri) throws Exception{

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("contentManager", "manageTheContent");
		provider.setCredentials(AuthScope.ANY, credentials);
		HttpClient client = HttpClientBuilder.create()
				.setDefaultCredentialsProvider(provider)
				.build();
		String url = Constants.epDeleteDocmapByUriAndLanguage + uri;
		HttpRequest request = RequestBuilder.delete()
				.setUri(url)
				.addHeader("Content-Type", "application/xml")
				.addHeader("Authorization", "Basic Y29udGVudE1hbmFnZXI6bWFuYWdlVGhlQ29udGVudA==")
				.build();
		HttpResponse response = client.execute((HttpUriRequest) request);
		HttpEntity entity = response.getEntity();
		if (response != null){
			System.out.println(entity);
			return true;
		} else {
			return false;
		}
	}
}



