package org.lds.cm.content.automation.tests.SeleniumTests.Dashboard_Searches;

import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.RoleModels.Editor;
import org.lds.cm.content.automation.model.RoleModels.ICS_Admin;
import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.Logout;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SeleniumUtil.Search;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.lds.cm.content.automation.util.LogUtils.*;

public class Search_Timing {
    private static final Logger LOG = getLogger();
    private WebDriver webDriver;
    private NormanLevy norman;//setup verifies that he doesn't have icsadmin
    private FonoSaia fono; //setup verifies that he has icsadmin

    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException, CustomException {
        norman = new NormanLevy();
        fono = new FonoSaia();
        ICS_Admin ics = new ICS_Admin();

        webDriver = PreTestSetup.setup();
        if (norman.hasClass(ics.getName()) || !norman.hasUsableClass())
        {    RoleAssignment.assignRole(norman, new Editor()); }
        if (!fono.hasClass(ics.getName()))
        {    RoleAssignment.assignRole(fono, new ICS_Admin()); }
    }

    @AfterMethod (alwaysRun = true)
    public void cleanUp()
    {
        webDriver.close();
        JDBCUtils.closeUp();
    }

    @Test (groups={"selenium"}, timeOut=300000)
    public void smoke_SearchTest_Timing() throws SQLException {
        long time1 = icsSearch();
        Logout.logout(webDriver);
        long time2 = nonIcsSearch();

        int difference = (int) (time1 - time2);

        LOG.info("{} - {} = {}", time1, time2, difference);

        Assert.isTrue(difference <= 2 && difference >= -2, "There was a difference greater than 2 seconds");
    }

    private long icsSearch() throws SQLException{
        Login.login(webDriver, fono.getUsername(), fono.getPassword());
        return makeAllSearches();
    }

    private long nonIcsSearch() throws SQLException {
        Login.login(webDriver, norman.getUsername(), norman.getPassword());
        return makeAllSearches();
    }

    private long makeAllSearches() {

        long startTime = System.nanoTime();
        makeSearch("/general-conference/2000");
        long finishTime = System.nanoTime();
        long first = finishTime - startTime;

        startTime = System.nanoTime();
        makeSearch("/manual/primary-5");
        finishTime = System.nanoTime();
        long second = finishTime - startTime;

        startTime = System.nanoTime();
        makeSearch("/broadcasts/christmas-devotional");
        finishTime = System.nanoTime();
        long third = finishTime - startTime;

        //timing calls are done in nanoseconds, so a divisor is necessary to put it back into seconds
        long divisor = 1000000000;
        long calculation = (first + second + third) / divisor;
        return calculation / 3;
    }

    //The two webElement calls are there to make it past the "loading" screens.  Will time out after 10 seconds if button can't be found
    public void makeSearch(String uri) {
        SetupTests.waitForLoading(webDriver);
        Search.search(webDriver, uri);
        SetupTests.waitForLoading(webDriver);
    }
}
