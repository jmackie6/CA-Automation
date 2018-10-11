package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.springframework.util.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class GetAudio {

    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException
    {	APIRules.fixAPIRule("ws/v1/getAudio");	}


    @Test (groups = { "endpoints" })
    public void getAudioWsValid()throws SQLException, IOException, ParseException
    {	validAudioTest("/ws/v1");	}

    @Test (groups = { "endpoints" })
    public void getAudioServValid()throws SQLException, IOException, ParseException
    {	validAudioTest("/services/api");	}

    @Test (groups = { "endpoints" })
    public void getAudioWsBadAudioId() throws IOException
    {   badAudioTest("/ws/v1");    }

    @Test (groups = { "endpoints" })
    public void getAudioServBadAudioId() throws IOException
    {   badAudioTest("/services/api");    }

    private void validAudioTest(String urlUsage) throws SQLException, IOException, ParseException
    {
        String url = Constants.baseURL + urlUsage + "/getAudio?audioId=";
        ArrayList<String> errors = new ArrayList<String>();
        ResultSet rs = JDBCUtils.getRandomRows("audio", Constants.numOfTestsToRun);

        while (rs.next()) {
            String newUrl = url + rs.getInt("audio_id");
            String response = NetUtils.getHTML(newUrl);
            org.springframework.util.Assert.isTrue(response != null, "getAudio returned a null response");
            org.springframework.util.Assert.isTrue(response.length() > 0, "getAudio didn't return a url and the length was 0");
            //if the response doesn't match what is in the database add it to the errors arraylist
            if (!verifyResponse(response, rs.getString("url"), rs.getLong("duration"), rs.getLong("file_size")))
                errors.add(newUrl + "\t" + response);
        }
        rs.close();

        //if there are errors, make a string of them and output them
        Assert.isTrue(errors.size() == 0, "" + ErrorUtils.stringify(errors));
    }

    private boolean verifyResponse(String response, String url, long duration, long size) throws ParseException
    {
        if(response == null || url == null || duration < 0|| size < 0)
            return false;

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);

        long d = (long) jsonObject.get("duration");
        String u = (String) jsonObject.get("url");
        long s = (long) jsonObject.get("size"); //database calls it file_size.  returned postman information calls it size...

        //Comparing the two data streams information
        if (url.compareTo(u) != 0)
            return false;
        if (duration != d)
            return false;
        if (size != s)
            return false;

        //eventually make a method to verify that the audio type is valid
        //return MediaUtils.verifyAudio(u);
        return true;
    }

    private void badAudioTest(String type) throws IOException
    {
        String url = Constants.baseURL + type + "/getAudio?audioId=0";
        String Verification = NetUtils.getHTML(url);
        Assert.isTrue(Verification != null, "getAudio returned null");
        Assert.isTrue(Verification.length() > 0, "getAudio didn't return a url");
        Assert.isTrue(Verification.contains("No audio found for given ID"), "Error, something came back for audioId=0 call . . .\n" + Verification);
    }

    @AfterMethod (alwaysRun = true)
    public void cleanUp() {JDBCUtils.closeUp();}
}
