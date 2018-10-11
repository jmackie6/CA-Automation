package org.lds.cm.content.automation.tests.SeleniumTests;


import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.RoleModels.Editor;
import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sun.reflect.annotation.ExceptionProxy;

import java.io.File;
import java.sql.SQLException;
import java.util.Random;

//reference code - https://stackoverflow.com/questions/45573483/how-to-check-downloaded-files-selenium-webdriver
public class DownloadPDFUI
{
    private final int timer = 300000;
    private WebDriver webDriver;
    private NormanLevy norman;
    private String downloadPath = "";
    private Random random = new Random();

    @BeforeMethod (alwaysRun = true, timeOut = timer)
    public void setUp() throws InterruptedException, SQLException
    {
        downloadPath = System.getProperty("user.home") + "/Downloads";
        ChromeOptions co = new ChromeOptions();
        co.setExperimentalOption("download.default_directory", downloadPath);
        webDriver = PreTestSetup.setup();
        norman = new NormanLevy();
        if(!norman.hasClass("editor") && !norman.hasClass("project_manager"))
            RoleAssignment.assignRole(norman, new Editor());
    }

    @AfterMethod (alwaysRun = true)
    public void cleanUp()
    {
        webDriver.quit();
        JDBCUtils.closeUp();
    }


    @Test (groups={"selenium"}, timeOut=timer)
    public void downloadPDF() throws CustomException, SQLException
    {
        File downloadDirectory = new File(downloadPath);
        if(!downloadDirectory.exists())
            throw new CustomException("chose to use " + downloadPath + " but according to the code it doesn't exist");
        int initialCount = downloadDirectory.list().length;
        Login.login(webDriver, norman.getUsername(), norman.getPassword());
        SetupTests.waitForLoading(webDriver);
        WebElement element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        element.click();
        element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.DownloadPDFMenuXpath)));
        element.click();

        WebElement Results100PerPage = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[3]/div[2]/div/button[5]")));
        Results100PerPage.click();

        WebElement pdfButton = null;
        //try 10 times to get a random row choice.
        int rowChoice = 1;
        for(int i = 0; i < 30; i++) {
            int choice = chooseRandomRow();
            if(choice != -1)
            {
                rowChoice = choice;
                try {
                    pdfButton = (new WebDriverWait(webDriver, 10))
                            .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + rowChoice + "]/td[5]/div")));
                    if (pdfButton.getAttribute("disabled") == null && pdfButton.getAttribute("disabled").compareToIgnoreCase("true") == 0)
                        i = 100;

                }catch(Exception e){}
            }
        }
        Assert.isTrue(pdfButton != null, "A usable random row was not chosen.  Run a few good Fast Track PDFs first");
        pdfButton.click();
        SetupTests.waitForLoading(webDriver);
        int nextCount = downloadDirectory.list().length;
        BrowserUtils.sleep(5000);
        Assert.isTrue(nextCount > initialCount, "Download folder didn't have more files " + nextCount);

        String fileName = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + rowChoice + "]/td[2]"))).getText();

        boolean found = false;
        File[] downloadedFiles = downloadDirectory.listFiles();
        for(int i = 0; i < downloadedFiles.length; i++)
        {
            if(downloadedFiles[i].getName().contains(fileName))
                found = true;
        }
        Assert.isTrue(found, "Downloaded file wasn't found in the downloads folder");
    }

    @Test (groups={"selenium"}, timeOut=timer)
    public void downloadLog() throws CustomException, SQLException
    {
        File downloadDirectory = new File(downloadPath);
        if(!downloadDirectory.exists())
            throw new CustomException("chose to use " + downloadPath + " but according to the code it doesn't exist");
        int initialCount = downloadDirectory.list().length;
        System.out.println(initialCount);
        Login.login(webDriver, norman.getUsername(), norman.getPassword());
        SetupTests.waitForLoading(webDriver);
        WebElement element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        element.click();
        element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.DownloadPDFMenuXpath)));
        element.click();

        WebElement Results100PerPage = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[3]/div[2]/div/button[5]")));
        Results100PerPage.click();
        SetupTests.waitForLoading(webDriver);

        WebElement LogFileButton = null;
        //try 10 times to get a random row choice.
        int rowChoice = 1;
        for(int i = 0; i < 30; i++) {
            int choice = chooseRandomRow();
            if(choice != -1)
            {
                rowChoice = choice;
                try {
                    LogFileButton = (new WebDriverWait(webDriver, 10))
                            .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + rowChoice + "]/td[6]/div")));
                    if (LogFileButton.getAttribute("disabled") == null && LogFileButton.getAttribute("disabled").compareToIgnoreCase("null") == 0)
                        i = 100;

                }catch(Exception e){}
            }
        }

        Assert.isTrue(LogFileButton != null, "A usable random row was not chosen.  Run a few good Fast Track PDFs first");
        LogFileButton.click();
        SetupTests.waitForLoading(webDriver);
        int nextCount = downloadDirectory.list().length;
        System.out.println(nextCount);
        BrowserUtils.sleep(5000);
        Assert.isTrue(nextCount > initialCount, "Download folder didn't have more files.  The count is still = " + nextCount
                + "\nAttempted to access row " + rowChoice);

        String fileName = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + rowChoice + "]/td[2]"))).getText();

        boolean found = false;
        File[] downloadedFiles = downloadDirectory.listFiles();
        for(int i = 0; i < downloadedFiles.length; i++)
        {
            if(downloadedFiles[i].getName().contains(fileName))
                found = true;
        }
        Assert.isTrue(found, "Downloaded file wasn't found in the downloads folder");
    }

    /** Choose a random row to download information from.  If the fileName doesn't have 3 underscores try again */
    private int chooseRandomRow()
    {
        try {
            /** Get the number of rows */
            int rowResults = (new WebDriverWait(webDriver, 30))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr"))).size();
            int x = random.nextInt(rowResults);

            /**Verify that the log and pdf download buttons are available */
            String uri = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + (x + 1) + "]/td[1]"))).getText();
            if(uri.contains("error"))
                return -1;

            /** If everything is working then return the value*/
            return x + 1;
        }
        catch(Exception e)
        {
            return -1;
        }
    }
}
