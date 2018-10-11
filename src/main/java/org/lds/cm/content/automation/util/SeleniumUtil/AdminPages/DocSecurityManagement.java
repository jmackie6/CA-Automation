package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import junit.framework.Assert;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;


public class DocSecurityManagement {

    public static void goToPage (WebDriver driver) {
        System.out.println("\nGoing To Manage Security Page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavManageDocSecurity\"]"));
        page.click();

        BrowserUtils.sleep(1000);
    }

    public static void gotToAddDocumentTab (WebDriver driver) {
        System.out.println("\nSelecting Add Document Tab\n");
        WebElement add_document = driver.findElement(By.xpath("//*[@id=\"searchTabs\"]/li[2]/a"));
        add_document.click();

        BrowserUtils.sleep(1000);
    }

    public static void searchByUri (WebDriver driver, String searchTerm) throws InterruptedException, AWTException {
        System.out.println("\nSearching For URI\n");
        WebElement search_bar = driver.findElement(By.xpath("//*[@id=\"searchBar\"]"));
        search_bar.clear();
        search_bar.sendKeys(searchTerm);

        Thread.sleep(1000);

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);

        WebElement search_button = driver.findElement(By.xpath("//*[@id=\"searchDocuments\"]/div[1]/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[1]"));
        search_button.click();

        Thread.sleep(5000);
    }

    public static void searchByFileId (WebDriver driver, String searchTerm) throws InterruptedException, AWTException {
        System.out.println("\nSearching For File ID\n");
        WebElement file_id_option = driver.findElement(By.xpath("//*[@id=\"searchDocuments\"]/div[1]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[3]/input"));
        file_id_option.click();
        WebElement search_bar = driver.findElement(By.xpath("//*[@id=\"searchBar\"]"));
        search_bar.clear();
        search_bar.sendKeys(searchTerm);

        Thread.sleep(1000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);

        WebElement search_button = driver.findElement(By.xpath("//*[@id=\"searchDocuments\"]/div[1]/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[1]"));
        search_button.click();

        Thread.sleep(5000);
    }

    public static void searchByFileName (WebDriver driver, String searchTerm) throws InterruptedException, AWTException {
        System.out.println("\nSearching For File Name\n");
        WebElement file_name_option = driver.findElement(By.xpath("//*[@id=\"searchDocuments\"]/div[1]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[4]/input"));
        file_name_option.click();
        WebElement search_bar = driver.findElement(By.xpath("//*[@id=\"searchBar\"]"));
        search_bar.clear();
        search_bar.sendKeys(searchTerm);

        Thread.sleep(1000);

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);

        WebElement search_button = driver.findElement(By.xpath("//*[@id=\"searchDocuments\"]/div[1]/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[1]"));
        search_button.click();

        Thread.sleep(5000);
    }

    public static void selectLockedDocument (WebDriver driver, String document) {
        System.out.println("\nSelecting an Entry From the Documents Tab\n");
        WebElement table = driver.findElement(By.xpath("//*[@id=\"documentsTable\"]/tbody"));
        List<WebElement> secured_docs = table.findElements(By.tagName("tr"));

        int row = 1;
        boolean entryFound = false;

        for (WebElement entries : secured_docs) {

            List<WebElement> results = entries.findElements(By.xpath("td"));

            for (int i = 1; i < results.size(); i++) {
              WebElement entry_name = driver.findElement(By.xpath("//*[@id=\"documentsTable\"]/tbody/tr[" + row + "]/td[" + i + "]"));

                if (entry_name.getText().equals(document)) {
                    System.out.println("\nEntry found\n");
                    entryFound = true;
                    entry_name.click();

                    BrowserUtils.sleep(500);
                    break;
                }
            }

            row++;
        }

        if(entryFound == false)
        {
            System.out.println("\nEntry not found\n");
        }
    }

    public static void selectDocumentToLock (WebDriver driver, String document) {
        System.out.println("\nStarting Process to Lock Document\n");
        WebElement table = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody"));
        List<WebElement> searchResults = table.findElements(By.tagName("tr"));

        int row = 1;
        boolean docFound = false;

        for (WebElement documents : searchResults)
        {

            List<WebElement> tdResults = documents.findElements(By.xpath("td"));

            for (int i = 1; i < tdResults.size(); i++)
            {
                WebElement doc_name = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + row + "]/td[" + i + "]"));
                WebElement lock_icon = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + row + "]/td[4]/div/div/span[2]"));

                if (doc_name.getText().equals(document))
                {
                    System.out.println("\nDocument found\n");
                    docFound = true;
                    lock_icon.click();

                    BrowserUtils.sleep(500);
                    break;
                }
            }

            row++;
        }

        if(docFound == false)
        {
            System.out.println("\nDocument not found\n");
        }

    }

    public static void deleteSecuredDocument (WebDriver driver, String document) {
        System.out.println("\nStarting Process to Delete Document\n");
        WebElement table = driver.findElement(By.xpath("//*[@id=\"documentsTable\"]/tbody"));
        List<WebElement> secured_docs = table.findElements(By.tagName("tr"));

        int row = 1;
        boolean docFound = false;

        for (WebElement documents : secured_docs)
        {

            List<WebElement> results = documents.findElements(By.xpath("td"));

            for (int i = 1; i < results.size(); i++)
            {
                WebElement doc_name = driver.findElement(By.xpath("//*[@id=\"documentsTable\"]/tbody/tr[" + row + "]/td[" + i + "]"));
                WebElement delete_icon = driver.findElement(By.xpath("//*[@id=\"documentsTable\"]/tbody/tr[" + row + "]/td[5]/span"));

                if (doc_name.getText().equals(document))
                {
                    System.out.println("\nDocument Deleted\n");
                    docFound = true;
                    delete_icon.click();

                    BrowserUtils.sleep(500);
                    break;
                }
            }

            row++;
        }

        if(docFound == false)
        {
            System.out.println("\nDocument not found\n");
        }

    }

    public static void secureDocToUsers (WebDriver driver, String[] userList) {
        System.out.println("\nSecuring Document to the Users listed\n");
        List<WebElement> users = driver.findElements(By.xpath("/html/body/div[1]/div/div/edit-permissions/div[2]/div[2]/div[2]/div"));
        int user_count = users.size();

        for ( int i = 1; i <= user_count; i++) {
            WebElement user_name = driver.findElement(By.xpath("/html/body/div[1]/div/div/edit-permissions/div[2]/div[2]/div[2]/div[" + i + "]/label"));
            String text = user_name.getText();
            text = text.trim();

            for (int j = 0; j < userList.length; j++) {

                if (text.equals(userList[j]) && !user_name.isSelected()) {

                    WebElement value = driver.findElement(By.xpath("/html/body/div[1]/div/div/edit-permissions/div[2]/div[2]/div[2]/div[" + i + "]/label/input"));
                    System.out.println("\nDocument Secured to: " + userList[j] + "\n");
                    value.click();
                    break;

                }
            }
        }
    }

    public static void secureDocToGroups (WebDriver driver, String[] groupList) {
        System.out.println("\nSecuring Document to the Groups Listed\n");
        List<WebElement> groups = driver.findElements(By.xpath("/html/body/div[1]/div/div/edit-permissions/div[2]/div[2]/div[4]/div"));
        int group_count = groups.size();

        for (int i = 1; i<= group_count; i++) {
            WebElement group_name = driver.findElement(By.xpath("/html/body/div[1]/div/div/edit-permissions/div[2]/div[2]/div[4]/div[" + i + "]/label"));
            String text = group_name.getCssValue("div");
            text = text.trim();

            for (int j = 0; j < groupList.length; j++) {
                if (text.equals(groupList[j]) && !group_name.isSelected()) {

                    WebElement value = driver.findElement(By.xpath("/html/body/div[1]/div/div/edit-permissions/div[2]/div[2]/div[4]/div[" + i + "]/label/input"));
                    System.out.println("\nDocument Secured to: " + groupList[j] + "\n");
                    value.click();
                    break;

                }
            }
        }
    }

    public static void saveAndClose (WebDriver driver) {
        System.out.println("\nSaving Secure Settings\n");
        WebElement save = driver.findElement(By.xpath("/html/body/div[1]/div/div/edit-permissions/div[3]/button[2]"));
        save.click();
    }

    public static void cancel (WebDriver driver) {
        System.out.println("\nCancelling Secure Document\n");
        WebElement cancel = driver.findElement(By.xpath("/html/body/div[1]/div/div/edit-permissions/div[3]/button[1]"));
        cancel.click();
    }

    public static void confirmSecureDocDelete (WebDriver driver) {
        System.out.println("\nConfirming Secure Document Delete\n");
        WebElement confirmDelete = driver.findElement(By.xpath("/html/body/div[1]/div/div/delete-permissions/div[3]/button[2]"));
        confirmDelete.click();
    }

    public static void cancelSecureDocDelete (WebDriver driver) {
        System.out.println("\nCancelling Secure Document Delete\n");
        WebElement cancelDelete = driver.findElement(By.xpath("/html/body/div[1]/div/div/delete-permissions/div[3]/button[1]"));
        cancelDelete.click();
    }

    public static void expandAll (WebDriver driver) {
        System.out.println("\nExpand All Search Results\n");
        WebElement expand = driver.findElement(By.xpath("//*[@id=\"searchDocuments\"]/div[2]/doc-tree/div[2]/div[1]/div[1]/button[1]"));
        expand.click();
    }

    public static void collapseAll (WebDriver driver) {
        System.out.println("\nCollapse All Search Results\n");
        WebElement collapse = driver.findElement(By.xpath("//*[@id=\"searchDocuments\"]/div[2]/doc-tree/div[2]/div[1]/div[1]/button[2]"));
        collapse.click();
    }

    public static void clearSearch (WebDriver driver) {
        System.out.println("\nClearing the Search\n");
        WebElement clear = driver.findElement(By.xpath("//*[@id=\"searchDocuments\"]/div[1]/doc-search/div[1]/div[1]/div[1]/div[2]/div/a[3]"));
        clear.click();
    }

    // WORK IN PROGRESS
     public static void pageNumber (WebDriver driver, String pageNumber) {
//        int result = Integer.parseInt(pageNumber) + 2;
//        String newPageNumber = new Integer(pageNumber).toString();

        System.out.println("Go to specific page Number\n ");
        WebElement page_Number = driver.findElement(By.xpath("//*[@id=\"searchDocuments\"]/div[2]/doc-tree/div[2]/div[3]/div[3]/ul/li[" + pageNumber + "]/a"));

        if (page_Number.isDisplayed()) {
            System.out.println("page is visible and clickable");
            page_Number.click();
        }
        else {
            System.out.println("page is not visible and not clickable");
            Assert.fail();
        }
    }

}
