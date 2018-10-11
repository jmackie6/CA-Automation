package org.lds.cm.content.automation.tests.RolesTests;

import org.lds.cm.content.automation.enums.Pages;
import org.lds.cm.content.automation.model.RoleModels.*;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.UserManagement;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.Logout;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.*;

public class RolesMenuTesting
{
    private WebDriver webdriver;
    private ArrayList<String> passes;
    private Map<Pages, String> urls;
    private boolean selected = false;
    private NormanLevy norman;
    private FonoSaia fono;


    /** Setup and Clean up methods */
    //This is called before any method in this class is run.  It is only run once
    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException
    {
        fono = new FonoSaia();
        //If fono doesn't have the ICS_Admin role
        if(!(fono.hasClass("ICS_ADMIN")))
            RoleAssignment.assignRole(fono, new ICS_Admin());

        norman = new NormanLevy();

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

    //This method needs to be called at the beginning of every method to set up the menu buttons...
    //not called before the lock, unlock, approver, or roleless because they can't access the menu
    public void setUpMethods(RolesBaseClass rbc) throws InterruptedException
    {
        webdriver = PreTestSetup.setup();
        selected = false;
        passes = new ArrayList<>();
    }

    //Called after every method.  Just close the webdriver so that setupMethods doesn't break.
    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        webdriver.quit();
        JDBCUtils.closeUp();
    }


/** Each role needed a different method so that each can be run individually     */
    @Test (groups={"roles"})
    public void viewer() throws InterruptedException, SQLException
    {
        Viewer view = new Viewer();
        setUpMethods(view);
        redundancy(view);
    }

    @Test (groups={"roles"})
    public void ics_Admin() throws InterruptedException, SQLException
    {
        ICS_Admin ics = new ICS_Admin();
        setUpMethods(ics);
        redundancy(ics);
    }

    @Test (groups={"roles"})
    public void ics_support() throws InterruptedException, SQLException
    {
        Ics_Support ics = new Ics_Support();
        setUpMethods(ics);
        redundancy(ics);
    }

    @Test (groups={"roles"})
    public void contributor() throws InterruptedException, SQLException
    {
        Contributor contributor = new Contributor();
        setUpMethods(contributor);
        redundancy(contributor);
    }

    @Test (groups={"roles"})
    public void editor() throws InterruptedException, SQLException
    {
        Editor editor = new Editor();
        setUpMethods(editor);
        redundancy(editor);
    }

    @Test (groups={"roles"})
    public void projectManager() throws InterruptedException, SQLException
    {
        ProjectManager pm = new ProjectManager();
        setUpMethods(pm);
        redundancy(pm);
    }

    @Test (groups={"roles"})
    public void publisher() throws InterruptedException, SQLException
    {
        Publisher publisher = new Publisher();
        setUpMethods(publisher);
        redundancy(publisher);
    }

    @Test (groups={"roles"})
    public void manager() throws InterruptedException, SQLException
    {
        Manager manager = new Manager();
        setUpMethods(manager);
        redundancy(manager);
    }

    @Test (groups={"roles"})
    public void lock() throws InterruptedException, SQLException
    {
        Lock lock = new Lock();
        setUpMethods(lock);
        redundancyBlock(lock);
    }

    @Test (groups={"roles"})
    public void unlock() throws InterruptedException, SQLException
    {
        Unlock unlock = new Unlock();
        setUpMethods(unlock);
        redundancyBlock(unlock);
    }

    @Test(groups={"roles"})
    public void printScenarios() throws InterruptedException, SQLException
    {
        Print_Scenarios ps = new Print_Scenarios();
        setUpMethods(ps);
        redundancyBlock(ps);
    }

    @Test(groups={"roles"})
    public void psdAdmin() throws InterruptedException, SQLException
    {
        Psd_Admin psdAmin = new Psd_Admin();
        setUpMethods(psdAmin);
        redundancyBlock(psdAmin);
    }

    @Test (groups={"roles"})
    public void approver() throws InterruptedException, SQLException
    {
        Approver approver = new Approver();
        setUpMethods(approver);
        redundancyBlock(approver);
    }

    @Test (groups={"roles"})
    public void legacy() throws InterruptedException, SQLException
    {
        Legacy legacy = new Legacy();
        setUpMethods(legacy);
        redundancyBlock(legacy);
    }

    //Valid log in, but not roles have been associated yet
    @Test (groups={"roles"})
    public void roleless() throws InterruptedException, SQLException
    {
        Roleless roleless = new Roleless();
        setUpMethods(roleless);
        redundancyBlock(roleless);
    }

    /** To avoid redundant code for each role.  The role name is passed to this method to do all the work */
    private void redundancy(RolesBaseClass rbc)  throws InterruptedException, SQLException
    {
        String rolesURL = TestMenuUI(rbc);
        String extraMenuButtonsTest = TestExtraButtons(rbc);
        String finale = "";

        if(rolesURL.length() != 0 || extraMenuButtonsTest.length() != 0)
            finale = "Roles Report -  - - - - - - - - - - - - -\n" + rolesURL + "\n" + extraMenuButtonsTest;

        //if the final report has no length pass the test, if not the error message will have the report
        Assert.isTrue(finale.length() == 0, "\n"+finale);
    }


