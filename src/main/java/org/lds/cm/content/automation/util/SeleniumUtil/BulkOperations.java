package org.lds.cm.content.automation.util.SeleniumUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BulkOperations {

    public static void bulkOperation(WebDriver driver, String bulkOperationName) throws InterruptedException {

        WebElement bulkOperationsTable = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/popup-menu/div[2]/div/div")));
        List<WebElement> bulkOperations = bulkOperationsTable.findElements(By.tagName("a"));

        for (WebElement bulkOperation : bulkOperations) {
            if (bulkOperation.getText().equals(bulkOperationName)) {
                WebElement bulkOperationButton = (new WebDriverWait(driver, 30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/popup-menu/div[2]/div/div/a[" + (bulkOperations.indexOf(bulkOperation) + 1) + "]")));
                bulkOperationButton.click();
                Thread.sleep(1000);
                System.out.println("The " + bulkOperationName + " button was clicked");
                break;
            }
        }
    }

    public static void confirmAction(WebDriver driver) {
        // Will confirm any action in any confirm modal
        System.out.println("\nConfirming Bulk Action\n");
        WebElement confirm = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/bulk-confirm/div[3]/button[2]")));
        confirm.click();
    }

    public static void cancelAction(WebDriver driver) {
        // Will cancel any action in any confirm modal
        System.out.println("\nCancelling Bulk Action\n");
        WebElement cancel = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/bulk-confirm/div[3]/button[1]")));
        cancel.click();
    }

    public static void exitBulkModal(WebDriver driver) {
        // Will exit any confirm modal for bulk actions
        System.out.println("\nExiting Bulk Action Modal\n");
        WebElement exit = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/bulk-confirm/div[1]/button/span")));
        exit.click();
    }
}
