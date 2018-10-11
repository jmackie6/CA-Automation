package org.lds.cm.content.automation.tests.endpoints.TransformApiController;

import org.json.simple.JSONObject;
import org.lds.cm.content.automation.enums.DocumentSource;
import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import oracle.xdb.XMLType;

import java.io.File;
import java.sql.ResultSet;

public class TransformEditorialTest {

    @AfterMethod(alwaysRun = true)
    public static void closeUp() { JDBCUtils.closeUp(); }

//    @Test(groups="endpoints", timeOut=180000)
//    public void compareContentCentralToEventCentral() throws Exception {
//        // Upload a file from Event Central, make edits to that same file,
//        // then upload the file again
//        JSONObject json = new JSONObject();
//        json.put("languageCode", 0);
//        json.put("controlId", 45);
//        json.put("navigationDateMonth", 10);
//        json.put("navigationDateYear", 2020);
//        json.put("pdNumber", "PD87654321");
//        json.put("authorName", "Jeffrey R Holland");
//        json.put("authorSurname", "Holland");
//        json.put("eventName", "Test General Conference 2020");
//        json.put("shortTitle", "Test General 2020");
//        json.put("navigationTitle", "Be A Believer");
//        json.put("sessionName", "Sunday Morning");
//        json.put("category", "General Conference");
//        json.put("isoLang", "eng");
//
//        String metadata = json.toString();
//        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/eventCentralOriginal.docx");
//        JSONObject response = QATransformService.editorialTransform(testFile, metadata);
//        String transformSuccess = response.get("transformSuccess").toString();
//        String fileId = "PD87654321_000%";
//
//        File testFile2 = new File(Constants.transformFileStartDir + "/EventCentralDocx/eventCentralCompare.docx");
//        JSONObject response2 = QATransformService.editorialTransform(testFile2, metadata);
//        String transformSuccess2 = response2.get("transformSuccess").toString();
//
//
//        // Get the file contents by file_id from CC DB
//        String xml = "";
//        if (transformSuccess.equals("true") && transformSuccess2.equals("true")){
//            ResultSet rs = JDBCUtils.getResultSet("select * from document where file_id like '" + fileId + "'");
//            if (rs.next()){
//                XMLType docXML = (XMLType) rs.getObject("document_xml");
//                xml = docXML.getStringVal();
//                System.out.println(xml);
//            } else {
//                System.out.println("Nothing was returned from the database. Check your query and try again.");
//            }
//        } else {
//            System.out.println("The transform was not successful.");
//        }
//
//        Assert.assertTrue(xml.contains("Making a change to this document."), "X");
//        // Delete the file for future use
//        QADeleteService.deleteFromDbByFileIdSingleDocument("PD87654321_000%");
//
//    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBasicCall () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 9);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD66333333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QADocumentService.numAddedByFileId("PD66333333_000_43uchtdorf") == 1);
        Assert.assertTrue(QADocumentService.numApprovedByFileId("PD66333333_000_43uchtdorf") == 0);
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD66333333_000_43uchtdorf"));

        QADocumentService.verifySourceByFileId("PD66333333_000_43uchtdorf", DocumentSource.EDITORIAL_API, false);

        QADeleteService.deleteFromDbByFileIdSingleDocument("PD66333333_000_43uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformSourceTest () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 9);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD31415926");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD31415926_000_43uchtdorf"));

        QADocumentService.verifySourceByFileId("PD31415926_000_43uchtdorf", DocumentSource.EDITORIAL_API, false);

