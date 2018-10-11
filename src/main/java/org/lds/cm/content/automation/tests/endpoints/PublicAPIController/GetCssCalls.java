package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class GetCssCalls
{

    private static final String previewCssQueryById = "select * from preview_css where preview_css_id=?";
    private SoftAssert sa = new SoftAssert();

    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException
    {
        APIRules.fixAPIRule("ws/v1/getPreviewCss");
        APIRules.fixAPIRule("ws/v1/getPrintCss");
        APIRules.fixAPIRule("ws/v1/getAllCss");
    }
    /////////////////////// Passing Test Cases ////////////////////////
//All of them are run twice (the if, assert) way around because there are subtle differences between the lanes
    @Test (groups = { "endpoints" })
    public void getPreviewCssWs()  throws IOException, SQLException
    {
        if(!getPreviewValidCssTests("/ws/v1"))
        {
            sa.assertTrue(getPreviewValidCssTests("/ws/v1"), "Made 2 getPreviewCss attempts and both failed");
        }
    }

    @Test (groups = { "endpoints" })
    public void getPreviewCssServ()  throws IOException, SQLException
    {
        if(!getPreviewValidCssTests("/services/api"))
        {
            sa.assertTrue(getPreviewValidCssTests("/services/api"), "Made 2 getPreviewCss attempts and both failed");
        }
    }

    @Test (groups = { "endpoints" })
    public void getPrintCssWs() throws SQLException
    {
        if(!getPrintValidCssTests("/ws/v1"))
        {
            sa.assertTrue(getPrintValidCssTests("/ws/v1"), "Made 2 getPrintCss attempts and both failed");
        }
    }

    @Test (groups = { "endpoints" })
    public void getPrintCssServ() throws SQLException
    {
        if(!getPrintValidCssTests("/services/api"))
        {
            sa.assertTrue(getPrintValidCssTests("/services/api"), "Made 2 getPrintCss attempts and both failed");
        }

    }


    ///////////////////// Failing Css Calls ///////////////////////////////////
    @Test (groups = { "endpoints" })
    public void getPreviewCssWsBad() throws IOException
    {        getCssTestBad(Constants.baseURL + "/ws/v1/getPreviewCss?cssId=0");    }

    @Test (groups = { "endpoints" })
    public void getPreviewCssServBad() throws IOException
    {        getCssTestBad(Constants.baseURL + "/services/api/getPreviewCss?cssId=0");    }

    @Test (groups = { "endpoints" })
    public void getPrintCssWsBad() throws IOException
    {        getCssTestBad(Constants.baseURL + "/ws/v1/getPrintCss?cssId=0");    }

    @Test (groups = { "endpoints" })
    public void getPrintCssServBad() throws IOException
    {        getCssTestBad(Constants.baseURL + "/services/api/getPrintCss?cssId=0");    }



    private void getCssTestBad(String url) throws IOException
    {
        String response = NetUtils.getHTML(url);
        org.springframework.util.Assert.isTrue(response != null, "get returned null");
        org.springframework.util.Assert.isTrue(response.length() == 0, "response contained messaging");
    }

    private boolean getPreviewValidCssTests(String service)  throws IOException, SQLException
    {
        String url = Constants.baseURL + service + "/getPreviewCss?cssId=";
        ArrayList<String> errors = new ArrayList<String>();

        //previewcss table only have 10 rows (3/21/18)
        ResultSet rs = JDBCUtils.getRandomRows("preview_css", 3);

        while (rs.next()) {
            String newUrl = url + rs.getInt("preview_css_id");
            String response = NetUtils.getHTML(newUrl);
            org.springframework.util.Assert.isTrue(response != null, "getPreviewCss returned null");
            org.springframework.util.Assert.isTrue(response.length() > 0, "getPreviewCss didn't return information");
            //if the response doesn't match what is in the database add it to the errors arraylist
            if (!verifyResponse(response, rs.getClob("preview_css")))
                errors.add(newUrl + "\t");

        }
        rs.close();
        //if there are errors, make a string of them and output them
        if(errors.size() > 0) {
            System.out.println(ErrorUtils.stringify(errors));
            return false;
        }
        return true;
    }

    private boolean getPrintValidCssTests(String service) throws SQLException
    {
        String url = Constants.baseURL + service + "/getPrintCss?cssId=";
        ArrayList<String> errors = new ArrayList<String>();

        ResultSet rs = JDBCUtils.getRandomRows("preview_css", 3);  //previewcss table only have 10 rows (3/21/18)

        while (rs.next()) {
            String newUrl = url + rs.getInt("preview_css_id");
            try {
                String response = NetUtils.getHTML(newUrl);
                org.springframework.util.Assert.isTrue(response != null, "get returned null");
                //response.length = 0 when there's nothing, but the clob is null in the db
                if (response.length() == 0 && rs.getClob("print_css") == null)
                    newUrl = "awesome..."; //break doesn't work here cause it finishes the while loop.  Just do something to make it passable
                else {
                    org.springframework.util.Assert.isTrue(response.length() > 0, "getPrintCss didn't return information");
                    if (!verifyResponse(response, rs.getClob("print_css")))
                        errors.add(newUrl + "\t");
                }
            } catch (Exception e) {
                //if an error was thrown add all information to the error arraylist
                errors.add(newUrl + "\n" + e.getMessage());
            }
        }
        rs.close();

        //if there are errors, make a string of them and output them
        if(errors.size() > 0) {
            System.out.println(ErrorUtils.stringify(errors));
            return false;
        }
        return true;
    }

    //does its best, but unfortunately there's a bit of difference between the lanes
    private boolean verifyResponse(String response, Clob clob) throws IOException, SQLException
    {
        //response come with extra newline spaces to make it easier to read.  Not necessary in the db
        String change = response.replace("\n", "");
        String clobData = JDBCUtils.clobToString(clob);

        //remove the -test from all urls in the css (some point to test, others to prod etc.......)
        if (Constants.environment != "prod") {
            String lookFor = "-" + Constants.environment;
            clobData = clobData.replace(lookFor, ".");
            change = change.replace(lookFor, ".");
        }

        //database has boxes and html code returns ?
        clobData = clobData.replace("□", "?");

        //if the strings aren't the same print them out for comparisson
        if (change.compareTo(clobData) != 0) {
            //System.out.println("Database - " + clobData + "\nChange - " + change + "\nComparison = " + change.compareTo(clobData));
            return false;
        }
        return true;
    }





    //if we ever see getAllCss as bringing back more then 100 items, I suggest we just compare counts and spot check
    @Test (groups = { "endpoints" })
    public void getAllCssWs()throws SQLException, ParseException, IOException
    {
        String url = Constants.baseURL + "/ws/v1/getAllCss";
        getAllCssVerification(url);
    }

    @Test (groups = { "endpoints" })
    public void getAllCssServ()throws SQLException, ParseException, IOException
    {
        String url = Constants.baseURL + "/services/api/getAllCss";
        getAllCssVerification(url);
    }

    private void getAllCssVerification(String url) throws SQLException, ParseException, IOException
    {
        String response = NetUtils.getHTML(url);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) jsonObject.get("previewCssFiles");
        for(int i = 0; i < jsonArray.size(); i++){
            verifyAllCssResponse(jsonArray.get(i).toString());
        }
        Assert.isTrue(response != null, "getAllCss returned null");
        Assert.isTrue(response.length() > 0, "getAllCss didn't return anything in the response");
    }

    //Can't use the JSON Parser apparently . . .  need to redo...
    private boolean verifyAllCssResponse(String x) throws SQLException, ParseException
    {
//            System.out.println(x);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(x);

        Long id = (Long) jsonObject.get("id");
        String name = (String) jsonObject.get("name");
        String contentType = (String) jsonObject.get("contentType");
        String url = (String) jsonObject.get("url");

        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(id + "");
        ResultSet rs = JDBCUtils.getResultSet(previewCssQueryById, fillIn);

        ArrayList<String> errors = new ArrayList<String>();
        while(rs.next())
        {
            if(rs.getString("preview_css_id").compareTo(id.toString()) != 0)
                errors.add("ids are not equivalent");
            if(url.compareTo(Constants.baseURL + "/ws/v1/getPreviewCss?cssId=" + id) != 0)
                errors.add("url was not correct");
            if(name.compareTo(rs.getString("name")) != 0)
                errors.add("names don't matach");
            if(rs.getString("content_type_id") == null)
            {
                if(contentType.length() > 0)
                    errors.add("content types are not both null");
                //if it is 0 then it is fine
            }
            else
            {
                try {
                    ArrayList<String> fillInData = new ArrayList<>();
                    fillInData.add(contentType);
                    ResultSet rs2 = JDBCUtils.getResultSet("select content_type_id from content_type where content_type_name=?", fillInData);
                    while (rs2.next())
                        if(rs.getInt("content_type_id") != rs2.getInt("content_type_id"))
                            errors.add("content types don't match");
                    rs2.close();
                }
                catch(Exception e){}
            }
        }
        rs.close();
        if(errors.size() == 0)
            return true;
        System.out.println(ErrorUtils.stringify(errors));
        return false;
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp()  {  JDBCUtils.closeUp();   }

}
