package org.lds.cm.content.automation.tests.endpoints.LanguageAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetXMLByCode {

    @Test(groups= { "endpoints" })
    public static void getByCode() throws IOException, SQLException {

        // get rs w/ query to get random dialect every time
        String sql = "SELECT LANGUAGE_CODE FROM (SELECT * FROM LANGUAGE WHERE LANGUAGE.COUNT_AS_APPROVED_Y_N_FLAG = 'Y'"
                      + " ORDER BY dbms_random.value) where rownum = 1";
        ResultSet rs = JDBCUtils.getResultSet(sql);

        rs.next();
        String languageCode = rs.getString("LANGUAGE_CODE");
        rs.close();

        // get data from endpoint, pass in language code from above
        String byCode = NetUtils.getHTML(Constants.baseURL + "/ws/v1/language/getXmlByCode?code=" + languageCode);

        // Check the original request
        // <language name="Malay" iso1="ms" iso3="msa" code="348" dialect="ms-MY" />
        if(byCode.contains("code=\"" + languageCode + "\"")) {
            String sqlCheck = "SELECT LANGUAGE_CODE FROM LANGUAGE WHERE LANGUAGE_CODE = " +  "'" + languageCode + "'";
            rs = JDBCUtils.getResultSet(sqlCheck);
        }else{
            assert false : "CODE \"" + languageCode + "\"" + " " + byCode + "\n";
        }

        rs.next();
        String checkCode = rs.getString("LANGUAGE_CODE");
        rs.close();

        if(checkCode.equals(languageCode)){
            System.out.println("\n****************  PASSED  *******************\n");
        }else{
            assert false : "CODE does not match the original request from the End Point\n.";
        }
    }

    @AfterMethod (alwaysRun = true)
    private static void closeUp()  {     JDBCUtils.closeUp();   }
}
