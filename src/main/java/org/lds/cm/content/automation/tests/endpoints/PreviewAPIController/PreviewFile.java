package org.lds.cm.content.automation.tests.endpoints.PreviewAPIController;

import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.QAFileUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreviewFile {

    /* this test transforms a folder of documents in order to grab the guids returned in the success message,
       then uses those guids to preview them through the previewFile endpoint for ws/v1 and services/api controllers
       (we have to transform documents to grab their guids because guids expire after a certain amount of time in the DB) */

    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException
    {   APIRules.fixAPIRule("ws/v1/previewFile");   }

    @Test (groups = { "endpoints" })
    public static void previewDocTypes() throws Exception {

        // path to folder used, could use any folder of documents really
        File startDir = new File(Constants.transformFileStartDir + "/engEnsignHTML");
        String ensignContentGroupId = "2";
        String previewURL = "";
        List<File> filesToTransform = new ArrayList<>();

        List<String> passed_docs = new ArrayList<>();
        List<String> failure1 = new ArrayList<>();
        List<String> failure2 = new ArrayList<>();
        List<String> failure3 = new ArrayList<>();

        QAFileUtils.loadTestFiles(filesToTransform, startDir);

        // transform and preview each document in folder
        for (File file : filesToTransform) {
            previewURL = QATransformService.transformFileGivenContentGroupId(file, ensignContentGroupId).previewURL;

            // getting guid from transform success message
            int index = previewURL.lastIndexOf("=");
            String guid = previewURL.substring(index + 1, previewURL.length());

            // passing guid to endpoint for EACH controller
            String byGuid1 = NetUtils.getHTML(Constants.baseURL + "/ws/v1/previewFile?guid=" + guid);
            String byGuid2 = NetUtils.getHTML(Constants.baseURL + "/services/api/previewFile?guid=" + guid);

            String docFileId = "";
            String wsv1FileId = "";
            String servicesApiFileId = "";

            // looking for file id in the document transformed, the document returned by ws/v1, and the document returned by services/api
            Pattern pattern = Pattern.compile("\"file\">(.*?)<");
            Matcher originalDoc = null;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String file_contents;

            int num = file.getPath().lastIndexOf("/");

            while ((file_contents = br.readLine()) != null) {
                originalDoc = pattern.matcher(file_contents);

                if (originalDoc.find()) {
                    docFileId = originalDoc.group(1);
                } // no else statement here since it's looking line by line instead of the whole document at once
            }
            br.close();

            Matcher wsV1 = pattern.matcher(byGuid1);
            Matcher servicesApi = pattern.matcher(byGuid2);

            if (wsV1.find() && servicesApi.find()) {
                wsv1FileId = wsV1.group(1);
                servicesApiFileId = servicesApi.group(1);
            } else {
                org.junit.Assert.fail("\nA File ID wasn't found in one or both of the following:\nThe previewFile endpoint results from ws/v1 controller.\nThe previewFile endpoint results from services/api controller.");
            }

            /* checking that the file ids grabbed from the endpoints match each other and the file id from the original document
               depending on the results, the document name will be added to one of 4 lists */
            if (!wsv1FileId.equals(servicesApiFileId) && wsv1FileId.equals(docFileId)) {
                failure1.add(file.getPath().substring(num + 1));
            } else if (wsv1FileId.equals(servicesApiFileId) && !wsv1FileId.equals(docFileId)) {
                failure2.add(file.getPath().substring(num + 1));
            } else if (!wsv1FileId.equals(servicesApiFileId) && !wsv1FileId.equals(docFileId)) {
                failure3.add(file.getPath().substring(num + 1));
            } else {
                passed_docs.add(file.getPath().substring(num + 1));
            }
        }

        // if the total size of the failure lists is greater than 0, we display the error below along with the associated documents
        int total_size = failure1.size() + failure2.size() + failure3.size();
        Assert.assertFalse(total_size > 0, "\n\nSome or all documents were not successfully returned by the previewFile endpoint for ws/v1 and services/api controllers. Below are specific errors and the associated documents.\n\n"
                + "ERROR: The file id pulled from the ws/v1 controller document, doesn't match the file id pulled from services/api controller document. One of the two are incorrect.\nDOCUMENTS AFFECTED: "
                + failure1 + "\n\nERROR: One of the two file id's pulled from documents returned by the ws/v1 and services/api controllers, doesn't match the database file id associated with the document. Either one or both of the documents wasn't returned, or was incorrect.\nDOCUMENTS AFFECTED: "
                + failure2 + "\n\nERROR: The file id pulled from the ws/v1 controller document, doesn't match the file id pulled from services/api controller document. One of the two are incorrect.\nAND\nOne of the two file id's (pulled from documents returned by the ws/v1 and services/api controllers) doesn't match the database file id associated with the document.\nEither one or both of the documents wasn't returned, or was incorrect.\nDOCUMENTS AFFECTED: " + failure3 + "\n");

        // if there are no documents in any of the failure lists, the success message below will be displayed with the associated documents
        System.out.println("\nThe following documents were successfully returned by the previewFile endpoint for ws/v1 and services/api controllers:\n" + passed_docs);
    }
}
