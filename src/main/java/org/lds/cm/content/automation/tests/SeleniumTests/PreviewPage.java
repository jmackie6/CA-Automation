package org.lds.cm.content.automation.tests.SeleniumTests;

import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.*;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.BackgroundProcesses;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

public class PreviewPage {
    private WebDriver driver;
    private FonoSaia fono;

    String file_name = "gifts-of-peace.html";
    String doc_uri = "/automation/transform/broadcasts/first-presidency-christmas-devotional/2016/12";
    int locked;
    private String metadata_term = "Brother of Jared";

    @AfterMethod (alwaysRun = true)
    public static void closeUp() { JDBCUtils.closeUp(); }

    @Test
    public void backToManifestFile() throws SQLException, InterruptedException, AWTException {
        previewPageTestSetUp();
        backToManifest();
        driver.close();
    }

    @Test
    public void editUnlockedDoc() throws SQLException, InterruptedException, AWTException {
        previewPageTestSetUp();
        editUnlockedDocument(doc_uri);
        driver.close();
    }

    @Test
    public void editLockedDoc() throws SQLException, InterruptedException, AWTException {
        login();
        SetupTests.waitTilLoad(driver);

        searchByUriAndLang(doc_uri, "English (eng)");
        SetupTests.waitTilLoad(driver);

        Dashboard.clickActionsButton(driver, file_name);
        SetupTests.waitForOpenModal(driver);
        lockDocument();
        SetupTests.waitTilLoad(driver);

        toPreviewPage(file_name);
        SetupTests.waitTilLoad2(driver);
        editLockedDocument();


        // we need the document to be unlocked when our tests are finished
        Dashboard.goHome(driver);
        SetupTests.waitTilLoad(driver);

        Dashboard.clickActionsButton(driver, file_name);
        SetupTests.waitTilLoad(driver);

        unLockDocument();
        driver.close();
    }

    @Test
    public void validateDoc() throws SQLException, InterruptedException, AWTException {
        previewPageTestSetUp();
        validateDocument(file_name);
        verifyValidation(file_name);
        driver.close();
    }

    @Test
    public void editDocMetadata() throws SQLException, InterruptedException, AWTException {
        previewPageTestSetUp();
        editMetadata();
        verifyMetadata();
        driver.close();
    }

    @Test
    public void previousAndNextButtonTest() throws SQLException, InterruptedException, AWTException {
        previewPageTestSetUp();
        previousButton(); // assertion in function if button isn't visible
        nextButton(); // assertion in function if button isn't visible
        Dashboard.goHome(driver);
        SetupTests.waitTilLoad(driver);
        Dashboard.previewSpecificFile(driver, "_manifest.html");
        SetupTests.waitTilLoad2(driver);
        WebElement previous = driver.findElement(By.xpath("//*[@id=\"previewDiv\"]/div[1]/button[1]"));
        Assert.assertTrue(!previous.isEnabled(), "\nPrevious button isn't disabled when it should be.\n");
        Dashboard.goHome(driver);
        SetupTests.waitTilLoad(driver);
        Dashboard.previewSpecificFile(driver, file_name);
        SetupTests.waitTilLoad2(driver);
        WebElement next = driver.findElement(By.xpath("//*[@id=\"previewDiv\"]/div[1]/button[2]"));
        Assert.assertTrue(!next.isEnabled(), "\nNext button isn't disabled when it should be.\n");
        driver.close();
    }

    @Test
    public void commentTest() throws SQLException, InterruptedException, AWTException {
        deleteComment();
        previewPageTestSetUp();
        addComment();
        verifyComment();
        driver.close();
    }

    @Test
    public void cssTest() throws InterruptedException, AWTException, SQLException {
        previewPageTestSetUp();
        changeCss();
        driver.close();
    }
/*  waiting for manifest button to be fixed so it is disbaled if there isn't a manifest file available
    @Test
    public void backToManifestWithoutManifestFile() throws SQLException, InterruptedException, AWTException {
        login();
        SetupTests.waitTilLoad(driver);
        searchByUriAndLang(doc_uri, "English (eng)");
        SetupTests.waitTilLoad(driver);
        Dashboard.clickActionsButton(driver, "_manifest.html");
        deleteFile();
        SetupTests.waitTilLoad(driver);
        Dashboard.previewSpecificFile(driver, file_name);
        SetupTests.waitTilLoad2(driver);

    }
    */

