package org.lds.cm.content.automation.tests.endpoints.MediaValidation;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class MediaValidationTests {


	@Test
	public static void mediaValidTest() throws Exception{
		String fileName = Constants.transformFileStartDir + "/MediaValidation/MediaValid.html";
//		ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.MEDIA, SeverityTypes.FATAL, 0);
		final Map<String, Integer> expectedErrors = new HashMap<>();
		ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
		ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
	}

   @Test(enabled=true)
	public static void mediaFatalTest() throws Exception{
		String fileName = Constants.transformFileStartDir + "/MediaValidation/MediaFatal.html";
//		ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.MEDIA, SeverityTypes.FATAL, 1);
	    Map<String, Integer> expectedErrors = new HashMap<>();
	    expectedErrors.put("MEDIA-FATAL", 1);
	    expectedErrors.put("CUSTOM_ASSERTION-WARNING", 1);
		ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
		ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
	}

   @Test(enabled=false) ///TODO:modify HTML so that schema warnings return
   public static void schemaMediaWarningTest() throws Exception {
   		String fileName = Constants.transformFileStartDir + "/MediaValidation/MediaWarning.html";
   		final Map<String, Integer> expectedErrors = new HashMap<>();
   		expectedErrors.put("MEDIA-WARNING", 3);
   		ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
   		ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
//   		ErrorUtils.testFiles(fileName, ErrorTypes.MEDIA, SeverityTypes.WARNING, 0);
//   		ErrorUtils.validateFiles(fileName, ErrorTypes.MEDIA, SeverityTypes.WARNING, 3);
   }

   @Test(enabled=false)//no records found in db
   public static void schemaMediaFatalTest() throws Exception {
   		String fileName = Constants.transformFileStartDir + "/MediaValidation/SchemaMediaFatal.html";
   		//ErrorUtils.testFiles(fileName, ErrorTypes.MEDIA, SeverityTypes.FATAL, 1);
//   		ErrorUtils.validateFilesFiltered(fileName, ErrorTypes.MEDIA, SeverityTypes.FATAL, 1);
	   final Map<String, Integer> expectedErrors = new HashMap<>();
	   expectedErrors.put("MEDIA-FATAL", 1);
	   expectedErrors.put("SCHEMA_HTML5-FATAL", 2);
	   ErrorUtils.testFilesWithMaps(fileName, expectedErrors);
	   ErrorUtils.validateFilesWithMaps(fileName, expectedErrors);
   }

	@AfterClass(alwaysRun=true)
	public void closeUp() { JDBCUtils.closeUp(); }
}
