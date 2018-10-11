package org.lds.cm.content.automation.tests.endpoints.OxygenAPIController;

import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.springframework.util.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetOxygenFile
{
    private String DocumentTableCall = "select * from (select * from document order by dbms_random.value) "
            + "where rownum <= 1 and modified_by_app_user_id is not null";
    private String InvalidCallResponse = "403 Access Denied: User does not exist.";

    @Test (groups = { "endpoints" })
    public void getOxygenFileValid() throws SQLException, IOException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            validCallsComplete();
        validCallsCompleteSure();
    }

    @Test (groups = { "endpoints" })
    public void getOxygenValidNoOetid() throws SQLException, IOException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            validCallsNoOetid();
    }

    private void validCallsNoOetid() throws SQLException, IOException
    {
        String oetid = "", fileText = "";
        StringBuilder sb = new StringBuilder(Constants.baseURL + "/ws/v1/oxygenFile?");
        ResultSet rs = JDBCUtils.getResultSet(DocumentTableCall);
        if(rs.next())
        {
            sb.append("uid=" + rs.getInt("modified_by_app_user_id"));
            sb.append("&docid=" + rs.getInt("document_id"));
            oetid = rs.getString("Oxygen_edit_app_user_id");
            fileText = QADocumentService.fromFileId(rs.getString("file_id")).getDocumentXML();
        }
        rs.close();

        String response = NetUtils.getHTML(sb.toString());
        if(oetid == null)
            Assert.isTrue(response.contains(InvalidCallResponse), "Valid call came back\n" + sb.toString() + "\n" + response);
        else //not sure what a valid call looks like at the moment...
            Assert.isTrue(fileText.contains(response.replace("\n", "")), "Didn't match up\nhtml Call - "
                    + sb.toString() + "\nhtml Response - " + response.replace("\n", "") + "\n\nDatabase - " + fileText);
    }

    private void validCallsComplete() throws SQLException, IOException
    {
        int expiredFlag = 0;
        StringBuilder sb = new StringBuilder(Constants.baseURL + "/ws/v1/oxygenFile?");
        String fileText = "";
        String query = "select * from document d, oxygen_edit_tracking o where o.document_id = d.document_id" +
                " and rownum <= 1 order by dbms_random.value";
        ResultSet rs = JDBCUtils.getResultSet(query);
        if(rs.next())
        {
            sb.append("uid=" + rs.getString("app_user_id"));
            sb.append("&docid=" + rs.getString("document_id"));
            sb.append("&oetid=" + rs.getString("oxygen_edit_trk_id"));
            fileText = QADocumentService.fromFileId(rs.getString("file_id")).getDocumentXML();
            expiredFlag = rs.getInt("expired");
        }
        rs.close();
        String response = NetUtils.getHTML(sb.toString());
        if(expiredFlag == 1)
            Assert.isTrue(response.contains(InvalidCallResponse), "Valid call came back for\n" + sb.toString() + "\n" + response);
        else  //The database starts wtih an extra <DOC HTML 1.0> title so I used a contains instead of substring comparison
            Assert.isTrue(fileText.contains(response.replace("\n", "")), "Didn't match up\nhtml Call - "
                    + sb.toString() + "\nhtml Response - "+ response.replace("\n", "") + "\n\nDatabase - " + fileText);
    }

    /** Most rows in the oxygen_edit_tracking table have expired flags that will cause a bad response
     *      this ensures that the expired flag is not set and will return a valid response   */
    private void validCallsCompleteSure() throws SQLException, IOException
    {
        StringBuilder sb = new StringBuilder(Constants.baseURL + "/ws/v1/oxygenFile?");
        String fileText = "";
        String query = "select * from document d, oxygen_edit_tracking o where o.document_id = d.document_id" +
                " and o.expired = 0 and rownum <= 1 order by dbms_random.value";
        ResultSet rs = JDBCUtils.getResultSet(query);
        if(rs.next())
        {
            sb.append("uid=" + rs.getString("app_user_id"));
            sb.append("&docid=" + rs.getString("document_id"));
            sb.append("&oetid=" + rs.getString("oxygen_edit_trk_id"));
            fileText = QADocumentService.fromFileId(rs.getString("file_id")).getDocumentXML();
        }
        rs.close();
        String response = NetUtils.getHTML(sb.toString());
        //The database usually starts wtih an extra <DOC HTML 1.0> title so I used a contains instead of substring comparison
        Assert.isTrue(fileText.contains(response.replace("\n", "")), "Didn't match up\nhtmlCall - "
                    + sb.toString() + "\nhtml Response - "+ response.replace("\n", "") + "\n\nDatabase - " + fileText);
    }

    @AfterMethod(alwaysRun=true)
    public void cleanup()	{	JDBCUtils.closeUp();	}
}
