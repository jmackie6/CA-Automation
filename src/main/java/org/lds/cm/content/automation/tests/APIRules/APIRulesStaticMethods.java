package org.lds.cm.content.automation.tests.APIRules;

import static org.apache.http.entity.mime.MultipartEntityBuilder.create;

import opennlp.tools.parser.Cons;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.lds.cm.content.automation.model.APIModels.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class APIRulesStaticMethods
{
    private static HashMap<String, API> map;
    /** Necessary Queries to run the below methods */
    private static final String selection = "select enabled from api_rules where name = ?";
    private static final String update = "update api_rules set Enabled =? where name = ?";

    /** Constant values for enabled and disabled rules */
    private static final String enable = "1";
    private static final String disable = "0";


    public static void setUP() throws SQLException {
        map = new HashMap<>();
        map.put("ws/v1/transform", new Transform());
        map.put("ws/v1/editorial/transform", new EditorialTransform());
        map.put("ws/v1/generateScriptureRefs", new GenerateScriptRefs());
        map.put("ws/v1/getAuthorData", new GetAuthorData());
        map.put("ws/v1/getDocument", new GetDocument());
        map.put("ws/v1/getDocumentByDamIdAndVersion", new GetDocByDAMId());
        map.put("ws/v1/getImage", new GetImage());
        map.put("ws/v1/getLanguageXml", new GetLanguage());
        map.put("ws/v1/getPreviewCss", new GetPreviewCss());
        map.put("ws/v1/getAllCss", new GetAllCss());
        map.put("ws/v1/getAllMatchingFileIDs", new GetAllMathcingFileIDs());
        map.put("ws/v1/contentGroups", new ContentGroups());
        map.put("ws/v1/getPrintCss", new GetPrintCss());
        map.put("ws/v1/processCount", new ProcessCount());
        map.put("ws/v1/getAudio", new GetAudio());
        map.put("ws/v1/getVideoUrl", new GetVideoUrl());
        map.put("ws/v1/documentContentTypes", new DocumentContentTypes());
        map.put("ws/v1/previewFile", new PreviewFile());
        map.put("ws/v1/contentTypes", new ContentTypes());
        map.put("ws/v1/previewFileByUri", new PreviewFileByUri());
        map.put("ws/v1/getPreviewJavascript", new GetPreviewJavascript());
        map.put("ws/v1/getPrintJavascript", new GetPrintJavascript());
        map.put("ws/v1/previewFileByUriAndLanguage", new PreviewFileByUriAndLanguage());
        map.put("ws/v1/publishBroadcast", new PublishBroadcast());
        map.put("ws/v1/sandbox/previewFile", new SandboxPreviewFile());
        map.put("ws/v1/sandbox/transform", new SandboxTransform());
        map.put("ws/v1/getMediaIdsByFileId", new GetMediaIdsByFileId());
        map.put("ws/v1/getImageOxygen", new GetImageOxygen());
        map.put("ws/v1/validateHtml5", new ValidateHTML5());
        map.put("ws/v1/updateDocumentMedia", new UpdateDocumentMedia());
        map.put("ws/v1/generateAuthorUri", new GenerateAuthorUri());
        map.put("ws/v1/generateBroadcastFileId", new GenerateBroadcastFileId());
        map.put("ws/v1/generateBroadcastUri", new GenerateBroadcastUri());
        map.put("ws/v1/getDocumentByDamIdAndVersion", new GetDocumentByDamIdAndVersion());
        map.put("ws/v1/deleteDocument", new DeleteDocument());
    }
    /** Method used by other classes to force the specified api rule to allow endpoint to work*/
    public static void fixAPIRule(String rule) throws SQLException, InterruptedException
    {
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(enable);
        fillIn.add(rule);
        JDBCUtils.executeUpdate(update, fillIn);
    }


    // **************** Static Methods used for any POST calls *************************

    public static String uploadDocumentMedia200(String apiRuleName, String apiQuery, JSONObject json) throws IOException, SQLException, InterruptedException {
        String url;
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(apiQuery);
        ResultSet rs = JDBCUtils.getResultSet(selection, fillIn);

        String enabled = rs.toString();
        if (enabled.compareTo(disable) != 0){
            fillIn.clear();
            fillIn.add(disable);
            fillIn.add(apiQuery);
            JDBCUtils.executeUpdate(update, fillIn);
        }
        rs.close();


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(Constants.epUpdateDocumentMedia);

        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", " text/html, application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);
        System.out.println(response);


        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Status code: " + statusCode);
        if (statusCode != 200){
            return statusCode + " was returned for " + Constants.epUpdateDocumentMedia;
        }

        String body = EntityUtils.toString(response.getEntity());
        System.out.println(body);
        client.close();
        response.close();

        return "";
    }

    public static String fourOThreeError(String apiRuleName, String apiQuery) throws IOException, SQLException, InterruptedException {
        String url;
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(apiQuery);
        ResultSet rs = JDBCUtils.getResultSet(selection, fillIn);

        String enabled = rs.toString();
        if (enabled.compareTo(disable) != 0){
            fillIn.clear();
            fillIn.add(disable);
            fillIn.add(apiQuery);
            JDBCUtils.executeUpdate(update, fillIn);
        }
        rs.close();


        url = Constants.baseURL + "/" + apiRuleName + map.get(apiRuleName).getUri();

        // Make a post request  and check the status code and make sure you get a 200
        CloseableHttpResponse closeableHttpResponse = NetUtils.getPost(url);

        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code: " + statusCode);
        if (statusCode != 403){
            return statusCode + " was returned for " + url;
        }

        String body = EntityUtils.toString(closeableHttpResponse.getEntity());
        System.out.println(body);
//        if(body.contains(errorMessage)){
//            return apiRuleName + " returned " + errorMessage;
//        }
        closeableHttpResponse.close();

        return "";
    }

    public static String twoHundredStatus(String apiRuleName, String apiQuery) throws IOException, SQLException, InterruptedException {
        String url;
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(apiQuery);
        ResultSet rs = JDBCUtils.getResultSet(selection, fillIn);

        String enabled = rs.toString();
        if (enabled.compareTo(enable) != 0){
            fillIn.clear();
            fillIn.add(enable);
            fillIn.add(apiQuery);
            JDBCUtils.executeUpdate(update, fillIn);
        }
        rs.close();


        url = Constants.baseURL + "/" + apiRuleName + map.get(apiRuleName).getUri();

        // Make a post request  and check the status code and make sure you get a 200
        CloseableHttpResponse closeableHttpResponse = NetUtils.getPost(url);
        String checkForWebDown = EntityUtils.toString(closeableHttpResponse.getEntity());


        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code: " + statusCode);
        closeableHttpResponse.close();
        if (statusCode != 200) {
            return statusCode + " was returned for " + url;
        } else if (statusCode == 200 && checkForWebDown.contains("This Web site is down for maintenance")){
            return "The website is down";
        } else {
            return "";
        }


    }

    public static String apiFileUploadStatus(String apiQuery, File file, String constant) throws SQLException, IOException, InterruptedException {
        String result;
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(apiQuery);
        ResultSet rs = JDBCUtils.getResultSet(selection, fillIn);

        String enabled = rs.toString();
        if (enabled.compareTo(enable) != 0) {
            fillIn.clear();
            fillIn.add(enable);
            fillIn.add(apiQuery);
            JDBCUtils.executeUpdate(update, fillIn);
        }
        rs.close();

//        JSONObject jsonResponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(constant);

        HttpEntity entity = create()
                .addBinaryBody("file", file)
                .build();
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        int code = response.getStatusLine().getStatusCode();
        System.out.println("Status code: " + code);
        if (code == 200)
        {
            result = "";
        }
        else {
            result = "The transform did not work: " + code;
            System.out.println(result);
        }

        response.close();
        return result;
    }

    public static String apiFileUploadStatus403(String apiQuery, File file, String constant) throws SQLException, IOException, InterruptedException {
        String result;
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(apiQuery);
        ResultSet rs = JDBCUtils.getResultSet(selection, fillIn);

        String enabled = rs.toString();
        if (enabled.compareTo(disable) != 0) {
            fillIn.clear();
            fillIn.add(disable);
            fillIn.add(apiQuery);
            JDBCUtils.executeUpdate(update, fillIn);
        }
        rs.close();

//        JSONObject jsonResponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(constant);

        HttpEntity entity = create()
                .addBinaryBody("file", file)
                .build();
        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        int code = response.getStatusLine().getStatusCode();
        System.out.println("Status code: " + code);
        if (code == 403)
        {
            result = "";
        }
        else {
            result = "The transform either worked and we got a 200, or we got another error code. Error Code: " + code;
            System.out.println(result);
        }

        response.close();

        return result;
    }

    //******************************************************************************************

    //*********************** Static Methods used for any GET calls **********************************

    public static String fourOThreeErrorGet(String apiRuleName, String apiQuery) throws IOException, SQLException, InterruptedException {
        String url;
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(apiQuery);
        ResultSet rs = JDBCUtils.getResultSet(selection, fillIn);

        String enabled = rs.toString();
        if (enabled.compareTo(disable) != 0){
            fillIn.clear();
            fillIn.add(disable);
            fillIn.add(apiQuery);
            JDBCUtils.executeUpdate(update, fillIn);
        }
        rs.close();
        url = Constants.baseURL + "/" + apiRuleName + map.get(apiRuleName).getUri();

        // Make a post request  and check the status code and make sure you get a 403
        CloseableHttpResponse closeableHttpResponse = NetUtils.getFullHTMLResponse(url);

        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code: " + statusCode);
        if (statusCode != 403){
            return statusCode + " was returned for " + url;
        }

        String body = EntityUtils.toString(closeableHttpResponse.getEntity());
        System.out.println(body);
//        if(!body.contains(errorMessage)){
//            return apiRuleName + " returned " + errorMessage;
//        }

        closeableHttpResponse.close();

        return "";

    }

    public static String twoHundredStatusGet(String apiRuleName, String apiQuery) throws IOException, SQLException, InterruptedException {
        String url;
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(apiQuery);
        ResultSet rs = JDBCUtils.getResultSet(selection, fillIn);

        String enabled = rs.toString();
        if (enabled.compareTo(enable) != 0){
            fillIn.clear();
            fillIn.add(enable);
            fillIn.add(apiQuery);
            JDBCUtils.executeUpdate(update, fillIn);
        }
        rs.close();


        url = Constants.baseURL + "/" + apiRuleName + map.get(apiRuleName).getUri();

        // Make a post request  and check the status code and make sure you get a 200
        CloseableHttpResponse closeableHttpResponse = NetUtils.getFullHTMLResponse(url);
        String checkForWebDown = EntityUtils.toString(closeableHttpResponse.getEntity());

        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        System.out.println("Status code: " + statusCode);
        if (statusCode != 200){
            return statusCode + " was returned for " + url;
        } else if (statusCode == 200 && checkForWebDown.contains("This Web site is down for maintenance")){
            return "The website is down";
        }

        if(checkForWebDown != null) {
            System.out.println(checkForWebDown);
        }
//        if(body.contains(errorMessage)){
//            return apiRuleName + " returned " + errorMessage;
//        }
        closeableHttpResponse.close();

        return "";

    }
    //******************************************************************************************

}