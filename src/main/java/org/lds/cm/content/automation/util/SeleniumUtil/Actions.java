package org.lds.cm.content.automation.util.SeleniumUtil;

import java.awt.*;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Actions {

    public static void selectSpecificAction(WebDriver driver, String specific_action) {
        int index = 0;

        List<WebElement> actions = driver.findElements(By.xpath("/html/body/div[1]/div/div/popup-menu/div[2]/div/div/a"));
        System.out.println("SIZE: " + actions.size());

        for (int i = 1; i <= actions.size(); i++) {
            WebElement action = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/popup-menu/div[2]/div/div/a[" + i + "]")));
            System.out.println("Action displayed? " + action.isDisplayed());
            System.out.println("ACTION: " + action.getText());
            if (!action.getText().equals(specific_action)) {
                index++;
            } else {
                action.click();
                index--;
                break;
            }
        }
        org.testng.Assert.assertTrue(index < actions.size(), "The action specified was not found.");
    }

    public static void lock(WebDriver driver) throws AWTException {

        WebElement action = driver.findElement(By.linkText("Source Lock"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nLocking Document\n");
    }

    public static void unlock(WebDriver driver) throws AWTException {
        WebElement action = driver.findElement(By.linkText("Source Unlock"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nUnlocking Document\n");
    }

    public static void fastTrackPdf(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Fast Track"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Fast Track PDF\n");
    }

    public static void print(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Print"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Print\n");
    }

    public static void approve(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Approve"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Approve\n");
    }

    public static void delete(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Delete"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Delete\n");
    }

    public static void delete_confirmation(WebDriver driver) {
        WebElement delete_confirmation = (new WebDriverWait(driver, 60))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/bulk-confirm/div[3]/button[2]")));
        delete_confirmation.click();
    }

    public static void publish(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Publish"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Publish\n");
    }

    public static void confirmPublish(WebDriver driver) {
        WebElement confirm_button = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/publish-warn-result/div/div[3]/button[2]")));
        confirm_button.click();
    }

    public static void clickPublishOK(WebDriver driver) {
        WebElement OK = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/publish-start-result/div[3]/button")));
        OK.click();
    }

    public static void publishExitOut(WebDriver driver) {
        WebElement exit = driver.findElement(By.xpath("/html/body/div[1]/div/div/publish-start-result/div[1]/button/span"));
        exit.click();
    }

    public static void publishHistory(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Publish History"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Publish History\n");
    }

    public static void publishHistoryClose(WebDriver driver) {
        WebElement close = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/dashboard-publish-history-component/div[3]/button")));
        close.click();
    }

    public static void publishHistoryExit(WebDriver driver) {
        WebElement exit = driver.findElement(By.xpath("//*[@id=\"publishHistoryModal\"]/div/div/div[1]/button/span"));
        exit.click();
    }

    public static void selectVersionNumber(WebDriver driver, String document) {
        System.out.println("\nSelecting version number of specified document to view Publish History\n");

        List<WebElement> documents = driver.findElements(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr/td"));
        int doc_count = documents.size();

        for (int i = 1; i <= doc_count; i++) {
            WebElement doc_name = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + i + "]/td[1]/span/span/a"));

            String doc_info = doc_name.getText();

            if (doc_info.equals(document)) {
                WebElement version = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + i + "]/td[7]/div/span/a[4]"));
                version.click();
            } else {
                System.out.println("\nThe document is not listed in the search results\n");
            }
        }
    }

    public static void updateGroupAndOwner(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Update Group and Owner"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Update Group and Owner\n");
    }

    public static void editMetadata(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Edit Metadata"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Update Metadata\n");
    }

    public static void validate(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Validate"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Validate\n");
    }

    public static void releaseSystemLock(WebDriver driver) {
        WebElement action = driver.findElement(By.linkText("Release System Lock"));

        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + action.getLocation().y + ")");
        action.click();

        System.out.println("\nSelecting Release System Lock\n");
    }

}
