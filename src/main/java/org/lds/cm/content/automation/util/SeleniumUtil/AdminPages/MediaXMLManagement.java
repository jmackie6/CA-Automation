package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MediaXMLManagement {

    public static void goToPage(WebDriver driver) {
        System.out.println("\nGoing to Manage Media XML page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavMediaXml\"]"));
        page.click();
    }

    public static void searchByFileId(WebDriver driver, String fileId) {
        System.out.println("\nTyping in File ID to search by\n");
        WebElement file_id_field = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[1]/div/input"));
        file_id_field.sendKeys(fileId);
    }

    public static void clickSearch(WebDriver driver) {
        System.out.println("\nSelecting search button\n");
        WebElement search = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/button/span"));
        search.click();
    }

    public static void clickUpload(WebDriver driver) {
        System.out.println("\nSelecting upload button\n");
        WebElement upload = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[4]/div/button/span"));
        upload.click();
    }

    public static void chooseFileToUpload(WebDriver driver) {
        System.out.println("\nSelecting Files to Upload\n");
        // Waiting to finish this function until we are writing a test for this page.
        // Do we want this to upload files locally from whatever laptop it is being run on or the same approach as our transform?
    }

    public static void cancelDocUpload(WebDriver driver) {
        System.out.println("\nCancelling Document Upload\n");
        WebElement cancel = driver.findElement(By.xpath("//*[@id=\"newMediaXmlModal\"]/div/div/div[3]/button[1]"));
        cancel.click();
    }

    public static void saveAndClose(WebDriver driver) {
        System.out.println("\nSaving Document Upload\n");
        WebElement save = driver.findElement(By.xpath("//*[@id=\"newMediaXmlModal\"]/div/div/div[3]/button[2]"));
        save.click();
    }

    public static void xOut(WebDriver driver) {
        System.out.println("\nExiting Upload Modal\n");
        WebElement exit = driver.findElement(By.xpath("//*[@id=\"newMediaXmlModal\"]/div/div/div[1]/button"));
        exit.click();
    }

    public static void viewMediaDoc(WebDriver driver, String fileId) {
        System.out.println("\nSelecting Media XML file to view\n");
        List<WebElement> results = driver.findElements(By.xpath("/html/body/div/div/div[2]/div[3]/div/table/tbody/tr"));
        int length = results.size();

        for (int i = 1; i <= length; i++) {
            WebElement row = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/table/tbody/tr[" + i + "]/td[1]"));
            String file = row.getText();

            if (file.equals(fileId)) {
                System.out.println("\nVieweing File: " + fileId + "\n");
                WebElement view_button = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/table/tbody/tr[" + i + "]/td[6]/span[3]"));
                view_button.click();
            }
        }
    }

    public static void xOutViewModal(WebDriver driver) {
        System.out.println("\nExiting View Media XML File Modal\n");
        WebElement exit = driver.findElement(By.xpath("//*[@id=\"viewFileModal\"]/div/div/div[1]/button"));
        exit.click();
    }

    public static void closeViewModal(WebDriver driver) {
        System.out.println("\nClosing View Media XML File Modal\n");
        WebElement close = driver.findElement(By.xpath("//*[@id=\"viewFileModal\"]/div/div/div[3]/button"));
        close.click();
    }

    public static void processMediaDoc(WebDriver driver, String fileId) {
        System.out.println("\nSelecting Media XML File to process\n");
        List<WebElement> results = driver.findElements(By.xpath("/html/body/div/div/div[2]/div[3]/div/table/tbody/tr"));
        int length = results.size();

        for (int i = 1; i <= length; i++) {
            WebElement row = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/table/tbody/tr[" + i + "]/td[1]"));
            String file = row.getText();

            if (file.equals(fileId)) {
                System.out.println("\nProcessing File: " + fileId + "\n");
                WebElement process_button = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/table/tbody/tr[" + i + "]/td[6]/span[2]"));
                process_button.click();
            }
        }
    }

    public static void deleteMediaDoc(WebDriver driver, String fileId) {
        System.out.println("\nSelecting Media XML File to delete\n");
        List<WebElement> results = driver.findElements(By.xpath("/html/body/div/div/div[2]/div[3]/div/table/tbody/tr"));
        int length = results.size();

        for (int i = 1; i <= length; i++) {
            WebElement row = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/table/tbody/tr[" + i + "]/td[1]"));
            String file = row.getText();

            if (file.equals(fileId)) {
                System.out.println("\nDeleting File: " + fileId + "\n");
                WebElement delete_button = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/table/tbody/tr[" + i + "]/td[6]/span[1]"));
                delete_button.click();
            }
        }
    }

    public static void previousButton(WebDriver driver) {
        System.out.println("\nSelecting Previous Page Button\n");
        WebElement previous = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/div/div/div[1]/ul/li[1]/a"));
        previous.click();
    }

    public static void nextButton(WebDriver driver) {
        System.out.println("\nSelecting Next Page Button\n");
        WebElement next = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/div/div/div[1]/ul/li[2]/a"));
        next.click();
    }

    public static void resultsPerPage(WebDriver driver, int number) {
        System.out.println("\nSelecting Results Per Page\n");

        if (number == 5) {
            System.out.println("\nListing first 5 results\n");
            WebElement five = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/div/div/div[2]/button[1]"));
            five.click();
        } else if (number == 10) {
            System.out.println("\nListing first 10 results\n");
            WebElement ten = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/div/div/div[2]/button[2]"));
            ten.click();
        } else if (number == 25) {
            System.out.println("\nListing first 25 results\n");
            WebElement twenty_five = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/div/div/div[2]/button[3]"));
            twenty_five.click();
        } else if (number == 50) {
            System.out.println("\nListing first 50 results\n");
            WebElement fifty = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/div/div/div[2]/button[4]"));
            fifty.click();
        } else if (number == 100) {
            System.out.println("\nListing first 100 results\n");
            WebElement one_hundred = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div/div/div/div[2]/button[5]"));
            one_hundred.click();
        } else {
            System.out.println("\nThe number given is not an option\n");
        }
    }
}