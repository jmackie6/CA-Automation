package org.lds.cm.content.automation.model.UserModels;

import org.lds.cm.content.automation.util.JDBCUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserBaseClass
{
    protected String preferred_name = "";
    protected long accountID = -1;
    protected int app_user_id = -1;
    protected String username = "";
    protected String password = "";
    private final static String queryByName = "select * from app_user where preferred_name =? and deleted_flag=0 order by app_user_id desc";
    private final static String queryByUserID ="select * from app_user_role natural join app_role where app_user_id=?";
    private final static String insertion = "insert into app_user_content_group (app_user_id, content_group_id, default_content_group) "
            + " values (?,?,?)";


    public UserBaseClass(String preferred_name, String username, String password) throws SQLException
    {
        this.preferred_name = preferred_name;
        this.username = username;
        this.password = password;
        accountID = -1;
        app_user_id = -1;

        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(preferred_name);

        ResultSet rs = JDBCUtils.getResultSet(queryByName, fillInData);
        if (rs.next()) {
            accountID = rs.getLong("lds_account_id");
            app_user_id = rs.getInt("app_user_id");
        }
        rs.close();

        /** Need to add code to add content group to user if none is assigned*/
//        int count = 0;
//        fillInData.clear();
//        fillInData.add(app_user_id+"");
//        rs = JDBCUtils.getResultSet("select count(*) c from app_user_content_group where app_user_id=?", fillInData);
//        if(rs.next())
//            count = rs.getInt("c");
//        rs.close();
//        if(count == 0)
//        {
//            System.out.println("Adding content group to user through db...");
//            fillInData.clear();
//            fillInData.add(app_user_id + "");
//            fillInData.add("1");
//            fillInData.add("0");
//            rs = JDBCUtils.getResultSet(insertion, fillInData);
//            rs.close();
//        }
    }

    public String getPreferred_name() {  return preferred_name;   }
    public void setPreferred_name(String preferred_name) {  this.preferred_name = preferred_name;   }
    public long getAccountID() {  return accountID;  }
    public void setAccountID(long accountID) {   this.accountID = accountID;  }
    public int getApp_user_id() {   return app_user_id;   }
    public void setApp_user_id(int app_user_id) { this.app_user_id = app_user_id;    }
    public String getUsername() {  return username;    }
    public void setUsername(String username) {  this.username = username;    }
    public String getPassword() {  return password;    }
    public void setPassword(String password) {  this.password = password;   }


    //check to see if the user has only the role_id, or if there are more
    public boolean onlyClass(int role_id)
    {
        String query = "select * from app_user_role where app_user_id =  " + app_user_id;
        int roles = 0;
        boolean found = false;
        try {
            ResultSet rs = JDBCUtils.getResultSet(query);
            while(rs.next())
            {
                roles++;
                if(rs.getInt("app_role_id") == role_id)
                    found = true;
            }
        }
        catch(Exception e){roles = -1;}
        if(roles == 1 && found)
            return true;
        return false;
    }

    public boolean hasClass(String roleName)
    {
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(app_user_id + "");
        try
        {
            ResultSet rs = JDBCUtils.getResultSet(queryByUserID, fillInData);
            while(rs.next())
            {
                if(rs.getString("role_name").compareToIgnoreCase(roleName) == 0)
                    return true;
            }
        }
        catch(Exception e)
        {
            return false;
        }
        return false;
    }

    /** Verify that the user has a "usable" class.
     *      Lock, Unlock, Approve, Legacy all give you access denied pg when logging in  */

    public boolean hasUsableClass() throws SQLException
    {
        boolean success = false;
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(app_user_id + "");
        ResultSet rs = JDBCUtils.getResultSet(queryByUserID, fillInData);
        while(rs.next())
        {
            String role = rs.getString("role_name");
            if(role.compareToIgnoreCase("lock") != 0 || role.compareToIgnoreCase("unlock") != 0 ||
                    role.compareToIgnoreCase("approve") != 0 || role.compareToIgnoreCase("legacy") != 0)
                success = true;
        }
        rs.close();
        return success;
    }

    public boolean hasAdminClass() throws SQLException
    {
        boolean success = false;
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(app_user_id + "");
        ResultSet rs = JDBCUtils.getResultSet(queryByUserID, fillInData);
        while(rs.next())
        {
            String role = rs.getString("role_name");
            if(role.compareToIgnoreCase("ics_admin") != 0 || role.compareToIgnoreCase("manager") != 0)
                success = true;
        }
        rs.close();
        return success;
    }


    //Verify that the user has at least 10 roles
    private String ActionQuery = "select count(*) c from app_user_role where app_user_id=?";
    public boolean hasActionRoles() throws SQLException
    {
        ArrayList<String> fillInData = new ArrayList<>();
        fillInData.add(app_user_id + "");
        ResultSet rs = JDBCUtils.getResultSet(ActionQuery, fillInData);
        if(rs.next())
            return rs.getInt("c") >= 10;
        return false;
    }

    public static UserBaseClass createUser(String username) throws SQLException
    {
        if(username.compareTo("ngiwb3") == 0)
            return new NormanLevy();
        else
            return new FonoSaia();
    }
}
