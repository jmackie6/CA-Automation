package org.lds.cm.content.automation.tests;

import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class OddsAndEnds
{

    /** Check the canary page to see if it is down for maintenance. */
    @Test (groups="setup")
    public static void websiteDown() throws IOException
    {
        //Assert.isTrue(1==1);
        System.out.println("Verifying if the website is down or not");
        String response = NetUtils.getHTML(Constants.baseURL + "/canary");
        /** If the response has the phrase return the opposite to tell if the website is up or down*/
        Assert.isTrue(!(response.contains("Web site is down for maintenance")), "The Website is down...");
    }

    /** Goal is to clear the Chrome Browser Cache.  Currently not working...*/
    @Test
    public void clearChromeBrowserCache() throws InterruptedException
    {
        WebDriver webDriver = PreTestSetup.setup();
        try {
            if (webDriver instanceof ChromeDriver) {
                webDriver.get("chrome://settings/clearBrowserData");
                WebElement element = (new WebDriverWait(webDriver, 10))
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"clearBrowsingDataConfirm\"]")));
                element.click();
                element = (new WebDriverWait(webDriver, 10))
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"clearBrowsingDataSecondary\"]")));
                element.click();
            }
        }
        finally{ webDriver.quit();}
    }

}
