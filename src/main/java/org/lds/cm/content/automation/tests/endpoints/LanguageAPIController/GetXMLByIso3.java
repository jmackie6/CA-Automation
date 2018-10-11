package org.lds.cm.content.automation.tests.endpoints.LanguageAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetXMLByIso3 {

    @Test (groups = { "endpoints" })
    public static void getByIso3() throws IOException, SQLException {

        // get rs w/ query to get random dialect every time
        String sql = "SELECT ISO_LANG_CD_PART3 FROM (SELECT * FROM LANGUAGE WHERE LANGUAGE.COUNT_AS_APPROVED_Y_N_FLAG = 'Y'" +
                     "ORDER BY dbms_random.value) where rownum = 1";
        ResultSet rs = JDBCUtils.getResultSet(sql);

        rs.next();
        String iso3 = rs.getString("ISO_LANG_CD_PART3");
        rs.close();

        // get data from endpoint, pass in iso3 from above
        String byIso3 = NetUtils.getHTML(Constants.baseURL + "/ws/v1/language/getXmlByIso3?iso3=" + iso3);

        // Check the original request from End Point
        // <language name="Malay" iso1="ms" iso3="msa" code="348" dialect="ms-MY" />
        if(byIso3.contains("iso3=\"" + iso3 + "\"")) {
            String sqlCheck = "SELECT ISO_LANG_CD_PART3 FROM LANGUAGE WHERE ISO_LANG_CD_PART3 = " +  "'" + iso3 + "'";
            rs = JDBCUtils.getResultSet(sqlCheck);
        }else{
            assert false : "ISO3 \"" + iso3 + "\"" + " " + byIso3 + "\n";
        }

        rs.next();
        String checkCode = rs.getString("ISO_LANG_CD_PART3");
        rs.close();

        if(checkCode.equals(iso3)){
            System.out.println("\n****************  PASSED  *******************\n");
        }else{
            assert false : "ISO3 does not match the original request from the End Point\n.";
        }
    }
    @AfterMethod(alwaysRun = true)
    private static void closeUp()  {     JDBCUtils.closeUp();   }
}
