package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;
import org.lds.cm.content.automation.model.UserModels.UserBaseClass;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class UserManagement {
    public static void goToManageUsersPage (WebDriver driver) {
        System.out.println("\nGoing to manage users page\n");
        WebElement page = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(MenuButtonConstants.ManageUserMenuXpath)));
        page.click();
    }

    public static void selectUserModal (WebDriver driver, String user) {

        System.out.println("\n Selecting a specific user from list");
        List<WebElement> rowResults = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"currentUsersTable\"]/tbody/tr")));

        //List<WebElement> rowResults = table.findElements(By.tagName("tr"));
        System.out.println("number of rows: " + rowResults.size());

        int row_num,col_num;
        boolean userFound = false;
        row_num=1;
        for (WebElement trRow : rowResults)
        {
            List<WebElement> tdResults = trRow.findElements(By.xpath("td"));

            for (int i = 1; i < tdResults.size(); i++)
            {
                WebElement username = (new WebDriverWait(driver, 30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"currentUsersTable\"]/tbody/tr[" + row_num + "]/td[" + i + "]")));
                if (username.getText().equals(user))
                {
                    System.out.println("INFO: user found");
                    userFound = true;
                    username.click();
                    System.out.println("INFO: user clicked");
                    break;
                }
            }
            row_num++;
        }

        if(!userFound)
        {
            System.out.println("User not found");
        }
    }

    private static void saveAndCloseButton (WebDriver driver) throws InterruptedException {

        System.out.println("\nmanage users - save and close button \n");
        WebElement saveButton = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[3]/button[2]")));
        saveButton.click();
        Thread.sleep(1000);
    }

    public static void cancelButton (WebDriver driver) throws InterruptedException {

        System.out.println("\nmanage users - cancel button inside modal \n");
        WebElement cancelButton = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[3]/button[1]"));
        cancelButton.click();
        Thread.sleep(1000);
    }

    public static void addUserTab (WebDriver driver) throws InterruptedException {

        System.out.println("\nmanage users - Going to add user tab \n");
        WebElement addUserTab = driver.findElement(By.xpath("/html/body/div/div/div[3]/div/div/ul/li[2]/a"));
        addUserTab.click();
        Thread.sleep(1000);
    }

    public static void addUserSearch (WebDriver driver, String search) throws InterruptedException {

        System.out.println("\nmanage users - putting in search string \n");
        WebElement addUserSearch = driver.findElement(By.xpath("//*[@id=\"searchTerm\"]"));
        addUserSearch.click();
        Thread.sleep(1000);

        System.out.println("added text to search field");
        addUserSearch.sendKeys(search);

        BrowserUtils.sleep(1000);

        UserManagement.addUserSearchButton(driver);
    }

    private static void addUserSearchButton (WebDriver driver) throws InterruptedException {

        System.out.println("\nmanage users - search user button \n");
        WebElement addUserSearchButton = driver.findElement(By.xpath("/html/body/div/div/div[3]/div/div/div/div[2]/div[1]/div/div[2]/a"));
        addUserSearchButton.click();
        Thread.sleep(1000);
    }

    //NOT COMPLETED need to finish. This function will take in the user name of the user being search and will push the add button if the username
    //is found in the list of all usernames
    public static void selectUserFromSearch (WebDriver driver, String username) throws InterruptedException {

//        System.out.println("\nmanage users - selecting user from search results\n");
//        //*[@id="userSearchTable"]/tbody/tr/td[2] - username
//        //*[@id="userSearchTable"]/tbody/tr/td[4]/span/i - add button for the user
//
//        WebElement table = driver.findElement(By.xpath("//*[@id=\"userSearchTable\"]/tbody"));
//        List<WebElement> rowResults = table.findElements(By.tagName("tr"));
//        System.out.println("number of rows: " + rowResults.size());
//
//        int row_num,col_num;
//        boolean userFound = false;
//        row_num=1;
//        for (WebElement trRow : rowResults)
//        {
//            List<WebElement> tdResults = trRow.findElements(By.xpath("td"));
//            col_num=1;
//            for (int i = 1; i < tdResults.size(); i++)
//            {
//                //*[@id="currentUsersTable"]/tbody/tr[12]/td[3]
//                WebElement userName = driver.findElement(By.xpath("//*[@id=\"userSearchTable\"]/tbody/tr/td[" + i + "]"));
//                if (userName.getText().equals(username))
//                {
//                    System.out.println("INFO: user found");
//                    threadSleep(1000);
//                    userFound = true;
//                    //*[@id="row.branch.path"]
//                    //html/body/div[2]/div/doc-tree/div[2]/div[2]/div[1]/div[1]/table/tbody/tr[4]/td[4]/div[1]/span/button
//                    WebElement add = driver.findElement(By.xpath("//*[@id=\"userSearchTable\"]/tbody/tr/td[" + i + "]/span/i"));
//                    add.click();
//                    System.out.println("INFO: user added");
//                    threadSleep(4000);
//                    break;
//                }
//            }
//
//            row_num++;
//        }
////        WebElement addUserSearchButton = driver.findElement(By.xpath("/html/body/div/div/div[3]/div/div/div/div[2]/div[1]/div/div[2]/a"));
////        addUserSearchButton.click();
//        Thread.sleep(1000);
    }


    //This function is not working yet, last step to click x button is not recognized somehow
    public static void deleteUser (WebDriver driver, String user) {

        System.out.println("\n Deleting a specific user from list");
        WebElement table = driver.findElement(By.xpath("//*[@id=\"currentUsersTable\"]/tbody"));
        List<WebElement> rowResults = table.findElements(By.tagName("tr"));
        int row_num;
        boolean userFound = false;
        row_num = 1;
        for (WebElement trRow : rowResults)
        {
            List<WebElement> tdResults = trRow.findElements(By.xpath("td"));
            for (int i = 1; i < tdResults.size(); i++)
            {
                WebElement username = driver.findElement(By.xpath("//*[@id=\"currentUsersTable\"]/tbody/tr[" + row_num + "]/td[" + i + "]"));
//                WebElement deleteUser = driver.findElement(By.xpath("//*[@id=\"currentUsersTable\"]/tbody/tr[" + row_num + "]/td[" + i + "]/span"));
                if (username.getText().equals(user))
                {
                    System.out.println("INFO: user found");

                    BrowserUtils.sleep(1000);
                    userFound = true;
                    //this area is not quite working
//                    <span class="btn-xs btn-danger clickable" title="Delete user" ng-click="confirmDeleteUser(row)">
//                                                <i class="fa fa-times-circle"></i>
//                                            </span>
                    WebElement deleteUser = driver.findElement(By.xpath("//*[@id=\"currentUsersTable\"]/tbody/tr[" + row_num + "]/td[" + i + "]/span"));
                    //*[@id="currentUsersTable"]/tbody/tr[5]/td[6]/span/i
                    //*[@id="currentUsersTable"]/tbody/tr[2]/td[6]/span
//                    WebElement deleteUser = driver.findElement(By.className("btn-xs btn-danger clickable"));
                    deleteUser.sendKeys(Keys.ENTER);
//                    deleteUser.click();
                    System.out.println("INFO: user deleted");

                    BrowserUtils.sleep(4000);
                    break;
                }
            }
            row_num++;
        }

        if(!userFound)
        {
            System.out.println("User not found");
        }
    }


    public static void usersTab (WebDriver driver) throws InterruptedException {

        System.out.println("\nmanage users - Going to user tab \n");
        WebElement userTab = driver.findElement(By.xpath(" /html/body/div[2]/div/div[3]/div/div/ul/li[1]/a"));
        userTab.click();
        Thread.sleep(1000);
    }


    public static void userRoles (WebDriver driver, String[] roles) throws InterruptedException {
        System.out.println("\nmanage users - Give list a list of roles passed in \n");
        List<WebElement> labels = (new WebDriverWait(driver, 60))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div")));

        int count = labels.size();

        //deselect each role for the user
        System.out.println("manage users - deleting all roles\n");
        for ( int i = 1; i <= count; i++) {
            WebElement role = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div[" + i + "]/label/input")));
            if (role.isSelected())
            {
                role.click();
            }
        }

        boolean hasManager = false;
        boolean has_ICSAdmin = false;
        System.out.println("manage users - checking roles passed in\n");
        for ( int i = 1; i <= count; i++) {
            WebElement role = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div[" + i + "]/label")));
           // System.out.println("\n");
           // System.out.println(role.getText());
           // System.out.println("\n");
            String value = role.getText();

            value = value.trim();
            if(value.compareToIgnoreCase("manager") == 0)
                hasManager = true;
            if(value.compareToIgnoreCase("ics_admin") == 0)
                has_ICSAdmin = true;


            for (int j = 0; j < roles.length; j++) {

                if (value.equals(roles[j]))
                {
                    System.out.println("Checking role\n");
                    WebElement roleButton = (new WebDriverWait(driver, 30))
                            .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div[" + i + "]/label/input")));
                    roleButton.click();
                    break;
                }
            }
        }
        UserManagement.saveAndCloseButton(driver);
        SetupTests.waitForLoading(driver);
    }

    public static void selectUserRole (WebDriver driver, String role) throws InterruptedException {
        System.out.println("\nManage Users - will give user the role passed in, if already checked it will not give the user the role\n");
        List<WebElement> rolesNumber = driver.findElements(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div"));

        int count = rolesNumber.size();
        System.out.println(count);
        System.out.println("Manage Users - going through each role and selecting the role that was passed in");
        for ( int i = 1; i <= count; i++) {
            WebElement roleText = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div[" + i + "]/label"));
            String roleValue = roleText.getText();
            roleValue = roleValue.trim();

            if (roleValue.equals(role) && roleText.isSelected())
            {
                System.out.println("role already selected");
                UserManagement.saveAndCloseButton(driver);
                break;
            }
            else if (roleValue.equals(role) && !roleText.isSelected()) {
                WebElement r = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div[" + i + "]/label/input"));
                System.out.println("role given to user");
                r.click();
                UserManagement.saveAndCloseButton(driver);
                break;
            }
        }
    }

    public static void giveUserSingleRole (WebDriver driver, String role, UserBaseClass user) throws InterruptedException {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        element.click();
        element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(MenuButtonConstants.ManageUserMenuXpath)));
        element.click();

        selectUserModal(driver, user.getPreferred_name());

        List<WebElement> rolesNumber = driver.findElements(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div"));
        int count = rolesNumber.size();
        for ( int i = 1; i <= count; i++) {
            WebElement roleText = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div[" + i + "]/label"));
            String roleValue = roleText.getText();
            roleValue = roleValue.trim();

            String checked = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div[" + i + "]/label/input")).getAttribute("checked");
            if (roleValue.compareTo(role) == 0 && checked == null) {
                WebElement r = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div[" + i + "]/label/input"));
                r.click();
            }
            else if((roleValue.compareTo(role) != 0) && verify(checked))
            {
                WebElement r = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[1]/div[" + i + "]/label/input"));
                r.click();
            }
        }

        UserManagement.saveAndCloseButton(driver);
    }

    private static boolean verify(String checked)
    {
        if(checked == null)
            return false;
        if(checked.equals("true"))
            return true;
        return false;
    }


    public static void userGroups(WebDriver driver, String[] groups) throws InterruptedException {

        System.out.println("\nManage users - will give a user all the groups passed into the list\n");
        List<WebElement> labels = driver.findElements(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[2]/div"));

        int count = labels.size();
        System.out.println(count);
        System.out.println(groups.length);
        //deselect each role for the user
        for ( int i = 1; i <= count; i++) {
            WebElement group = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[2]/div[" + i + "]/label/input"));
            if (group.isSelected())
            {
                group.click();
            }
        }

        for ( int i = 1; i <= count; i++) {

            WebElement group = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[2]/div[" + i + "]/label"));
            System.out.println("\n");
            System.out.println(group.getText());
            System.out.println("\n");
            String value = group.getText();

            value = value.trim();

            System.out.println(value.length());

            for (int j = 0; j < groups.length; j++) {

                if (value.equals(groups[j]))
                {
                    System.out.println("checking group\n");
                    WebElement groupButton = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[2]/div[" + i + "]/label/input"));
                    groupButton.click();
                    break;
                }
            }
        }
        System.out.println("saving groups selected and passed in\n");
        UserManagement.saveAndCloseButton(driver);
    }

    public static void selectUserGroup (WebDriver driver, String group) throws InterruptedException {


        System.out.println("\nManage Users - will give user the role passed in, if already checked it will not give the user the role\n");
        List<WebElement> groupsNumber = driver.findElements(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[2]/div"));

        int count = groupsNumber.size();
        System.out.println(count);
        System.out.println("Manage Users - going through each role and selecting the role that was passed in");
        for ( int i = 1; i <= count; i++) {
            WebElement groupText = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[2]/div[" + i + "]/label"));
            String groupValue = groupText.getText();
            groupValue = groupValue.trim();

            if (groupValue.equals(group) && groupText.isSelected())
            {
                System.out.println("group already selected");

                UserManagement.saveAndCloseButton(driver);
                break;
            }
            else if (groupValue.equals(group) && !groupText.isSelected()) {
                WebElement g = driver.findElement(By.xpath("//*[@id=\"userRolesModal\"]/div/div/div[2]/div/div/div[2]/div[" + i + "]/label/input"));
                System.out.println("group given to user");
                g.click();
                UserManagement.saveAndCloseButton(driver);
                break;
            }
        }
    }
}
