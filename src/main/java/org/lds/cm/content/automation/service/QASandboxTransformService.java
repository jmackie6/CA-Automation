package org.lds.cm.content.automation.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.QAFileUtils;
import org.lds.cm.content.automation.util.QATransformationResult;
import org.lds.cm.content.automation.util.XMLUtils;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


 

public class QASandboxTransformService {
	
	static SoftAssert softAssert = new SoftAssert();

	/**
	 * Creates an HttpPost object and posts it to the sandbox transform endpoint
	 * of the lane marked in Constants.java.
	 * @param file
	 * @return QATransformationResult The result of the transformation
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ParseException
	 * @throws SAXParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 * @throws SQLException
	 * @throws DOMException
	 * @throws JAXBException
	 * @throws Docx4JException
	 */
	public static QATransformationResult transformFile(File file) throws ClientProtocolException, IOException,
				ParseException, SAXParseException, ParserConfigurationException, SAXException, XPathExpressionException, SQLException, DOMException, JAXBException, Docx4JException  { 
		
		System.out.println("\n** START Sandbox Transform **");
				
		JSONObject jsonResponse = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost(Constants.epSandboxTransform);


		HttpEntity entity1 = MultipartEntityBuilder.create().addBinaryBody("file", file).build();
		httppost.setEntity(entity1);

		CloseableHttpResponse response = httpClient.execute(httppost);
		// get response
		int code = response.getStatusLine().getStatusCode();
		HttpEntity entity2 = response.getEntity();
		String responseString = EntityUtils.toString(entity2);

		if (code == 200)
		{
			jsonResponse = (JSONObject) new JSONParser().parse(responseString);
		}
		else {
			Assert.fail("Transform returned this status code: " + code);
		}

		EntityUtils.consume(entity2);


		
		boolean successful = (boolean) jsonResponse.get("transformSuccess");
		
		// See if the file was written to the Sandbox DB		
		if(successful)
		{
			softAssert.assertEquals(successful, true);						
			
			// See if it is successful in the process log
			Long process_log = (Long) jsonResponse.get("processLogId");
			boolean pSuccessful = processSuccessful(process_log.toString());
				System.out.println(pSuccessful ? "Transform successful in process log" : "Transform not successful in process log!" );
						
			// Get the docIds for the docs that were transformed and check the database
			JSONArray urlJSONList = (JSONArray) jsonResponse.get("urls");
			ArrayList <String> docIds = new ArrayList<>();			
			for (int i = 0; i < urlJSONList.size(); i++) {
				String testJSON = (String) urlJSONList.get(i);
				String [] urlParts = testJSON.split("=|&");
				String docId = urlParts[1];			
				docIds.add(docId);		
				
				// Make sure they are in the document sandbox database
				try {
				 	QAHtml5Document doc = QASandboxTransformService.ExistsInDbByDocID(docId); 		
					
					Assert.assertNotNull(doc, "doc not found");
											
				} catch (SQLException e) {
					Assert.assertNotNull(null, "docID " + docId + " not found in the database\n");
					System.out.println("  Transform Failed");
				}
				
			}				
			
		}
		
		else {			
			System.out.println("Post failed");
			softAssert.assertEquals(successful, false);
			// output error message why the transform failed
			System.out.println("**************************");
			System.out.println("**** Transform Failed ****");
			System.out.println("**************************");
			String message = (String) jsonResponse.get("error");
			System.out.println(message);
			
		}
		softAssert.assertAll();

		System.out.println("** END Sandbox Transform **\n");	
		return QATransformationResult.fromJSON(jsonResponse);		
		
	
	} //end transformFile
	
