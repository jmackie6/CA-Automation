package org.lds.cm.content.automation.tests.APIRules;

import com.google.gson.*;
import com.mongodb.util.JSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.springframework.util.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class APIRulesDb {
    File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/lead-kindly-light.html");

    @BeforeClass (alwaysRun = true, timeOut = 180000)
    public void setUp() throws SQLException {
        APIRulesStaticMethods.setUP();
    }

    @AfterClass (alwaysRun = true, timeOut = 180000)
    public void cleanUp() throws SQLException, InterruptedException {
        ResultSet apiCleanUp = JDBCUtils.getResultSet("select name from api_rules");
        while (apiCleanUp.next()){
           APIRulesStaticMethods.fixAPIRule(apiCleanUp.getString("name"));
        }
        apiCleanUp.close();
        JDBCUtils.closeUp();
    }


    //*************Testing for POST API rules 403 status***********************
    @Test (timeOut = 180000)
    public void transformTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeError("ws/v1/transform", "ws/v1/transform");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void validateHtml5403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.apiFileUploadStatus403("ws/v1/validateHtml5", testFile, Constants.epValidate);
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void editorialTransformTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeError("ws/v1/editorial/transform", "ws/v1/editorial/transform");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void sandboxTransformTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.apiFileUploadStatus403("ws/v1/sandbox/transform", testFile, Constants.epSandboxTransform);
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void updateDocumentMedia403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeError("ws/v1/updateDocumentMedia", "ws/v1/updateDocumentMedia");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the deleteDocument endpoint returns a 403 when disabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void deleteDocument403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeError("ws/v1/deleteDocument", "ws/v1/deleteDocument");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    //**************************************************************************


    //*************Testing for POST API rules 200 status***********************
    @Test (timeOut = 180000)
    public void transformTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.apiFileUploadStatus("ws/v1/transform", testFile, Constants.epTransform);
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void validateHtml5200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.apiFileUploadStatus("ws/v1/validateHtml5", testFile, Constants.epValidate);
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void editorialTransformTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatus("ws/v1/editorial/transform", "ws/v1/editorial/transform");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void sandboxTransformTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.apiFileUploadStatus("ws/v1/sandbox/transform", testFile, Constants.epSandboxTransform);
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void updateDocumentMedia200() throws IOException, SQLException, InterruptedException {
        JSONObject image = new JSONObject();
        JSONObject pathValue1 = new JSONObject();
        pathValue1.put("type", "video");
        pathValue1.put("url", "http://media.ldscdn.org/images/videos/general-conference/april-2017-general-conference" +
                "/2017-03-0020-carol-f-mcconkie-590x331-ldsorg-article.jpg");
        JSONObject pathValue2 = new JSONObject();
        pathValue2.put("type", "nav");
        pathValue2.put("url", "http://media.ldscdn.org/images/videos/general-conference" +
                "/april-2017-general-conference/2017-03-0020-carol-f-mcconkie-100x83-6x5.jpg");
        JSONObject pathValue3 = new JSONObject();
        pathValue3.put("type", "poster");
        pathValue3.put("url", "http://media.ldscdn.org/images/videos/general-conference/april-2017-general-conference" +
                "/2017-03-0020-carol-f-mcconkie-900x505.jpg");
        org.json.JSONArray imagePathArray = new org.json.JSONArray();
        imagePathArray.put(pathValue1);
        imagePathArray.put(pathValue2);
        imagePathArray.put(pathValue3);
        image.put("path", imagePathArray);


        JSONObject audio = new JSONObject();
        JSONObject audioValue = new JSONObject();
        audioValue.put("type", "mp3");
        audioValue.put("url", "http://media2.ldscdn.org/assets/general-conference/april-2017-general-conference" +
                "/2017-03-0010-bonnie-h-cordon-64k-eng.mp3");
        org.json.JSONArray  audioPathArray = new org.json.JSONArray();
        audioPathArray.put(audioValue);
        audio.put("path", audioPathArray);

        JSONObject video = new JSONObject();
        JSONObject videoValue1 = new JSONObject();
        videoValue1.put("type", "ovp");
        videoValue1.put("format", "streaming");
        videoValue1.put("id", "5372724733001");
        JSONObject videoValue2 = new JSONObject();
        videoValue2.put("type", "mp4");
        videoValue2.put("size", "360p");
        videoValue2.put("url", "http://media2.ldscdn.org/assets/general-conference/april-2017-general-conference" +
                "/2017-03-0010-bonnie-h-cordon-360p-eng.mp4");
        JSONObject videoValue3 = new JSONObject();
        videoValue3.put("type", "mp4");
        videoValue3.put("size", "720p");
        videoValue3.put("url", "http://media2.ldscdn.org/assets/general-conference/april-2017-general-conference" +
                "/2017-03-0010-bonnie-h-cordon-720p-eng.mp4");
        JSONObject videoValue4 = new JSONObject();
        videoValue4.put("type", "mp4");
        videoValue4.put("size", "1080p");
        videoValue4.put("url", "http://media2.ldscdn.org/assets/general-conference/april-2017-general-conference" +
                "/2017-03-0010-bonnie-h-cordon-1080p-eng.mp4");

        org.json.JSONArray videoPathArray = new org.json.JSONArray();
        videoPathArray.put(videoValue1);
        videoPathArray.put(videoValue2);
        videoPathArray.put(videoValue3);
        videoPathArray.put(videoValue4);
        video.put("path", videoPathArray);

        JSONObject mediaObject = new JSONObject();
        mediaObject.put("image", image);
        mediaObject.put("audio", audio);
        mediaObject.put("video", video);


        JSONObject json = new JSONObject();
        json.put("longTitle", "Trust in the Lord and Lean Not");
        json.put("shortTitle", "Trust in the Lord and Lean Not");
        json.put("accountId", "1197194726001");
        json.put("playerId", "1301988543001");
        json.put("fileId", "PD60003501_000_11cordon");
        json.put("speaker", "Bonnie H. Cordon");
        json.put("conference", true);
        json.put("media", mediaObject);

        System.out.println(json);


//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonParser jp = new JsonParser();
//        JsonElement je = jp.parse(json.toString());
//        String prettyJsonString = gson.toJson(je);
//        System.out.println(prettyJsonString);


        String result = APIRulesStaticMethods.uploadDocumentMedia200("ws/v1/updateDocumentMedia", "ws/v1/updateDocumentMedia", json);
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the deleteDocument endpoint returns a 200 when enabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void deleteDocument200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatus("ws/v1/deleteDocument", "ws/v1/deleteDocument");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    //***************************************************************************


    //*************Testing for GET API rules 403 status*************************
    @Test (timeOut = 180000)
    public void uploadDocumentMedia403() throws InterruptedException, SQLException, IOException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/updateDocumentMedia", "ws/v1/updateDocumentMedia");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void generateScriptureRefTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/generateScriptureRefs", "ws/v1/generateScriptureRefs");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getAuthorDataTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getAuthorData", "ws/v1/getAuthorData");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getDocumentTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getDocument", "ws/v1/getDocument");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getDocumentByDamIdAndVersionTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getDocumentByDamIdAndVersion", "ws/v1/getDocumentByDamIdAndVersion");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getImageTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getImage", "ws/v1/getImage");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getLanguageXmlTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getLanguageXml", "ws/v1/getLanguageXml");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getPreviewCssTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getPreviewCss", "ws/v1/getPreviewCss");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getAllCssTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getAllCss", "ws/v1/getAllCss");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getAllMatchingFileIDsTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getAllMatchingFileIDs", "ws/v1/getAllMatchingFileIDs");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void contentGroupsTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/contentGroups", "ws/v1/contentGroups");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getPrintCssTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getPrintCss", "ws/v1/getPrintCss");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void processCountTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/processCount", "ws/v1/processCount");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getAudioTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getAudio", "ws/v1/getAudio");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getVideoUrlTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getVideoUrl", "ws/v1/getVideoUrl");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void documentContentTypesTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/documentContentTypes", "ws/v1/documentContentTypes");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void previewFileTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/previewFile", "ws/v1/previewFile");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void contentTypesTest403() throws IOException, SQLException , InterruptedException{
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/contentTypes", "ws/v1/contentTypes");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void previewFileByUriTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/previewFileByUri", "ws/v1/previewFileByUri");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getPreviewJavascriptTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getPreviewJavascript", "ws/v1/getPreviewJavascript");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getPrintJavascriptTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getPrintJavascript", "ws/v1/getPrintJavascript");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void previewFileByUriAndLanguageTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/previewFileByUriAndLanguage", "ws/v1/previewFileByUriAndLanguage");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void publishBroadcastTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/publishBroadcast", "ws/v1/publishBroadcast");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void sandboxPreviewFileTest403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/sandbox/previewFile", "ws/v1/sandbox/previewFile");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the getMediaIdsByFileId endpoint returns a 403 when disabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void getMediaIdsByFileId403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getMediaIdsByFileId", "ws/v1/getMediaIdsByFileId");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void generateAuthorUri403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/generateAuthorUri", "ws/v1/generateAuthorUri");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the generateBroadcastFileId endpoint returns a 403 when disabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void generateBroadcastFileId403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/generateBroadcastFileId", "ws/v1/generateBroadcastFileId");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the generateBroadcastUri endpoint returns a 403 when disabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void generateBroadcastUri403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/generateBroadcastUri", "ws/v1/generateBroadcastUri");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the getDocumentByDamIdAndVersion endpoint returns a 403 when disabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void getDocumentByDamIdAndVersion403() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.fourOThreeErrorGet("ws/v1/getDocumentByDamIdAndVersion", "ws/v1/getDocumentByDamIdAndVersion");
        Assert.isTrue(result.compareTo("")==0, result);
    }
    //***************************************************************************


    //*************Testing for GET API rules 200 status*************************
    @Test (timeOut = 180000)
    public void generateScriptureRefTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/generateScriptureRefs", "ws/v1/generateScriptureRefs");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getAuthorDataTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getAuthorData", "ws/v1/getAuthorData");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getDocumentTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getDocument", "ws/v1/getDocument");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getDocumentByDamIdAndVersionTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getDocumentByDamIdAndVersion", "ws/v1/getDocumentByDamIdAndVersion");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getImageTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getImage", "ws/v1/getImage");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getLanguageXmlTest200() throws IOException, SQLException , InterruptedException{
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getLanguageXml", "ws/v1/getLanguageXml");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getPreviewCssTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getPreviewCss", "ws/v1/getPreviewCss");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getAllCssTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getAllCss", "ws/v1/getAllCss");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getAllMatchingFileIDsTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getAllMatchingFileIDs", "ws/v1/getAllMatchingFileIDs");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void contentGroupsTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/contentGroups", "ws/v1/contentGroups");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getPrintCssTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getPrintCss", "ws/v1/getPrintCss");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void processCountTest200() throws IOException, SQLException , InterruptedException{
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/processCount", "ws/v1/processCount");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getAudioTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getAudio", "ws/v1/getAudio");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getVideoUrlTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getVideoUrl", "ws/v1/getVideoUrl");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void documentContentTypesTest200() throws IOException, SQLException , InterruptedException{
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/documentContentTypes", "ws/v1/documentContentTypes");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void previewFileTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/previewFile", "ws/v1/previewFile");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void contentTypesTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/contentTypes", "ws/v1/contentTypes");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void previewFileByUriTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/previewFileByUri", "ws/v1/previewFileByUri");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getPreviewJavascriptTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getPreviewJavascript", "ws/v1/getPreviewJavascript");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getPrintJavascriptTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getPrintJavascript", "ws/v1/getPrintJavascript");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void previewFileByUriAndLanguageTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/previewFileByUriAndLanguage", "ws/v1/previewFileByUriAndLanguage");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void publishBroadcastTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/publishBroadcast", "ws/v1/publishBroadcast");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void sandboxPreviewFileTest200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/sandbox/previewFile", "ws/v1/sandbox/previewFile");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    @Test (timeOut = 180000)
    public void getMediaIdsByFileId200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getMediaIdsByFileId", "ws/v1/getMediaIdsByFileId");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the generateAuthorUri endpoint returns a 200 when enabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void generateAuthorUri200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/generateAuthorUri", "ws/v1/generateAuthorUri");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the generateBroadcastFileId endpoint returns a 200 when enabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void generateBroadcastFileId200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/generateBroadcastFileId", "ws/v1/generateBroadcastFileId");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the generateBroadcastUri endpoint returns a 200 when enabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void generateBroadcastUri200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/generateBroadcastUri", "ws/v1/generateBroadcastUri");
        Assert.isTrue(result.compareTo("")==0, result);
    }

    /**
     * Test to check that the getDocumentByDamIdAndVersion endpoint returns a 200 when enabled
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @Test (timeOut = 180000)
    public void getDocumentByDamIdAndVersion200() throws IOException, SQLException, InterruptedException {
        String result = APIRulesStaticMethods.twoHundredStatusGet("ws/v1/getDocumentByDamIdAndVersion", "ws/v1/getDocumentByDamIdAndVersion");
        Assert.isTrue(result.compareTo("")==0, result);
    }
    //***************************************************************************

}
