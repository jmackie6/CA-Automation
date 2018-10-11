package org.lds.cm.content.automation.service;

import static org.apache.http.entity.mime.MultipartEntityBuilder.create;
import static org.lds.cm.content.automation.util.Constants.Constants.*;

import java.net.HttpURLConnection;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.enums.*;
import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.model.ReturnObj;
import org.lds.cm.content.automation.util.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.lds.cm.content.automation.util.Constants.Constants.epEditorialTransform;
import static org.lds.cm.content.automation.util.Constants.Constants.epTransform;
import static org.lds.cm.content.automation.util.Constants.Constants.epTransformSA;

public class QATransformService {
    static SoftAssert softAssert = new SoftAssert();
    private static ReturnObj returnObj = new ReturnObj();
    private static ArrayList<String> fillInData = new ArrayList<String>();

    public static QATransformationResult transformFile(File file) throws Exception {
        System.out.println("\n** Start Transform **");

//        checkRoot(file); // delete before running the transform - open file grab sku or pd number/file id delete from document table
        JSONObject jsonResponse;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // build request epTransform = https://publish-test.ldschurch.org/content_automation/ws/v1/transform

            HttpPost httppost = new HttpPost(epTransform);

            // Commenting out header security information until they put it back in to the code.
            //  httppost.addHeader("client_id", Constants.apiClientId);
            //  httppost.addHeader("client_secret", Constants.apiClientSecret);
            Integer contentGroupId = getContentGroup(file); //get content group id number to pass to the endpoint

            System.out.println("contentGroup: " + contentGroupId);

            // StringEntity params = new StringEntity("details={\"contentGroupId\":\"4\"} ");
//            StringEntity params = new StringEntity("{ \"contentGroupId\" : \"" + contentGroup + "\" }");
//            ArrayList<NameValuePair> params = new ArrayList<>();
//            params.add(new BasicNameValuePair("contentGroupId", contentGroupId.toString()));
//            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpEntity entity1 = create()
                    .addTextBody("contentGroupId", contentGroupId.toString())
                    .addBinaryBody("file", file)
                    .build();
            httppost.setEntity(entity1);

            JSONParser parser = new JSONParser();
            try (CloseableHttpResponse response = httpClient.execute(httppost)) {
                // get response

                HttpEntity entity2 = response.getEntity();
                String responseString = EntityUtils.toString(entity2);

//                jsonResponse = (JSONObject) new JSONParser().parse(responseString);
                Object obj = parser.parse(responseString);
                JSONObject jsonObject = (JSONObject) obj;
                jsonResponse = jsonObject;
                String transformSuccess = (String) jsonObject.get("transformSuccess");
                System.out.println(transformSuccess);

            }
        }

        boolean successful = (boolean) jsonResponse.get("transformSuccess");

