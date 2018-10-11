package org.lds.cm.content.automation.tests;

import oracle.xdb.XMLType;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.service.QALockUnlockService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QATransformTest {

// 1. Transform each file
// 2. Query database for files matching fileId and contentType
// 3. For each html5 file, get docInfo object
// 4. Verify docInfo object is correct (call verification method)
// 		a. fileId matches xmlFileId
// 		b. language matches xmlLanguage
// 5. Verify uri-mapping file exists in MarkLogic
// 6. Can we iterate through uri-mapping to verify all files were created in oracle?


    // 1. Transform each file
    @Test(enabled=false)
    public static void testTransform() throws Exception {

        System.out.println("\n**** In QATransformTest/testTransform module ****\n");

        File startDir = new File(Constants.transformFileStartDir);
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            if (file.getName().endsWith(".docx")) {
                System.out.println("Skipping this file because we can't get the content group for it yet");
            } else {
                QATransformService.transformFile(file);    // 1. Transform each file
            }
        }
    }

    // 2. Query database for files matching fileId and contentType
    @Test(enabled = false)
    public static void checkContentTypeandFileIdTest() throws SQLException, IOException {
        String type = "article";
        //String fileId = "10735_000_035";
        String fileId = "03122_000_020";  //should fail for type = chapter


        for (QAHtml5Document document : QATransformService.fromContentTypeAndFileId(type, fileId)) {
            System.out.println("checkContentTypeandFileIdTest: \n"
                    + "Content Type" + " | " + "File Id" + "\n"
                    + "-------------------------------------------------------\n"
                    + document.getContentType() + "      |   " + document.getFileId());

            Assert.assertNotNull(type, "Content Type for " + type + " came back null");
            Assert.assertEquals(document.getContentType(), type);
            Assert.assertNotNull(fileId, "File Id for " + fileId + " came back null");
            Assert.assertEquals(document.getFileId(), fileId);
        }
    }

    // 3. For each html5 file, get docInfo object
    @Test(enabled = false)
    public static void getDocInfo() {
        System.out.println("In the QATransformTest/getDocInfo module");
    }

    // 4. Verify docInfo object is correct (call verification method)
    //	a. fileId matches xmlFileId
    //	b. language matches xmlLanguage
    @Test(enabled = false)
    public static void verifyDocInfoCorrect() {
        System.out.println("In the QATransformTest/verifyDocInfoCorrect module");
    }

    static String user_id = "81";

    @Test(enabled = false)
    public static void unlockDocs() throws SQLException, IOException {

        List<String> items = new ArrayList<>();

        items.add("1396412");
        items.add("1396413");
        items.add("1396414");


        for (String item : items) {

            QALockUnlockService.documentUnlock(item, user_id);
        }
    }



    // 6. Can we iterate through uri-mapping to verify all files were created in oracle?
    @Test(enabled = false)
    public static void iterateUriMapping() {
        System.out.println("In the QATransformTest/iterateUriMapping module");

    }

//    Enterprise Tester #1533 - change data-aid
    @Test(enabled=true)
    public static void changeDataAIDTest() throws IOException, ParseException {
//        String fileName = Constants.transformFileStartDir + "/DataAIDHTML/06897_266_bible-maps_map.html";
        String fileName = Constants.transformFileStartDir + "/DataAIDHTML/family-finances.html";
        ErrorUtils.testFilesTransformToFail(fileName);

    }

//    Enterprise Tester #1533 - change fileName
    @Test(enabled = true)
    public static void changeFileName() throws Exception {
        String fileName = Constants.transformFileStartDir + "/DataAIDHTML/changeFileName/acquiring-spiritual-knowledge.html";
        ErrorUtils.testFilesTransformToFail(fileName);
    }

//    Enterprise Tester #1746 - can't have folder level (manifest level) uri
    @Test(enabled=true)
    public static void folderLevelUriTest() throws IOException, ParseException{
        String fileName = Constants.transformFileStartDir + "/DataAIDHTML/acquiring-spiritual-knowledge_manifest_uri.html";
        ErrorUtils.testFilesTransformToFail(fileName);
    }


    ///TODO: 1. Get all the data-aids and make sure they are all empty
    ///      2. Delete the file from database - use QADeleteService - delete file
    @Test(enabled = true)//not removing the data-aids
    public static void removeDataAIDNewTransform() throws Exception {
        String fileName = Constants.transformFileStartDir + "/DataAIDHTML/acquiring-spiritual-knowledge.html";
        String tableDocId = QADocumentUtils.getDocumentTableID(fileName);
        String testUserId = JDBCUtils.getTestUserID();
        QALockUnlockService.documentUnlock(tableDocId, testUserId);

        final JSONObject jObj = QATransformService.transform(new File(fileName));
        String sqlQuery = "SELECT EXTRACT(d.DOCUMENT_XML, '/html') documentXML FROM DOCUMENT d WHERE d.document_id = " + tableDocId;
        final ResultSet rs = JDBCUtils.getResultSet(sqlQuery);
        final StringBuilder sb = new StringBuilder();
        if (rs.next()) {
            XMLType xml = (XMLType)rs.getObject("documentXML");
            sb.append(xml.getStringVal());
        }
        final Document tempDoc = XMLUtils.getDocumentFromString(sb.toString());
        final NodeList tempNodeList = XPathUtils.getElements("//@data-aid[normalize-space()]", tempDoc);
        Assert.assertEquals(tempNodeList.getLength(), 0);
        final NodeList tempNodeList2 = XPathUtils.getElements("//@data-aid-version[normalize-space()]", tempDoc);
        Assert.assertEquals(tempNodeList2.getLength(), 0);
        String fileID = XPathUtils.getStringValue("//lds:title[@type='file']", tempDoc, true);
        QADeleteService.bulkDeleteFromDbByFileId(fileID);

    }

    @Test(enabled = true)
    public static void changeFilenameTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/CoverArtManifestTest.html";
        final Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }


}



