package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentTypesTest {

    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException
    {	APIRules.fixAPIRule("ws/v1/contentTypes");	}

    @Test (groups = { "endpoints" })
    public void checkContentTypesWs() throws Exception
    {        checkContentTypeIds("/ws/v1");    }

    @Test (groups = { "endpoints" })
    public void checkContentTypesServ() throws Exception
    {        checkContentTypeIds("/services/api");    }

    public static void checkContentTypeIds(String type) throws IOException, SQLException {
        //System.out.println("\nChecking that the content type ids from the endpoint and DB match\n");

        ResultSet rs = JDBCUtils.getResultSet("SELECT * FROM CONTENT_TYPE");
        // array of content type ids
        String epContentTypes = NetUtils.getHTML(Constants.baseURL + type + "/contentTypes");
        String numbers = epContentTypes.replaceAll("[^0-9]", "");
        String[] content_type_ids = numbers.split("");
        Arrays.sort(content_type_ids);

        System.out.println("\nContent Type ID's from endpoint: " + Arrays.toString(content_type_ids));

        // get list of content type ids from database
        List<String> dbContentTypeIds = new ArrayList<>();

        while (rs.next()) {
            String dbContentTypeId = rs.getString(1);
            dbContentTypeIds.add(dbContentTypeId);
        }
        rs.close();
        System.out.println("\nContent Type ID's from DB: " + dbContentTypeIds);

        // compare endpoint and database content type ids

        int num = 0;
        for (int i = 0; i < content_type_ids.length; i++) {
            if (content_type_ids[i].equals(dbContentTypeIds.get(i))) {
                System.out.println("\nEndpoint ID: " + content_type_ids[i] + " matches database ID: " + dbContentTypeIds.get(i));
            } else {
                num++;
            }
        }

        Assert.assertTrue(num == 0, "\nOne or more of the endpoint/database ID's did NOT match\n");

    }

    @AfterClass(alwaysRun = true)
    public void cleanup()	{	JDBCUtils.closeUp();	}
}