	public static ArrayList<String> getActiveCssCodes () {
		String sql = "select preview_css_id from preview_css where deleted_flag = 0";
		ArrayList<String> codes = new ArrayList<>();
		
		try {
			ResultSet rs = JDBCUtils.getResultSet(sql);
	
			while ((rs.next())) {
				codes.add(rs.getString("preview_css_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return codes;
	}
	
	public static void checkRoot(File file) throws SAXParseException, ParserConfigurationException, SAXException, IOException, DOMException, SQLException, JAXBException, Docx4JException {
		// get source - which is the file_id (needed for merged webml broadcast where the file_ids are mismatched, so we can't use the manifest endpoint)
		// Check to see if we are dealing with ldswebml
		
		// Convert to XML if *.docx
		if (QAFileUtils.isDocXFile(file)) {
			convertDocXToXML(file);
		}
		
		else {
			Document docFromFile = XMLUtils.getDocumentFromFile(file);
			Node rootNode = docFromFile.getDocumentElement();
			System.out.println("rootNode: " + rootNode);
			
			String rootNodeString = rootNode.getNodeName().toString();
			System.out.println(rootNodeString);
	
			if (rootNodeString.equalsIgnoreCase("ldswebml")) {
	
				ArrayList<String> arrlist = new ArrayList<>();
				String xpath = "//source/text()";
				NodeList sources = XMLUtils.getNodeListFromXpath(file, xpath);
	
				for (int i = 0; i < sources.getLength(); i++) {
					String fileId = sources.item(i).getNodeValue();
					arrlist.add(fileId);
				}
				System.out.println(arrlist);
				
			}
			
					
			else if (rootNodeString.equalsIgnoreCase("magazine") || rootNodeString.equalsIgnoreCase("book") || rootNodeString.equalsIgnoreCase("testament")) {
	
				ArrayList<String> arrlist = new ArrayList<>();
				String xpath = "//@fileID";
				NodeList sources = XMLUtils.getNodeListFromXpath(file, xpath);
	
				for (int i = 0; i < sources.getLength(); i++) {
					String fileId = sources.item(i).getNodeValue();
					arrlist.add(fileId);
				}
				System.out.println(arrlist);
				
			}
			
			else {
				ArrayList<String> arrlist = new ArrayList<>();
				String xpath = "//*[contains(@type,'file')]/text()";
				NodeList sources = XMLUtils.getNodeListFromXpath(file, xpath);
	
				for (int i = 0; i < sources.getLength(); i++) {
					String fileId = sources.item(i).getNodeValue();
					arrlist.add(fileId);
				}
				System.out.println(arrlist);
			}
		}
	} //end checkRoot
			


	private static File convertDocXToXML(final File inputFile) throws JAXBException, Docx4JException, SQLException {

		final File destDir = new File(Constants.transformFileStartDir + "/docx");
		final Path dest = new File(destDir, inputFile.getName()).toPath();
		XMLUtils.convertDocxToXML(inputFile, dest.toString());
		
		return destDir;
	}
		
	public static QAHtml5Document ExistsInDbByDocID(String docID) throws SQLException, IOException {
		String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT_SANDBOX d where d.DOCUMENT_SANDBOX_ID= " + "'" + docID + "'";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		List <QAHtml5Document> docs = listFromResultSet(rs);
		if (docs.size() > 0) {
			return docs.get(0);
		} else {
			return null;
		}
	}		
	
	//Query database to see that the process was successful in the process_log
	public static boolean processSuccessful(String processLogId) throws SQLException, IOException {
		String sql = "select d.STATUS from PROCESS_LOG d where d.PROCESS_LOG_ID = " + "'" + processLogId + "'";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		String firstAns = "";
		while ((rs.next()))
		{
			firstAns = rs.getString("STATUS");
			break;
		}
		rs.close();

		Assert.assertEquals(firstAns, "SUCCESSFUL");		
		return firstAns.equals("SUCCESSFUL");
	}		
		

		
		
	//Query database for files matching fileId and contentType
	public static List<QAHtml5Document> fromContentTypeAndFileId(String contentType, String fileId) throws SQLException, IOException {
		String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT_SANDBOX d where d.CONTENT_TYPE = " + "'" + contentType + "' AND d.FILE_ID = " + "'" + fileId + "'";
		ResultSet rs = JDBCUtils.getResultSet(sql);
		return listFromResultSet(rs);
	}
	

	
	private static List<QAHtml5Document> listFromResultSet(ResultSet rs) throws SQLException, IOException {
		
		List<QAHtml5Document> documents = new ArrayList<>();
		while ((rs.next())) {
			QAHtml5Document doc = new QAHtml5Document();
			doc.setContentType(rs.getString("CONTENT_TYPE"));
			doc.setCreatedDate(rs.getTimestamp("CREATED_DATE"));		//
			doc.setDocumentId(rs.getLong("DOCUMENT_SANDBOX_ID"));			
			doc.setDocumentXML("DOCUMENT_XML");						
			doc.setFileId(rs.getString("FILE_ID"));
			doc.setFileName(rs.getString("FILE_NAME"));
			doc.setLanguageID(rs.getInt("LANGUAGE_ID"));
			doc.setPath(rs.getString("PATH"));
			
			documents.add(doc);
		}
		return documents;
	}			
		
}