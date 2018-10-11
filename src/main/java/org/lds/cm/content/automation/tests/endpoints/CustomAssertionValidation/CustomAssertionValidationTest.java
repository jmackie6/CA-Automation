package org.lds.cm.content.automation.tests.endpoints.CustomAssertionValidation;

import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
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

public class CustomAssertionValidationTest {

    @Test(enabled=false) //difference between db and expected
    public static void customAssertionArticleTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/ArticleCustomErrorTest.html";
//        String fileName = Constants.transformFileStartDir + "/SchemaValidationHTML/SchemaMediaFatal.html";
//        ErrorUtils.testFiles(fileName,  ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 0);
//        ErrorUtils.validateFiles(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.FATAL, 3);
        final Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 2);
        expectedErrors.put("CUSTOM_ASSERTION-INFO", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionManifestCoverArtTest() throws IOException, ParseException, SQLException, ParserConfigurationException, SAXException, XPathExpressionException {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/CoverArtManifestTest.html";

        final Map<String, Integer> expectedErrorMapping = new HashMap<>();
        expectedErrorMapping.put("CUSTOM_ASSERTION-FATAL", 1);
        expectedErrorMapping.put("MEDIA-FATAL", 1);

        ErrorUtils.testFilesWithMaps(fileName, expectedErrorMapping);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrorMapping);
    }

    @Test(enabled=true)
    public static void customDocumentH1Test() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/DocumentH1Test.html";

        final Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);

        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionDoubleQuoteTest()throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/DocumentDoubleQuoteTest.html";

        final Map<String, Integer> expectedErrorMapping = new HashMap<>();
        expectedErrorMapping.put("CUSTOM_ASSERTION-WARNING", 2);
        expectedErrorMapping.put("SCHEMA_HTML5-FATAL", 1);
        expectedErrorMapping.put("MEDIA-FATAL", 1);

        ErrorUtils.testFilesWithMaps(fileName, expectedErrorMapping);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrorMapping);


    }

    @Test(enabled=true) //works in stage - may not work in test - currently down
    public static void customAssertionEmptyElementsTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/EmptyElementTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.FATAL, 11);
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 3);
        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("SCHEMA_HTML5-FATAL", 13);
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 8);
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 3);
        expectedErrors.put("MEDIA-FATAL", 2);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=false)//discrepency between db and expected errors
    public static void customAssertionFigureTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/FigureTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.FATAL, 3);
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 2);
        Map<String, Integer> expectedErrors = new HashMap<>();
//        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 3);
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 2);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 5);
        expectedErrors.put("MEDIA-FATAL", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionFootnoteTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/FootnoteTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.FATAL, 2);
        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 2);
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 5);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true) ///TODO:No errors returned, need to double-check the HTML
    public static void customAssertionArticleItemTypeTest() throws IOException, SQLException, ParserConfigurationException, SAXException, ParseException, XPathExpressionException{
//        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/ArticleCustomErrorTest.html";
        String fileName = "./src/main/resources/TransformTestFiles/CustomAssertionErrorValidation/ArticleCustomErrorTest.html";
//        ErrorUtils.testFiles(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFiles(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionSectionTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/SectionTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 4);
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 4);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 4);
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 2);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)//no db records - works in stage, not test
    public static void customAssertionHeaderTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/HeaderTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.FATAL, 4);

        Map<String, Integer> expectedErrors = new HashMap<>();
//        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 4);
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 2);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionArticleAuthorDataTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/ArticleAuthorDataError.html";

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled = true)
    public static void customAssertionBroadcastAuthorDataTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/BroadcastAuthorDataError.html";
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionSingleQuoteTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/SingleQuoteError.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.FATAL, 1);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 1);
        expectedErrors.put("MEDIA-FATAL", 1);
        expectedErrors.put("CROSS_REF-WARNING", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=false)///TODO:Understand why there is a different set of errors between transform and validation
    public static void customAssertionInlineImageTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/InlineImageTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 4);

        final Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 5);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 4);
        expectedErrors.put("MEDIA-FATAL", 3);
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName,expectedErrors);
    }

    @Test///TODO:Diff between validtaion and transform errors
    public static void customAssertionVideoAttributionTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/VideoAttributionTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.FATAL, 1);
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);

        final Map<String, Integer> expectedErrors = new HashMap<>();
//        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 1);
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 1);
        expectedErrors.put("MEDIA-FATAL", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 3);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=false)//no db records
    public static void customAssertionSubordinateTest() throws IOException, SQLException, ParserConfigurationException, SAXException, XPathExpressionException {

        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/SubordinateTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.FATAL, 1);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 3);
        expectedErrors.put("MEDIA-FATAL", 1);
//        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 1);
        expectedErrors.put("CROSS_REF-WARNING", 1);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }



    @Test(enabled=true)
    public static void customAssertionGeneralConferenceAuthorDataTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/GeneralConferenceAuthorDataTest.html";
        //ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionArticleBylineTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/AuthorMetaBylineTest.html";
        //ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 3);

        Map<String, Integer> expectedErrors = new HashMap<>();
//        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 3);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 3);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionBroadcastBylineTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/BroadcastMetaBylineTest.html";

        //ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);

        Map<String, Integer> expectedErrors = new HashMap<>();
//        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 2);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionGeneralConferenceBylineTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/GeneralConferenceBylineTest.html";
        //ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);

        Map<String, Integer> expectedErrors = new HashMap<>();
//        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 2);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionGeneralConferenceManifestItemTypeTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/GC_ManifestItemTypeTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 1);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionChapterTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/ChapterTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 2);
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 2);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 4);
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 2);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionBookTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/BookTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 2);
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 2);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 4);
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 2);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionMagazineTest() throws Exception {
        String fileName = Constants.transformFileStartDir + "/CustomAssertionErrorValidation/MagazineTest.html";
//        ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 2);
//        ErrorUtils.validateFilesFilteredSoftAssert(fileName, ErrorTypes.CUSTOM_ASSERTION, SeverityTypes.WARNING, 2);

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
        expectedErrors.put("SCHEMA_HTML5-FATAL", 4);
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 2);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
    }

    @Test(enabled=true)
    public static void customAssertionMediaServiceStageTest() throws Exception{
        String fileName = Constants.transformFileStartDir + "/engVideoHTML/church-service-missionaries/2012-02-03-im-a-familysearch-missionary-sister-horning.html";

        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("CUSTOM_ASSERTION-FATAL", 1);
        ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
        ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);

    }

    @AfterClass(alwaysRun=true)
    public void closeUp() { JDBCUtils.closeUp(); }
}

