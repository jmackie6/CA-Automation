package org.lds.cm.content.automation.util.SeleniumUtil;

import org.lds.cm.content.automation.model.SingleSearchObject;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SavedSearchPages
{
    /**Variables*/
    //Dashboard
    private static final String Load_Search_Button = "/html/body/div/div[1]/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/div/button[2]";
    private static final String Create_Search_Button = "/html/body/div[1]/div/div/load-search-modal/div[3]/button[1]";

    //Load Saved Search Screen
    private static final String First_Saved_Search = "//*[@id=\"saved-searches\"]/div[1]/button";
    private static final String First_Edit_Saved_Search = "//*[@id=\"saved-searches\"]/div[2]/div/div[1]/button";
    private static final String First_Delete_Search = "//*[@id=\"saved-searches\"]/div[2]/div/div[2]/button";

    //Edit the Saved Search Screen
    private static final String fileIdRadioButton = "//*[@id=\"editSearchModal\"]/div/div/div[2]/div[3]/div/label[3]/input";
    private static final String fileNameRadioButton = "//*[@id=\"editSearchModal\"]/div/div/div[2]/div[3]/div/label[4]/input";
    private static final String pathRadioButton = "//*[@id=\"editSearchModal\"]/div/div/div[2]/div[3]/div/label[5]/input";
    private static final String URI_Box = "/html/body/div[1]/div/div/edit-search-modal/div[2]/div[1]/div/input";
    private static final String ContentGroups_Box = "//*[@id=\"group_chosen\"]/ul/li/input";
    private static final String Owner_Box = "//*[@id=\"user_chosen\"]/ul/li/input";
    private static final String Language_Box = "//*[@id=\"languages_chosen\"]/ul/li/input";
    private static final String States_Box = "//*[@id=\"states_chosen\"]/ul/li/input";
    private static final String Cancel_Button = "/html/body/div[1]/div/div/edit-search-modal/div[3]/button[1]";
    private static final String Clear_All_Filters = "/html/body/div[1]/div/div/edit-search-modal/div[3]/button[2]";
    private static final String Save_Query = "/html/body/div[1]/div/div/edit-search-modal/div[3]/button[3]";
    private static final String Search = "/html/body/div[1]/div/div/edit-search-modal/div[3]/button[4]";

    //Saving the Saved Search Screen
    private static final String Search_Name_InputBox = "//*[@id=\"savedSearchName\"]";
    private static final String Update_Search = "/html/body/div[1]/div/div/save-search-modal/div[3]/button[3]";
    private static final String create_Search_Save_Button = "/html/body/div[1]/div/div/save-search-modal/div[3]/button[2]";

    /**SingleSearchObject Methods*/
    public static void createSearch(WebDriver driver, SingleSearchObject sso)
    {
        //open_Create_Search(driver);
        fillInSearch(driver, sso);
        editSearch_SaveQuery(driver);

        //new search needs a title, if it was forgotten then put in a search title
        if(sso.getOther().length() != 0)
            enterName(driver, sso.getOther());
        else
            enterName(driver, "New Test Search");

        saveCreatedSearch(driver);
    }

    public static void edit_First_Search(WebDriver driver, SingleSearchObject sso)
    {
        edit_search(driver);
        clear_All_Filters(driver);
        fillInSearch(driver, sso);
        editSearch_SaveQuery(driver);
        updateSearch(driver);
    }

    public static void addToFirstSearch(WebDriver driver, SingleSearchObject sso)
    {
        edit_search(driver);
        fillInSearch(driver, sso);
        editSearch_SaveQuery(driver);
        updateSearch(driver);
    }

    public static void fillInSearch(WebDriver driver, SingleSearchObject sso)
    {
        //fill in the search bar passed on what info was in the sso
        String search = sso.getUri();
        if(sso.getFileID().length() != 0) {
            searchFileId(driver);
            search = sso.getFileID();
        }
        else if(sso.getFileName().length() != 0) {
            searchFileName(driver);
            search = sso.getFileName();
        }
        else if(sso.getPath().length() != 0) {
            searchPath(driver);
            search = sso.getPath();
        }
        editSearch_uri(driver, search);

        if(sso.getContentGroup().length() != 0)
            editSearch_ContentGroups(driver, sso.getContentGroup());
        if(sso.getLanguage().length() != 0)
            editSearch_Languages(driver, sso.getLanguage());
        if(sso.getUser().length() != 0)
            editSearch_Owner(driver, sso.getUser());
        if(sso.getStatus().length() != 0)
            editSearch_States(driver, sso.getStatus());
        BrowserUtils.sleep(500);
    }


    /**Methods for clicking specific buttons*/
    public static void open_Create_Search(WebDriver driver) {

        WebElement create_search_button =(new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(Create_Search_Button)));
        create_search_button.click();
    }

    public static void open_saved_search(WebDriver driver) {
        BrowserUtils.sleep(500);
        WebElement load_search_button =(new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(Load_Search_Button)));
        load_search_button.click();
    }

    public static void saveCreatedSearch(WebDriver driver) {
        WebElement load_search_button =(new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(create_Search_Save_Button)));
        load_search_button.click();
    }

    public static void searchFileId(WebDriver driver) {
        WebElement file_id_radio_button =(new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(fileIdRadioButton)));
        file_id_radio_button.click();
    }

    public static void searchFileName(WebDriver driver) {
        WebElement file_name_radio_button = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(fileNameRadioButton)));
        file_name_radio_button.click();
    }

    public static void searchPath(WebDriver driver) {
        WebElement path_radio_button = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(pathRadioButton)));
        path_radio_button.click();
    }

    //clicks the first created search
    public static void execute_search(WebDriver driver) {
        WebElement created_search = driver.findElement(By.xpath(First_Saved_Search));
        created_search.click();
    }

    public static void load_Specific_Search(WebDriver driver, String searchTitle)
    {
        List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"saved-searches\"]"));
        for(WebElement element: elements)
        {
            if(element.getText().compareTo(searchTitle) == 0)
                element.click();
        }
    }

    //edit the first search
    public static void edit_search(WebDriver driver) {
        WebElement edit_created_search_button =(new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(First_Edit_Saved_Search)));
        edit_created_search_button.click();
    }

    //edit the first search
    public static void clear_All_Filters(WebDriver driver) {
        WebElement edit_created_search_button =(new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(Clear_All_Filters)));
        edit_created_search_button.click();
    }

    //delete the first search
    public static void delete_search(WebDriver driver) {
        WebElement delete_created_search_button = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(First_Delete_Search)));
        delete_created_search_button.click();
    }

    public static void cancel_load_search(WebDriver driver) {
        WebElement cancel_button = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(Cancel_Button)));
        cancel_button.click();
    }

    public static void editSearch_uri(WebDriver driver, String uri)
    {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(URI_Box)));
        element.sendKeys(uri + Keys.ENTER);
    }

    public static void editSearch_ContentGroups(WebDriver driver, String ContentGroup)
    {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(ContentGroups_Box)));
        element.click();
        element.sendKeys(ContentGroup + Keys.ENTER);

    }

    public static void editSearch_Owner(WebDriver driver, String ContentGroup)
    {
        WebElement element = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(Owner_Box)));
        element.click();
        element.sendKeys(ContentGroup + Keys.ENTER);
    }

    public static void editSearch_Languages(WebDriver driver, String language)
    {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(Language_Box)));
        element.click();
        element.sendKeys(language + Keys.ENTER);
    }

    public static void editSearch_States(WebDriver driver, String states)
    {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(States_Box)));
        element.click();
        element.sendKeys(states + Keys.ENTER);
    }

    public static void editSearch_SaveQuery(WebDriver driver)
    {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(Save_Query)));
//        PreTestSetup.mouseMove(element);
        element.click();
    }

    public static void enterName(WebDriver driver, String name) {
        WebElement enter_name_button =(new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(Search_Name_InputBox)));
        //PreTestSetup.mouseMove(enter_name_button);
        enter_name_button.clear();
        enter_name_button.sendKeys(name);
    }

    public static void updateSearch(WebDriver driver)
    {
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(Update_Search)));
        PreTestSetup.mouseMove(element);
        element.click();
    }
}
