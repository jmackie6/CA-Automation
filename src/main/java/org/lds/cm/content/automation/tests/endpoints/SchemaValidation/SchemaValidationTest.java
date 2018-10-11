package org.lds.cm.content.automation.tests.endpoints.SchemaValidation;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
import org.lds.cm.content.automation.service.QATransformService;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.QAFileUtils;
import org.lds.cm.content.automation.util.QATransformationResult;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class SchemaValidationTest {

	
	@Test (groups = "validation")
	public static void schemaValidTest() throws IOException, ParseException, SQLException, Exception {

		String filePath = Constants.transformFileStartDir + "/SchemaValidationHTML/SchemaValidationValid.html";
		final Map<String, Integer> expectedErrors = new HashMap<>();
		ErrorUtils.testFilesWithMaps(filePath, expectedErrors);

	}

	@Test
	public static void schemaFatalTest() throws Exception {

		String fileName = Constants.transformFileStartDir + "/SchemaValidationHTML/Schema_Html5_Fatal.html";
		final Map<String, Integer> expectedErrors = new HashMap<>();
		expectedErrors.put("SCHEMA_HTML5-FATAL", 14);
		expectedErrors.put("MEDIA-FATAL", 4);
		ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
		ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
	}

	@Test (enabled=false, groups = "validation")//this is the file we got as a test file, but getting different results from the validation and the transform.
	public static void schemaMultiTest() throws Exception{
		String fileName = Constants.transformFileStartDir + "/SchemaValidationHTML/schema-test-file.html";
		final Map<String, Integer> expectedErrors = new HashMap<>();

		expectedErrors.put("SCHEMA_HTML5-FATAL", 14);
		expectedErrors.put( "MEDIA-FATAL", 4 );
		expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1 );
		expectedErrors.put("LINK-INFO", 2 );
		expectedErrors.put("CROSS_REF-WARNING", 11 );

		ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
		ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
	}


	@Test(enabled=false)//no db records
	public static void schemaMediaFatalTest() throws Exception{
		String fileName = Constants.transformFileStartDir + "/SchemaValidationHTML/SchemaMediaFatal.html";
		final Map<String, Integer> expectedErrors = new HashMap<>();
		expectedErrors.put("SCHEMA_HTML5-FATAL", 2);
		expectedErrors.put("MEDIA-FATAL", 1);

		ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
		ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
	}

	@AfterClass(alwaysRun=true)
	public void closeUp() { JDBCUtils.closeUp(); }

	
}
