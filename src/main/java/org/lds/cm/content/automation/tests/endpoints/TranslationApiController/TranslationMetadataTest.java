package org.lds.cm.content.automation.tests.endpoints.TranslationApiController;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TranslationMetadataTest {

   // /ws/v1/translation/document/metadata	GET		String pdNumber, String languageCode, Boolean conferenceFlag
     /** Conference 2017 data: pdNumber: PD60003501 - If this is ever deleted, make sure you upload documents and
      *  change the PD number that matches this one to the new PD number uploaded
      *  **/

    @Test(groups= { "endpoints" })
    public static void lockedDocumentTest() throws ParseException, IOException, SQLException {
        String check = makeEndpointCall("PD60003501", "000", true, true);
        Assert.isTrue(check.compareTo("Success")==0,check);
        boolean dbCheck = compareResultToDB("PD60003501", "000", true);
        Assert.isTrue(dbCheck, "The number of results from the database and from the endpoint call are different.");
    }

    @Test(groups= { "endpoints" })
    public static void emptyPDNumber() throws ParseException, IOException {
        String check = noParameterValueCall("", "000", "true" );
        Assert.isTrue(check.compareTo("Success")==0, check);
    }

    @Test(groups= { "endpoints" })
    public static void emptyLangCode() throws ParseException, IOException {
        String check = noParameterValueCall("PD60003501", "", "true" );
        Assert.isTrue(check.compareTo("Success")==0, check);

    }

    @Test(groups= { "endpoints" })
    public static void emptyConferenceFlag() throws ParseException, IOException {
        String check = noParameterValueCall("PD60003501", "000", "" );
        Assert.isTrue(check.compareTo("Success")==0, check);
    }

    @Test(groups= { "endpoints" })
    public static void noPDNumber() throws ParseException, IOException {
        String check = makeEndpointCallMissingParameters(false, true, true, "", "000", "true");
        Assert.isTrue(check.compareTo("Success")==0, check);
    }

    @Test(groups= { "endpoints" })
    public static void noLangCode() throws ParseException, IOException {
        String check = makeEndpointCallMissingParameters(true, false, true, "PD60003501", "", "true");
        Assert.isTrue(check.compareTo("Success")==0, check);
    }

    @Test(groups= { "endpoints" })
    public static void noConferenceFlag() throws ParseException, IOException {
        String check = makeEndpointCallMissingParameters(true, true, false, "PD60003501", "000", "");
        Assert.isTrue(check.compareTo("Success")==0, check);
    }

    @Test(groups= { "endpoints" })
    public static void invalidPDNumber() throws ParseException, IOException {
        String check = invalidValueCall("PD60003501213235344", "000", "true" );
        Assert.isTrue(check.compareTo("Success")==0, check);
    }

    @Test(groups= { "endpoints" })
    public static void invalidLangCode() throws ParseException, IOException {
        String check = invalidValueCall("PD60003501", "1005", "true" );
        Assert.isTrue(check.compareTo("Success")==0, check);
    }

    @Test(groups= { "endpoints" })
    public static void conferenceFlagTrue() throws ParseException, IOException, SQLException {
        String check = makeEndpointCall("PD50028768", "002", true, true);
        Assert.isTrue(check.compareTo("Success")==0,check);
        boolean dbCheck = compareResultToDB("PD50028768", "002", true);
        Assert.isTrue(dbCheck, "The number of results from the database and from the endpoint call are different.");
    }

    @Test(groups= { "endpoints" })
    public static void conferenceFlagFalse() throws ParseException, IOException, SQLException {
        String check = makeEndpointCall("PD50028768", "002", false, true);
        Assert.isTrue(check.compareTo("Success")==0,check);
        boolean dbCheck = compareResultToDB("PD50028768", "002", true);
        Assert.isTrue(dbCheck, "The number of results from the database and from the endpoint call are different.");
    }

    @Test(groups= { "endpoints" })
    public static void fatalValidationErrorsButNotLocked() throws ParseException, IOException, SQLException {
        File file = new File(Constants.transformFileStartDir + "/engTestGenConf/church-auditing-department-report-2016.html");
        QATransformService.transform(file);
        QADocumentService.unlockByFileId("PD29384851");
        String check = makeEndpointCall("PD29384851", "000", true, false);
        Assert.isTrue(check.compareTo("Success")==0, check);

    }

    @Test(groups= { "endpoints" })
    public static void lockedButHasFatalValidationErrors() throws ParseException, IOException, SQLException {
        QADocumentService.lockByFileId("PD29384851");
        String check = makeEndpointCall("PD29384851", "000", true, false);
        Assert.isTrue(check.compareTo("Success")==0, check);

    }

    @Test(groups= { "endpoints" })
    public static void addedStatusCheck() throws ParseException, IOException, SQLException {
        File file = new File(Constants.transformFileStartDir + "/engTestGenConf/church-auditing-test.html");
        QATransformService.transform(file);
        // Change document validated status to 0
        QADocumentService.unvalidatedByFileId("PD54326789");
        // Change document_state_id to added
        QADocumentService.addedByFileId("PD54326789");
        // Delete any FATAL validation errors
        QADocumentService.deleteFatalValidateErrorsByFileId("PD54326789");
        String check = makeEndpointCall("PD54326789", "000", true, false);
        Assert.isTrue(check.compareTo("Success")==0, check);


    }

    public static String makeEndpointCall(String pdNumber, String langCode, Boolean conferenceFlag, boolean successTest) throws IOException, ParseException {
        String result = "";
        String containsMessage = "";
        // Create the url for the call
        String url = Constants.epTranslationMetadata + "languageCode=" + langCode + "&" + "pdNumber=" + pdNumber + "&" + "conferenceFlag=" + conferenceFlag;
        System.out.println(url);
//Invalid pdNumber was given or the document\/documents are not locked and or have fatal validation errors
        // Make the call
        CloseableHttpResponse response = NetUtils.getFullHTMLResponse(url);
        JSONArray jsonArray = NetUtils.getJsonArray(url);
        if (successTest){
            containsMessage = "Success";
        } else {
            containsMessage = "Invalid pdNumber was given or the document\\/documents are not locked and or have fatal validation errors";
        }
        if (jsonArray.toString().contains(containsMessage)) {
            result = "Success";
        }
        System.out.println(jsonArray);
        response.close();
        return result;
    }

    public static String makeEndpointCallMissingParameters(boolean pdNumberB, boolean langCodeB, boolean conferenceFlagB , String pdNumber, String langCode, String conferenceFlag) throws IOException, ParseException {
        String result;
        String containsMessage = "";
        String lang = "languageCode=";
        if (!langCodeB){
            lang = "";
            containsMessage = "Language Code is missing null";
        }

        String pd = "&pdNumber=";
        if (!pdNumberB){
            pd = "";
            containsMessage = "pdNumber is missing null";
        }

        String conFlag = "&conferenceFlag=";
        if (!conferenceFlagB) {
            conFlag = "";
            containsMessage = "Success";
        }

        String urlBuilder = lang + langCode + pd + pdNumber + conFlag + conferenceFlag;



        String url = Constants.epTranslationMetadata + urlBuilder;
        System.out.println(url);
        System.out.println(containsMessage);

        // Make the call
        CloseableHttpResponse response = NetUtils.getFullHTMLResponse(url);
        JSONArray jsonArray = NetUtils.getJsonArray(url);
        if (jsonArray.toString().contains(containsMessage)){
            result = "Success";
        } else {
            result = "A different message appeared.";
        }
        System.out.println(jsonArray);
        response.close();
        return result;


    }

    public static String noParameterValueCall(String pdNumber, String langCode, String conferenceFlag) throws IOException, ParseException {
        String result;
        String containsMessage;

        // Conditionals for noParameter Value
        if (pdNumber.isEmpty()){
            containsMessage = "Invalid pdNumber was given or the document\\/documents are not locked and or have fatal validation errors";
        } else if (langCode.isEmpty()){
            containsMessage = "Language Code is invalid : null";
        } else {
            containsMessage = "Success";
        }


        String url = Constants.epTranslationMetadata + "languageCode=" + langCode + "&pdNumber=" + pdNumber + "&conferenceFlag=" + conferenceFlag;
        System.out.println(url);
        System.out.println(containsMessage);

        // Make the call
        CloseableHttpResponse response = NetUtils.getFullHTMLResponse(url);
        JSONArray jsonArray = NetUtils.getJsonArray(url);
        if (jsonArray.toString().contains(containsMessage)){
            result = "Success";
        } else {
            result = "A different message appeared.";
        }
        System.out.println(jsonArray);
        response.close();
        return result;


    }

    public static String invalidValueCall(String pdNumber, String langCode, String conferenceFlag) throws IOException, ParseException {
        String result;
        String containsMessage;

        // Conditionals for noParameter Value
        if (pdNumber != "PD60003501"){
            containsMessage = "Invalid pdNumber was given or the document\\/documents are not locked and or have fatal validation errors";
        } else if (langCode != "000"){
            containsMessage = "Language Code is invalid";
        } else {
            containsMessage = "Success";
        }


        String url = Constants.epTranslationMetadata + "languageCode=" + langCode + "&pdNumber=" + pdNumber + "&conferenceFlag=" + conferenceFlag;
        System.out.println(url);
        System.out.println(containsMessage);

        // Make the call
        CloseableHttpResponse response = NetUtils.getFullHTMLResponse(url);
        JSONArray jsonArray = NetUtils.getJsonArray(url);
        if (jsonArray.toString().contains(containsMessage)){
            result = "Success";
        } else {
            result = "A different message appeared.";
        }
        System.out.println(jsonArray);
        response.close();
        return result;


    }

    public static Boolean compareResultToDB (String pdNumber, String langCode, Boolean conferenceFlag) throws IOException, ParseException, SQLException {
        boolean result = false;
        int dbCheck = 0;
        // Create the url for the call
        String url = Constants.epTranslationMetadata + "languageCode=" + langCode + "&" + "pdNumber=" + pdNumber + "&" + "conferenceFlag=" + conferenceFlag;
        System.out.println(url);
        // Make the call
        CloseableHttpResponse response = NetUtils.getFullHTMLResponse(url);
        JSONArray jsonArray = NetUtils.getJsonArray(url);
        int jsonArrayCount = jsonArray.size();

        String query = "SELECT count(*) FROM document d, document_state ds "
                + "WHERE d.document_state_id = ds.document_state_id "
                + "AND ds.document_state_id = '2' AND file_id LIKE '" + pdNumber + "_" + langCode + "%' "
                + "AND file_id NOT IN (SELECT file_id FROM document d2, validation_error e WHERE d2.document_id = e.document_id "
                + "AND e.SEVERITY_ID = '4' AND file_id LIKE '" + pdNumber + "_" + langCode + "%')";

        ResultSet rs = JDBCUtils.getResultSet(query);

        if (rs.next()){
           dbCheck = rs.getInt(1);
        }
        rs.close();

        if(dbCheck == jsonArrayCount){
            result = true;
        }

        response.close();
        return result;

    }

}
