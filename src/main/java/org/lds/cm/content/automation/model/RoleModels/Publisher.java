package org.lds.cm.content.automation.model.RoleModels;

import org.lds.cm.content.automation.enums.Pages;

import java.sql.SQLException;
import java.util.ArrayList;

public class Publisher extends RolesBaseClass
{
    public Publisher() throws SQLException
    {
        super("PUBLISHER");
        setRole_id();

        setupUrlAccess();
    }

    public void setupUrlAccess()
    {
        ArrayList<Pages> p = new ArrayList<>();
        p.add(Pages.Dashboard);
        p.add(Pages.PublishHistory);
        p.add(Pages.Settings);
        p.add(Pages.DownloadPDF);
        p.add(Pages.HelpResources);
        setPages(p);

        ArrayList<String> actions = new ArrayList<>();
        actions.add("Fast Track");
        actions.add("Print Export");
        actions.add("Publish History");
        actions.add("Approve");
        setAvailableActions(actions);

        ArrayList<String> previewButtons = new ArrayList<>();
        previewButtons.add("Print Export");
        setPreviewButtons(previewButtons);
    }
}
