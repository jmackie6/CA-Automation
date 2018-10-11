package org.lds.cm.content.automation.tests.SeleniumTests;

import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.RoleModels.Editor;
import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.Pages.LinkSearch;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinkSearchUI
{
    private static final String query = "select * from \n" +
            "(select d.path, l.lang_name from document d join language l\n" +
            " on l.language_code=d.language_id order by dbms_random.value)\n" +
            "where rownum <= 1";
    private WebDriver webDriver;


    @Test (groups={"selenium"}, timeOut=180000)
    public void linkSearchTest() throws SQLException, InterruptedException, CustomException
    {
        /** Normal Setup */
        NormanLevy normanLevy = new NormanLevy();
        if(!normanLevy.hasClass("contributor") && !normanLevy.hasClass("editor"))
            RoleAssignment.assignRole(normanLevy, new Editor());

        webDriver = PreTestSetup.setup();
        Login.login(webDriver, normanLevy.getUsername(), normanLevy.getPassword());

        /** Attempt 10 database calls.   (If not enough info is returned null is sent back.)*/
        ArrayList<String> variables = null;
        int count = 0;
        while(variables == null)
        {
            variables = determineVariables();
            count++;
            if(count == 10)
                throw new CustomException("Made 10 bad database calls.  Please try again");
        }

        /** Navigate to the Link Search Page */
        SetupTests.waitForLoading(webDriver);
        WebElement menuButtons = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
        menuButtons.click();
        menuButtons = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.LinkSearchButtonXpath)));
        menuButtons.click();

        /** Enter the path, language and click search */
        SetupTests.waitForLoading(webDriver);
        LinkSearch.searchField(webDriver, variables.get(0));
        LinkSearch.selectLanguage(webDriver, variables.get(1));  //issues selecting language 4/18
        LinkSearch.searchButton(webDriver);
        SetupTests.waitForLoading(webDriver);

        /** Choose a random row in the table*/
        Random random = new Random();
        WebElement documentsTable = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody")));
        List<WebElement> numElements = documentsTable.findElements(By.tagName("tr"));

        //if the number of elements is negative it breaks the random generator, so wait a sec and try again.
        if(numElements.size() <= 0)
        {
            BrowserUtils.sleep(1000);
            documentsTable = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody")));
            numElements = documentsTable.findElements(By.tagName("tr"));

        }
        int randomRowNumber = random.nextInt(numElements.size()) + 1;
        String fileName = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + randomRowNumber + "]/td[1]/span"))).getText();
        WebElement randomLinksButton = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + randomRowNumber + "]/td[4]/div/a")));
        randomLinksButton.click();

        SetupTests.waitForLoading(webDriver); //takes too long, so just used this below call for waiting
        //WebElement waiting = (new WebDriverWait(webDriver, 20))
        //        .until(ExpectedConditions.presenceOfElementLocated(By.id("snippet-0")));
        try
        {
            /** Check every copyable link on the page to make sure that the path+fileName are contained in the link*/
            count = 0;
            while(true)
            {
                String link = webDriver.findElement(By.id("snippet-" + count)).getText();
                if(!link.contains(variables.get(0)) || !link.contains(fileName))
                    throw new CustomException("link did not contain correct information\n" + link + "\n" + variables.get(0) + "\t" + fileName);
               count++;
            }
        }
        catch(Exception e)
        {
            /** Couldn't find a way to get a list of all snippets, so an exception will be thrown when it tries to find
             *   more then are there.  But if a customeException knowingly thrown comes down, throw it and fail the test*/
            if(e instanceof CustomException)
                throw e;
        }

        webDriver.navigate().back();

        SetupTests.waitForLoading(webDriver);
        /** If you survive to here you must have passed*/
        documentsTable = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody")));
        int numElementsCount = documentsTable.findElements(By.tagName("tr")).size();
        Assert.isTrue(numElementsCount == numElements.size(), "back button didn't go back to a still loaded Link Search page");

    }

    //pull information from the database.  Language has to be put together to fit the pattern "English (eng)"
    private ArrayList<String> determineVariables() throws SQLException
    {
        ArrayList<String> answers = new ArrayList<String>();
        ResultSet rs = JDBCUtils.getResultSet(query);
        if(rs.next())
        {
            answers.add(rs.getString("Path"));
            answers.add(rs.getString("lang_name"));
        }rs.close();
        if(answers.size() != 2)
            return null;

        return answers;
    }

    @AfterMethod (alwaysRun = true)
    public void cleanUp()
    {
        JDBCUtils.closeUp();
        webDriver.close();
    }
}
