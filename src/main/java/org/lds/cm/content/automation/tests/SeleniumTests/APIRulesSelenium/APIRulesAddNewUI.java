package org.lds.cm.content.automation.tests.SeleniumTests.APIRulesSelenium;

import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.tests.APIRules.APIRulesStaticMethods;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.WebDriver;
import org.springframework.util.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.SQLException;

public class APIRulesAddNewUI {
    private WebDriver webDriver;
    private FonoSaia fono;

    @BeforeClass(alwaysRun = true, timeOut = 180000)
    public void setup() throws SQLException {
        APIRulesStaticMethods.setUP();
    }

    @AfterTest(alwaysRun = true, timeOut = 180000)
    public void closeBrowser(){
        webDriver.quit();
    }

    @Test
    public void addNewApiRuleTest() throws SQLException, InterruptedException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();
        APIRules.contentCentralLogin(webDriver, fono);
        SetupTests.waitForLoading(webDriver);
        APIRules.clickMainMenu(webDriver);
        APIRules.clickAPIRules(webDriver);

        // Add a rule to the Api rules page
        APIRules.addNewRule(webDriver, "ws/v1/testing", "ws/v1/testing", "ws/v1/testing endpoint is currently unavailable");
        APIRules.saveAndClose(webDriver, "/html/body/div[1]/div/div/api-rules-edit-component/div[4]/button[1]");
        BrowserUtils.sleep(5000);

        // Call a method to get information from the DB and see whether or not the new API rule was added
        String result = APIRules.addedAPIRuleInDB("ws/v1/testing");
        Assert.isTrue(result.compareTo("ws/v1/testing")==0, result);

        // Delete the rule just added
        APIRules.deleteApiRule("ws/v1/testing");

    }
}
