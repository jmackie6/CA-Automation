package org.lds.cm.content.automation.model.RoleModels;

import java.sql.SQLException;
import java.util.ArrayList;

public class Approver extends RolesBaseClass
{
    public Approver() throws SQLException
    {
        super("APPROVER");
        setRole_id();
        setUpActions();
    }

    private void setUpActions()
    {
        ArrayList<String> available = new ArrayList<>();
        available.add("Approve");
        available.add("Publish History");
        setAvailableActions(available);
    }
}