        if (successful) {
            softAssert.assertEquals(successful, true);

            //check the db does the file exist?
            String guid = (String) jsonResponse.get("previewUrl");
            String[] parts = guid.split("=");
            String part2 = parts[1];
            String fileID = fromGUID(part2); //return document using path, open manifest and get each link, path, lang, filename
            System.out.println("FileId from Transform: " + fileID);

            //QAManifestService.returnAllFileIDs(fileID);
            QAHtml5Document doc = existsInDbByFileID(fileID); // database manifest endpoint

            if (doc != null) {
                System.out.println("FileId from database: " + doc.getFileId());
                System.out.println(" \n Transform Successful\n");
            } else {
                System.out.println("  Transform Failed: Not found in database");
                return null;
            }

        } else {
            Assert.assertEquals(successful, false);
            // output error message why the transform failed
            System.out.println("**************************");
            System.out.println("**** Transform Failed ****");
            System.out.println("**************************");
            String message = (String) jsonResponse.get("error");
            System.out.println(message);
            Assert.fail();

        }
        return QATransformationResult.fromJSON(jsonResponse);
    } //end transformFile

    public static QATransformationResult transformFileWithoutContentGroup(File file) throws Exception {

        System.out.println("\n** Start Transform **");

        JSONObject jsonResponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // build request epTransform = https://publish-test.ldschurch.org/content_automation/ws/v1/transform

        HttpPost httppost = new HttpPost(epTransform);

        HttpEntity entity1 = create()
                .addTextBody("contentGroupId", "")
                .addBinaryBody("file", file)
                .build();
        httppost.setEntity(entity1);

        CloseableHttpResponse response = httpClient.execute(httppost);
        // get response

        int code = response.getStatusLine().getStatusCode();
        HttpEntity entity2 = response.getEntity();
        String responseString = EntityUtils.toString(entity2);

        if (code == 200) {
            jsonResponse = (JSONObject) new JSONParser().parse(responseString);
        } else {
            Assert.fail("Transform returned this status code: " + code);
        }

        boolean successful = (boolean) jsonResponse.get("transformSuccess");

        if (successful) {
            softAssert.assertEquals(successful, true);
            System.out.println("  \nTransform Successful\n");

        } else {
            // output error message why the transform failed
            System.out.println("**************************");
            System.out.println("**** Transform Failed ****");
            System.out.println("**************************");
            String message = (String) jsonResponse.get("error");
            System.out.println(message);
            Assert.fail();

        }
        softAssert.assertAll();

        System.out.println("** END Transform **\n");

        return QATransformationResult.fromJSON(jsonResponse);

    } //end transformFile

    public static QATransformationResult transformFileGivenContentGroupId(File file, String contentGroupId) throws Exception {

        if (contentGroupId == null) {
            contentGroupId = TransformationContentTypeID.TEST_DEFAULT.toString();
        }

        System.out.println("\n** Start Transform **");

        JSONObject jsonResponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // build request epTransform = https://publish-test.ldschurch.org/content_automation/ws/v1/transform

        HttpPost httppost = new HttpPost(epTransform);

        System.out.println("contentGroup: " + contentGroupId);

        HttpEntity entity1 = create()
                .addTextBody("contentGroupId", contentGroupId)
                .addBinaryBody("file", file)
                .build();

        httppost.setEntity(entity1);

        CloseableHttpResponse response = httpClient.execute(httppost);
        // get response

        int code = response.getStatusLine().getStatusCode();

        /**
         * Occasionally the transform automation is getting a status code 302 which is a temporary redirect. This code should fix
         * that
         */
        if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER) {
            System.out.println("Http connection moved : Error code = " + code);
            org.apache.http.Header[] locationHeader = response.getHeaders("Location");
            httppost = new HttpPost(locationHeader[0].getValue());
            entity1 = create()
                    .addTextBody("contentGroupId", contentGroupId)
                    .addBinaryBody("file", file)
                    .build();
            httppost.setEntity(entity1);
            response = httpClient.execute(httppost);
        }

        HttpEntity entity2 = response.getEntity();
        String responseString = EntityUtils.toString(entity2);

        System.out.println("  \nError Code:" + code);
        if (code == 200) {
            jsonResponse = (JSONObject) new JSONParser().parse(responseString);

        } else {
            Assert.fail("Transform returned this status code: " + code);
        }

        boolean successful = (boolean) jsonResponse.get("transformSuccess");

        if (successful) {
            softAssert.assertEquals(successful, true);
            System.out.println("  \nTransform Successful\n");

        } else {
            // output error message why the transform failed
            System.out.println("**************************");
            System.out.println("**** Transform Failed ****");
            System.out.println("**************************");
            String message = (String) jsonResponse.get("error");
            System.out.println(message);
            Assert.fail();

        }
        softAssert.assertAll();

        System.out.println("** END Transform **\n");

        return QATransformationResult.fromJSON(jsonResponse);

    } //end transformFile

    public static QATransformationResult transformFileGivenContentGroupIdServicesAPI(File file, String contentGroupId) throws Exception {

        if (contentGroupId == null) {
            contentGroupId = TransformationContentTypeID.TEST_DEFAULT.toString();
        }

        System.out.println("\n** Start Transform **");

        JSONObject jsonResponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // build request epTransform = https://publish-test.ldschurch.org/content_automation/ws/v1/transform

        HttpPost httppost = new HttpPost(epTransformSA);

        System.out.println("contentGroup: " + contentGroupId);

        HttpEntity entity1 = create()
                .addTextBody("contentGroupId", contentGroupId)
                .addBinaryBody("file", file)
                .build();
        httppost.setEntity(entity1);

        CloseableHttpResponse response = httpClient.execute(httppost);
        // get response

        int code = response.getStatusLine().getStatusCode();

        /**
         * Occasionally the transform automation is getting a status code 302 which is a temporary redirect. This code should fix
         * that
         */
        if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM || code == HttpURLConnection.HTTP_SEE_OTHER) {
            System.out.println("Http connection moved : Error code = " + code);
            org.apache.http.Header[] locationHeader = response.getHeaders("Location");
            httppost = new HttpPost(locationHeader[0].getValue());
            entity1 = create()
                    .addTextBody("contentGroupId", contentGroupId)
                    .addBinaryBody("file", file)
                    .build();
            httppost.setEntity(entity1);
            response = httpClient.execute(httppost);
        }

        HttpEntity entity2 = response.getEntity();
        String responseString = EntityUtils.toString(entity2);

        System.out.println("  \nError Code:" + code);
        if (code == 200) {
            jsonResponse = (JSONObject) new JSONParser().parse(responseString);

        } else {
            Assert.fail("Transform returned this status code: " + code);
        }

        boolean successful = (boolean) jsonResponse.get("transformSuccess");

        if (successful) {
            softAssert.assertEquals(successful, true);
            System.out.println("  \nTransform Successful\n");

        } else {
            // output error message why the transform failed
            System.out.println("**************************");
            System.out.println("**** Transform Failed ****");
            System.out.println("**************************");
            String message = (String) jsonResponse.get("error");
            System.out.println(message);
            Assert.fail();

        }
        softAssert.assertAll();

        System.out.println("** END Transform **\n");

        return QATransformationResult.fromJSON(jsonResponse);
    }

    public static JSONObject editorialTransform(File file, String json) throws Exception {

        JSONObject jsonResponse = null;
        System.out.println(json);
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httppost = new HttpPost(epEditorialTransform);

        HttpEntity entity1 = create()
                .addBinaryBody("file", file)
                .addTextBody("metadata", json)
                .build();

        httppost.setEntity(entity1);

        CloseableHttpResponse response = httpClient.execute(httppost);
        // get response

        int code = response.getStatusLine().getStatusCode();
        HttpEntity entity2 = response.getEntity();
        String responseString = EntityUtils.toString(entity2);

        if (code == 200) {
            jsonResponse = (JSONObject) new JSONParser().parse(responseString);
        } else {
            Assert.fail("Transform returned this status code: " + code);
        }

        return jsonResponse;
    }

    //Refactored function to be used instead of transformFileGivenContentGroupId
    public static JSONObject transform(File file) throws IOException, ParseException {
        epTransform = "https://publish-test.ldschurch.org/content_automation/ws/v1/transform";
        System.out.println("*********Transforming*********");
        final JSONObject returnObject = processPostWithFile(file, TransformationContentTypeID.TEST_DEFAULT, epTransform);
        return returnObject;

    } //end transformFile

    public static JSONObject processPostWithFile(File file, TransformationContentTypeID typeId, String urlRequest) throws IOException, ParseException{
        final LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
        final CloseableHttpClient httpClient = HttpClients.custom()
                .setRedirectStrategy(redirectStrategy)
                .build();
        HttpPost httpPost = new HttpPost(urlRequest);
        try {



            JSONParser jParse = new JSONParser();


            HttpEntity entity = create()
                    .addTextBody("contentGroupId", typeId.toString())
                    .addBinaryBody("file", file)
                    .build();

            httpPost.setEntity(entity);
            CloseableHttpResponse closeableResponse = httpClient.execute(httpPost);
            JSONObject temp = (JSONObject) jParse.parse(EntityUtils.toString(closeableResponse.getEntity()));
//           return (JSONObject) jParse.parse(EntityUtils.toString(closeableResponse.getEntity()));
            return temp;
        }finally {
//            httpClient.getConnectionManager().shutdown();
            httpClient.close();
            httpPost.releaseConnection();
        }

    }

    public static void deleteDocIfExistsInDb(String fileId) throws Exception {
//        boolean transResult = false;
//        boolean exists = existsInDbByFileId(fileId);
//        transResult = QATransformService.transformFileGivenContentGroupId(file, Constants.broadcastContentGroupId).wasSuccessful();
//        if(transResult) {
//            if(exists) {
//                QADeleteService.deleteFromDbByFileIdSingleDocument(fileId);
//            }
//        }
//        else if (!transResult) {
//            if(exists) {
//                QADeleteService.deleteFromDbByFileIdSingleDocument(fileId);
//            }
//        }

        boolean exists = existsInDbByFileId(fileId);
        if(exists) {
            QADeleteService.deleteFromDbByFileIdSingleDocument(fileId);
        }

    }

    public static JSONObject transform(File file, String contentType) throws IOException, ParseException {

        System.out.println("\n** Start Transform **");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        // build request epTransform = https://publish-test.ldschurch.org/content_automation/ws/v1/transform

        HttpPost httppost = new HttpPost(epTransform);
        HttpEntity entity1 = create()
                .addTextBody("contentGroupId", contentType)
                .addBinaryBody("file", file)
                .build();
        httppost.setEntity(entity1);

        // get response
        System.out.println("Executing Transform");
        CloseableHttpResponse response = httpClient.execute(httppost);

        HttpEntity entity2 = response.getEntity();
        String responseString = EntityUtils.toString(entity2);

        JSONObject returnObject = (JSONObject) new JSONParser().parse(responseString);
        return returnObject;

//        System.out.println("** END Transform **\n");
//        return QATransformationResult.fromJSON(jsonResponse);
    } //end transformFile

    public static String validate(final File file) throws SAXException, IOException, ParserConfigurationException {

        System.out.println("******Starting Validation*******");
        final CloseableHttpClient httpClient = HttpClients.createDefault();

        final HttpPost httpPost = new HttpPost(epValidate);
        final HttpEntity entity1 = create()
                .addBinaryBody("file", file)
                .build();
        httpPost.setEntity(entity1);

        //get response
        System.out.println("Validating");
        CloseableHttpResponse response = httpClient.execute(httpPost);

        HttpEntity entity2 = response.getEntity();
        String responseString = EntityUtils.toString(entity2);

//        JSONObject jsonResponse = (JSONObject) new JSONParser().parse(responseString);
//        QATransformationResult returnResult = QATransformationResult.fromXML(responseString);
        System.out.println("****Finished Validation*****");
        return responseString;
//        return returnResult; //QATransformationResult.fromJSON(jsonResponse);

    }

    public QATransformationResult validate(Path filePath) {
        throw new UnsupportedOperationException("Function not ready!");
        //return null;
    }

    public static String getContentGroupIdScriptures() throws Exception {
        String scripturesGroupId;
        scripturesGroupId = "301";

        return scripturesGroupId;
    }

    public static String getContentGroupIdDocx() throws Exception {
        String scripturesGroupId;
        scripturesGroupId = "1101";
        return scripturesGroupId;
    }

    private static int getContentGroup(File file) throws Exception {

        // Check type
        if (QAFileUtils.isDocXFile(file)) {
            convertDocXToXML(file);
        } else {
            Document docFromFile = XMLUtils.getDocumentFromFile(file);

            Node rootNode = docFromFile.getDocumentElement();
            System.out.println(rootNode);

            String rootNodeString = rootNode.getNodeName();
            System.out.println(rootNodeString);

            if (rootNodeString.equalsIgnoreCase("ldswebml")) {

                ArrayList<String> arrlist = new ArrayList<>();
                String xpath = "//source/text()";
                NodeList sources = XMLUtils.getNodeListFromXpath(file, xpath);

                for (int i = 0; i < sources.getLength(); i++) {
                    String fileId = sources.item(i).getNodeValue();

                    arrlist.add(fileId);
                }
                System.out.println(arrlist);

                Document tempDoc = XMLUtils.getDocumentFromFile(file);

                NodeList htmlNodeList = tempDoc.getElementsByTagName("ldswebml");
                System.out.println("INFO: " + htmlNodeList);
                Node htmlNode = htmlNodeList.item(0);

                String documentUri = htmlNode.getAttributes().getNamedItem("uri").getNodeValue();
                documentUri = documentUri.substring(0, documentUri.lastIndexOf("/"));
                System.out.println("URI: " + documentUri);
                int id = getContentTypeFromUri(documentUri);
                return id;

            } else if (rootNodeString.equalsIgnoreCase("magazine") || rootNodeString.equalsIgnoreCase("book") || rootNodeString.equalsIgnoreCase("testament")) {

                ArrayList<String> arrlist = new ArrayList<>();
                String xpath = "//source/text()";
                NodeList sources = XMLUtils.getNodeListFromXpath(file, xpath);

                for (int i = 0; i < sources.getLength(); i++) {
                    String fileId = sources.item(i).getNodeValue();

                    arrlist.add(fileId);
                }
                System.out.println(arrlist);
                Document tempDoc = XMLUtils.getDocumentFromFile(file);

                NodeList htmlNodeList = tempDoc.getElementsByTagName("publicationID");
                System.out.println("INFO: " + htmlNodeList);
                Node htmlNode = htmlNodeList.item(0);

                String documentUri = htmlNode.getAttributes().getNamedItem("type").getNodeValue();

                return getContentNumberFromType(documentUri);

            } else { //html
                ArrayList<String> arrlist = new ArrayList<>();
                String xpath = "//*[contains(@type,'file')]/text()";
                NodeList sources = XMLUtils.getNodeListFromXpath(file, xpath);

                for (int i = 0; i < sources.getLength(); i++) {
                    String fileId = sources.item(i).getNodeValue();

                    arrlist.add(fileId);
                }
                System.out.println(arrlist);
                Document tempDoc = XMLUtils.getDocumentFromFile(file);

                NodeList htmlNodeList = tempDoc.getElementsByTagName("html");
                System.out.println("INFO: " + htmlNodeList);
                Node htmlNode = htmlNodeList.item(0);

                String documentUri = htmlNode.getAttributes().getNamedItem("data-uri").getNodeValue();
                documentUri = documentUri.substring(0, documentUri.lastIndexOf("/"));
                System.out.println("URI: " + documentUri);
                return getContentTypeFromUri(documentUri);
            }
        }
        return 0;

    }



    @SuppressWarnings("null")
    public static int getContentTypeFromUri(String uri) {
        String content = getFirstUriSegment(uri);

        if (StringUtils.isBlank(content)) {
            return (Integer) null;
        }

        assert content != null;

        switch (content) {
            case "ensign":
                return 2;
            case "liahona":
                return 3;
            case "friend":
                return 5;
            case "new-era":
                return 4;
            case "manual":
                return 401;
            case "scriptures":
                return 301;
            case "broadcasts":
                return 402;
            case "general-conference":
                return 1;
            default:
                return 1101;
        }
    }

    public static int getContentNumberFromType(String type) {

        switch (type) {
            case "ensign":
                return 2;
            case "liahona":
                return 3;
            case "friend":
                return 5;
            case "new-era":
                return 4;
            case "manual":
                return 401;
            case "scriptures":
                return 301;
            case "broadcasts":
                return 402;
            case "general-conference":
                return 1;
            default:
                return 603;
        }
    }

    static String getFirstUriSegment(String uri) {
        if (StringUtils.isBlank(uri)) {
            return null;
        }

        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        return uri.substring(0, uri.indexOf("/")).toLowerCase();
    }

    public static void checkRoot(File file) throws Exception {
        // get source - which is the file_id (needed for merged webml broadcast where the file_ids are mismatched, so we can't use the manifest endpoint)
        // Check to see if we are dealing with ldswebml

        //checkDocx(file);
        if (QAFileUtils.isDocXFile(file)) {
            convertDocXToXML(file);
        } else {
            Document docFromFile = XMLUtils.getDocumentFromFile(file);

            Node rootNode = docFromFile.getDocumentElement();
            System.out.println(rootNode);

            String rootNodeString = rootNode.getNodeName();
            System.out.println(rootNodeString);

            if (rootNodeString.equalsIgnoreCase("ldswebml")) {

                ArrayList<String> arrlist = new ArrayList<>();
                String xpath = "//source/text()";
                NodeList sources = XMLUtils.getNodeListFromXpath(file, xpath);

                for (int i = 0; i < sources.getLength(); i++) {
                    String fileId = sources.item(i).getNodeValue();
//                    QADeleteService.deleteFromAnnotationsMLAndDB(fileId);
                    arrlist.add(fileId);
                }
                System.out.println(arrlist);

            } else if (rootNodeString.equalsIgnoreCase("magazine") || rootNodeString.equalsIgnoreCase("book") || rootNodeString.equalsIgnoreCase("testament")) {

                ArrayList<String> arrlist = new ArrayList<>();
                String xpath = "//@fileID";
                NodeList sources = XMLUtils.getNodeListFromXpath(file, xpath);

                for (int i = 0; i < sources.getLength(); i++) {
                    String fileId = sources.item(i).getNodeValue();
                    //QADeleteService.deleteFromAnnotationsMLAndDB(fileId); //this overwrites the previously transformed file needs to be fixed
                    arrlist.add(fileId);
                }
                System.out.println(arrlist);

            } else {
                ArrayList<String> arrlist = new ArrayList<>();
                String xpath = "//*[contains(@type,'file')]/text()";
                NodeList sources = XMLUtils.getNodeListFromXpath(file, xpath);

                for (int i = 0; i < sources.getLength(); i++) {
                    String fileId = sources.item(i).getNodeValue();
                    //QADeleteService.deleteFromAnnotationsMLAndDB(fileId);	//this overwrites the previously transformed file needs to be fixed
                    arrlist.add(fileId);
                }
                System.out.println(arrlist);
            }
        }
    } //end checkRoot

    private static File convertDocXToXML(final File inputFile) throws JAXBException, Docx4JException, SQLException {

        final File destDir = new File(Constants.transformFileStartDir + "/docx");
        final Path dest = new File(destDir, inputFile.getName()).toPath();
        XMLUtils.convertDocxToXML(inputFile, dest.toString());

        return destDir;
    }

    // Returns a doc if found in DOCUMENT table, otherwise null
    // Should this be private?
    public static QAHtml5Document existsInDbByFileID(String fileID) throws SQLException, IOException {
        String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where d.FILE_ID=?";
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(fileID);
        ResultSet rs = JDBCUtils.getResultSet(sql, fillIn);
        List<QAHtml5Document> docs = listFromResultSet(rs);
        if (docs.size() > 0) {
            return docs.get(0);
        } else {
            return null;
        }
    }

    // Modify the create-date and modified-date of a doc
    public static void modifyDates(String docId) throws SQLException, IOException {
        System.out.println("  Setting create-date and modified-date to past date for docId:" + docId);

        Assert.assertNotNull(docId);
        try {

            String sql1 = "update document set modified_date = '14-JUL-17',CREATED_DATE = '14-JUL-17' WHERE DOCUMENT_ID = '" + docId + "'";
            JDBCUtils.getResultSet(sql1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Assure that the created/modified date is not within the last 2 days
    public static boolean datesNotRecent(String docId) {

        Assert.assertNotNull(docId);
        try {
            String sql = "select d.MODIFIED_DATE, d.CREATED_DATE from document d WHERE d.DOCUMENT_ID = '" + docId + "'";
            JDBCUtils.getResultSet(sql);
            ResultSet rs = JDBCUtils.getResultSet(sql);

            boolean resultFound = false;
            while (rs.next()) {
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());    // Yong Mook Kim, https://www.mkyong.com/java/how-to-get-current-timestamps-in-java/
                Timestamp modDate = rs.getTimestamp("modified_date");
                Timestamp creDate = rs.getTimestamp("created_date");
                Assert.assertEquals(((currentTime.getTime() - creDate.getTime()) / 1000 / 60 / 60 / 24) > 2, true);
                Assert.assertEquals(((currentTime.getTime() - modDate.getTime()) / 1000 / 60 / 60 / 24) > 2, true);

                resultFound = true;
                break;
            }
            Assert.assertEquals(resultFound, true, "Document of docId:" + docId + " not found");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("  Modified-date and created-date verified as more than 2 days old for docId:" + docId);
        return true;
    }

    public static String fromGUID(String guid) throws SQLException, IOException {
        String sql = "select d.FILE_ID from process_log p join document d on d.TRANSFORM_PROCESS_LOG_ID = p.PROCESS_LOG_ID where p.BATCH_GUID =  " + "'" + guid + "'";
        String file_id = null;

        try {
            ResultSet rs = JDBCUtils.getResultSet(sql);

            if (rs != null) {
                while (rs.next()) {
                    file_id = rs.getString("FILE_ID");
                }
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return file_id;
    }

    //Query database for files matching fileId and contentType
    public static List<QAHtml5Document> fromContentTypeAndFileId(String contentType, String fileId) throws SQLException, IOException {
        String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where d.CONTENT_TYPE = " + "'" + contentType + "' AND d.FILE_ID = " + "'" + fileId + "'";
        ResultSet rs = JDBCUtils.getResultSet(sql);
        return listFromResultSet(rs);
    }

    //	check mark logic for uri mapping
    public static void verifyUriMapping(String docInfo) throws IOException {
        String pathToCheck = MarkLogicDatabase.URI_MAPPING.getContentRoot() + docInfo;
        System.out.println("pathToCheck is: " + pathToCheck);

        File tempFile = MarkLogicUtils.readFile(pathToCheck);
        String fileContents = org.apache.commons.io.FileUtils.readFileToString(tempFile);
        //printDebug(fileContents);

        Assert.assertTrue(StringUtils.isNotEmpty(fileContents));

        // second try, call utility method to check for existence
        Assert.assertTrue(MarkLogicUtils.docExists(pathToCheck), "Call to ML.docExists()");
    }

    public static QAHtml5Document ExistsInDbByDocID(String docID) throws SQLException, IOException {
        if (docID != null) {
            String sql = "select d.*, NVL2(DOCUMENT_XML, (DOCUMENT_XML).getClobVal(), NULL) as document_xml_clob from DOCUMENT d where d.DOCUMENT_ID= " + "'" + docID + "'";
            ResultSet rs = JDBCUtils.getResultSet(sql);
            List<QAHtml5Document> docs = listFromResultSet(rs);
            if (docs.size() > 0) {
                return docs.get(0);
            }
        }
        return null;
    }

    public static ResultSet queryDocById(String docID) throws SQLException, IOException {
        String sql = "select * from DOCUMENT d where d.DOCUMENT_ID= " + "'" + docID + "'";
        ResultSet rs = JDBCUtils.getResultSet(sql);
        return rs;
    }

    public static String docIdFromProcessId(Long processId) throws SQLException, IOException {
        String docId = null;
        if (processId != null) {
            String sql = "select document_id from document where transform_process_log_id = '" + processId + "'";
            ResultSet rs = JDBCUtils.getResultSet(sql);
            while (rs.next()) {
                docId = Long.toString(rs.getLong("DOCUMENT_ID"));
            }
            rs.close();
        }
        return docId;
    }
    public static boolean existsInDbByFilePath(String filePath) throws Exception{
        final Document currentDoc = XMLUtils.getDocumentFromFile(new File(filePath));
        String fileID = XPathUtils.getStringValue("//lds:title[@type='file']", currentDoc, true);
        String getFileDBSql = "SELECT COUNT(1) as docCount FROM DOCUMENT d WHERE d.FILE_ID LIKE '%" + fileID + "%' ";
        final ResultSet rs = JDBCUtils.getResultSet(getFileDBSql);

        boolean returning = false;
        if(rs.next()){
            returning = rs.getBigDecimal("docCount").intValue() > 0;
        }
        rs.close();

        return returning;
    }

    public static boolean existsInDbByFileId(String file_id) throws SQLException, IOException {
        boolean exist = false;
        final String sql = "select count(*) AS rowcount from document where file_id = (?)";
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, false);
        returnObj.resultSet.next();
        int num = returnObj.resultSet.getInt("rowcount");
        System.out.println(num);
        if (num > 0) {
            exist = true;
        } else {
            exist = false;
        }

        returnObj.resultSet.close();

        fillInData.clear();
        return exist;
    }

    public static boolean existsInDbByDocId(String docId) throws SQLException, IOException {
        boolean exist = false;
        final String sql = "select count(*) AS rowcount from document where document_id = (?)";
        fillInData.add(docId);
        returnObj = JDBCUtils.getResultSet(sql, fillInData, false);
        returnObj.resultSet.next();
        int num = returnObj.resultSet.getInt("rowcount");
        System.out.println(num);
        if (num > 0) {
            exist = true;
        } else {
            exist = false;
        }

        returnObj.resultSet.close();

        fillInData.clear();
        return exist;
    }

    public static int numGroupByFileId(String file_id, TransformationContentTypeID contentGroupId) throws SQLException {

        String sql = "select count(*) AS rowcount from CONTENT_AUTO.DOCUMENT_CONTENT_GROUP where DOCUMENT_ID in"
                + " (select document_id from document where file_id like (?)) and CONTENT_GROUP_ID = (?)";

        fillInData.add(file_id);
        fillInData.add(contentGroupId.toString());
        returnObj = JDBCUtils.getResultSet(sql, fillInData, false);
        returnObj.resultSet.next();
        int groupNum = returnObj.resultSet.getInt("rowcount");
        returnObj.resultSet.close();

        fillInData.clear();
        System.out.println("Number of documents with liahona group: " + groupNum);
        return groupNum;
    }

    public static int updateGroupByFileId(TransformationContentTypeID contentGroupId, String file_id) throws SQLException {

        String updateQuery = "update CONTENT_AUTO.DOCUMENT_CONTENT_GROUP set content_group_id = (?) where DOCUMENT_ID "
                + "in (select document_id from document where file_id like (?))";
        fillInData.add(contentGroupId.toString());
        fillInData.add(file_id);
        returnObj = JDBCUtils.getResultSet(updateQuery, fillInData, false);
        int update = returnObj.intVal;

        fillInData.clear();
        System.out.println("Update number for " + contentGroupId.name() + ": " + update);
        return update;
    }

    private static List<QAHtml5Document> listFromResultSet(ResultSet rs) throws SQLException, IOException {
        List<QAHtml5Document> documents = new ArrayList<>();
        while ((rs.next())) {
            QAHtml5Document doc = new QAHtml5Document();
            doc.setApproved(rs.getString("APPROVED"));
            doc.setCitation(rs.getString("CITATION"));
            doc.setContentType(rs.getString("CONTENT_TYPE"));
            doc.setCreatedDate(rs.getTimestamp("CREATED_DATE"));
            doc.setDataAid(rs.getString("DATA_AID"));
            doc.setDefaultPreviewFlag(rs.getBoolean("DEFAULT_PREVIEW_FLAG"));
            doc.setDocumentId(rs.getLong("DOCUMENT_ID"));

            Clob temp = rs.getClob("document_xml_clob");
            String clobAsString = JDBCUtils.clobToString(temp);
            doc.setDocumentXML(clobAsString);

            doc.setFileId(rs.getString("FILE_ID"));
            doc.setFileName(rs.getString("FILE_NAME"));
            doc.setFolderID(rs.getLong("FOLDER_ID"));
            doc.setLanguageID(rs.getInt("LANGUAGE_ID"));
            doc.setMediaXmlID(rs.getLong("MEDIA_XML_ID"));
            doc.setModifiedByAppUserID(rs.getLong("MODIFIED_BY_APP_USER_ID"));
            doc.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));
            doc.setOwnerAppUserID(rs.getLong("OWNER_APP_USER_ID"));
            doc.setOxygenEditAppUserId(rs.getLong("OXYGEN_EDIT_APP_USER_ID"));
            doc.setOxygenEditDate(rs.getTimestamp("OXYGEN_EDIT_DATE"));
            doc.setPath(rs.getString("PATH"));
            doc.setStatusAppUserID(rs.getLong("STATUS_APP_USER_ID"));
            doc.setStatusChangeDate(rs.getTimestamp("STATUS_CHANGE_DATE"));
            doc.setSystemLock(rs.getString("SYSTEM_LOCK"));
            doc.setTransformProcessLogID(rs.getLong("TRANSFORM_PROCESS_LOG_ID"));
            doc.setValidated(rs.getBoolean("VALIDATED"));
            doc.setXmlModifiedDate(rs.getTimestamp("XML_MODIFIED_DATE"));

            documents.add(doc);
        }
        return documents;
    }

    public static void uncheckDefaultContentGroup() throws SQLException {
        ResultSet rs = JDBCUtils.getResultSet("select COUNT(*) as rowcount from CONTENT_AUTO.CONTENT_GROUP where default_group = '1'");
        // DEV Lane query used because IDs are different
//        ResultSet rs = JDBCUtils.getResultSet("select COUNT(*) as rowcount from CONTENT_AUTO.CONTENT_GROUP where default_group = '1801'");
        rs.next();
        int count = rs.getInt("rowcount");
        rs.close();
        if (count == 1) {
            String updateQuery = "update CONTENT_AUTO.CONTENT_GROUP set default_group = '0' where default_group = '1'";
            JDBCUtils.executeUpdate(updateQuery);
            System.out.println("\nunchecking the default content group\n");
        } else {
            System.out.println("No need to change the default content group since No default content group is set");
        }

        JDBCUtils.closeUp();

    }

    public static void checkGeneralConferenceAsDefaultContentGroup() throws SQLException {
        String updateQuery = "update CONTENT_AUTO.CONTENT_GROUP set default_group = '1' where CONTENT_GROUP_ID = '1'";
        // DEV Lane query to update content_group default because the IDs are different for stage and test
//        String updateQuery = "update CONTENT_AUTO.CONTENT_GROUP set default_group = '1' where CONTENT_GROUP_ID = '1801'";
        int update = JDBCUtils.executeUpdate(updateQuery);
        System.out.println(update);
        if (update == 1) {
            System.out.println("\nGeneral Conference set as the default content Group\n");
        } else {
            System.out.println("\nNo need to make general conference the default content group\n");
        }

        JDBCUtils.closeUp();

    }

}
