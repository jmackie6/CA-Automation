package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AppConfig {

    public static void goToPage (WebDriver driver) throws InterruptedException{
        System.out.println("\nGoing to ActionsItems page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavAppConfig\"]"));
        page.click();
        Thread.sleep(3000);
    }

    public static void editAppField (WebDriver driver, String value) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement val = driver.findElement(By.xpath("//*[@id=\"configValue\"]"));
        val.clear();
        val.sendKeys(value);
        val.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
    }

    public static void enableShellFileCreation (WebDriver driver) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement shell = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/table/tbody/tr[4]/td[3]/span"));
        shell.click();
        Thread.sleep(1000);
    }

    public static void enableMediaXmlCron (WebDriver driver) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement mediaXML = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/table/tbody/tr[5]/td[3]/span/i"));
        mediaXML.click();
        Thread.sleep(1000);
    }

    public static void enableDamResubmison (WebDriver driver) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement resubmision = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/table/tbody/tr[6]/td[3]/span/i"));
        resubmision.click();
        Thread.sleep(1000);
    }

    public static void generalNotifications (WebDriver driver) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement general = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/table/tbody/tr[3]/td[3]/span/i"));
        general.click();
        Thread.sleep(1000);
    }

    public static void damFailureNotifications (WebDriver driver) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement dam = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/table/tbody/tr[2]/td[3]/span/i"));
        dam.click();
        Thread.sleep(1000);
    }

    public static void flaskbackArchiveDate (WebDriver driver) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement flashBack = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/table/tbody/tr[1]/td[3]/span/i"));
        flashBack.click();
        Thread.sleep(1000);
    }

    public static void saveAndCloseButton (WebDriver driver) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement saveAndCloseButton = driver.findElement(By.xpath("//*[@id=\"appConfigModal\"]/div/div/div[3]/button[2]"));
        saveAndCloseButton.click();
        Thread.sleep(1000);
    }

    public static void cancelButton (WebDriver driver) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement cancelButton = driver.findElement(By.xpath("//*[@id=\"appConfigModal\"]/div/div/div[3]/button[1]"));
        cancelButton.click();
        Thread.sleep(1000);
    }

    public static void xOut (WebDriver driver) throws InterruptedException{
        System.out.println("\nActions Items - \n");
        WebElement xout = driver.findElement(By.xpath("//*[@id=\"appConfigModal\"]/div/div/div[1]/button"));
        xout.click();
        Thread.sleep(1000);
    }
}
