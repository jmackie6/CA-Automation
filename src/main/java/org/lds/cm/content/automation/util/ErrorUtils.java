package org.lds.cm.content.automation.util;

import okhttp3.Protocol;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.enums.ContentType;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
import org.lds.cm.content.automation.enums.TransformationContentTypeID;
import org.lds.cm.content.automation.model.QATransform;
import org.lds.cm.content.automation.model.QAValidationError;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.Constants.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.text.html.parser.Parser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lds.cm.content.automation.util.Constants.Constants.epTransform;

//Anything to do with Error writing, constants etc.
public class ErrorUtils
{
    private static final boolean validateDB = false;

    public static String stringify(ArrayList<String> errors)
    {
        StringBuilder sb = new StringBuilder("Error Report -- \n");
        for(String x: errors)
            sb.append(x + "\n");
        return sb.toString();
    }

    public static void testFilesTransformToFail(final String filePath) throws IOException, ParseException{
        System.out.println("********** Starting Transform **********");
        final JSONObject returnJSON =  QATransformService.transform(new File(filePath));
        final QATransformationResult transformResult = QATransformationResult.fromJSON(returnJSON);
        System.out.println(returnJSON);
        Assert.assertTrue(!transformResult.wasSuccessful());


    }


    private static JSONObject processRestRequest(String urlRequest, String requestMethod, Map<String, String> requestParameter) throws IOException, ParseException{
        final URL urlConn = new URL(urlRequest);
        final HttpURLConnection conn = (HttpURLConnection) urlConn.openConnection();
        final JSONParser parser = new JSONParser();
        try{
            conn.setRequestMethod(requestMethod);
            if(conn.getResponseCode() != 200){
                throw new RuntimeException("Request failed :" + conn.getResponseCode());
            }

            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer sb = new StringBuffer();

            String output = "";
            while((output = br.readLine()) != null){
                sb.append(output);
            }

            final JSONObject returnObj = (JSONObject)parser.parse(sb.toString());
            br.close();
            return returnObj;


        }catch(Exception e){
            if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || conn.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER) {
                return processRedirect(conn.getHeaderField("Location").toString(), requestMethod, requestParameter);
            }
        }finally{
            conn.disconnect();
        }

