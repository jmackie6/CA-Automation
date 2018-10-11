package org.lds.cm.content.automation.util;

import com.google.common.base.Function;
import org.junit.Test;
import org.lds.cm.content.automation.model.UserModels.UserBaseClass;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.GroupManagement;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.UserManagement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;


/**These are test cases that can be called at the beginning of test suites
 *      - to prep the database
 *      - to get users set up
 *      - wait for the loading page to disappear so that buttons can be clickable
 *      - to get everything else set up that may be needed
 */
public class SetupTests
{

    /**This test will take all locked documents and put them in a validated state
     *
     * If prod was copied recently and/or you haven't run this in a while you may want to run it manually first
     *      It could take some time to run if it has to do 615,000+ files in a run...  Oracle sql developer can do
     *      that much faster.  If it is less then 100, this will run fast enough.
     */
    @Test
    public void unlockAllDocuments()
    {
        try {

            //VALIDATED == 3
            //not likely to change, but just in case the first query will get that value from the db
            String query = "select * from Document_state where document_state = 'VALIDATED'";
            ResultSet rs = JDBCUtils.getResultSet(query);

            int ValidatedState = -100;
            while(rs.next())
                ValidatedState = rs.getInt("Document_State_Id");

            //LOCKED == 2
            //also not likely to change, but just in case
            query = "select * from Document_state where document_state = 'LOCKED'";
            int LockedState = -100;
            ResultSet rs2 = JDBCUtils.getResultSet(query);
            while(rs2.next())
                LockedState = rs2.getInt("Document_State_Id");



            //if either of the states came back incorrectly break here
            if(ValidatedState == -100 || LockedState == -100)
                throw new SQLException("Error with one of the select calls...");

            //an update with a where will update all rows in the table
            query = "update Document set document_state_id = " + ValidatedState + " where document_state_id = " + LockedState;

            //an update query doesn't return anything in the result set
            JDBCUtils.executeUpdate(query);
        }
        catch(Exception e)
        {
            //automatic fail
            Assert.isTrue(1 == 2, "unable to unlock all docs in the database\n" + e.getMessage());
        }
    }

    //Wait for the loading page to disappear.  Technically can wait up to 5 minutes if it doesn't fail (but unlikely)
    public static void waitForLoading(WebDriver webDriver)
    {
        for(int i = 0; i < 30; i++)
        {
            try{
                WebElement element = (new WebDriverWait(webDriver, 5))
                        .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.MainMenuButtonXpath)));
                element.click();
                element.click();
                return;
            }catch(Exception e){BrowserUtils.sleep(1000);}
        }

//        for(int i = 0; i < 30; i++)
//        {
//            try{
//                List<WebElement> elements = (new WebDriverWait(webDriver, 5))
//                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='loading']")));
//               // System.out.println(i + "\t" + elements.size());
//                BrowserUtils.sleep(1000);
//            }catch(Exception e) {return;}
//        }

        /**
        WebElement element = null;
        for(int i =0; i < 30; i++)
        {
            element =(new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"content-header\"]")));

            if(element.getAttribute("class").compareTo("loading ng-hide") == 0)
                i = 300;
            try{   Thread.sleep(1000);} catch(Exception e){e.printStackTrace();}
        }
         */
    }

    /* waits for loading to disappear on Dashboard and in other areas of the app */
    public static void waitTilLoad(WebDriver driver) {
        WebElement element = null;
        for(int i =0; i < 30; i++)
        {
            element =(new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"dashboard-id\"]")));

            if(element.getAttribute("class").compareTo("loading ng-hide") == 0)
                i = 300;
            try{   Thread.sleep(1000);} catch(Exception e){e.printStackTrace();}
        }
    }

    /* waits for loading to disappear on Preview page and in other areas of the app */
    public static void waitTilLoad2(WebDriver driver) {
        WebElement element = null;
        for(int i =0; i < 30; i++)
        {
            element =(new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"dashboard-id\"]")));

            if(element.getAttribute("class").compareTo("text-muted loading-spinner ng-hide") == 0)
                i = 300;
            try{   Thread.sleep(1000);} catch(Exception e){e.printStackTrace();}
        }
    }

    /* could be used for any modals being opened, this function helps timing from being thrown off */
    public static void waitForOpenModal(WebDriver driver) {
        WebElement element = null;
        for(int i =0; i < 30; i++)
        {
            element =(new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body")));

            if(element.getAttribute("class").compareTo("modal-open") == 0)
                i = 300;
            try{   Thread.sleep(1000);} catch(Exception e){e.printStackTrace();}
        }
    }
}
