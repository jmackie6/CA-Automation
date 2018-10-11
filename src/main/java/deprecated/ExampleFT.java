package deprecated;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.lds.cm.content.automation.enums.MarkLogicDatabase;
import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.MarkLogicUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.XMLUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ExampleFT {
	
	public static int length = 100;
	
	@Test
	public void testFindDocById () throws SQLException, IOException, ParserConfigurationException, SAXException {
		long documentId = 75625L;
		QAHtml5Document document = QADocumentService.fromDocumentId(documentId);
		System.out.println(document);
	
	}
	
	@Test
	public void findMediaXml () throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String exampleQuery1 = "<conference-media conferenceId=\"PD50028768_002_000\"></conference-media>";
		List<Document> mediaXmlDocs = MarkLogicUtils.findMediaXMLByExample(exampleQuery1, MarkLogicDatabase.PUBLISHED, length);
		
		System.out.println("SIZE: " + mediaXmlDocs);
		
//		File xml = MarkLogicUtils.readFileFromML("/webml/ldsorg content/english/conference/2011/april/media/PD50028768_002_000-media.xml", false);
//		System.out.println(xml.getName());
//		File xml = MarkLogicUtils.readFileFromML("/webml/mobile/apw/general-conference/2013/04/beautiful-mornings/10785_012_56porter.xml", false);
//		System.out.println(xml.getName());
		File xml = MarkLogicUtils.readFile("/preview/content-automation/content/eng/friend/2014/05/_manifest.html");
		System.out.println(xml.getName());
	}
	
	@Test
	public void findHtml5ByFileId () throws XPathExpressionException, ParseException, ClientProtocolException, org.json.simple.parser.ParseException, IOException {
		// so if I transform a file, I can grab the file ID, then bring back all html5 content with that file ID.
		// Then I can compare what I would expect to see in the html5 content, with what I actually see.
		
		String [] fileIdArray = {"10991_000"};
		
		for (String fileId : fileIdArray) {
			List<Document> docs = MarkLogicUtils.findHtml5ByFileId(fileId, MarkLogicDatabase.PUBLISHED, MarkLogicDatabase.PUBLISHED.getContentRoot(), 200);
			
			String videoXpath = "//html/body/div[@class=\"body-block\"]/video";
			
			for (Document doc : docs) {
				NodeList videoNodes = XMLUtils.getNodeListFromXpath(doc, videoXpath, null);
				
				if (videoNodes != null && videoNodes.getLength() > 0) {
					for (int nodeIndex = 0; nodeIndex < videoNodes.getLength(); nodeIndex++) {
						Element videoNode = (Element) videoNodes.item(nodeIndex);
						String videoUrl = videoNode.getAttribute("src");
						if (StringUtils.isNotEmpty(videoUrl)) {
							JSONObject results = NetUtils.getJson(videoUrl);
							String hlsurl = (String) results.get("HLSURL");
							Assert.assertTrue(StringUtils.isNotEmpty(hlsurl), "HLSURL is empty: " + videoUrl);
							System.out.println(hlsurl);
						}
					}
				}
				
			}
		}
		
	}
	/**
	 * Simple test to see if a file can be queried from MarkLogic
	 * @throws IOException 
	 */
	@Test
	public void readFileFromMarkLogic () throws IOException {
		String pathToCheck = MarkLogicDatabase.PUBLISHED.getContentRoot() + "/eng/manual/welfare-and-self-reliance/the-bishop.html";
		// first try, read the file and check for empty contents
		File tempFile = MarkLogicUtils.readFile(pathToCheck);
		String fileContents = FileUtils.readFileToString(tempFile);
		System.out.println(fileContents);
		Assert.assertTrue(!fileContents.isEmpty());
		
		// second try, call utility method to check for existence
		Assert.assertTrue(MarkLogicUtils.docExists(pathToCheck), "Call to ML.docExists()");
	}
	
	/**
	 * retrieve an HTML5 doc and look for an lds:meta element with an lds:title child, then print out some data
	 * @throws XPathExpressionException 
	 */
	@Test
	public void checkExistenceLdsMeta_ldsTitle () throws XPathExpressionException {
		String pathToCheck = "C:\\Users\\Public\\december.html";
		File tempFile = new File(pathToCheck);
		
		try {
			Document document = XMLUtils.getDocumentFromFile(tempFile);
			
			System.out.println(document.getBaseURI());
			System.out.println(document.getDoctype());
			System.out.println(document.getDocumentURI());
			System.out.println(document.getLocalName());
			System.out.println(document.getTextContent());
			System.out.println(document.getOwnerDocument());
			System.out.println(document.getPrefix());
			System.out.println(document.getUserData("key"));
			
			String xpath = "//lds:meta/lds:title";
			NodeList nodes = XMLUtils.getNodeListFromXpath(document, xpath, null);
//			String xpathNoNamespace = "//a[@class=\"legacy-cross-ref\"]";
//			NodeList nodes = XMLUtils.getNodeListFromXpath(document, xpath);
			
			Assert.assertTrue(nodes != null);
			Assert.assertTrue(nodes.getLength() > 0);
			
			for (int nodeIndex = 0; nodeIndex < nodes.getLength(); nodeIndex++) {
				Node currentNode = nodes.item(nodeIndex);
				NamedNodeMap attributes = currentNode.getAttributes();
				
				for (int attributeIndex = 0; attributeIndex < attributes.getLength(); attributeIndex++) {
					Node currentAttribute = attributes.item(attributeIndex);
					System.out.println(currentNode.getNodeName() + " " + currentAttribute.getNodeName() + "=" + currentAttribute.getNodeValue() + " >> " + currentNode.getTextContent());
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			Assert.fail("Test failed: " + e.getMessage());
		}
	}
	
	/*
	 * open a media xml file, and for each talk retrieve the corresponding HTML5 document
	 */
	@Test
	public void checkMediaNodes () throws XPathExpressionException, SAXParseException, ParserConfigurationException, SAXException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String mediaXmlPath = "/webml/ldsorg content/english/conference/2014/october/media/10991_000_000-media.xml";
		File mediaXmlFile = MarkLogicUtils.readFile(mediaXmlPath);
		Document mediaXmlDocument = XMLUtils.getDocumentFromFile(mediaXmlFile);
		String conferenceItemXpath = "//conference-item";
		NodeList conferenceItemNodes = XMLUtils.getNodeListFromXpath(mediaXmlDocument, conferenceItemXpath, null);
		
		Assert.assertTrue(conferenceItemNodes != null);
		Assert.assertTrue(conferenceItemNodes.getLength() > 0);
		
		for (int nodeIndex = 0; nodeIndex < conferenceItemNodes.getLength(); nodeIndex++) {
			Node conferenceItem = conferenceItemNodes.item(nodeIndex);
			NamedNodeMap attributes = conferenceItem.getAttributes();
			Node itemType = attributes.getNamedItem("type");
			Node itemId = attributes.getNamedItem("articleId");
			
			if (itemType != null && itemType.getNodeValue().equals("talk")) {
				System.out.println(itemId.getNodeValue());
				String exampleQuery = "<lds:title type=\"file\">" + itemId.getNodeValue() + "</lds:title>";
				List<Document> html5Docs = MarkLogicUtils.findHtml5ByExample(exampleQuery, MarkLogicDatabase.PUBLISHED, length);
				
				// assert that we have at least one document
				Assert.assertTrue(html5Docs.size() > 0, "Checking " + itemId.getNodeValue());
			}
			
		}
	}
	
	@Test
	public void processConferenceMediaByExample () throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String exampleQuery = "<conference-media conferenceId=\"$word 12565_\"></conference-media>";
		List<Document> mediaXmlDocs = MarkLogicUtils.findMediaXMLByExample(exampleQuery, MarkLogicDatabase.PUBLISHED, length);
		
		for (Document mediaXmlDoc : mediaXmlDocs) {
			System.out.println(mediaXmlDoc.getDocumentElement().getAttribute("conferenceId"));
		}
	}
	
	/**
	 * Get the cover art map file and read it as a document object
	 */
	@Test
	public void getCoverArtMap () throws XPathExpressionException, SAXParseException, ParserConfigurationException, SAXException, IOException {
		String coverArtMapLocation = "xslt/cover-art-map.xsl";
		File coverArtMap = org.lds.cm.content.automation.util.QAFileUtils.getResourceFile(coverArtMapLocation);
		Document coverArtDocument = XMLUtils.getDocumentFromFile(coverArtMap);
		String xpath = "//image[@mediaID=\"1482478\"]";
		NodeList coverArtNodes = XMLUtils.getNodeListFromXpath(coverArtDocument, xpath, null);
		Assert.assertTrue(coverArtNodes != null, "No cover art entries matching xpath " + xpath);
	}
	
	/**
	 * Demonstrate the getResultSet method in JDBCUtils
	 */
	@Test
	public void performSqlQuery() throws SQLException {
		String query = "select * from transform_xslt order by modified_date desc";
		ResultSet rs = JDBCUtils.getResultSet(query);
		while (rs.next()) {
			System.out.println(rs.getString("MODIFIED_DATE") + " - " + rs.getString("XSLT_NAME"));
		}
	}
	
	
	
	/**
	 * Z:/ is where test MarkLogic is mapped on my local machine. This can be stored in the properties file if it is needed. 
	 * 
	 * This just makes it easier to delete entire directories on MarkLogic because MarkLogic's 
	 * API requires that you specify the URI of every file you want to delete.
	 * 
	 * This can be used to delete things for testing xrefs.
	 */
	@Test
	public void deleteMarkLogicDirectory() {
		String testMLPath = "C:/dev/content-automation/content/eng/manual/new-testament-teacher-manual";
		File directory = new File(testMLPath);
		List<File> files = (List<File>) FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		
		for (File file: files) {
			String markLogicPath = file.getAbsolutePath().substring(2, file.getAbsolutePath().length()).replace("\\", "/");
			System.out.println(markLogicPath);
		}
		
		// use this to delete with webDAV, this deletes files and folders
//		File directoryToDelete = new File(testMLPath);
//		try {
//			FileUtils.deleteDirectory(directoryToDelete);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// use this to delete with MarkLogic Java API, this does not delete folders, only files 
		MarkLogicUtils.deleteDirectory(MarkLogicDatabase.PUBLISHED, directory);
	}
}
