package org.lds.cm.content.automation.model.RoleModels;

import org.lds.cm.content.automation.enums.Pages;

import java.sql.SQLException;
import java.util.ArrayList;

public class Viewer extends RolesBaseClass
{
    public Viewer() throws SQLException
    {
        super("VIEWER");
        setRole_id();

        setupUrlAccess();
    }

    public void setupUrlAccess()
    {
        ArrayList<Pages> pages = new ArrayList<Pages>();
        pages.add(Pages.Dashboard);
        pages.add(Pages.Settings);
        pages.add(Pages.HelpResources);
        setPages(pages);

        ArrayList<String> previewButtons = new ArrayList<>();
        previewButtons.add("Print Export");
        setPreviewButtons(previewButtons);
    }
}
