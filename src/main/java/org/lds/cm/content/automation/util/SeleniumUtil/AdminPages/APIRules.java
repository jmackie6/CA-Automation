package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.lds.cm.content.automation.model.RoleModels.ICS_Admin;
import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class APIRules
{
    /** Method used by other classes to force the specified api rule to allow endpoint to work*/
    public static void fixAPIRule(String rule) throws SQLException, InterruptedException
    {
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(rule);
        JDBCUtils.executeUpdate("update api_rules set Enabled = 1 where name = ?", fillInData);
    }

    // Login to Content Central Method
    public static void contentCentralLogin (WebDriver webDriver, FonoSaia fono) throws InterruptedException, SQLException {
        if (!fono.hasClass("ICS_admin"))
            RoleAssignment.assignRole(fono, new ICS_Admin());
        Login.login(webDriver, fono.getUsername(), fono.getPassword());
    }

    // Method to click the main menu
    public static void clickMainMenu (WebDriver webDriver){
        WebElement mainMenu = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        mainMenu.click();
    }

    // Method to click the API rules link
    public static void clickAPIRules(WebDriver webDriver){
        WebElement apiRules = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.APIRulesMenuXpath)));
        apiRules.click();
    }

    public static void addNewRule (WebDriver webDriver, String name, String desc, String message){
        // Click the add button
        WebElement apiAddNew = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/div[1]/span")));
        apiAddNew.click();

        // Enter information into fields
        WebElement inputName = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"ruleName\"]")));
        inputName.sendKeys(name);
        WebElement addDescription = webDriver.findElement(By.xpath("//*[@id=\"ruleDescription\"]"));
        addDescription.sendKeys(desc);
        WebElement addMessage = webDriver.findElement(By.xpath("//*[@id=\"ruleMessage\"]"));
        addMessage.sendKeys(message);

        // Click the checkbox to enable rule
        WebElement checkBox = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"ruleEnabled\"]")));
        checkBox.click();
    }

    // Method to get the row that you want on the API Rules page and click the edit button
    public static void getApiRow (WebDriver webDriver, String xpathToRow, String xpathtoEditBtn){
        WebElement apiRulesRow = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathToRow)));
        WebElement apiRulesEditBtn = apiRulesRow.findElement(By.xpath(xpathtoEditBtn));
        apiRulesEditBtn.click();
    }

    // Method to click the checkbox
    public static void clickCheckBox (WebDriver webDriver, String xpathToCheckBox, String ngEmptyOrNotEmpty){
        WebElement checkBox = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xpathToCheckBox)));
        if (checkBox.getAttribute("class").contains(ngEmptyOrNotEmpty)){
            checkBox.click();
        }
    }

    // Method to click the save and close button
    public static void saveAndClose (WebDriver webDriver, String xpathToSave){
        WebElement saveClose = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xpathToSave)));
        saveClose.click();
    }

    // Method to get the name of the rule to hit the endpoint
    public static WebElement findApiName (WebDriver webDriver, String xpathToRow, String xpathToApiName){
        WebElement apiRulesRow = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpathToRow)));
        WebElement getName = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        return getName = apiRulesRow.findElement(By.xpath(xpathToApiName));
    }

    public static String addedAPIRuleInDB(String apiRule) throws SQLException {
        String result = "";
        ResultSet rs = JDBCUtils.getResultSet("select name from api_rules where name = '" + apiRule + "'" );
        if (rs.next()) {
            result = rs.getString("name").toString();
        } else {
            result = "The API Rule was not successfully added";
        }

        return result;
    }

    // Delete an API Rule
    public static void deleteApiRule(String apiRule) throws SQLException {
        ResultSet deleteRS = JDBCUtils.getResultSet("delete from api_rules where name = '" + apiRule + "'");
        if(deleteRS.next())
            System.out.print(deleteRS.toString());
//
    }

}
