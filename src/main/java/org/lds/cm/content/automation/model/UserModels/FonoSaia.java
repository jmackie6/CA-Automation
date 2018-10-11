package org.lds.cm.content.automation.model.UserModels;

import org.lds.cm.content.automation.util.JDBCUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FonoSaia extends UserBaseClass
{
    public FonoSaia() throws SQLException
    {
        super("Fono Saia 'Ilangana", "ngide1", "password1");

        //Because Fono has a ' in his name, it screws up the
        final String query = "select * from app_user where preferred_name like 'Fono%' and deleted_flag=0 order by app_user_id desc";
        ResultSet rs = JDBCUtils.getResultSet(query);
        if (rs.next()) {
            accountID = rs.getLong("lds_account_id");
            app_user_id = rs.getInt("app_user_id");
        }
        rs.close();
    }

}
