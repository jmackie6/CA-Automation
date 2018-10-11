package org.lds.cm.content.automation.model.RoleModels;

import org.lds.cm.content.automation.enums.Pages;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;

import java.sql.SQLException;
import java.util.ArrayList;

public class Ics_Support extends RolesBaseClass
{
    public Ics_Support() throws SQLException
    {
        super("ICS_SUPPORT");
        setRole_id();

        setUp();
    }

    private void setUp()
    {
        ArrayList<Pages> p = new ArrayList<>();
        p.add(Pages.Dashboard);
        p.add(Pages.PublishHistory);
        p.add(Pages.ContentReport);
        p.add(Pages.DatabaseReport);
        p.add(Pages.Settings);
        p.add(Pages.ManageUsers);
        p.add(Pages.ManageGroups);
        p.add(Pages.ManageRoles);
        p.add(Pages.ManageSecurity);
        p.add(Pages.BackgroundProcesses);
        p.add(Pages.MediaXML);
        p.add(Pages.ManageTransforms);
        p.add(Pages.MarkupValidation);
        p.add(Pages.ManageCoverArt);
        p.add(Pages.ManageCSS);
        p.add(Pages.ManageJavascript);
        p.add(Pages.Brightcove);
        p.add(Pages.AppConfig);
        p.add(Pages.UserConfig);
        p.add(Pages.QueueConfig);
        p.add(Pages.HelpResources);
        p.add(Pages.ContentTemplates);
        p.add(Pages.OxygenStyleSheets);
        p.add(Pages.CreateManifests);
        p.add(Pages.ContentTypeManagement);
        p.add(Pages.VideoCache);
        setPages(p);

        ArrayList<String> btns = new ArrayList<>();
        btns.add(MenuButtonConstants.AuthorBuilderButtonXpath);
        setExtraMenuButtons(btns);

        ArrayList<String> actions = new ArrayList<>();
        actions.add("Delete");
        actions.add("Publish History");
        actions.add("Release System Lock");
        setAvailableActions(actions);

        ArrayList<String> previewButtons = new ArrayList<>();
        previewButtons.add("Print Export");
        setPreviewButtons(previewButtons);
    }

}