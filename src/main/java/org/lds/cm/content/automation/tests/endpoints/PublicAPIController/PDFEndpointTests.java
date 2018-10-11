package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.lds.cm.content.automation.util.ErrorUtils.stringify;

/**  April 2018 - Did only get calls, not post call */
public class PDFEndpointTests
{
    @Test(groups="endpoints")
    public void getPDF() throws SQLException, IOException
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getPDFCall();
    }

    @Test(groups="endpoints")
    public void getPDFMissing() throws SQLException, IOException
    {
        String processId = "", uri = "", pdfFile = "";

        ResultSet rs = JDBCUtils.getRandomRows("pdf", 1);
        if(rs.next())
        {
            processId = rs.getString("process_log_id");
            pdfFile = rs.getString("pdf_id");
            uri = rs.getString("uri");
        }
        rs.close();

        ArrayList<String> errors = new ArrayList<>();

        String badPDF = Constants.baseURL + "/ws/v1/pdfFile?pdfFileId=0&processLogId=" + processId;
        String response = NetUtils.getHTML(badPDF);
        if(response.compareTo(uri) != 0)
            errors.add("Uri was returned for " + badPDF + "\nHtml Response - " + response + "\nDatabase - " + uri);

   //Apparently missing data works...
        String missingPDF = Constants.baseURL + "/ws/v1/pdfFile?processLogId=" + processId;
        response = NetUtils.getHTML(missingPDF);
        if(response.compareTo(uri) != 0)
            errors.add("Uri was returned for " + missingPDF + "\nHtml Response - " + response + "\nDatabase - " + uri);

        String missingProcessID = Constants.baseURL + "/ws/v1/pdfFile?pdfFileId=" + pdfFile;
        response = NetUtils.getHTML(missingProcessID);
        if(response.compareTo(uri) != 0)
            errors.add("Uri was returned for " + missingProcessID + "\nHtml Response - " + response + "\nDatabase - " + uri);

        Assert.isTrue(errors.size() == 0, ErrorUtils.stringify(errors));
    }

    @Test(groups="endpoints")
    public void getPDFBad() throws IOException, SQLException
    {
        String pdfFile = "";
        ResultSet rs = JDBCUtils.getRandomRows("pdf", 1);
        if(rs.next())
        {
            pdfFile = rs.getString("pdf_id");
        }
        rs.close();
        ArrayList<String> errors = new ArrayList<>();
        String badCall = Constants.baseURL + "/ws/v1/pdfFile";
        String response = NetUtils.getHTML(badCall);
        if(response.contains("/"))
            errors.add("Uri was returned for " + badCall + "\n" + response);

        String badProcess = Constants.baseURL + "/ws/v1/pdfFile?processLogId=0&pdfFileId=" + pdfFile;
        response = NetUtils.getHTML(badProcess);
        if(response.contains("/"))
            errors.add("Uri was returned for " + badProcess + "\n" + response);

        Assert.isTrue(errors.size() == 0, ErrorUtils.stringify(errors));
    }

    private void getPDFCall() throws SQLException, IOException
    {
        String fileURI = "";
        StringBuilder sb = new StringBuilder(Constants.baseURL + "/ws/v1/pdfFile?");
        ResultSet rs = JDBCUtils.getRandomRows("pdf", 1);
        if(rs.next())
        {
            sb.append("processLogId=" + rs.getInt("process_log_id"));
            sb.append("&pdfFileId=" + rs.getInt("pdf_id"));
            fileURI = rs.getString("uri");
        }
        rs.close();
        String response = NetUtils.getHTML(sb.toString());
        Assert.isTrue(response.compareTo(fileURI) == 0, "database and response don't match\n"
                + sb.toString() + "\n" + response + "\n" + fileURI);
    }


    @AfterMethod
    public void cleanUp()
    {
        JDBCUtils.closeUp();
    }

}
