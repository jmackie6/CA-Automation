package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.springframework.util.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

public class ProcessCount
{
    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException
    {
        APIRules.fixAPIRule("ws/v1/processCount");
    }

    @Test (groups = { "endpoints" })
    public void processCountWs() throws NumberFormatException, IOException
    {
        String url = Constants.baseURL + "/ws/v1/processCount";
        verification(url);
    }

    @Test (groups = { "endpoints" })
    public void processCountServ() throws NumberFormatException, IOException
    {
        String url = Constants.baseURL + "/services/api/processCount";
        verification(url);
    }

    private void verification(String url) throws NumberFormatException, IOException
    {
        String response = NetUtils.getHTML(url);
        //System.out.println(response);
        Assert.isTrue(response != null, "Call returned null");
        Assert.isTrue(response.contains("Running background processes: "), "doesn't contain correct messaging\n" + response);

        //parse the last number to see if it is an integer.  If it isn't it will throw a NumberFormatException
        Integer.parseInt(response.substring(response.length() - 1));
    }
}
