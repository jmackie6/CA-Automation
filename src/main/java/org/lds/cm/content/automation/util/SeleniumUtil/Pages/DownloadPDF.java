package org.lds.cm.content.automation.util.SeleniumUtil.Pages;

import org.lds.cm.content.automation.util.BrowserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;

public class DownloadPDF {

    private static java.util.Random random = new java.util.Random();

    public static void goToPage (WebDriver driver) {
        System.out.println("\nGoing to Download PDF page\n");
        WebElement page = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"download-pdf--id\"]/div")));
        page.click();
    }

    public static void searchField (WebDriver driver, String searchURI_or_filename) throws InterruptedException{
        System.out.println("\nDownload PDF page - Search field\n");
        WebElement searchField = driver.findElement(By.xpath("//*[@id=\"txtName\"]"));
        searchField.click();

        searchField.sendKeys(searchURI_or_filename);
        searchField.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
    }

    public static void searchButton (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - clicking search button\n");
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[3]/a"));
        searchButton.click();
        Thread.sleep(3000);
    }

    public static void refreshButton (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - clicking refresh button\n");
        WebElement refreshButton = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[2]/div[2]/div[2]/div/div[4]/a")));
        refreshButton.click();
    }

    public static void clearSearchButton (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - clearSearchButton\n");
        WebElement clearSearchButton = driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[5]/a"));
        clearSearchButton.click();
        Thread.sleep(1000);
    }

    public static void datePickerModal (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - opening up date picker modal\n");
        WebElement datePickerModal = driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[2]/p/span/button/i"));
        datePickerModal.click();
        Thread.sleep(1000);
    }

    public static void datePickerField (WebDriver driver, String date) throws InterruptedException{
        System.out.println("\nDownload PDF page - putting in date into the date field\n");
        WebElement datePicker = driver.findElement(By.xpath("/html/body/div/div[1]/div[2]/div[2]/div/div[2]/p/input"));
        datePicker.click();
        datePicker.sendKeys(date);
        datePicker.sendKeys(Keys.ENTER);
        Thread.sleep(1000);
    }


    public static void selectAndPreviewRandomPDF (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - selecting and previewing random pdf\n");

        WebElement documentsTable = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody"));
        List<WebElement> numElements = documentsTable.findElements(By.tagName("tr"));

        int randomRowNumber = random.nextInt(numElements.size());
        if (randomRowNumber == 0)
        {
            randomRowNumber = 1;
        }

        WebElement randomLogFile = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + randomRowNumber + "]/td[2]"));
        randomLogFile.click();
    }


    public static void previewSpecificPDF (WebDriver driver, String uri) throws InterruptedException{
        System.out.println("\nDownload PDF page - download specific pdf\n");

        WebElement table = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody"));
        List<WebElement> rowResults = table.findElements(By.tagName("tr"));

        int row_num,col_num;
        row_num = 1;

        for (WebElement row : rowResults) {
            WebElement URI = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + row_num + "]/td[1]"));
            if (URI.getText().equals(uri)) {
                //*[@id="pdfListResults"]/tbody/tr[2]/td[5]/div/i
                WebElement pdfDownloadButton = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + row_num + "]/td[2]"));
                System.out.println("INFO: uri found to preview the file");
                BrowserUtils.sleep(1000);
                pdfDownloadButton.click();
                break;
            }
            row_num++;
        }
        System.out.println("\nPreview of specific pdf file or uri will open now\n");
        BrowserUtils.sleep(4000);
    }

    public static void previewPDFModalCloseButton (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - close button preview pdf modal\n");
        WebElement closeModal = driver.findElement(By.xpath("//*[@id=\"previewPdfModal\"]/div/div/div[3]/button[1]"));
        closeModal.click();
        Thread.sleep(1000);
    }

    public static void previewPDFModalDownlaodPDFButton (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - download PDF button on preview pdf modal\n");
        WebElement previewPdfDownload = driver.findElement(By.xpath("//*[@id=\"previewPdfModal\"]/div/div/div[3]/button[2]"));
        previewPdfDownload.click();
        Thread.sleep(1000);
    }

    public static void previewPDFModalDownlaodLogFileButton (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - download logfile button on preview pdf modal\n");
        WebElement previewLogFileDownload = driver.findElement(By.xpath("//*[@id=\"previewPdfModal\"]/div/div/div[3]/button[3]"));
        previewLogFileDownload.click();
        Thread.sleep(1000);
    }

    public static void previewPDFModalLogFileTab (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - logfile tab on preview PDF modal\n");
        WebElement logFileTab = driver.findElement(By.xpath("//*[@id=\"logFileTab\"]/a"));
        logFileTab.click();
        Thread.sleep(1000);
    }

    public static void previewPDFModalPDFTab (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - pdf tab on preview pdf modal\n");
        WebElement logFileTab = driver.findElement(By.xpath("//*[@id=\"pdfPreviewTab\"]/a"));
        logFileTab.click();
        Thread.sleep(1000);
    }

    public static void previewPDFModalXButton (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - x button on preview pdf modal\n");
        WebElement datePicker = driver.findElement(By.xpath("//*[@id=\"previewPdfModal\"]/div/div/div[1]/button"));
        datePicker.click();
        Thread.sleep(1000);
    }

    //working but could fail sometimes TODO make it so it skips log files that are not clickable
    public static void downloadRandomPDF (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - download random pdf\n");

        WebElement documentsTable = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody"));
        List<WebElement> documentsTableElements = documentsTable.findElements(By.tagName("tr"));

        boolean rand = true;
        int randomRowNumber = random.nextInt(documentsTableElements.size());
        System.out.println("3");
        WebElement randomPDF = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + randomRowNumber + "]/td[5]/div/i"));
