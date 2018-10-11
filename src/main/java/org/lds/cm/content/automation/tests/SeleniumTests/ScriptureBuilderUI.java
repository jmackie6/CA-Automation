package org.lds.cm.content.automation.tests.SeleniumTests;

import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.RoleModels.Editor;
import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.tests.APIRules.APIRulesStaticMethods;
import org.lds.cm.content.automation.util.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.Pages.ScriptureLink;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScriptureBuilderUI
{
    /** The smoke test case suggests to go to lds.org/scriptures+url, these don't do that because verifying
     *  that would be difficult */

    private static NormanLevy norman;
    private static WebDriver webDriver;

    @BeforeMethod (alwaysRun = true)
    public static void setUp() throws SQLException, InterruptedException
    {
        webDriver = PreTestSetup.setup();
        norman = new NormanLevy();
        if(!norman.hasClass("Contributor") && !norman.hasClass("Editor"))
            RoleAssignment.assignRole(norman, new Editor());

        APIRulesStaticMethods.fixAPIRule("ws/v1/generateScriptureRefs");
    }

    @AfterMethod (alwaysRun = true)
    public static void close()
    {
        webDriver.close();
        JDBCUtils.closeUp();
    }

    @AfterClass (alwaysRun = true)
    public static void cleanUp()
    {
        webDriver.quit();
        JDBCUtils.closeUp();
    }

    @Test (groups={"selenium"}, timeOut=180000)
    public void ScriptureBuilderDandC() throws IOException, SQLException
    {
        startSBTests();
        ArrayList<String> details = new ArrayList<>();
        details.add("Doctrine and Covenants");
        details.add("Doctrine and Covenants");
        details.add("138");
        details.add("2");
        finishEasyTests(details);
        String query = details.get(1) + " " + details.get(2) + ":" + details.get(3);
        verifyEasyLink(query);
    }

    @Test (groups={"selenium"}, timeOut=180000)
    public void ScriptureBuilderOT() throws IOException, SQLException
    {
        startSBTests();
        ArrayList<String> details = new ArrayList<>();
        details.add("Old Testament");
        details.add("Job");
        details.add("1");
        details.add("37");
        finishEasyTests(details);
        String query = details.get(1) + " " + details.get(2) + ":" + details.get(3);
        verifyEasyLink(query);
    }

    /** May 1, 2018 - for some reason the chapter section under JST is seen as Introduction by the automation
     *      so it isn't able to select the chapter correctly*/
    public void ScriptureBuilderJST() throws IOException, SQLException
    {
        startSBTests();
        ArrayList<String> details = new ArrayList<>();
        details.add("Joseph Smith Translation Appendix");
        details.add("JST, Mark");
        details.add("9");
        details.add("2");
        finishEasyTests(details);
        String query = details.get(1) + " " + details.get(2) + ":" + details.get(3);
        verifyEasyLink(query);
    }

   /** May 1, 2018 - same issues as JST*/
    public void ScriptureBuilderPoGP() throws IOException, SQLException
    {
        startSBTests();
        ArrayList<String> details = new ArrayList<>();
        details.add("Pearl of Great Price");
        details.add("Moses");
        details.add("5");
        details.add("13");
        finishEasyTests(details);
        String query = details.get(1) + " " + details.get(2) + ":" + details.get(3);
        verifyEasyLink(query);
    }

    @Test (groups={"selenium"}, timeOut=180000)
    public void ScriptureBuilderTC() throws InterruptedException, SQLException
    {
        startSBTests();
        ArrayList<String> details = new ArrayList<>();
        details.add("Triple Combination");
        details.add("Title Page");
        details.add("2");
        finishMissingBookTests(details);
        verifyMissingLink(1);
    }

    @Test (groups={"selenium"}, timeOut=180000)
    public void ScriptureBuilderHarmony() throws SQLException
    {
        startSBTests();
        ArrayList<String> details = new ArrayList<>();
        details.add("Harmony of the Gospels");
        details.add("Ⅳ. The Last Week: Atonement and Resurrection");
        finishMissingTestamentTests(details);
        verifyMissingLink(2);
    }

    private void startSBTests() throws SQLException
    {
        Login.login(webDriver, norman.getUsername(), norman.getPassword());
        SetupTests.waitForLoading(webDriver);
        WebElement element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        element.click();
        element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.ScriptureLinkButtonXpath)));
        element.click();
    }

    /** 'Easy' tests are ones that have full arraylists  */
    private void finishEasyTests(ArrayList<String> details)
    {
        ScriptureLink.selectTestament(webDriver, details.get(0));
        BrowserUtils.sleep(500);
        ScriptureLink.selectBook(webDriver, details.get(1));
        ScriptureLink.selectChapter(webDriver, details.get(2));
        ScriptureLink.enterVerses(webDriver, details.get(3));
    }

    private void verifyEasyLink(String query) throws IOException
    {
        String urlCall = Constants.baseURL + "/ws/v1/generateScriptureRefs?source=" + query;
        String response = NetUtils.getHTML(urlCall.replace(" ", "%20"));
        String link = ScriptureLink.getURI(webDriver);
        Assert.isTrue(response.contains(link), "html call and scripture builder have different links\nHTML Response-" +
                response + "\nRefBuilder Link" + link);
    }

    private void finishMissingBookTests(ArrayList<String> details) throws InterruptedException
    {
        ScriptureLink.selectTestament(webDriver, details.get(0));
        ScriptureLink.selectChapter(webDriver, details.get(1));
        Thread.sleep(500);
        ScriptureLink.enterVerses(webDriver, details.get(2));
    }

    /** This test is misbehaving... The put nothing into the verses box solves the problem for some reason  */
    private void finishMissingTestamentTests(ArrayList<String> details)
    {
        ScriptureLink.selectBook(webDriver, details.get(0));
        ScriptureLink.selectChapter(webDriver, details.get(1));
        ScriptureLink.enterVerses(webDriver, "");
    }

    /**Not entirely sure how to generate the scripture ref for the strange combinations
     *
     * type 1 - Title Page
     *  type 2 - Harmony of the Gospels
     *  default is an oops
     *
     */
    private void verifyMissingLink(int type)
    {
        String link = ScriptureLink.getURI(webDriver);
        switch(type)
        {
            case 1:
                Assert.isTrue(link.contains("/scriptures/triple/title-page.html?verse=2#p2"), "html call and scripture builder have different links" + "\nLink - " + link);
                break;
            case 2:
                Assert.isTrue(link.contains("/scriptures/harmony/harmony-8.html"), "html call and scripture builder have different links" + "\nLink - " + link);
                break;
            default:
                assert false;
                break;
        }
    }
}