        QADeleteService.deleteFromDbByFileIdSingleDocument("PD31415926_000_43uchtdorf");

    }


    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformConductingNotes () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", "1x");
        json.put("navigationDateMonth", 9);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD66333322");
        json.put("authorName", "Henry B. Eyring");
        json.put("authorSurname", "Eyring");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Conducting Notes");
        json.put("sessionName", "Saturday Afternoon Session");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/conducting_notes.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD66333322_000_1xeyring"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD66333322_000_1xeyring");

    }


    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformOffCameraContinuity () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", "1w");
        json.put("navigationDateMonth", 9);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD66333323");
        json.put("authorName", "Off Camera");
        json.put("authorSurname", "Camera");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Off Camera Continuity");
        json.put("sessionName", "Saturday Afternoon Session");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/off_camera_continuity.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD66333323_000_1xeyring"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD66333323_000_1wcamera");

    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformAddedStateAndNotApproved () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 44);
        json.put("navigationDateMonth", 9);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD66333388");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD66333388_000_43uchtdorf"));
        Assert.assertTrue(QADocumentService.numAddedByFileId("PD66333388_000_43uchtdorf") == 1);
        Assert.assertTrue(QADocumentService.numApprovedByFileId("PD66333388_000_43uchtdorf") == 0);
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD66333388_000_44uchtdorf");

    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadFileTypeHtml () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 13);
        json.put("navigationDateMonth", 4);
        json.put("navigationDateYear", 2010);
        json.put("pdNumber", "PD36333339");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test-General-Conference-2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test-General-Conference-2019");
        json.put("sessionName", "Priesthood Session");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/lead-kindly-light.html");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("Invalid file received, this endpoint only accepts DocX"));
        Assert.assertFalse(QATransformService.existsInDbByFileId("PD36333339_000_43uchtdorf"));

    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadFileTypeXML () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 13);
        json.put("navigationDateMonth", 4);
        json.put("navigationDateYear", 2010);
        json.put("pdNumber", "PD36333338");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test-General-Conference-2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test-General-Conference-2019");
        json.put("sessionName", "Priesthood Session");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/engFriendLDSXML/_content-magazines-eng-00664_000_000.xml");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("Invalid file received, this endpoint only accepts DocX"));
        Assert.assertFalse(QATransformService.existsInDbByFileId("PD36333338_000_43uchtdorf"));

    }

    //TODO not done need to check database for uri and groupID
    @Test
    public void editorialTransformNonGeneralConferenceCategory () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 11);
        json.put("navigationDateMonth", 2);
        json.put("navigationDateYear", 2020);
        json.put("pdNumber", "PD36443335");
        json.put("authorName", "D. Todd Christofferson");
        json.put("authorSurname", "Christofferson");
        json.put("eventName", "north american west scb");
        json.put("shortTitle", "north american west scb");
        json.put("navigationTitle", "Test Broadcast 2020");
        json.put("sessionName", "Stake Conference Broadcast");
        json.put("category", "Stake Conference Broadcasts");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/stake_conference_broadcast.docx");



        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD36443335_000_11christofferson"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD36443335_000_11christofferson");
        //check content group to make sure that it is gen confe

    }

    @Test
    public void editorialTransformNoDefualtContentGroup () throws Exception {
        QATransformService.uncheckDefaultContentGroup();

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 10);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD36333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019");
        json.put("sessionName", "Priesthood Session");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD36333335_000_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD36333335_000_43uchtdorf");
    }

    //TODO not done need to check database for uri and groupID
    @Test
    public void editorialTransformNoDefaultGroupNonGenConf() throws Exception {
        QATransformService.uncheckDefaultContentGroup();

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 11);
        json.put("navigationDateMonth", 3);
        json.put("navigationDateYear", 2020);
        json.put("pdNumber", "PD36447635");
        json.put("authorName", "D. Todd Christofferson");
        json.put("authorSurname", "Christofferson");
        json.put("eventName", "north american west scb");
        json.put("shortTitle", "north american west scb");
        json.put("navigationTitle", "Test Broadcast 2020");
        json.put("sessionName", "Stake Conference Broadcast");
        json.put("category", "Christmas Devotional");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/stake_conference_broadcast.docx");

        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD36447635_000_11christofferson"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD36447635_000_11christofferson");
    }


    @Test
    public void editorialTransformIncompleteMetadataPDNumber () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();
        json.put("languageCode", 140);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 1);
        json.put("navigationDateYear", 2019);
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 pd number");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "fra");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("Missing event metadata value"));
    }


    @Test
    public void editorialTransformIncompleteMetadataCategory () throws Exception {
        QATransformService.uncheckDefaultContentGroup();

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 2);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD22333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 category");
        json.put("sessionName", "Sunday Morning");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("Missing event metadata value"));
        Assert.assertFalse(QATransformService.existsInDbByFileId("PD22333335_000_43uchtdorf"));
    }


    @Test
    public void editorialTransformIncompleteMetadataLanguageCode () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();

        json.put("controlId", 43);
        json.put("navigationDateMonth", 3);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD33333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 language code");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("Missing event metadata value"));
        Assert.assertFalse(QATransformService.existsInDbByFileId("PD33333335_000_43uchtdorf"));
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformIncompleteMetadataControlId () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("navigationDateMonth", 4);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD44333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 control id");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("\"transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD44333335_000_43uchtdorf"));
    }

    @Test
    public void editorialTransformIncompleteMetadataNavigationMonth () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD55333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 nav month");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("Missing event metadata value"));
        Assert.assertFalse(QATransformService.existsInDbByFileId("PD55333335_000_43uchtdorf"));
    }


    @Test
    public void editorialTransformIncompleteMetadataNavigationYear () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 6);
        json.put("pdNumber", "PD66333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 nav year");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("Missing event metadata value"));
        Assert.assertFalse(QATransformService.existsInDbByFileId("PD66333335_000_43uchtdorf"));
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformIncompleteMetadataAuthorName () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 7);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD77333335");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 author name");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD77333335_000_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD77333335_000_43uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformIncompleteMetadataAuthorSurname () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 8);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD88333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 author surname");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD88333335_000_43"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD88333335_000_43");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformIncompleteMetadataAuthorNameAndAuthorSurname () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 9);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD99333335");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 name and surname");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD99333335_000_43"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD99333335_000_43");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformIncompleteMetadataEventName () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 10);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD10333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 event name");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD10333335_000_43"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD10333335_000_43");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformIncompleteMetadataShortTile () throws Exception {
        QATransformService.uncheckDefaultContentGroup();
        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 11);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD11333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("navigationTitle", "Test General Conference 2019 short title");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD11333335_000_43"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD11333335_000_43");

    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformIncompleteMetadataNavigationTitle () throws Exception {
        QATransformService.uncheckDefaultContentGroup();

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 12);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD12333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("eventName", "Test-General-Conference-2019");
        json.put("shortTitle", "Test General 2019");
        json.put("sessionName", "Priesthood Session");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD12333335_000_43"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD12333335_000_43");
    }


    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformIncompleteMetadataSessionName () throws Exception {
        QATransformService.uncheckDefaultContentGroup();

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 1);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD13333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 session name");
        json.put("category", "General Conference");
        json.put("isoLang", "eng");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD13333335_000_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD13333335_000_43uchtdorf");
    }


    @Test
    public void editorialTransformIncompleteMetadataIsoLang () throws Exception {
        QATransformService.uncheckDefaultContentGroup();

        JSONObject json = new JSONObject();
        json.put("languageCode", 0);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 2);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD14333335");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Test General Conference 2019 iso lang");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("Missing event metadata value") || response.contains("The language attribute must not be a null value"));
        Assert.assertFalse(QATransformService.existsInDbByFileId("PD14333335_000_43uchtdorf"));
    }









    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataPDNumber () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 1);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "3456435634575375367538548356");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer pd Number");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("3456435634575375367538548356_002_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("3456435634575375367538548356_002_43uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataCategory () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 2);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD22233333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer Category");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "asdgdsfgdfghdafa asdfgd ");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD22233333_002_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD22233333_002_43uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataLanguageCode () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 234567);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 3);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD66663333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer Language code");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD66663333_234567_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD66663333_234567_43uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataControlId () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", "asdgds");
        json.put("navigationDateMonth", 4);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD44333333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer Control ID");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Thread.sleep(2000);
        Assert.assertTrue(response.contains("Invalid metadata received"));
        Assert.assertFalse(QATransformService.existsInDbByFileId("PD44333333_002_asdgdsuchtdorf"));
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataNavigationMonth () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 45);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD55333333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer Nav Month");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD55333333_002_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD55333333_002_43uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataNavigationYear () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 6);
        json.put("navigationDateYear", 45342632);
        json.put("pdNumber", "PD66666633");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer Nav Year");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD66666633_002_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD66666633_002_43uchtdorf");

    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataAuthorName () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 7);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD77333333");
        json.put("authorName", "Dieter-F& Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer author name");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/42HOLLAND-56.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD77333333_002_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD77333333_002_43uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataAuthorSurname () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 8);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD88333333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "&-Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer author surname");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD88333333_002_43&-uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD88333333_002_43&-uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataEventName () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 9);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD99333333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test-General-Conference & ^ %2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer event name");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD99333333_002_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD99333333_002_43uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataShortTile () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 10);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD10333333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test-General & 2019");
        json.put("navigationTitle", "Be A Believer short title");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD10333333_002_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD10333333_002_43uchtdorf");
    }

    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataNavigationTitle () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 11);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD11333333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be-A Believer&");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD11333333_002_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD11333333_002_43uchtdorf");
    }


    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataSessionName () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 12);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD12333333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer seession name");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "spa");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("transformSuccess\":true"));
        Assert.assertTrue(QATransformService.existsInDbByFileId("PD12333333_002_43uchtdorf"));
        QADeleteService.deleteFromDbByFileIdSingleDocument("PD12333333_002_43uchtdorf");
    }


    @Test(groups="endpoints", timeOut=180000)
    public void editorialTransformBadMetadataIsoLang () throws Exception {

        JSONObject json = new JSONObject();
        json.put("languageCode", 2);
        json.put("controlId", 43);
        json.put("navigationDateMonth", 1);
        json.put("navigationDateYear", 2019);
        json.put("pdNumber", "PD13333333");
        json.put("authorName", "Dieter F Uchtdorf");
        json.put("authorSurname", "Uchtdorf");
        json.put("eventName", "Test General Conference 2019");
        json.put("shortTitle", "Test General 2019");
        json.put("navigationTitle", "Be A Believer iso lang");
        json.put("sessionName", "Sunday Morning");
        json.put("category", "General Conference");
        json.put("isoLang", "dfjskalfhsdldfka");

        String metadata = json.toString();
        File testFile = new File(Constants.transformFileStartDir + "/EventCentralDocx/43uchtdorf.docx");
        String response = QATransformService.editorialTransform(testFile, metadata).toString();
        System.out.println(response);
        Assert.assertTrue(response.contains("The language attribute must not be a null value"));
        Assert.assertFalse(QATransformService.existsInDbByFileId("PD13333333_002_43uchtdorf"));
    }

}
