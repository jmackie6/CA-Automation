package org.lds.cm.content.automation.model.RoleModels;

import org.lds.cm.content.automation.enums.Pages;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;

import java.sql.SQLException;
import java.util.ArrayList;

public class Editor extends RolesBaseClass
{
    public Editor() throws SQLException
    {
        super("EDITOR");
        setRole_id();

        setupUrlAccess();
    }

    private void setupUrlAccess()
    {
        ArrayList<Pages> p = new ArrayList<>();
        p.add(Pages.Dashboard);
        p.add(Pages.Transform);
        p.add(Pages.ValidationReport);
        p.add(Pages.PublishHistory);
        p.add(Pages.Settings);
        p.add(Pages.LinkSearch);
        p.add(Pages.ContentDownload);
        p.add(Pages.DownloadPDF);
        p.add(Pages.HelpResources);
        setPages(p);

        ArrayList<String> btns = new ArrayList<>();
        btns.add(MenuButtonConstants.ScriptureLinkButtonXpath);
        btns.add(MenuButtonConstants.AuthorBuilderButtonXpath);
        setExtraMenuButtons(btns);

        ArrayList<String> actions = new ArrayList<>();
        actions.add("Approve");
        actions.add("Fast Track");
        actions.add("Publish History");
        actions.add("Edit Metadata");
        actions.add("Validate");
        actions.add("Download");
        setAvailableActions(actions);

        ArrayList<String> previewButtons = new ArrayList<>();
        previewButtons.add("Edit");
        previewButtons.add("Refresh");
        previewButtons.add("Validate");
        previewButtons.add("Edit Metadata");
        previewButtons.add("Print Export");
        setPreviewButtons(previewButtons);
    }
}
