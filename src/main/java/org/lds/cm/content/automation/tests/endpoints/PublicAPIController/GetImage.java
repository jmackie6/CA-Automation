package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetImage
{
    /**How the Oxygen value is displayed in the url
     *   - inurl = call the getImageOxygen call
     *   - valueTrue = call the getImage call with oxygen=true
     *   - valueFalse = call the getImage call with oxygen=false
     *   - missing = call the getImage without the oxygen value
     */
    private enum OxygenType
    {
        inUrl, valueTrue, valueFalse, missing;
    }

    @AfterMethod (alwaysRun=true)
    public void cleanup()	{	JDBCUtils.closeUp();	}


    @BeforeClass (alwaysRun=true)
    public void setUp() throws SQLException, InterruptedException
    {
        APIRules.fixAPIRule("ws/v1/getImage");
        APIRules.fixAPIRule("ws/v1/getImageOxygen");
    }

/////////////////////  Valid Calls ws/v1  ////////////////////////////////////////////
    @Test (groups = { "endpoints" })
    public void getImageValidTeleWs()throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/ws/v1", OxygenType.inUrl, false);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTitanWs()throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/ws/v1", OxygenType.inUrl, true);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTitanWsVT() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/ws/v1", OxygenType.valueTrue, true);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTeleWsVT() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/ws/v1", OxygenType.valueTrue, false);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTitanWsVF() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/ws/v1", OxygenType.valueFalse, true);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTeleWsVF() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/ws/v1", OxygenType.valueFalse, false);
    }


    @Test (groups = { "endpoints" })
    public void getImageValidTitanWsMiss() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/ws/v1", OxygenType.missing, true);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTeleWsMiss() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/ws/v1", OxygenType.missing, false);
    }






    ////////////////////  Valid Calls services/api  ////////////////////////////////////////////
    @Test (groups = { "endpoints" })
    public void getImageValidTeleServ()throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/services/api", OxygenType.inUrl, false);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTitanServ()throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/services/api", OxygenType.inUrl, true);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTitanServVT() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/services/api", OxygenType.valueTrue, true);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTeleServVT() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/services/api", OxygenType.valueTrue, false);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTitanServVF() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/services/api", OxygenType.valueFalse, true);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTeleServVF() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/services/api", OxygenType.valueFalse, false);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTitanServMiss() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/services/api", OxygenType.missing, true);
    }

    @Test (groups = { "endpoints" })
    public void getImageValidTeleServMiss() throws Exception
    {
        for(int i = 0; i < Constants.numOfTestsToRun; i++)
            getImageValid("/services/api", OxygenType.missing, false);
    }





    ////////////////////  All Bad Calls  ////////////////////////////////////////////
    @Test (groups = { "endpoints" })
    void getImageBadWs() throws Exception
    { getImageBad("/ws/v1", OxygenType.inUrl); }

    @Test (groups = { "endpoints" })
    void getImageBadServ() throws Exception
    { getImageBad("/services/api", OxygenType.inUrl); }

    @Test (groups = { "endpoints" })
    void getImageBadWsVT() throws Exception
    { getImageBad("/ws/v1", OxygenType.valueTrue); }

    @Test (groups = { "endpoints" })
    void getImageBadServVT() throws Exception
    { getImageBad("/services/api", OxygenType.valueTrue); }

    @Test (groups = { "endpoints" })
    void getImageBadWsVF() throws Exception
    { getImageBad("/ws/v1", OxygenType.valueFalse); }

    @Test (groups = { "endpoints" })
    void getImageBadServVF() throws Exception
    { getImageBad("/services/api", OxygenType.valueFalse); }

    @Test (groups = { "endpoints" })
    void getImageBadWsMiss() throws Exception
    { getImageBad("/ws/v1", OxygenType.missing); }

    @Test (groups = { "endpoints" })
    void getImageBadServMiss() throws Exception
    { getImageBad("/services/api", OxygenType.missing); }



//////////////////// Guts of all the Calls ///////////////////////////////

    /**
     * @param type - String value for what api you are hitting.  /ws/v1 or /services/api
     * @param ot - how the url will be designed.  (see enum explanation)
     * @param useTitan - look for a random mediaId that is Titan guid long, or 7 digit short
     * @throws Exception
     */
    private void getImageValid(String type, OxygenType ot, boolean useTitan) throws SQLException, IOException
    {
        StringBuilder sb = new StringBuilder(Constants.baseURL + type + "/getImage");

        StringBuilder query = new StringBuilder("select * from (select * from cover_art order by dbms_random.value) " +
                "where rownum <= 1 and Length(media_id)");
        if (useTitan)
            query.append(" > 8");
        else
            query.append(" < 10");


        ResultSet rs = JDBCUtils.getResultSet(query.toString());
        String mediaId = "";
        while (rs.next()) {
            mediaId = rs.getString("media_id");
        }
        rs.close();

        if (ot == OxygenType.inUrl)
            sb.append("Oxygen?mediaId=" + mediaId);
        else if (ot == OxygenType.valueTrue)
            sb.append("?mediaId=" + mediaId + "&oxygen=true");
        else if (ot == OxygenType.valueFalse)
            sb.append("?mediaId=" + mediaId + "&oxygen=false");
        else //ot == OxygenType.missing
            sb.append("?mediaId=" + mediaId);

        String response = NetUtils.getHTML(sb.toString());

        Assert.isTrue(response.length() > 0, "Nothing was returned");
        Assert.isTrue(!response.contains("The website is temporarily down"), "Webiste is down . . . try again later");
        //
        Assert.isTrue(response.contains("PNG") || response.contains("JPG") || response.contains("IMG"),
                "Image didn't contain traditional file ending (png, jpg or img).\n" + response);
    }

    private void getImageBad(String type, OxygenType ot) throws IOException
    {
        String mediaId = "1234567a";
        StringBuilder sb = new StringBuilder(Constants.baseURL + type + "/getImage");
        if(ot == OxygenType.inUrl)
            sb.append("Oxygen?mediaId=" + mediaId);
        else if(ot == OxygenType.valueTrue)
            sb.append("?mediaId=" + mediaId + "&oxygen=true");
        else if(ot == OxygenType.valueFalse)
            sb.append("?mediaId=" + mediaId + "&oxygen=false");
        else //ot == OxygenType.missing
            sb.append("?mediaId=" + mediaId);

        String response = NetUtils.getHTML(sb.toString());
        Assert.isTrue(response.length() == 0, "Something was returned for bad call - " + sb.toString() + "\nresponse - "  + response);
        Assert.isTrue(!response.contains("The website is temporarily down"), "Website is down . . . try again later");
    }
}
