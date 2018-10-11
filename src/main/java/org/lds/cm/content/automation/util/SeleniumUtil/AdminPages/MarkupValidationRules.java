package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MarkupValidationRules {

    public static void goToPage(WebDriver driver) {
        System.out.println("\nGoing to Markup Validation Rules page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavCustomValidation\"]"));
        page.click();
    }

    public static void addRule(WebDriver driver, String name, String boolXpath, int expectedResult, String errorMsg, String elemXpath, String contentType, String language, String severity) throws InterruptedException {
        System.out.println("Creating new Markup Validation Rule\n");

        System.out.println("Clicking add button\n");
        WebElement add = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/span/i"));
        add.click();

        Thread.sleep(1000);

        System.out.println("Typing in Name\n");
        WebElement name_field = driver.findElement(By.xpath("//*[@id=\"xpathName\"]"));
        name_field.sendKeys(name);

        Thread.sleep(1000);

        System.out.println("Typing in Boolean XPath\n");
        WebElement bool_xpath_field = driver.findElement(By.xpath("//*[@id=\"xpathBoolean\"]"));
        bool_xpath_field.sendKeys(boolXpath);

        Thread.sleep(1000);

        System.out.println("Choosing Expected Result\n");
        if (expectedResult == 1) {
            WebElement truth = driver.findElement(By.xpath("//*[@id=\"xpathExpected\"]/option[2]"));
            truth.click();
        } else if (expectedResult == 0) {
            WebElement falsy = driver.findElement(By.xpath("//*[@id=\"xpathExpected\"]/option[1]"));
            falsy.click();
        } else {
            System.out.println("\nThe number listed is not between 0-1\n");
        }

        Thread.sleep(1000);

        System.out.println("\nTyping in Error Message\n");
        WebElement error_field = driver.findElement(By.xpath("//*[@id=\"xpathMsg\"]"));
        error_field.sendKeys(errorMsg);

        Thread.sleep(1000);

        System.out.println("\nTyping in Element XPath\n");
        WebElement elem_xpath_field = driver.findElement(By.xpath("//*[@id=\"xpathElement\"]"));
        elem_xpath_field.sendKeys(elemXpath);

        Thread.sleep(1000);

        System.out.println("\nSelecting Content Type\n");
        List<WebElement> dropdown = driver.findElements(By.xpath("//*[@id=\"xpathContentType\"]/option"));
        int length = dropdown.size();

        for (int i = 1; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"xpathContentType\"]/option[" + i + "]"));
            String type = option.getText();

            if (type.equals(contentType)) {
                option.click();
            }
        }

        Thread.sleep(1000);

        System.out.println("\nSelecting Language\n");
        List<WebElement> lang_dropdown = driver.findElements(By.xpath("//*[@id=\"xpathLanguage\"]/option"));
        int lang_length = lang_dropdown.size();

        for (int i = 1; i <= lang_length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"xpathLanguage\"]/option[" + i + "]"));
            String lang = option.getText();

            if (lang.equals(language)) {
                option.click();
            }
        }

        Thread.sleep(1000);

        System.out.println("\nSelecting Severity\n");
        List<WebElement> severity_dropdown = driver.findElements(By.xpath("//*[@id=\"xpathSeverity\"]/option"));
        int severity_length = severity_dropdown.size();

        for (int i = 1; i <= severity_length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"xpathSeverity\"]/option[" + i + "]"));
            String choice = option.getText();

            if (choice.equals(severity)) {
                option.click();
            }
        }
    }

    public static void saveAndClose(WebDriver driver) {
        System.out.println("\nSelecting Save and Close Button\n");
        WebElement save = driver.findElement(By.xpath("//*[@id=\"newXpathModal\"]/div/div/div[3]/button[2]"));
        save.click();
    }

    public static void cancel(WebDriver driver) {
        System.out.println("\nSelecting Cancel Button\n");
        WebElement cancel = driver.findElement(By.xpath("//*[@id=\"newXpathModal\"]/div/div/div[3]/button[1]"));
        cancel.click();
    }

    public static void xOut(WebDriver driver) {
        System.out.println("\nExiting Markup Validation Modal\n");
        WebElement exit = driver.findElement(By.xpath("//*[@id=\"newXpathModal\"]/div/div/div[1]/button"));
        exit.click();
    }

    public static void selectRuleToEdit(WebDriver driver, String name) {
        System.out.println("\nSelecting Rule to Edit\n");
        List<WebElement> table = driver.findElements(By.xpath("//*[@id=\"usersTable\"]/tbody/tr"));
        int length = table.size();

        for (int i = 1; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"usersTable\"]/tbody/tr[" + i + "]/td[1]"));
            String rule = option.getText();

            if (rule.equals(name)) {
                System.out.println("\nEditing the rule: " + name + " \n");
                WebElement edit_button = driver.findElement(By.xpath("//*[@id=\"usersTable\"]/tbody/tr[" + i + "]/td[9]/span[1]/i"));
                edit_button.click();
            }
        }
    }

    public static void selectRuleToDelete(WebDriver driver, String name) {
        System.out.println("\nSelecting Rule to Delete\n");
        List<WebElement> table = driver.findElements(By.xpath("//*[@id=\"usersTable\"]/tbody/tr"));
        int length = table.size();

        for (int i = 1; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"usersTable\"]/tbody/tr[" + i + "]/td[1]"));
            String rule = option.getText();

            if (rule.equals(name)) {
                System.out.println("\nDeleting the rule: " + name + " \n");
                WebElement edit_button = driver.findElement(By.xpath("//*[@id=\"usersTable\"]/tbody/tr[" + i + "]/td[2]"));
                edit_button.click();
            }
        }
    }
}


