package org.lds.cm.content.automation.model.RoleModels;

import org.lds.cm.content.automation.enums.Pages;

import java.sql.SQLException;
import java.util.ArrayList;

public class Manager extends RolesBaseClass
{
    public Manager() throws SQLException
    {
        super("MANAGER");
        setRole_id();

        setupUrlAccess();
    }

    public void setupUrlAccess()
    {
        ArrayList<Pages> p = new ArrayList<>();
        p.add(Pages.Dashboard);
        p.add(Pages.Settings);
        p.add(Pages.HelpResources);
        p.add(Pages.ManageUsers);
        p.add(Pages.ManageGroups);
        setPages(p);

        ArrayList<String> previewButtons = new ArrayList<>();
        previewButtons.add("Print Export");
        setPreviewButtons(previewButtons);
    }

}
