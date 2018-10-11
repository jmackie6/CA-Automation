package org.lds.cm.content.automation.util.SeleniumUtil.Pages;

import com.sun.istack.internal.NotNull;
import junit.framework.Assert;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class LinkSearch {
    //Link search - type in search, click search, select language, click clear search, expand all, collapse all, number of pages, paging, click view links, click copy, click open

    private static java.util.Random random = new java.util.Random();

    public static void goToPage (WebDriver driver) throws InterruptedException {

        System.out.println("\nLink Search - going to page\n");
        WebElement page = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.LinkSearchButtonXpath)));
        page.click();
        Thread.sleep(1000);

    }

    public static void searchButton (WebDriver driver) throws InterruptedException{

        System.out.println("\nLink Search - pressing the search button\n");
        WebElement searchB = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/div[1]/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[1]")));
            searchB.click();
        Thread.sleep(4000);
    }

    public static void searchField (@NotNull WebDriver driver, String search_term) throws InterruptedException{

        System.out.println("\nLink Search - putting value into search field\n");
        WebElement searchBar = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"searchBar\"]")));
        searchBar.click();

        searchBar.sendKeys(search_term);
        searchBar.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
    }

    //TODO need to finish
    public static void selectLanguage (WebDriver driver, String language){
        System.out.println("\nLink Search - selecting language to search by\n");
        WebElement lang = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"languages_chosen\"]/ul/li/input")));
        lang.click();
        lang.sendKeys(language);
        lang.sendKeys(Keys.ENTER);
    }

    //TODO write bug since there is no clear search button
    public static void clearSearch (WebDriver driver) throws InterruptedException{

        System.out.println("\nLink Search - clear search button\n");
        WebElement clearSearch = driver.findElement(By.xpath(""));
        clearSearch.click();
        Thread.sleep(1000);

    }

    public static void numberOfResults (WebDriver driver, String pageSize) throws InterruptedException{
        Thread.sleep(2000);
        System.out.println("\nLink Search - display a specific number of results\n");
        WebElement res = driver.findElement(By.xpath("//*[@id=\"pageSize_chosen\"]/a/div/b"));
        res.click();
        String show = "Show ";
        String page = " per page";
        String value = "";
        if (pageSize == "25"){
            value = show + pageSize + page;
        }else if (pageSize == "50"){
            value = show + pageSize + page;
        } else if (pageSize == "75") {
            value = show + pageSize + page;
        } else if (pageSize == "100") {
            value = show + pageSize + page;
        } else if (pageSize == "150") {
            value = show + pageSize + page;
        } else if (pageSize == "200") {
            value = show + pageSize + page;
        } else if (pageSize == "300") {
            value = show + pageSize + page;
        }

        System.out.println(value);
        WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"pageSize_chosen\"]/div/ul"));
        System.out.println("INFO: iterating through each result");
        List<WebElement> resultsOptions = dropdown.findElements(By.tagName("li"));
        Boolean resultExist = false;
        for (WebElement option : resultsOptions)
        {
            if (option.getText().equals(value))
            {
                System.out.println("INFO: result found and selected");
                resultExist = true;

                BrowserUtils.sleep(1000);

                option.click(); // click the desired option
                break;
            }
        }

        if (resultExist == false){
            System.out.println("ERROR: no result option for " + pageSize);
            Assert.fail();
        } else {
            System.out.println("INFO: Option chosen");
        }
    }

