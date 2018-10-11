package org.lds.cm.content.automation.model.RoleModels;

import java.sql.SQLException;

public class Legacy extends RolesBaseClass
{
    public Legacy() throws SQLException
    {
        super("LEGACY_CLIFF_TEST");
        setRole_id();
    }
}
