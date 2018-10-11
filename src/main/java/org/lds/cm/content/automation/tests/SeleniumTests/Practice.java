package org.lds.cm.content.automation.tests.SeleniumTests;

import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.sql.SQLException;
import java.util.Properties;

public class Practice
{
    private WebDriver webDriver;
    private NormanLevy norman;

    //For odds and ends to be tested.
    @Test
    public void practice() throws SQLException, InterruptedException, SQLException, AddressException, MessagingException {

        webDriver = PreTestSetup.setup();
        norman = new NormanLevy();

        Login.login(webDriver, norman.getUsername(), norman.getPassword());

        /** Navigate to the Link Search Page */
        SetupTests.waitForLoading(webDriver);
        WebElement menuButtons = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        menuButtons.click();
        menuButtons = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.ManageGroupsMenuXpath)));
        menuButtons.click();
    }

}
