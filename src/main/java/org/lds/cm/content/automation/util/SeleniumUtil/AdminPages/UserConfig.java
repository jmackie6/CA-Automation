package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class UserConfig {

    public static void goToPage(WebDriver driver) {
        System.out.println("\nGoing to User Config page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavUserConfig\"]"));
        page.click();
    }

    public static void addConfig(WebDriver driver) {
        System.out.println("\nSelecting add button\n");
        WebElement add = driver.findElement(By.xpath("/html/body/div/div/div[2]/span"));
        add.click();
    }

    public static void enterName(WebDriver driver, String name) {
        System.out.println("\nTyping in name listed\n");
        WebElement name_field = driver.findElement(By.xpath("//*[@id=\"configName\"]"));
        name_field.sendKeys(name);
    }

    public static void enterDisplayName(WebDriver driver, String displayName) {
        System.out.println("\nTyping in display name\n");
        WebElement display_name = driver.findElement(By.xpath("//*[@id=\"configDisplayName\"]"));
        display_name.sendKeys(displayName);
    }

    public static void enterDescription(WebDriver driver, String description) {
        System.out.println("\nTyping in description\n");
        WebElement description_field = driver.findElement(By.xpath("//*[@id=\"configDescription\"]"));
        description_field.sendKeys(description);
    }

    public static void cancelNewConfig(WebDriver driver) {
        System.out.println("\nCancelling New User Config Creation\n");
        WebElement cancel = driver.findElement(By.xpath("//*[@id=\"userConfigModal\"]/div/div/div[3]/button[1]"));
        cancel.click();
    }

    public static void saveNewConfig(WebDriver driver) {
        System.out.println("\nSaving New Config Creation\n");
        WebElement save = driver.findElement(By.xpath("//*[@id=\"userConfigModal\"]/div/div/div[3]/button[2]"));
        save.click();
    }

    public static void xOutNewConfig(WebDriver driver) {
        System.out.println("\nExit Creat New Config modal\n");
        WebElement exit = driver.findElement(By.xpath("//*[@id=\"userConfigModal\"]/div/div/div[1]/button"));
        exit.click();
    }

    public static void editConfig(WebDriver driver, String displayName) {
        System.out.println("\nSelecting config to edit\n");
        List<WebElement> table = driver.findElements(By.xpath("/html/body/div/div/div[3]/div[2]/div/table/tbody/tr"));
        int length = table.size();

        for (int i = 1; i <= length; i++) {
            WebElement row = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[2]/div/table/tbody/tr[" + i + "]/td[2]"));
            String name = row.getText();

            if (name.equals(displayName)) {
                System.out.println("\nSelecting: " + name + " to edit\n");
                WebElement edit = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[2]/div/table/tbody/tr[" + i + "]/td[5]/span[1]"));
                edit.click();
            }
        }
    }

    public static void deleteConfig(WebDriver driver, String displayName) {
        System.out.println("\nSelecting config to delete\n");
        List<WebElement> table = driver.findElements(By.xpath("/html/body/div/div/div[3]/div[2]/div/table/tbody/tr"));
        int length = table.size();

        for (int i = 1; i <= length; i++) {
            WebElement row = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[2]/div/table/tbody/tr[" + i + "]/td[2]"));
            String name = row.getText();

            if (name.equals(displayName)) {
                System.out.println("\nSelecting: " + name + " to delete\n");
                WebElement delete = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[2]/div/table/tbody/tr[" + i + "]/td[5]/span[2]"));
                delete.click();
            }
        }
    }
}
