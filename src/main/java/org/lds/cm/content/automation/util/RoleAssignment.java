package org.lds.cm.content.automation.util;

import org.lds.cm.content.automation.model.RoleModels.RolesBaseClass;
import org.lds.cm.content.automation.model.UserModels.UserBaseClass;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoleAssignment
{
    private static String maxQuery = "select MAX(app_user_role_id) id from app_user_role";
    private static String insertion = "insert into app_user_role (app_user_role_id, app_user_id, app_role_id) "+
            " values (?,?,?)";
    public static void assignRole(UserBaseClass ubc, RolesBaseClass rbc) throws SQLException, InterruptedException
    {
        ResultSet rs = JDBCUtils.getResultSet(maxQuery);
        int id = -1;
        if(rs.next())
        {
            id = rs.getInt("id");
        }
        rs.close();
        Assert.isTrue(id > 0);

        System.out.println(id);
        ArrayList<String> fillIn = new ArrayList<String>();
        fillIn.add((id +1)+ "");
        fillIn.add(ubc.getApp_user_id() + "");
        fillIn.add(rbc.getRole_id() + "");

        int outcome = JDBCUtils.executeUpdate(insertion, fillIn);
        Assert.isTrue(outcome == 1, "Inserting new class into the database was unsuccessful");
    }
}
