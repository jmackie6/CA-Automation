package org.lds.cm.content.automation.tests.SeleniumTests;

import org.lds.cm.content.automation.model.RoleModels.*;
import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.tests.endpoints.DocumentTests.DeleteDocument;
import org.lds.cm.content.automation.util.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.SeleniumUtil.*;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.UserManagement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.lds.cm.content.automation.service.QATransformService;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

import static org.lds.cm.content.automation.util.LogUtils.getLogger;

public class BulkOperationsUI {

    private static final Logger LOG = getLogger();
    private WebDriver webDriver;
    private FonoSaia fono;

    private static final String jpnLiahonaFileId = "bulk30012_300";
    private static final String spaGeneralConferenceFileId = "bulk00298_002";
    private static final int numDocsSpaGenConf = 45;
    private static final String testContentGroupId = "1101";
    private static final int numDocsJpnLiahona = 48;

    @AfterMethod (alwaysRun = true)
    public void afterMethod() throws SQLException, InterruptedException {
        QADocumentService.unlockByFileId(spaGeneralConferenceFileId);
        QADocumentService.unlockByFileId(jpnLiahonaFileId);
        //unapprove them
        webDriver.quit();
        JDBCUtils.closeUp();
    }

    @BeforeClass (alwaysRun =  true)
    public void beforeClass() throws Exception {

        QADocumentService.deleteValidationErrorsByFileId(spaGeneralConferenceFileId);
        QADocumentService.deleteValidationErrorsByFileId(jpnLiahonaFileId);
        //transform the bulk files if needed
        File startDirJpnLiahona = new File(Constants.bulkOperationsFileStartDir + "/Magazines/JpnLiahona");
        transformFiles(startDirJpnLiahona);
        File startDirSpaGeneralConference = new File(Constants.bulkOperationsFileStartDir + "/GeneralConference/spaGeneralConference");
        transformFiles(startDirSpaGeneralConference);
        QADocumentService.unlockByFileId(spaGeneralConferenceFileId);
        QADocumentService.unlockByFileId(jpnLiahonaFileId);
        //unapprove them
        QADocumentService.unapproveByFileId(jpnLiahonaFileId);
        QADocumentService.unapproveByFileId(spaGeneralConferenceFileId);
    }

    @Test(priority = 1, groups={"selenium"}, timeOut=180000)
    public void bulkApproveConference() throws SQLException, InterruptedException, IllegalArgumentException {

        QADocumentService.unapproveByFileId(spaGeneralConferenceFileId);

        int numApproved = QADocumentService.numApprovedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numApproved == 0, "All documents need to be unapproved before the test");

        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Approve");
        BulkOperations.confirmAction(webDriver);

        numApproved = QADocumentService.numApprovedByFileId(spaGeneralConferenceFileId);

        Assert.isTrue(numApproved == numDocsSpaGenConf, "Num docs Approved: " + numApproved + "\nnumDocs needed to be approved: " + numDocsSpaGenConf);

