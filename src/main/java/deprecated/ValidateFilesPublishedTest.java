package deprecated;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

public class ValidateFilesPublishedTest {
	private final static String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.

	//JMS: This is not a permanent solution to logging. It's just temporary until we decide which direction to go.
	private static final boolean DEBUG = true;	
	private static void printDebug(String s) {if (DEBUG) System.out.println(s);}

	@BeforeClass
	public void init () {
	}
	
	//TODO: Going off the file_id won't work when there is a Liahona and Conference with the same ID
	@DataProvider(name = "files")
	public static String[][] cpFiles() {
			return new String[][] {{"10985_300_000.xml"}};
	}


	/**
	 * Tests that fileName is in the content_publish table.
	 * @param rawFileID file to test
	 * @throws IOException 
	 * @throws SQLException 
	 */
//	@Test(dataProvider = "files")
//	public static void validateContentPublishRecords(String rawFileID) throws SQLException, IOException {
//		printDebug(newLine + "Running TEST validateContentPublishRecords");
//		QAFileID fileID = QAFileID.fromString(rawFileID);
//		List<QAContentPublish> docs = QAPublishService.fromFileIDLike(fileID.getContentIdUnderScoreLanguageString());
//		assertNotNull(docs, "Document: " + rawFileID + " not in DB");
//		for (QAContentPublish doc : docs) {
//			printDebug(rawFileID + " document_id: " + doc.getDocumentId() + " DAM ID: " + doc.getDamID());
//		}
//	}

	/**
	 * Simple test to see if a file can be queried from MarkLogic
	 * @param rawFileID file to test
	 * @throws SQLException 
	 * @throws IOException 
	 */
//	@Test(dataProvider = "files")
	//TODO: This gets all the documents and checks if they all got published. That's not always what you want.
	//TODO: uris will contain all matches for fileIDStart. 2 problems. 1st, it gives Liahonas + Conference; 2nd it gives all languages, so it gets 
	//TODO: multiple hits when multiple languages in DB.
	//TODO: If you published a subset, you probably want to check from the content_publish table like verifyFilesInDAM does
//	public void verifyFilesInMarkLogic (String rawFileID) throws SQLException, IOException {
//		printDebug(newLine + "Running TEST verifyFilesInMarkLogic");
//		QAFileID fileID = QAFileID.fromString(rawFileID);
//		String fileIDStart = fileID.getContentID();
//		String ISOlangAlpha3 = fileID.getLanguage().getIsoPart3Code();
//		List<String> uris = QADocumentService.getURIFromFileIDLike(fileIDStart);
//		for (String uri : uris) {
//			String pathToCheck = MarkLogicDatabase.PUBLISHED.getContentRoot() + "/" + ISOlangAlpha3 + uri +".html";
//			//front-cover and back-cover don't get published from manifest publishing. Might want to change this logic
//			if (pathToCheck.contains("front-cover") || pathToCheck.contains("back-cover")) {
//				continue;
//			}
//			printDebug("pathToCheck: " + pathToCheck);
//			printDebug("ML HOST: " + Constants.mlPreviewHost);
//			// first try, read the file and check for empty contents
//			File tempFile = null;
//			//TODO: This is an ugly hack. I want it to check all the possible files, even if it fails on one.
//			//TODO: I need to fix this before I can use this for true automation
//			String fileContents = "HACK";
//			try {
//				tempFile = MarkLogicUtils.readFile(pathToCheck);
//				fileContents = FileUtils.readFileToString(tempFile);
//				printDebug("FILE: " + pathToCheck + " FOUND FOUND FOUND FOUND FOUND");
//			} catch (ResourceNotFoundException e) {
//				printDebug("File: " + pathToCheck + " not found");
//			}
//			//printDebug(fileContents);
//			printDebug("Not printing fileContents");
//			Assert.assertTrue(StringUtils.isNotEmpty(fileContents));
//
//			// second try, call utility method to check for existence
//			//TODO: Uncomment this once I get this code straightened out
//			//Assert.assertTrue(MarkLogicUtils.docExists(pathToCheck), "Call to ML.docExists()");
//		}
//	}

