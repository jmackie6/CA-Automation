package org.lds.cm.content.automation.tests.LoadTests;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.sql.*;

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

 *     You will also need to make sure that your code has main/resources/LoadTesting/ReportFiles/CompleteRun.jmx
 *     Also if any of the needed CSV files are open, the code will not work
 */
public class TransformLoadTests
{
    private static String LoggerFile= Constants.LoadTestingFilesStartDir + "/ReportFiles/report.jtl";
    private static String csvFile = Constants.LoadTestingFilesStartDir + "/ReportFiles/report.csv";
    private static String fixedJMXFile = Constants.LoadTestingFilesStartDir + "/ReportFiles/FinishedRun.jmx";

    private enum fileChoice {Publishing, HtmlDocs, DocxDocs, UriLanguage, Preview}
    private static int entriesInFile = 1;


    @BeforeClass(alwaysRun = true)
    public void setUp() throws IOException
    {
        File file = new File(LoggerFile);
        if(file.exists())
            file.delete();
        file.createNewFile();

        file = new File(csvFile);
        if(file.exists())
            file.delete();
        file.createNewFile();
    }

    @AfterMethod (alwaysRun = true)
    private void closeUp()
    { JDBCUtils.closeUp(); }


    @Test
    public static void existingJMX() throws IOException, SQLException, CustomException
    {
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

        // Load existing .jmx Test Plan
        /** //Attempt using multiple csv files and one jmx
        for(fileChoice fc : fileChoice.values())
            setUpCSVFiles(fc);
        HashTree testPlanTree = SaveService.loadTree(setUpJMXFile());
         **/


        //SingleCSV route
        setUpSingleCSVFile();
        HashTree testPlanTree = SaveService.loadTree(new File(Constants.LoadTestingFilesStartDir + "/ReportFiles/SingleCSVAttempt.jmx"));

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
        csvlogger.setErrorLogging(true);
        testPlanTree.add(testPlanTree.getArray()[0], csvlogger);
        // Run JMeter Test
        jmeter.configure(testPlanTree);
        jmeter.run();
    }

    private static void setUpSingleCSVFile()
    {

    }

    //publishing file - docID (document table)
    //Preview file - uri/language
    //html doc paths
    //docx paths
    /** Create and fill the csv files */
    private static void setUpCSVFiles(fileChoice fc) throws IOException, SQLException, CustomException
    {
        File f = createFile(fc);
        PrintWriter pw = new PrintWriter(f, "UTF-8");
        if(fc == fileChoice.Publishing || fc == fileChoice.UriLanguage)
            pw.print(results(fc));
        else if(fc == fileChoice.Preview)
            pw.print(previewResults());
        else
            pw.print(fileList(fc));
        pw.close();
    }

    /** This method makes sure that the csv files are cleared and created */
    private static File createFile(fileChoice fc) throws IOException
    {
        File f = new File(Constants.LoadTestingFilesStartDir + "/Publishing.csv");
        switch (fc)
        {
            case Publishing: break;
            case HtmlDocs: f = new File(Constants.LoadTestingFilesStartDir + "/HtmlDocs.csv"); break;
            case DocxDocs: f = new File(Constants.LoadTestingFilesStartDir + "/DocxDocs.csv"); break;
            case UriLanguage: f = new File(Constants.LoadTestingFilesStartDir + "/UriLanguage.csv"); break;
            default: f = new File(Constants.LoadTestingFilesStartDir + "/SandboxPreview.csv"); break;
        }

        if(f.exists())
            f.delete();
        f.createNewFile();
        return f;
    }

    /** the document_sandbox table in dev is the only one with information to make the sandobx preview calls
     *     Hence the new connection and chaos   */
    private static String previewResults() throws SQLException
    {
        //Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@ldap://oid11g.ldschurch.org:3060/twdev1,cn=OracleContext,dc=wh,dc=ldsglobal,dc=net",
        //        "CONTENT_AUTO_PTI", "lR5n7AQqM3OaH4VkoIyW5S9skTiGJ5");
        //conn.createStatement();
        //PreparedStatement ps = conn.prepareStatement("select * from (select * from document_sandbox order by dbms_random.value) where rownum <= " + entriesInFile);
        //ResultSet rs = ps.executeQuery();
        ResultSet rs = JDBCUtils.getRandomRows("document_sandbox", entriesInFile);
        StringBuilder sb = new StringBuilder();
        while(rs.next())
        {
            String listLine =rs.getString("document_sandbox_id") + "," + rs.getString("path") + ",\n";
            //System.out.println(listLine);
            sb.append(listLine);
        }
        rs.close();
        //ps.close();
        //conn.close();
        return sb.toString();
    }

