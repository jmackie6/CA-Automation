package org.lds.cm.content.automation.tests.endpoints.LanguageAPIController;

import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetXMLByDialect {

    @Test (groups = { "endpoints" })
    public static void getByDialect() throws IOException, SQLException {

        // get rs w/ query to get random dialect every time
        String sql = "SELECT DIALECT FROM (SELECT * FROM LANGUAGE WHERE DIALECT != '(null)' ORDER BY dbms_random.value) where rownum = 1";
        ResultSet rs = JDBCUtils.getResultSet(sql);

        rs.next();
        String dialect = rs.getString("DIALECT");
        rs.close();

        // get data from endpoint, pass in dialect from above
        String byDialect = NetUtils.getHTML(Constants.baseURL + "/ws/v1/language/getXmlByDialect?dialect=" + dialect);

        // Check the original request
        // <language name="Malay" iso1="ms" iso3="msa" code="348" dialect="ms-MY" />
        if(byDialect.contains("dialect=\"" + dialect + "\"")) {
            String sqlCheck = "SELECT DIALECT FROM LANGUAGE WHERE DIALECT = " +  "'" + dialect + "'";
            rs = JDBCUtils.getResultSet(sqlCheck);
        }else{
            assert false : "Dialect \"" + dialect + "\" could not be found in database.\n";
        }

        rs.next();
        String checkDialect = rs.getString("DIALECT");
        rs.close();

        if(checkDialect.equals(dialect)){
           System.out.println("\n****************  PASSED  *******************\n");
        }else{
            assert false : "Dialect does not match the original request from the End Point\n.";
        }
    }

    @AfterMethod(alwaysRun = true)
    private static void closeUp()  {     JDBCUtils.closeUp();   }
}
