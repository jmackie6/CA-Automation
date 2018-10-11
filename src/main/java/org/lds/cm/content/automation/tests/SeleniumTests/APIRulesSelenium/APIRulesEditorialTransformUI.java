package org.lds.cm.content.automation.tests.SeleniumTests.APIRulesSelenium;

import org.lds.cm.content.automation.tests.APIRules.APIRulesStaticMethods;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class APIRulesEditorialTransformUI {
    private WebDriver webDriver;
    private FonoSaia fono;

    @AfterClass (alwaysRun = true, timeOut = 180000)
    public void cleanUp() throws SQLException, InterruptedException {
        APIRulesStaticMethods.fixAPIRule("ws/v1/Editorial/Transform");
    }

    @BeforeClass (alwaysRun = true, timeOut = 180000)
    public void setUp() throws SQLException, InterruptedException {
        APIRulesStaticMethods.setUP();
    }

    @AfterMethod
    public void closeUp(){
        JDBCUtils.closeUp();
        webDriver.quit();
    }

    @Test (timeOut = 180000)
    public void editorialTransformDisabled() throws InterruptedException, SQLException, IOException {
        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/lead-kindly-light.html");
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login and check to see if fono has correct roles
        APIRules.contentCentralLogin(webDriver, fono);

        SetupTests.waitForLoading(webDriver);

        // Click on the Main Menu
        APIRules.clickMainMenu(webDriver);

        // Click on the API Rules link
        APIRules.clickAPIRules(webDriver);

        // Get the row that needs to be tested, find the edit button and click it
        String xpathToRow = "/html/body/div/div/div[2]/div[2]/div[2]/div/table/tbody/tr";
        int rowNum = 0;
        List <WebElement> apiRows = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathToRow)));
        for (int i = 0; i < apiRows.size(); i++){
            if (apiRows.get(i).getText().contains("ws/v1/editorial/transform")) {
                rowNum = i + 1;
                xpathToRow = xpathToRow + "[" + rowNum + "]";
                i = 100;
            }
        }
        String xpathToEditBtn = "td[5]/span[1]";
        APIRules.getApiRow(webDriver, xpathToRow, xpathToEditBtn);

        // Click the check box
        String xpathToCheckBox = "//*[@id=\"ruleEnabled\"]";
        String ngEmptyOrNotEmpty = "ng-not-empty";
        APIRules.clickCheckBox(webDriver, xpathToCheckBox, ngEmptyOrNotEmpty);

        // Click Save and Close
        ///html/body/div[1]/div/div[2]/div[2]/div[2]/div/table/tbody/tr[4]
        String xpathToSaveAndClose = "/html/body/div[1]/div/div/api-rules-edit-component/div[4]/button[1]";
        APIRules.saveAndClose(webDriver, xpathToSaveAndClose);

        // Get the name of the API Rule and send it to the fourOThreeError method
        String xpathToApiName = xpathToRow + "/td[1]";
        WebElement getName = APIRules.findApiName(webDriver, xpathToRow, xpathToApiName);

        String error = APIRulesStaticMethods.apiFileUploadStatus403(getName.getText(), testFile, Constants.epEditorialTransform);
        Assert.isTrue(error.compareTo("")==0, error);

        webDriver.quit();
    }





    @Test (timeOut = 180000)
    public void editorialTransformEnabled() throws InterruptedException, SQLException, IOException {
        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/lead-kindly-light.html");
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login and check to see if fono has correct roles
        APIRules.contentCentralLogin(webDriver, fono);

        SetupTests.waitForLoading(webDriver);

        // Click on the Main Menu
        APIRules.clickMainMenu(webDriver);

        // Click on the API Rules link
        APIRules.clickAPIRules(webDriver);

        // Get the row that needs to be tested, find the edit button and click it
        String xpathToRow = "/html/body/div/div/div[2]/div[2]/div[2]/div/table/tbody/tr";
        int rowNum = 0;
        List <WebElement> apiRows = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathToRow)));
        for (int i = 0; i < apiRows.size(); i++){
            if (apiRows.get(i).getText().contains("ws/v1/editorial/transform")) {
                rowNum = i + 1;
                xpathToRow = xpathToRow + "[" + rowNum + "]";
                i = 100;
            }
        }
        String xpathToEditBtn = "td[5]/span[1]";
        APIRules.getApiRow(webDriver, xpathToRow, xpathToEditBtn);

        // Click the check box
        String xpathToCheckBox = "//*[@id=\"ruleEnabled\"]";
        String ngEmptyOrNotEmpty = "ng-not-empty";
        APIRules.clickCheckBox(webDriver, xpathToCheckBox, ngEmptyOrNotEmpty);

        // Click Save and Close
        String xpathToSaveAndClose = "/html/body/div[1]/div/div/api-rules-edit-component/div[4]/button[1]";
        APIRules.saveAndClose(webDriver, xpathToSaveAndClose);

        // Get the name of the API Rule and send it to the fourOThreeError method
        String xpathToApiName = xpathToRow + "/td[1]";
        WebElement getName = APIRules.findApiName(webDriver, xpathToRow, xpathToApiName);

        String error = APIRulesStaticMethods.apiFileUploadStatus(getName.getText(), testFile, Constants.epEditorialTransform);
        Assert.isTrue(error.compareTo("")==0, error);
        webDriver.quit();
    }





}
