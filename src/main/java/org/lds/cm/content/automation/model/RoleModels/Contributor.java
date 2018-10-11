package org.lds.cm.content.automation.model.RoleModels;

import org.lds.cm.content.automation.enums.Pages;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;

import java.sql.SQLException;
import java.util.ArrayList;

public class Contributor extends RolesBaseClass
{
    public Contributor() throws SQLException
    {
        super("CONTRIBUTOR");
        setRole_id();

        setUp();
    }


    private void setUp()
    {
        ArrayList<Pages> p = new ArrayList<>();
        p.add(Pages.Transform);
        p.add(Pages.Dashboard);
        p.add(Pages.ValidationReport);
        p.add(Pages.Settings);
        p.add(Pages.LinkSearch);
        p.add(Pages.DownloadPDF);
        p.add(Pages.HelpResources);
        setPages(p);

        ArrayList<String> btns = new ArrayList<>();
        btns.add(MenuButtonConstants.ScriptureLinkButtonXpath);
        btns.add(MenuButtonConstants.AuthorBuilderButtonXpath);
        setExtraMenuButtons(btns);

        ArrayList<String> actions = new ArrayList<>();
        actions.add("Fast Track");
        actions.add("Publish History");
        actions.add("Validate");
        setAvailableActions(actions);

        ArrayList<String> previewButtons = new ArrayList<>();
        previewButtons.add("Validate");
        previewButtons.add("Print Export");
        setPreviewButtons(previewButtons);
    }
}
