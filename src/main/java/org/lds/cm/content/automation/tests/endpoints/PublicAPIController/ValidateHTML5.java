package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.XMLUtils;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.apache.http.entity.mime.MultipartEntityBuilder.create;

public class ValidateHTML5
{
    private final static String query = "select count(*) counter from validation_error ve\n" +
            "join document d on ve.document_id = d.document_id\n" +
            "join language l on d.language_id = l.language_code\n" +
            "where lang_name=? and file_name=?";

   // @Test
    public void validateHTML5Ws() throws  IOException, ParserConfigurationException, SAXException, SQLException
    {
        String fileName = "gifts-of-peace.html";
        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add("English");
        fillIn.add(fileName);

        htmlValidation("/ws/v1", "/engBroadcastHTML/12/" + fileName);
        int secondCount = 0;
        ResultSet secondPass = JDBCUtils.getResultSet(query, fillIn);
        if(secondPass.next())
            secondCount = secondPass.getInt("counter");

        //verify that all numbers came back the same.
       // Assert.isTrue(firstCount == secondCount);
    }


    public int htmlValidation(String type, String file) throws IOException, ParserConfigurationException, SAXException
    {
        NodeList nodes = getNodeList(type, file);
        return nodes.getLength();
    }

    public NodeList getNodeList(String type, String fileName) throws IOException, ParserConfigurationException, SAXException
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(Constants.baseURL + type + "/validateHtml5");
        File file = new File(Constants.transformFileStartDir + fileName);
        //File file = new File(Constants.transformFileStartDir + "/SchemaValidationHTML/schema-test-file.html");
        // Commenting out header security information until they put it back in to the code.
        //  httppost.addHeader("client_id", Constants.apiClientId);
        //  httppost.addHeader("client_secret", Constants.apiClientSecret);

        HttpEntity entity1 = create()
                .addBinaryBody("file", file)
                .build();
        httppost.setEntity(entity1);

        // get response
        CloseableHttpResponse response = httpClient.execute(httppost);
        HttpEntity entity2 = response.getEntity();
        String responseString = EntityUtils.toString(entity2);

        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(responseString);

        responseString = responseString.substring(55);
        System.out.println(responseString);

        Document doc = XMLUtils.getDocumentFromString(responseString);
        doc.getDocumentElement().normalize();
        NodeList validationErrors = doc.getElementsByTagName("Validation-errors");
        return validationErrors;
    }


    @AfterMethod(alwaysRun=true)
    public static void cleanup()
    {    JDBCUtils.closeUp();    }
}
