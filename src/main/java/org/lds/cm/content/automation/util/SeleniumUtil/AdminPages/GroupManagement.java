package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;


import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;

import java.util.List;

public class GroupManagement {

    private static String ManageGroupsMenuButton = MenuButtonConstants.ManageUserMenuXpath;

    public static String getMagaeGroupsMenuButtonXpath()
    {
        return ManageGroupsMenuButton;
    }

    public static void GoToPage(WebDriver driver) {
        System.out.println("\nGoing To Manage Groups Page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavManageGroups\"]"));
        page.click();

        BrowserUtils.sleep(3000);
    }

    public static void createNew(WebDriver driver) {
        System.out.println("\nCreating New Group\n");
        WebElement create = driver.findElement(By.xpath("/html/body/div/div/div[3]/div/div/div/a"));
        create.click();

        BrowserUtils.sleep(3000);
    }

    public static void groupName(WebDriver driver, String groupName) {
        System.out.println("\nEntering Group Name\n");
        WebElement groupNameField = driver.findElement(By.xpath("//*[@id=\"groupName\"]"));
        groupNameField.click();

        BrowserUtils.sleep(500);
        groupNameField.sendKeys(groupName);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void Description(WebDriver driver, String description) {
        System.out.println("\nEntering Description\n");
        WebElement descriptionField = driver.findElement(By.xpath("//*[@id=\"description\"]"));
        descriptionField.click();

        BrowserUtils.sleep(500);
        descriptionField.sendKeys(description);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void Status(WebDriver driver) {
        System.out.println("\nSelecting Status\n");

        WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[2]/div[3]"));
        dropdown.click();

        int index = (int) Math.round(Math.random());
        WebElement active = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[2]/div[3]/select/option[2]"));
        WebElement inactive = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[2]/div[3]/select/option[3]"));

        if (index == 0) {
            active.click();
        } else {
            inactive.click();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void DefaultField(WebDriver driver) {
        System.out.println("\nSelecting Default Field\n");

        WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[2]/div[4]"));
        dropdown.click();

        int index = (int) Math.round(Math.random());
        WebElement yes = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[2]/div[4]/select/option[1]"));
        WebElement no = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[2]/div[4]/select/option[2]"));

        if (index == 0) {
            no.click();
        } else {
            yes.click();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void SelectMembers(WebDriver driver, String[] members) {
        System.out.println("\nSelecting Members for Group\n");
        List<WebElement> memberList = driver.findElements(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[2]/div[5]/div"));
        int member_count = memberList.size();

        for (int i = 1; i <= member_count; i++) {
            WebElement member_name = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[2]/div[5]/div[" + i + "]/div"));
            String text = member_name.getText();
            text = text.trim();

            for (int j = 0; j < members.length; j++) {
                if (text.equals(members[j]) && !member_name.isSelected()) {
                    WebElement value = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[2]/div[5]/div[" + i + "]/div/div/input"));
                    value.click();
                    break;
                }
            }
        }
    }

    public static void SaveAndClose(WebDriver driver) {
        System.out.println("\nSaving New Group\n");
        WebElement save = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[3]/button[2]"));
        save.click();
    }

    public static void SelectCancel(WebDriver driver) {
        System.out.println("\nCancel Group Creation or Edit");
        WebElement cancel = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[3]/button[1]"));
        cancel.click();
    }

    public static void ExitOutofModal(WebDriver driver) {
        System.out.println("\nX Out of Modal");
        WebElement exitIcon = driver.findElement(By.xpath("//*[@id=\"manageGroupsModal\"]/div/div/div[1]/button"));
        exitIcon.click();
    }

    public static void SelectGroupForEdit(WebDriver driver, String group) {
        System.out.println("\nSelecting Group to Edit\n");
        WebElement table = driver.findElement(By.xpath("//*[@id=\"manageGroupsTable\"]/tbody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        int row = 1;
        boolean groupFound = false;

        for (WebElement groups : rows) {

            List<WebElement> results = groups.findElements(By.xpath("td"));

            for (int i = 1; i < results.size(); i++) {
                WebElement group_name = driver.findElement(By.xpath("//*[@id=\"manageGroupsTable\"]/tbody/tr[" + row + "]/td[" + i + "]"));

                if (group_name.getText().equals(group)) {
                    System.out.println("\nGroup Found For Edit\n");
                    groupFound = true;
                    group_name.click();

                    BrowserUtils.sleep(500);
                    break;
                }
            }

            row++;
        }

        if (groupFound == false) {
            System.out.println("\nEntry not found\n");
        }
    }

    public static void DeleteGroup (WebDriver driver, String group) {
        System.out.println("\nStarting Deletion Process\n");
        WebElement table = driver.findElement(By.xpath("//*[@id=\"manageGroupsTable\"]/tbody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        int row = 1;
        boolean groupFound = false;

        for (WebElement groups : rows) {

            List<WebElement> results = groups.findElements(By.xpath("td"));

            for (int i = 1; i < results.size(); i++) {
                WebElement group_name = driver.findElement(By.xpath("//*[@id=\"manageGroupsTable\"]/tbody/tr[" + row + "]/td[2]"));
                //*[@id="manageGroupsTable"]/tbody/tr[1]/td[2]
                WebElement delete = driver.findElement(By.xpath("//*[@id=\"manageGroupsTable\"]/tbody/tr[" + row + "]/td[5]/span/i"));

                if (group_name.getText().equals(group)) {
                    System.out.println("\nGroup Found and Deleted\n");
                    groupFound = true;
                    delete.click();

                    BrowserUtils.sleep(500);
                    break;
                }
            }

            row++;
        }
    }

    public static void SelectReassignGroup (WebDriver driver, String group) {
        System.out.println("\nReassigning Documents to New Group\n");
        WebElement dropdown = driver.findElement(By.xpath("/html/body/div[1]/div/div/delete-groups/div[2]/div[1]/div[2]/fieldset/div"));
        dropdown.click();

        WebElement dropdown_field = driver.findElement(By.xpath("/html/body/div[1]/div/div/delete-groups/div[2]/div[1]/div[2]/fieldset/div/div/div/input"));
        dropdown_field.sendKeys(group);
        dropdown_field.sendKeys(Keys.RETURN);
    }

    public static void ConfirmReassign (WebDriver driver) {
        System.out.println("\nConfirming Document Reassign to Delete Group\n");
        WebElement confirm = driver.findElement(By.xpath("/html/body/div[1]/div/div/delete-groups/div[2]/div[2]/button[1]"));
        confirm.click();
    }

    public static void CancelReassign (WebDriver driver) {
        System.out.println("\nCancel Reassign - Won't Delete\n");
        WebElement cancel = driver.findElement(By.xpath("/html/body/div[1]/div/div/delete-groups/div[2]/div[2]/button[2]"));
        cancel.click();
    }
}