        return null;
    }

    private static JSONObject processRedirect(String location, String requestMethod, Map<String, String> requestParameter) throws  IOException, ParseException {

        final URL requestConn = new URL(location);
        final HttpURLConnection conn = (HttpURLConnection) requestConn.openConnection();
        final JSONParser parser = new JSONParser();

        conn.setRequestMethod(requestMethod);

        for (Map.Entry<String, String> param : requestParameter.entrySet()) {
            conn.setRequestProperty(param.getKey(), param.getValue());
        }

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed REST service: " + conn.getResponseCode());
        }


        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuilder sb = new StringBuilder();
        String output = "";

        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        conn.disconnect();
        br.close();
        return (JSONObject) parser.parse(sb.toString());



    }


    public static void testFilesWithMaps(final String filePath, final Map<String, Integer> expectedErrors) throws IOException, ParseException, SQLException, XPathExpressionException, ParserConfigurationException, SAXException{
        final JSONObject jObj = QATransformService.transform(new File(filePath));
        final QATransformationResult returnResult = QATransformationResult.fromJSONToMap(jObj);

        if(!returnResult.wasSuccessful()){
            System.out.println(jObj.toJSONString());
        }

        returnResult.printErrors();
        Assert.assertTrue(returnResult.wasSuccessful());
        Assert.assertTrue(compareErrorObjects(expectedErrors, returnResult.getErrorMapping()));
        if(validateDB) {
            Assert.assertTrue(compareDatabaseErrorsWithMapping(filePath, returnResult.getErrorMapping()));
        }

    }



    public static void validateFilesWithMaps(final String filePath, final Map<String, Integer> expectedErrors) throws IOException, SAXException, SQLException, ParserConfigurationException, XPathExpressionException {
        final File currentFile = new File(filePath);
        final String validationResult = QATransformService.validate(currentFile);
        final QATransformationResult transformationResult = QATransformationResult.fromXMLFilteredWithMaps(validationResult);
        final Map<String, Integer> actualErrors = transformationResult.getErrorMapping();

        if(!transformationResult.wasSuccessful()){
            transformationResult.printErrors();
        }

        Assert.assertTrue(compareErrorObjects(expectedErrors, actualErrors));
        if(validateDB) {
            Assert.assertTrue(compareDatabaseErrorsWithMapping(filePath, expectedErrors));
        }
    }



    private static boolean confirmXMLErrors(final QAValidationError currentError, final ErrorTypes errorType, final SeverityTypes severityType, final int numberOfErrorsExpected){
        final ErrorTypes currentErrorType = ErrorTypes.valueOf(currentError.getValidationType());
        final SeverityTypes currentSeverity = SeverityTypes.valueOf(currentError.getValidationSeverity());

        if (currentErrorType != errorType){
            return false;
        }else if(currentSeverity != severityType){
            return false;
        }

        return true;

    }

    private static boolean confirmJSONErrors(final QAValidationError currentError, final ErrorTypes errorType, final SeverityTypes severityType, final int numberOfErrorsExpected){
        final ErrorTypes currentErrorType = ErrorTypes.valueOf(currentError.getValidationType());
        final SeverityTypes currentSeverity = SeverityTypes.valueOf(currentError.getValidationSeverity());

        if (currentErrorType != errorType){
            return false;
        }else if(currentSeverity != severityType){
            return false;
        }

        return true;

    }



    private static boolean compareDatabaseErrorsWithMapping(final String filePath, final Map<String, Integer> expectedErrors) throws SQLException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        String sqlQuery = "SELECT ve.SEVERITY_ID,  ve.VALIDATION_TYPE_ID, COUNT(1) as errorCount FROM DOCUMENT d INNER JOIN VALIDATION_ERROR ve on d.DOCUMENT_ID = ve.DOCUMENT_ID" +
                " WHERE d.FILE_ID LIKE ? GROUP BY ve.SEVERITY_ID, ve.VALIDATION_TYPE_ID";
        final Document currentDoc = XMLUtils.getDocumentFromFile(new File(filePath));
        final String fileID = XPathUtils.getStringValue("//lds:title[@type='file']", currentDoc, true);
        final ArrayList<String> vars = new ArrayList<>();
        String newFileName = "%" + fileID + "%";
        vars.add(newFileName);
        ResultSet rs = JDBCUtils.getResultSet(sqlQuery, vars);
        int index = 0;

        while(rs.next()){
            SeverityTypes currentSeverity = SeverityTypes.getTypeFromInt(rs.getInt("SEVERITY_ID"));
            ErrorTypes currentErrorType = ErrorTypes.getTypeFromInt(rs.getInt("VALIDATION_TYPE_ID"));
            int actualErrorNumber = rs.getInt("ERRORCOUNT");
//            System.out.println("Here we are.");
            String tempKey = String.join("-", currentErrorType.name(), currentSeverity.name() );
            boolean containedKey = expectedErrors.containsKey(tempKey);
            int numberOfErrors = expectedErrors.entrySet().stream().filter(e -> e.getKey().equals(tempKey)).map(Map.Entry::getValue).findFirst().orElse(0);
            Assert.assertTrue(containedKey);
            Assert.assertEquals(numberOfErrors, actualErrorNumber);
            index++;
        }

        if(expectedErrors.size() > 1 && index == 0){
            System.out.println("********************************");
            System.out.println("----------No database records found!--------------");
            return false;
        }


        return true;

    }

    private static String getFileName(String filepath){
        String[] filepieces = filepath.split("/");
        String[] filenamepieces = filepieces[filepieces.length - 1].split("\\.");
        return filenamepieces[0];
    }

    private static boolean compareErrorObjects(Map<String, Integer> expectedErrors, Map<String, Integer> actualErrors){
        int index = 0;
        for(Map.Entry<String, Integer> expectedError : expectedErrors.entrySet()){
            if(actualErrors.containsKey(expectedError.getKey()) && actualErrors.get(expectedError.getKey()).equals(expectedError.getValue())){
                index++;
            }
        }

        if(index == actualErrors.size()){
            return true;
        }

        return false;

    }




}
