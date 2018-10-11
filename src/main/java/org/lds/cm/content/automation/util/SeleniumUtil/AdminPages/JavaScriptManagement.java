package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class JavaScriptManagement {

    public static void goToPage(WebDriver driver) {
        System.out.println("\nGoing to Manage Print Javascript page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavManagePrintJavaScript\"]"));
         page.click();
    }

    public static void clickCreateNew(WebDriver driver) {
        System.out.println("\nSelecting Create New button\n");
        WebElement create_new = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div[1]/a"));
        create_new.click();
    }

    public static void enterName(WebDriver driver, String name) {
        System.out.println("\nTyping in name of new Javascript\n");
        WebElement name_field = driver.findElement(By.xpath("//*[@id=\"name\"]"));
        name_field.sendKeys(name);
    }

    public static void chooseFile(WebDriver driver) {
        System.out.println("\nSelecting file for upload\n");

        // Waiting to finish this function to determine if we should store files in code or pull them from local machine.
    }

    public static void cancelCreateNew(WebDriver driver) {
        System.out.println("\nCancelling New Javascript Creation\n");
        WebElement cancel = driver.findElement(By.xpath("//*[@id=\"printJavascriptModal\"]/div/div/div[3]/button[1]"));
        cancel.click();
    }

    public static void saveNewJavascript(WebDriver driver) {
        System.out.println("\nSaving New Javascript\n");
        WebElement save = driver.findElement(By.xpath("//*[@id=\"printJavascriptModal\"]/div/div/div[3]/button[2]"));
        save.click();
    }

    public static void exitModal(WebDriver driver) {
        System.out.println("\nExiting New Javascript Modal\n");
        WebElement exit = driver.findElement(By.xpath("//*[@id=\"printJavascriptModal\"]/div/div/div[1]/button"));
        exit.click();
    }

    public static void clickRefresh(WebDriver driver) {
        System.out.println("\nSelecting Refresh Button\n");
        WebElement refresh = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div/div[2]/a"));
        refresh.click();
    }

    public static void selectFileToEdit(WebDriver driver, String name) {
        System.out.println("\nSelecting file to edit\n");
        List<WebElement> javascript = driver.findElements(By.xpath("//*[@id=\"printJavascriptTable\"]/tbody/tr"));
        int length = javascript.size();

        for (int i = 1; i <= length; i++) {
            WebElement row = driver.findElement(By.xpath("//*[@id=\"printJavascriptTable\"]/tbody/tr[" + i + "]/td[1]"));
            String js_name = row.getText();

            if (js_name.equals(name)) {
                row.click();
                System.out.println("\nSelected " + js_name + " to edit\n");
            }
        }
    }

    public static void selectFileToDelete(WebDriver driver, String name) {
        System.out.println("\nSelecting file to Delete\n");
        List<WebElement> javascript = driver.findElements(By.xpath("//*[@id=\"printJavascriptTable\"]/tbody/tr"));
        int length = javascript.size();

        for (int i = 1; i <= length; i++) {
            WebElement row = driver.findElement(By.xpath("//*[@id=\"printJavascriptTable\"]/tbody/tr[" + i + "]/td[1]"));
            String js_name = row.getText();

            if (js_name.equals(name)) {
                WebElement delete = driver.findElement(By.xpath("//*[@id=\"printJavascriptTable\"]/tbody/tr[" + i + "]/td[3]/span"));
                delete.click();
                System.out.println("\nSelected " + js_name + " to delete\n");
            }
        }
    }
}