//TODO finish this function if necessary
    public static void numPage (WebDriver driver, String numPage) throws InterruptedException{

//        System.out.println("\nLink Search - go to a specific page that is available \n");
//        WebElement num = driver.findElement(By.xpath(""));
//        Thread.sleep(1000);
    }
    public static void nextPage (WebDriver driver) throws InterruptedException{

        System.out.println("\nLink Search - next page button\n");
        WebElement pageSelection = driver.findElement(By.xpath("/html/body/div/div/div[4]/doc-tree/div[2]/div[1]/div[4]/ul"));
        List<WebElement> count = pageSelection.findElements(By.tagName("li"));
        System.out.println(count.size());

        int pageIndex = count.size() - 1;

        WebElement nextPage = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[3]/ul/li[" + pageIndex + "]/a"));
        nextPage.click();
    }

    public static void nextPageSection (WebDriver driver) throws InterruptedException{

        System.out.println("\nLink Search - last page section  button\n");
        WebElement pageSelection = driver.findElement(By.xpath("/html/body/div/div/div[4]/doc-tree/div[2]/div[1]/div[4]/ul"));
        List<WebElement> count = pageSelection.findElements(By.tagName("li"));
        System.out.println(count.size());

        WebElement nextPageSelection = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[3]/ul/li[" + count.size() + "]/a"));
        nextPageSelection.click();
    }
    public static void prevPage (WebDriver driver) throws InterruptedException{

        System.out.println("\nLink Search - previous page button\n");
        WebElement previousPage = driver.findElement(By.xpath("/html/body/div/div/div[4]/doc-tree/div[2]/div[1]/div[4]/ul/li[2]/a"));
        previousPage.click();
        Thread.sleep(3000);
    }

    public static void prevPageSection (WebDriver driver) throws InterruptedException{

        System.out.println("\nLink Search - previous page section button\n");
        WebElement previousPage = driver.findElement(By.xpath("/html/body/div/div/div[4]/doc-tree/div[2]/div[1]/div[4]/ul/li[1]/a"));
        previousPage.click();
        Thread.sleep(3000);
    }
//TODO need to finish if this is needed
    public static void languageCount (WebDriver driver) throws InterruptedException{

//        System.out.println("\nLink Search - language count button\n");
//        WebElement s = driver.findElement(By.xpath("/html/body/div[1]/div/div[4]/doc-tree/div[2]/div[1]/div[2]"));
//        Thread.sleep(1000);
    }

    public static void viewLinksButtonRandom (WebDriver driver) throws InterruptedException{

        System.out.println("\nLink Search - view links button on a random result\n");
        Thread.sleep(1000);
        WebElement documentsTable = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody")));
        List<WebElement> numElements = documentsTable.findElements(By.tagName("tr"));

        int randomRowNumber = random.nextInt(numElements.size()) + 1;
        WebElement randomLinksButton = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + randomRowNumber + "]/td[4]/div/a")));
        randomLinksButton.click();
    }


    public static void viewLinksButtonSpecific (WebDriver driver, String filename) throws InterruptedException{

        System.out.println("\nLink Search - view links button of a specific file\n");
        System.out.println(filename);
        WebElement table = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody"));
        List<WebElement> rowResults = table.findElements(By.tagName("tr"));
        System.out.println("number of rows: " + rowResults.size());

        int row_num,col_num;
        row_num = 1;

        for (WebElement row : rowResults) {

            WebElement filenameLink = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + row_num + "]/td[1]/span"));

            if (filenameLink.getText().equals(filename)) {
                System.out.println("INFO: filename found");

                BrowserUtils.sleep(1000);
                WebElement specificLinksButton = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + row_num + "]/td[4]/div/a"));
                specificLinksButton.click();
                System.out.println("\nView Links of specific file completed\n");
                break;
            }
            row_num++;
        }


        BrowserUtils.sleep(4000);
    }

    public static void copyLinkRandom (WebDriver driver) throws InterruptedException{

        System.out.println("\nLink Search - Content links page copy link button random\n");
        Thread.sleep(1000);
        WebElement documentsTable = driver.findElement(By.xpath("/html/body/div/div/div[2]"));

        int randomRowNumber = random.nextInt(7) + 1;
        WebElement copy = (new WebDriverWait(driver, 10))
            .until(ExpectedConditions.presenceOfElementLocated(By.xpath(("/html/body/div/div/div[2]/div/div[" + randomRowNumber + "]/div[3]/button"))));
        copy.click();
    }

    public static void copyLinkSpecific (WebDriver driver, String paragraph) throws InterruptedException{

        System.out.println("\nLink Search - copy link of specific file - content links page\n");
        WebElement table = driver.findElement(By.xpath("/html/body/div/div/div[2]/div"));
        List<WebElement> rowResults = table.findElements(By.tagName("div"));
        System.out.println("number of rows: " + rowResults.size());

        int row_num,col_num;
        row_num = 1;

        for (WebElement row : rowResults) {
            WebElement para = driver.findElement(By.xpath("//*[@id=\"p" + row_num + "\"]"));
            if (para.getText().equals(paragraph)) {
                System.out.println("INFO: paragraph found");

                BrowserUtils.sleep(1000);
                row_num += 1;
                WebElement copy = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[" + row_num + "]/div[3]/button"));
                copy.click();
                System.out.println("\ncopy link of specific paragraph\n");
                break;
            }
            row_num++;
        }


        BrowserUtils.sleep(4000);
    }
}
