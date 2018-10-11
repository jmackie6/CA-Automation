package org.lds.cm.content.automation.tests.endpoints.MediaXmlApiController;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.QAFileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.entity.mime.MultipartEntityBuilder.create;

public class UploadMediaXml {

    /* this test attempts to upload valid media-xml files, and fails if they are not successfully uploaded */

    @Test (groups = { "endpoints" })
    public static void validMediaXml() throws Exception {
        List<File> files = new ArrayList<>();
        List<String> success = new ArrayList<>();
        List<String> fail = new ArrayList<>();

        // will add more documents to valid/invalid media-xml folders

        File media_xml = new File(Constants.mediaXmlFileStartDir + "/ValidXML");
        QAFileUtils.loadTestFiles(files, media_xml);

        for (File file : files) {
            int num = file.getPath().lastIndexOf("/");
            if (uploadMediaXml(file).toString().contains("[true]")) {
                success.add(file.getPath().substring(num + 1));
            } else {
                fail.add(file.getPath().substring(num + 1));
            }
        }

        Assert.assertFalse(fail.size() > 0, "\nThe following media-xml files failed to upload: " + fail + "\n");
        System.out.println("All media-xml files were successfully uploaded!\nDocuments: " + success + "\n");
    }

    /* this test attempts to upload invalid media-xml, and fails if it is successfully uploaded */

    @Test (groups = { "endpoints" })
    public static void invalidMediaXml() throws Exception {
        List<File> files = new ArrayList<>();
        List<String> success = new ArrayList<>();
        List<String> fail = new ArrayList<>();

        File media_xml = new File(Constants.mediaXmlFileStartDir + "/InvalidXML");
        QAFileUtils.loadTestFiles(files, media_xml);

        for (File file : files) {
            int num = file.getPath().lastIndexOf("/");
            if (uploadMediaXml(file).toString().equals("{}")) {
                fail.add(file.getPath().substring(num + 1));
            } else {
                success.add(file.getPath().substring(num + 1));
            }
        }

        Assert.assertFalse(success.size() > 0, "\nThe following invalid media-xml files were successfully uploaded but should have failed: " + success + "\n");
        System.out.println("All invalid media-xml files failed to be uploaded. This is working as desired. \nDocuments: " + fail + "\n");
    }

    /* this function posts to the uploadMediaXml endpoint */

    public static JSONObject uploadMediaXml(File file) throws IOException, ParseException {

        JSONObject jsonResponse = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpPost httppost = new HttpPost(Constants.baseURL + "/ws/v1/uploadMediaXml");

            HttpEntity entity1 = create()
                    .addTextBody("mediaType", "conference-media")
                    .addBinaryBody("file", file)
                    .build();
            httppost.setEntity(entity1);

            CloseableHttpResponse response = httpClient.execute(httppost);

            HttpEntity entity2 = response.getEntity();
            String responseString = EntityUtils.toString(entity2);
            jsonResponse = (JSONObject) new JSONParser().parse(responseString);

            httpClient.close();
            response.close();
            return jsonResponse;
        }

    }
}