        QADocumentService.unlockByFileId(spaGeneralConferenceFileId);
        QADocumentService.unapproveByFileId(spaGeneralConferenceFileId);
        webDriver.close();

    }

    @Test(priority = 2, groups={"selenium"}, timeOut=180000)
    public void bulkLockConference() throws SQLException, InterruptedException {

        QADocumentService.validatedByFileId(spaGeneralConferenceFileId);
        QADocumentService.unlockByFileId(spaGeneralConferenceFileId);
        QADocumentService.deleteFatalValidateErrorsByFileId(spaGeneralConferenceFileId);
        int numLocked = QADocumentService.numLockedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numLocked == 0, "All documents need to be unlocked before the test");

        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Source Lock");
        BulkOperations.confirmAction(webDriver);

        numLocked = QADocumentService.numLockedByFileId(spaGeneralConferenceFileId);

        Assert.isTrue(numLocked == numDocsSpaGenConf, "Num docs Locked: " + numLocked + "\nnumDocs needed to be Locked: " + numDocsSpaGenConf);
        QADocumentService.unlockByFileId(spaGeneralConferenceFileId);
        webDriver.close();
    }


    @Test(priority = 3, groups={"selenium"}, timeOut=180000)
    public void bulkUnlockConference() throws SQLException, InterruptedException {

        QADocumentService.lockByFileId(spaGeneralConferenceFileId);
        QADocumentService.validatedByFileId(spaGeneralConferenceFileId);
        int numUnlocked = QADocumentService.numUnlockedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numUnlocked == 0, "All documents need to be locked before the test");

        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Source Unlock");
        BulkOperations.confirmAction(webDriver);


        numUnlocked = QADocumentService.numUnlockedByFileId(spaGeneralConferenceFileId);

        Assert.isTrue(numUnlocked == numDocsSpaGenConf, "Num docs unlocked: " + numUnlocked + "\nnumDocs needed to be unlocked: " + numDocsSpaGenConf);
        webDriver.close();
    }

    @Test(priority = 4, groups={"selenium"}, timeOut=180000)
    public void bulkValidateConference() throws Exception {

        File startDirSpaGeneralConference = new File(Constants.bulkOperationsFileStartDir + "/GeneralConference/spaGeneralConference");
        transformFiles(startDirSpaGeneralConference);
        int numValidationErrorsConference = QADocumentService.numValidationErrorsByFileId(spaGeneralConferenceFileId);
        int numValidated = QADocumentService.numValidatedByFileId(spaGeneralConferenceFileId);
        QADocumentService.addedByFileId(spaGeneralConferenceFileId);
        QADocumentService.unvalidatedByFileId(spaGeneralConferenceFileId);

        QADocumentService.deleteValidationErrorsByFileId(spaGeneralConferenceFileId);
        int numV = QADocumentService.numValidatedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numV == 0, "All documents need to be in the ADDED status before the test");

        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Validate");
        BulkOperations.confirmAction(webDriver);

        int numValidationErrors2 = QADocumentService.numValidationErrorsByFileId(spaGeneralConferenceFileId);
        int numVal = QADocumentService.numValidatedByFileId(spaGeneralConferenceFileId);

        Assert.isTrue( numVal == numDocsSpaGenConf && numValidationErrorsConference == numValidationErrors2, "Num docs Validated: " + numValidated + "\nnumDocs needed to be validated: " + numDocsSpaGenConf + "\nnumber of validation errors before validation: " + numValidationErrorsConference + "\nnumber of validation errors after: " + numValidationErrors2);
        QADocumentService.unlockByFileId(spaGeneralConferenceFileId);
        webDriver.close();
    }

    @Test(priority = 15, groups={"selenium"}, timeOut=600000, enabled = true)
    public void bulkPublishConference() throws Exception {

        String publishedDoc1FileId = "bulk00298_002_015";
        String publishedDoc2FileId = "bulk00298_002_004";

        QADocumentService.unlockByFileId(spaGeneralConferenceFileId);
        QADocumentService.lockByFileId(publishedDoc1FileId);
        QADocumentService.lockByFileId(publishedDoc2FileId);
        QADocumentService.validatedByFileId(publishedDoc1FileId);
        QADocumentService.validatedByFileId(publishedDoc2FileId);
        QADocumentService.deleteFatalValidateErrorsByFileId(publishedDoc1FileId);
        QADocumentService.deleteFatalValidateErrorsByFileId(publishedDoc2FileId);
        int numLocked = QADocumentService.numLockedByFileId(spaGeneralConferenceFileId);
        int numPublished = QADocumentService.numPublishedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numLocked == 2 && numPublished == 0, "two documents need to be locked to publish and no documents should be published");


        fono = new FonoSaia();
        webDriver = PreTestSetup.setup();
        Login.login(webDriver, fono.getUsername(), fono.getPassword());


        Search.searchFileId(webDriver);
        Search.search(webDriver, spaGeneralConferenceFileId);


        WebElement singleDocument1 = (new WebDriverWait(webDriver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[8]/td[1]/span/span/input")));

        WebElement singleDocument2 = (new WebDriverWait(webDriver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[9]/td[1]/span/span/input")));


        singleDocument1.click();
        if (!singleDocument1.isSelected()) {
            junit.framework.Assert.fail();
        } else {
            System.out.println("first document checked was clicked");
        }

        singleDocument2.click();
        if (!singleDocument2.isSelected()) {
            junit.framework.Assert.fail();
        } else {
            System.out.println("second document checked was clicked");
        }


        Dashboard.openBulkOperationsMenu(webDriver);
        BulkOperations.bulkOperation(webDriver, "Publish");
        BulkOperations.confirmAction(webDriver);

        //confirm publish action
        WebElement confirmPublish = (new WebDriverWait(webDriver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/publish-start-result/div[3]/button")));
        confirmPublish.click();
        numLocked = QADocumentService.numLockedByFileId(spaGeneralConferenceFileId);



        numPublished = QADocumentService.numPublishedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numLocked == 2, "Num docs Locked: " + numLocked + "\nnumDocs needed to be Locked: " + numDocsSpaGenConf);
        Assert.isTrue(numPublished == 2, "Number of documents published: " + numPublished + "\nnumber of docs that need to be published is : 2");

        String fullUriSecondDoc = "/automation/bulkOperations/general-conference/1998/10/are-we-keeping-pace";
        String fullUriFirstDoc = "/automation/bulkOperations/general-conference/1998/10/a-voice-of-warning";

        //delete published documents
        QADeleteService.deleteFromDbByFileIdSingleDocument(publishedDoc1FileId);
        QADeleteService.deleteFromDbByFileIdSingleDocument(publishedDoc2FileId);

        //delete the annotations for these documents
        QADeleteService.deleteDocMaps("/automation/bulkOperations/general-conference/1998/10/*&locale=spa");
//        QADeleteService.deleteSingleDocmapByFileId(publishedDoc2FileId);

        //re-transform the documents
        ///Users/bradkubie/IdeaProjects/content-automation/qa/src/main/resources/BulkTestFiles/GeneralConference/spaGeneralConference/a-voice-of-warning.html
        File spaGeneralConference1Doc = new File(Constants.bulkOperationsFileStartDir + "/GeneralConference/spaGeneralConference/are-we-keeping-pace.html");
        File spaGeneralConference2Doc = new File(Constants.bulkOperationsFileStartDir + "/GeneralConference/spaGeneralConference/a-voice-of-warning.html");

        QATransformService.transformFileGivenContentGroupId(spaGeneralConference1Doc, testContentGroupId);
        QATransformService.transformFileGivenContentGroupId(spaGeneralConference2Doc, testContentGroupId);

    }

    @Test(priority = 5, groups={"selenium"}, timeOut=180000)
    public void bulkUpdateGroupConference() throws InterruptedException, SQLException {

        QADocumentService.updateGroupByFileIdLiahona(spaGeneralConferenceFileId);
        int numConferenceGroup = QADocumentService.numUpdatedGroupByFileIdConference(spaGeneralConferenceFileId);
        Assert.isTrue(numConferenceGroup == 0, "No documents in publication have the conference group");


        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Update Group and Owner");
        BulkOperations.confirmAction(webDriver);
        UpdateGroupAndOwner.selectContentGroup(webDriver, "General Conference");
        UpdateGroupAndOwner.selectOwner(webDriver, "Fono Saia 'Ilangana");
        UpdateGroupAndOwner.saveAndClose(webDriver);

        numConferenceGroup = QADocumentService.numUpdatedGroupByFileIdConference(spaGeneralConferenceFileId);

        Assert.isTrue(numConferenceGroup == 45, "Number of documents that must be changed to conference group is :" + numDocsSpaGenConf + "\nActual: " + numConferenceGroup);
        QADocumentService.updateGroupByFileIdLiahona(spaGeneralConferenceFileId);

    }


    @Test(priority = 7, groups={"selenium"}, timeOut=180000)
    public void bulkLockLiahona()throws SQLException, InterruptedException {

        QADocumentService.validatedByFileId(jpnLiahonaFileId);
        QADocumentService.unlockByFileId(jpnLiahonaFileId);
        QADocumentService.deleteFatalValidateErrorsByFileId(jpnLiahonaFileId);
        int numLocked = QADocumentService.numLockedByFileId(jpnLiahonaFileId);
        Assert.isTrue(numLocked == 0, "All documents need to be unlocked before the test");

        bulkOperationSetup(jpnLiahonaFileId);

        BulkOperations.bulkOperation(webDriver, "Source Lock");
        BulkOperations.confirmAction(webDriver);

        numLocked = QADocumentService.numLockedByFileId(jpnLiahonaFileId);

        Assert.isTrue(numLocked == numDocsJpnLiahona, "Num docs Locked: " + numLocked + "\nnumDocs needed to be Locked: " + numDocsJpnLiahona);
        QADocumentService.unlockByFileId(jpnLiahonaFileId);
    }


    @Test(priority = 8, groups={"selenium"}, timeOut=180000)
    public void bulkUnlockLiahona() throws SQLException, InterruptedException
    {
        QADocumentService.lockByFileId(jpnLiahonaFileId);
        QADocumentService.validatedByFileId(jpnLiahonaFileId);
        int numUnlocked = QADocumentService.numUnlockedByFileId(jpnLiahonaFileId);
        Assert.isTrue(numUnlocked == 0, "All documents need to be locked before the test");

        bulkOperationSetup(jpnLiahonaFileId);

        BulkOperations.bulkOperation(webDriver, "Source Unlock");
        BulkOperations.confirmAction(webDriver);


        numUnlocked = QADocumentService.numUnlockedByFileId(jpnLiahonaFileId);

        Assert.isTrue(numUnlocked == numDocsJpnLiahona, "Num docs unlocked: " + numUnlocked + "\nnumDocs needed to be unlocked: " + numDocsJpnLiahona);

    }


    @Test(priority = 9, groups={"selenium"}, timeOut=600000)
    public void bulkValidateLiahona() throws Exception {
        File startDirJpnLiahona = new File(Constants.bulkOperationsFileStartDir + "/Magazines/JpnLiahona");
        transformFiles(startDirJpnLiahona);
        int numValidatedCount = QADocumentService.numValidatedByFileId(jpnLiahonaFileId);
        int numValidationErrorsLiahona = QADocumentService.numValidationErrorsByFileId(jpnLiahonaFileId);
        QADocumentService.addedByFileId(jpnLiahonaFileId);
        QADocumentService.unvalidatedByFileId(jpnLiahonaFileId);
        int numV = QADocumentService.numValidatedByFileId(jpnLiahonaFileId);
        QADocumentService.deleteValidationErrorsByFileId(jpnLiahonaFileId);

        Assert.isTrue(numV == 0, "All documents need to be in the ADDED status before the test");

        bulkOperationSetup(jpnLiahonaFileId);

        BulkOperations.bulkOperation(webDriver, "Validate");
        BulkOperations.confirmAction(webDriver);

        int numValidationErrors2 = QADocumentService.numValidationErrorsByFileId(jpnLiahonaFileId);
        int numVal = QADocumentService.numValidatedByFileId(jpnLiahonaFileId);

        Assert.isTrue(numVal == numDocsJpnLiahona && numValidationErrorsLiahona == numValidationErrors2, "Num docs Validated: " + numValidatedCount + "\nnumDocs needed to be validated: " + numDocsJpnLiahona + "\nnumber of validation errors before validation: " + numValidationErrorsLiahona + "\nnumber of validation errors after: " + numValidationErrors2);
    }


    @Test(priority = 6, groups={"selenium"}, timeOut=180000)
    public void bulkApproveLiahona() throws SQLException, InterruptedException
    {
        QADocumentService.unapproveByFileId(jpnLiahonaFileId);
        int numApproved = QADocumentService.numApprovedByFileId(jpnLiahonaFileId);
        Assert.isTrue(numApproved == 0, "All documents need to be unapproved before the test");

        bulkOperationSetup(jpnLiahonaFileId);

        BulkOperations.bulkOperation(webDriver, "Approve");
        BulkOperations.confirmAction(webDriver);

        numApproved = QADocumentService.numApprovedByFileId(jpnLiahonaFileId);

        Assert.isTrue(numApproved == numDocsJpnLiahona, "Num docs Approved: " + numApproved + "\nnumDocs needed to be approved: " + numDocsSpaGenConf);
        QADocumentService.unapproveByFileId(jpnLiahonaFileId);

    }


    @Test(priority = 10, groups={"selenium"}, timeOut=180000)
    public void bulkUpdateGroupAndOwnerLiahona() throws SQLException, InterruptedException, SQLException, IllegalArgumentException
    {
        QADocumentService.updateGroupByFileIdGeneralConference(jpnLiahonaFileId);
        int numConferenceGroup = QADocumentService.numUpdatedGroupByFileIdLiahona(jpnLiahonaFileId);
        Assert.isTrue(numConferenceGroup == 0, "No documents in publication have the conference group");


        bulkOperationSetup(jpnLiahonaFileId);

        BulkOperations.bulkOperation(webDriver, "Update Group and Owner");
        BulkOperations.confirmAction(webDriver);
        UpdateGroupAndOwner.selectContentGroup(webDriver, "Liahona");
        UpdateGroupAndOwner.selectOwner(webDriver, "Jared Mackie");
        UpdateGroupAndOwner.saveAndClose(webDriver);

        numConferenceGroup = QADocumentService.numUpdatedGroupByFileIdLiahona(jpnLiahonaFileId);

        Assert.isTrue(numConferenceGroup == numDocsJpnLiahona, "Number of documents that must be changed to conference group is :" + numDocsJpnLiahona + "\nActual: " + numConferenceGroup);
        QADocumentService.updateGroupByFileIdGeneralConference(jpnLiahonaFileId);
    }


    @Test(priority = 12, groups={"selenium"}, timeOut=180000)
    public void bulkLockFailFatalValidationErrors() throws SQLException, InterruptedException, SQLException
    {

        QADocumentService.validatedByFileId(spaGeneralConferenceFileId);
        QADocumentService.unlockByFileId(spaGeneralConferenceFileId);

        int numLocked = QADocumentService.numLockedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numLocked == 0, "All documents need to be unlocked before the test");

        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Source Lock");
        BulkOperations.cancelAction(webDriver);

        numLocked = QADocumentService.numLockedByFileId(spaGeneralConferenceFileId);

        Assert.isTrue(numLocked == 0, "No documents should be locked due to the fatal validation errors");
    }

    @Test(priority = 13, groups={"selenium"}, timeOut=180000)
    public void bulkLockFailAdded() throws SQLException, InterruptedException, SQLException
    {

        QADocumentService.addedByFileId(spaGeneralConferenceFileId);
        QADocumentService.unvalidatedByFileId(spaGeneralConferenceFileId);

        int numLocked = QADocumentService.numLockedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numLocked == 0, "All documents need to be unlocked before the test");

        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Source Lock");
        BulkOperations.cancelAction(webDriver);

        numLocked = QADocumentService.numLockedByFileId(spaGeneralConferenceFileId);

        Assert.isTrue(numLocked == 0, "No documents should be locked due to the documents being added");
    }

    @Test(priority = 11, groups={"selenium"}, timeOut=180000)
    public void bulkApproveFail()throws SQLException, InterruptedException, IllegalArgumentException {

        QADocumentService.approveByFileId(spaGeneralConferenceFileId);

        int numApproved = QADocumentService.numApprovedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numApproved == 45, "All documents need to be approved before the test");

        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Approve");
        BulkOperations.cancelAction(webDriver);

        numApproved = QADocumentService.numApprovedByFileId(spaGeneralConferenceFileId);

        Assert.isTrue(numApproved == numDocsSpaGenConf, "Num docs Approved: " + numApproved + "\nnumDocs needed to be approved: " + numDocsSpaGenConf);
    }

    @Test(priority = 14, groups={"selenium"}, timeOut=180000)
    public void bulkUnlockFail() throws SQLException, InterruptedException, IllegalArgumentException {

        QADocumentService.unlockByFileId(spaGeneralConferenceFileId);
        QADocumentService.validatedByFileId(spaGeneralConferenceFileId);

        int numUnlocked = QADocumentService.numUnlockedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numUnlocked == numDocsSpaGenConf, "All documents need to be unlocked before the test");

        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Source Lock");
        BulkOperations.cancelAction(webDriver);

        numUnlocked = QADocumentService.numUnlockedByFileId(spaGeneralConferenceFileId);

        Assert.isTrue(numUnlocked == numDocsSpaGenConf, "all documents should be unlocked due to the documents already being unlockec");
    }

    @Test(priority = 15, groups={"selenium"}, timeOut=180000)
    public void bulkValidateFail() throws SQLException, InterruptedException, IllegalArgumentException {

        QADocumentService.validatedByFileId(spaGeneralConferenceFileId);
        QADocumentService.deleteFatalValidateErrorsByFileId(spaGeneralConferenceFileId);
        QADocumentService.lockByFileId(spaGeneralConferenceFileId);
        int numValidated = QADocumentService.numValidatedByFileId(spaGeneralConferenceFileId);
        Assert.isTrue(numValidated == numDocsSpaGenConf, "All documents need to be in the Validated status before the test");

        bulkOperationSetup(spaGeneralConferenceFileId);

        BulkOperations.bulkOperation(webDriver, "Validate");
        BulkOperations.cancelAction(webDriver);

        int numLocked = QADocumentService.numLockedByFileId(spaGeneralConferenceFileId);

        int numVal = QADocumentService.numValidatedByFileId(spaGeneralConferenceFileId);

        Assert.isTrue(numLocked == numDocsSpaGenConf && numVal == numDocsSpaGenConf , "No documents should have changed or been validated" );

    }

    public void bulkOperationSetup(String fileId) throws SQLException, InterruptedException, IllegalArgumentException {
        //Login as fono to content central
        fono = new FonoSaia();
        if(!fono.hasUsableClass()) {
            RoleAssignment.assignRole(fono, new ICS_Admin());
        }
        webDriver = PreTestSetup.setup();
        Login.login(webDriver, fono.getUsername(), fono.getPassword());

        if(!fono.hasActionRoles()) {
            String[] roles = {"ICS_ADMIN", "VIEWER", "CONTRIBUTOR", "EDITOR", "PROJECT_MANAGER", "PUBLISHER", "MANAGER", "LOCK", "UNLOCK", "APPROVER"};
            SetupTests.waitForLoading(webDriver);
            Dashboard.clickGearIcon(webDriver);
            UserManagement.goToManageUsersPage(webDriver);
            UserManagement.selectUserModal(webDriver, fono.getPreferred_name());
            UserManagement.userRoles(webDriver, roles);
            WebElement HomeButton = (new WebDriverWait(webDriver, 10))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(MenuButtonConstants.HomeButtonXpath)));
            HomeButton.click();
            SetupTests.waitForLoading(webDriver);
        }

        Search.searchFileId(webDriver);
        //search by spa conference by fileid
        Search.search(webDriver, fileId);
        WebElement rootCheckBox = (new WebDriverWait(webDriver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[5]/td[1]/span/span/input")));

        //check the root checkbox
        rootCheckBox.click();
        if (!rootCheckBox.isSelected()) {
            junit.framework.Assert.fail();
        } else {
            System.out.println("The root checkbox in row 5 was successfully checked");
        }

        Dashboard.openBulkOperationsMenu(webDriver);

    }

    public void transformFiles(File startDir) throws Exception {
        QADeleteService.deleteFromAnnotationsMLAndDB("bulk00298_002");
        List<File> filesToTransform = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        for (File file : filesToTransform) {
            System.out.println(file.getAbsolutePath());
            QATransformService.transformFileGivenContentGroupId(file, testContentGroupId);

        }
    }

}
