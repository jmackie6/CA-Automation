package org.lds.cm.content.automation.tests.SeleniumTests.Dashboard_Searches;

import org.lds.cm.content.automation.enums.SearchType;
import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.RoleModels.ICS_Admin;
import org.lds.cm.content.automation.model.SingleSearchObject;
import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.*;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search_Load {

    private WebDriver driver = null;
    private SingleSearchObject ssoCreate, ssoLoad;

    public Search_Load(){}

    @AfterMethod (alwaysRun = true)
    public void ensureDriverQuit(){  JDBCUtils.closeUp(); driver.quit();  }

    @Test (groups={"selenium"}, timeOut = 200000, priority = 1)
    public void smokeLoadSearch() throws InterruptedException, SQLException, CustomException
    {
        //Setup the test...
        NormanLevy norman = new NormanLevy();
//        FonoSaia fono = new FonoSaia();
        driver = PreTestSetup.setup();
        if(!norman.hasUsableClass())
            RoleAssignment.assignRole(norman, new ICS_Admin());
        Login.login(driver, norman.getUsername(), norman.getPassword());

        ArrayList<String> errors = new ArrayList<>();
        ssoCreate = ssoCreateSearch();
        ssoLoad = ssoLoadSearch();

        SetupTests.waitForLoading(driver);

        //first verify that there are no other saved searches.  If there is a saved search of the same name the test will fail
        SavedSearchPages.open_saved_search(driver);

        //delete all of the previously saved searches.  If none exist it will throw an error
        try {
            List<WebElement> wes = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"saved-searches\"]/div[2]/div/div[2]/button")));
            for (WebElement element : wes) {
                element.click();
            }
        } catch(Exception e){ }

        /** Wait a moment before clicking on create search button.  If searches are being deleted, the box is changing shape*/
        BrowserUtils.sleep(500);
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/load-search-modal/div[3]/button[1]")));
        element.click();

        //create a search
        SetupTests.waitForLoading(driver);
        SavedSearchPages.createSearch(driver, ssoCreate);
        SetupTests.waitForLoading(driver);


        element = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[1]/div[1]")));
        Assert.isTrue(element.getText().contains("Document Dashboard"), "Error creating the Search: \n");
        Assert.isTrue(errors.size() == 0, stringify(errors));
    }

    @Test (groups={"selenium"}, timeOut = 200000, priority = 2)
    public void editCreatedSearch() {
        ArrayList<String> errors = new ArrayList<>();
        SetupTests.waitForLoading(driver);
        BrowserUtils.sleep(500);
        WebElement element = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[1]/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/div/button[4]")));
        element.click();
        SavedSearchPages.edit_First_Search(driver, ssoLoad);


        SetupTests.waitForLoading(driver);
        BrowserUtils.sleep(500);  /** Code needs a brief pause or it doesn't find the next button*/
        element = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/div/button[4]")));
        element.click();
        SavedSearchPages.addToFirstSearch(driver, ssoCreate);

        //verify that the search has loaded (Should be only 2 docs chosen)
        SetupTests.waitForLoading(driver);

        //there should be no errors, but if there are put them in a string and output them
        driver.close();
        JDBCUtils.closeUp();
        Assert.isTrue(errors.size() == 0, stringify(errors));
    }

    public SingleSearchObject ssoCreateSearch()
    {
        Map<SearchType, String> ssoInput = new HashMap<SearchType, String>();
        ssoInput.put(SearchType.uri, "/general-conference/2015/04");
        ssoInput.put(SearchType.contentGroup, "Ensig"); //For some reason Engisn causes automation issues
        ssoInput.put(SearchType.user, "Scott Welty");
        ssoInput.put(SearchType.language, "Spanish");
        ssoInput.put(SearchType.other, "Test Search");
        return new SingleSearchObject(ssoInput);
    }

    public SingleSearchObject ssoLoadSearch()
    {
        Map<SearchType, String> ssoInput = new HashMap<>();
        ssoInput.put(SearchType.uri, "/friend/2002/04/funstuf");
        ssoInput.put(SearchType.contentGroup, "friend");
        ssoInput.put(SearchType.language, "English");
        ssoInput.put(SearchType.other, "Test Search");
        return new SingleSearchObject(ssoInput);
    }

    public String stringify(ArrayList<String> errors)
    {
        StringBuilder sb = new StringBuilder("There was an error:\n");
        for (String x : errors)
            sb.append(x + "\n");
        return sb.toString();
    }
}
