package org.lds.cm.content.automation.util.SeleniumUtil.Pages;

import junit.framework.Assert;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PublishHistory {

    private static java.util.Random random = new java.util.Random();

    //TODO need to add option for searching by fileId, filename and path
    public static void publishHistoryPage (WebDriver driver) throws InterruptedException {
        System.out.println("\ngoing to publish history page\n");
        WebElement publishHistoryPage = driver.findElement(By.xpath("//*[@id=\"mainNavPublishHistory\"]/a"));
        publishHistoryPage.click();
        Thread.sleep(3000);
    }

    public static void searchField (WebDriver driver, String search_term) throws InterruptedException {
        System.out.println("\nTyping content into search field\n");
        WebElement searchBar = driver.findElement(By.xpath("//*[@id=\"searchBar\"]"));
        searchBar.click();

        searchBar.sendKeys(search_term);
        searchBar.sendKeys(Keys.ENTER);

        Thread.sleep(1000);
    }

    public static void searchFileId(WebDriver driver){
        WebElement file_id_radio_button = driver.findElement(By.xpath("/html/body/div/div/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[3]/input"));
        file_id_radio_button.click();

        BrowserUtils.sleep(1000);

        if(file_id_radio_button.isSelected()){
            System.out.println("File_id radio button selected on dashboard");
        }
        else {
            System.out.println("Unable to select file_id radio button on publish history page\n");
            Assert.fail();
        }
    }

    public static void searchFileName(WebDriver driver){
        WebElement file_name_radio_button = driver.findElement(By.xpath("/html/body/div/div/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[4]/input"));
        file_name_radio_button.click();

        BrowserUtils.sleep(1000);

        if(file_name_radio_button.isSelected()){
            System.out.println("File_name radio button selected on publish history page");
        }
        else {
            System.out.println("Unable to select file_name radio button on dashboard\n");
            Assert.fail();
        }
    }

    public static void searchPath(WebDriver driver){
        WebElement file_path_radio_button = driver.findElement(By.xpath("/html/body/div/div/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[5]/input"));
        file_path_radio_button.click();
        BrowserUtils.sleep(1000);
    }
    public static void searchButton (WebDriver driver) throws InterruptedException {
        System.out.println("\nClicking the search button on publish history page\n");
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[1]"));
        searchButton.click();
        Thread.sleep(5000);
    }


    public static void selectUser(WebDriver driver, String user){
        System.out.println("\nSelecting user on dashboard\n");

        System.out.println("INFO: Selecting user dropdown");
        WebElement select_users = driver.findElement(By.xpath("//*[@id=\"user_chosen\"]/a/span"));
        select_users.click();
        BrowserUtils.sleep(1000);


        System.out.println("INFO: finding element with the list of users");
        BrowserUtils.sleep(1000);


        WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"user_chosen\"]/div/ul"));
        System.out.println("INFO: iterating through each user");
        List<WebElement> userOptions = dropdown.findElements(By.tagName("li"));
        Boolean userExist = false;
        for (WebElement option : userOptions)
        {
            if (option.getText().equals(user))
            {
                System.out.println("User found and selected");
                userExist = true;
                BrowserUtils.sleep(1000);

                option.click(); // click the desired option
                break;
            }
        }

        if (userExist == false){
            System.out.println("ERROR: user does not exist or the user passed in was misspelled\nFirst letter is capitalized and same applies if there is a last name");
            Assert.fail();
        }

        BrowserUtils.sleep(1000);

    }

    public static void selectUserBySearching(WebDriver driver, String user) {
        System.out.println("\nSelecting user by searching\n");

        BrowserUtils.sleep(1000);

        System.out.println("INFO: Selecting user dropdown");
        WebElement select_users_dropdown = driver.findElement(By.xpath("//*[@id=\"user_chosen\"]/a/span"));
        select_users_dropdown.click();
        BrowserUtils.sleep(1000);


        WebElement input_users = driver.findElement(By.xpath("//*[@id=\"user_chosen\"]/div/div/input"));
        input_users.clear();
        input_users.sendKeys(user);
        BrowserUtils.sleep(1000);


        System.out.println("INFO: Pushing enter");

        input_users.sendKeys(Keys.ENTER);
        BrowserUtils.sleep(1000);

    }

    public static void searchLanugage (WebDriver driver, String language) {


        System.out.println("\nSearch language by selecting from dropdown and not typing language\n");
        WebElement languageSearch = driver.findElement(By.xpath("//*[@id=\"languages_chosen\"]/a/div/b"));
        languageSearch.click();

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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    public static void clearSearchButton (WebDriver driver) {

        System.out.println("Publish history - clear search button\n ");
        WebElement clearSearch = driver.findElement(By.xpath("/html/body/div[2]/div/doc-search/div[1]/div[1]/div[1]/div[2]/div/a[3]"));

        if (clearSearch.isDisplayed()) {
            System.out.println("Clear Search button is visible and clickable");
            clearSearch.click();
        }
        else {
            System.out.println("Clear Search button is not visible and not clickable");
            Assert.fail();
        }
    }

    public static void nextPageOfResults (WebDriver driver) throws InterruptedException {

        //use size of div or whatever to know how many li items are in the ul to get the next button
        System.out.println("Publish History - next page button\n ");
        WebElement results = driver.findElement(By.xpath("/html/body/div/div/div[3]/ul"));
        List<WebElement> divResults = results.findElements(By.xpath("li"));
        System.out.println("next page button size of array: " + divResults.size());
        WebElement nextPageButton = driver.findElement(By.xpath("/html/body/div/div/div[3]/ul/li[" + divResults.size() + "]/a"));
        nextPageButton.click();
    }

    public static void previousPageOfResults (WebDriver driver) throws InterruptedException {


        System.out.println("Publish History - previous page button\n ");
        WebElement previousPageButton = driver.findElement(By.xpath("/html/body/div/div/div[3]/ul/li[1]/a"));

        previousPageButton.click();
    }



    public static void numberOfResultsPerPage (WebDriver driver, String pageSize) throws InterruptedException {

        Thread.sleep(1000);
        //use size of div or whatever to know how many li items are in the ul to get the next button
        System.out.println("Publish History - number of errors displayed on a page\n ");

        for (int i = 1; i <= 7; i++)
        {
            WebElement page = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[" + i + "]/button"));
            String value = page.getText();
            value = value.trim();

            if (pageSize.equals(value))
            {
                System.out.println("page size found\n ");
                WebElement pageButton = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[" + i + "]/button"));
                pageButton.click();
                System.out.println("page size button clicked\n ");
            }
        }

    }

    public static void previewRandomPublish (WebDriver driver) throws InterruptedException {

        System.out.println("Publish History - preview random result \n ");

        WebElement documentsTable = driver.findElement(By.xpath("//*[@id=\"publishHistoryTable\"]/table/tbody"));
        List<WebElement> numElements = documentsTable.findElements(By.tagName("tr"));

        int randomRowNumber = random.nextInt(numElements.size()) + 1;

        WebElement randomLogFile = driver.findElement(By.xpath("//*[@id=\"publishHistoryTable\"]/table/tbody/tr[" + randomRowNumber + "]/td[3]"));
        randomLogFile.click();

    }
}