	/**
	 * Tests whether or not the documents contained in rawFileID are in the Annotations Server Doc Maps
	 * @param rawFileID file to test
	 * @throws SQLException
	 * @throws IOException
	 */
//	@Test(dataProvider = "files")
//	public void verifyDocMapinAnnotationServer(String rawFileID) throws SQLException, IOException {
//		printDebug(newLine + "Running TEST verifyDocMapinAnnotationServer");
//		QAFileID fileID = QAFileID.fromString(rawFileID);
//		printDebug("gonna check for: " + fileID.getContentIdUnderScoreLanguageString());
//		List<QAContentPublish> docs = QAPublishService.fromFileIDLike(fileID.getContentIdUnderScoreLanguageString());
//		if(docs.isEmpty()) {
//			Assert.fail("Document: " + rawFileID + " not in DB");
//		}
//		for (QAContentPublish doc : docs) {
//			String dataAid = doc.getDataAid();
//			//TODO: If testing to make sure all docs in Doc Maps, should fail if count==0
//			System.out.println("returned count: " + QADocMapService.getDocMap(Integer.parseInt(dataAid)) + "for data-aid: " + dataAid);
//		}
//	}

	/**
	 * When run via testng, won't run unless validateContentPublishRecords succeeded, since that indicates there
	 * is no content_publish record. Calls query DAM endpoint and makes a basic check to make sure it made an
	 * appropriate return.
	 * @param rawFileID file to test
	 * @throws IOException 
	 * @throws SQLException 
	 */
//	@Test(dependsOnMethods = { "validateContentPublishRecords" },dataProvider = "files")
//	@Test(dataProvider = "files")
//	public void verifyFilesInDAM (String rawFileID) throws SQLException, IOException {
//		printDebug(newLine + "Running TEST verifyFilesInDAM");
//		QAFileID fileID = QAFileID.fromString(rawFileID);
//		List<QAContentPublish> docs = QAPublishService.fromFileIDLike(fileID.getContentIdUnderScoreLanguageString());
//		if(docs.isEmpty()) {
//			Assert.fail("Document: " + rawFileID + " not in DB");
//		}
//		for (QAContentPublish doc : docs) {
//			String damID = doc.getDamID();
//			JSONObject response = ContentAutomationQAEndpointUtil.queryDAM(damID);
//			assertNotNull(response,"DAM Query failed. Returned null");
//			printDebug("Response: " + response.toString());
//			//TODO Look into testing other fields
//			assertTrue((Boolean)response.get("isCurrentVersion"), "DAM Query failed");
//			printDebug("DAM returned assetID: " + (String)response.get("assetID"));
//		}
//	}

	/**
	 * Simple test to see if file is in URI Mappings in Mark Logic
	 * @param fileName file to test
	 * @throws IOException 
	 */
	//TODO Currently tests for file existence, in future could test file contents.
	//TODO This looks for -uris.xml some(all?) conferences are -gc-uris.xml I need to figure out how to 
	//TODO check for both. Also, there are a few uris that are different entirely.
//	@Test(dataProvider = "files")
//	public void verifyFilesInURIMapping (String fileName) throws IOException {
//		printDebug(newLine + "Running TEST verifyFilesInURIMapping");
//		String fileMinusSuffix = fileName.substring(0, fileName.length()-4);
//		printDebug("fileMinusSuffix is: " + fileMinusSuffix);
////		String pathToCheck = MarkLogicDatabase.uriMapping.getContentRoot()  + fileMinusSuffix +"-uris.xml";
//		String pathToCheck = MarkLogicDatabase.URI_MAPPING.getContentRoot()  + fileMinusSuffix +"-gc-uris.xml";
//		printDebug("pathToCheck is: " + pathToCheck);
//	
//		File tempFile = MarkLogicUtils.readFile(pathToCheck);
//		String fileContents = org.apache.commons.io.FileUtils.readFileToString(tempFile);
//		//printDebug(fileContents);
//		printDebug("Not printing fileContents");
//		Assert.assertTrue(StringUtils.isNotEmpty(fileContents));
//	
//		// second try, call utility method to check for existence
//	
//		Assert.assertTrue(MarkLogicUtils.docExists(pathToCheck), "Call to ML.docExists()");
//	}

}
