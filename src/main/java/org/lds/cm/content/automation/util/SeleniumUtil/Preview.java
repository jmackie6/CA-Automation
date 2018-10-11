package org.lds.cm.content.automation.util.SeleniumUtil;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;


public class Preview {

    //Link search - type in search, click search, select language, click clear search, expand all, collapse all, number of pages, paging, click view links, click copy, click open


    public static void backToManifestButton (WebDriver driver) {
        System.out.println("\nPreview Page - manifest button\n");

        WebElement backToManifestButton = (new WebDriverWait(driver, 60))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"previewDiv\"]/button[1]")));
        backToManifestButton.click();
    }

    public static void editButton (WebDriver driver) throws InterruptedException {
        System.out.println("\nPreview Page - edit button\n");

        if (driver.findElement(By.xpath("//*[@id=\"previewDiv\"]/button[2]")).isDisplayed())
        {
            WebElement edit = driver.findElement(By.xpath("//*[@id=\"previewDiv\"]/button[2]"));
            edit.click();
        }
        else
        {
            System.out.println("\nedit button not displayed \n");
            Assert.fail();
        }


    }

    public static void validateButton (WebDriver driver) {
        System.out.println("\nPreview Page - validate button\n");
        WebElement validate = driver.findElement(By.xpath("//*[@id=\"previewDiv\"]/button[3]"));
        validate.click();
    }


    public static void refreshButton (WebDriver driver) {
        System.out.println("\nPreview Page - refresh button\n");
        if (driver.findElement(By.xpath("//*[@id=\"btnRefresh\"]")).isDisplayed())
        {
            WebElement refresh = driver.findElement(By.xpath("//*[@id=\"btnRefresh\"]"));
            refresh.click();
        }
        else
        {
            System.out.println("\nrefresh button not displayed \n");
            Assert.fail();
        }

    }

    public static void updateMetaData (WebDriver driver) {
        System.out.println("\nPreview Page - add metadata \n");
        WebElement updateMetadata = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"previewDiv\"]/button[5]")));
        updateMetadata.click();
    }

    public static void commentsTab (WebDriver driver) {
        System.out.println("\nPreview Page - comments tab\n");
        WebElement commentsTab = driver.findElement(By.xpath("//*[@id=\"comments-id\"]"));
        commentsTab.click();
    }

    public static void addCommentButton (WebDriver driver) {
        System.out.println("\nPreview Page - add comment button\n");
        WebElement addComment = driver.findElement(By.xpath("//*[@id=\"previewCtrl\"]/div/div[3]/div/button"));
        addComment.click();
    }

    public static void typeComment (WebDriver driver, String comment) {
        System.out.println("\nPreview Page\n");
        WebElement typeComment = driver.findElement(By.xpath("//*[@id=\"new-comment-form\"]/div/textarea"));
        typeComment.click();

        typeComment.sendKeys(comment);
        System.out.println("INFO: Pushing enter");
        typeComment.sendKeys(Keys.ENTER);
    }

    public static void submitComment (WebDriver driver) {
        System.out.println("\nPreview Page - submit button\n");
        WebElement submitComment = driver.findElement(By.xpath("//*[@id=\"new-comment-form\"]/div/button"));
        submitComment.click();
    }

    public static void validationErrorTabs (WebDriver driver) {
        System.out.println("\nPreview Page - view different errors\n");

        if (driver.findElement(By.xpath("//*[@id=\"scripture\"]")).isDisplayed())
        {
            System.out.println("clicking scripture errors tab");
            WebElement validationError = driver.findElement(By.xpath("//*[@id=\"scripture\"]/a"));
            validationError.click();
        }

        if (driver.findElement(By.xpath("//*[@id=\"mediaVE\"]")).isDisplayed())
        {
            System.out.println("clicking media errors tab");
            WebElement validationError = driver.findElement(By.xpath("//*[@id=\"mediaVE\"]/a"));
            validationError.click();
        }

        if (driver.findElement(By.id("customVE")).isDisplayed())
        {
            System.out.println("clicking3");
            WebElement validationError = driver.findElement(By.xpath("//*[@id=\"customVE\"]/a"));
            validationError.click();
        }

        if (driver.findElement(By.id("markup")).isDisplayed())
        {
            System.out.println("clicking markup errors tab");
            WebElement validationError = driver.findElement(By.xpath("//*[@id=\"markup\"]/a"));
            validationError.click();
        }

        if (driver.findElement(By.id("linkVE")).isDisplayed())
        {
            System.out.println("clicking link errors tab");
            WebElement validationError = driver.findElement(By.xpath("//*[@id=\"linkVE\"]/a"));
            validationError.click();
        }

        if (driver.findElement(By.id("schema")).isDisplayed())
        {
            System.out.println("clicking schema errors tab");
            WebElement validationError = driver.findElement(By.xpath("//*[@id=\"linkVE\"]/a"));
            validationError.click();
        }

        if (driver.findElement(By.id("fileVE")).isDisplayed())
        {
            System.out.println("clicking file errors tab");
            WebElement validationError = driver.findElement(By.xpath("//*[@id=\"fileVE\"]/a"));
            validationError.click();
        }

    }

    public static void cssType (WebDriver driver, String cssType) {
        System.out.println("\nPreview Page\n");
        WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"-id\"]"));
        dropdown.click();
        WebElement css = driver.findElement(By.linkText(cssType));
        css.click();
    }

    public static void previousButton(WebDriver driver) {

        WebElement previous = driver.findElement(By.xpath("//*[@id=\"previewDiv\"]/div[1]/button[1]"));

        if (previous.isDisplayed()) {
            ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + previous.getLocation().y + ")");
            previous.click();
        } else {
            Assert.fail("\nPrevious button isn't displayed on the page.\n");
        }
    }

    public static void nextButton(WebDriver driver) {
        WebElement next = driver.findElement(By.xpath("//*[@id=\"previewDiv\"]/div[1]/button[2]"));

        if (next.isDisplayed()) {
            ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + next.getLocation().y + ")");
            next.click();
        } else {
            Assert.fail("\nNext button isn't displayed on the page.\n");
        }
    }
}
