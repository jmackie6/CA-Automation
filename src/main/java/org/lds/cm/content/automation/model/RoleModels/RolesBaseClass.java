package org.lds.cm.content.automation.model.RoleModels;

import org.lds.cm.content.automation.enums.Pages;
import org.lds.cm.content.automation.util.JDBCUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class RolesBaseClass
{
     private String name = "";
     private int role_id = -1;
     private ArrayList<Pages> pages;
     private ArrayList<String> extraMenuButtons; //Scripture and Author Builder (because they are modals and not pages)
     private ArrayList<String> availableActions;
     private ArrayList<String> previewButtons;
     private static final String rolesBaseClassQuery = "select * from app_role where role_name=?";
     private static final String PSD_ADMINDELETE_QUERY_STRING = "UPDATE app_config SET value = ? WHERE name = \"PSD_ADMIN_CAN_DELETE\"";

     public RolesBaseClass(String name)
     {
         this.name = name;
         pages = new ArrayList<>();
         extraMenuButtons = new ArrayList<>();
         availableActions = new ArrayList<>();
         previewButtons = new ArrayList<>();
     }

     public void setName(String n) { name = n;}
     public String getName() { return name; }

     public int getRole_id() { return role_id; }

    public void setRole_id() throws SQLException {
         ArrayList<String> fillIn = new ArrayList<String>();
         fillIn.add(name);
        ResultSet rs = JDBCUtils.getResultSet(rolesBaseClassQuery, fillIn);
        if (rs.next())
            role_id = rs.getInt("app_role_id");
        rs.close();
    }

    public ArrayList<Pages> getPageAccess() { return pages; }
    public void setPages(ArrayList<Pages> urls) { pages = urls; }
    public ArrayList<String> getExtraMenuButtons() { return extraMenuButtons; }
    public void setExtraMenuButtons(ArrayList<String> emb) {extraMenuButtons = emb; }
    public ArrayList<String> getAvailableActions() { return availableActions; }

    /** Certain actions are not bulk actions as well.  So remove it from the array before returning it */
    public ArrayList<String> getAvailableBulkActions()
    {
        ArrayList<String> returning = new ArrayList<>();
        for(String x : availableActions)
        {
            if(x.compareToIgnoreCase("Publish History") != 0
                    && x.compareToIgnoreCase("Fast Track") != 0
                    && x.compareToIgnoreCase("Print Export") != 0
                    && x.compareToIgnoreCase("Edit Metadata") != 0)
                returning.add(x);
        }
        return returning;
    }
    public void setAvailableActions(ArrayList<String> actions) { availableActions = actions; }
    public ArrayList<String> getPreviewButtons() { return previewButtons; }
    public void setPreviewButtons(ArrayList<String> previewBtns) { previewButtons = previewBtns; }

    /**
     * Updates the PSD_ADMIN_CAN_DELETE value in the app_config table to the input value
     * @param value TRUE/FALSE
     * @return True if successful false if not
     * @throws SQLException
     */
    public boolean setPcsAdminCanDelete(String value) throws SQLException {
        boolean retVal = false;
        ArrayList<String> fillIn = new ArrayList<String>();
        fillIn.add(value);
        ResultSet rs = JDBCUtils.getResultSet(PSD_ADMINDELETE_QUERY_STRING, fillIn);
        if (rs.next()) {
            if (rs.rowUpdated()) {
                retVal = true;
            }
        }
        rs.close();
        return retVal;
    }

}
