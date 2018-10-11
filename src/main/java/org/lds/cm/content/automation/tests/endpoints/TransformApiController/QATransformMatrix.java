package org.lds.cm.content.automation.tests.endpoints.TransformApiController;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.lds.cm.content.automation.enums.DocumentSource;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.apache.http.entity.mime.MultipartEntityBuilder.create;

public class QATransformMatrix {
    
    @BeforeTest (alwaysRun = true)
    public void setup(ITestContext ctx) {
        TestRunner runner = (TestRunner) ctx;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss");
        LocalDateTime now = LocalDateTime.now();
        String outputDirectory = "C:\\TestNG-output\\" + dtf.format(now);
        runner.setOutputDirectory(outputDirectory);
    }

    //  Haven't seen a database call made through
//    @AfterMethod (alwaysRun = true) Constants.
//    public void cleanUp()   {   JDBCUtils.closeUp();    }

    @Test(enabled = true, groups="endpoints")
    public static void servicesApiTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/englishFilesForWebmlAndLDSXML ****\n");

        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/lead-kindly-light.html");
        QATransformService.transformFileGivenContentGroupIdServicesAPI(testFile, Constants.generalConferenceContentGroupId);

        QADeleteService.deleteFromDbByFileIdSingleDocument("testPD00028166_000_027");

    }

    @Test(enabled = true, groups="endpoints")
    public static void testTransformApiSource() throws Exception {

        System.out.println("\n**** In QATransformMatrix/testTransformApiSource ****\n");

        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/ye-are-the-temple-of-god.html");
        QATransformService.transformFileGivenContentGroupIdServicesAPI(testFile, Constants.generalConferenceContentGroupId);

        QADocumentService.verifySourceByFileId("testPD00028166_000_030", DocumentSource.TRANSFORM_API, false);

        QADeleteService.deleteFromDbByFileIdSingleDocument("testPD00028166_000_030");

    }

    @Test(enabled = true, groups="endpoints")
    public static void docxTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/docxTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/docx");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);

        }

        QADeleteService.bulkDeleteFromDbByFileId("");
    }

