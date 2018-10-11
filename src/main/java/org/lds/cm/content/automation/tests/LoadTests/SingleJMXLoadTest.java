package org.lds.cm.content.automation.tests.LoadTests;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**Code example located at
 * http://uttesh.blogspot.com/2015/04/jmeter-load-testing-by-code-jmeter-api.html
 * https://stackoverflow.com/questions/25511949/trying-to-generate-jmeter-test-plan-jmx-with-jmeter-api-mismatch-between-jme
 *
 *
 * This code assumes that you have downloaded JMeter 4.0 from  https://jmeter.apache.org/download_jmeter.cgi
 *   And that you have unzipped the files into your "Documents" folder
 *   the code will need the file "/Documents/apache-jmeter-4.0/bin/jmeter.properties"
 *   in order to run


 *   If you have a windows machine there is an extra setup step
 *         On the search bar look up regedit
 *         Go to "HKEY_LOCAL_MACHINE/Software/JavaSoft"
 *         Right click on the folder, go to New, click Key
 *         Name the new Key "Prefs"

 *    (If you don't you'll get this error
 *      WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs
 at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5." )

 *     You will also need to make sure that your code has main/resources/LoadTesting/ReportFiles/SingleCSVAttempt.jmx
 *     Also if any of the needed CSV files are open with something like windows excel, the code will not work
 */

public class SingleJMXLoadTest {
    private static String LoggerFile = Constants.LoadTestingFilesStartDir + "/ReportFiles/report.jtl";
    private static String csvFile = Constants.LoadTestingFilesStartDir + "/ReportFiles/report.csv";
    private static String fixedJMXFile = Constants.LoadTestingFilesStartDir + "/ReportFiles/FinishedRun.jmx";
    private static String savedJMXFile = Constants.LoadTestingFilesStartDir + "/ReportFiles/SingleCSVAttempt.jmx";
    private static String createdCSVFile = Constants.LoadTestingFilesStartDir + "/SingleCSVInformation.csv";
    private static String dateTime = "";
    private static int entriesInFile = 100;


    @BeforeClass(alwaysRun = true)
    public void setUp() throws IOException {
        LoggerFile = LoggerFile.replace("\\.", "");
        File file = new File(LoggerFile);
        if (file.exists())
            file.delete();
        file.createNewFile();

        csvFile = csvFile.replace("\\." , "");
        file = new File(csvFile);
        if (file.exists())
            file.delete();
        file.createNewFile();

        fixedJMXFile = fixedJMXFile.replace("\\.", "");
        file = new File(fixedJMXFile);
        if (file.exists())
            file.delete();
        file.createNewFile();

        createdCSVFile = createdCSVFile.replace("\\.", "");
        file = new File(createdCSVFile);
        if (file.exists())
            file.delete();
        file.createNewFile();

        Date CurrentDate = new Date();
        dateTime = CurrentDate.getMonth() + "-" + CurrentDate.getDate() + "-" + CurrentDate.getYear() + "-" + CurrentDate.getTime();
    }

    @AfterMethod(alwaysRun = true)
    private void closeUp() {
        JDBCUtils.closeUp();
    }


    @Test
    public static void existingJMX() throws IOException, SQLException {
        // JMeter Engine
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        // Initialize Properties, logging, locale, etc.
        String JMeterLocation = System.getProperty("user.home");
        JMeterLocation += "/Documents/apache-jmeter-4.0";
        JMeterUtils.setJMeterHome(JMeterLocation);
        JMeterUtils.loadJMeterProperties(JMeterLocation + "/bin/jmeter.properties");
        JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
        JMeterUtils.initLocale();

        // Initialize JMeter SaveService
        SaveService.loadProperties();

        //SingleCSV route
        setUpSingleCSVFile();
        File edited = editJMX();
        HashTree testPlanTree = SaveService.loadTree(edited);

        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }

        ResultCollector csvlogger = new ResultCollector(summer);
        csvlogger.setFilename(csvFile);
        csvlogger.setProperty("responseData", true);
        csvlogger.setProperty("samplerData", true);
        csvlogger.setProperty("requestHeaders", true);
        csvlogger.setProperty("fieldNames", true);
        csvlogger.setProperty("dataType", false);
        csvlogger.setProperty("saveAsXML", false);
        csvlogger.setErrorLogging(true);
        testPlanTree.add(testPlanTree.getArray()[0], csvlogger);

        // Run JMeter Test
        jmeter.configure(testPlanTree);
        jmeter.run();

        configureResults();
    }

    private static void setUpSingleCSVFile() throws SQLException, FileNotFoundException, UnsupportedEncodingException {
        ArrayList<String> DocxFiles = new ArrayList<>();
        ArrayList<String> HtmlFiles = new ArrayList<>();
        ArrayList<String> SandboxPreviews = new ArrayList<>();
        ArrayList<String> Preview = new ArrayList<>();
        ArrayList<String> PublishIds = new ArrayList<>();

        //Fill in the arrayLists with the file paths and info from the database
        File directory = new File(Constants.transformFileStartDir + "/docx");
        File[] listOfFiles = directory.listFiles();
        for (File f : listOfFiles)
            DocxFiles.add(f.getAbsolutePath().replace(".\\", ""));

        directory = new File(Constants.transformFileStartDir + "/spaGeneralConferenceHTML/10");
        listOfFiles = directory.listFiles();
        for (File f : listOfFiles)
            HtmlFiles.add(f.getAbsolutePath().replace(".\\", ""));

        ResultSet rs = JDBCUtils.getRandomRows("document_sandbox", entriesInFile);
        while (rs.next())
            SandboxPreviews.add(rs.getString("document_sandbox_id") + "," + rs.getString("path"));
        rs.close();

        rs = JDBCUtils.getRandomRows("document", entriesInFile);
        while (rs.next()) {
            PublishIds.add(rs.getString("document_id"));
            String uriPath = rs.getString("path") + "/" + rs.getString("file_name");
            Preview.add(uriPath.replace(".html", "") + "," + QADeleteService.getLangAbbreviation(rs.getString("language_id")));
        }
        rs.close();

        //Verify that the arraylists have something before trying to write to files
        if (DocxFiles.size() == 0) DocxFiles.add("");
        if (HtmlFiles.size() == 0) HtmlFiles.add("");
        if (SandboxPreviews.size() == 0) SandboxPreviews.add(",");
        if (PublishIds.size() == 0) PublishIds.add("");
        if (Preview.size() == 0) Preview.add(",");

        File fix = new File(createdCSVFile);
        PrintWriter pw = new PrintWriter(fix, "UTF-8");
        int docxCount = 0, htmlCount = 0, spCount = 0, pubCount = 0, preCount = 0;

        for (int i = 0; i < entriesInFile; i++) {
            pw.println(DocxFiles.get(docxCount) + "," + HtmlFiles.get(htmlCount) + "," + Preview.get(preCount) + "," +
                    SandboxPreviews.get(spCount) + "," + PublishIds.get(pubCount));
            docxCount++;
            htmlCount++;
            spCount++;
            pubCount++;
            preCount++;

            //if the count would be out of bounds, reset to 0 and cycle through again
            if (docxCount >= DocxFiles.size()) docxCount = 0;
            if (htmlCount >= HtmlFiles.size()) htmlCount = 0;
            if (spCount >= SandboxPreviews.size()) spCount = 0;
            if (pubCount >= PublishIds.size()) pubCount = 0;
            if (preCount >= Preview.size()) preCount = 0;
        }

        pw.close();
    }

    private static File editJMX() throws IOException
    {
        File file = new File(savedJMXFile);
        BufferedReader br = new BufferedReader(new FileReader(file));

        File fix = new File(fixedJMXFile);
        if(fix.exists())
            fix.delete();
        fix.createNewFile();
        PrintWriter pw = new PrintWriter(fix, "UTF-8");

        String reading = br.readLine();
        while(reading != null)
        {
            if(reading.contains("\"filename\""))
                reading = replace(reading);
            else if(reading.contains("Argument.value"))
            {
                if(Constants.environment != "prod")
                    reading = reading.replace("stage", Constants.environment);
                else
                    reading = reading.replace("-stage", "");
            }

            pw.print(reading + "\n");
            reading = br.readLine();
        }
        pw.close();
        br.close();
        return fix;
    }

    /**Replace all the filenames so that they don't point to Rebecca's personal computer, but into the coding directory
     *      with date stamped unique files. */
    private static String replace(String line)
    {
        File base = new File(Constants.LoadTestingFilesStartDir);
        line = line.replace("C:\\Users\\rchiyop\\Desktop\\SingleCSVResults.xml", base.getAbsolutePath() + "\\Results\\SingleCSVResults-"
                        + dateTime + ".xml");
        line = line.replace("C:\\Users\\rchiyop\\Desktop\\SingleCSV.csv",  createdCSVFile);
        line = line.replace("C:\\Users\\rchiyop\\Desktop\\SingleCSVSummary.xml", base.getAbsolutePath() + "\\Results\\SingleCSVSummary-" + dateTime + ".csv");
        line = line.replace("\\.", "");
        return line;
    }

    /** Create a Summary File because I couldn't figure out how to do it with the jmeter api */
    private static void configureResults() throws IOException
    {
        File base = new File(Constants.LoadTestingFilesStartDir);
        File results = new File(base.getAbsolutePath() + "\\Results\\SingleCSVSummary-" + dateTime + ".csv");
        BufferedReader br = new BufferedReader(new FileReader(results));
        String current = br.readLine(); //first line has header names that won't go through the while loop parsing correctly

        //Arrays to hold all of the information.
        //1. EditoralTransform  2. Preview  3. Publish  4. SandboxPreview  5. SandboxTransform  6. UITransform
        int[] Counts = {0,0,0,0,0, 0};
        long[] Times = {0, 0, 0, 0, 0,0};
        int[] mins = {0,0,0,0,0,0};
        int[] maxs = {0,0,0,0,0,0};


        while((current = br.readLine()) != null)
        {
            String[] parts = current.split(",");

            //Grab the label that will tell you where in the array to add information
            int option = 5;
            if(parts[2].compareToIgnoreCase("EditorialTransform") == 0) option = 0;
            else if (parts[2].compareToIgnoreCase("Preview") == 0) option = 1;
            else if (parts[2].compareToIgnoreCase("QAPublishFile") == 0) option = 2;
            else if (parts[2].compareToIgnoreCase("QASandboxPreview") == 0) option = 3;
            else if (parts[2].compareToIgnoreCase("QASandboxTransformRequest") == 0) option = 4;
            //else choose UITransform

            int elapsedTime = 0;
            try{ elapsedTime = Integer.parseInt(parts[1]);} catch(NumberFormatException e){}

            Counts[option] += 1;
            Times[option] += elapsedTime;

            if(mins[option] == 0)
                mins[option] = elapsedTime;
            else if (elapsedTime < mins[option])
                mins[option] = elapsedTime;

            if(elapsedTime > maxs[option])
                maxs[option] = elapsedTime;

        }
        br.close();

        //Create the SimpleSummary file with all the necessary information
        File SimpleSummary = new File(base.getAbsolutePath() + "\\Results\\SimpleSummary-" + dateTime + ".csv");
        PrintWriter pw = new PrintWriter(SimpleSummary.getAbsolutePath(), "UTF-8");
        pw.println("ThreadName,NumOfThreads,AverageTime,MinTime,MaxTime");
        pw.println("EditorialTransform," + Counts[0] + "," + new BigDecimal(Times[0]/Counts[0]) + "," + mins[0] + "," + maxs[0]);
        pw.println("Preview," + Counts[1] + "," + new BigDecimal(Times[1]/Counts[1]) + "," + mins[1] + "," + maxs[1]);
        pw.println("QAPublishFile," + Counts[2] + "," + new BigDecimal(Times[2]/Counts[2]) + "," + mins[2] + "," + maxs[2]);
        pw.println("QASandboxPreview," + Counts[3] + "," + new BigDecimal(Times[3]/Counts[3]) + "," + mins[3] + "," + maxs[3]);
        pw.println("QASandboxTransform," + Counts[4] + "," + new BigDecimal(Times[4]/Counts[4]) + "," + mins[4] + "," + maxs[4]);
        long total = Counts[0] + Counts[1] + Counts[2] + Counts[3] + Counts[4];
        pw.println("Total," + total + "," + ((Times[0] + Times[1] + Times[2] + Times[3] + Times[4])/total) + "," + find(mins, true) + "," + find(maxs, false));
        pw.close();
    }

    //Find the min or max in an array of values
    private static int find(int[] values, boolean findMin)
    {
        int returning = values[0];
        if(findMin)
        {
            if(values[1] < returning)
                returning = values[1];
            if(values[2] < returning)
                returning = values[2];
            if(values[3] < returning)
                returning = values[3];
            if(values[4] < returning)
                returning = values[4];
        }
        else
        {
            if(values[1] > returning)
                returning = values[1];
            if(values[2] > returning)
                returning = values[2];
            if(values[3] > returning)
                returning = values[3];
            if(values[4] > returning)
                returning = values[4];

        }
        return returning;
    }
}