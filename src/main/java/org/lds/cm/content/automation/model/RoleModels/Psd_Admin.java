package org.lds.cm.content.automation.model.RoleModels;

import java.sql.SQLException;
import java.util.ArrayList;

public class Psd_Admin extends RolesBaseClass
{
    public Psd_Admin() throws SQLException
    {
        super("PSD_ADMIN");
        setRole_id();

        setupUrlAccess();
    }

    public void setupUrlAccess()
    {
        ArrayList<String> actions = new ArrayList<>();
        actions.add("Delete");
        actions.add("Publish History");
        setAvailableActions(actions);
    }
}
