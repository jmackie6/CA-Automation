package org.lds.cm.content.automation.tests.endpoints.TranslationApiController;

import com.google.common.base.Function;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.lds.cm.content.automation.enums.DocumentSource;
import org.lds.cm.content.automation.service.QADocumentService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class TranslationUploadTest {

    private int translationRequestId = 111970;
    private String languageCode = "300";
    private int fileId = 6023644;
    private String file = "6023644";
    private String fileName = "6023644.xml";
    private String uri = "/general-conference/2017/10/44uchtdorf";


    private String email = "QAContentCentral@gmail.com";

    @BeforeMethod (alwaysRun = true)
    public void fileExists() throws IOException {checkFileValidity(file, uri);}

    @Test
    public void translateDocAllParams() throws IOException, InterruptedException {
        allParamsIncluded(translationRequestId, languageCode, fileId, fileName, "", null, email);
        clearEmailData();
    }

    @Test
    public void translateEmptyFileIdVal() throws IOException {
        emptyValue(translationRequestId, languageCode, "", fileName, "", null, email);
    }

    @Test void translateEmptyFileNameVal() throws IOException {
        emptyValue(translationRequestId, languageCode, file, "", "", null, email);
    }

    @Test
    public void translateEmptyEmailVal() throws IOException {
        emptyValue(translationRequestId, languageCode, file, fileName, "", null, "");
    }

    @Test
    public void translateDocNoEmail() throws IOException, InterruptedException {
        missingEmail(translationRequestId, languageCode, fileId, fileName, "", null);
    }

    @Test
    public void translateDocNoFileName() throws IOException, InterruptedException {
        missingFileName(translationRequestId, languageCode, fileId, "", null, email);
        clearEmailData();
    }

    @Test
    public void translateDocNoFileId() throws IOException {
        missingFileId(translationRequestId, languageCode,fileName, "", null, email);
    }

    /**
     * Calls the translation endpoint and checks the database for the file and verifies that the source is correct
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSource() throws IOException, SQLException {
        System.out.println("Calling endpoint");
        callEndpoint(translationRequestId, languageCode, fileId, fileName, "", null, email);

        System.out.println("Getting fileId for " + file);
        String fileId = getFileId(file);

        QADocumentService.verifySourceByFileId(fileId, DocumentSource.TRANSLATION, false);
    }

    private void checkFileValidity(String file, String uri) throws IOException {
        // check response status to see if document exists
        int response_status = NetUtils.getResponseStatus(Constants.epTranslatedFile + file);
        Assert.assertFalse(response_status == 204, "\nReceived a response status of 204. No content was found for the given file.\n");

        // if the document exists check the uri to make sure it's the correct document
        String world_server_output = NetUtils.getHTML(Constants.epTranslatedFile + file);
        Assert.assertTrue(world_server_output.contains(uri), "\nThe document returned was either empty or the incorrect document.\n");
    }

    private String getFileId(String file) throws IOException {
        //Get HTML and make sure that it is correct
        String world_server_output = NetUtils.getHTML(Constants.epTranslatedFile + file);
        Assert.assertTrue(world_server_output.contains(uri), "\nThe document returned was either empty or the incorrect document.\n");

        //get the start and end index of fileId
        int startIndex = world_server_output.indexOf("<lds:title type=\"file\">");
        String substring = world_server_output.substring(startIndex);
        startIndex = substring.indexOf("<lds:title type=\"file\">");
        int endIndex = substring.indexOf("</lds:title>");

        //extract and return fileId
        String retval = substring.substring(startIndex + "<lds:title type=\"file\">".length(), endIndex);
        return retval;
    }

    private void allParamsIncluded(int translationRequestId, String language, int fileId, String fileName, String sourceDocumentId, String notificationUrl, String notificationEmails) throws IOException, InterruptedException {
        String json = "{\"translationRequestId\": " + translationRequestId + ", \"language\": \"" + language + "\", \"fileId\": " + fileId + ", \"fileName\": \"" + fileName + "\", \"sourceDocumentId\": \"" + sourceDocumentId + "\", \"notificationUrl\": " + notificationUrl + ", \"notificationEmails\": [\"" + notificationEmails + "\"]}";
        String[] response = {translateDocument(json)[0], translateDocument(json)[1]};
        Assert.assertTrue(response[1].contains("200 OK") && response[0].equals("Upload Request received"), "Expected a response code of 200 and message of \"Upload Request received\". Instead received the following: \n Response Code: " + response[1] + "\n Message: " + response[0] + "\n");
        String email_content = checkEmailContent();
        Assert.assertTrue(email_content.contains("Success!"), "Translation was not successful according to the email sent. Email did not contain \"Success!\".");
    }

    /**
     * Same as allParamsIncluded but it doesnt check the email confirmation
     * @param translationRequestId
     * @param language
     * @param fileId
     * @param fileName
     * @param sourceDocumentId
     * @param notificationUrl
     * @param notificationEmails
     */
    private void callEndpoint(int translationRequestId, String language, int fileId, String fileName,
                              String sourceDocumentId, String notificationUrl, String notificationEmails) throws IOException {
        String json = "{\"translationRequestId\": " + translationRequestId + ", \"language\": \"" +
                language + "\", \"fileId\": " + fileId + ", \"fileName\": \"" +
                fileName + "\", \"sourceDocumentId\": \"" + sourceDocumentId +
                "\", \"notificationUrl\": " + notificationUrl + ", \"notificationEmails\": [\"" +
                notificationEmails + "\"]}";
        String[] response = {translateDocument(json)[0], translateDocument(json)[1]};
        Assert.assertTrue(response[1].contains("200 OK") && response[0].equals("Upload Request received"),
                "Expected a response code of 200 and message of \"Upload Request received\". " +
                        "Instead received the following: \n Response Code: " +
                        response[1] + "\n Message: " + response[0] + "\n");
    }


    // these functions handles empty values for the more majors parameters (File ID, File Name, Email)

    private void emptyValue(int translationRequestId, String language, String fileId, String fileName, String sourceDocumentId, String notificationUrl, String notificationEmails) throws IOException {
        //String json = "{\"translationRequestId\": " + translationRequestId + ", \"language\": \"" + language + "\", ";
        StringBuilder json = new StringBuilder();
        json.append("{\"translationRequestId\": " + translationRequestId + ", \"language\": \"" + language + "\", ");

        // check if values are missing, and build json according to that
        if (!fileId.equals("")) {
            json.append("\"fileId\": " + fileId + ", ");
        } else {
            json.append("\"fileId\": , ");
        }

        if (!fileName.equals("")) {
            json.append("\"fileName\": \"" + fileName + "\", ");
        } else {
            json.append("\"fileName\": , ");
        }


        json.append("\"sourceDocumentId\": " + sourceDocumentId + ", \"notificationUrl\": " + notificationUrl + ", ");

        if (!notificationEmails.equals("")) {
            json.append("\"notificationEmails\": [\"" + notificationEmails + "\"]}");
        } else {
            json.append("\"notificationEmails\": }");
        }

        String[] response = {translateDocument(json.toString())[0], translateDocument(json.toString())[1]};
        Assert.assertTrue(response[1].contains("400 Bad Request"), "Expected a response code of 404 Bad Request. Instead received the following: \n Response Code: " + response[1] + "\n Message: " + response[0] + "\n");
        System.out.println("\nReceived expected behavior when translating with missing value for File ID, File Name, or Notification Email.\n");
    }

    private void missingEmail(int translationRequestId, String language, int fileId, String fileName, String sourceDocumentId, String notificationUrl) throws IOException, InterruptedException {
        String json = "{\"translationRequestId\": " + translationRequestId + ", \"language\": \"" + language + "\", \"fileId\": " + fileId + ", \"fileName\": \"" + fileName + "\", \"sourceDocumentId\": \"" + sourceDocumentId + "\", \"notificationUrl\": " + notificationUrl + "}";
        String response_code = translateDocument(json)[1];
        Assert.assertTrue(response_code.contains("200 OK"), "\nExpected a response code of 200 OK. Instead received the following: \nResponse Code: " + response_code + "\n");
        System.out.println("\nReceived expected behavior when translating without an email.\n");
        Assert.assertTrue(!checkEmailContent().contains("Success!"), "An email was found, even though it should not have been sent (no email was provided).");
    }

    private void missingFileName(int translationRequestId, String language, int fileId, String sourceDocumentId, String notificationUrl, String notificationEmails) throws IOException, InterruptedException {
        String json = "{\"translationRequestId\": " + translationRequestId + ", \"language\": \"" + language + "\", \"fileId\": " + fileId + ", \"sourceDocumentId\": \"" + sourceDocumentId + "\", \"notificationUrl\": " + notificationUrl + ", \"notificationEmails\": [\"" + notificationEmails + "\"]}";
        String[] response = {translateDocument(json)[0], translateDocument(json)[1]};
        Assert.assertTrue(response[1].contains("200 OK") && response[0].equals("Upload Request received"), "Expected a response code of 200 and message of \"Upload Request received\". Instead received the following: \n Response Code: " + response[1] + "\n Message: " + response[0] + "\n");
        String email_content = checkEmailContent();
        Assert.assertTrue(email_content.contains("Success!"), "\nResult of translated file upload request did not result in success. This is incorrect.\n");
        System.out.println("\nReceived expected behavior when translating without File Name.\n");
    }

    private void missingFileId(int translationRequestId, String language, String fileName, String sourceDocumentId, String notificationUrl, String notificationEmails) throws IOException {
        String json = "{\"translationRequestId\": " + translationRequestId + ", \"language\": \"" + language + "\", \"fileName\": \"" + fileName + "\", \"sourceDocumentId\": \"" + sourceDocumentId + "\", \"notificationUrl\": " + notificationUrl + ", \"notificationEmails\": [\"" + notificationEmails + "\"]}";
        String[] response = {translateDocument(json)[0], translateDocument(json)[1]};
        Assert.assertTrue(response[0].equals("Invalid Upload Request submitted") && response[1].contains("200 OK"), "\nExpected a response code of 200 and message of \"Invalid Upload Request submitted\". Instead received the following: \n Response Code: " + response[1] + "\n Message: " + response[0] + "\n");
        System.out.println("\nReceived expected behavior when translating without File ID.\n");
    }

    private String[] translateDocument(String json) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(Constants.epTranslationUpload);
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        String[] test = {EntityUtils.toString(response.getEntity()), String.valueOf(response.getStatusLine())};
        return test;
    }

    private String checkEmailContent() throws InterruptedException {
        // setup, replace with your chromdriver path
        System.setProperty("webdriver.chrome.driver", Constants.driverLocation + "/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        driver.get("https://accounts.google.com/ServiceLogin?");

        // gmail login
        driver.findElement(By.id("identifierId")).sendKeys("QAContentCentral@gmail.com", Keys.RETURN);
        driver.findElement(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input")).sendKeys("QAContentAutomation", Keys.RETURN);

        // get to inbox
        driver.findElement(By.id("gbwa")).click();
        driver.findElement(By.xpath("//*[@id=\"gb23\"]/span[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"aso_search_form_anchor\"]/div/input")).sendKeys("content-central-notification@ldschurch.org", Keys.RETURN);
        Thread.sleep(2000);

        // click email
        driver.findElement(By.xpath("//*[@id=\":2\"]/div")).click();

        // store email content
        String email_content = driver.findElement(By.xpath("//*[@id=\":2\"]/div")).getText();

        driver.close();

        return email_content;
    }

    private void clearEmailData() throws InterruptedException {
        // setup, replace with your chromdriver path
        System.setProperty("webdriver.chrome.driver", Constants.driverLocation + "/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        driver.get("https://accounts.google.com/ServiceLogin?");

        driver.findElement(By.id("identifierId")).sendKeys("QAContentCentral@gmail.com", Keys.RETURN);
        driver.findElement(By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input")).sendKeys("QAContentAutomation", Keys.RETURN);

        // get to inbox
        driver.findElement(By.id("gbwa")).click();
        driver.findElement(By.xpath("//*[@id=\"gb23\"]/span[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"aso_search_form_anchor\"]/div/input")).sendKeys("content-central-notification@ldschurch.org", Keys.RETURN);
        Thread.sleep(2000);

        // delete emails
        driver.findElement(By.xpath("//*[@id=\":lh\"]")).click();

        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement trash = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.xpath("//*[@id=\":5\"]/div[2]/div[1]/div[1]/div/div/div[2]/div[3]/div"));
            }});

        Actions actions = new Actions(driver);
        actions.moveToElement(trash).click().perform();
        Thread.sleep(2000);

        // sign out
        driver.findElement(By.xpath("//*[@id=\"gb\"]/div[2]/div[3]/div/div[2]/div/a/span")).click();
        driver.findElement(By.xpath("//*[@id=\"gb_71\"]")).click();
        driver.close();
    }
}
