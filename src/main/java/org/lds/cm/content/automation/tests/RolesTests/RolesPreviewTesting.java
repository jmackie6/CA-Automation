package org.lds.cm.content.automation.tests.RolesTests;

import org.lds.cm.content.automation.model.RoleModels.*;
import org.lds.cm.content.automation.model.UserModels.*;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.*;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.UserManagement;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RolesPreviewTesting
{
    private final int timer = 300000;
    private FonoSaia fono;
    private NormanLevy norman;
    private WebDriver webDriver;
    private ArrayList<String> normallyAccessible;
    private ArrayList<String> pass;
    private String uri = "";

    @BeforeClass (alwaysRun = true, timeOut = timer)
    public void setUp() throws SQLException, InterruptedException
    {
        fono = new FonoSaia();
        //If fono doesn't have the ICS_Admin role he can't reassign roles to norman for these tests
        if(!(fono.hasClass("ICS_ADMIN")))
            RoleAssignment.assignRole(fono, new ICS_Admin());

        norman = new NormanLevy();

        normallyAccessible = new ArrayList<>();
        normallyAccessible.add("Back to Manifest");
        normallyAccessible.add("Prev");
        normallyAccessible.add("Next");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpMethods() throws InterruptedException
    {
        webDriver = PreTestSetup.setup();
        pass = new ArrayList<>();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup()
    {
        webDriver.quit();
        JDBCUtils.closeUp();
    }

    /**  Normal Tests that click the Actions button and see which are available */
    @Test (groups={"roles"}, timeOut = timer)
    public void ics_admin() throws SQLException, InterruptedException
    {
        ICS_Admin role = new ICS_Admin();
        setUp(role);
        hitButtons();
        verification(role);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void ics_support() throws SQLException, InterruptedException
    {
        Ics_Support role = new Ics_Support();
        setUp(role);
        hitButtons();
        verification(role);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void contributor() throws SQLException, InterruptedException
    {
        Contributor role = new Contributor();
        setUp(role);
        hitButtons();
        verification(role);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void editor() throws SQLException, InterruptedException
    {
        Editor role = new Editor();
        setUp(role);
        hitButtons();
        verification(role);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void projectManager() throws SQLException, InterruptedException
    {
        ProjectManager role = new ProjectManager();
        setUp(role);
        hitButtons();
        verification(role);

    }

    @Test (groups={"roles"}, timeOut = timer)
    public void publisher() throws SQLException, InterruptedException
    {
        Publisher role = new Publisher();
        setUp(role);
        hitButtons();
        verification(role);
    }


    /** You can't log in with these test cases, so they are paired with the Viewer class first */
    @Test (groups={"roles"}, timeOut = timer)
    public void approver() throws SQLException, InterruptedException
    {
        Approver role = new Approver();
        additionalSetUp(role);
        hitButtons();
        verification(role);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void lock() throws SQLException, InterruptedException
    {
        Lock role = new Lock();
        additionalSetUp(role);
        hitButtons();
        verification(role);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void print_scenarios() throws SQLException, InterruptedException
    {
        Print_Scenarios role = new Print_Scenarios();
        additionalSetUp(role);
        hitButtons();
        verification(role);
    }
    @Test (groups={"roles"}, timeOut = timer)
    public void unlock() throws SQLException, InterruptedException
    {
        Unlock role = new Unlock();
        additionalSetUp(role);
        hitButtons();
        verification(role);
    }

    @Test(groups={"roles"}, timeOut = timer)
    public void psdAdmin() throws SQLException, InterruptedException
    {
        Psd_Admin role = new Psd_Admin();
        additionalSetUp(role);
        hitButtons();
        verification(role);
    }

    /** These test cases don't even have the Actions button showing up */
    @Test (groups={"roles"}, timeOut = timer)
    public void legacy() throws SQLException, InterruptedException
    {
        Legacy role = new Legacy();
        additionalSetUp(role);
        hitButtons();
        verification(role);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void manager() throws SQLException, InterruptedException
    {
        Manager role = new Manager();
        setUp(role);
        hitButtons();
        verification(role);
    }

    @Test (groups={"roles"}, timeOut = timer)
    public void viewer() throws SQLException, InterruptedException
    {
        Viewer role = new Viewer();
        setUp(role);
        hitButtons();
        verification(role);
    }

    //press the buttons to get to the preview page
    private void setUp(RolesBaseClass rbc) throws InterruptedException, SQLException
    {
        if (!norman.onlyClass(rbc.getRole_id())) {
            Login.login(webDriver, fono.getUsername(), fono.getPassword());
            SetupTests.waitForLoading(webDriver);
            UserManagement.giveUserSingleRole(webDriver, rbc.getName(), norman);
            SetupTests.waitForLoading(webDriver);
            Logout.logout(webDriver);
        }
        boolean hasClass = norman.onlyClass(rbc.getRole_id());
        if (rbc.getName().compareTo("ROLELESS") == 0)
            hasClass = true;
        if (hasClass)
            Login.login(webDriver, norman.getUsername(), norman.getPassword());
        else Assert.isTrue(hasClass, "Norman wasn't give the role?!");
        SetupTests.waitForLoading(webDriver);
    }

    //press the buttons to get to the preview page
    private void additionalSetUp(RolesBaseClass rbc) throws InterruptedException, SQLException
    {
        Login.login(webDriver, fono.getUsername(), fono.getPassword());
        SetupTests.waitForLoading(webDriver);
        WebElement element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        element.click();
        element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(MenuButtonConstants.ManageUserMenuXpath)));
        element.click();
        UserManagement.selectUserModal(webDriver, norman.getPreferred_name());
        SetupTests.waitForLoading(webDriver);
        String roles[] = {"VIEWER", rbc.getName()};
        UserManagement.userRoles(webDriver, roles);
        SetupTests.waitForLoading(webDriver);
        Logout.logout(webDriver);

        Login.login(webDriver, norman.getUsername(), norman.getPassword());
        SetupTests.waitForLoading(webDriver);
    }

    //
    private void hitButtons() throws SQLException
    {
        int docID = 0;
        ResultSet rs = JDBCUtils.getResultSet("select document_id from ( select * from document \n" +
                "where document_state_id != 2\n" +
                "and file_name not like '_manifest.html' \n" +
                "and path like '%general-conference%'\n" +
                "order by dbms_random.value )\n" +
                "where rownum <= 1\n");
        if(rs.next())
            docID = rs.getInt("document_id");
        rs.close();

        /** Go straight to the preview page instead of doing the search and clicking.
         *  This circumvents trying to get unlocked documents.  Might still accidentaly get a manifest*/
        uri = ("https://publish-test.ldschurch.org/content_automation/#!/preview-file?docId=" + docID);
        System.out.println(uri);
        webDriver.get(uri);

        /** Wait until the custom loading page is loaded...*/
        for(int i =0 ; i < 30; i++) {
            try {
                WebElement element = (new WebDriverWait(webDriver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"previewCtrl\"]/div/div[1]/span[2]")));
                System.out.println(element.getAttribute("class"));
                if(element.getAttribute("class").compareToIgnoreCase("text-muted loading-spinner ng-hide") == 0)
                    i = 100;
                else
                    BrowserUtils.sleep(1000);
            } catch (Exception e) {
                BrowserUtils.sleep(1000);
            }
        }

        try {
            List<WebElement> elements = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"previewDiv\"]/button")));
            for (WebElement we : elements)
                if (we.getAttribute("class").compareTo("btn btn-default ng-hide") != 0)
                    pass.add(we.getText());
        } catch(Exception e){}

        try {
            List<WebElement> prevNext = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"previewDiv\"]/div[1]/button")));
            for (WebElement we : prevNext)
                if (we.getAttribute("class").compareTo("btn btn-default ng-hide") != 0)
                    pass.add(we.getText());
        }catch(Exception e){}
    }

    private void verification(RolesBaseClass rbc)
    {
        ArrayList<String> accessible = rbc.getPreviewButtons();
        accessible.addAll(normallyAccessible);

        for(int j = 0; j < accessible.size(); j++)
        {
            for(int i = 0; i < pass.size(); i++)
            {
                /**Compare the two strings and if they are the same remove it */
                if((accessible.get(j).trim()).compareToIgnoreCase(pass.get(i).trim()) == 0) {
                    pass.remove(i);
                    i = 100; //break out of the for loop
                    accessible.remove(j);
                    j--;
                }
                /** The strings for prev and next have strange not normal << so straight comparisons don't work */
                else if(pass.get(i).trim().contains(accessible.get(j).trim()))
                {
                    pass.remove(i);
                    i = 100; //break out of the for loop
                    accessible.remove(j);
                    j--;
                }
            }
        }

        Assert.isTrue(accessible.size() == 0 && pass.size() == 0, stringify(accessible));
    }

    private String stringify(ArrayList<String> successes)
    {
        StringBuilder sb = new StringBuilder("Error with preview button availability for " + uri + "\n");
        if(successes.size() > 0)
        {
            sb.append("These should have been available ");
            for(String a: successes)
                sb.append(a + "\t" );
        }
        if(pass.size() > 0)
        {
            sb.append("\nThese shouldn't have been available ");
            for(String x : pass)
                sb.append(x + "\t");
        }
        return sb.toString();
    }
}
