package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.lds.cm.content.automation.model.RoleModels.ICS_Admin;
import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.SQLException;

public class ContentReport {

    // Master constants
    private static final String masterCreated = "May 23, 2016";
    private static final String masterModified = "Jun 20, 2018";
    private static final String masterPublished = "Jun 20, 2018";

    
    public static String getMasterCreated(){
        return masterCreated;
    }
    
    public static String getMasterModified(){
        return masterModified;
    }
    
    public static String getMasterPublished(){
        return masterPublished;
    }


    public static void loginToCC(WebDriver webDriver, FonoSaia fono) throws SQLException, InterruptedException {
        if (!fono.hasClass("ICS_admin"))
            RoleAssignment.assignRole(fono, new ICS_Admin());
        Login.login(webDriver, fono.getUsername(), fono.getPassword());
    }

    public static void clickReportsLinkThenContentReport(WebDriver webDriver, String xPathToReports, String xPathToContentReport){
        WebElement reports = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xPathToReports)));
        reports.click();

        WebElement contentReport = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xPathToContentReport)));
        contentReport.click();
    }

    public static WebElement goToContentReportAndSearch(WebDriver webDriver, String sendKeys){
        // Click the Reports link then click on Content Report
        String xPathToReports = "//*[@id=\"reports-id\"]/a";
        String xPathToContentReport = "//*[@id=\"content-report-id\"]/a";
        ContentReport.clickReportsLinkThenContentReport(webDriver, xPathToReports, xPathToContentReport);

        // Start a search for something you know will return results
        BrowserUtils.sleep(5000);
        WebElement searchBox = webDriver.findElement(By.xpath(MenuButtonConstants.PathToContentReportSearchBox));
        searchBox.sendKeys(sendKeys);
        BrowserUtils.sleep(5000);

        // Click the search button
        WebElement searchButton = ContentReport.contentReportPageSearchButton(webDriver);
        BrowserUtils.sleep(5000);
        searchButton.click();
        BrowserUtils.sleep(5000);

        return searchButton;

    }

    public static String getDateBasedOnEnvironment(){
        String createdDateField;
        String env = Constants.environment;
        switch (env) {
            case "test":
                createdDateField = "";
                break;
            case "stage":
                createdDateField = "2018/06/20";
                break;
            case "dev":
                createdDateField = "2017/03/28";
                break;
            default:
                createdDateField = "Environment variable not set";
                break;
        }

        return createdDateField;
    }

    public static String getSearchCriteriaBasedOnEnvironment(){
        String search;
        String env = Constants.environment;
        if (!env.equals("stage")){
            search = "/general-conference/2017/04";
        } else {
            search = "/general-conference/2006/04";
        }

        return search;
    }


    public static WebElement contentReportPageSearchButton(WebDriver webDriver){
        WebElement searchButton = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.PathToContentReportSearchBtn)));
        searchButton.click();
        return searchButton;
    }

    public static void clickIntoApprovedBox(WebDriver webDriver){
        WebElement approveBox = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"approved\"]")));
        approveBox.click();
    }

    public static void clickApprovedOption(WebDriver webDriver){
        WebElement approveOption = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"approved\"]/option[2]")));
        approveOption.click();
    }

    public static void clickIntoCreatedDateBox(WebDriver webDriver){
        WebElement createdDateBox = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/select")));
        createdDateBox.click();
    }

    public static void createdModifiedOrPublishedDate(WebDriver webDriver, String xPathToOption){
        // Click modified date option
        WebElement dateOption = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/select/option[3]")));
        dateOption.click();
    }

    public static WebElement clickAWebElement(WebDriver webDriver, String xPathToElement){
        WebElement element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xPathToElement)));
        return element;
    }

    public static void clickCreateSearch(WebDriver webDriver){
        WebElement createSearch = webDriver.findElement(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/div/button[1]"));
        createSearch.click();
        BrowserUtils.sleep(1000);
    }


}