    private void login() throws SQLException, InterruptedException {
        fono = new FonoSaia();
        driver = PreTestSetup.setup();
        driver.manage().window().maximize();
        Login.login(driver, fono.getUsername(), fono.getPassword());
    }


    private void searchByUriAndLang(String uri, String lang) {
        Search.searchLanguageSelect(driver, lang);
        Search.search(driver, uri);
    }

    private void toPreviewPage(String file_name) throws SQLException, InterruptedException, AWTException {
        Dashboard.previewSpecificFile(driver, file_name);
    }

    /* this function checks that the file name in the uri on the preview page is equal to _manifest
       this confirms that the back to manifest button took us to the correct page */

    private void backToManifest() throws InterruptedException {

        Preview.backToManifestButton(driver);
        SetupTests.waitTilLoad2(driver);

        // must switch to frame in order to locate elements within the iframe tag
        driver.switchTo().frame(driver.findElement(By.name("previewFile")));
        String uri = driver.findElement(By.tagName("html")).getAttribute("data-uri");

        // get the file name from the uri

        int index = uri.lastIndexOf("/");
        String doc_name = uri.substring(index + 1, uri.length());

        // if the document name found on the page we are redirected to matches '_manifest', this portion of the test passes
        Assert.assertTrue(doc_name.equals("_manifest"), "\nSelecting Back to Manifest didn't redirect us to the Manifest file.\n");

        driver.switchTo().defaultContent();
    }

    private void editLockedDocument() {

        if (driver.findElement(By.xpath("//*[@id=\"previewDiv\"]/button[2]")).isDisplayed() == false && locked == 1) {
            System.out.println("\nSince the document is locked, the edit button is NOT displayed on the Preview page. This is working as desired.\n");
        }  else {
            Assert.fail("\nThe edit button is displayed when the document is locked. This is incorrect.\n");
        }
    }