    /** Get values from the database to fill the preview/urilanguage and publishing csv files */
    private static String results(fileChoice fc) throws SQLException, CustomException
    {
        ResultSet rs = JDBCUtils.getRandomRows("document", entriesInFile);
        StringBuilder sb = new StringBuilder();

        switch(fc)
        {
            case Publishing:
                while(rs.next())
                    sb.append(rs.getInt("document_Id") + ",\n");
                break;
            case UriLanguage:
                while(rs.next())
                {
                    sb.append(rs.getString("path") + "/" + rs.getString("file_name") + ",");
                    sb.append(QADeleteService.getLangAbbreviation(rs.getString("language_id")) + ",\n");
                }
                break;
            default:
                throw new CustomException("Error, Wrong type?   " + fc);
        }
        rs.close();
        String returning = sb.toString();
        return returning.substring(0, returning.length() - 2);  //there is a trailing ,\n on the string
    }

    /** Fill the DocxDocs and HtmlDocs csv files with a list of absolute paths to the docs in the
     * dccx and spaGCHTML folders in the transform folder
     *
     * File saved in the html files have an additional \. in the uris that make the program not work*/
    private static String fileList(fileChoice fc) throws CustomException
    {
        StringBuilder sb = new StringBuilder();
        File directory = new File(Constants.transformFileStartDir + "/docx");

        if(fc == fileChoice.HtmlDocs)
            directory = new File(Constants.transformFileStartDir + "/spaGeneralConferenceHTML/10");
        else if(fc != fileChoice.DocxDocs)
            throw new CustomException("Error, wrong type of csv file creation");

        File[] listOfFiles = directory.listFiles();
        for(File f : listOfFiles) {
            String filePath = f.getAbsolutePath();
            filePath = filePath.replace(".\\", "");
            sb.append(filePath + ",\n");
        }

        String returning = sb.toString();
        return returning.substring(0, returning.length() - 2);  //there is a trailing ,\n on the string
    }



    /** The original CompleteRun.jmx is set up by using the GUI on Rebecca's computer.
     *      A little work has to be done to edit the jmx file to run on any computer.
     *      */
    private static File setUpJMXFile() throws  IOException
    {
        File file = new File(Constants.LoadTestingFilesStartDir + "/ReportFiles/CompleteRun.jmx");
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

    //maybe change Results.csv to something dynamic based off date so it can be run multiple times?
    /** This calls multiple replace methods on the lines of the xml document to replace all the
     *      absolute file paths from Rebecca's compueter to generic ones that work on any computer
     *      ased on the LoadTesting folder in the automation program*/
    private static String replace(String line)
    {
        File base = new File(Constants.LoadTestingFilesStartDir);
        line = line.replace("C:\\Users\\rchiyop\\Desktop\\docxIds.csv", base.getAbsolutePath() + "\\DocxDocs.csv");
        line = line.replace("C:\\Users\\rchiyop\\Desktop\\URI_Lang.csv", base.getAbsolutePath() + "\\UriLanguage.csv");
        line = line.replace("C:\\Users\\rchiyop\\Desktop\\loadTestingResults.xml", base.getAbsolutePath() + "\\ReportFiles\\Results.xml");
        line = line.replace("C:\\Users\\rchiyop\\Desktop\\htmlDocs.csv", base.getAbsolutePath() + "\\HtmlDocs.csv");
        line = line.replace("C:\\Users\\rchiyop\\Desktop\\publishingNumbers.csv", base.getAbsolutePath() + "\\Publishing.csv");
        line = line.replace("C:\\Users\\rchiyop\\Desktop\\Preview.csv", base.getAbsolutePath() + "\\SandboxPreview.csv");
        line = line.replace("\\.", "");
        return line;
    }

}
