package org.lds.cm.content.automation.tests.RolesTests;

import org.lds.cm.content.automation.enums.Pages;
import org.lds.cm.content.automation.model.RoleModels.*;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.model.UserModels.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.UserManagement;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.Logout;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SetupTests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class RolesURLTesting
{
    private final int timer = 500000;
    private WebDriver webdriver;
    private ArrayList<String> passes;
    private Map<Pages, String> urls;
    private NormanLevy norman;
    private FonoSaia fono;

/** Setup and Clean up methods */
    //This is called before any method in this class is run.  It is only run once
    @BeforeClass (alwaysRun = true, timeOut = timer)
    public void setUp() throws SQLException, InterruptedException
    {
        fono = new FonoSaia();
        //If fono doesn't have the ICS_Admin role he can't reassign roles to norman for these tests
        if(!(fono.hasClass("ICS_ADMIN")))
            RoleAssignment.assignRole(fono, new ICS_Admin());

        norman = new NormanLevy();
        fono = new FonoSaia();

        /**originally this hasmap was used to associated urls to buttons on the page.  This can be reduced to an arraylist
         *  of type pages, but that would mean refactoring most of the code
         */

        urls = new HashMap<>();
        urls.put(Pages.Dashboard, Pages.Dashboard.getPageName());
        urls.put(Pages.Transform, Pages.Transform.getPageName());
        urls.put(Pages.ValidationReport, Pages.ValidationReport.getPageName());
        urls.put(Pages.PublishHistory, Pages.PublishHistory.getPageName());
        urls.put(Pages.ContentReport, Pages.ContentReport.getPageName());
        urls.put(Pages.Settings, Pages.Settings.getPageName());
        urls.put(Pages.LinkSearch, Pages.LinkSearch.getPageName());
        urls.put(Pages.ManageUsers, Pages.ManageUsers.getPageName());
        urls.put(Pages.ManageGroups, Pages.ManageGroups.getPageName());
        urls.put(Pages.ManageRoles, Pages.ManageRoles.getPageName());
        urls.put(Pages.Brightcove, Pages.Brightcove.getPageName());
        urls.put(Pages.HelpResources, Pages.HelpResources.getPageName());
        urls.put(Pages.DownloadPDF, Pages.DownloadPDF.getPageName());
        urls.put(Pages.ContentDownload, Pages.ContentDownload.getPageName());
        urls.put(Pages.QueueConfig, Pages.QueueConfig.getPageName());
        urls.put(Pages.UserConfig, Pages.UserConfig.getPageName());
        urls.put(Pages.AppConfig, Pages.AppConfig.getPageName());
        urls.put(Pages.ManageJavascript, Pages.ManageJavascript.getPageName());
        urls.put(Pages.ManageCSS, Pages.ManageCSS.getPageName());
        urls.put(Pages.ManageCoverArt, Pages.ManageCoverArt.getPageName());
        urls.put(Pages.MarkupValidation, Pages.MarkupValidation.getPageName());
        urls.put(Pages.ManageTransforms, Pages.ManageTransforms.getPageName());
        urls.put(Pages.MediaXML, Pages.MediaXML.getPageName());
        urls.put(Pages.BackgroundProcesses, Pages.BackgroundProcesses.getPageName());
        urls.put(Pages.ManageSecurity, Pages.ManageSecurity.getPageName());
        urls.put(Pages.ContentTemplates, Pages.ContentTemplates.getPageName());
        urls.put(Pages.OxygenStyleSheets, Pages.OxygenStyleSheets.getPageName());
        urls.put(Pages.CreateManifests, Pages.CreateManifests.getPageName());
        urls.put(Pages.ContentTypeManagement, Pages.ContentTypeManagement.getPageName());
        urls.put(Pages.VideoCache, Pages.VideoCache.getPageName());
        urls.put(Pages.DatabaseReport, Pages.DatabaseReport.getPageName());
    }

    //This method is called before every method.  Just set up the webdriver and clear the passes array
    @BeforeMethod (alwaysRun = true)
    public void setUpMethods()  throws InterruptedException
    {
        webdriver = PreTestSetup.setup();
        passes = new ArrayList<>();
    }

    //Called after every method.  Just close the webdriver so that setupMethods doesn't break.
    @AfterMethod (alwaysRun = true)
    public void cleanup()
    {
        webdriver.quit();
        JDBCUtils.closeUp();
    }

/** Each role needed a different method so that each can be run individually     */
    //try to hit all of the urls without logging in
    @Test (groups={"roles"}, timeOut = timer)
    public void unAuthenticated()
    {
        accessURLS();
        Assert.isTrue(passes.size() == 0, stringify());
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void viewer() throws InterruptedException, SQLException
    {
        Viewer view = new Viewer();
        redundancy(view);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void ics_Admin() throws InterruptedException, SQLException
    {
        ICS_Admin ics = new ICS_Admin();
        redundancy(ics);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void ics_support() throws InterruptedException, SQLException
    {
        Ics_Support ics = new Ics_Support();
        redundancy(ics);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void contributor() throws InterruptedException, SQLException
    {
        Contributor contributor = new Contributor();
        redundancy(contributor);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void editor() throws InterruptedException, SQLException
    {
        Editor editor = new Editor();
        redundancy(editor);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void projectManager() throws InterruptedException, SQLException
    {
        ProjectManager pm = new ProjectManager();
        redundancy(pm);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void publisher() throws InterruptedException, SQLException
    {
        Publisher publisher = new Publisher();
        redundancy(publisher);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void manager() throws InterruptedException, SQLException
    {
        Manager manager = new Manager();
        redundancy(manager);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void lock() throws InterruptedException, SQLException
    {
        Lock lock = new Lock();
        redundancy(lock);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void unlock() throws InterruptedException, SQLException
    {
        Unlock unlock = new Unlock();
        redundancy(unlock);
    }

    @Test(groups={"roles"}, timeOut=timer)
    public void psdAdmin() throws InterruptedException, SQLException
    {
        Psd_Admin psd_admin = new Psd_Admin();
        redundancy(psd_admin);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void approver() throws InterruptedException, SQLException
    {
        Approver approver = new Approver();
        redundancy(approver);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void legacy() throws InterruptedException, SQLException
    {
        Legacy legacy = new Legacy();
        redundancy(legacy);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void print_scenarios() throws InterruptedException, SQLException
    {
        Print_Scenarios print = new Print_Scenarios();
        redundancy(print);
    }

    //Valid log in, but not roles have been associated yet
    @Test (groups={"roles"}, timeOut = timer)
    public void roleless() throws InterruptedException, SQLException
    {
        Roleless roleless = new Roleless();
        redundancy(roleless);
    }

/** To avoid redundant code for each role.  The role name is passed to this method to do all the work */
    private void redundancy(RolesBaseClass rbc) throws InterruptedException, SQLException
    {
        String rolesURL = rolesURLHittingTest(rbc);
        String finale = "";

        if(rolesURL.length() != 0)
            finale = "Roles Report -  - - - - - - - - - - - - -\n" + rolesURL;

        //if the final report has no length pass the test, if not the error message will have the report
        Assert.isTrue(finale.length() == 0, ""+finale);
    }

    /** 3 Methods used to test hitting urls */
    //guts of the test cases named after roles.
    //Set up Norman to have only that role and try to hit all of the URLS
    private String rolesURLHittingTest(RolesBaseClass rbc) throws InterruptedException, SQLException
    {
        if (!norman.onlyClass(rbc.getRole_id())) {
            Login.login(webdriver, fono.getUsername(), fono.getPassword());
            SetupTests.waitForLoading(webdriver);
            UserManagement.giveUserSingleRole(webdriver, rbc.getName(), norman);
            Logout.logout(webdriver);
        }
        boolean hasClass = norman.onlyClass(rbc.getRole_id());
        if (rbc.getName().compareTo("ROLELESS") == 0)
            hasClass = true;

        if (hasClass) {
            Login.login(webdriver, norman.getUsername(), norman.getPassword());
            accessURLS();
            return stringifyPartial(rbc.getPageAccess(), rbc.getName());
        } else {
            return "There was an issue setting Norman up.";
        }
    }

    //run through the urlList created in @BeforeClass method
    private void accessURLS()
    {
        Iterator it = urls.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            String response = testURL(((Pages)pair.getKey()).getUrl(), (String)pair.getValue());
            if(response.length() != 0)
                passes.add(response);
        }
    }

    /**build a url, go to the page, and compare the title of the page to what it should be
        if you can find it add it to the pass arraylist, otherwise return an empty string
     */
    private String testURL(String url, String button)
    {
        String destination = Constants.baseURL + url;
        webdriver.get(destination);
        String text = "";
        try {
            int x = (new WebDriverWait(webdriver, 10))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("page-header"))).size();
            if (x != 0)
                text = webdriver.findElement(By.className("page-header")).getText();

            if (text.contains(button))
                return url;
        } catch (Exception e){}
        return "";
    }


/**2 methods to put together error message strings that allow you to know which pages were/weren't supposed
        to be accessed.  I'm sure the logic can reduce this to 1 method, but for now it is 2. */
    private String stringifyPartial(ArrayList<Pages> urlList, String roleName)

    {
        for(int i =0; i < passes.size(); i++)
        {
            for (int j = 0; j < urlList.size(); j++)
            {
                if(passes.get(i).compareTo(urlList.get(j).getUrl()) == 0)
                {
                    urlList.remove(j);
                    passes.remove(i);
                    i--;
                    j = urlList.size();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        if(urlList.size() > 0) {
            sb.append("should/couldn't access these with " + roleName + ":\n");
            for (Pages x : urlList)
                sb.append(x.getUrl() + "\n");
        }
        if(passes.size() > 0) {
            sb.append("Shouldn't/could access these without " + roleName + ":\n");
            for (String x : passes)
                sb.append(x + "\n");
        }
        return sb.toString();
    }

    private String stringify()
    {
        StringBuilder sb = new StringBuilder("Was not able to access these pages with ICSAdmin rights:\n");
        for (String x : passes)
            sb.append(x + "\n");
        return sb.toString();
    }

}
