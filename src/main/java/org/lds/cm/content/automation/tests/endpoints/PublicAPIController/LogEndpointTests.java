package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/** April 2018 - only did get calls */
public class LogEndpointTests
{
    private String comparison = "The ID you provided does not have a log file associated with it.";
    private String bothBadComparison ="You must provide a process log ID or a log file ID";

    @Test(groups="endpoints")
    public void getLog() throws SQLException, IOException
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getLogCall();
    }

    @Test(groups="endpoints")
    public void getLogMissing() throws SQLException, IOException
    {
        String processId = "", logText = "", logFile = "";

        ResultSet rs = JDBCUtils.getRandomRows("log_file", 1);
        if(rs.next())
        {
            processId = rs.getString("process_log_id");
            logFile = rs.getString("log_file_id");
            logText = rs.getString("log_text");
        }
        rs.close();

        String badLog = Constants.baseURL + "/ws/v1/logFile?logFileId=0&processLogId=" + processId;
        String response = NetUtils.getHTML(badLog);
        Assert.isTrue(response.compareTo(logText) == 0, "Uri was returned for " + badLog + "\n" + response);

        //Apparently missing data works...
        String missingLog = Constants.baseURL + "/ws/v1/logFile?processLogId=" + processId;
        response = NetUtils.getHTML(missingLog);
        Assert.isTrue(response.compareTo(logText) == 0, "Uri was returned for " + missingLog + "\n" + response);

        String missingProcessID = Constants.baseURL + "/ws/v1/logFile?logFileId=" + logFile;
        response = NetUtils.getHTML(missingProcessID);
        Assert.isTrue(response.compareTo(logText) == 0, "Uri was returned for " + missingProcessID + "\n" + response);
    }

    @Test(groups="endpoints")
    public void getLogBad() throws IOException, SQLException
    {
        String logFile = "";
        ResultSet rs = JDBCUtils.getRandomRows("log_file", 1);
        if(rs.next())
        {
            logFile = rs.getString("log_file_id");
        }
        rs.close();

        String badCall = Constants.baseURL + "/ws/v1/logFile";
        String response = NetUtils.getHTML(badCall);
        Assert.isTrue(response.compareTo(bothBadComparison) == 0, "Uri was returned for " + badCall + "\n" + response);

        String badProcess = Constants.baseURL + "/ws/v1/logFile?processLogId=0&logFileId=" + logFile;
        response = NetUtils.getHTML(badProcess);
        Assert.isTrue(response.compareTo(comparison) == 0, "Uri was returned for " + badProcess + "\n" + response);
    }

    private void getLogCall() throws SQLException, IOException
    {
        String fileText = "";
        StringBuilder sb = new StringBuilder(Constants.baseURL + "/ws/v1/logFile?");
        ResultSet rs = JDBCUtils.getRandomRows("log_file", 1);
        if(rs.next())
        {
            sb.append("processLogId=" + rs.getInt("process_log_id"));
            sb.append("&logFileId=" + rs.getInt("log_file_id"));
            fileText = rs.getString("log_text");
        }
        rs.close();
        String response = NetUtils.getHTML(sb.toString());
        Assert.isTrue(response.compareTo(fileText) == 0, "database and response don't match\n"
                + sb.toString() + "\n" + response + "\n" + fileText);
    }


    @AfterMethod
    public void cleanUp()
    {
        JDBCUtils.closeUp();
    }
}
