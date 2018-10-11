package org.lds.cm.content.automation.tests.RolesTests;

import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.RoleModels.*;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.model.UserModels.*;
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

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class
RolesActionPresenceTesting
{
    private FonoSaia fono;
    private NormanLevy norman;
    private WebDriver webDriver;
    private static final String path = "/general-conference/2017/10";
    private static final String query = "select count(*) count from document where path=?";
    private static final String normal = "normal";
    private static final String bulk = "bulk";
    private ArrayList<String> actions;
    private ArrayList<String> pass;

    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, CustomException, InterruptedException
    {
        System.out.println("setUpClass");
        fono = new FonoSaia();
        //If fono doesn't have the ICS_Admin role he can't reassign roles to norman for these tests
        if(!(fono.hasClass("ICS_ADMIN")))
            RoleAssignment.assignRole(fono, new ICS_Admin());

        norman = new NormanLevy();

        ArrayList<String> fillIn = new ArrayList<>();
        fillIn.add(path);
        ResultSet rs = JDBCUtils.getResultSet(query, fillIn);
        if(rs.next())
        {
            int x = rs.getInt("count");
            if(x == 0)
                throw new CustomException("No documents located in " + path + ", so the search will fail.  Please add documents to this path and try again.");
        }
        else
            throw new CustomException("no documents located in " + path + ", so the search will fail.  Please add documents to this path and try again.");
        rs.close();

        actions = new ArrayList<>();
        actions.add("Approve");
        actions.add("Delete");
        actions.add("Fast Track");
        actions.add("Print Export");
        actions.add("Publish");
        actions.add("Publish History");
        actions.add("Release System Lock");
        actions.add("Source Lock");
        actions.add("Source Unlock");
        actions.add("Update Group and Owner");
        actions.add("Edit Metadata");
        actions.add("Validate");
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
    @Test (groups={"roles"},  timeOut = 180000)
    public void ics_admin() throws SQLException, InterruptedException, AWTException
    {
        ICS_Admin role = new ICS_Admin();
        normalSetUp(role);
        normalCall();
        verification(role, normal);
    }

    @Test (groups={"roles"},  timeOut = 180000)
    public void ics_support() throws SQLException, InterruptedException, AWTException {
        Ics_Support role = new Ics_Support();
        normalSetUp(role);
        normalCall();
        verification(role, normal);
    }

    @Test (groups={"roles"},  timeOut = 180000)
    public void ics_adminBulk() throws SQLException, InterruptedException
    {
        ICS_Admin role = new ICS_Admin();
        normalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void contributor() throws SQLException, InterruptedException, AWTException
    {
        Contributor role = new Contributor();
        normalSetUp(role);
        normalCall();
        verification(role, normal);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void contributorBulk() throws SQLException, InterruptedException
    {
        Contributor role = new Contributor();
        normalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void editor() throws SQLException, InterruptedException, AWTException
    {
        Editor role = new Editor();
        normalSetUp(role);
        normalCall();
        verification(role, normal);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void editorBulk() throws SQLException, InterruptedException
    {
        Editor role = new Editor();
        normalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void projectManager() throws SQLException, InterruptedException, AWTException
    {
        ProjectManager role = new ProjectManager();
        normalSetUp(role);
        normalCall();
        verification(role, normal);

    }

    @Test (groups={"roles"}, timeOut=180000)
    public void projectManagerBulk() throws SQLException, InterruptedException
    {
        ProjectManager role = new ProjectManager();
        normalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void publisher() throws SQLException, InterruptedException, AWTException
    {
        Publisher role = new Publisher();
        normalSetUp(role);
        normalCall();
        verification(role, normal);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void publisherBulk() throws SQLException, InterruptedException
    {
        Publisher role = new Publisher();
        normalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }


    /** You can't log in with these test cases, so they are paired with the Viewer class first */
    @Test (groups={"roles"}, timeOut=180000)
    public void approver() throws SQLException, InterruptedException, AWTException
    {
        Approver role = new Approver();
        additionalSetUp(role);
        normalCall();
        verification(role, normal);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void approverBulk() throws SQLException, InterruptedException
    {
        Approver role = new Approver();
        additionalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void lock() throws SQLException, InterruptedException, AWTException
    {
        Lock role = new Lock();
        additionalSetUp(role);
        normalCall();
        verification(role, normal);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void lockBulk() throws SQLException, InterruptedException
    {
        Lock role = new Lock();
        additionalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void unlock() throws SQLException, InterruptedException, AWTException
    {
        Unlock role = new Unlock();
        additionalSetUp(role);
        normalCall();
        verification(role, normal);
    }


    @Test (groups={"roles"}, timeOut=180000)
    public void unLockBulk() throws SQLException, InterruptedException
    {
        Unlock role = new Unlock();
        additionalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }

    @Test(groups={"roles"}, timeOut=180000)
    public void psdAdmin() throws SQLException, InterruptedException, AWTException
    {
        Psd_Admin role = new Psd_Admin();
        additionalSetUp(role);
        normalCall();
        verification(role, normal);
    }

    @Test(groups={"roles"}, timeOut=180000)
    public void psdAdminBulk() throws SQLException, InterruptedException
    {
        Psd_Admin role = new Psd_Admin();
        additionalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }


    @Test(groups={"roles"}, timeOut=180000)
    public void printScenarios() throws SQLException, InterruptedException, AWTException
    {
        Print_Scenarios role = new Print_Scenarios();
        additionalSetUp(role);
        normalCall();
        verification(role, normal);
    }

    @Test(groups={"roles"}, timeOut=180000)
    public void printScenariosBulk() throws SQLException, InterruptedException
    {
        Print_Scenarios role = new Print_Scenarios();
        additionalSetUp(role);
        normalBulkCall();
        verification(role, bulk);
    }


    /** These test cases don't even have the Actions button showing up */
    @Test (groups={"roles"}, timeOut=180000)
    public void legacy() throws SQLException, InterruptedException
    {
        Legacy role = new Legacy();
        additionalSetUp(role);
        noActionBtnCall();
        verification(role, normal);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void legacyBulk() throws SQLException, InterruptedException
    {
        Legacy role = new Legacy();
        additionalSetUp(role);
        noBulkBtnCall();
        verification(role, bulk);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void manager() throws SQLException, InterruptedException
    {
        Manager role = new Manager();
        normalSetUp(role);
        noActionBtnCall();
        verification(role, normal);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void managerBulk() throws SQLException, InterruptedException
    {
        Manager role = new Manager();
        additionalSetUp(role);
        noBulkBtnCall();
        verification(role, bulk);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void viewer() throws SQLException, InterruptedException
    {
        Viewer role = new Viewer();
        normalSetUp(role);
        noActionBtnCall();
        verification(role, normal);
    }

    @Test (groups={"roles"}, timeOut=180000)
    public void viewerBulk() throws SQLException, InterruptedException
    {
        Viewer role = new Viewer();
        additionalSetUp(role);
        noBulkBtnCall();
        verification(role, bulk);
    }




    private void normalSetUp(RolesBaseClass rbc) throws InterruptedException, SQLException
    {
        if(!norman.onlyClass(rbc.getRole_id())) {
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
        SetupTests.waitForLoading(webDriver);
    }

    /**Viewer has no access to any of the actions buttons.  Viewer is paired with roles like approver, lock etc.
     *   So that the action options are available.   */
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

    private void normalCall() throws AWTException
    {
        Search.search(webDriver, "/general-conference/2017/10");
        SetupTests.waitForLoading(webDriver);

        Random random = new Random();
        List<WebElement> table = (new WebDriverWait(webDriver, 45))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"row.branch.path\"]")));

 //       int x = random.nextInt(table.size());
        int x = table.size() - 1;
        try{
            for(int i = 0; i < table.size(); i++) {
                if (table.get(x).isDisplayed()) {
                    table.get(x).click();
                    i = table.size() + 10;
                } else {
                    x = random.nextInt(table.size());
                }
            }
        }
        catch(Exception e) {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_PAGE_DOWN);
            robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
            table.get(x).click();
        }

        List<WebElement> ActionModalList = (new WebDriverWait(webDriver, 10))
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[1]/div/div/popup-menu/div[2]/div/div/a")));
        for(int i = 0; i< ActionModalList.size(); i++)
            pass.add(ActionModalList.get(i).getText());
    }

    private void noActionBtnCall()
    {
        Search.search(webDriver, path);
        SetupTests.waitForLoading(webDriver);

        try {
            Random random = new Random();
            List<WebElement> table = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("row.branch.path")));
            int x = random.nextInt(table.size());
            try {
                table.get(x).click();
            } catch (Exception e) {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_PAGE_DOWN);
                robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
                table.get(x).click();
            }

            List<WebElement> ActionModalList = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[1]/div/div/popup-menu/div[2]/div/div/a")));
            for (int i = 0; i < ActionModalList.size(); i++)
                pass.add(ActionModalList.get(i).getText());
        }
        catch(Exception e){} //if no buttons are found then this test passes

    }

    private void normalBulkCall()
    {
        Search.search(webDriver, path);
        SetupTests.waitForLoading(webDriver);

        WebElement element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[1]/div[2]/doc-tree/div[2]/div[1]/div[1]/div/button[1]")));
        element.click();

        List<WebElement> ActionModalList = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[1]/div/div/popup-menu/div[2]/div/div/a")));
        for(int i = 0; i< ActionModalList.size(); i++)
            pass.add(ActionModalList.get(i).getText());
    }

    private void noBulkBtnCall()
    {
        Search.search(webDriver, path);
        SetupTests.waitForLoading(webDriver);
        try {
            WebElement element = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("/html/body/div/div[1]/div[2]/doc-tree/div[2]/div[1]/div[1]/div/button[1]")));
            element.click();

            List<WebElement> ActionModalList = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[1]/div/div/popup-menu/div[2]/div/div/a")));
            for (int i = 0; i < ActionModalList.size(); i++)
                pass.add(ActionModalList.get(i).getText());
        } catch (Exception e) {
        } //if no buttons are found then this test passes
    }

    /** Verification works the same for both.   Pass in the type (normal or bulk)
           so that error printing can differentiate  */
    private void verification(RolesBaseClass rbc, String ActionType)
    {
        ArrayList<String> ActionsArrayList = rbc.getAvailableActions();
        if(ActionType.compareToIgnoreCase("bulk") == 0)
            ActionsArrayList = rbc.getAvailableBulkActions();

        for(int j = 0; j < ActionsArrayList.size(); j++)
        {
            for(int i = 0; i < pass.size(); i++)
            {
                if((ActionsArrayList.get(j)).compareToIgnoreCase(pass.get(i).trim()) == 0) {
                    pass.remove(i);
                    i = 100; //break out of the for loop
                    ActionsArrayList.remove(j);
                    j--;
                }
            }
        }

        Assert.isTrue(ActionsArrayList.size() == 0 && pass.size() == 0, stringify(ActionType, ActionsArrayList));
    }

    private String stringify(String ActionType, ArrayList<String> actions)
    {
        StringBuilder sb = new StringBuilder("Error with " + ActionType + " action availability.\n");
        if(actions.size() > 0)
        {
            sb.append("These should have been available ");
            for(String a: actions)
                sb.append( a + "\t" );
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
