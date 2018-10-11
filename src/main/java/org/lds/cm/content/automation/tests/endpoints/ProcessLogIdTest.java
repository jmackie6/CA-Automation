package org.lds.cm.content.automation.tests.endpoints;

import java.io.IOException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class ProcessLogIdTest {

    // TODO test to make sure that there is a process log id in each log file entry in the database
    // TODO call the endpoint for each value in the log_file table
    // TODO make sure that the result is the same as what is in the database

    @AfterMethod(alwaysRun=true)
    public static void closeUp() { JDBCUtils.closeUp(); }

    /**
     * Test all the ProcessLogIds that are found in the logFile table
     */
    @Test
    public static void testAllProcessLogIdsWithEndpoint() throws IOException {
        System.out.println("\n**** Process Log Id test ****\n");
        System.out.println("Test: testURLs");
        System.out.println("Description: Test all the Print Javascript URLs to make sure that they are valid.");
        System.out.println("ML HOST: " + Constants.mlPreviewHost);
        System.out.println();

        List<String> process_log_ids = new ArrayList<>();
        List<String> failed_process_log_ids = new ArrayList<>();

        try {
            ResultSet rs = JDBCUtils.getResultSet("select process_log_id from CONTENT_AUTO.LOG_FILE");
            while (rs.next())
            {
                process_log_ids.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < process_log_ids.size(); i++) {
            String processString = Constants.epGetProcessLogId + process_log_ids.get(i);
            String processResponse = NetUtils.getHTML(processString);
            if (!processResponse.isEmpty()) {
                System.out.println(processString);

                int processStatusCode = NetUtils.getResponseStatus(processString);
                System.out.println("Status code: " + processStatusCode);
                int statusCode = NetUtils.getResponseStatus(processString);
                Assert.assertEquals(statusCode == 200, failed_process_log_ids.add(processString + " returned " + processStatusCode));

                if (processStatusCode != 200) {
                    failed_process_log_ids.add(processString + " returned " + processStatusCode);
                }
            }
        }

        Assert.assertEquals(failed_process_log_ids.size(), 0);
        System.out.println();
    }

    @Test
    public static void compareTextProcessLogEndpointAndDB() throws IOException {

        List<String> process_log_ids = new ArrayList<>();
        Map<String, String> epLogText = new HashMap<>();

        try {
            ResultSet rs = JDBCUtils.getResultSet("select log_text, process_log_id from CONTENT_AUTO.LOG_FILE");
            System.out.println("Comparing database and endpoint results...\n");
            while (rs.next()) {
                process_log_ids.add(rs.getString(2));
                // get JavaScript from PreviewJavascript endpoint and put into a map
                for (int i = 0; i < process_log_ids.size(); i++) {

                    String id = process_log_ids.get(i);
                    String processString = Constants.epGetProcessLogId + process_log_ids.get(i);
                    int statusCode = NetUtils.getResponseStatus(processString);
                    Assert.assertEquals(statusCode == 200, "Failing test since we did not get a 200 back from the call");
                    String processResponse = NetUtils.getHTML(processString);
                    if (!processResponse.isEmpty()) {
                        epLogText.put(id, processResponse);
                    }
                }
            }
                while (rs.next()) {
                    Blob blob = rs.getBlob(1);
                    byte[] bdata = blob.getBytes(1, (int) blob.length());
                    String dblog = new String(bdata);
                    String dbId = rs.getString(2);

                    System.out.println();

                    Assert.assertEquals(epLogText.get(dbId), dblog);
                }
                System.out.println();
            rs.close();
            } catch(SQLException e){
                e.printStackTrace();
            }

        }
    }