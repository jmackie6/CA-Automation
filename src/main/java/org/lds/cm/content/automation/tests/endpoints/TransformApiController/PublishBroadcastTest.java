package org.lds.cm.content.automation.tests.endpoints.TransformApiController;

import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.NetUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public class PublishBroadcastTest {

    private final File testFile = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/lead-kindly-light.html");
    private final static String file_id = "testPD00028166_000_027";
    private final static String file_id2 = "TestPD60003501_000_21uchtdorf";
    private final static String file_id3 = "TestPD60003501_000_12marriott";
    private final static String file_id4 = "TestPD60003501_000_63koch";
    private final static String file_id5 = "TestPD60003501_000_26holland";

    @Test(groups = { "endpoints" })
    public void approveContent() throws Exception {

        File testFile2 = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/test-a-yearning-for-home.html");
        QADocumentService.unapproveByFileId(file_id2);
        String previewURL = QATransformService.transformFileGivenContentGroupId(testFile2, Constants.generalConferenceContentGroupId).previewURL;
        System.out.println(previewURL);
        // getting guid from transform success message
        int index = previewURL.lastIndexOf("=");
        String guid = previewURL.substring(index + 1, previewURL.length());
        System.out.println(guid);
        String jsonResult = NetUtils.getJson(Constants.epApprove + guid).toJSONString();
        System.out.println(jsonResult);
        int numApproved = QADocumentService.numApprovedByFileId(file_id2);
        Assert.assertTrue(jsonResult.contains("\"success\":\"1\"") && numApproved == 1, "Expected success, but found - " + jsonResult);
        // Delete file from database if successful
        if (jsonResult.contains("\"success\":\"1\"") && numApproved == 1){
            QADeleteService.deleteFromDbByFileIdSingleDocument(file_id2);
        }
    }

    @Test (groups = { "endpoints" })
    public void approveContentServicesAPI() throws Exception {
        File testFile3 = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/test-abiding-in-god-and-repairing-the-breach.html");
        QADocumentService.unapproveByFileId(file_id3);
        String previewURL = QATransformService.transformFileGivenContentGroupId(testFile3, Constants.generalConferenceContentGroupId).previewURL;
        System.out.println(previewURL);
        // getting guid from transform success message
        int index = previewURL.lastIndexOf("=");
        String guid = previewURL.substring(index + 1, previewURL.length());
        System.out.println(guid);
        String jsonResult = NetUtils.getJson(Constants.epPublishBroadcastSA + guid).toJSONString();
        System.out.println(jsonResult);
        int numApproved = QADocumentService.numApprovedByFileId(file_id3);
        Assert.assertTrue(jsonResult.contains("\"success\":\"1\"") && numApproved == 1, "Expected success, but found - " + jsonResult);
        // Delete file from database if successful
        if (jsonResult.contains("\"success\":\"1\"") && numApproved == 1){
            QADeleteService.deleteFromDbByFileIdSingleDocument(file_id3);
        }
    }

    @Test (groups = { "endpoints" })
    public void approveContentAgain() throws Exception {
        QADocumentService.approveByFileId(file_id);
        String previewURL = QATransformService.transformFileGivenContentGroupId(testFile, Constants.generalConferenceContentGroupId).previewURL;

        // getting guid from transform success message
        int index = previewURL.lastIndexOf("=");
        String guid = previewURL.substring(index + 1, previewURL.length());
        System.out.println(guid);
        String jsonResult = NetUtils.getJson(Constants.epApprove + guid).toJSONString();
        System.out.println(jsonResult);
        int numApproved = QADocumentService.numApprovedByFileId(file_id);
        Assert.assertTrue(jsonResult.contains("\"success\":\"1\"") && numApproved == 1, "Expected success, but found - " + jsonResult);
        // Delete file from database if successful
        if (jsonResult.contains("\"success\":\"1\"") && numApproved == 1){
            QADeleteService.deleteFromDbByFileIdSingleDocument(file_id);
        }
    }

    @Test (groups = { "endpoints" })
    public void approveContentNoGuidPassedIn() throws Exception {
        File testFile4 = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/test-apart-but-still-one.html");
        QADocumentService.unapproveByFileId(file_id4);
        String previewURL = QATransformService.transformFileGivenContentGroupId(testFile4, Constants.generalConferenceContentGroupId).previewURL;
        System.out.println(previewURL);
        // getting guid from transform success message
        int index = previewURL.lastIndexOf("=");
        String guid = previewURL.substring(index + 1, previewURL.length());
        System.out.println(guid);
        String jsonResult = NetUtils.getJson(Constants.epApprove).toJSONString();
        System.out.println("Json Returned: " + jsonResult);
        int numApproved = QADocumentService.numApprovedByFileId(file_id4);
        Assert.assertTrue(jsonResult.contains("\"success\":\"0\"") && jsonResult.contains("No GUID specified.") && numApproved == 0, "Expected success, but found - " + jsonResult);
        // Delete file from database if successful
        if (jsonResult.contains("\"success\":\"1\"") && numApproved == 1){
            QADeleteService.deleteFromDbByFileIdSingleDocument(file_id4);
        }
    }

    @Test (groups = { "endpoints" })
    public void approveContentInvalidGuid() throws Exception {
        File testFile5 = new File(Constants.transformFileStartDir + "/engGeneralConferenceHTML/10/test-be-ye-therefore-perfect-eventually.html");
        QADocumentService.unapproveByFileId(file_id5);
        String invalidGuid = "4634444447474747747474747";
        String previewURL = QATransformService.transformFileGivenContentGroupId(testFile5, Constants.generalConferenceContentGroupId).previewURL;
        System.out.println(previewURL);

        String jsonResult = NetUtils.getJson(Constants.epApprove + invalidGuid).toJSONString();
        System.out.println("Json Returned: " + jsonResult);
        int numApproved = QADocumentService.numApprovedByFileId(file_id5);
        Assert.assertTrue(jsonResult.contains("Invalid GUID") && jsonResult.contains("\"success\":\"0\"") && numApproved == 0,"Expected success, but found - " + jsonResult);
        // Delete file from database if successful
        if (jsonResult.contains("\"success\":\"1\"") && numApproved == 1){
            QADeleteService.deleteFromDbByFileIdSingleDocument(file_id5);
        }
    }

}
