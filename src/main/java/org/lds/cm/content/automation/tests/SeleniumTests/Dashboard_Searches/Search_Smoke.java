package org.lds.cm.content.automation.tests.SeleniumTests.Dashboard_Searches;

import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.RoleModels.ICS_Admin;
import org.lds.cm.content.automation.model.UserModels.NormanLevy;
import org.lds.cm.content.automation.util.RoleAssignment;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.Dashboard;
import org.lds.cm.content.automation.util.SeleniumUtil.Login;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SeleniumUtil.Search;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Search_Smoke
{
    @Test (groups={"selenium"}, timeOut=180000)
    public void smokeSearch() throws InterruptedException, SQLException, CustomException, NumberFormatException
    {
        NormanLevy norman = new NormanLevy();
        if(!norman.hasUsableClass())
            RoleAssignment.assignRole(norman, new ICS_Admin());

        WebDriver driver = PreTestSetup.setup();

        Login.login(driver, norman.getUsername(), norman.getPassword());

        SetupTests.waitForLoading(driver);
        Search.search(driver, "/general-conference/2017/10");
        SetupTests.waitForLoading(driver);

        List<WebElement> elements = (new WebDriverWait(driver, 10)
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div/div[1]/div[2]/doc-tree/div[2]/div[1]/div[4]/ul/li"))));

        WebElement element = driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/doc-tree/div[2]/div[1]/div[4]/ul/li[" + (elements.size() - 1) + "]/a"));
        int finalPage = Integer.parseInt(element.getText());

        Dashboard.selectingPageSize(driver, "100");
        SetupTests.waitForLoading(driver);

        elements = (new WebDriverWait(driver, 10)
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div/div[1]/div[2]/doc-tree/div[2]/div[1]/div[4]/ul/li"))));

        element = driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/doc-tree/div[2]/div[1]/div[4]/ul/li[" + (elements.size() - 1) + "]/a"));
        int newFinalPage = Integer.parseInt(element.getText());

        //finalPage should be 25 per page and newFinalPage should be 100 per page so
        //newFinalPage should be 1/4 of finalPage.  The .8 addition should be enough to bump it to the next int
        boolean finalChange = ((int) ((finalPage / 4.0) + .8) == newFinalPage);
        Assert.isTrue(finalChange, "Results per page change didn't work correctly.");


        String language = "English";
        Search.searchLanguageTyping(driver, language);
        Search.searchButton(driver);
        SetupTests.waitForLoading(driver);

        WebElement table = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody"));
        List<WebElement> rowResults = table.findElements(By.tagName("tr"));
        boolean notRightLanguage = false;

        int row_num = 1;
        for (WebElement trRow : rowResults) {
            WebElement filenameLink = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + row_num + "]/td[2]/div/span"));
            String fnl = filenameLink.getText();
            if (fnl.length() != 0 && fnl.compareTo(language) != 0) {
                notRightLanguage = true;
                break;
            }
        }

        Assert.isTrue(!notRightLanguage, "Search didn't work correctly for language filtering.");

        int dbcount = 0, CCcount = 0;
        ResultSet rs = JDBCUtils.getResultSet("select count(*) from document where " +
                "path='/general-conference/2017/10' and data_aid is not null and " +
                "content_type != 'front-cover' and language_id=0");
        if(rs.next())  {   dbcount = rs.getInt("count(*)");    }
        rs.close();

        WebElement number = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"search-directive-quick-stats\"]/li[1]/a/span[2]")));
        CCcount = Integer.parseInt(number.getText());
        Assert.isTrue(dbcount == CCcount, "Not enough results came from the search.\tDatabase="
            +dbcount + "\t\tContentCentral=" + CCcount);

        driver.quit();
        JDBCUtils.closeUp();
    }

}
