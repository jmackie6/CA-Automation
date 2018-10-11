package org.lds.cm.content.automation.tests.SeleniumTests;

import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.RoleModels.Editor;
import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SeleniumUtil.Upload;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.sql.SQLException;

/**The way that this test is designed is to use a copy and paste from the system clipboard
 *  to the windows explorer where the cursor automatically calls focus.  This will not work
 *  on a MAC because of how the download window acts differently from the windows veresion.
 */
public class TransformUI
{
    private NormanLevy norman;
    private WebDriver webDriver;

    /** Need to assign Norman to all content groups */
    @BeforeMethod (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException, CustomException
    {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("mac"))
            throw new CustomException("These tests are set up only for Windows.  They will be ignored for a non-mac machine.");
        webDriver = PreTestSetup.setup();
        norman = new NormanLevy();
        if(!norman.hasClass("contributor") && !norman.hasClass("editor"))
            RoleAssignment.assignRole(norman, new Editor());

        System.out.println("WARNING!!!  You can not do anything else while this test is running or it will fail!");
    }

    @AfterMethod (alwaysRun = true)
    public void cleanUp()
    {
        webDriver.close();
        JDBCUtils.closeUp();
    }

    @Test (timeOut=180000)
    public void uploadUIWebml() throws InterruptedException, AWTException, CustomException, SQLException
    {
        uploadUI("/engGeneralConferenceWEBML/03020_000_000.xml");
    }

    @Test (timeOut = 180000)
    //This doc is a bad file type and doesn't pops up a warning error before the transform process
    public void badUploadUIDocx() throws InterruptedException, AWTException, CustomException, SQLException
    {
        badUploadUI("/docx/test-docx-abuse-ofender.docx", "Exception");
    }

    @Test (timeOut = 180000)
    public void uploadUIBadHtml() throws InterruptedException, AWTException, CustomException, SQLException
    {
        badUploadUI("/engEnsignHTML/06/i-have-a-question/what-is-the-gospel-principle-of-justification-by-faith.html", "failed");
    }

    @Test   (timeOut = 180000)
    public void uploadUILdsxml() throws InterruptedException, AWTException, CustomException, SQLException
    {
        uploadUI("/engEnsignLDSXML/_content-magazines-eng-03125_000_000.xml");
    }




    public void uploadUI(String fileName) throws InterruptedException, AWTException, CustomException, SQLException
    {
        Login.login(webDriver, norman.getUsername(), norman.getPassword());
        SetupTests.waitForLoading(webDriver);
        WebElement uploadButton = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.UploadButtonXpath)));
        uploadButton.click();
        Upload.addFilesButton(webDriver);

        File f = new File(Constants.transformFileStartDir + fileName);
        Upload.uploadFileIntoDropArea(webDriver, f.getAbsolutePath());

        BrowserUtils.sleep(500);
        try{Upload.contentGroupDropdown(webDriver, "General Conference");}
        catch(Exception e)
        {try{Upload.contentGroupDropdown(webDriver, "Manual");}
        catch(Exception es){ Upload.contentGroupDropdown(webDriver, "Ensign");}}

        BrowserUtils.sleep(500);
        Upload.startUploadButton(webDriver);

        String response = waitTilLoaded();
        if(!response.contains("Transform finished")) {
            String guid = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/xform-result-modal/div[1]/h4"))).getText();
            throw new CustomException("Transform did not return the expected results\nExpected - Transform finished\nRecieved - " + guid + "\t" + response);
        }

        WebElement element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/xform-result-modal/div[3]/button")));
        element.click();
    }

    /**Waits up to 300 seconds for the transform to complete.
     *   Will toss a CustomException if the transform finishes unsuccessfully
     *   Will toss a CustomException if transform takes too long
     *
     */
    public String waitTilLoaded() throws CustomException, InterruptedException
    {
        for(int i =0; i < 30; i++)
        {
            try {
                WebElement element = (new WebDriverWait(webDriver, 10))
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/xform-result-modal/div[2]/div/div[2]/div[3]")));
                return element.getText();
            }
            catch(Exception e)
            {
                if(e instanceof CustomException)
                    throw e;
            }
            Thread.sleep(1000);
        }
        throw new CustomException("Transform took too long...");
    }


    public void badUploadUI(String fileName, String message) throws InterruptedException, AWTException, CustomException, SQLException
    {
        Login.login(webDriver, norman.getUsername(), norman.getPassword());
        SetupTests.waitForLoading(webDriver);
        WebElement uploadButton = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.UploadButtonXpath)));
        uploadButton.click();
        Upload.addFilesButton(webDriver);

        File f = new File(Constants.transformFileStartDir + fileName);
        Upload.uploadFileIntoDropArea(webDriver, f.getAbsolutePath());

        BrowserUtils.sleep(500);
        try{Upload.contentGroupDropdown(webDriver, "General Conference");}
        catch(Exception e)
        {try{Upload.contentGroupDropdown(webDriver, "Manual");}
        catch(Exception es){ Upload.contentGroupDropdown(webDriver, "Ensign");}}

        BrowserUtils.sleep(500);
        Upload.startUploadButton(webDriver);

        String response = waitTilLoaded();
        if(!response.contains(message)) {
            String guid = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/xform-result-modal/div[1]/h4"))).getText();
            throw new CustomException("Transform did not return the expected results\nExpected - " + message + "\nRecieved - " + guid + "\t" + response);
        }

        WebElement element = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/xform-result-modal/div[3]/button")));
        element.click();
    }
}
