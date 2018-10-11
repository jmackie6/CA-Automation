package org.lds.cm.content.automation.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.lds.cm.content.automation.model.QAContentPublish;
import org.lds.cm.content.automation.model.QAProcessLog;
import org.lds.cm.content.automation.service.QALockUnlockService;
import org.lds.cm.content.automation.service.QAPublishService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.QADocumentUtils;
import org.lds.cm.content.automation.util.XSLUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class QAPublishTest 
{	

	@Test(enabled=false)
	public static void publishTestRepublishWithNoChange() throws Exception{
		System.out.println("***** republish published file *****");
//		long docId = 973245L;
		String filePath = Constants.transformFileStartDir + "/PublishEndpointTestHTML/an-humble-and-a-contrite-heart.html";
		String docId = QADocumentUtils.getDocumentID(filePath);
        String userID = JDBCUtils.getTestUserID();

        if(!QATransformService.existsInDbByFilePath(filePath)){
        	final Map<String, Integer> expectedErrors = new HashMap<>();
			ErrorUtils.testFilesWithMaps(filePath, expectedErrors);
			ErrorUtils.testFilesWithMaps(filePath, expectedErrors);
		}
		String tableDocId = QADocumentUtils.getDocumentTableID(filePath);
//		QALockUnlockService.documentLock(tableDocId, userID);

		final QAContentPublish publishObj = QAContentPublish.fromDBWithDocumentID(tableDocId);
		if(publishObj != null && !publishObj.isPublished()){
			QAPublishService.publishFile(Long.valueOf(tableDocId));
		}

//		final JSONObject responseObj = QAPublishService.publishFile(Long.valueOf(tableDocId));
		final QAProcessLog processLog = QAPublishService.publishFile(Long.valueOf(tableDocId));
		Assert.assertTrue(processLog.wasSuccessfullyPublished());

	}

	@Test(enabled=false)
	public static void publishTestRepublishDeleteFirstPara() throws Exception{
		System.out.println("***** Republish minus first para *****");
		String filePath = Constants.transformFileStartDir + "/PublishEndpointTestHTML/pubTestMissingPara1/an-humble-and-a-contrite-heart.html";
//		String docId = QADocumentUtils.getDocumentID(filePath);
//		String xslFilePath = Constants.xslFileStartDir + "/RemoveParaID1.xsl";
//		String tempFilePath = XSLUtils.executeTransform(xslFilePath, filePath);
//		ErrorUtils.testFilesTransformToFail(tempFilePath);
		//Cleanup temp files
       ErrorUtils.testFilesTransformToFail(filePath);
//		Files.delete(Paths.get(tempFilePath));


	}

	@Test(enabled=false)//previously published file with para added
	public static void publishTestRepublishAddedNewPara() throws Exception{
		System.out.println("***** Repulish adding para *****");
		String filePath = Constants.transformFileStartDir + "/PublishEndpointTestHTML/pubTestAddedNewPara/an-humble-and-a-contrite-heart.html";
		String tableDocId = QADocumentUtils.getDocumentTableID(filePath);
		final QAContentPublish publishObj = QAContentPublish.fromDBWithDocumentID(filePath);
		if(publishObj != null && !publishObj.isPublished()){
			QAPublishService.publishFile(Long.valueOf(tableDocId));
		}

		final QAProcessLog processLog = QAPublishService.publishFile(Long.valueOf(tableDocId));
		Assert.assertTrue(processLog.wasSuccessfullyPublished());

	}

	@Test(enabled=false)//previously published file with edited para
	public static void publishTestReublishEditedPara() throws Exception{
		System.out.println("***** Repulish with edited para *****");
		String userID = JDBCUtils.getTestUserID();
		String filePath = Constants.transformFileStartDir + "/PublishEndpointTestHTML/pubTestEditedPara/an-humble-and-a-contrite-heart.html";
		String tableDocId = QADocumentUtils.getDocumentTableID(filePath);
		final QAContentPublish publishObj = QAContentPublish.fromDBWithDocumentID(filePath);
		if(publishObj != null && !publishObj.isPublished()){
			QAPublishService.publishFile(Long.valueOf(tableDocId));
		}

		final QAProcessLog processLog = QAPublishService.publishFile(Long.valueOf(tableDocId));
		Assert.assertTrue(processLog.wasSuccessfullyPublished());
	}

	@Test(enabled=false) //previously published locked document
	public static void publishTestLockedDoc() throws Exception{
		System.out.println("***** Publish locked document *****");
		String filePath = Constants.transformFileStartDir + "/PublishEndpointTestHTML/pubTestLockedDoc/an-humble-and-a-contrite-heart.html";
		String userID = JDBCUtils.getTestUserID();
		String tableDocId = QADocumentUtils.getDocumentTableID(filePath);
		QALockUnlockService.documentLock(tableDocId, userID);
		final QAContentPublish publishObj = QAContentPublish.fromDBWithDocumentID(tableDocId);
		if(publishObj != null && !publishObj.isPublished()){
			QAPublishService.publishFile(Long.valueOf(tableDocId));
		}

		final QAProcessLog processLog = QAPublishService.publishFile(Long.valueOf(tableDocId));
		Assert.assertTrue(processLog.wasSuccessfullyPublished());

	}

}
