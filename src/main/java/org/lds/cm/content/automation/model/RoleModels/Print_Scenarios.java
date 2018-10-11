package org.lds.cm.content.automation.model.RoleModels;

import java.sql.SQLException;
import java.util.ArrayList;

public class Print_Scenarios extends RolesBaseClass
{
    public Print_Scenarios() throws SQLException
    {
        super("Print_Scenarios");
        setRole_id();
        setUpActions();
    }

    private void setUpActions()
    {
        ArrayList<String> available = new ArrayList<>();
        available.add("Publish History");
        setAvailableActions(available);
    }

}