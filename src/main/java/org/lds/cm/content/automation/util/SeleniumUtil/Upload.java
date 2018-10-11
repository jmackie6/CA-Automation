package org.lds.cm.content.automation.util.SeleniumUtil;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;

import junit.framework.Assert;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.lang.model.element.Element;

public class Upload {

    public static void uploadPage (WebDriver driver) {
        WebElement uploadPage = driver.findElement(By.xpath(MenuButtonConstants.UploadButtonXpath));
            uploadPage.click();
    }

    public static void addFilesButton (WebDriver driver) {
        WebElement addFiles = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"uploader_browse\"]")));
        addFiles.click();
    }

    public static void validateCheckbox (WebDriver driver) {
        WebElement validateCheckbox = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[3]/label/input"));
        validateCheckbox.click();
    }

    public static void contentGroupDropdown (WebDriver driver, String contentGroup) {

        WebElement select_content_group = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable((By.xpath("//*[@id=\"contentGroup\"]"))));
        select_content_group.click();

        Select content_group = new Select(driver.findElement(By.xpath("//*[@id=\"contentGroup\"]")));
        content_group.selectByVisibleText(contentGroup);
        select_content_group.click();
    }

    public static void contentGroupDropdownByIndex(WebDriver driver, int index) {
       WebElement content_group = (new WebDriverWait(driver, 10))
               .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"contentGroup\"]")));
       content_group.click();
       Select dropdown = new Select(driver.findElement(By.xpath("//*[@id=\"contentGroup\"]")));
       dropdown.selectByIndex(1);
    }

    public static void startUploadButton ( WebDriver driver) {
        System.out.println("INFO: Start transform button");
        WebElement start_upload = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable((By.xpath("//*[@id=\"uploader_container\"]/div/div/div[2]/div[1]/div/a[2]"))));
        start_upload.click();
    }

    /**  An absolute file path must be used.  Use the commented out code as an example of how to get the absolute path */
    public static void uploadFileIntoDropArea(WebDriver driver, String filePath) throws InterruptedException, AWTException {
//        addFiles.sendKeys("C:\\Users\\jwm66\\IdeaProjects\\content-automation\\qa\\src\\main\\resources\\TransformTestFiles\\engBroadcastWEBML\\_content-broadcast-PD60000934_000_000.xml");

        //name of file to grab from project
        //Thread.currentThread().getContextClassLoader().getResource("");
//        File temp = File.createTempFile("i-am-a-temp-file", ".tmp" );
//
//        String absolutePath = temp.getAbsolutePath();
//        System.out.println("File path : " + absolutePath);


        /**Can't do a wait for the file system to pop up */
        BrowserUtils.sleep(1000);

        /** Have to save an empty string to the clipboard before saving the file path*/
        StringSelection empty = new StringSelection("");
        StringSelection ss = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(empty, null);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);


        /**native key strokes for CTRL, V and ENTER keys */
        Robot robot = new Robot();

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    //TODO need to finish this test and be able to delete a file that has been added to the upload box
    public static void deleteAddedFile (WebDriver driver) {
        //*[@id="uploader_filelist"]/li
    }
}
