package org.lds.cm.content.automation.util.SeleniumUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class Print {

    public static void selectCopyFromLanguage (WebDriver driver, String language) {
        System.out.println("\nSelecting Language to Copy From\n");

        WebElement dropdown = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"defaultLanguageSelect\"]")));
        dropdown.click();

        boolean lang_found = true;

        List<WebElement> lang_list = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"defaultLanguageSelect\"]/option")));
        int lang_count = lang_list.size();

        for (int i = 1; i < lang_count; i++) {
            WebElement lang_row = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"defaultLanguageSelect\"]/option[" + i + "]")));
            String lang_type = lang_row.getText();

            if (lang_type.equals(language)) {
                lang_row.click();
            } else {
                lang_found = false;
            }
        }
        if (!lang_found) {
            System.out.println("\nLanguage not found\n");
        }
    }

    public static void selectApplyToLanguage (WebDriver driver, String language) {
        System.out.println("\nSelecting Language to Apply To\n");

        boolean lang_found = true;

        List<WebElement> apply_to = driver.findElements(By.xpath("//*[@id=\"copyToLanguagesSelect\"]/option"));
        int count = apply_to.size();

        for (int i = 1; i <= count; i++) {
            WebElement lang = driver.findElement(By.xpath("//*[@id=\"copyToLanguagesSelect\"]/option[" + i + "]"));
            String text = lang.getText();

            if (text.equals(language)) {
                lang.click();
            } else {
                lang_found = false;
            }
        }
        if (!lang_found) {
            System.out.println("\nLanguage Not Found\n");
        }
    }

    public static void allToPrint (WebDriver driver) {
        System.out.println("\nMoving all documents to Print\n");
        WebElement all_to_print = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[4]")));
        all_to_print.click();
    }

    public static void allToSource (WebDriver driver) {
        System.out.println("\nMoving all documents to Source");
        WebElement all_to_source = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[1]")));
        all_to_source.click();
    }

    public static void selectedToPrint (WebDriver driver) {
        System.out.println("\nMoving selected documents to Print\n");
        WebElement few_to_print = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[3]")));
        few_to_print.click();
    }

    public static void selectedToSource (WebDriver driver) {
        System.out.println("\nMoving selected documents to Source\n");
        WebElement few_to_source = driver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[2]"));
        few_to_source.click();
    }

    public static void selectDocuments (WebDriver driver, String[] documents) {
        System.out.println("\nSelecting specific documents\n");
        List<WebElement> doc_list = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[1]/div/ul/li")));
        int doc_count = doc_list.size();

        for (int i = 1; i <= doc_count; i++) {
            WebElement doc_name = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[1]/div/ul/li[" + i + "]")));
            String doc_info = doc_name.getText();

            for (int j = 0; j < documents.length; j++) {
                if (doc_info.contains(documents[j])) {
                    doc_name.click();
                }
            }
        }
    }

    public static void clickDownload (WebDriver driver) {
        System.out.println("\nSelecting Download button\n");
        WebElement download = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[3]/button[2]")));
        download.click();
    }

    public static void cancel (WebDriver driver) {
        System.out.println("\nSelecting Cancel button\n");
        WebElement cancel = driver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[3]/button[1]"));
        cancel.click();
    }

    public static void xOut (WebDriver driver) {
        System.out.println("\nSelecting x-out button\n");
        WebElement x_out = driver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[1]/button/span"));
        x_out.click();
    }


}
