package org.lds.cm.content.automation.model.RoleModels;

import org.lds.cm.content.automation.enums.Pages;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectManager extends RolesBaseClass
{
    public ProjectManager() throws SQLException
    {
        super("PROJECT_MANAGER");
        setRole_id();

        setupUrlAccess();
    }

    private void setupUrlAccess()
    {
        ArrayList<Pages> p = new ArrayList<>();
        p.add(Pages.Dashboard);
        p.add(Pages.ValidationReport);
        p.add(Pages.PublishHistory);
        p.add(Pages.Settings);
        p.add(Pages.ContentDownload);
        p.add(Pages.DownloadPDF);
        p.add(Pages.HelpResources);
        setPages(p);

        ArrayList<String> actions = new ArrayList<>();
        actions.add("Fast Track");
        actions.add("Print Export");
        actions.add("Publish");
        actions.add("Publish History");
        actions.add("Update Group and Owner");
        actions.add("Validate");
        actions.add("Download");
        setAvailableActions(actions);

        ArrayList<String> previewButtons = new ArrayList<>();
        previewButtons.add("Validate");
        previewButtons.add("Print Export");
        setPreviewButtons(previewButtons);
    }

}
