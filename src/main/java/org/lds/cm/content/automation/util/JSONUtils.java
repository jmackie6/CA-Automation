package org.lds.cm.content.automation.util;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class JSONUtils {

    public static JSONObject getJSONFromResponse(CloseableHttpResponse response) throws IOException, ParseException
    {
       HttpEntity hEntity = response.getEntity();
       JSONParser jParser = new JSONParser();
       return (JSONObject) jParser.parse(EntityUtils.toString(hEntity));
    }
    
    public static JSONObject getJSONFromString(String jsonString) throws Exception{
        JSONParser jsonParser = new JSONParser();
        return (JSONObject)jsonParser.parse(jsonString);
    }




}
