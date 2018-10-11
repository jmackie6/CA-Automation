package org.lds.cm.content.automation.tests.endpoints.TranslationApiController;

import oracle.xdb.XMLType;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.model.ReturnObj;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class TranslationGetDocumentTest {

    @AfterMethod (alwaysRun = true)
    public static void closeUp() { JDBCUtils.closeUp(); }

    @BeforeClass(alwaysRun = true)
    public static void transform() throws Exception {
        QADocumentService.unlockByFileId("TRANSLATIONPD60005236_000_12nelson");
        File testFile = new File(Constants.translationStartDir + "/translation_get_doc.html");
        QATransformService.transformFileGivenContentGroupId(testFile, Constants.manualContentGroupId);
    }

    @Test(groups= { "endpoints" })
    public static void lockedDocumentTest() throws ParseException, IOException, SQLException, IllegalArgumentException{
        String fileId = "TRANSLATIONPD60005236_000_12nelson";
        QADocumentService.lockByFileId(fileId);
        String languageCode = "000";
        String uri = "/test/translation-get/broadcasts/worldwide-devotional-for-young-adults/2018/06/hope-of-israel";
        String htmlResponse = getTranslationUriAndLang(languageCode, uri);
        System.out.println("This is the response: " + htmlResponse);

        Assert.assertTrue(htmlResponse.contains("/test/translation-get/broadcasts/worldwide-devotional-for-young-adults/2018/06/hope-of-israel"));
        Assert.assertTrue(htmlResponse.contains("President and Sister Nelson encourage youth to join the Lord’s battalion to help gather Israel."));
        //compareEndpointAndDb(htmlResponse, fileId);
        //make sure the lang tag gets stripped
        Assert.assertTrue(htmlResponse.contains("lang=\"\""));
    }

    @Test(groups= { "endpoints" })
    public static void unlockedDocumentTest() throws ParseException, IOException, SQLException {
        QADocumentService.unlockByFileId("TRANSLATIONPD60005236_000_12nelson");
        String languageCode = "000";
        String uri = "/test/translation-get/broadcasts/worldwide-devotional-for-young-adults/2018/06/hope-of-israel";
        //check database first for documents for that langcode and uri
        String htmlResponse = getTranslationUriAndLang(languageCode, uri);
        //String endpointURL = Constants.epTranslationDocument + URI + "&languageId=" + languageId;
        //getTranslationUriAndLang(languageCode, uri)
        int status = NetUtils.getResponseStatus(Constants.epTranslationDocument + uri + "&languageId=" + languageCode);
        System.out.println(htmlResponse);


        //response gets 200 back
        Assert.assertEquals(status, 200);
        Assert.assertTrue(htmlResponse.contains("Document is not source locked in Content Central"));
        Assert.assertTrue(htmlResponse.contains("/test/translation-get/broadcasts/worldwide-devotional-for-young-adults/2018/06/hope-of-israel"));

    }

    @Test(groups= { "endpoints" })
    public static void InvalidUriAndLangTest() throws ParseException, IOException, SQLException {

        String languageCode = "325435342";
        String uri = "/thisIsATestUri/test";
        String htmlResponse = getTranslationUriAndLang(languageCode, uri);
        System.out.println(htmlResponse);
        Assert.assertEquals(htmlResponse, "Invalid languageId specified: " + languageCode);
    }

    @Test(groups= { "endpoints" })
    public static void validUriInvalidLangTest() throws ParseException, IOException, SQLException {

        String languageCode = "23435";
        String uri = "/test/translation-get/broadcasts/worldwide-devotional-for-young-adults/2018/06/hope-of-israel";
        //check database first for documents fo that langcode and uri
        String htmlResponse = getTranslationUriAndLang(languageCode, uri);
        System.out.println(htmlResponse);
        Assert.assertEquals(htmlResponse, "Invalid languageId specified: " + languageCode);
    }

    @Test(groups= { "endpoints" })
    public static void validUriMissingLangTest() throws ParseException, IOException, SQLException {

        String param = "/test/translation-get/broadcasts/worldwide-devotional-for-young-adults/2018/06/hope-of-israel";
        boolean lang = false;
        boolean uri = true;
        //check database first for documents for that langcode and uri
        String htmlResponse = getTranslationUriAndLangMissingParams(lang, uri, param);
        System.out.println(htmlResponse);
        Assert.assertTrue(htmlResponse.contains("You have received an error while accessing Content Central."), "Test Failed");
    }

    @Test(groups= { "endpoints" })
    public static void validLangInvalidUriTest() throws ParseException, IOException, SQLException {

        QADocumentService.lockByFileId("TRANSLATIONPD60005236_000_12nelson");
        String languageCode = "000";
        String uri = "/thisIsATestUri/test";
        String htmlResponse = getTranslationUriAndLang(languageCode, uri);
        System.out.println(htmlResponse);
        Assert.assertEquals(htmlResponse, "No file could be found for languageId  0 and uri "  + uri);
    }

    @Test(groups= { "endpoints" })
    public static void missingUriValidLangTest() throws ParseException, IOException, SQLException {

        String param = "000";
        boolean lang = true;
        boolean uri = false;
        String htmlResponse = getTranslationUriAndLangMissingParams(lang, uri, param);
        System.out.println(htmlResponse);
//        Assert.assertEquals(htmlResponse, "Both uri and languageId are required parameters");
        Assert.assertTrue(htmlResponse.contains("You have received an error while accessing Content Central."), "Test Failed");
    }

    @Test(groups= { "endpoints" })
    public static void missingUriAndLangTest() throws ParseException, IOException, SQLException {

        String param = "test";
        String url = Constants.epTranslationDocument + "ur="  + param + "&languagId=" + param;
        String htmlResponse = NetUtils.getHTML(url);
        System.out.println(htmlResponse);
//        Assert.assertEquals(htmlResponse, "Both uri and languageId are required parameters");
        Assert.assertTrue(htmlResponse.contains("You have received an error while accessing Content Central."), "Test Failed");
    }

    @Test(groups= { "endpoints" })
    public static void stripsDataAidAndVersionTest() throws ParseException, IOException, SQLException {

        QADocumentService.lockByFileId("TESTTRANSLATIONPD50028768_002_3020");
        String languageCode = "002";
        String uri = "/test/translation/general-conference/2011/04/hope";
        String htmlResponse = getTranslationUriAndLang(languageCode, uri);
        System.out.println(htmlResponse);
        boolean b1 = Pattern.matches("data-aid=\"(\\d{7,11})\"", htmlResponse);
        System.out.println(b1);

        boolean b2 = Pattern.matches("data-aid-version=\"(\\d{7,11})\"", htmlResponse);
        System.out.println(b2);
        Assert.assertTrue(htmlResponse.contains("/test/translation/general-conference/2011/04/hope"));
        //make sure the lang tag gets stripped
        Assert.assertTrue(htmlResponse.contains("lang=\"\""));
        //make sure that data-aids get stripped
        Assert.assertFalse(b1);
        Assert.assertFalse(b2);
    }

    @Test(groups = { "endpoints" })
    public static void getDocumentByDocumentIDGCPostEventTrueLocked() throws SQLException, IOException {
        String docId = "235878";
        String fileId = "PD50021411_140_0040";
        String htmlResponse = getTranslationDocumentId(docId, fileId,true, "lock");
        System.out.println(htmlResponse);

        Assert.assertTrue(htmlResponse.contains("/general-conference/2010/10/charity-never-faileth"));
        Assert.assertTrue(htmlResponse.contains("Au lieu de nous juger et de nous critiquer mutuellement, puissions-nous avoir l’amour pur du Christ pour nos compagnons de route dans ce voyage de la vie."));
        Assert.assertTrue(htmlResponse.contains("lang=\"\""));

    }

    @Test(groups = { "endpoints" })
    public static void getDocumentByDocumentIDGCPostEventTrueUnLocked() throws SQLException, IOException {
        String docId = "235878";
        String fileId = "PD50021411_140_0040";
        String htmlResponse = getTranslationDocumentId(docId, fileId,true, "unlock");
        System.out.println(htmlResponse);

        Assert.assertTrue(htmlResponse.contains("Unable to retrieve file.  Document is not source locked in Content Central.  documentId: 235878"));
    }

    @Test(groups = { "endpoints" })
    public static void getDocumentByDocIdGCPostEventFalseLocked() throws SQLException ,IOException {
        String docId = "235878";
        String fileId = "PD50021411_140_0040";
        String htmlResponse = getTranslationDocumentId(docId, fileId,false, "lock");
        System.out.println(htmlResponse);

        Assert.assertTrue(htmlResponse.contains("/general-conference/2010/10/charity-never-faileth"));
        Assert.assertTrue(htmlResponse.contains("Au lieu de nous juger et de nous critiquer mutuellement, puissions-nous avoir l’amour pur du Christ pour nos compagnons de route dans ce voyage de la vie."));
//        compareEndpointAndDb(htmlResponse, fileId);
        //make sure the lang tag gets stripped
        Assert.assertTrue(htmlResponse.contains("lang=\"\""));
    }

    @Test(groups = { "endpoints" })
    public static void getDocumentByDocIdGCPostEventFalseUnlocked() throws SQLException, IOException {
        String docId = "235878";
        String fileId = "PD50021411_140_0040";
        String htmlResponse = getTranslationDocumentId(docId, fileId,false, "unlock");
        System.out.println(htmlResponse);

        Assert.assertTrue(htmlResponse.contains("/general-conference/2010/10/charity-never-faileth"));
        Assert.assertTrue(htmlResponse.contains("Au lieu de nous juger et de nous critiquer mutuellement, puissions-nous avoir l’amour pur du Christ pour nos compagnons de route dans ce voyage de la vie."));
        Assert.assertTrue(htmlResponse.contains("lang=\"\""));
    }

    //702807
    @Test(groups = { "endpoints" })
    public static void getDocumentByDocumentIDBroadcastPostEventTrueLocked() throws SQLException, IOException {
        String docId = "702807";
        String fileId = "66666_000_000";
        String htmlResponse = getTranslationDocumentId(docId, fileId,true, "lock");
        System.out.println(htmlResponse);

        Assert.assertTrue(htmlResponse.contains("/broadcasts/event-central-testing/2016/08/_manifest"));
        Assert.assertTrue(htmlResponse.contains("<lds:title type=\"file\">66666_000_000</lds:title>"));
        Assert.assertTrue(htmlResponse.contains("lang=\"\""));

    }

    @Test(groups = { "endpoints" })
    public static void getDocumentByDocumentIDBroadcastPostEventTrueUnLocked() throws SQLException, IOException {
        String docId = "702807";
        String fileId = "66666_000_000";
        String htmlResponse = getTranslationDocumentId(docId, fileId,true, "unlock");
        System.out.println(htmlResponse);

        Assert.assertTrue(htmlResponse.contains("Unable to retrieve file.  Document is not source locked in Content Central.  documentId: 702807"));
    }

    @Test(groups = { "endpoints" })
    public static void getDocumentByDocIdBroadcastPostEventFalseLocked() throws SQLException ,IOException {
        String docId = "702807";
        String fileId = "66666_000_000";
        String htmlResponse = getTranslationDocumentId(docId, fileId,false, "lock");
        System.out.println(htmlResponse);

        Assert.assertTrue(htmlResponse.contains("/broadcasts/event-central-testing/2016/08/_manifest"));
        Assert.assertTrue(htmlResponse.contains("<lds:title type=\"file\">66666_000_000</lds:title>"));
//        compareEndpointAndDb(htmlResponse, fileId);
        //make sure the lang tag gets stripped
        Assert.assertTrue(htmlResponse.contains("lang=\"\""));
    }

    @Test(groups = { "endpoints" })
    public static void getDocumentByDocIdBroadcastPostEventFalseUnlocked() throws SQLException, IOException {
        String docId = "702807";
        String fileId = "66666_000_000";
        String htmlResponse = getTranslationDocumentId(docId, fileId,false, "unlock");
        System.out.println(htmlResponse);

        Assert.assertTrue(htmlResponse.contains("/broadcasts/event-central-testing/2016/08/_manifest"));
        Assert.assertTrue(htmlResponse.contains("<lds:title type=\"file\">66666_000_000</lds:title>"));
        Assert.assertTrue(htmlResponse.contains("lang=\"\""));
    }


    @Test(groups = { "endpoints" })
    public static void getDocByDocIDMagPostEventTrueLocked() throws SQLException, IOException{
        String docId = "1096094";
        String fileId = "12345_000_00011";
        String htmlResponse = getTranslationDocumentId(docId, fileId,true, "lock");
        System.out.println(htmlResponse);
        // <lds:title type="file">12345_000_00011</lds:title>
        Assert.assertTrue(htmlResponse.contains("<lds:title type=\"file\">12345_000_00011</lds:title>"));
    }

    @Test(groups = { "endpoints" })
    public static void getDocByDocIDMagPostEventTrueUnLocked() throws SQLException, IOException{
        String docId = "1096094";
        String fileId = "12345_000_00011";
        String htmlResponse = getTranslationDocumentId(docId, fileId,true, "unlock");
        System.out.println(htmlResponse);
        // <lds:title type="file">12345_000_00011</lds:title>
        Assert.assertTrue(htmlResponse.contains("Unable to retrieve file.  Document is not source locked in Content Central.  documentId: 1096094"));
    }

    @Test(groups = { "endpoints" })
    public static void getDocByDocIDMagPostEventFalseLocked() throws SQLException, IOException{
        String docId = "1096094";
        String fileId = "12345_000_00011";
        String htmlResponse = getTranslationDocumentId(docId, fileId,false, "lock");
        System.out.println(htmlResponse);
        // <lds:title type="file">12345_000_00011</lds:title>
        Assert.assertTrue(htmlResponse.contains("<lds:title type=\"file\">12345_000_00011</lds:title>"));
    }

    @Test(groups = { "endpoints" })
    public static void getDocByDocIDMagPostEventFalseUnLocked() throws SQLException, IOException{
        String docId = "1096094";
        String fileId = "12345_000_00011";
        String htmlResponse = getTranslationDocumentId(docId, fileId,false, "unlock");
        System.out.println(htmlResponse);
        // <lds:title type="file">12345_000_00011</lds:title>
        Assert.assertTrue(htmlResponse.contains("Unable to retrieve file.  Document is not source locked in Content Central.  documentId: 1096094"));
    }

    @Test(groups = { "endpoints" })
    public static void getDocByDocIDManualPostEventTrueLocked() throws SQLException, IOException{
        String docId = "1088660";
        String fileId = "34969_000_003";
        String htmlResponse = getTranslationDocumentId(docId, fileId,true, "lock");
        System.out.println(htmlResponse);
        // <lds:title type="file">12345_000_00011</lds:title>
        Assert.assertTrue(htmlResponse.contains("/manual/primary-1/adapting-the-manual"));
    }

    @Test(groups = { "endpoints" })
    public static void getDocByDocIDManualPostEventTrueUnLocked() throws SQLException, IOException{
        String docId = "1088660";
        String fileId = "34969_000_003";
        String htmlResponse = getTranslationDocumentId(docId, fileId,true, "unlock");
        System.out.println(htmlResponse);
        // <lds:title type="file">12345_000_00011</lds:title>
        Assert.assertTrue(htmlResponse.contains("Unable to retrieve file.  Document is not source locked in Content Central.  documentId: 1088660"));
    }

    @Test(groups = { "endpoints" })
    public static void getDocByDocIDManualPostEventFalseLocked() throws SQLException, IOException{
        String docId = "1088660";
        String fileId = "34969_000_003";
        String htmlResponse = getTranslationDocumentId(docId, fileId,false, "lock");
        System.out.println(htmlResponse);
        // <lds:title type="file">12345_000_00011</lds:title>
        Assert.assertTrue(htmlResponse.contains("/manual/primary-1/adapting-the-manual"));
    }

    @Test(groups = { "endpoints" })
    public static void getDocByDocIDManualPostEventFalseUnLocked() throws SQLException, IOException{
        String docId = "1088660";
        String fileId = "34969_000_003";
        String htmlResponse = getTranslationDocumentId(docId, fileId,false, "unlock");
        System.out.println(htmlResponse);
        // <lds:title type="file">12345_000_00011</lds:title>
        Assert.assertTrue(htmlResponse.contains("Unable to retrieve file.  Document is not source locked in Content Central.  documentId: 1088660"));
    }



    @Test(groups = { "endpoints" })
    public static void invalidDocId() throws IOException, SQLException {
        String docId = "23587asdf8870";
        //No file could be found for documentId
        String htmlResponse = getTranslationDocumentId(docId, "", true, "");
        System.out.println(htmlResponse);
        //Assert.assertTrue(htmlResponse.contains("No file could be found for documentId " + docId));
        Assert.assertTrue(htmlResponse.contains("You have received an error while accessing Content Central."));

    }

    @Test(groups = { "endpoints" })
    public static void missingDocId() throws IOException, SQLException {
        String docId = "";
        //No file could be found for documentId
        String htmlResponse = getTranslationDocumentId(docId, "", true, "");
        System.out.println(htmlResponse);
        Assert.assertTrue(htmlResponse.contains("Invalid documentId specified: null"));

    }




    public static String getTranslationDocumentId (String documentId, String fileId, boolean isPostEvent, String lockOrUnlock) throws IOException, SQLException {
        if (lockOrUnlock == "lock"){
            QADocumentService.lockByFileId(fileId);
        } else if (lockOrUnlock == "unlock"){
            QADocumentService.unlockByFileId(fileId);
        } else {
            System.out.println(lockOrUnlock);
        }
        String endpointURL = Constants.epTranslationDocumentDocId + documentId + "&isPostEvent=" + isPostEvent;
        System.out.println(endpointURL);
        String htmlResponse = NetUtils.getHTML(endpointURL);
        return htmlResponse;
    }


    public static String getTranslationUriAndLang (String languageId, String URI) throws IOException {

        String endpointURL = Constants.epTranslationDocument + URI + "&languageId=" + languageId;
        System.out.println(endpointURL);
        String htmlResponse = NetUtils.getHTML(endpointURL);
        return htmlResponse;
    }

    public static String getTranslationUriAndLangMissingParams (boolean languageIdB, boolean URIB, String param) throws IOException {

        String url = "";
        if (languageIdB && !URIB) {
            url = Constants.epTranslationDocument + "languageId=" + param;
        } else if (!languageIdB && URIB) {
            url = Constants.epTranslationDocument + param;
        } else if (!languageIdB && !URIB) {
            url = Constants.epTranslationDocument + param + "&languagId=" + param;
        }

        System.out.println("URL: " + url);
        String htmlResponse = NetUtils.getHTML(url);
        return htmlResponse;
    }


    public static void compareEndpointAndDb(String endpointResponse, String file_id) throws SQLException {
        ReturnObj returnObj = new ReturnObj();
        String sql = "select document_xml from document where file_id = (?)";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, false);
        String dbValue = "";

        while (returnObj.resultSet.next()) {
        XMLType xml = (XMLType) returnObj.resultSet.getObject(1);
            dbValue = xml.getStringVal();
            StringBuilder sb = new StringBuilder(dbValue);
            sb.insert(0, "<!DOCTYPE html>");
            dbValue = sb.toString();
            xml.close();
        }
        Assert.assertEquals(dbValue, endpointResponse);
        if (endpointResponse.equals(dbValue)) {
            System.out.println("Db and endpoint results are the same");
        }
    }


}