package org.lds.cm.content.automation.tests.SeleniumTests;

import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.RoleModels.ICS_Admin;
import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.GroupManagement;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.util.concurrent.ThreadLocalRandom;

import java.sql.SQLException;
import java.util.List;

public class GroupManagementUI {
    private WebDriver webDriver;
    private WebElement menuButtons;
    /**
     *  Group Manage Admin Page
     */
    public static String defaultedGroup(int select) {
        return "//*[@id=\"manageGroupsTable\"]/tbody/tr["+ select +"]";
    }

    public static final String GroupManageDefaultNo = "//*[@class=\"modal-body\"]/div[4]/select/option[1]";
    public static final String GroupManageDefaultYes = "//*[@class=\"modal-body\"]/div[4]/select/option[2]";
    public static final String GroupManageSave = "//*[@class=\"modal-footer\"]/button[2]";
    public static final String GroupManageAdd = "//*[@class=\"page-body container\"]/div/div/div/a/i";



    // Click ona group, Change to Yes or No, Save the result
    private void groupManipulation(int selected, boolean isDefaulted){
        // Access a group
        menuButtons = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(defaultedGroup(selected))));
        menuButtons.click();

        // Select Yes or No
        if(isDefaulted) {
            menuButtons = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(GroupManageDefaultNo)));
            menuButtons.click();
        }else{
            menuButtons = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(GroupManageDefaultYes)));
            menuButtons.click();
        }

        //Select save
        menuButtons = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(GroupManageSave)));
        menuButtons.click();
    }

    @Test (timeOut = 180000)
    public void groupManagementTest() throws SQLException, InterruptedException, CustomException {
        // Create Norma Levy - ics-admin role
        NormanLevy normanLevy = new NormanLevy();
        if (!normanLevy.hasClass("ics_admin"))
            RoleAssignment.assignRole(normanLevy, new ICS_Admin());

        // authenticate Norman
        webDriver = PreTestSetup.setup();
        Login.login(webDriver, normanLevy.getUsername(), normanLevy.getPassword());

        // Navigate to the manageGroups
        SetupTests.waitForLoading(webDriver);
        menuButtons = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        menuButtons.click();
        menuButtons = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(GroupManagement.getMagaeGroupsMenuButtonXpath())));
        menuButtons.click();

        // Get all the groups names
        List<WebElement> memberList = (new WebDriverWait(webDriver, 30))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"manageGroupsTable\"]/tbody/tr")));
        int member_count = memberList.size();


        int defaultOption = 0;
        boolean isDefaultSelected = false;

        // Loop through all the Group's names and find a Default select if any
        for (int i = 1; i <= member_count; i++) {

            // Is default option displayed
            WebElement member_name = webDriver.findElement(By.xpath("//*[@id=\"manageGroupsTable\"]/tbody/tr[" + i + "]/td[1]/span/i"));
            isDefaultSelected = member_name.isDisplayed();

            if(isDefaultSelected) {
                defaultOption = i;
                groupManipulation(i, true);
                break;
            }
        }

        // Randomly select a group number if there is no default option
        if(!isDefaultSelected) {
            int randomGroupNum = ThreadLocalRandom.current().nextInt(1, member_count + 1);
            defaultOption = randomGroupNum;
            groupManipulation(randomGroupNum, false);
        }


        boolean isChecked = false;
        // Loop through all the Group's names anf find Inactive and Active groups
        for (int i = 1; i <= member_count; i++) {

            // Is default option displayed
            WebElement member_name = webDriver.findElement(By.xpath("//*[@id=\"manageGroupsTable\"]/tbody/tr[" + i + "]"));

            String text = member_name.getText();
            text = text.trim();
            System.out.println(text);
            // if Inactive Status
            if(text.contains("Inactive") && i != defaultOption && !isChecked){
                SetupTests.waitForLoading(webDriver);
                groupManipulation(i, false);
                isChecked = true;
            }

            // if Inactive Status
            if(isChecked){
                SetupTests.waitForLoading(webDriver);

                int randomActiveGroupNum = ThreadLocalRandom.current().nextInt(1,12 + 1);
                groupManipulation(randomActiveGroupNum, false);
                break;
            }
        }

        // Create a group
        SetupTests.waitForLoading(webDriver);
        menuButtons = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(GroupManageAdd)));
        menuButtons.click();
    }
}
