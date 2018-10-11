package org.lds.cm.content.automation.tests.endpoints.OxygenAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.springframework.util.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetEditTrackingId
{
    private String badCallResponse="We are sorry, there is a technical problem and we are unable" +
            " to process your sign in right now. Please try again later.";

    @Test (groups = { "endpoints" })
    public void getEditTrackingIdValid() throws SQLException, IOException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            getEditTrackingId();
    }

    private void getEditTrackingId() throws SQLException, IOException
    {
        StringBuilder sb = new StringBuilder(Constants.baseURL + "/oxygen/getEditTrackingId?");
        String query = "select * from (select document_id from document order by dbms_random.value) where rownum <= ?";
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add("1");
        ResultSet rs = JDBCUtils.getResultSet(query, fillInData);
        if(rs.next())
            sb.append("docid=" + rs.getString("document_id"));
        rs.close();
        rs = JDBCUtils.getRandomRows("app_user", 1);
        if(rs.next())
            sb.append("&uid=" + rs.getString("app_user_id"));
        rs.close();

        int statusCode = NetUtils.getResponseStatus(badCallResponse);
        Assert.isTrue(statusCode == 200, "Failing test since we did not get a 200 back from the call");
        String response = NetUtils.getHTML(sb.toString());

        Assert.isTrue(response.contains(badCallResponse), "Bad response came back for call " + sb.toString() + "\nResponse - " + response);
    }

    @AfterClass (alwaysRun = true)
    public void cleanup()	{	JDBCUtils.closeUp();	}
}
