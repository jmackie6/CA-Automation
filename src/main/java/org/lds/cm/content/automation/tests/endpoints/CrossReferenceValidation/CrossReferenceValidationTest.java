package org.lds.cm.content.automation.tests.endpoints.CrossReferenceValidation;

import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
import org.lds.cm.content.automation.service.QADeleteService;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CrossReferenceValidationTest {

    @AfterClass (alwaysRun = true)
    public static void delete_docs() throws SQLException {
        QADeleteService.deleteFromDbByFileIdSingleDocument("CROSSREFVALIDFATAL_000_000");
        QADeleteService.deleteFromDbByFileIdSingleDocument("CROSSREFVALID_000_000");
        JDBCUtils.closeUp();
    }

    @Test
    public static void crossRefValidTest() throws IOException, ParseException, SQLException, XPathExpressionException, ParserConfigurationException, SAXException {
        String filePath = Constants.transformFileStartDir + "/CrossReferenceValidation/CrossRefValidTest.html";
        final Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CROSS_REF-WARNING", 1);
        expectedErrors.put("CUSTOM_ASSERTION-INFO", 1);
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 3);
        ErrorUtils.testFilesWithMaps(filePath, expectedErrors);
        ErrorUtils.validateFilesWithMaps(filePath, expectedErrors);
    }


    @Test
    public static void crossRefActiveTest() throws IOException, ParseException, SQLException, XPathExpressionException, ParserConfigurationException, SAXException {
        String filePath = Constants.transformFileStartDir + "/CrossReferenceValidation/CrossRefFatalMedia.html";
        final Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CROSS_REF-WARNING", 2);
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 3);
        expectedErrors.put("CUSTOM_ASSERTION-INFO", 1);
        ErrorUtils.testFilesWithMaps(filePath, expectedErrors);
        ErrorUtils.validateFilesWithMaps(filePath, expectedErrors);
    }

}
