package org.lds.cm.content.automation.model.RoleModels;

import java.sql.SQLException;

public class Feature_Testing extends RolesBaseClass
{
    public Feature_Testing() throws SQLException
    {
        super("FEATURE_TESTING");
        setRole_id();
    }
}
