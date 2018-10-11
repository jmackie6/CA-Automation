package org.lds.cm.content.automation.model.RoleModels;

import java.sql.SQLException;
import java.util.ArrayList;

public class Lock extends RolesBaseClass
{
    public Lock() throws SQLException
    {
        super("LOCK");
        setRole_id();
        setUp();
    }

    private void setUp()
    {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Source Lock");
        actions.add("Publish History");
        setAvailableActions(actions);
    }
}