//    @Test(enabled = true, groups="endpoints")
//    public static void engBroadcastHTMLTransform() throws Exception {
//
//        System.out.println("\n**** In QATransformMatrix/engBroadcastHTMLTransform ****\n");
//
//        File startDir = new File(Constants.transformFileStartDir + "/engBroadcastHTML");
//        List<File> filesToTransform = new ArrayList<>();
//
//        QAFileUtils.loadTestFiles(filesToTransform, startDir);
//
//        for (File file : filesToTransform) {
////            System.out.println(file.getAbsolutePath());
//            String content = new Scanner(file).useDelimiter("\\Z").next();
////            content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/1999/REC-html401-19991224/strict.dtd\">\n" + content;
////            content.replace("<lds:meta xmlns:lds=\"http://www.lds.org/schema/lds-meta/v1\">", "<lds:meta xmlns:lds=\"http://www.lds.org/schema/lds-meta/v1\"/>");
//            System.out.println(content);
//
//            file = XMLUtils.removeDataAids(content, file);
//            System.out.println(file.toString());
//            QATransformService.transformFileGivenContentGroupId(file, Constants.broadcastContentGroupId);
//        }
//
//        QADeleteService.bulkDeleteFromDbByFileId("testPD00013126_000");
//    }

    @Test(enabled = true, groups="endpoints")
    public static void engBroadcastWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engBroadcastWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engBroadcastWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.broadcastContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD00045368_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engEnsignHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engEnsignHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engEnsignHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.ensignContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00091_000");
    }

    @Test(enabled = false, groups="endpoints")
    public static void engEnsignEntireYearTransform260() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engEnsignEntireYearTransform260 ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engEnsign2016");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.ensignContentGroupId);
        }

    }

    @Test(enabled = true, groups="endpoints")
    public static void engEnsignLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engEnsignLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engEnsignLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.ensignContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00097_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engFriendHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engFriendHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engFriendHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.friendContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00037_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engFriendLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engFriendLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engFriendLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.friendContentGroupId);

        }
        QADeleteService.bulkDeleteFromDbByFileId("test00096_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engGeneralConferenceHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engGeneralConferenceHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.generalConferenceContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD00028166_000");
    }

    @Test(enabled = false, groups="endpoints")
    public static void testHtml() throws Exception {

        System.out.println("\n**** In QATransformMatrix/testHtml ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/testHtml");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFile(file);

        }
        QADeleteService.bulkDeleteFromDbByFileId("test00019_000");
    }

    @Test(enabled = false, groups="endpoints")
    public static void testXML() throws Exception {

        System.out.println("\n**** In QATransformMatrix/testXML ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/testXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFile(file);
        }
//        QADeleteService.bulkDeleteFromDbByFileId("PD00009501_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engGeneralConferenceWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engGeneralConferenceWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engGeneralConferenceWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.generalConferenceContentGroupId);

        }
        QADeleteService.bulkDeleteFromDbByFileId("test00086_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engHymnsHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engHymnsHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engHymnsHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00043_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engHymnsAllHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engHymnsAllHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engHymnsAllHTMLTransform");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);

        }
        QADeleteService.bulkDeleteFromDbByFileId("test00043_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engLiahonaHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engLiahonaHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engLiahonaHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.liahonaContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00077_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engLiahonaLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engLiahonaLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engLiahonaLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.liahonaContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00095_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engNewEraHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engNewEraHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engNewEraHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.newEraContentGroupId);

        }
        QADeleteService.bulkDeleteFromDbByFileId("test00013_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engNewEraLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engNewEraLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engNewEraLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.newEraContentGroupId);

        }
        QADeleteService.bulkDeleteFromDbByFileId("test00093_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engManualHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engManualHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engManualHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);

        }
        QADeleteService.bulkDeleteFromDbByFileId("test00068_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engManualLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engManualLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engManualLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);

        }
        QADeleteService.bulkDeleteFromDbByFileId("test00094_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engScripturesHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engScripturesHTMLTransform ****\n");

        String scripturesGroupId;
        File startDir = new File(Constants.transformFileStartDir + "/engScripturesHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);
        for (File file : filesToTransform) {
            final JSONObject returnObj = QATransformService.transform(file);
            Assert.assertTrue(transformSuccess(returnObj));
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00039_000");
    }

    private static boolean transformSuccess(final JSONObject obj){
        return (boolean) obj.get("transformSuccess");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engScripturesLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engScripturesLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engScripturesLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.scripturesContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00093_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engVideoHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engVideoHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engVideoHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.testContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test1297VC187");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engYouthHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engYouthHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engYouthHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.testContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test-youth-learn-learning-teaching-ideas_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void engYouthWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/engYouthWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/engYouthWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.testContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("automation-transform-automation-transform-youth-activities_000");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnBroadcastHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnBroadcastHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnBroadcastHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.broadcastContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD30013126_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnBroadcastWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnBroadcastWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnBroadcastWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.broadcastContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD30050909_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnGeneralConferenceHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnGeneralConferenceHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnGeneralConferenceHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.generalConferenceContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD30015887_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnGeneralConferenceWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnGeneralConferenceWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnGeneralConferenceWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.generalConferenceContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD30015887_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnHymnsHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnHymnsHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnHymnsHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test30043_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnLiahonaHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnLiahonaHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnLiahonaHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.liahonaContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test30012_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnLiahonaLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnLiahonaLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnLiahonaLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.liahonaContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test30099_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnManualHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnManualHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnManualHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD30031652_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnManualLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnManualLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnManualLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test30098_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnScripturesHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnScripturesHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnScripturesHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.scripturesContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test30006_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnScripturesLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnScripturesLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnScripturesLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.scripturesContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("30097_258");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnVideoHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnVideoHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnVideoHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.testContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test1297VC2129");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnYouthHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnYouthHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnYouthHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.youthContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test-youth-learn-learning-teaching-ideas_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void jpnYouthWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/jpnYouthWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/jpnYouthWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.youthContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("automation-transform-youth-learn-ap_300");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusBroadcastHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusBroadcastHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusBroadcastHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.broadcastContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD60002153_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusBroadcastWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusBroadcastWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusBroadcastWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.broadcastContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD17353116_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusGeneralConferenceHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusGeneralConferenceHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusGeneralConferenceHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.generalConferenceContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD17321411_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusGeneralConferenceWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusGeneralConferenceWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusGeneralConferenceWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);


        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.generalConferenceContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD17309501_173");
    }


    @Test(enabled = true, groups="endpoints")
    public static void rusHymnsHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusHymnsHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusHymnsHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test17331_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusLiahonaHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusLiahonaHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusLiahonaHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.liahonaContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test17324_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusLiahonaLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusLiahonaLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusLiahonaLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.liahonaContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test17399_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusManualHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusManualHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusManualHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD17300906_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusManualLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusManualLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusManualLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test13301_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusScripturesHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusScripturesHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusScripturesHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            final JSONObject returnObj = QATransformService.transform(file);
            Assert.assertTrue(transformSuccess(returnObj));
            QATransformService.transformFileGivenContentGroupId(file, Constants.scripturesContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test17306_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusScripturesLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusScripturesLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusScripturesLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.scripturesContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("17398_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusVideoHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusVideoHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusVideoHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.testContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test1297VC2129");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusYouthHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusYouthHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusYouthHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.youthContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test-youth-learn-learning-teaching-ideas_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void rusYouthWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/rusYouthWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/rusYouthWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.youthContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("automation-transform-youth-activities_173");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaBroadcastHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaBroadcastHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaBroadcastHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.broadcastContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD00202798_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaBroadcastWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaBroadcastWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaBroadcastWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.broadcastContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("testPD00248587_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaGeneralConferenceHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaGeneralConferenceHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaGeneralConferenceHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.generalConferenceContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00298_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaGeneralConferenceWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaGeneralConferenceWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaGeneralConferenceWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.generalConferenceContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("PD00206561_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaHymnsHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaHymnsHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaHymnsHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00231_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaLiahonaHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaLiahonaHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaLiahonaHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.liahonaContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00223_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaLiahonaLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaLiahonaLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaLiahonaLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.liahonaContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00299_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaManualHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaManualHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaManualHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00213_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaManualLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaManualLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaManualLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.manualContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00297_002");
    }

    @Test(enabled = false, groups="endpoints")
    public static void spaScripturesHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaScripturesHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaScripturesHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.scripturesContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test00206_002");
    }

    @Test(enabled = false, groups="endpoints")
    public static void spaScripturesLDSXMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaScripturesLDSXMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaScripturesLDSXML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.scripturesContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("00297_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaVideoHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaVideoHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaVideoHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.testContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test1297VC21294");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaYouthHTMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaYouthHTMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaYouthHTML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.youthContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("test-youth-learn-learning-teaching-ideas_002");
    }

    @Test(enabled = true, groups="endpoints")
    public static void spaYouthWEBMLTransform() throws Exception {

        System.out.println("\n**** In QATransformMatrix/spaYouthWEBMLTransform ****\n");

        File startDir = new File(Constants.transformFileStartDir + "/spaYouthWEBML");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, Constants.youthContentGroupId);
        }
        QADeleteService.bulkDeleteFromDbByFileId("automation-transform-youth-activities_002");
    }


}
