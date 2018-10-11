package org.lds.cm.content.automation.tests.endpoints.TransformApiController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lds.cm.content.automation.enums.DocumentSource;
import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.service.QASandboxTransformService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.QAFileUtils;
import org.lds.cm.content.automation.util.QATransformationResult;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class QASandboxTransformTest {

	@AfterMethod(alwaysRun = true)
	public static void closeUp() { JDBCUtils.closeUp(); }

// Sandbox Tests	
// 	1. Transform a variety of tests
// 	2. Transform a single file to the sandbox without overwriting anything (in doc table or sandbox)
// 	3. Generate proper previews with each of the CSS types
			
		static SoftAssert softAssert = new SoftAssert();
	
		// 1. Transform a variety of files and check database
		@Test  (groups = { "endpoints" })
		public static void testTransform() throws Exception {
			
			System.out.println("\n**** In QASandboxTransformTest/testTransform module ****\n");
			
			// Load the files from the designated directory				
//			File startDir = new File(Constants.transformFileStartDir);
			File startDir = new File(Constants.transformFileStartDir + "/spaGeneralConferenceHTML");		// Replace the line above with this one to test with less documents
				System.out.println("Loading files to test...");			
			List<File> filesToTransform = new ArrayList<>();			
			QAFileUtils.loadTestFiles(filesToTransform, startDir);			
				Assert.assertNotEquals(filesToTransform.size(),0,"No files found in the passed folder");
				System.out.println(filesToTransform.size() + " files found");				

			// Transform all of the files				
			for (File file : filesToTransform) {
					System.out.println("\nTransforming file: " + file.getPath());  // was getAbsolutePath
				QATransformationResult result = QASandboxTransformService.transformFile(file);	// 1. Transform each file						
					System.out.println(result.wasSuccessful() ? "Transform result shows successful" : "ERROR: Transform result shows failure" );
					
			}
									
			System.out.println("\n**** EXIT QASandboxTransformTest/testTransform module ****\n");			
			
		}

	@Test  (groups = { "endpoints" })
	public static void testSandboxTransformSource() throws Exception {

		System.out.println("\n**** In QASandboxTransformTest/testSandboxTransformSource module ****\n");

		File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/ye-are-the-temple-of-god.html");
		QATransformationResult result = QASandboxTransformService.transformFile(testFile);
		System.out.println(result.wasSuccessful() ? "Transform result shows successful" : "ERROR: Transform result shows failure" );

		QADocumentService.verifySourceByFileId("testPD00028166_000_030", DocumentSource.TRANSLATION, true);

		System.out.println("\n**** EXIT QASandboxTransformTest/testSandboxTransformSource module ****\n");

	}
		
		// 2. Transform a single file to the sandbox without overwriting anything (in doc table or sandbox)
		@Test  (groups = { "endpoints" })
		public static void docTableNotChangedTest() throws Exception {
			
			System.out.println("\n**** In QASandboxTransformTest/docTableNotChangedTest module ****\n");			
			

				// Transform a doc to the doc table	
//				File testFile = new File(Constants.transformFileStartDir + "/jpnGeneralConferenceHTML/04/be-of-good-cheer.html");
				File testFile = new File(Constants.transformFileStartDir + "/jpnGeneralConferenceHTML/04/his-arm-is-sufficient.html");
				System.out.println("Transform (non-sandbox) for file:" + testFile.getPath() + ")" );
				QATransformationResult result = QATransformService.transformFileGivenContentGroupId(testFile, "1");
					Assert.assertNotNull(result, "Transform failed");
				
				// Find the doc ID of the doc in the document table
				Long docPLI = result.processLogId;
				String docId = QATransformService.docIdFromProcessId(docPLI);							
					
				// Modify the create date of the doc in the doc table (to compare later)
				QATransformService.modifyDates(docId);				
					Assert.assertEquals(QATransformService.datesNotRecent(docId),true, "Failed to modify the date");								
					
				// Get the pre-sandbox-transform document
				QAHtml5Document docBefore = QATransformService.ExistsInDbByDocID(docId);
					Assert.assertNotNull(docBefore);
					System.out.println("  Success: Uploaded doc found in the database" );
				
				// Transform that same doc to the sandbox table (see that it's there) 
					System.out.println("First Sandbox transform for file:" + testFile.getPath() + ")" );
				QATransformationResult sbResult = QASandboxTransformService.transformFile(testFile);	
				ArrayList <String> sbDocIds = sbResult.docIds;
					Assert.assertEquals(sbDocIds.size() > 0, true);
				String sbFirstDocId1 = sbDocIds.get(0);
				QAHtml5Document dbDocSearch = QASandboxTransformService.ExistsInDbByDocID(sbFirstDocId1);
					Assert.assertNotNull(dbDocSearch);	
					System.out.println("  Success: Uploaded doc found in the sandbox database" );
				
				// Make sure the doc in the doc table wasn't changed by the sandbox upload
					System.out.println("Assuring that the doc was overwritten in the document table" );
				QAHtml5Document docAfter = QATransformService.ExistsInDbByDocID(docId);
					Assert.assertNotNull(docAfter);
					Assert.assertEquals(docBefore.isEqualTo(docAfter),true);
				
				// Transform to the sandbox again
					System.out.println("Second Sandbox transform for file:" + testFile.getPath() + ")" );
				QATransformationResult sbResult2 = QASandboxTransformService.transformFile(testFile);	
				ArrayList <String> sbDocIds2 = sbResult2.docIds;
					Assert.assertEquals(sbDocIds2.size() > 0, true);
				String sbFirstDocId2 = sbDocIds2.get(0);
				QAHtml5Document dbDocSearch2 = QASandboxTransformService.ExistsInDbByDocID(sbFirstDocId2);				
					Assert.assertNotNull(dbDocSearch2);
					System.out.println("  Success: Uploaded doc found in the sandbox database" );
				
				// Again Check the document database to assure that it wasn't changed there
				QAHtml5Document docAfter2 = QATransformService.ExistsInDbByDocID(docId);
					Assert.assertEquals(docBefore.isEqualTo(docAfter2),true);	
					System.out.println("  Success: Document not overwritten in in the document database" );
					Assert.assertNotEquals(QATransformService.datesNotRecent(docId),false);
					System.out.println("  Success: Create and Modify dates not overwritten" );
				
				// Make sure sandbox doc (accessed by document_sandbox_id) of the first upload is still there
				QAHtml5Document sbDocSearch = QASandboxTransformService.ExistsInDbByDocID(sbFirstDocId1);
					Assert.assertNotNull(sbDocSearch);
					System.out.println("  Success: First upload to sandbox still found in the database" );
				
				// Make sure document_sandbox_id of the second is not the same as the first (no overwrite)	
					Assert.assertNotEquals(sbFirstDocId1, sbFirstDocId2);
					System.out.println("  Success: The second upload to the sandbox is a separate entry in the database" );
			
			
			System.out.println("\n**** EXIT QASandboxTransformTest/docTableNotChangedTest module ****\n");
			
		}
		
		
		// 3. Generate proper previews with each of the CSS types
		@Test  (groups = { "endpoints" })
		public static void cssAppliedTest ()
		{
		
			System.out.println("\n**** In QASandboxTransformTest/cssAppliedTest module ****\n");
			
			try {
				
			// Transform a file
				
				File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/one-by-one.html");									
				QATransformationResult sbResult = QASandboxTransformService.transformFile(testFile);
				ArrayList <String> docIds = sbResult.docIds;
					Assert.assertEquals(docIds.size() > 0, true);
				String docId = docIds.get(0);
				QAHtml5Document doc = QASandboxTransformService.ExistsInDbByDocID(docId);
					Assert.assertNotNull(doc);	
					
					
			// Get language, uri, and file-id values from transform	result
					
				String lang = String.format("%03d", doc.getLanguageID());
				String uri = doc.getPath();
				String fileId = doc.getFileId();
				

			// Create a preview with all of the valid CSS codes in the database 
				
				ArrayList<String> cssCodes = QASandboxTransformService.getActiveCssCodes ();
				Assert.assertEquals(cssCodes.size() > 0,  true, "No active CSS codes found in the database\n");
				for (String cssId : cssCodes ) 
				{					
					System.out.println("--- Testing doc preview for WS/V1 and SERVICES/API controllers with CSS: " + cssId + " ---");

					String wsV1 = NetUtils.getHTML(Constants.baseURL + "/ws/v1/sandbox/previewFile?docId=" + docId + "&uri=" + uri + "&cssId=" + cssId );
					String servicesApi = NetUtils.getHTML(Constants.baseURL + "/services/api/sandbox/previewFile?docId=" + docId + "&uri=" + uri + "&cssId=" + cssId );
						
				/* Comparing the URI, CSS, Language, and File Id pulled from the document transformed,
				   and the document returned by the preview html for BOTH controllers (ws/v1 and services/api) */
					
					Pattern uriPat = Pattern.compile("data-uri=\"" + uri );	
					Matcher uriMat1 = uriPat.matcher(wsV1);
					Matcher uriMat2 = uriPat.matcher(servicesApi);
						softAssert.assertEquals(uriMat1.find(),true,"WS/V1: URI not found in the preview for cssId:" + cssId + "\n");
					    softAssert.assertEquals(uriMat2.find(),true,"SERVICES/API: URI not found in the preview for cssId:" + cssId + "\n");
					
					Pattern cssPat = Pattern.compile("cssId=" + cssId + "\" id=\"testCss\"");
					Matcher cssMat1 = cssPat.matcher(wsV1);
					Matcher cssMat2 = cssPat.matcher(servicesApi);
						softAssert.assertEquals(cssMat1.find(),true,"WS/V1: CSS not found in the preview for cssId:" + cssId + "\n");
						softAssert.assertEquals(cssMat2.find(),true,"SERVICES/API: CSS not found in the preview for cssId:" + cssId + "\n");

					Pattern langPat = Pattern.compile("<lds:pub-lang>" + lang + "</lds:pub-lang>");	
					Matcher langMat1 = langPat.matcher(wsV1);
					Matcher langMat2 = langPat.matcher(servicesApi);
						softAssert.assertEquals(langMat1.find(),true,"WS/V1: Language not found in the preview for cssId:" + cssId + "\n");
						softAssert.assertEquals(langMat2.find(),true,"SERVICES/API: Language not found in the preview for cssId:" + cssId + "\n");

					Pattern fileIdPat = Pattern.compile("<lds:title type=\"file\">" + fileId + "</lds:title>");
					Matcher fileIdMat1 = fileIdPat.matcher(wsV1);
					Matcher fileIdMat2 = fileIdPat.matcher(servicesApi);
						softAssert.assertEquals(fileIdMat1.find(),true,"WS/V1: File ID not found in the preview for cssId:" + cssId + "\n");
					    softAssert.assertEquals(fileIdMat2.find(),true,"SERVICES/API: File ID not found in the preview for cssId:" + cssId + "\n");
									
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			softAssert.assertAll();
			
			System.out.println("No errors found in the previews for either WS/V1 or Services/API controllers ");
			
			System.out.println("\n**** EXIT QASandboxTransformTest/cssAppliedTest module ****\n");			
		}
			
}



