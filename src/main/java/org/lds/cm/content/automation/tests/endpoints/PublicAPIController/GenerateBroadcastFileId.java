package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class GenerateBroadcastFileId {

    private static String apiRuleName = "ws/v1/generateBroadcastFileId";

    private static String PD_GOOD = "PD12345678";
    private static String PD_TOO_LONG = "PD123456789";
    private static String PD_TOO_SHORT = "PD1234567";
    private static String PD_WITH_CHAR = "PD1234567A";
    private static String PD_TOO_LONG_WITH_CHAR = "PD12345678A";
    private static String PD_TOO_SHORT_WITH_CHAR = "PD123456A";

    private static String LANG_1 = "0";
    private static String LANG_2 = "00";
    private static String LANG_3 = "000";
    private static String LANG_TOO_LONG = "0000";
    private static String LANG_CHAR = "A";
    private static String LANG_TOO_LONG_WITH_CHAR = "A000";

    private static String fileName = "11Monson";

    @BeforeClass(alwaysRun = true)
    public void setUp() throws SQLException, InterruptedException {
        APIRules.fixAPIRule(apiRuleName);
    }

    @Test (groups = ("endpoints"))
    public void checkFileId () throws IOException {
        String url = Constants.baseURL + "/" + apiRuleName + "?pdNumber=" + PD_GOOD + "&ldsLang=" + LANG_3 + "&fileName=" + fileName;
        String[] response = generateFileId(url);
        Assert.assertTrue(response[0].contentEquals("200"), response[0] + "error code received from " + url);
        Assert.assertTrue(response[1].startsWith("<fileId>") && response[1].endsWith("</fileId>"), "Invalid response: " + response[1]);
        Assert.assertTrue(verifyFileId(response[1],PD_GOOD, LANG_3, fileName), getMismatchResponse(response[1], PD_GOOD, LANG_3, fileName));
    }

    private String[] generateFileId(String url) throws IOException {
        CloseableHttpResponse closeableHttpResponse = HttpClients.createDefault().execute(new HttpGet(url));

        HttpEntity httpEntity = closeableHttpResponse.getEntity();

        String response = EntityUtils.toString(httpEntity);

        String statusCode = Integer.toString(closeableHttpResponse.getStatusLine().getStatusCode());

        String[] retVal = new String[2];

        retVal[0] = statusCode;
        retVal[1] = response;

        return retVal;
    }

    private boolean verifyFileId(String response, String pdNumber, String ldsLang, String fileName) {
        boolean valid = true;

        String fileId = response.replace("<fileId>", "");
        fileId = fileId.replace("</fileId>", "");
        fileName = convertToUriFormat(fileName);
        pdNumber = pdNumber.toUpperCase();
        ldsLang = convertLdsLangCodeEx(ldsLang);
        if(!fileId.equals(pdNumber + "_" + ldsLang + "_" + fileName)) {
            valid = false;
        }

        return valid;
    }

    private String getMismatchResponse(String response, String pdNumber, String ldsLang, String fileName) {
        return "Response \"" + response + "\" not properly formatted for pdNumber \"" + pdNumber + "\" ldsLang: \"" + ldsLang + "\" fileName: \"" + fileName;
    }

    private static String convertLdsLangCodeEx(String lang) {
        DecimalFormat df = new DecimalFormat("000");
        String reg = "\\d+";
        if(!lang.matches(reg) || lang.length() > 3) {
            return "XXX";
        } else {
            int langInt = Integer.parseInt(lang);
            return df.format(langInt);
        }
    }

    private static String convertToUriFormat(String input){
        //set trim leading and trailing whitespace
        input = input.trim();
        //set input to lower-case
        input = input.toLowerCase();
        //replace multi-space sequences with single space
        input = input.replaceAll("\\s{2,}","\\x20");
        //replace whitespace with hyphen
        input = input.replaceAll("\\s","-");
        //replace escaped whitespace sequences with hyphen
        input = input.replaceAll("%20","-");
        //replace dash sequences with hyphen
        input = input.replaceAll("[–—]","-");
        //replace ampersand representations
        input = input.replaceAll("%26","-and-");
        input = input.replaceAll("&amp;","-and-");
        input = input.replaceAll("&","-and-");
        //delete all all punctuation and other extraneous characters and character sequences that might be used in an event title
        input = input.replaceAll("[®™%″′()…&_=!?;:.,“”‘‘’'\"]+","");
        input = input.replaceAll("[-]{2,}","-");
        //delete all accented characters
        input = StringUtils.stripAccents(input);
        return input;
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup()	{	JDBCUtils.closeUp();	}
}
