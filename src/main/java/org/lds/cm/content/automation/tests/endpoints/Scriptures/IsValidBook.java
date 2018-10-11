package org.lds.cm.content.automation.tests.endpoints.Scriptures;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IsValidBook
{
    private final static String specificBookquery = "select scripture_text, scripture_name, language_id from "
            + "(select * from scripture s, scripture_text st WHERE st.scripture_id = s.scripture_id and "
            + "s.scripture_type = 'BOOK' and s.language_id = ? and s.parent_scripture_id is not null and "
            + "rownum<=1 order by dbms_random.value)";
    private final static String languageQuery = "select distinct Language_id from "
            + "(select * from scripture order by dbms_random.value) where rownum <= 1";

    @AfterMethod (alwaysRun = true)
    public static void closeUp() {JDBCUtils.closeUp();}


    @Test (groups = { "endpoints" })
    public void isValidBookValid() throws SQLException, IOException
    {
        for(int i =0; i < Constants.numOfTestsToRun; i++)
            validCall();
    }

    @Test (groups = { "endpoints" })
    public void isValidBookBad() throws IOException
    {
        inValidCalls();
    }

    @Test (groups = { "endpoints" })
    public void isValidBookMissing() throws IOException
    {
        String missingBook = Constants.baseURL + "/ws/v1/scripture/isValidBook?languageId=000";
        String response = NetUtils.getHTML(missingBook);
        Assert.isTrue(response.compareTo("false") == 0, "Missing Book from call didn't return false\n" + response);
        String missingLanguage = Constants.baseURL + "/ws/v1/scripture/isValidBook?Book=Mormon";
        response = NetUtils.getHTML(missingLanguage);
        Assert.isTrue(response.compareTo("false") == 0, "Missing Language from call didn't return false\n" + response);
    }

    private void validCall() throws SQLException, IOException
    {
        StringBuilder sb = new StringBuilder(Constants.baseURL + "/ws/v1/scripture/isValidBook?languageId=");
        String languageId = "";
        ResultSet rs = JDBCUtils.getResultSet(languageQuery);
        if(rs.next())
            languageId = (String.format("%03d", rs.getInt("language_id")));
        rs.close();

        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(languageId);

        rs = JDBCUtils.getResultSet(specificBookquery, fillInData);
        while(rs.next())
        {
            String book = rs.getString("scripture_text");
            sb.append(languageId + "&book=" + book.replace(" " , "%20"));
            //sb.append("000&book=3%20Nephi");
        }
        rs.close();
        String response = NetUtils.getHTML(sb.toString());

        Assert.isTrue(response.compareTo("true") == 0, "response came back false\n" +  response + "\n" +  sb.toString());
    }

    private void inValidCalls() throws IOException
    {
        String badLanguage = Constants.baseURL + "/ws/v1/scripture/isValidBook?languageId=1001&Book=Moroni";
        String response = NetUtils.getHTML(badLanguage);
        Assert.isTrue(response.compareTo("false") == 0, "Bad Language from call didn't return false\n" + response);
        String badCombo = Constants.baseURL + "/ws/v1/scripture/isValidBook?languageId=922&Book=Moroni";
        response = NetUtils.getHTML(badCombo);
        Assert.isTrue(response.compareTo("false") == 0, "Bad Combination from call didn't return false\n" + response);
        String badBook = Constants.baseURL + "/ws/v1/scripture/isValidBook?languageId=0&Book=Awesomeness";
        response = NetUtils.getHTML(badBook);
        Assert.isTrue(response.compareTo("false") == 0, "Bad Book from call didn't return false\n" + response);
    }
}
