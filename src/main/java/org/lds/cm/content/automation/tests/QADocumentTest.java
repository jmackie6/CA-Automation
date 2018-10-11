package org.lds.cm.content.automation.tests;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.model.QALanguage;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.service.QALanguageService;
import org.lds.cm.content.automation.model.QADocInfo;
import org.lds.cm.content.automation.util.XMLUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class QADocumentTest {	
	SoftAssert softAssert = new SoftAssert();

	@Test  //Checks whether data in Oracle Tables matches documentXML data
	public void checkFieldsOnRandomDocuments () {
		
		System.out.println("\n**** In QADocumentTest: ****\n");
		long startTime = System.currentTimeMillis();
		

		try {
			//List<QAHtml5Document> documents = QADocumentService.getAllDocuments();
			List<QAHtml5Document> documents = QADocumentService.getSampleDocuments(2);
			int counter = 0;
			ArrayList<String> arrlist = new ArrayList<>(); 
			
			
			for (QAHtml5Document document : documents) {
				// get docInfo object
				QADocInfo docInfo = XMLUtils.getDocInfo(document);

				// file ID 
				// Verify document table data matches Document xml data?

					String XMLfileID = docInfo.getXmlFileId();
					String TablefileID = document.getFileId();
				if (XMLfileID.equals(TablefileID)) {		
					// do nothing	
				}
				
				else { 
					// Compare fails, add to a list.  Then, assert that the list is empty.  
					// If not, list all docs in the error.
					
					arrlist.add(XMLfileID);
					arrlist.add(TablefileID);
					softAssert.assertEquals(arrlist.isEmpty(), true, "Test#" + counter + " failed, file IDs don't match on XML ID " + XMLfileID + " and Table ID " + TablefileID + "\n");
					  
				}
				
				// language ID
				
				QALanguage language = QALanguageService.findLangByLangCode(document.getLanguageID());

					String XMLlangID = docInfo.getXmlLanguage();
					String TablelangID = language.getIsoPart3Code();
				if (XMLlangID.equals(TablelangID)) {
		
				}
				
				else { 
					arrlist.add(XMLlangID);
					arrlist.add(TablelangID);
					softAssert.assertEquals(arrlist.isEmpty(), true , "Language IDs don't match on XML ID " + XMLlangID + " and Table ID " + TablelangID + "\n");
					
				}
				
				// Content type
					String XMLtypeID = docInfo.getXmlDataContentType();
					String TabletypeID = document.getContentType();
				if (XMLtypeID.equals(TabletypeID)) {
				
		
				}
				else { 
					arrlist.add(XMLtypeID);
					arrlist.add(TabletypeID);
					softAssert.assertEquals(arrlist.isEmpty(), true, "Content types don't match on XML ID " + XMLtypeID + " and Table ID " + TabletypeID + "\n");

				}
				
				// URI
					String fileID = document.getFileId();
					String type = document.getContentType();
					String XMLURI = docInfo.getURI();
					String TableURI = QADocumentService.getURIFromFileIDandType(type, fileID);
				
						
				if (XMLURI.equals(TableURI) ) {

				}
				else { 
					arrlist.add(XMLURI);
					arrlist.add(TableURI);
					softAssert.assertEquals(arrlist.isEmpty(), true, "URIs don't match on XML " + XMLURI + " and Table " + TableURI + "\n");

				}		
				
				counter++;
			}
			
			System.out.println("\n" + counter + " Files processed \n");
			//some tasks
			long endTime = System.currentTimeMillis();
			long difference = endTime - startTime;
			long minutes = (difference/1000)/60;
			long seconds = (difference / 1000) % 60;
			System.out.println("Excecution time: " + minutes + " Minute(s) " + seconds + " Second(s)");
			
			softAssert.assertAll();
			
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	// Create a method to test them
	public void testSQLReadStatement() throws IOException, SQLException {

		QADocumentService.numApprovedByFileId("PD50028768_258");
		QADocumentService.numLockedByFileId("PD50028768_258");
		QADocumentService.numUnlockedByFileId("PD50028768_258");
		QADocumentService.numValidatedByFileId("PD50028768_258");
		QADocumentService.numPublishedByFileId("PD50028768_258");
		QADocumentService.numValidationErrorsByFileId("PD50028768_258");
		QADocumentService.numUpdatedGroupByFileIdConference("PD50028768_258");
		QADocumentService.numUpdatedGroupByFileIdLiahona("PD50028768_258");
	}

	@Test
	// Test update statements
	public void updatedSQLStatement() throws IOException, SQLException, InterruptedException {

		QADocumentService.unlockByFileId("PD50028768_258");
		QADocumentService.addedByFileId("PD50028768_258");
		QADocumentService.validatedByFileId("PD50028768_258");
		QADocumentService.unvalidatedByFileId("PD50028768_258");
		QADocumentService.unlockByFileId("PD50028768_258");
		QADocumentService.approveByFileId("PD50028768_258");
		QADocumentService.unapproveByFileId("PD50028768_258");
		QADocumentService.updateGroupByFileIdGeneralConference("PD50028768_258");
		QADocumentService.updateGroupByFileIdLiahona("PD50028768_258");
		QADocumentService.deleteValidationErrorsByFileId("PD50028768_258");
		QADocumentService.deleteFatalValidateErrorsByFileId("PD50028768_258");
	}

	@Test
	// List <QAHtml5Document>
	public void sampleDocuments() throws IOException, SQLException {
	//QAHtml5Document HTML5docs = QADocumentService.fromFileId("1");
	//	QADocumentService.fromFileId("23");
	//	System.out.println(HTML5docs.getDocumentId());
	//	for (QAHtml5Document HTML5doc : HTML5docs) {
	 //	System.out.println(HTML5doc.getFileName());
	 	//}

	//	QADocumentService.getURIFromFileIDandType("lds","123");
	//	QADocumentService.getDocumentCountByState("1");
		QADocumentService.fromFileId("1");
		QADocumentService.fromURIAndLanguage("12",1);
		QADocumentService.fromFileName("1");
		QADocumentService.fromContentTypeAndFileId("1","1");
		QADocumentService.fromURIAndFileId("1","1");
		QADocumentService.fromURIAndLanguage("1",1);
	}
}
