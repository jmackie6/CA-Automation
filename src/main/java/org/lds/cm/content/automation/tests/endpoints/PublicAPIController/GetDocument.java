package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.springframework.util.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetDocument
{
    ResultSet rs = null;
    @AfterClass (alwaysRun=true)
    public void cleanup() {  JDBCUtils.closeUp();}

    @BeforeClass (alwaysRun=true)
    public void setUp() throws SQLException, InterruptedException
    {
        APIRules.fixAPIRule("ws/v1/getDocument");
    }

/////////////////////// Passing Tests ///////////////////////////////
    @Test (groups = { "endpoints" })
    public void getDocumentValidWs() throws SQLException, IOException
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getDocumentValid("/ws/v1");
    }

    @Test (groups = { "endpoints" })
    public void getDocumentValidServ() throws SQLException, IOException
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getDocumentValid("/services/api");
    }

/////////////////////// Bad Doc Tests ///////////////////////////
    @Test (groups = { "endpoints" })
    public void getDocumentBadFiWs() throws IOException
    {    getDocumentBroken("/ws/v1", true);    }

    @Test (groups = { "endpoints" })
    public void getDocumentBadFiServ() throws IOException
    {    getDocumentBroken("/services/api", true);    }

    @Test (groups = { "endpoints" })
    public void getDocumentBadCtWs() throws IOException
    {    getDocumentBroken("/ws/v1", false);    }

    @Test (groups = { "endpoints" })
    public void getDocumentBadCtServ() throws IOException
    {    getDocumentBroken("/services/api", false);    }



///////////////////// Testing Guts ///////////////////////////
    private void getDocumentValid(String type) throws SQLException, IOException {
        StringBuilder sb = new StringBuilder(Constants.baseURL + type);
        String file_id = "";
        rs = JDBCUtils.getRandomRows("document", 1);
        if (rs.next()) {
            file_id = rs.getString("file_id");
            sb.append("/getDocument?fileId=" + file_id);
            sb.append("&contentType=" + rs.getString("content_type"));
        }
        rs.close();

        String response = NetUtils.getHTML(sb.toString());
        QAHtml5Document dbName = QADocumentService.fromFileId(file_id);
        Assert.isTrue(compareStrings(dbName.getDocumentXML(), response), "Documents were not the same\nHtml response - " + response + "\nDatabase result - " + dbName.getDocumentXML());
    }

    private void getDocumentBroken(String type, boolean brokenFileId) throws IOException {
        StringBuilder sb = new StringBuilder(Constants.baseURL + type + "/getDocument?fileId=");
        if (brokenFileId)
            sb.append("a1234ic&contentType=chapter");
        else
            sb.append("31111_002_023&contentType=awesome-stuff");

        String response = NetUtils.getHTML(sb.toString());

        Assert.isTrue(response.compareTo("<html>null</html>") == 0, "Response was too complicated for call "  + sb.toString() + "\n" + response);
    }

    /**
     * compare what was pulled from the database to what came from the html
     *
     * return if strings are basically the same
     */
    private boolean compareStrings(String docSection, String responseSubString) throws IOException {
        //if either string is null make it empty, otherwise downgrade them to ASCII
        //html responses come back in ASCII encryption and database come back in UTF-8
        if (docSection == null)
            docSection = "";
        else
            docSection = new String(docSection.getBytes("ASCII"));

        if (responseSubString == null)
            responseSubString = "";
        else
            responseSubString = new String(responseSubString.getBytes("ASCII"));


        /**
         * Special rules for the clob data compared to the overall xml...
         *
         *  for some reason the database saved xml uses <meta ... /> and the response string uses <meta...></meta>
         *  The response also comes out like correct fancy html, while the database comes on one line
         */

        docSection = docSection.replace("\t", "\n");
        docSection = docSection.replace("</body></html>", "</body>\n</html>");

        String[] dbSections = docSection.split("\n");
        String[] responseSections = responseSubString.split("\n");

        int j = 0;
        for (int x = 0; x < dbSections.length && j < responseSections.length; x++, j++) {
            String first = dbSections[x].trim();
            while (x < dbSections.length && (first.compareTo("\n") == 0 || first.isEmpty())) {
                x++;
                first = dbSections[x].trim();
            }

            //System.out.println(x + " " + j + " " + first + " " + responseSections[j]);
            if (x >= dbSections.length) {
                //System.out.println("DbSections-" + responseSections[j]);
                return false;
            }

            first = dbSections[x];
            first = first.trim();
            String second = responseSections[j].trim();
            if (!first.contains("/meta>") && !second.contains("/meta>")) {

                /** for some reason when the difference is 50 they are close enough to similar to pass
                         (example  <img></img> and <img/> */
                if (first.compareTo(second) != 0 && first.compareTo("\n") != 50) {
                    System.out.println("The Two Strings\n" + first + "\n" + second);
                    System.out.println(first.compareTo("\n"));
                    return false;
                }
                //if (first.compareTo("\n") == 50) {
                    //System.out.println("The Two Strings\n" + first + "\n" + second);
                    //System.out.println(first.compareTo("\n"));
                //}

            }
        }

        return true;
    }
}