package org.lds.cm.content.automation.util.SeleniumUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class UpdateMetadata {

    public static void searchTerm(WebDriver driver, String term) {

        System.out.println("\nSearching for term to add as metadata\n");
        WebElement search_field = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/div/div/temis-component/div[2]/div[2]/div[1]/div[1]/div[1]/input[1]")));
        search_field.clear();
        System.out.println("\nTyping in term given\n");
        search_field.sendKeys(term);
        System.out.println("\nPushing enter\n");
        search_field.sendKeys(Keys.ENTER);
    }

    public static void selectTerms(WebDriver driver, String[] terms) {

        System.out.println("\nSelecting Term listed\n");

        List<WebElement> results = driver.findElements(By.xpath("/html/body/div[1]/div/div/temis-component/div[2]/div[2]/div[1]/div[2]/label"));
        int size = results.size();

         for (int i = 1; i <= size; i++) {
            WebElement option = driver.findElement(By.xpath("/html/body/div[1]/div/div/temis-component/div[2]/div[2]/div[1]/div[2]/label[" + i + "]"));
            String name = option.getText();
            System.out.println("\nSelecting: "  + name);

            for (int j = 0; j < terms.length; j++) {

                if (name.equals(terms[j])) {
                    WebElement checkbox = driver.findElement(By.xpath("/html/body/div[1]/div/div/temis-component/div[2]/div[2]/div[1]/div[2]/label[" + i + "]/input"));
                    checkbox.click();
                    break;
                }
            }
        }
    }

    public static void selectTerm(WebDriver driver, String term) {


        System.out.println("\nSelecting Term listed\n");

        List<WebElement> results = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[1]/div/div/temis-component/div[2]/div[2]/div[1]/div[1]/div[4]/label")));
        int size = results.size();

        for (int i = 1; i <= size; i++) {
            WebElement option = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/temis-component/div[2]/div[2]/div[1]/div[1]/div[4]/label[" + i + "]")));
            String name = option.getText();

            WebElement checkbox = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/temis-component/div[2]/div[2]/div[1]/div[1]/div[4]/label[" + i + "]/input")));

            if (name.equals(term) && !checkbox.isSelected()) {
                option.click();
                break;
            } else if (name.equals(term) && checkbox.isSelected()){
                option.click();
                option.click(); // clicks twice to remain selected
            }
        }
    }

    public static void selectProjects(WebDriver driver, String[] projects) {
        System.out.println("\nSelecting Project listed\n");

        List<WebElement> results = driver.findElements(By.xpath("//*[@id=\"temisModal\"]/div/div/div[2]/div/div[1]/div[2]/label"));

        int size = results.size();

        for (int i = 1; i <= size; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"temisModal\"]/div/div/div[2]/div/div[1]/div[2]/label[" + i + "]"));
            String name = option.getText();

            for (int j = 0; j < projects.length; j++) {
                if (name.equals(projects[j])) {
                    WebElement checkbox = driver.findElement(By.xpath("//*[@id=\"temisModal\"]/div/div/div[2]/div/div[1]/div[2]/label[" + i + "]/input"));
                    checkbox.click();
                }
            }
        }
    }

    public static void clearResults(WebDriver driver) {
        System.out.println("\nClearing Results");
        WebElement clear = driver.findElement(By.xpath("/html/body/div[1]/div/div/temis-component/div[3]/button[1]"));
        clear.click();
    }

    public static void cancel(WebDriver driver) {
        System.out.println("\nSelecting Cancel");
        WebElement cancel = driver.findElement(By.xpath("/html/body/div[1]/div/div/temis-component/div[3]/button[3]"));
        cancel.click();
    }

    public static void save(WebDriver driver) {
        System.out.println("\nSaving New Metadata");
        WebElement save = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/temis-component/div[3]/button[2]")));
        save.click();
    }

    public static void xOut(WebDriver driver) {
        System.out.println("\nSelecting X to exit\n");
        WebElement exit = driver.findElement(By.xpath("/html/body/div[1]/div/div/temis-component/div[1]/button/span"));
        exit.click();
    }


}
