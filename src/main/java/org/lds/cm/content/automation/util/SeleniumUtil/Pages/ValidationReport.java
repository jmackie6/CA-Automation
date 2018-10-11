package org.lds.cm.content.automation.util.SeleniumUtil.Pages;

import java.util.List;

import junit.framework.Assert;

import org.lds.cm.content.automation.util.BrowserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ValidationReport {

    public static void validationPage (WebDriver driver) throws InterruptedException{

        Thread.sleep(2000);
        WebElement validationPage = driver.findElement(By.xpath("//*[@id=\"mainNavValidationReport\"]/a"));

        validationPage.click();
        System.out.println("\nValidation Report - going to page\n");

        Thread.sleep(3000);
    }

    public static void searchField (WebDriver driver, String uri) throws InterruptedException{

        System.out.println("\nValidation Report - putting string value into search field\n");
        WebElement searchField = driver.findElement(By.xpath("//*[@id=\"searchBar\"]"));
        searchField.click();

        searchField.sendKeys(uri);
        searchField.sendKeys(Keys.ENTER);
        Thread.sleep(5000);
    }


    public static void searchFileId(WebDriver driver){
        WebElement file_id_radio_button = driver.findElement(By.xpath("/html/body/div/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[3]/input"));
        file_id_radio_button.click();


        BrowserUtils.sleep(1000);

        if(file_id_radio_button.isSelected()){
            System.out.println("File_id radio button selected on validation report page");
        }
        else {
            System.out.println("Unable to select file_id radio button on validation report page\n");
            Assert.fail();
        }
    }

    public static void searchFileName(WebDriver driver){
        WebElement file_name_radio_button = driver.findElement(By.xpath("/html/body/div/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[4]/input"));
        file_name_radio_button.click();


        BrowserUtils.sleep(1000);

        if(file_name_radio_button.isSelected()){
            System.out.println("File_name radio button selected on validation report page");
        }
        else {
            System.out.println("Unable to select file_name radio button on validation report page\n");
            Assert.fail();
        }
    }

    public static void searchPath(WebDriver driver){
        WebElement file_path_radio_button = driver.findElement(By.xpath("/html/body/div/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[5]/input"));
        file_path_radio_button.click();

        BrowserUtils.sleep(1000);
    }

    public static void searchButton (WebDriver driver) throws InterruptedException{

        System.out.println("\nValidation Report - pressing the search button\n");
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[1]/span"));
        searchButton.click();
        Thread.sleep(1000);
    }

    public static void createSearchButton (WebDriver driver) throws InterruptedException{

        WebElement createSearchButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/a[2]"));
        createSearchButton.click();
        Thread.sleep(1000);
    }

    public static void loadSearchButton (WebDriver driver) throws InterruptedException{

        WebElement loadSearchButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/a[1]"));
        loadSearchButton.click();
        Thread.sleep(1000);
    }

    public static void selectErrorType (WebDriver driver, String errorType) throws InterruptedException{

        WebElement selectErrorType = driver.findElement(By.xpath("//*[@id=\"add_chosen\"]/a/div/b"));
        selectErrorType.click();
        Thread.sleep(1000);

        WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"add_chosen\"]/div/ul"));
        System.out.println("INFO: iterating through each error type");
        List<WebElement> errorOptions = dropdown.findElements(By.tagName("li"));
        Boolean errExist = false;
        for (WebElement option : errorOptions)
        {
            if (option.getText().equals(errorType))
            {
                System.out.println("INFO: error found and selected");
                errExist = true;
                Thread.sleep(1000);
                option.click(); // click the desired option
                break;
            }
        }

        if (errExist == false){
            System.out.println("ERROR: error was misplelled or not capitalized");
            Assert.fail();
        } else {
            System.out.println("INFO: Successful with selecting tha error type given");
        }

        Thread.sleep(1000);
    }

    public static void selectLanguage (WebDriver driver,String language) throws InterruptedException{

        System.out.println("\nValidation Report - Selecting a language\n");
        WebElement selectLanguage = driver.findElement(By.xpath("//*[@id=\"languages_chosen\"]/ul/li/input"));
        selectLanguage.click();
        Thread.sleep(1000);

        WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"languages_chosen\"]/div/ul"));
        System.out.println("INFO: iterating through each language");
        List<WebElement> languageOptions = dropdown.findElements(By.tagName("li"));
        Boolean langExist = false;
        for (WebElement option : languageOptions)
        {
            if (option.getText().equals(language))
            {
                System.out.println("INFO: language found and selected");
                langExist = true;
                Thread.sleep(1000);
                option.click(); // click the desired option
                break;
            }
        }

        if (langExist == false){
            System.out.println("ERROR: Language must contain three letter code like this Navajo (nav) or the language was misplelled");
            Assert.fail();
        } else {
            System.out.println("INFO: Successful with selecting tha language given");
        }

        Thread.sleep(1000);
    }

    public static void clearSearchButton (WebDriver driver) throws InterruptedException{

        System.out.println("Validation Report - clear search button\n ");
        WebElement clearSearch = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/a[3]"));

        if (clearSearch.isDisplayed()) {
            System.out.println("Clear Search button is visible and clickable");
            clearSearch.click();
        }
        else {
            System.out.println("Clear Search button is not visible and not clickable");
            Assert.fail();
        }

        Thread.sleep(1000);
    }

    public static void downloadButton (WebDriver driver) throws InterruptedException {

        System.out.println("Validation Report - download button\n ");
        WebElement downloadButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[2]"));

        if (downloadButton.isDisplayed()) {
            System.out.println("download button is visible and clickable");
            downloadButton.click();
        }
        else {
            System.out.println("download button is not visible and not clickable");
            Assert.fail();
        }

        Thread.sleep(1000);

    }

    public static void nextPageOfResults (WebDriver driver) throws InterruptedException {


        System.out.println("Validation Report - next page button\n ");
        WebElement results = driver.findElement(By.xpath("/html/body/div/div/div[4]/div/ul"));
        List<WebElement> divResults = results.findElements(By.xpath("li"));
        System.out.println("next page button size of array: " + divResults.size());
        WebElement nextPageButton = driver.findElement(By.xpath("/html/body/div/div/div[4]/div/ul/li[" + divResults.size() + "]/a"));
        nextPageButton.click();
    }

    public static void previousPageOfResults (WebDriver driver, int numberOfResults) throws InterruptedException {


        System.out.println("Validation Report - previous page button\n ");

        WebElement previousPageButton = driver.findElement(By.xpath("/html/body/div/div/div[4]/div/ul/li[1]/a"));
        previousPageButton.click();
    }



    public static void numberOfResultsPerPage (WebDriver driver, String pageSize) throws InterruptedException {

        Thread.sleep(1000);
        //use size of div or whatever to know how many li items are in the ul to get the next button
        System.out.println("Validation Report - number of errors displayed on a page\n ");

        for (int i = 1; i <= 7; i++)
        {
            WebElement page = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[" + i + "]/button"));
            String value = page.getText();
            value = value.trim();

            if (pageSize.equals(value))
            {
                System.out.println("page size found\n ");
                WebElement pageButton = driver.findElement(By.xpath("/html/body/div/div/div[4]/div[" + i + "]/button"));
                pageButton.click();
                System.out.println("page size button clicked\n ");
            }
        }

    }

}
