package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.lds.cm.content.automation.util.BrowserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RoleManagement {

    public static void goToPage (WebDriver driver) throws InterruptedException {
        System.out.println("\nGoing to manage roles page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavManageRoles\"]"));
        page.click();
        Thread.sleep(1000);
    }

    public static void addRoleField (WebDriver driver, String role) throws InterruptedException {
        System.out.println("\nClicking and typing in new role\n");
        WebElement roleField = driver.findElement(By.xpath("//*[@id=\"roleName\"]"));
        roleField.click();

        roleField.sendKeys(role);
        BrowserUtils.sleep(1000);


        System.out.println("INFO: Pushing enter");

        roleField.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
    }

    public static void addRoleButton (WebDriver driver) throws InterruptedException {
        System.out.println("\nGoing to manage roles page\n");
        WebElement addRoleButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/div[2]/div/button"));
        addRoleButton.click();
        Thread.sleep(2000);
    }
}