    private void editUnlockedDocument(String uri) throws InterruptedException, AWTException {

        if (driver.findElement(By.xpath("//*[@id=\"previewDiv\"]/button[2]")).isDisplayed() == true && locked == 0) {
            System.out.println("\nSince the document is unlocked, the edit button is displayed on the Preview page. This is working as desired.\n");

            Preview.editButton(driver);
            Thread.sleep(3000); /* change to wait until element in oxygen is visible? */

            String windowHandle = driver.getWindowHandle();

            for (String oxygen : driver.getWindowHandles()) {
                driver.switchTo().window(oxygen);
            }

            String oxygen_path = (new WebDriverWait(driver, 60))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"1\"]/span[2]/span[4]/input"))).getAttribute("value");

            int index = oxygen_path.lastIndexOf("/");
            String oxygen_uri = oxygen_path.substring(0, index);

            Assert.assertTrue(oxygen_uri.equals(uri), "\nSomething went wrong when opening the Oxygen editor tab\n");

            driver.close();
            driver.switchTo().window(windowHandle);

        } else {
            Assert.fail("\nThe edit button is not present on the Preview page. Since the document is NOT locked, this is incorrect.\n");
        }
    }

    private void lockDocument() throws AWTException, InterruptedException {
        Actions.lock(driver);
        locked++;
    }

    private void unLockDocument() throws AWTException {
        Actions.unlock(driver);
        locked--;
    }

    private void validateDocument(String document) {
        Preview.validateButton(driver);
        SetupTests.waitTilLoad2(driver);
    }

    private void verifyValidation(String document) {
        Dashboard.clickGearIcon(driver);
        BackgroundProcesses.goToPage(driver);
        SetupTests.waitTilLoad(driver);
        WebElement file_name = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[1]/div[2]/div/table/tbody/tr[1]/td[4]"));
        WebElement status = driver.findElement(By.xpath("/html/body/div/div/div[3]/div[1]/div[2]/div/table/tbody/tr[1]/td[1]"));
        Assert.assertTrue(file_name.getText().equals(document) && status.getText().equals("SUCCESSFUL"), "Validation from the Preview page failed");
    }

    private void editMetadata() {
        Preview.updateMetaData(driver);
        org.lds.cm.content.automation.util.SeleniumUtil.UpdateMetadata.searchTerm(driver, metadata_term);
        org.lds.cm.content.automation.util.SeleniumUtil.UpdateMetadata.selectTerm(driver, metadata_term);
        org.lds.cm.content.automation.util.SeleniumUtil.UpdateMetadata.save(driver);
        SetupTests.waitTilLoad(driver);
    }

    private void verifyMetadata() throws InterruptedException {
        Preview.updateMetaData(driver);
        Thread.sleep(2000);
        WebElement current_metadata = driver.findElement(By.xpath("/html/body/div[1]/div/div/temis-component/div[2]/div[2]/div[2]/div/label"));

        Assert.assertTrue(current_metadata.isDisplayed() && current_metadata.getText().equals("Brother of Jared"), "The term was NOT successfully saved. The Metadata test has failed.");
    }

    private void previousButton() {
        Preview.previousButton(driver);
        SetupTests.waitTilLoad2(driver);

        // must switch to frame in order to locate elements within the iframe tag
        driver.switchTo().frame(driver.findElement(By.name("previewFile")));
        String uri = driver.findElement(By.tagName("html")).getAttribute("data-uri");

        // get the file name from the uri

        int index = uri.lastIndexOf("/");
        String doc_name = uri.substring(index + 1, uri.length());

        Assert.assertTrue(!doc_name.equals("gifts-of-peace"), "\nSelecting the previous button didn't change the document.\n");
        driver.switchTo().defaultContent();
    }

    private void nextButton() {
        Preview.nextButton(driver);
        SetupTests.waitTilLoad2(driver);

        driver.switchTo().frame(driver.findElement(By.name("previewFile")));
        String uri2 = driver.findElement(By.tagName("html")).getAttribute("data-uri");

        int index = uri2.lastIndexOf("/");
        String doc_name = uri2.substring(index + 1, uri2.length());

        Assert.assertTrue(doc_name.equals("gifts-of-peace"), "\nSelecting the next button didn't change the document.\n");
        driver.switchTo().defaultContent();
    }

    private void addComment() throws InterruptedException {
        Preview.commentsTab(driver);
        Preview.addCommentButton(driver);
        Preview.typeComment(driver, "\nADDING COMMENT TEST.\n");
        Preview.submitComment(driver);
        Thread.sleep(2000);
    }

    private void verifyComment() {
        WebElement comment = driver.findElement(By.xpath("//*[@id=\"previewCtrl\"]/div/div[3]/div/div[2]/div[2]/div[2]"));
        Assert.assertTrue(comment.isDisplayed() && comment.getText().equals("ADDING COMMENT TEST."), "\nThe comment was not successfully added to the Preview page.\n");
    }

    private void deleteComment() throws SQLException {
        String query = "DELETE FROM DOCUMENT_COMMENT WHERE DOCUMENT_ID = 1018555";
        Connection conn = DriverManager.getConnection(Constants.dbUrl, Constants.dbUsername, Constants.dbPassword);
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.execute();
    }

    private void previewPageTestSetUp() throws SQLException, InterruptedException, AWTException {
        login();
        SetupTests.waitTilLoad(driver);
        searchByUriAndLang(doc_uri, "English (eng)");
        SetupTests.waitTilLoad(driver);
        toPreviewPage(file_name);
        SetupTests.waitTilLoad2(driver);
    }

    private void changeCss() {
        Preview.cssType(driver, "GL-iOS Manuals"); // should we go through each option?
        SetupTests.waitTilLoad2(driver);

        ArrayList<String> errorLogs = BrowserUtils.errorLogCheck(driver);
        Assert.assertTrue(errorLogs.size() == 0, stringify(errorLogs));
    }

    private String stringify(ArrayList<String> strings) {
        StringBuilder sb = new StringBuilder("List of errors found:\n");
        for(String x: strings)
            sb.append(x + "\n");
        return sb.toString();

    }

    private void uploadManifest() throws Exception {
        File manifest = new File(Constants.transformFileStartDir + "engBroadcastHTML.12/_manifest.html");
        QATransformService.transformFileGivenContentGroupIdServicesAPI(manifest, "1");
    }

    private void deleteFile() {
        Actions.delete(driver);
        Actions.delete_confirmation(driver);
    }
}
