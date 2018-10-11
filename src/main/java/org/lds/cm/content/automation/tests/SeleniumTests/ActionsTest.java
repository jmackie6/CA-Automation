package org.lds.cm.content.automation.tests.SeleniumTests;

import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.Pages.DownloadPDF;
import org.lds.cm.content.automation.util.SeleniumUtil.ReleaseSystemLock;
import org.lds.cm.content.automation.util.SeleniumUtil.*;
import org.lds.cm.content.automation.util.SeleniumUtil.UpdateGroupAndOwner;
import org.lds.cm.content.automation.util.SetupTests;
import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class ActionsTest {

    private WebDriver webdriver;
    private FonoSaia fono;
    private String file_id = "ACTIONS_000_019";


    private String file_name = "document1.html";
    private String file_name2 = "document2.html";
    private String random_cg;
    private String metadata_term = "Brother of Jared";
    private String pathToDownloadFolder = System.getProperty("user.home") + "/Downloads";



    @AfterClass (alwaysRun = true)
    public void cleanUp() { JDBCUtils.closeUp(); }

    @AfterMethod (alwaysRun = true)
    public void deleteFile() throws Exception {
        deleteTestDoc(file_id);
    }


    @Test
    public void approve() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Approve", file_name);
        Thread.sleep(2000);
        verifyApprove(file_id);
        //deleteTestDoc(file_id);
    }

    @Test
    public void delete() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Delete", file_name);

        completeDelete();
        verifyDelete(file_id);
    }

    @Test
    public void download() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Download", file_name);
        verifyDownload(file_name,pathToDownloadFolder);
    }

    @Test
    public void fastTrack() throws Exception {
        transformTestDoc(file_name);
        transformTestDoc(file_name2);
        login();
        searchForDoc(file_id);
        clickAction("Fast Track", file_name);
        completeFastTrackPdf();

        verifyFastTrackPdf(file_id);
        deleteTestDoc(file_name2);
    }

    @Test
    public void print() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Print Export", file_name);
        completePrint();
        verifyPrint(file_id, pathToDownloadFolder);  // Pass in the path to the download folder on your machine
        //deleteTestDoc(file_id);
    }

    @Test
    public void publish() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Source Lock", file_name);
        clickAction("Publish", file_name);
        completePublish();
        verifyPublish(file_id);
     //   deleteTestDoc(file_id);
    }

    @Test
    public void publishHistory() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Source Lock", file_name);
        clickAction("Publish", file_name);
        completePublish();
