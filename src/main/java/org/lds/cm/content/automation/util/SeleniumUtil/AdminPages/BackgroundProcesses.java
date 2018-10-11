package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BackgroundProcesses {
    public static void goToPage(WebDriver driver) {
        System.out.println("\nGoing to Background Processes Page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"ADMINISTRATION-BOX\"]/a[3]"));
        page.click();
    }
    public static void clickScriptsTab(WebDriver driver) {
        System.out.println("\nSelecting Scripts tab\n");
        WebElement scripts = driver.findElement(By.xpath("/html/body/div/div/ul/li[2]/a"));
        scripts.click();
    }

    public static void clickTaskListTab(WebDriver driver) {
        System.out.println("\nSelecting Task List Tab\n");
        WebElement task_list = driver.findElement(By.xpath("/html/body/div/div/ul/li[1]/a"));
        task_list.click();
    }

    public static void selectType(WebDriver driver, String type) {
        System.out.println("\nSelecting a type in the dropdown\n");
        List<WebElement> dropdown = driver.findElements(By.xpath("/html/body/div/div/div[3]/div[1]/div/div/div[1]/select/option"));
        int length = dropdown.size();

        for (int i = 1; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[1]/div/div/div[1]/select/option[" + i + "]"));
            String name = option.getText();

            if (name.equals(type)) {
                option.click();
                System.out.println("\nSelecting " + name + " as type\n");
            }

        }
    }

    public static void selectStatus(WebDriver driver, String status) {
        System.out.println("\nSelecting a status in the dropdown\n");
        List<WebElement> dropdown = driver.findElements(By.xpath("/html/body/div/div/div[3]/div[1]/div/div/div[2]/select/option"));
        int length = dropdown.size();

        for (int i = 1; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[1]/div/div/div[2]/select/option[" + i + "]"));
            String name = option.getText();

            if (name.equals(status)) {
                option.click();
                System.out.println("\nSelecting " + name + " as status\n");
            }
        }
    }

    public static void clickRefresh(WebDriver driver) {
        System.out.println("\nSelecting Refresh\n");
        WebElement refresh = driver.findElement(By.xpath("//*[@id=\"btnRefresh\"]"));
        refresh.click();
    }

    public static void previousPage(WebDriver driver) {
        System.out.println("\nSelecting Previous Page\n");
        WebElement previous = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[3]/ul/li[1]/a"));
        previous.click();
    }

    public static void nextPage(WebDriver driver) {
        System.out.println("\nSelecting Next Page\n");
        WebElement next = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[3]/ul/li[2]/a"));
        next.click();
    }

    public static void resultsPerPage(WebDriver driver, int number) {
        System.out.println("\nSelecting Results Per Page\n");

        if (number == 5) {
            System.out.println("\nListing first 5 results\n");
            WebElement five = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[8]/button"));
            five.click();
        } else if (number == 10) {
            System.out.println("\nListing first 10 results\n");
            WebElement ten = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[7]/button"));
            ten.click();
        } else if (number == 25) {
            System.out.println("\nListing first 25 results\n");
            WebElement twenty_five = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[6]/button"));
            twenty_five.click();
        } else if (number == 50) {
            System.out.println("\nListing first 50 results\n");
            WebElement fifty = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[5]/button"));
            fifty.click();
        } else if (number == 100) {
            System.out.println("\nListing first 100 results\n");
            WebElement one_hundred = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[4]/button"));
            one_hundred.click();
        } else {
            System.out.println("\nThe number given is not an option\n");
        }
    }

    public static void bulkPublish(WebDriver driver, String language, String path) {
        System.out.println("\nBackground Process - Bulk Publish\n");

        System.out.println("\nTyping in Language\n");
        WebElement language_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[1]/div/div/input[1]"));
        language_field.sendKeys(language);

        System.out.println("\nTyping in Path\n");
        WebElement path_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[1]/div/div/input[2]"));
        path_field.sendKeys(path);

        System.out.println("\nSelecting Bulk Publish\n");
        WebElement publish = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[1]/div/div/button"));
        publish.click();
    }

    public static void resumbitDamFailures(WebDriver driver) {
        System.out.println("\nBackground Process - Assign Resubmit DAM Failures\n");

        System.out.println("\nResubmitting DAM Failures\n");
        WebElement resubmit = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[2]/div/div/div/button"));
        resubmit.click();
    }

    public static void fixCrossReferences(WebDriver driver, String oldUri, String newUri) {
        System.out.println("\nBackground Process - Fix Cross Reference\n");

        System.out.println("\nTyping in existing URI\n");
        WebElement old_uri = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[3]/div/div/input[1]"));
        old_uri.sendKeys(oldUri);

        System.out.println("\nTyping in New URI\n");
        WebElement new_uri = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[3]/div/div/input[2]"));
        new_uri.sendKeys(newUri);

        System.out.println("\nSelecting Fix Cross References\n");
        WebElement fix = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[3]/div/div/button"));
        fix.click();
    }

    public static void bulkLockUnlock(WebDriver driver, String language, String uri, int choice) {
        System.out.println("\nBackground Process - Bulk Lock/Unlock\n");

        System.out.println("\nTyping in Language\n");
        WebElement language_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[4]/div/div/input[1]"));
        language_field.sendKeys(language);

        System.out.println("\nTyping in URI\n");
        WebElement uri_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[4]/div/div/input[2]"));
        uri_field.sendKeys(uri);

        System.out.println("\nSelecting Lock or Unlock\n");
        if (choice == 1) {
            System.out.println("\nLock selected\n");
            WebElement lock = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[4]/div/div/select/option[1]"));
            lock.click();
        } else if (choice == 0) {
            System.out.println("\nUnlock selected\n");
            WebElement unlock = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[4]/div/div/select/option[2]"));
            unlock.click();
        } else {
            System.out.println("\nYou did not provide an number within the correct range (which is 0-1)\n");
        }

        System.out.println("\nSelecting Bulk Lock/Unlock Button\n");
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[4]/div/div/button"));
        button.click();
    }

    public static void assignContentGroup(WebDriver driver, String language, String uri, String contentGroup) {
        System.out.println("\nBackground Process - Assign Content Group\n");

        System.out.println("\nTyping in Language\n");
        WebElement language_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[5]/div/div/input[1]"));
        language_field.sendKeys(language);

        System.out.println("\nTyping in URI\n");
        WebElement uri_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[5]/div/div/input[2]"));
        uri_field.sendKeys(uri);

        List<WebElement> dropdown = driver.findElements(By.xpath("//*[@id=\"contentGroupId\"]/option"));
        int length = dropdown.size();

        for (int i = 1; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"contentGroupId\"]/option[" + i + "]"));
            String name = option.getText();

            if (name.equals(contentGroup)) {
                System.out.println("\nSelecting " + name + " as content group\n");
                option.click();
            }
        }

        System.out.println("\nSelecting Assign Content Group Button\n");
    }

    public static void processDocumentMedia(WebDriver driver, String language, String uri) {
        System.out.println("\nBackground Process - Process Document Media\n");

        System.out.println("\nTyping in language\n");
        WebElement language_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[6]/div/div/div/input[1]"));
        language_field.sendKeys(language);

        System.out.println("\nTyping in URI\n");
        WebElement uri_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[6]/div/div/div/input[2]"));
        uri_field.sendKeys(uri);

        System.out.println("\nSelecting Process Document Media button\n");
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[6]/div/div/div/button"));
        button.click();
    }

    public static void downloadContent(WebDriver driver, String language, String path) {
        System.out.println("\nBackground Process - Download Content\n");

        System.out.println("\nTyping in Language\n");
        WebElement language_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[7]/div/div/input[1]"));
        language_field.sendKeys(language);

        System.out.println("\nTyping in Path\n");
        WebElement path_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[7]/div/div/input[2]"));
        path_field.sendKeys(path);

        System.out.println("\nSelecting Download Content button\n");
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[7]/div/div/button"));
        button.click();
    }

    public static void cleanUpInProcess(WebDriver driver) {
        System.out.println("\nBackground Process - Clean up In-Process\n");
        System.out.println("\nSelecting Clean up In-Process Button\n");
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[8]/div/button"));
        button.click();
    }

    public static void removePDF(WebDriver driver, String days) {
        System.out.println("\nBackground Process - Remove PDFs\n");

        System.out.println("\nTyping in number of days\n");
        WebElement days_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[9]/div/div/input"));
        days_field.sendKeys(days);

        System.out.println("\nSelecting Remove PDFs button\n");
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[9]/div/div/button"));
        button.click();
    }

    public static void removeDownloadContent(WebDriver driver, String days) {
        System.out.println("\nBackground Process - Remove Download Content\n");

        System.out.println("\nTyping in number of days\n");
        WebElement days_field = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[10]/div/div/input"));
        days_field.sendKeys(days);

        System.out.println("\nSelecting Remove Download Content button\n");
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[10]/div/div/button"));
        button.click();
    }
}
