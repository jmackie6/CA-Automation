package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.http.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetDocumentByDamIDAndVersion {

    private enum callTypes
    {
        DVValid, DVBadDamId, DVBadCV, DValid, DBadDamId
    }

    //made it global so that it can be closed better
    private ResultSet rs = null;

    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException
    {
        APIRules.fixAPIRule("ws/v1/getDocumentByDamIdAndVersion");
        APIRules.fixAPIRule("ws/v1/getDocumentByDamId");
    }

///////////////////////// Passing Test Cases /////////////////////////////
    @Test (groups = { "endpoints" })
    public void DocByDamIDAndVersionValidWs() throws ParseException,
            org.json.simple.parser.ParseException, IOException, SQLException, CustomException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            checkEndpoint("/ws/v1", callTypes.DVValid);
    }

    @Test (groups = { "endpoints" })
    public void DocByDamIDAndVersionValidServ() throws ParseException,
            org.json.simple.parser.ParseException, IOException, SQLException,  CustomException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            checkEndpoint("/services/api", callTypes.DVValid);
    }

    @Test (groups = { "endpoints" })
    public void DocByDamIDValidWs() throws ParseException, IOException, SQLException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            checkEndpointNoVersion("/ws/v1", callTypes.DValid);
    }

    @Test (groups = { "endpoints" })
    public void DocByDamIDValidServ() throws ParseException, IOException, SQLException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            checkEndpointNoVersion("/services/api", callTypes.DValid);
    }


