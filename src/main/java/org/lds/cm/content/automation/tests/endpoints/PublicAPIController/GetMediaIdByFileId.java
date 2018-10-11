package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.lds.cm.content.automation.util.XMLUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetMediaIdByFileId {
       /**Find Media Media will pull fileIds from the cover_art table and these will have info in the HTML responses
     * Find Media Doc will pull fileIds from the document table and these will have empty wierd HTML responses
     */

       @BeforeClass  (alwaysRun=true)
       public void setUp() throws SQLException, InterruptedException
       {
           APIRules.fixAPIRule("ws/v1/getMediaIdsByFileId");
       }

/////////////////////// Passing Test Cases //////////////////////////////////////
    @Test (groups = { "endpoints" })
    public void findMediaIdByFileIdPassMediaWs() throws IOException, SQLException
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            validCACalls("/ws/v1");
    }

    @Test (groups = { "endpoints" })
    public void findMediaIdByFileIdPassDocWs() throws IOException, SQLException
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            validDocCalls("/ws/v1");
    }

    @Test (groups = { "endpoints" })
    public void findMediaIdByFileIdPassMediaServ() throws IOException, SQLException
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            validCACalls("/services/api");
    }

    @Test (groups = { "endpoints" })
    public void findMediaIdByFileIdPassDocServ() throws IOException, SQLException
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            validDocCalls("/services/api");
    }


//////////////////////////// Failed Test Cases //////////////////////////////
    @Test (groups = { "endpoints" })
    public void findMediaIdByFileIdBadWs() throws IOException, SQLException
    {    badCalls("/ws/v1");    }

    @Test (groups = { "endpoints" })
    public void findMediaIdByFileIdBadServ() throws IOException, SQLException
    {    badCalls("/services/api");    }


////////////////////////// Test Case Guts ////////////////////////////////

    public void validCACalls(String type) throws IOException, SQLException
    {
        ResultSet rs = null;
        String fileId = "";

        rs = JDBCUtils.getRandomRows("cover_art", 1);
        if (rs.next())
            fileId = (rs.getString("file_id"));
        rs.close();

        String urlCall = Constants.baseURL + type + "/getMediaIdsByFileId?fileId=" + fileId;
        String response = NetUtils.getHTML(urlCall);

        String id = "//@fileId";
        NodeList ep_file_id = XMLUtils.getNodeListFromXpath(response, id, null);
        int responsecount = ep_file_id.getLength();
        String respondedFileId = ep_file_id.item(0).getNodeValue();

        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(respondedFileId);
        rs = JDBCUtils.getResultSet("select count(*) as count from cover_art where file_id=?", fillIn);
        int dbcount = 0;
        if(rs.next())
            dbcount = rs.getInt("count");
        rs.close();

        Assert.assertEquals(responsecount, dbcount, "Counts didn't match\nResponse count - " + responsecount + "\tDatabase count - " + dbcount);
    }

    public void validDocCalls(String type) throws IOException, SQLException
    {
            String fileId = "";

            ResultSet rs = JDBCUtils.getRandomRows("document", 1);
            if (rs.next())
                fileId = (rs.getString("file_id"));
            rs.close();

            String urlCall = Constants.baseURL + type + "/getMediaIdsByFileId?fileId=" + fileId;
            String response = NetUtils.getHTML(urlCall);

            String id = "//@fileId";
            NodeList ep_file_id = XMLUtils.getNodeListFromXpath(response, id, null);
            int responsecount = ep_file_id.getLength();
            int dbcount = 0;
            String respondedFileId = "";
            if (responsecount > 0) {
                respondedFileId = ep_file_id.item(0).getNodeValue();

                ArrayList<String> fillIn = new ArrayList<>();
                fillIn.add(respondedFileId);
                rs = JDBCUtils.getResultSet("select count(*) as count from cover_art where file_id=?", fillIn);
                if (rs.next())
                    dbcount = rs.getInt("count");
                rs.close();
            }
            Assert.assertEquals(responsecount, dbcount, "Counts didn't match");
    }

    public void badCalls(String type) throws IOException, SQLException
    {
        String urlCall = Constants.baseURL + type + "/getMediaIdsByFileId?fileId=PX1234_5678_1049";
        String response = NetUtils.getHTML(urlCall);

        String id = "//@fileId";
        NodeList ep_file_id = XMLUtils.getNodeListFromXpath(response, id, null);
        int responsecount = ep_file_id.getLength();

        Assert.assertEquals(responsecount, 0, "Counts didn't match.  Html returned - " + responsecount + " for call to " + urlCall);
    }


    @AfterMethod(alwaysRun=true)
    public void cleanUp()
    {   JDBCUtils.closeUp();    }
}
