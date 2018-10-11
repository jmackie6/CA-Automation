package org.lds.cm.content.automation.util.SeleniumUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReleaseSystemLock {
    public static void releaseSystemLockButton(WebDriver driver) {
        System.out.println("\nSelecting Release System Lock button");
        WebElement release = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/release-system-lock/div/div[2]/div[4]/div[2]/button")));
        release.click();
    }

    public static void releaseSystemLockAndPublishButton(WebDriver driver) {
        System.out.println("\nSelecting Release System Lock and Publish button");
        WebElement release_and_publish = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/release-system-lock/div/div[2]/div[2]/div[2]/button")));
        release_and_publish.click();
    }

    public static void cancel(WebDriver driver) {
        System.out.println("\nCancelling Release System Lock");
        WebElement cancel = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/release-system-lock/div/div[3]/button")));
        cancel.click();
    }

    public static void exit(WebDriver driver) {
        System.out.println("\nExiting Release System Lock modal");
        WebElement exit = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/release-system-lock/div/div[1]/button/span")));
        exit.click();
    }
}
