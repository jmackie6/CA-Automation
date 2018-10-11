package org.lds.cm.content.automation.tests.endpoints.ScriptureRefValidation;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.enums.ErrorTypes;
import org.lds.cm.content.automation.enums.SeverityTypes;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ScriptureRefValidationTests {

    @Test  (groups = "validation")
    public void scriptureRefValidationSuccess() throws Exception {
        String filePath = Constants.transformFileStartDir + "/ScriptureReferenceValidation/ScriptureRefValidationSuccessTest.html";
        Map<String, Integer> expectedErrors = new HashMap<>();
        ErrorUtils.testFilesWithMaps(filePath, expectedErrors);
        ErrorUtils.validateFilesWithMaps(filePath, expectedErrors);
    }


    @Test (groups = "validation")
    public void scriptureRefValidationNonExist() throws Exception{

        String filePath = Constants.transformFileStartDir + "/ScriptureReferenceValidation/ScriptureRefValidationFailHref.html";
        Map<String, Integer> expectedErrors = new HashMap<>();
        expectedErrors.put("SCRIPTURE_REF-FATAL", 3);
        ErrorUtils.testFilesWithMaps(filePath, expectedErrors);
        ErrorUtils.validateFilesWithMaps(filePath, expectedErrors);
    }

    @AfterClass(alwaysRun=true)
    public void closeUp() { JDBCUtils.closeUp(); }


}