package org.lds.cm.content.automation.tests.endpoints.LinkValidation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class LinkValidationTests {
	
	
	@Test
	public static void whiteListValidationSuccess() throws Exception {

		String filePath = Constants.transformFileStartDir + "/LinkValidation/WhiteListTest.html";

//		ErrorUtils.testFiles(filePath, null, null, 0 );
		final Map<String, Integer> expectedError = new HashMap<>();
		ErrorUtils.testFilesWithMaps(filePath, expectedError);
		ErrorUtils.validateFilesWithMaps(filePath, expectedError);
	}


	@Test
	public static void whiteListValidationInfoError() throws Exception {

		String xmlString = Constants.transformFileStartDir + "/LinkValidation/WhiteListInfoFailure.html";

//		ErrorUtils.testFiles(xmlString, ErrorTypes.LINK, SeverityTypes.INFO, 3 );

		final Map<String, Integer> expectedError = new HashMap<>();
		expectedError.put("LINK-INFO", 3);
		ErrorUtils.testFilesWithMaps(xmlString, expectedError);
		ErrorUtils.validateFilesWithMaps(xmlString, expectedError);
	}


	@Test
	public static void whiteListValidationFailure() throws Exception{

		String xmlString = Constants.transformFileStartDir + "/LinkValidation/WhiteListFailureTest.html";

//		ErrorUtils.testFiles(xmlString, ErrorTypes.LINK, SeverityTypes.ERROR, 3 );
		final Map<String, Integer> expectedError = new HashMap<>();
		expectedError.put("LINK-ERROR", 3);
		ErrorUtils.testFilesWithMaps(xmlString, expectedError);
		ErrorUtils.validateFilesWithMaps(xmlString, expectedError);
	}

	@AfterClass(alwaysRun=true)
	public void closeUp() { JDBCUtils.closeUp(); }

}
