package org.lds.cm.content.automation.tests.endpoints.ManifestTest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lds.cm.content.automation.util.NetUtils.httpGetJSONWithParams;
import static org.lds.cm.content.automation.util.NetUtils.httpPostRequestJSON;

public class UpdateManifest {

    @Test(enabled=true)
    public static void updateManifestEnglishTitle() throws Exception{
        String documentId = "1154724";
        Map<String, String> params = new HashMap<>();
        params.put("documentId", documentId);
        String urlString = Constants.epUpdateManifestEnglishTitle;
        JSONObject jObj = httpGetJSONWithParams(urlString, params);
        Assert.assertEquals(jObj.get("Response"), "We have received your request.");


    }

}
