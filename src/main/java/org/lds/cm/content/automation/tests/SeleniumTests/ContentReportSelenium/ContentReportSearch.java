package org.lds.cm.content.automation.tests.SeleniumTests.ContentReportSelenium;

import org.lds.cm.content.automation.model.UserModels.FonoSaia;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.ContentReport;
import org.lds.cm.content.automation.util.SeleniumUtil.PreTestSetup;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.util.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContentReportSearch {
    private WebDriver webDriver;
    private FonoSaia fono;

    @AfterClass(alwaysRun = true, timeOut = 180000)
    public void closeUp() throws SQLException {
        webDriver.quit();
    }

    @Test(timeOut = 180000)
    public void contentReportSearchByURI() throws SQLException, InterruptedException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        String search = ContentReport.getSearchCriteriaBasedOnEnvironment();
        ContentReport.goToContentReportAndSearch(webDriver, search);


        // After search has loaded, check that there are results by counting the number of table rows
        List<WebElement> tableRows = webDriver.findElements(By.xpath("html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr"));
        int tableRowCount = tableRows.size();
        Assert.isTrue(tableRowCount > 0, "Nothing was returned from the search.");
    }

    @Test(timeOut = 180000)
    public void contentReportSearchByFileId() throws SQLException, InterruptedException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        // Click the Reports link then click on Content Report
        String xPathToReports = "//*[@id=\"reports-id\"]/a";
        String xPathToContentReport = "//*[@id=\"content-report-id\"]/a";
        ContentReport.clickReportsLinkThenContentReport(webDriver, xPathToReports, xPathToContentReport);
        BrowserUtils.sleep(5000);


        //Click the File ID radio button before entering the search
        String xPathToFileIdRadioBtn = "/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[3]/input";
        WebElement fileId = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xPathToFileIdRadioBtn)));
        fileId.click();

        // Start a search for something you know will return results
        WebElement searchBox = webDriver.findElement(By.xpath(MenuButtonConstants.PathToContentReportSearchBox));
        searchBox.sendKeys("PD60003501_893_12mcconkie");

        // Click the search button
        WebElement searchButton = ContentReport.contentReportPageSearchButton(webDriver);
        searchButton.click();
        BrowserUtils.sleep(15000);

        // Check to see if a single row is returned
        List<WebElement> tableRows = webDriver.findElements(By.xpath("html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr"));
        int tableRowCount = tableRows.size();
        Assert.isTrue(tableRowCount==1, "Nothing was returned from the search.");

        // File ID search with PDNumber and Language Code and with just PDNumber
        // Find the number of documents and make sure they are not the same
        searchBox.clear();
        searchBox.sendKeys("PD60003501_893");
        searchButton.click();
        BrowserUtils.sleep(5000);
        WebElement documentsNumber = webDriver.findElement(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[3]/div/div[3]/div/button[1]/b"));
        String compareDocNum = documentsNumber.getText();
        System.out.println(compareDocNum);

        searchBox.clear();
        searchBox.sendKeys("PD60003501");
        searchButton.click();
        BrowserUtils.sleep(5000);
        WebElement documentsNumberPDNum = webDriver.findElement(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[3]/div/div[3]/div/button[1]/b"));
        String compareDocNumPDNum = documentsNumberPDNum.getText();
        System.out.println(compareDocNumPDNum);

        Assert.isTrue(compareDocNum != compareDocNumPDNum, "The search by File ID using PDNumber and Lang Code returned the same number of results as searching" +
                "by PDNumber only.  These Numbers should be different.");

    }

    @Test(timeOut = 180000)
    public void contentReportSearchByFileName() throws SQLException, InterruptedException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        // Click the Reports link then click on Content Report
        String xPathToReports = "//*[@id=\"reports-id\"]/a";
        String xPathToContentReport = "//*[@id=\"content-report-id\"]/a";
        ContentReport.clickReportsLinkThenContentReport(webDriver, xPathToReports, xPathToContentReport);
        BrowserUtils.sleep(5000);


        //Click the File ID radio button before entering the search
        String xPathToFileNameRadioBtn = "/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[4]/input";
        WebElement fileName = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xPathToFileNameRadioBtn)));
        fileName.click();

        // Start a search for something you know will return results
        WebElement searchBox = webDriver.findElement(By.xpath(MenuButtonConstants.PathToContentReportSearchBox));
        searchBox.sendKeys("three-sisters.html");

        // Click the search button
        WebElement searchButton = ContentReport.contentReportPageSearchButton(webDriver);
        searchButton.click();
        BrowserUtils.sleep(15000);

        // Check to see if a single row is returned
        List<WebElement> tableRows = webDriver.findElements(By.xpath("html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr"));
        int tableRowCount = tableRows.size();
        Assert.isTrue(tableRowCount > 0, "Nothing was returned from the search.");
    }

    @Test(timeOut = 180000)
    public void contentReportSearchByURIAndLang() throws SQLException, InterruptedException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        // Click the Reports link then click on Content Report
        String xPathToReports = "//*[@id=\"reports-id\"]/a";
        String xPathToContentReport = "//*[@id=\"content-report-id\"]/a";
        ContentReport.clickReportsLinkThenContentReport(webDriver, xPathToReports, xPathToContentReport);

        // Start a search for something you know will return results
        BrowserUtils.sleep(5000);
        WebElement searchBox = webDriver.findElement(By.xpath(MenuButtonConstants.PathToContentReportSearchBox));
        searchBox.sendKeys("/general-conference/2017/04");
        BrowserUtils.sleep(5000);
        // Click into the "Select Languages" box
        WebElement langBox = webDriver.findElement(By.xpath("//*[@id=\"languages_chosen\"]/ul/li/input"));
        langBox.click();
        langBox.sendKeys("engl");
        // Select the english language
        String langEng = "//*[@id=\"languages_chosen\"]/div/ul/li";
        WebElement englishChoice = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(langEng)));
        englishChoice.click();
        // Click the search button
        WebElement searchButton = ContentReport.contentReportPageSearchButton(webDriver);
        searchButton.click();
        BrowserUtils.sleep(15000);

        //Click the page size button
        WebElement pageSize = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[3]/div/div[1]/div/button[7]")));
        pageSize.click();
        BrowserUtils.sleep(500);

        // After search has loaded, check that there are results by counting the number of table rows
        List<WebElement> tableRowsSingleLang = webDriver.findElements(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[4]"));
        int tableRowCount = tableRowsSingleLang.size();
        ArrayList<String> inCorrectLangs = new ArrayList<>();
        for(int i = 0; i < tableRowCount; i++){
//            System.out.println(tableRows.get(i).getText());

            if (!tableRowsSingleLang.get(i).getText().equals("Eng")){
                inCorrectLangs.add("This language is not correct: " + tableRowsSingleLang.get(i).getText());
            }
        }

        // Assert that the inCorrectLangs Array size is 0
        if (inCorrectLangs.size() > 0){
            System.out.println(inCorrectLangs);
            Assert.isTrue(inCorrectLangs.size() == 0, "There was a language returned that was not specified in the search.");
        } else {
            Assert.isTrue(inCorrectLangs.size() == 0, inCorrectLangs.toString());
        }

        // Check multiple languages
        langBox.clear();
        langBox.click();
        langBox.sendKeys("Italian");
        langBox.sendKeys(Keys.RETURN);
        langBox.sendKeys("Spanish");
        langBox.sendKeys(Keys.RETURN);
        BrowserUtils.sleep(10000);

        searchButton.click();
        BrowserUtils.sleep(10000);
        List<WebElement> tableRowsMultiLang = webDriver.findElements(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[4]"));
        int multiLangCount = tableRowsMultiLang.size();

        Assert.isTrue(tableRowCount < multiLangCount, "The numbers are the same and they should not be. Manually check to see if English, Spanish and Italian " +
                "are found in the search.");

    }

    @Test(timeOut = 180000)
    public void searchByCreatedDate() throws SQLException, InterruptedException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        String search = ContentReport.getSearchCriteriaBasedOnEnvironment();
        WebElement searchButton = ContentReport.goToContentReportAndSearch(webDriver, search);

        ContentReport.clickIntoCreatedDateBox(webDriver);
        String xPathToOption = "/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/select/option[1]";
        ContentReport.createdModifiedOrPublishedDate(webDriver, xPathToOption);

        // Enter a date into the date fields format = yyyy/mm/dd
        WebElement dateBox = webDriver.findElement(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/input[1]"));

        // Get a date to enter into the date field
        String date = ContentReport.getDateBasedOnEnvironment();

        dateBox.sendKeys(date);
        dateBox.sendKeys(Keys.RETURN);
        BrowserUtils.sleep(1000);

        // Click the search button again
        searchButton.click();
        BrowserUtils.sleep(500);

        String rows = "";
        if (Constants.baseURL.contains("publish-stage")){
            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[5]";
        } else {
            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[6]";
        }

        // Get array of all created dates and make sure they are all the same
        List<WebElement> createdDates = webDriver.findElements(By.xpath(rows));
        ArrayList<String> errors = new ArrayList<>();
        for(int i = 0; i < createdDates.size(); i++){
            if (!createdDates.get(i).getText().contains(ContentReport.getMasterCreated())){
                errors.add(createdDates.get(i).getText());
            }
        }

        Assert.isTrue(errors.size()==0, errors.toString());
    }

    @Test(timeOut = 180000)
    public void searchByModifiedDate() throws SQLException, InterruptedException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        String search = ContentReport.getSearchCriteriaBasedOnEnvironment();
        WebElement searchButton = ContentReport.goToContentReportAndSearch(webDriver, search);

        ContentReport.clickIntoCreatedDateBox(webDriver);
        String xPathToOption = "/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/select/option[2]";
        ContentReport.createdModifiedOrPublishedDate(webDriver, xPathToOption);

        // Enter a date into the date fields format = yyyy/mm/dd
        WebElement dateBox = webDriver.findElement(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/input[1]"));
        // Get a date to enter into the date field
        String date = ContentReport.getDateBasedOnEnvironment();
        dateBox.sendKeys(date);
        dateBox.sendKeys(Keys.RETURN);
        BrowserUtils.sleep(1000);

        // Click the search button again
        searchButton.click();
        BrowserUtils.sleep(500);

        String rows = "";
        if (Constants.baseURL.contains("publish-stage")){
            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[6]";
        } else {
            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[7]";
        }

        // Get array of all created dates and make sure they are all the same
        List<WebElement> modifiedDates = webDriver.findElements(By.xpath(rows));
        ArrayList<String> errors = new ArrayList<>();
        for(int i = 0; i < modifiedDates.size(); i++){
            if (!modifiedDates.get(i).getText().contains(ContentReport.getMasterModified())){
                errors.add(modifiedDates.get(i).getText());
            }
        }

        Assert.isTrue(errors.size()==0, errors.toString());
        BrowserUtils.sleep(5000);
    }

    @Test (timeOut = 180000)
    public void searchByPublishDate() throws SQLException, InterruptedException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        String search = ContentReport.getSearchCriteriaBasedOnEnvironment();
        WebElement searchButton = ContentReport.goToContentReportAndSearch(webDriver, search);

        ContentReport.clickIntoCreatedDateBox(webDriver);
        String xPathToOption = "/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/select/option[3]";
        ContentReport.createdModifiedOrPublishedDate(webDriver, xPathToOption);


        // Enter a date into the date fields format = yyyy/mm/dd
        WebElement dateBox = webDriver.findElement(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/input[1]"));
        // Get a date to enter into the date field
        String date = ContentReport.getDateBasedOnEnvironment();
        dateBox.sendKeys(date);
        dateBox.sendKeys(Keys.RETURN);
        BrowserUtils.sleep(1000);

        // Click the search button again
        searchButton.click();
        BrowserUtils.sleep(500);

        String rows = "";
        if (Constants.baseURL.contains("publish-stage")){
            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[7]";
        } else {
            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[8]";
        }

        // Get array of all created dates and make sure they are all the same
        List<WebElement> publishDates = webDriver.findElements(By.xpath(rows));
        ArrayList<String> errors = new ArrayList<>();
        for(int i = 0; i < publishDates.size(); i++){
            if (!publishDates.get(i).getText().contains(ContentReport.getMasterPublished())){
                errors.add(publishDates.get(i).getText());
            }
        }

        Assert.isTrue(errors.size()==0, "These dates are different: " + errors.toString());
        BrowserUtils.sleep(5000);
    }

    @Test (timeOut = 180000)
    public void searchByApproved() throws InterruptedException, SQLException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        // Start a search for something you know has approved documents
        String search = ContentReport.getSearchCriteriaBasedOnEnvironment();
        WebElement searchButton = ContentReport.goToContentReportAndSearch(webDriver, search);

        // Click the approve box, and then the approve option
        ContentReport.clickIntoApprovedBox(webDriver);
        BrowserUtils.sleep(500);
        ContentReport.clickApprovedOption(webDriver);
        BrowserUtils.sleep(500);

        searchButton.click();
        BrowserUtils.sleep(5000);

        String rows;
        if (Constants.baseURL.contains("publish-stage")){
            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[10]/div/i[2]";
        } else {
            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[2]/div/i[2]";
        }

        // Get array of all approved rows and make sure they are all approved
        List<WebElement> approvedStatus = webDriver.findElements(By.xpath(rows));
        ArrayList<String> errors = new ArrayList<>();
        for(int i = 0; i < approvedStatus.size(); i++){
            if (!approvedStatus.get(i).getAttribute("ng-if").equals("row.approved")){
                errors.add(approvedStatus.get(i).getText());
            }
        }

        Assert.isTrue(errors.size()==0, "These documents are not approved: " + errors.toString());
        BrowserUtils.sleep(5000);

    }

