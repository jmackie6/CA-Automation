package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.lds.cm.content.automation.util.BrowserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;

public class CSSManagement {

    public static void goToPage (WebDriver driver) throws InterruptedException {
        System.out.println("\nManage CSS -  Going to page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavManageCss\"]"));
        page.click();
        Thread.sleep(3000);
    }

    public static void createNewCSS (WebDriver driver) throws InterruptedException {
        System.out.println("\nManage CSS - pressing the create new css button\n");
        WebElement createNewCss = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/a"));
        createNewCss.click();
        Thread.sleep(1000);
    }

    public static void deleteCSS (WebDriver driver, String cssName) throws InterruptedException {
        System.out.println("\nManage CSS - deleting a specific css by css name\n");

        WebElement table = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/table/tbody"));
        List<WebElement> rowResults = table.findElements(By.tagName("tr"));

        int row_num,col_num;
        row_num = 1;

        for (WebElement row : rowResults) {
            WebElement deleteCssName = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/table/tbody/tr[" + row_num + "]/td[2]"));
            if (deleteCssName.getText().equals(cssName)) {

                WebElement deleteButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/table/tbody/tr[" + row_num + "]/td[7]/span[1]/i"));

                System.out.println("CSS name found");

                BrowserUtils.sleep(1000);
                deleteButton.click();
                System.out.println("CSS with " + cssName + " has been deleted.");
                deleteButton.sendKeys(Keys.ENTER);
                break;
            }
            row_num++;
        }



        BrowserUtils.sleep(4000);
    }

    public static void editCSS (WebDriver driver, String cssName) throws InterruptedException {
        System.out.println("\nManage CSS - edit a specific css by css name");
        WebElement table = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/table/tbody"));
        List<WebElement> rowResults = table.findElements(By.tagName("tr"));

        int row_num,col_num;
        row_num = 1;

        for (WebElement row : rowResults) {
            WebElement editCssName = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/table/tbody/tr[" + row_num + "]/td[2]"));
            if (editCssName.getText().equals(cssName)) {

                WebElement editButton = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/table/tbody/tr[" + row_num + "]/td[7]/span[2]/i"));

                System.out.println("INFO: css name found");

                BrowserUtils.sleep(1000);
                editButton.click();
                System.out.println("INFO: css with " + cssName + " edit button has been clicked");

                break;
            }
            row_num++;
        }
    }

    public static void  cssName (WebDriver driver, String cssName) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal - adding a cssName to cssName Field\n");
        WebElement cssname = driver.findElement(By.xpath("//*[@id=\"newCssName\"]"));
        cssname.click();
        cssname.clear();
        cssname.sendKeys(cssName);
        Thread.sleep(1000);
    }

    public static void  shortName (WebDriver driver, String shortName) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal - writing in shortname field\n");
        WebElement shortname = driver.findElement(By.xpath("//*[@id=\"newCssShortName\"]"));
        shortname.click();
        shortname.clear();
        shortname.sendKeys(shortName);
        Thread.sleep(1000);
    }

    public static void contentTypeCSS (WebDriver driver, String contenttype) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal - selecting a content type\n");

        WebElement contentType = driver.findElement(By.xpath("//*[@id=\"newCssContentType\"]"));
        contentType.click();
        Thread.sleep(1000);
        Select content_type = new Select(driver.findElement(By.xpath("//*[@id=\"newCssContentType\"]")));
        content_type.selectByVisibleText(contenttype);
        contentType.click();
    }

    public static void  defaultCSS (WebDriver driver, String yesOrNo) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal - selecting yes or no for default\n");
        WebElement defCSS = driver.findElement(By.xpath("//*[@id=\"newCssDefaultFlag\"]"));
        defCSS.click();
        Thread.sleep(1000);
        Select def_css = new Select(driver.findElement(By.xpath("//*[@id=\"newCssDefaultFlag\"]")));
        def_css.selectByVisibleText(yesOrNo);
        defCSS.click();

    }

    public static void previewJavaScript (WebDriver driver, String previewJavaScript) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal - selecting a preview javaScript from dropdown\n");
        WebElement previewJava = driver.findElement(By.xpath("//*[@id=\"preivewJavaScript\"]"));
        previewJava.click();
        Thread.sleep(1000);
        Select preview_javascript = new Select(driver.findElement(By.xpath("//*[@id=\"preivewJavaScript\"]")));
        preview_javascript.selectByVisibleText(previewJavaScript);
        previewJava.click();
    }

    public static void printJavaScript (WebDriver driver, String printJavaScript) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal - selecting a print JavaScript\n");
        WebElement printJava = driver.findElement(By.xpath("//*[@id=\"ddlScript\"]"));
        printJava.click();
        Thread.sleep(1000);

        Select preview_javascript = new Select(driver.findElement(By.xpath("//*[@id=\"ddlScript\"]")));
        preview_javascript.selectByVisibleText(printJavaScript);
        printJava.click();
    }

    public static void  previewCssFileViewButton (WebDriver driver) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal \n");
        WebElement previewCssView = driver.findElement(By.xpath("//*[@id=\"newCssModal\"]/div/div/div[2]/div[5]/button"));
        previewCssView.click();
        Thread.sleep(1000);
    }

    public static void  printCssFileViewButton (WebDriver driver) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal \n");
        WebElement printCssView = driver.findElement(By.xpath("//*[@id=\"newCssModal\"]/div/div/div[2]/div[7]/button"));
        printCssView.click();
        Thread.sleep(1000);
    }

    public static void  removeExistingFilePreview (WebDriver driver) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal \n");
        WebElement removeFilePreivew = driver.findElement(By.xpath("//*[@id=\"overwritePreviewCss\"]"));
        removeFilePreivew.click();
        Thread.sleep(1000);

    }

    public static void  removeExistingFilePrint (WebDriver driver) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal \n");
        WebElement removeFilePrint = driver.findElement(By.xpath("//*[@id=\"overwritePrintCss\"]"));
        removeFilePrint.click();
        Thread.sleep(1000);

    }

    public static void saveAndClose (WebDriver driver) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal - save and close button\n");
        WebElement saveAndClose = driver.findElement(By.xpath("//*[@id=\"newCssModal\"]/div/div/div[3]/button[2]"));
        saveAndClose.click();
        Thread.sleep(1000);
    }

    public static void cancelButton (WebDriver driver) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal - cancel button\n");
        WebElement cancel = driver.findElement(By.xpath("//*[@id=\"newCssModal\"]/div/div/div[3]/button[1]"));
        cancel.click();
        Thread.sleep(1000);
    }

    public static void xOut (WebDriver driver) throws InterruptedException {
        System.out.println("\nManage CSS create/edit modal - xout of modal\n");
        WebElement xout = driver.findElement(By.xpath("//*[@id=\"newCssModal\"]/div/div/div[1]/button"));
        xout.click();
        Thread.sleep(1000);
    }

    public static void previewCssFileButton (WebDriver driver, String filePath) throws InterruptedException, AWTException {
        System.out.println("\nManage CSS - adding a previewCSS file button\n");
        WebElement previewCss = driver.findElement(By.xpath("//*[@id=\"cssFile\"]"));
        previewCss.click();
        uploadFile(driver, filePath);
        Thread.sleep(1000);
    }

    public static void printCssFileButton (WebDriver driver, String filePath) throws InterruptedException, AWTException {
        System.out.println("\nManage CSS - adding a printCSS file button\n");
        WebElement printFile = driver.findElement(By.xpath("//*[@id=\"printFile\"]"));
        printFile.click();
        Thread.sleep(1000);
    }

    public static void uploadFile(WebDriver driver, String filePath) throws InterruptedException, AWTException {

        StringSelection ss = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

        //native key strokes for CTRL, V and ENTER keys
        Robot robot = new Robot();

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    //TODO
    public static void webPDF (WebDriver driver, String filePath) throws InterruptedException, AWTException {

    }

    //TODO
    public static void printPDF (WebDriver driver, String filePath) throws InterruptedException, AWTException {

    }
}
