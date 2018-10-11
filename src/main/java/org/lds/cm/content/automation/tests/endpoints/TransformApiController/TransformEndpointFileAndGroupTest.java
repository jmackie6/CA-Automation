package org.lds.cm.content.automation.tests.endpoints.TransformApiController;


import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.*;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.File;

/*
 This class will test the transform endpoint related to groups and making sure that a content group is passed in or that there is a default content group selected.
 */
public class TransformEndpointFileAndGroupTest {

//    public File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/lead-kindly-light.html");

    //TODO write tests to make sure that file is in the database with the right information

    @AfterMethod(alwaysRun = true)
    public static void closeUp() { JDBCUtils.closeUp(); }

    @Test(groups="endpoints")
    public void transformNoContentGroupNoDefaultGroup() throws Exception {
        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/lead-kindly-light.html");
        QATransformService.uncheckDefaultContentGroup();
        QATransformationResult jsonResponse = null;
        String contentGroup = "";
        boolean transformResult = QATransformService.transformFileWithoutContentGroup(testFile).wasSuccessful();
        Assert.assertEquals(transformResult, false, "Test Failed: cant transform document if there is no content group passed or default content group selected");

        if(!transformResult)
        {
            System.out.println("Test Successful: cant transform document if there is no content group passed or default content group selected");
        }
    }

    @Test(groups="endpoints")
    public void transformNoContentGroupYesDefaultGroup() throws Exception {
        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/test-a-yearning-for-home.html");
        QATransformService.checkGeneralConferenceAsDefaultContentGroup();
        boolean transformResult = QATransformService.transformFileWithoutContentGroup(testFile).wasSuccessful();
        Assert.assertEquals(transformResult, true, "Test Failed: cant transform document if there is no content group passed in but there is a default content group selected");

        if(transformResult)
        {
            System.out.println("Test Successful: can transform document if there is no content group passed in but there is a default content group selected");
        }

        // Delete file after a successful transform so that the test can be rerun.
        QADeleteService.deleteFromDbByFileIdSingleDocument("TestPD60003501_000_21uchtdorf");
    }


    @Test(groups="endpoints")
    public void transformYesContentGroupNoDefaultGroup() throws Exception {
        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/test-abiding-in-god-and-repairing-the-breach.html");
        String contentGroupId = "1";
        QATransformService.uncheckDefaultContentGroup();
        boolean transformResult = QATransformService.transformFileGivenContentGroupId(testFile, contentGroupId).wasSuccessful();
        Assert.assertEquals(transformResult, true, "Test Failed: cant transform document if there is a content group passed in but there isn't a default content group selected");
        if(transformResult)
        {
            System.out.println("Test Successful: can transform document if there is a content group passed in but there isn't a default content group selected");
        }

        // Delete file after a successful transform so that the test can be rerun.
        QADeleteService.deleteFromDbByFileIdSingleDocument("TestPD60003501_000_12marriott");

    }

    @Test(groups="endpoints")
    public void transformYesContentGroupYesDefaultGroup() throws Exception {
        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/test-apart-but-still-one.html");
        QATransformService.checkGeneralConferenceAsDefaultContentGroup();
        String contentGroupId = "1";
        boolean transformResult = QATransformService.transformFileGivenContentGroupId(testFile, contentGroupId).wasSuccessful();
        Assert.assertEquals(transformResult, true, "Test Failed: cant transform document if there is a content group passed in and there is a default content group selected/\n the content group passed in is the one selected");
        if(transformResult)
        {
            System.out.println("Test Successful: can transform document if there is a content group passed in and there is a default content group selected/\n the content group passed in is the one selected");
        }

        // Delete file after a successful transform so that the test can be rerun.
        QADeleteService.deleteFromDbByFileIdSingleDocument("TestPD60003501_000_63koch");
    }

    @Test(groups="endpoints")
    public void transformBadContentGroupNoDefaultGroup() throws Exception {
        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/lead-kindly-light.html");
        QATransformService.uncheckDefaultContentGroup();
        String contentGroupId = "2000";
        boolean transformResult = QATransformService.transformFileWithoutContentGroup(testFile).wasSuccessful();
        Assert.assertEquals(transformResult, false, "Test Failed: cant transform document if a bad content group is passed and there is no default content group selected");

        if(!transformResult)
        {
            System.out.println("Test Successful: cant transform document if an invalid content group id was passed in  and no default content group selected");
        }
    }


    @Test(groups="endpoints")
    public void transformBadContentGroupYesDefaultGroup() throws Exception {
        File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/test-be-ye-therefore-perfect-eventually.html");
        QATransformService.checkGeneralConferenceAsDefaultContentGroup();
        String contentGroupId = "2000";
        boolean transformResult = QATransformService.transformFileGivenContentGroupId(testFile, contentGroupId).wasSuccessful();
        Assert.assertEquals(transformResult, true, "Test Failed: cant transform document if there is bad content group passed in but there is a default content group selected/\n");
        if(transformResult)
        {
            System.out.println("Test Successful: can transform document if there is bad content group passed in but there is a default content group selected/\n");
        }

        // Delete file after a successful transform so that the test can be rerun.
        QADeleteService.deleteFromDbByFileIdSingleDocument("TestPD60003501_000_26holland");
    }



}