/////////////////////// Failing Test Cases /////////////////////////////////////
    @Test (groups = { "endpoints" })
    public void DocByDamIDVerBadDWs() throws SQLException, IOException, org.json.simple.parser.ParseException
    {   checkBadEndpointData("/ws/v1", callTypes.DVBadDamId);    }

    @Test (groups = { "endpoints" })
    public void DocByDamIDVerBadDServ() throws SQLException, IOException, org.json.simple.parser.ParseException
    {   checkBadEndpointData("/services/api", callTypes.DVBadDamId);    }

    @Test (groups = { "endpoints" })
    public void DocByDamIDVerBadVWs() throws SQLException, IOException, org.json.simple.parser.ParseException
    {   checkBadEndpointData("/ws/v1", callTypes.DVBadCV);    }

    @Test (groups = { "endpoints" })
    public void DocByDamIDVerBadVServ() throws SQLException, IOException, org.json.simple.parser.ParseException
    {   checkBadEndpointData("/services/api", callTypes.DVBadCV);    }

    @Test (groups = { "endpoints" })
    public void DocByDamIDBadDWs() throws IOException
    {   checkBadEndpointDataNoVersion("/ws/v1");    }

    @Test (groups = { "endpoints" })
    public void DocByDamIDBadDServ() throws IOException
    {   checkBadEndpointDataNoVersion("/services/api");    }



    ////////////////////////////// Test Case Guts ////////////////////////////////////////
    private void checkEndpoint(String type, callTypes ct)throws ParseException,
            org.json.simple.parser.ParseException, IOException, SQLException, CustomException
    {

        rs = JDBCUtils.getRandomRows("content_publish", 1);
        StringBuilder url = new StringBuilder(Constants.baseURL + type);
        String docId = "";
        if (rs.next())
        {
            if(ct == callTypes.DVValid) {
                url.append("/getDocumentByDamIdAndVersion?");
                url.append("damId=" + rs.getString("dam_id"));
                url.append("&version=" + rs.getString("content_version"));
            }
            docId = rs.getString("document_id");
        }
        rs.close();
        if(url.toString().contains("null"))
            return;
        String response = NetUtils.getHTML(url.toString());
        if(response.length() == 0)
            throw new CustomException("HTML didn't return a response...\n" + url.toString());

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        String status = (String) jsonObject.get("status");
        Assert.assertEquals(status.compareTo("succeeded"), 0, "Status didn't come in succeeded for the html call - " + url.toString());

        JSONObject responseObj = (JSONObject) jsonObject.get("response");
        if(responseObj == null)
            throw new CustomException("the response is null?\n" + url.toString());

        //Data to output so that you can understand
        //System.out.println("json Object - " + jsonObject.toString() + "\n" + url.toString());

        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(docId + "");
        rs = JDBCUtils.getResultSet("select document_id, l.lang_name LanguageName, au.preferred_name pn, drm.uri URI\n" +
                "    from document d\n" +
                "    join language l on language_id = l.language_code\n" +
                "    natural join document_rollup_mvw drm\n" +
                "    left join app_user au on d.owner_app_user_id = au.app_user_id\n" +
                "where document_id=?", fillIn);
        String dbuserName = null, dbLangName = null, dburi = null;
        if(rs.next())
        {
            dbuserName = rs.getString("pn");
            dbLangName = rs.getString("LanguageName");
            dburi = rs.getString("uri");
        }
        rs.close();

        String responseapp_User = (String)responseObj.get("appUser");
        Assertion(responseapp_User, dbuserName, "appUser");
        String responselangName = (String)responseObj.get("language");
        Assertion(responselangName, dbLangName, "language");
        String responseuri = (String)responseObj.get("uri");
        Assertion(responseuri, dburi, "uri");
    }


    private void checkBadEndpointData(String type, callTypes ct) throws SQLException, IOException,
            org.json.simple.parser.ParseException
    {
        StringBuilder sb = new StringBuilder(Constants.baseURL + type);
        String badDamId = "2rb57810692291bepq1f2eb395yf07d89eea4d4a";

        switch(ct)
        {
            case DVBadDamId:
                sb.append("/getDocumentByDamIdAndVersion?damId=" + badDamId);
                sb.append("&version=1");
                break;
            case DVBadCV:  //selected a random damId from the test database
                sb.append("/getDocumentByDamIdAndVersion?damId=27bea810692291be771f2eb3950f07d89eea4d40");
                sb.append("&version=2000");
                break;
            default: //case DBadDamId:
                sb.append("/getDocumentByDamId?damId=" + badDamId);
                break;
        }
        String response = NetUtils.getHTML(sb.toString());

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        String status = (String) jsonObject.get("status");
        Assertion(status, "failed", "Status");
        String error = (String) jsonObject.get("error");
        Assertion(error, "The requested document could not be located in the Published Content repository", "Error Message");
    }

    private void checkEndpointNoVersion(String type, callTypes ct)throws ParseException, IOException, SQLException {

        rs = JDBCUtils.getRandomRows("content_publish", 1);
        StringBuilder url = new StringBuilder(Constants.baseURL + type);
        String docId = "";
        if (rs.next()) {
            if (ct == callTypes.DValid) {
                url.append("/getDocumentByDamId?");
                url.append("damId=" + rs.getString("dam_id"));
            }
            docId = rs.getString("document_id");
        }

        rs.close();
        if (url.toString().contains("null"))
            return;

        String response = NetUtils.getHTML(url.toString());
        int statusCode = NetUtils.getResponseStatus(url.toString());
        System.out.println(statusCode);
        Assert.assertTrue(response.length() == 0 && statusCode == 404);

    }

    private void checkBadEndpointDataNoVersion(String type) throws IOException
    {
        StringBuilder sb = new StringBuilder(Constants.baseURL + type);
        String badDamId = "2rb57810692291bepq1f2eb395yf07d89eea4d4a";

        sb.append("/getDocumentByDamId?damId=" + badDamId);
        String response = NetUtils.getHTML(sb.toString());
        int statusCode = NetUtils.getResponseStatus(sb.toString());
        System.out.println(statusCode);
        Assert.assertTrue(response.length() == 0 && statusCode == 404);
    }

    private void Assertion(String first, String second, String type)
    {
        if(first == null || second == null)
            Assert.assertEquals(first, second, type + " - not both were null \nResponse:" + first + " \tDB:" + second+ "\n");
        else
            Assert.assertEquals(first.compareTo(second), 0, type + " - not both were the same  \nResponse:" + first + " \tDB:" + second + "\n");
    }

    @AfterMethod (alwaysRun = true)
    public void cleanup() throws SQLException
    {
        JDBCUtils.closeUp();
        rs.close();
    }
}