//        Search.refreshButton(webdriver);
        Search.clearSearch(webdriver);
        searchForDoc(file_id);
        clickAction("Publish History", file_name);
        completePublishHistory();
        //deleteTestDoc(file_id);
    }

    @Test
    public void sourceLock() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Source Lock", file_name);
        verifySourceLock(file_id);
        //deleteTestDoc(file_id);

    }

    @Test
    public void sourceUnlock() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Source Lock", file_name); // must lock first before we can unlock
        clickAction("Source Unlock", file_name);
        verifySourceUnlock(file_id);
       // deleteTestDoc(file_id);
    }

    @Test
    public void updateGroupAndOwner() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Update Group and Owner", file_name);
        completeUpdateGroupAndOwner();
        verifyUpdateGroupAndOwner(file_id);
     //   deleteTestDoc(file_id);
    }

    @Test
    public void updateMetadata() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Edit Metadata", file_name);
        completeEditMetadata();
        verifyEditMetadata(file_name);
        deleteTestDoc(file_id);
    }

    @Test
    public void validate() throws Exception {
        transformTestDoc(file_name);
        login();
        searchForDoc(file_id);
        clickAction("Validate", file_name);
        verifyValidate(file_id);
       // deleteTestDoc(file_id);
    }

    @Test
    public void releaseSystemLock() throws Exception {
        transformTestDoc(file_name);
        systemLockDoc(file_id);
        login();
        searchForDoc(file_id);
        clickAction("Release System Lock", file_name);
        completeReleaseSystemLock();
        verifyReleaseSystemLock(file_id);
      //  deleteTestDoc(file_id);
    }

    private void transformTestDoc(String file_name) throws Exception{
        File testDoc = new File(Constants.actionFilesStartDir + "/" + file_name);
        QATransformService.transformFileGivenContentGroupId(testDoc, "1");
    }

    private void deleteTestDoc(String file_id) throws Exception {
        try {
            ResultSet rs = JDBCUtils.getResultSet("SELECT FILE_ID FROM DOCUMENT WHERE FILE_ID = '" + file_id + "'");
            if (!rs.next()) {
                System.out.println("\nThe document specified was not found and will not be deleted\n");
            } else {
                QADeleteService.deleteFromDbByFileIdSingleDocument(file_id);
            }

            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void login() throws InterruptedException, SQLException{
        fono = new FonoSaia();
        webdriver = PreTestSetup.setup();

        Login.login(webdriver, fono.getUsername(), fono.getPassword());
        SetupTests.waitTilLoad(webdriver);
    }

    private void searchForDoc(String file_id) {
        Search.searchFileId(webdriver);
        Search.search(webdriver, file_id);
        SetupTests.waitTilLoad(webdriver);
    }

    private void clickAction(String action, String file_name) throws InterruptedException, SQLException {
        Dashboard.clickActionsButton(webdriver, file_name);
        SetupTests.waitForOpenModal(webdriver);


        Actions.selectSpecificAction(webdriver, action);
    }

    private void verifyApprove(String file_id) {
        webdriver.close();

        List<String> approvedDoc = new ArrayList<>();
        try {
            ResultSet rs = JDBCUtils.getResultSet("SELECT APPROVED FROM DOCUMENT WHERE FILE_ID = '" + file_id + "'");
            while(rs.next()) {
                approvedDoc.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int approved = Integer.parseInt(approvedDoc.get(0));
        if (approved == 1) {
            System.out.println("Approve action test was successful!");
        } else {
            Assert.fail("Approve action test was not successful: Document was not approved.");
        }
    }

    private void completeDelete() throws InterruptedException {
        Actions.delete_confirmation(webdriver);
        SetupTests.waitTilLoad(webdriver);
        webdriver.close();
    }

    private void verifyDelete(String file_id) {
        try {
            ResultSet rs = JDBCUtils.getResultSet("SELECT FILE_ID FROM DOCUMENT WHERE FILE_ID = '" + file_id + "'");
            if (!rs.next()) {
                System.out.println("Delete Document test was successful!");
            } else {
                Assert.fail("Delete document test was not successful: Document was not deleted.");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void verifyDownload(String file_name, String downloadPath) {
        SetupTests.waitTilLoad(webdriver);
        webdriver.close();

        File dir = new File(downloadPath);
        File[] dir_content = dir.listFiles();
        int counter = 0;
        for (int i = 0; i < dir_content.length; i++) {
            if (dir_content[i].getName().contains(file_name)) {
                counter--;
            } else {
                counter++;
            }
        }
        Assert.assertTrue(counter < dir_content.length, "\nDownload was not successful. Print test failed.");
    }

    private void completeFastTrackPdf() throws InterruptedException {

        SetupTests.waitForOpenModal(webdriver);

        FastTrackPDF.allToSourceFromFrontMatter(webdriver);
        FastTrackPDF.allDocsFromBodyToSource(webdriver);

        WebElement first_doc = (new WebDriverWait(webdriver, 60))
                //                                                       /html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[1]/div/ul/li[1]
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[1]/div/ul/li[1]")));

        WebElement second_doc = (new WebDriverWait(webdriver, 60))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[1]/div/ul/li[2]")));


        FastTrackPDF.selectCSS(webdriver, "Galley Proof");

        FastTrackPDF.allToSourceFromFrontMatter(webdriver);
        FastTrackPDF.allDocsFromBodyToSource(webdriver);
        first_doc.click();
        FastTrackPDF.selectedDocsToFrontMatter(webdriver);
        second_doc.click();
        FastTrackPDF.selectedDocsToBodyMatter(webdriver);
        FastTrackPDF.confirmCreation(webdriver);
        SetupTests.waitForLoading(webdriver);
    }

    private void verifyFastTrackPdf(String file_id) throws InterruptedException {
        Dashboard.clickGearIcon(webdriver);
        DownloadPDF.goToPage(webdriver);
        SetupTests.waitTilLoad(webdriver);
        webdriver.navigate().refresh();
        SetupTests.waitTilLoad(webdriver);
        webdriver.navigate().refresh();

        WebElement file_name = (new WebDriverWait(webdriver, 30))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pdfListResults\"]/tbody/tr[1]/td[2]")));

        Assert.assertTrue(file_name.getText().contains(file_id), "\nFast Track PDF creation was NOT successful. Fast Track PDF test failed.");
        webdriver.close();
    }

    private void completePrint() throws InterruptedException {
        SetupTests.waitForOpenModal(webdriver);
        org.lds.cm.content.automation.util.SeleniumUtil.Print.allToSource(webdriver);

        WebElement doc1 = webdriver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[1]/div/ul/li[1]"));
        WebElement doc2 = webdriver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[1]/div/ul/li[2]"));
        String[] docs_to_select = {doc1.getText(), doc2.getText()};

        org.lds.cm.content.automation.util.SeleniumUtil.Print.selectDocuments(webdriver, docs_to_select);
        org.lds.cm.content.automation.util.SeleniumUtil.Print.selectedToPrint(webdriver);
        org.lds.cm.content.automation.util.SeleniumUtil.Print.clickDownload(webdriver);
        SetupTests.waitTilLoad(webdriver);
        webdriver.close();
    }

    private void verifyPrint(String file_id, String downloadPath) {
        int index = file_id.indexOf("_");
        String id = file_id.substring(0, index);
        File dir = new File(downloadPath);
        File[] dir_content = dir.listFiles();
        int counter = 0;
        for (int i = 0; i < dir_content.length; i++) {
            if (dir_content[i].getName().contains(id)) {
                counter--;
            } else {
                counter++;
            }
        }
        Assert.assertTrue(counter < dir_content.length, "\nDownload was not successful. Print test failed.");
    }

    private void completePublish() throws InterruptedException {
        Actions.confirmPublish(webdriver);
        Actions.clickPublishOK(webdriver);
        SetupTests.waitTilLoad(webdriver);
    }

    private void verifyPublish(String file_id) {
        webdriver.close();
        try {
            ResultSet rs = JDBCUtils.getResultSet("SELECT * FROM CONTENT_PUBLISH WHERE DOCUMENT_ID IN (SELECT DOCUMENT_ID FROM DOCUMENT WHERE FILE_ID = '" + file_id + "')");
            if (!rs.next()) {
                Assert.fail("Publishing was not successful. Publish test has failed.");
            } else {
                System.out.println("Publishing was successful. Publish test passed!");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void completePublishHistory() {
        Actions.publishHistoryClose(webdriver);
        System.out.println("\nPublish History test has passed!");
        webdriver.close();
    }

    private void verifySourceUnlock(String file_id) {
        SetupTests.waitTilLoad(webdriver);
        webdriver.close();
        List<String> unlocked = new ArrayList<>();
        try {
            ResultSet rs = JDBCUtils.getResultSet("SELECT DOCUMENT_STATE_ID FROM DOCUMENT WHERE FILE_ID = '" + file_id + "'");
            while (rs.next()) {
                unlocked.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(unlocked.get(0).toString().equals("2"), "\nDocument was not unlocked. Source Unlock test failed.");
    }

    private void verifySourceLock(String file_id) {
        SetupTests.waitTilLoad(webdriver);
        webdriver.close();

        List<String> locked = new ArrayList<>();
        try {
            ResultSet rs = JDBCUtils.getResultSet("SELECT DOCUMENT_STATE_ID FROM DOCUMENT WHERE FILE_ID = '" + file_id + "'");
            while (rs.next()) {
                locked.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(locked.get(0).toString().equals("2"), "\nDocument was sucessfully locked. Source Lock test passed!");
    }

    private void completeUpdateGroupAndOwner() {
        List<WebElement> cg_list = (new WebDriverWait(webdriver, 30))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[1]/div/div/group-owner/div/div[2]/div/div/div[1]/select/option")));
        int cg_count = cg_list.size();

        Random random = new Random();
        random_cg = cg_list.get(random.nextInt(cg_count)).getText();
        System.out.println(random_cg);
        UpdateGroupAndOwner.selectContentGroup(webdriver, random_cg);
        UpdateGroupAndOwner.selectOwner(webdriver, fono.getPreferred_name());
        UpdateGroupAndOwner.saveAndClose(webdriver);
        BrowserUtils.sleep(5000);
        webdriver.close();
    }

    private void verifyUpdateGroupAndOwner(String file_id) {
        List<String> group_and_owner = new ArrayList<>();
        try {
            ResultSet user = JDBCUtils.getResultSet("SELECT PREFERRED_NAME FROM APP_USER WHERE APP_USER_ID IN (SELECT OWNER_APP_USER_ID FROM DOCUMENT WHERE DOCUMENT_ID IN (SELECT DOCUMENT_ID FROM DOCUMENT WHERE FILE_ID = '" + file_id + "'))");
            ResultSet content_group = JDBCUtils.getResultSet("SELECT CONTENT_GROUP_ID FROM CONTENT_AUTO.DOCUMENT_CONTENT_GROUP WHERE DOCUMENT_ID IN (SELECT DOCUMENT_ID FROM DOCUMENT WHERE DOCUMENT.FILE_ID = '" + file_id + "')");


            while (user.next() && content_group.next()) {
                group_and_owner.add(user.getString("preferred_name"));
                group_and_owner.add(content_group.getString("content_group_id"));
            }
            user.close();
            content_group.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

         if (group_and_owner.get(1).equals(random_cg)) {
            System.out.println("\nVerified that Content Group was successfully changed/saved.");
         } else if (group_and_owner.get(0).equals(fono.getPreferred_name())) {
            System.out.println("\nVerified that the User was successfully changed/saved");
         } else {
            System.out.println("\nEither the Content Group or User was NOT successfully updated.");
         }
    }

    private void completeEditMetadata() {
        org.lds.cm.content.automation.util.SeleniumUtil.UpdateMetadata.searchTerm(webdriver, metadata_term);
        org.lds.cm.content.automation.util.SeleniumUtil.UpdateMetadata.selectTerm(webdriver, metadata_term);
        org.lds.cm.content.automation.util.SeleniumUtil.UpdateMetadata.save(webdriver);
        SetupTests.waitTilLoad(webdriver);
    }

    private void verifyEditMetadata(String file_name) throws InterruptedException {
        Dashboard.clickActionsButton(webdriver, file_name);
        Actions.selectSpecificAction(webdriver, "Edit Metadata");
        SetupTests.waitTilLoad(webdriver);

        WebElement current_metadata = webdriver.findElement(By.xpath("/html/body/div[1]/div/div/temis-component/div[2]/div[2]/div[2]/div/label"));

        Assert.assertTrue(current_metadata.isDisplayed() && current_metadata.getText().equals("Brother of Jared"), "The term was NOT successfully saved. The Metadata action test has failed.");
        webdriver.close();
    }

    private void verifyValidate(String file_id) {
        SetupTests.waitTilLoad(webdriver);
        webdriver.close();
        List<String> validated = new ArrayList<>();
        try {
            ResultSet rs = JDBCUtils.getResultSet("SELECT VALIDATED FROM DOCUMENT WHERE FILE_ID = '" + file_id + "'");
            while (rs.next()) {
                validated.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(validated.get(0).toString().equals("1"), "\nDocument was NOT successfully validated. Validation test failed.");
    }

    private void systemLockDoc(String file_id) throws SQLException {
        String query = "UPDATE DOCUMENT SET SYSTEM_LOCK = 1 WHERE FILE_ID = '" + file_id + "'";
        Connection conn = DriverManager.getConnection(Constants.dbUrl, Constants.dbUsername, Constants.dbPassword);
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.execute();
    }

    private void completeReleaseSystemLock() {
        ReleaseSystemLock.releaseSystemLockButton(webdriver);
        SetupTests.waitTilLoad(webdriver);
        webdriver.close();
    }

    private void verifyReleaseSystemLock(String file_id) {
        List<String> system_lock = new ArrayList<>();
        try {
            ResultSet rs = JDBCUtils.getResultSet("SELECT SYSTEM_LOCK FROM DOCUMENT WHERE FILE_ID = '" + file_id + "'");
            while (rs.next()) {
                system_lock.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(system_lock.get(0).toString().equals("0"), "\nDocument was NOT released from system lock. Release System Lock test failed.");
    }
}