//        WebElement randomPDF = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[4]/td[5]/div/i"));
        //*[@id="pdfListResults"]/tbody/tr[4]/td[6]/div
//        <div class="btn btn-xs btn-info" title="Download log file" ng-disabled="row.logFileId === 0" ng-click="loadLogFile(row)" disabled="disabled">
//                                    <i class="fa fa-file-text-o"></i>
//                                </div>
//                <div class="btn btn-xs btn-info" title="Download log file" ng-disabled="row.logFileId === 0" ng-click="loadLogFile(row)">
//                                    <i class="fa fa-file-text-o"></i>
//                                </div>
//        WebElement disabled = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[4]/td[6]").findElement(By.));
        randomPDF.click();

        System.out.println("Random pdf was successfully download");

//        while(rand != true)
//        {
//            System.out.println("trying to download a random pdf");
////            int randomRowNumber = random.nextInt(documentsTableElements.size());
////            WebElement randomPDF = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + randomRowNumber + "]/[5]/div/i"));
//
//            if (randomPDF.isEnabled())
//            {
//                randomPDF.click();
//
//                System.out.println("Random pdf was successfully download");
//                rand = false;
//
//            }
//
//        }


    }

    //working but could fail sometimes TODO make it so it skips log files that are not clickable
    public static void downloadRandomLogFile (WebDriver driver) throws InterruptedException{
        System.out.println("\nDownload PDF page - download random logfile\n");
        WebElement documentsTable = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody"));
        List<WebElement> documentsTableElements = documentsTable.findElements(By.tagName("tr"));

        boolean rand = true;
        int randomRowNumber = random.nextInt(documentsTableElements.size());

        WebElement randomLogFile = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + randomRowNumber + "]/td[6]/div/i"));

        randomLogFile.click();

        System.out.println("Random pdf was successfully download");
    }

    //this function will take in the uri of the pdf you want to download but will download the pdf where the uri is what you passed in passed
    //TODO make so it can be more specific of what is downloaded like by user, filename and uri
    public static void downloadSpecificPDF (WebDriver driver, String pdfURI) throws InterruptedException{
        System.out.println("\nDownload PDF page - download specific pdf\n");

        WebElement table = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody"));
        List<WebElement> rowResults = table.findElements(By.tagName("tr"));

        int row_num,col_num;
        row_num = 1;

        for (WebElement row : rowResults) {
//*[@id="pdfListResults"]/tbody/tr[2]/td[1]
            WebElement URI = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + row_num + "]/td[1]"));
            if (URI.getText().equals(pdfURI)) {
                //*[@id="pdfListResults"]/tbody/tr[2]/td[5]/div/i
                WebElement pdfDownloadButton = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + row_num + "]/td[5]/div/i"));
                System.out.println("INFO: uri found");
                BrowserUtils.sleep(1000);
                pdfDownloadButton.click();
                break;
            }
            row_num++;
        }
        System.out.println("\ndownload specific pdf completed\n");
        BrowserUtils.sleep(4000);
    }

    //TODO make so it can be more specific of what is downloaded like by user, filename and uri
    public static void downloadSpecificLogFile (WebDriver driver, String logUri) throws InterruptedException{
        System.out.println("\nDownload PDF page - download specific logfile\n");

        WebElement table = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody"));
        List<WebElement> rowResults = table.findElements(By.tagName("tr"));

        int row_num,col_num;
        row_num = 1;

        for (WebElement row : rowResults) {

            WebElement URI = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + row_num + "]/td[1]"));
            if (URI.getText().equals(logUri)) {
                //*[@id="pdfListResults"]/tbody/tr[2]/td[5]/div/i
                WebElement logFileDownloadButton = driver.findElement(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[" + row_num + "]/td[6]/div/i"));
                System.out.println("INFO: uri found");
                BrowserUtils.sleep(1000);
                logFileDownloadButton.click();
                break;
            }
            row_num++;
        }
        System.out.println("\ndownload specific logfile completed\n");
        BrowserUtils.sleep(4000);
    }


    public static void selectUser (WebDriver driver, String user) throws InterruptedException{
        System.out.println("\nDownload PDF page - select user in user dropdown\n");
        WebElement userS = driver.findElement(By.xpath("//*[@id=\"filterUser\"]"));
        Thread.sleep(1000);

        userS.click();
        Select user_selection = new Select(driver.findElement(By.xpath("//*[@id=\"filterUser\"]")));
        user_selection.selectByVisibleText(user);
    }

    public static void nextPageButton (WebDriver driver) throws InterruptedException {

        //use size of div or whatever to know how many li items are in the ul to get the next button
        System.out.println("Validation Report - next page button \n ");
        WebElement results = driver.findElement(By.xpath("/html/body/div/div[1]/div[3]/div/ul"));
        List<WebElement> divResults = results.findElements(By.xpath("li"));
        System.out.println("next page button size of array: " + divResults.size());
        WebElement nextPageButton = driver.findElement(By.xpath("/html/body/div/div[1]/div[3]/div/ul/li[" + divResults.size() + "]/a"));
        nextPageButton.click();
    }

    public static void previousPageButton (WebDriver driver, int numberOfResults) throws InterruptedException {


        System.out.println("Validation Report - previous page button\n ");

        WebElement previousPageButton = driver.findElement(By.xpath("/html/body/div/div[1]/div[3]/div/ul/li[1]/a"));
        previousPageButton.click();
    }



    public static void numberOfResultsPerPage (WebDriver driver, String pageSize) {

        switch (pageSize) {
            case "5":
                WebElement five = (new WebDriverWait(driver, 30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[2]/div[3]/div[2]/div/button[1]")));
                five.click();
                break;
            case "10":
                WebElement ten = (new WebDriverWait(driver, 30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[2]/div[3]/div[2]/div/button[2]")));
                ten.click();
                break;
            case "25":
                WebElement twenty_five = (new WebDriverWait(driver, 30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[2]/div[3]/div[2]/div/button[3]")));
                twenty_five.click();
                break;
            case "50":
                WebElement fifty = (new WebDriverWait(driver, 30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[2]/div[3]/div[2]/div/button[4]")));
                fifty.click();
                break;
            case "100":
                WebElement one_hundred = (new WebDriverWait(driver, 30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[2]/div[3]/div[2]/div/button[5]")));
                one_hundred.click();
                break;
            default:
                Assert.fail("\nThe number given is not an option for results per page.");
        }

        //Assert.assertTrue(count < 5, "\nNumber for results per page not found.");
    }
}
