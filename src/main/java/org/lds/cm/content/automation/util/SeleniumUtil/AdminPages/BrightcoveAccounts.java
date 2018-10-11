package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BrightcoveAccounts {

    public static void goToPage(WebDriver driver) {
        System.out.println("\nGoing to Manage Brightcove Account Page\n");
        WebElement page_icon = driver.findElement(By.xpath("//*[@id=\"mainNavManageBrightcoveAccounts\"]"));
        page_icon.click();
    }

    public static void addAccount(WebDriver driver) {
        System.out.println("\nClicking Add Button\n");
    }

    public static void enterAccountId(WebDriver driver, String accountId) {
        System.out.println("\nTyping in Account ID\n");
        WebElement id_field = driver.findElement(By.xpath("//*[@id=\"accountID\"]"));
        id_field.sendKeys(accountId);
    }

    public static void enterAccountName(WebDriver driver, String accountName) {
        System.out.println("\nTyping in Account Name\n");
        WebElement account_name_field = driver.findElement(By.xpath("//*[@id=\"accountName\"]"));
        account_name_field.sendKeys(accountName);
    }

    public static void enterShortName(WebDriver driver, String shortName) {
        System.out.println("\nTyping in Short Name\n");
        WebElement short_name_field = driver.findElement(By.xpath("//*[@id=\"accountShortName\"]"));
        short_name_field.sendKeys(shortName);
    }

    public static void enterToken(WebDriver driver, String token) {
        System.out.println("\nTyping in Token\n");
        WebElement token_field = driver.findElement(By.xpath("//*[@id=\"accountToken\"]"));
        token_field.sendKeys(token);
    }

    public static void saveAndClose(WebDriver driver) {
        System.out.println("\nSaving New Account\n");
        WebElement save = driver.findElement(By.xpath("//*[@id=\"brightcoveAccountModal\"]/div/div/div[3]/button[2]"));
        save.click();
    }

    public static void cancel(WebDriver driver) {
        System.out.println("\nCancelling New Account Creation\n");
        WebElement cancel = driver.findElement(By.xpath("//*[@id=\"brightcoveAccountModal\"]/div/div/div[3]/button[1]"));
        cancel.click();
    }

    public static void xOut(WebDriver driver) {
        System.out.println("\nExiting Brightcove Account Creation Modal\n");
        WebElement exit = driver.findElement(By.xpath("//*[@id=\"brightcoveAccountModal\"]/div/div/div[1]/button"));
        exit.click();
    }

    public static void selectAccountForEdit(WebDriver driver, String accountID) {

        List<WebElement> table = driver.findElements(By.xpath("//*[@id=\"accountsTable\"]/tbody/tr"));
        int length = table.size();

        for (int i = 2; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"accountsTable\"]/tbody/tr[" + i + "]/td[1]"));
            String name = option.getText();

            if (name.contains(accountID)) {
                System.out.println("\nAccount ID " + name + " selected for edit\n");
                WebElement edit_button = driver.findElement(By.xpath("//*[@id=\"accountsTable\"]/tbody/tr[" + i + "]/td[5]/span[1]/i"));
                edit_button.click();
            }
        }
    }

    public static void selectAccountToDelete(WebDriver driver, String accountID) {

        List<WebElement> table = driver.findElements(By.xpath("//*[@id=\"accountsTable\"]/tbody/tr"));
        int length = table.size();

        for (int i = 2; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"accountsTable\"]/tbody/tr[" + i + "]/td[1]"));
            String name = option.getText();

            if (name.contains(accountID)) {
                System.out.println("\nAccount ID " + name + " selected to delete\n");
                WebElement delete_button = driver.findElement(By.xpath("//*[@id=\"accountsTable\"]/tbody/tr[" + i + "]/td[5]/span[2]"));
                delete_button.click();
            }
        }
    }
}
