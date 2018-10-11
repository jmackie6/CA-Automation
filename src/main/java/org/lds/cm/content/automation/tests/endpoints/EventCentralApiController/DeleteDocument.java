package org.lds.cm.content.automation.tests.endpoints.EventCentralApiController;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.QADocumentUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteDocument {
    @Test(enabled=true)
    public static void deleteDocumentEndpointTest() throws Exception {
        String file = "03271_000_028";
        String filePath = Constants.transformFileStartDir + "/engFriendHTML/07/children-in-early-kirtland.html";
        ResultSet resultSet = JDBCUtils.getResultSet("select * from document where file_id = '" + file + "'");
        if (!resultSet.next()){
            QATransformService.transform(new File(filePath));
        }


        String fileId = QADocumentUtils.getDocumentID(filePath);
        String uri = QADocumentUtils.getUriFromDocument(filePath);
        String tableDocId = QADocumentUtils.getDocumentTableID(filePath);
        String language="000";
        String deleteDocumentEndpoint = Constants.epDeleteDocument; // "https://publish-stage.ldschurch.org/content_automation/ws/v1/deleteDocument";

        List<NameValuePair> namedParams = new ArrayList<>();
        namedParams.add(new BasicNameValuePair("docId", tableDocId));
        namedParams.add(new BasicNameValuePair("fileId", fileId));
        namedParams.add(new BasicNameValuePair("uri", uri));
        namedParams.add(new BasicNameValuePair("language", language));

        JSONObject response = NetUtils.httpPostRequestJSON(deleteDocumentEndpoint, namedParams);

        //put the file back in Content Central
        QATransformService.transform(new File(filePath));
        Assert.assertTrue((boolean)response.get("success"));

    }

    @Test(enabled=true)
    public static void deleteDocumentEndpointTestFailure() throws Exception {
        //Write a test for this endpoint for a document that doesn't exist
        String filePath = Constants.transformFileStartDir + "/engFriendHTML/07/children-in-early-kirtland-failure.html";

        try {
            String fileId = QADocumentUtils.getDocumentID(filePath);
            String uri = QADocumentUtils.getUriFromDocument(filePath);
            String tableDocId = QADocumentUtils.getDocumentTableID(filePath);
            String language = "000";
            String deleteDocumentEndpoint = Constants.epDeleteDocument; //"https://publish-test.ldschurch.org/content_automation/ws/v1/deleteDocument";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("docId", tableDocId);
            parameters.put("fileId", fileId);
            parameters.put("uri", uri);
            parameters.put("language", language);

            Document currentDoc = NetUtils.httpDeleteRequestWithParams(deleteDocumentEndpoint, parameters);
            Assert.assertTrue(false);
        }catch(Exception e){
            Assert.assertFalse(false);
        }



    }
}
