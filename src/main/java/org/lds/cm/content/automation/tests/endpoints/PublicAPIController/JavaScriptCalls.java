package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.AfterClass;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

//both preview and print javascript url calls
public class JavaScriptCalls
{
    @BeforeClass (alwaysRun=true)
    public void setUp() throws SQLException, InterruptedException
    {
        APIRules.fixAPIRule("ws/v1/getPreviewJavascript");
        APIRules.fixAPIRule("ws/v1/getPrintJavascript");
    }

    @AfterMethod(alwaysRun=true)
    public void cleanUp() throws SQLException
    {
        JDBCUtils.closeUp();
    }

//////////////////////////////// Preview Javascript ////////////////////////////////////////
    @Test (groups = { "endpoints" })
    public void checkPreviewJavascriptWs()throws SQLException, IOException, ClassNotFoundException
    { checkRandomPreviewJavascript("/ws/v1");}

    @Test (groups = { "endpoints" })
    public void checkPreviewJavascriptServ()throws SQLException, IOException, ClassNotFoundException
    { checkRandomPreviewJavascript("/services/api");}

    private void checkRandomPreviewJavascript(String type) throws SQLException, IOException, ClassNotFoundException {

        String query = "select * from (select preview_css_id, javascript_id, file_blob, javascript.deleted_flag from preview_css\n" +
                "left join javascript on preview_css.preview_javascript_id = javascript_id\n" +
                "order by dbms_random.value)"; // where rownum <= 10
        ResultSet rs = JDBCUtils.getResultSet(query);
        while (rs.next()) {
            String deleteFlag = rs.getString("deleted_flag");
            String url = Constants.baseURL + type + "/getPreviewJavascript?cssId=" + rs.getString("preview_css_id");
            CloseableHttpResponse response = NetUtils.getFullHTMLResponse(url);
            Assert.isTrue(response.getStatusLine().getStatusCode() == 200, url + "\t returned an error code");
            if (deleteFlag != null) {
                String blobdeblob = JDBCUtils.blobToString(rs.getBlob("file_blob"));
                //System.out.println(response + "\n\n" + blobdeblob);
                SoftAssert sa = new SoftAssert();
                sa.assertEquals(response.getEntity().toString().compareTo(blobdeblob), 0, "File blobs don't match\nHtml Response - "
                        + response + "\nDatabase - " + blobdeblob);
            } else
//                System.out.println(response.getEntity().getContent());
                org.springframework.util.Assert.isTrue(response.getEntity().getContentLength() == 0,
                        "For html call - " + url + "\nReturned - " + response);
        }
        rs.close();
    }










    //////////////////////////////// Print Javascript ////////////////////////////////////////
    @Test (groups = { "endpoints" })
    public void checkPrintJavascriptWs()  throws SQLException, IOException, ClassNotFoundException
    { checkRandomPrintJavascript("/ws/v1");}

    @Test (groups = { "endpoints" })
    public void checkPrintJavascriptServ() throws SQLException, IOException, ClassNotFoundException
    { checkRandomPrintJavascript("/services/api");}

    private void checkRandomPrintJavascript(String type) throws SQLException, IOException, ClassNotFoundException {
        String query = "select * from (select preview_css_id, javascript_id, file_blob, javascript.deleted_flag from preview_css\n" +
                "left join javascript on preview_css.print_javascript_id = javascript_id\n" +
                "order by dbms_random.value) where rownum <= 3";
        ResultSet rs = JDBCUtils.getResultSet(query);
        while (rs.next()) {
            String deleteFlag = rs.getString("deleted_flag");
            String url = Constants.baseURL + type + "/getPrintJavascript?cssId=" + rs.getString("preview_css_id");
            String response = NetUtils.getHTML(url);
            if (deleteFlag != null) {
                String blobdeblob = JDBCUtils.blobToString(rs.getBlob("file_blob"));
                //System.out.println("Response: " + response + "\n\nBlob\n" + blobdeblob);
                org.springframework.util.Assert.isTrue(response.compareTo(blobdeblob) == 0, "File blobs don't match\nHtml Response - "
                        + response + "\nDatabase - " + blobdeblob);
            } else
                org.springframework.util.Assert.isTrue(response.length() == 0, "For html call - " + url + "\nReturned - " + response);
        }
        rs.close();
    }

}
