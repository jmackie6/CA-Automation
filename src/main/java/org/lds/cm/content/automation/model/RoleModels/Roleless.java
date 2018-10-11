package org.lds.cm.content.automation.model.RoleModels;

import java.sql.SQLException;

public class Roleless extends RolesBaseClass
{
        public Roleless() throws SQLException
        {
            super("ROLELESS");
            setRole_id();
        }
}

