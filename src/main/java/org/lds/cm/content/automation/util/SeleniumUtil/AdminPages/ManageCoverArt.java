package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ManageCoverArt {

    public static void goToPage(WebDriver driver) {
        System.out.println("\nGoing to Manage Cover Art page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavManageCoverArt\"]"));
        page.click();
    }

    public static void enterFileID(WebDriver driver, String fileID) {
        System.out.println("\nTyping in File ID\n");
        WebElement file_id_field = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div/div[1]/input"));
        file_id_field.sendKeys(fileID);
    }

    public static void enterMediaID(WebDriver driver, String mediaID) {
        System.out.println("\nTyping in Media ID\n");
        WebElement media_id_field = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div/div[2]/input"));
        media_id_field.sendKeys(mediaID);
    }

    public static void selectContentType(WebDriver driver, String contentType) {
        System.out.println("\nSelecting Content Type\n");
        List<WebElement> dropdown = driver.findElements(By.xpath("//*[@id=\"coverArtContentType\"]/option"));
        int length = dropdown.size();

        for (int i = 1; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"coverArtContentType\"]/option[" + i + "]"));
            String type = option.getText();

            if (type.equals(contentType)) {
                option.click();
            }
        }
    }

    public static void clickSearch(WebDriver driver) {
        System.out.println("\nSelecting Search Button\n");
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/div[1]/a"));
        button.click();
    }

    public static void clickRefresh(WebDriver driver) {
        System.out.println("\nSelecting Refresh Button\n");
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/div[2]/a"));
        button.click();
    }

    public static void clearSearch(WebDriver driver) {
        System.out.println("\nSelecting Clear Search\n");
        WebElement clear = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/div[3]/a"));
        clear.click();
    }

    public static void createNew(WebDriver driver, String fileId, String contentType, String mediaId) {
        System.out.println("\nSelecting Create New Button\n");
        WebElement button = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/div[4]/a"));
        button.click();

        System.out.println("\nTyping in File ID for New Cover Art\n");
        WebElement file_id_field = driver.findElement(By.xpath("//*[@id=\"fileId\"]"));
        file_id_field.sendKeys(fileId);

        System.out.println("\nSelecting Content Type\n");
        List<WebElement> dropdown = driver.findElements(By.xpath("//*[@id=\"coverArtContentType\"]/option"));
        int length = dropdown.size();

        for (int i = 1; i <= length; i++) {
            WebElement option = driver.findElement(By.xpath("//*[@id=\"coverArtContentType\"]/option[" + i + "]"));
            String type = option.getText();

            if (type.equals(contentType)) {
                option.click();
            }
        }

        System.out.println("\nTyping in Media ID\n");
        WebElement media_id_field = driver.findElement(By.xpath("//*[@id=\"previewMediaId\"]"));
        media_id_field.sendKeys(mediaId);
    }

    public static void saveAndClose(WebDriver driver) {
        System.out.println("\nSelecting Save and Close\n");
        WebElement save = driver.findElement(By.xpath("//*[@id=\"previewCoverArtModal\"]/div/div/div[3]/button[2]"));
        save.click();
    }

    public static void cancel(WebDriver driver) {
        System.out.println("\nSelecting Cancel\n");
        WebElement cancel = driver.findElement(By.xpath("//*[@id=\"previewCoverArtModal\"]/div/div/div[3]/button[1]"));
        cancel.click();
    }

    public static void xOut(WebDriver driver) {
        System.out.println("\nExiting New Cover Art Modal\n");
        WebElement exit = driver.findElement(By.xpath("//*[@id=\"previewCoverArtModal\"]/div/div/div[1]/button"));
        exit.click();
    }

    public static void previousPage(WebDriver driver) {
        System.out.println("\nSelecting Previous Page Button\n");
        WebElement previous = driver.findElement(By.xpath("/html/body/div/div/div[3]/div/ul/li[1]/a"));
        previous.click();
    }

    public static void nextPage(WebDriver driver) {
        System.out.println("\nSelecting Next Page Button\n");
        WebElement next = driver.findElement(By.xpath("/html/body/div/div/div[3]/div/ul/li[3]/a"));
        next.click();
    }

    public static void resultsPerPage(WebDriver driver, int number) {
        System.out.println("\nSelecting Results Per Page\n");

        if (number == 5) {
            System.out.println("\nListing first 5 results\n");
            WebElement five = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[8]/button"));
            five.click();
        } else if (number == 10) {
            System.out.println("\nListing first 10 results\n");
            WebElement ten = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[7]/button"));
            ten.click();
        } else if (number == 25) {
            System.out.println("\nListing first 25 results\n");
            WebElement twenty_five = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[6]/button"));
            twenty_five.click();
        } else if (number == 50) {
            System.out.println("\nListing first 50 results\n");
            WebElement fifty = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[5]/button"));
            fifty.click();
        } else if (number == 100) {
            System.out.println("\nListing first 100 results\n");
            WebElement one_hundred = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[4]/button"));
            one_hundred.click();
        } else {
            System.out.println("\nThe number given is not an option\n");
        }
    }

    public static void coverArtForEdit(WebDriver driver, String fileId) {
        System.out.println("\nSelecting cover art image for edit by File ID\n");
        List<WebElement> images = driver.findElements(By.xpath("//*[@id=\"coverArtTable\"]/tbody/tr"));
        int length = images.size();

        System.out.println(length);

        for (int i = 1; i <= length; i++) {
            WebElement image = driver.findElement(By.xpath("//*[@id=\"coverArtTable\"]/tbody/tr[" + i + "]/td[2]/p[1]"));
            String text = image.getText();
            String id = text.substring(text.lastIndexOf(" ") + 1);

            if (id.equals(fileId)) {
                image.click();
                System.out.println("\nEditing File ID: " + id);
            }
        }
    }

    public static void processCoverArt(WebDriver driver, String fileId) {
        System.out.println("\nSelecting Covert Art for process\n");
        List<WebElement> images = driver.findElements(By.xpath("//*[@id=\"coverArtTable\"]/tbody/tr"));
        int length = images.size();

        for (int i = 1; i <= length; i++) {
            WebElement image = driver.findElement(By.xpath("//*[@id=\"coverArtTable\"]/tbody/tr[" + i + "]/td[2]/p[1]"));
            String text = image.getText();
            String id = text.substring(text.lastIndexOf(" ") + 1);

            if (id.equals(fileId)) {
                WebElement process = driver.findElement(By.xpath("//*[@id=\"coverArtTable\"]/tbody/tr[" + i + "]/td[3]/div/span[1]/i"));
                process.click();
                System.out.println("\nProcessing File ID: " + id + " \n");
            }
        }
    }

    public static void deleteCoverArt(WebDriver driver, String fileId) {
        System.out.println("\nSelecting Covert Art to Delete\n");
        List<WebElement> images = driver.findElements(By.xpath("//*[@id=\"coverArtTable\"]/tbody/tr"));
        int length = images.size();

        for (int i = 1; i <= length; i++) {
            WebElement image = driver.findElement(By.xpath("//*[@id=\"coverArtTable\"]/tbody/tr[" + i + "]/td[2]/p[1]"));
            String text = image.getText();
            String id = text.substring(text.lastIndexOf(" ") + 1);

            if (id.equals(fileId)) {
                WebElement delete = driver.findElement(By.xpath("//*[@id=\"coverArtTable\"]/tbody/tr[" + i + "]/td[3]/div/span[2]/i"));
                delete.click();
                System.out.println("\nDeleting File ID " + id + " \n");
            }
        }
    }
}