//    @Test (timeOut = 180000)
//    public void searchByNotApproved() throws SQLException, InterruptedException {
//        webDriver = PreTestSetup.setup();
//        fono = new FonoSaia();
//
//        // Login to Content Central
//        ContentReport.loginToCC(webDriver, fono);
//        SetupTests.waitForLoading(webDriver);
//
//        // Start a search for something you know has approved documents
//        String search = ContentReport.getSearchCriteriaBasedOnEnvironment();
//        WebElement searchButton = ContentReport.goToContentReportAndSearch(webDriver, "/general-conference/2017/04");
//
//        // Click the approve box, and then the not approve option
//        ContentReport.clickIntoApprovedBox(webDriver);
//        BrowserUtils.sleep(500);
//        ContentReport.clickNotApprovedOption(webDriver);
//        BrowserUtils.sleep(500);
//
//        searchButton.click();
//        BrowserUtils.sleep(5000);
//
//        String rows;
//        if (Constants.baseURL.contains("publish-stage")){
//            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[10]/div/i";
//        } else {
//            rows = "/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr/td[2]/div/i";
//        }
//
//        // Get array of all approved rows and make sure they are all approved
//        List<WebElement> notApprovedStatus = webDriver.findElements(By.xpath(rows));
//        for (int i = 0; i < notApprovedStatus.size(); i++){
//            System.out.println(notApprovedStatus.get(i));
//        }
//        ArrayList<String> errors = new ArrayList<>();
//        for(int i = 0; i < notApprovedStatus.size(); i++){
//            if (!notApprovedStatus.get(i).getAttribute("ng-if").equals("row.approved")){
//                errors.add(notApprovedStatus.get(i).getText());
//            }
//        }
//    }

    @Test (timeOut = 180000)
    public void contentReportLoadSearch() throws InterruptedException, SQLException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        // Go to content Report page

        String xPathToReports = "//*[@id=\"reports-id\"]/a";
        String xPathToContentReport = "//*[@id=\"content-report-id\"]/a";
        ContentReport.clickReportsLinkThenContentReport(webDriver, xPathToReports, xPathToContentReport);
        BrowserUtils.sleep(1000);

        // Click the Load Search button
        WebElement clickLoadSearch = ContentReport.clickAWebElement(webDriver, "/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/div/button[2]");
        clickLoadSearch.click();
        BrowserUtils.sleep(500);
        // Click the first item in the load saved search modal
        WebElement savedSearch = ContentReport.clickAWebElement(webDriver, "//*[@id=\"saved-searches\"]/div[1]/button");
        savedSearch.click();
        BrowserUtils.sleep(5000);

        // Make sure results are loaded
        List<WebElement> notApprovedStatus = webDriver.findElements(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[3]/div/table/tbody/tr"));
        Assert.isTrue(notApprovedStatus.size() > 0, "No results returned from the search.");

    }

    @Test (timeOut = 180000)
    public void contentReportCreateSearch() throws SQLException, InterruptedException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        // Go to content Report page

        String xPathToReports = "//*[@id=\"reports-id\"]/a";
        String xPathToContentReport = "//*[@id=\"content-report-id\"]/a";
        ContentReport.clickReportsLinkThenContentReport(webDriver, xPathToReports, xPathToContentReport);
        BrowserUtils.sleep(1000);

        // Click the create search button
        ContentReport.clickCreateSearch(webDriver);
        // Send keys for the search box
        WebElement createSearchBox = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div/edit-search-modal/div[2]/div[1]/div/input")));
        createSearchBox.sendKeys("/general-conference/2017/04");
        createSearchBox.sendKeys(Keys.RETURN);
        BrowserUtils.sleep(5000);
        // Click Save Search
        WebElement saveSearch = ContentReport.clickAWebElement(webDriver,"/html/body/div[1]/div/div/edit-search-modal/div[3]/button[3]");
        saveSearch.click();
        BrowserUtils.sleep(1000);
        // Send keys to search name box and click save
        WebElement savedSearchName = (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"savedSearchName\"]")));
        savedSearchName.sendKeys("generalConference201704");
        BrowserUtils.sleep(5000);
        WebElement saveSearchNameModalBtn = ContentReport.clickAWebElement(webDriver, "/html/body/div[1]/div/div/save-search-modal/div[3]/button[2]");
        saveSearchNameModalBtn.click();
        BrowserUtils.sleep(5000);

        // Click Load Search button and see if the search was added
        WebElement clickLoadSearch = ContentReport.clickAWebElement(webDriver, "/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/div/button[4]");
        clickLoadSearch.click();
        BrowserUtils.sleep(5000);
        List<WebElement> listOfSavedSearches = webDriver.findElements(By.xpath("//*[@id=\"saved-searches\"]/div/button"));
        String checkForCreatedSearch = "";
        for (int i = 0; i < listOfSavedSearches.size(); i++){
            if (listOfSavedSearches.get(i).getText().equals("generalConference201704")){
                checkForCreatedSearch = listOfSavedSearches.get(i).getText();
            }
        }

        Assert.isTrue(checkForCreatedSearch.equals("generalConference201704"), "There were no results returned from the search.");

        // Delete the saved search that was just created so the test can be run again
        String savedSearchId = "";
        ResultSet resultSet = JDBCUtils.getResultSet("select saved_search_id from saved_search where app_user_id = "
                + "(select app_user_id from app_user where preferred_name like 'Fono Saia%' and lds_account_id > 50000)"
                + "AND search_name = 'generalConference201704'");
        if(resultSet.next()){
            savedSearchId = resultSet.getString("saved_search_id");
        }
        ResultSet deleteFromSavedSearchFolder = JDBCUtils.getResultSet("delete from saved_search_folder where saved_search_id = '" + savedSearchId + "'");
        if (deleteFromSavedSearchFolder.next()) {
            ResultSet rs = JDBCUtils.getResultSet("DELETE FROM saved_search where saved_search_id = '" + savedSearchId + "'");
            if (rs.next()) {
                System.out.println("Successfully deleted the saved search.");
            }
        }


    }

    @Test(timeOut = 180000)
    public void contentReportClearSearch() throws InterruptedException, SQLException {
        webDriver = PreTestSetup.setup();
        fono = new FonoSaia();

        // Login to Content Central
        ContentReport.loginToCC(webDriver, fono);
        SetupTests.waitForLoading(webDriver);

        // Run a search
        String search = ContentReport.getSearchCriteriaBasedOnEnvironment();
        ContentReport.goToContentReportAndSearch(webDriver, search);
        BrowserUtils.sleep(5000);

        // After search has loaded, click the clear search button
        WebElement clearSearch = ContentReport.clickAWebElement(webDriver, "/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/div/button[2]");
        clearSearch.click();
        BrowserUtils.sleep(1000);

        // After clearSearch has been click, check and make sure everything has been cleared
        String searchInput = webDriver.findElement(By.xpath("//*[@id=\"searchBar\"]")).getText();
        String langBox = webDriver.findElement(By.xpath("//*[@id=\"languages_chosen\"]/ul/li/input")).getText();
        String dateBox1 = webDriver.findElement(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/input[1]")).getText();
        String dateBox2 = webDriver.findElement(By.xpath("/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[2]/div[5]/div/input[2]")).getText();
        Assert.isTrue(searchInput.equals("") && langBox.equals("") && dateBox1.equals("") && dateBox2.equals(""),
                "Search Input Box: " + searchInput + "\n"
                + "Language Box: " + langBox + "\n"
                + "Date Box 1: " + dateBox1 + "\n"
                + "Date Box 2: " + dateBox2
        );

    }



}
