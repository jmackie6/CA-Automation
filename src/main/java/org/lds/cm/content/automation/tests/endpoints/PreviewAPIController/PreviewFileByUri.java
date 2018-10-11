package org.lds.cm.content.automation.tests.endpoints.PreviewAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreviewFileByUri {

    /* This test checks that the ws/v1 and services/api controllers will return documents through
       the previewFileByUri endpoint for added, locked, and validated documents */
    @BeforeClass (alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException
    {   APIRules.fixAPIRule("ws/v1/previewFileByUri");   }


    @AfterClass (alwaysRun = true)
    public void afterClass() {
        JDBCUtils.closeUp();
    }

    @Test (groups = { "endpoints" })
    public static void previewDocTypes() throws IOException {

        List<String> docInfo = new ArrayList<>();

        String ep_file_id1 = "";
        String ep_file_id2 = "";

        int[] status = new int[4];
        String[] status_title = {"ADDED", "LOCKED", "VALIDATED"};

        for (int i = 1; i <= 3; i++) {

             /* filling list with basic doc info from the two result sets. sql query uses for loop iterator to define the document_state_id.
               added - 1, locked - 2, validated - 3*/
            try {
                ResultSet cssId = JDBCUtils.getResultSet("SELECT PREVIEW_CSS_ID FROM (SELECT * FROM PREVIEW_CSS ORDER BY dbms_random.value ) where rownum = 1");
                ResultSet doc = JDBCUtils.getResultSet("SELECT LANGUAGE_ID, PATH, FILE_NAME, FILE_ID FROM (SELECT * FROM DOCUMENT WHERE DOCUMENT_STATE_ID = " + i + " ORDER BY dbms_random.value) WHERE rownum = 1 ORDER BY 4");

                while (cssId.next() && doc.next()) {
                    docInfo.add(cssId.getString(1));
                    docInfo.add(doc.getString(1));
                    docInfo.add(doc.getString(2));
                    docInfo.add(doc.getString(3));
                    docInfo.add(doc.getString(4));
                }
                cssId.close();
                doc.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }


            // stripping .html from the uri, endpoint doesn't respond if it is included
            int index = docInfo.get(3).lastIndexOf(".");
            String file_name = docInfo.get(3).substring(0, index);

            // piecing together URI using list info
            String uri = docInfo.get(2) + "/" + file_name;

            // assembling endpoint url for ws/v1 controller
            String byUri1 = NetUtils.getHTML(Constants.baseURL + "/ws/v1/previewFileByUri?cssId=" + docInfo.get(0) + "&languageId=" + docInfo.get(1) + "&uri=" + uri);
            // assembling endpoint url for services/api controller
            String byUri2 = NetUtils.getHTML(Constants.baseURL + "/services/api/previewFileByUri?cssId=" + docInfo.get(0) + "&languageId=" + docInfo.get(1) + "&uri=" + uri);

            // checking content returned by the endpoint for a file id
            Pattern pattern = Pattern.compile("\"file\">(.*?)<");
            Matcher matcher1 = pattern.matcher(byUri1);
            Matcher matcher2 = pattern.matcher(byUri2);

            /* if a file id is found in the content returned by the ws/v1 controller, set it to the variable.
               otherwise, fail the test (if no file id is found the document wasn't successfully returned) */
            if (matcher1.find()) {
                ep_file_id1 = matcher1.group(1);
            } else {
                Assert.fail("\nws/v1 controller was not successful in returning a document\n");
            }

            // do the same thing for the services/api controller
            if (matcher2.find()) {
                ep_file_id2 = matcher2.group(1);
            } else {
                Assert.fail("\nservices/api controller was not successful in returning a document\n");
            }

            // each scenario is assigned a number that will be used to determine the outcome of the test in the switch statement below
            if (!ep_file_id1.equals(ep_file_id2) && ep_file_id1.equals(docInfo.get(4))) {
                status[i] = 1;
            } else if (ep_file_id1.equals(ep_file_id2) && !ep_file_id1.equals(docInfo.get(4))) {
                status[i] = 2;
            } else if (!ep_file_id1.equals(ep_file_id2) && !ep_file_id1.equals(docInfo.get(4))) {
                status[i] = 3;
            } else {
                status[i] = 4;
            }

            // the value of status[i] will determine the message the user receives regarding the outcome of test
            switch (status[i]) {
                case 1:
                    System.out.println("\n" + status_title[i - 1] + " DOCUMENT: The file id pulled from the ws/v1 controller document (" + ep_file_id1 + "), doesn't match the file id pulled from services/api controller document (" + ep_file_id2 + ").\nOne of the two are incorrect.\n");
                    break;
                case 2:
                    System.out.println("\n" + status_title[i - 1] + " DOCUMENT: One of the two file id's pulled from documents returned by the ws/v1 (" + ep_file_id1 + ") and services/api controllers (" + ep_file_id2 + ") doesn't match the database file id associated with the document (" + docInfo.get(4) + ").\nEither one or both of the documents wasn't returned, or was incorrect.\n");
                    break;
                case 3:
                    System.out.println("\n" + status_title[i - 1] + " DOCUMENT: The file id pulled from the ws/v1 controller document (" + ep_file_id1 + "), doesn't match the file id pulled from services/api controller document (" + ep_file_id2 + ").\nOne of the two are incorrect.\nAND\nOne of the two file id's (pulled from documents returned by the ws/v1 and services/api controllers) doesn't match the database file id associated with the document.\nEither one or both of the documents wasn't returned, or was incorrect.\n");
                    break;
                case 4:
                    System.out.println("\n" + status_title[i - 1] + " DOCUMENT: ws/v1 and services/api endpoints both successfully returned the document.\nHere are the associated file id's from both controllers and the database:\nws/vi: " + ep_file_id1 + "\nservices/api: " + ep_file_id2 + "\ndatabase: " + docInfo.get(4));
            }

            // clear this list or the sql query data for the first query will be used for each type of document
            docInfo.clear();

        }
        // officially fail or pass the test. the messages in the switch statement above give more detailed info about why the test failed
        Assert.assertFalse(status[1] < 4 || status[2] < 4 || status[3] < 4, "\nDocuments were not returned correctly by the previewFileByUriAndLanguage endpoints for ADDED, LOCKED, or VALIDATED documents. See the output above for details.\n");
    }
}
