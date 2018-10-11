package org.lds.cm.content.automation.util.SeleniumUtil.Pages;

import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ScriptureLink {

    public static void goToPage(WebDriver driver) {
        System.out.println("\nGoing to Scripture Link Page\n");
        WebElement to_page = driver.findElement(By.xpath(MenuButtonConstants.ScriptureLinkButtonXpath));
        to_page.click();
    }

    public static void selectTestament(WebDriver driver, String testament) {
        System.out.println("\nSelecting Testament listed\n");
        List<WebElement> dropdown = (new WebDriverWait(driver, 10))
                .until (ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"testament\"]/option")));
        int size = dropdown.size();
        for (int i = 1; i <= size; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"testament\"]/option[" + i + "]"));
            String name = option.getText();

            if (name.equals(testament)) {
                option.click();
            }

        }
    }

    public static void selectBook(WebDriver driver, String book) {
        System.out.println("\nSelecting Book listed\n");
        List<WebElement> dropdown = (new WebDriverWait(driver, 10))
                .until (ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(("//*[@id=\"book\"]/option"))));
        int size = dropdown.size();
        for (int i = 2; i <= size; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"book\"]/option[" + i + "]"));
            String name = option.getText();

            if (name.equals(book)) {
                option.click();
            }
        }
    }

    /**For some reason the setup of select testament and book didn't work for chapter. */
    public static void selectChapter(WebDriver driver, String chapter) {
        System.out.println("\nSelecting Chapter listed\n");

        WebElement resultsPerPage = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"chapter\"]")));
        resultsPerPage.click();
        Select rpp  = new Select(resultsPerPage);

        List<WebElement> elements = rpp.getOptions();
        System.out.println(chapter);
        if(elements.size() < 5)
            for(WebElement we : elements)
                System.out.println(we.getText());
        rpp.selectByVisibleText(chapter);

       /** List<WebElement> dropdown = (new WebDriverWait(driver, 10))
                .until (ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(("//*[@id=\"chapter\"]/option"))));

        int size = dropdown.size();
        for (int i = 1; i <= size; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"chapter\"]/option[" + i + "]"));
            String name = option.getText();

            if (name.compareToIgnoreCase(chapter) == 0) {
                option.click();
            }
        }*/
    }

    public static void enterVerses(WebDriver driver, String verses) {
        System.out.println("\nEntering Verses listed\n");

        WebElement verses_field = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"verse\"]")));
        verses_field.clear();
        verses_field.sendKeys(verses + Keys.ENTER);
    }

    public static void copyLink(WebDriver driver) {
        System.out.println("\nCopying Scripture Link\n");
        WebElement copy = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"scriptureModal\"]/div/div/div[2]/div[2]/div/button/i")));
        copy.click();
    }

    public static void clearResults(WebDriver driver) {
        System.out.println("\nClearing Scripture Link Results\n");
        WebElement clear = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"scriptureModal\"]/div/div/div[3]/button[1]")));
        clear.click();
    }

    public static void cancel(WebDriver driver) {
        System.out.println("\nSelecting cancel to exit scripture link modal\n");
        WebElement cancel = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"scriptureModal\"]/div/div/div[3]/button[1]")));
        cancel.click();
    }

    public static void exit(WebDriver driver) {
        System.out.println("\nSelecting X to exit scripture link modal\n");
        WebElement exit = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"scriptureModal\"]/div/div/div[1]/button")));
        exit.click();
    }

    public static String getURI(WebDriver driver){
        WebElement uri = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.id("scriptureURI")));
        uri.click();
        return uri.getAttribute("value");
    }
}
