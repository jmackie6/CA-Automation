package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.lds.cm.content.automation.util.XMLUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class GetMatchingFileIds {

    @BeforeClass (alwaysRun=true)
    public void setUp() throws SQLException, InterruptedException
    {
        APIRules.fixAPIRule("ws/v1/getAllMatchingFileIDs");
    }

    @Test (groups = { "endpoints" })
    public void getMatchFileIdsWs() throws IOException, SQLException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            matchFileIdsPassing("/ws/v1");
    }

    @Test (groups = { "endpoints" })
    public void getMatchFileIdsServ() throws IOException, SQLException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            matchFileIdsPassing("/services/api");
    }

    @Test (groups = { "endpoints" })
    public void getMatchFileIdsWsBad() throws IOException, SQLException
    {     matchFileIdsBad("/ws/v1" );
        matchFileIdsBadEmpty("/ws/v1");
    }

    @Test (groups = { "endpoints" })
    public void getMatchFileIdsServBad() throws IOException, SQLException
    {     matchFileIdsBad("/services/api");
        matchFileIdsBadEmpty("/services/api");
    }

    private void matchFileIdsPassing(String type) throws IOException, SQLException
    {
        String fileId = "";
        ResultSet rs = JDBCUtils.getRandomRows("document", 1);
        if(rs.next())
            fileId = rs.getString("File_id");
        rs.close();

        fileId = trim(fileId);

        String response = NetUtils.getHTML(Constants.baseURL + type + "/getAllMatchingFileIDs?fileId=" + fileId);
        String id = "//@id";
        NodeList ep_file_id = XMLUtils.getNodeListFromXpath(response, id, null);
        ArrayList<String> responseFileIds = new ArrayList<String>();
        for (int i = 0; i < ep_file_id.getLength(); i++)
            responseFileIds.add(ep_file_id.item(i).getNodeValue());


        ResultSet rs2 = JDBCUtils.getResultSet("select file_id from document where file_id like '" + fileId + "%'");
        ArrayList<String> databaseStrings = new ArrayList<>();
        while(rs2.next())
            databaseStrings.add(rs2.getString("file_id"));
        rs2.close();

        Collections.sort(responseFileIds);
        Collections.sort(databaseStrings);

        org.springframework.util.Assert.isTrue(responseFileIds.size() == databaseStrings.size(), "Database findings size and response findings size don't match \n"
                + "html response size - " + responseFileIds.size() + "\t database size - " + databaseStrings.size());
        for(int i =0; i < responseFileIds.size(); i++)
            org.springframework.util.Assert.isTrue(responseFileIds.get(i).compareTo(databaseStrings.get(i)) == 0, "File Ids don't match\nresponse"
                    + responseFileIds.get(i) + "\t Database - " + databaseStrings.get(i));
    }

    private void matchFileIdsBad(String type) throws IOException, SQLException
    {

        String fileId = "";
        ResultSet rs = JDBCUtils.getRandomRows("document", 1);
        if(rs.next())
            fileId = rs.getString("File_id");
        rs.close();

        fileId = fileId.substring(0, fileId.indexOf('_'));


        String response = NetUtils.getHTML(Constants.baseURL + type + "/getAllMatchingFileIDs?fileId=" + fileId);
        org.springframework.util.Assert.isTrue(response.contains("<files>Error:"), "Returned too much information  - " + response);
    }

    private void matchFileIdsBadEmpty(String type) throws IOException, SQLException
    {
        String response = NetUtils.getHTML(Constants.baseURL + type + "/getAllMatchingFileIDs?fileId=");
        org.springframework.util.Assert.isTrue(response.compareTo("<files></files>") == 0, "Returned too much information  - " + response);
    }

    //take a fileid from the database and keep the file_id first part and language second part
    private String trim(String fileId)
    {
        int count = 0;
        StringBuilder sb = new StringBuilder();

        for(char x: fileId.toCharArray()) {
            if(x == '_')
                count++;
            if(count <= 1)
                sb.append(x);
        }
        return sb.toString();
    }


    @AfterMethod(alwaysRun=true)
    public void cleanup()
    {   JDBCUtils.closeUp();    }
}
