package org.lds.cm.content.automation.tests;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lds.cm.content.automation.model.QAContentPublish;
import org.lds.cm.content.automation.service.QACrossRefService;
import org.lds.cm.content.automation.service.QADocMapService;
import org.lds.cm.content.automation.service.QAPublishService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.QAFileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/*Cross References
 
Source PD10052296_000_000	Destination 36481_000_017

class = “legacy-cross-ref” remain legacy if target not activated
change to cross-ref if activated, deactivate if one is published other is not

Scenarios
1. Transform source only – inactive (remains legacy-cross-ref)
2. Transform destination then source - activate automatically changes from
	<a class= “legacy-cross-ref” file-id=”36481_000_017” para =”23”> to
	<a class= “cross-ref” href=”../../teachings_ joseph_smith/lesson3.html” para =”19”>
3. Transform source then destination – activate automatically after dest transforms
4. Publish source only – switches back to inactive
	-invalidate class=legacy-cross-ref” data=legacy-href=”../../teachings_ joseph_smith/lesson3.html?para=p19”
5. Publish destination then source - activate automatically
6. Publish source then destination – activate automatically after dest publishes
7. Cycles cross refs point to each other
8. 	T src, dest
	P src, dest
	T src again
	
*/

public class QACrossRefTest {

	//unused variables so commeting them out
	//String source  = "PD10052296_000_000";
	//String destination = "36481_000_017";
	
	// 1. Transform
	@Test
	public void crossRefTest() throws Exception {
		File startDir = new File(Constants.transformFileStartDir); 
		System.out.println("\n**** In QACrossRefTest: ****\n");
		List<File> filesToTransform = new ArrayList<>();
		QAFileUtils.loadTestFiles(filesToTransform, startDir);
		
		for (File file : filesToTransform) {
			System.out.println(file.getAbsolutePath());
			// QATransformService.transformFile(file);
			Assert.assertNotNull(file, "Transform for " + file + " came back null");
		}
	}
	
	
	//2. Verify uri-map src		MarkLogic  /preview/content-automation/uri-mapping
	//3. Verify uri-map dest
	@Test 
	public void verifyFilesInURIMappingTest() throws IOException {
		//String sourceURI = source + "-uris.xml";
		String sourceURI = "31118_000_000-uris.xml";
		//String destURI = destination + "-uris.xml";
		QACrossRefService.verifyFilesInURIMapping(sourceURI);
		System.out.println("Document: " + sourceURI + " found in ML DB");
		Assert.assertNotNull(sourceURI, "Language for " + sourceURI + " came back null");
		/*QACrossRefService.verifyFilesInURIMapping(destURI);
		System.out.println("Document: " + destURI + " found in ML DB");
		Assert.assertNotNull(destURI, "Language for " + destURI + " came back null");*/
		
	}
	
		
	
	@Test
	public void verifyDocMapinAnnotationServer() throws SQLException, IOException {
		String fileID = "00782_059_027";
		QAContentPublish doc = QAPublishService.fromFileIDLike(fileID);
		if(doc.isEmpty()) {
			Assert.fail("Document: " + fileID + " not in DB");
		}

			String dataAid = doc.getDataAid();
			// Should fail if count==0
			System.out.println("returned count: " + QADocMapService.getDocMap(dataAid) + " for data-aid: " + dataAid);
		}
	
	
		
	/*4. Check cross refs
	 	any that should have enable became live
		any that shouldn't enable remain dead
		
		load 36481_000_000.xml
		
*/	
	
	/*5. Repeat
	a. Looks for URI-MAP for destination  
	b. Xpath on URI-MAP for file-id
	c. Look for para
		ci. build URL
			cii. change cross-ref*/
	
/*
#################################################
Deleting
Has to be done on the source and the destination

1. Delete documents from Oracle
2. Delete doc-map
3. Delete URI-MAPPING from ML
4. Delete annotations
#################################################
*/	
	/*@Test
	public void deleteFromOracleTest() throws SQLException, IOException {  //SPLIT OUT, THEY CAN'T BE RUN AT THE SAME TIME
		String fileID = "36481_000_000";
		QACrossRefService.deleteFromValidationError(fileID);
		QACrossRefService.deleteFromPublishValidationError(fileID);
		QACrossRefService.deleteFromContentPublish(fileID);
		QACrossRefService.deleteFromDocumentContentGroup(fileID);
		QACrossRefService.deleteFromDocument(fileID);
	}*/
	
	@Test void deleteDocMapTest() {
		String dataAid = "5539161";
		QACrossRefService.deleteDocMap(dataAid);
	}
	
	@Test (dependsOnMethods={"deleteDocMapTest"})
	public void deleteURIMappingTest() {
		QACrossRefService.deleteURIMapping("36481_000_000-uris.xml");
	}
	
	@Test (dependsOnMethods={"deleteURIMappingTest"})
	public void deleteAnnotationsTest() {
		QACrossRefService.deleteAnnotations("36481_000_000-uris.xml");
	}
	
/*	@Test
	public void lockContent() {
		QACrossRefService.LockContent(uriPattern, languages, appUserId);
		
	}
	
	@Test
	public void unlockContent() {
		QACrossRefService.UnlockContent(uriPattern, languages, appUserId);
		
	}*/
	


}