    private String stringifyTMUI(ArrayList<Pages> accessiblePages)
    {
        ArrayList<Pages> copy = accessiblePages;
        for(int i = 0; i < copy.size(); i++)
        {
            for(int j = 0; j < passes.size(); j++)
            {
                if((passes.get(j)).compareTo(copy.get(i).getUrl()) == 0)
                {
                    copy.remove(i);
                    passes.remove(j);
                    j = passes.size();
                    i--;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        if(copy.size() > 0) {
            sb.append("should/couldn't access these: \n");
            for (Pages x : copy)
                sb.append(x.getUrl() + "\n");
        }
        if(passes.size() > 0) {
            sb.append("Shouldn't/could access these :\n");
            for (String x : passes)
                sb.append(x + "\n");
        }

        //System.out.println("copysize + " + copy.size() + "   passes.size " + passes.size());
        return sb.toString();
    }


/**UI Testing, verify that the correct buttons are in the menu */
    private String TestMenuUI(RolesBaseClass rbc) throws InterruptedException, SQLException
    {
        if(!norman.onlyClass(rbc.getRole_id())) {
            Login.login(webdriver, fono.getUsername(), fono.getPassword());
            SetupTests.waitForLoading(webdriver);
            UserManagement.giveUserSingleRole(webdriver, rbc.getName(), norman);
            SetupTests.waitForLoading(webdriver);
            Logout.logout(webdriver);
        }
        boolean hasClass = norman.onlyClass(rbc.getRole_id());
        if (rbc.getName().compareTo("ROLELESS") == 0)
            hasClass = true;
        if (hasClass)
            Login.login(webdriver, norman.getUsername(), norman.getPassword());
        SetupTests.waitForLoading(webdriver);

        passes = new ArrayList<>();


        Iterator it = urls.entrySet().iterator();
        while (it.hasNext()) {
            Pages x = (Pages) ((Map.Entry) (it.next())).getKey();
            try {
                clickMenuButton(x);
                Thread.sleep(1000);

                int elements = webdriver.findElements(By.xpath(x.getMenuButton())).size();
                if(elements != 0)
                    passes.add(x.getUrl());
            }  catch(Exception e){}
        }

        return stringifyTMUI(rbc.getPageAccess());
    }

    /** For certain pages the Rerports tab needs to be clicked because they aren't under the main menu*/
    private void clickMenuButton(Pages p)
    {
        if ((p == Pages.ValidationReport || p == Pages.PublishHistory || p == Pages.ContentReport || p == Pages.DatabaseReport)
                && selected)
        {
            WebElement element = (new WebDriverWait(webdriver, 5))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.ReportMenuButtonXpath)));
            selected = false;
            element.click();
        } else if (!selected) {
            WebElement element = (new WebDriverWait(webdriver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
            element.click();
            selected = true;
        }
    }

    private void redundancyBlock(RolesBaseClass rbc) throws InterruptedException, SQLException
    {
        if(!norman.onlyClass(rbc.getRole_id())) {
            Login.login(webdriver, fono.getUsername(), fono.getPassword());
            SetupTests.waitForLoading(webdriver);
            UserManagement.giveUserSingleRole(webdriver, rbc.getName(), norman);
            SetupTests.waitForLoading(webdriver);
            Logout.logout(webdriver);
        }
        boolean hasClass = norman.onlyClass(rbc.getRole_id());
        if (rbc.getName().compareTo("ROLELESS") == 0)
            hasClass = true;
        if (hasClass)
            Login.login(webdriver, norman.getUsername(), norman.getPassword());

        try {
            List<WebElement> mainMenuButtons = (new WebDriverWait(webdriver, 10))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
            Assert.isTrue(mainMenuButtons.size() == 0,
                    "Was able to hit the Main Menu Button");
        }
        catch(Exception e) /** If the above call throws a time out then it is caught here and should pass the test*/
        {}
    }

    private String TestExtraButtons(RolesBaseClass rbc) throws InterruptedException
    {
        String response = "";
        //click the menu button if not clicked already
        if(!selected)
            (new WebDriverWait(webdriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath))).click();

        int elements = webdriver.findElements(By.xpath(MenuButtonConstants.AuthorBuilderButtonXpath)).size();

        boolean icsTrue = (rbc.getName().compareToIgnoreCase("ics_admin") == 0) || (rbc.getName().compareToIgnoreCase("ics_support") == 0);
        boolean shouldHaveBtn =(rbc.getName().compareToIgnoreCase("Contributor") == 0 || rbc.getName().compareToIgnoreCase("Editor") == 0);

        if(elements!=0) {
            if (!shouldHaveBtn && !icsTrue)
                response += "Able to Access Author Builder with " + rbc.getName() + "\n";
        }
        else
            if(shouldHaveBtn || icsTrue)
                response += "Unable to Access Author Builder with " + rbc.getName() + "\n";


        elements = webdriver.findElements(By.xpath(MenuButtonConstants.ScriptureLinkButtonXpath)).size();
        if(elements!=0) {
            if (!shouldHaveBtn)
                response += "Able to Access Scripture Builder with " + rbc.getName() + "\n";
        }
        else
            if (shouldHaveBtn)
                response += "Unable to Access Scripture Builder with " + rbc.getName() + "\n";

        return response;
    }


}
