package org.lds.cm.content.automation.model.RoleModels;

import java.sql.SQLException;
import java.util.ArrayList;

public class Unlock extends RolesBaseClass
{
    public Unlock() throws SQLException
    {
        super("UNLOCK");
        setRole_id();
        setUp();
    }

    private void setUp()
    {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Source Unlock");
        actions.add("Publish History");
        setAvailableActions(actions);
    }
}