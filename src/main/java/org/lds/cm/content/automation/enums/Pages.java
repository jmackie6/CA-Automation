package org.lds.cm.content.automation.enums;

import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;


/**Enum representation of pages used by the roles testing.
    Connects the Title of the page (partial title as it is always changing)
             the end url piece of the page
             the xpath of the button used to access the page
 */
public enum Pages
{
    Dashboard ("Home", "/#!/dashboard", MenuButtonConstants.HomeButtonXpath),
    Transform("Upload", "/#!/transform", MenuButtonConstants.UploadButtonXpath),
    ValidationReport ("Validation Report", "/#!/validation-report", MenuButtonConstants.ValidationReportXpath),
    PublishHistory ("Publish History", "/#!/publish-history", MenuButtonConstants.PublishHistoryXpath),
    ContentReport("Content Report", "/#!/content-report", MenuButtonConstants.ContentReportXpath),
    DatabaseReport("Database Report", "/#!/db-preview-report", MenuButtonConstants.DatabaseReportXpath),
    Settings( "Settings", "/#!/user-settings", MenuButtonConstants.SettingsButtonXpath),
    LinkSearch("Link Search", "/#!/link-search", MenuButtonConstants.LinkSearchButtonXpath),
    ManageUsers ("User Management", "/#!/manage-users", MenuButtonConstants.ManageUserMenuXpath),
    ManageGroups("Group Management", "/#!/manage-groups", MenuButtonConstants.ManageGroupsMenuXpath),
    ManageRoles("Role Management", "/#!/manage-roles", MenuButtonConstants.ManageRolesMenuXpath),
    ManageSecurity("Document Security Management", "/#!/manage-doc-security", MenuButtonConstants.ManageSecurityMenuXpath),
    BackgroundProcesses("Background Processes", "/#!/background-processes", MenuButtonConstants.BackgroundProcessesMenuXpath),
    MediaXML("Media XML Management", "/#!/media-xml", MenuButtonConstants.MediaXMLMenuXpath),
    ManageTransforms("XSLT Management", "/#!/manage-transforms", MenuButtonConstants.ManageTransformsMenuXpath),
    MarkupValidation("Markup Validation Rules", "/#!/manage-xpath", MenuButtonConstants.MarkupValidationMenuXpath),
    ManageCoverArt("Manage Cover Art", "/#!/manage-cover-art", MenuButtonConstants.ManageCoverArtMenuXpath),
    ManageCSS("CSS Management", "/#!/manage-css", MenuButtonConstants.ManageCssMenuXpath),
    ManageJavascript("JavaScript Management", "/#!/manage-print-javascript", MenuButtonConstants.PrintJavaScriptMenuXpath),
    Brightcove("Brightcove Accounts", "/#!/brightcove-accounts", MenuButtonConstants.BrightcoveAccountsMenuXpath),
    AppConfig("Application Configuration", "/#!/app-config", MenuButtonConstants.AppConfigMenuXpath),
    UserConfig("User Configuration Properties", "/#!/user-config", MenuButtonConstants.UserConfigMenuXpath),
    QueueConfig("Queue Admin", "/#!/queue-admin", MenuButtonConstants.QueueAdminMenuXpath),
    ContentDownload("Content Download", "/#!/content-download", MenuButtonConstants.ContentDownloadMenuXpath),
    DownloadPDF("Download PDF", "/#!/download-pdf", MenuButtonConstants.DownloadPDFMenuXpath),
    HelpResources("Help Resources","/#!/help-resources", MenuButtonConstants.HelpResourcesMenuXpath),
    ContentTemplates("Content Templates", "/#!/content-templates", MenuButtonConstants.ContentTemplatesMenuXpath),
    OxygenStyleSheets("Oxygen Style Sheets", "/#!/oxygen-style-sheets", MenuButtonConstants.OxygenStyleSheets),
    CreateManifests("Create Manifests and Shell Files", "/#!/manifests-and-shell-files", MenuButtonConstants.CreateManifestsAndShellFilesMenuXpath),
    ContentTypeManagement("Content Type Management", "/#!/content-type-management", MenuButtonConstants.ContentTypeManagementMenuXpath),
    VideoCache("Brightcove Video Cache", "/#!/brightcove-video-cache", MenuButtonConstants.BrightcoveVideoCacheMenuXpath);


    private final String pageName;  //name of the page in question
    private final String url; //url to get to the page
    private final String menuButton; //xpath of the button in the menu

    private Pages(String pn, String u, String mb)
    {
        pageName = pn;
        url = u;
        menuButton = mb;
    }

    public String getPageName() {    return pageName;   }
    public String getUrl() {     return url;   }
    public String getMenuButton() {     return menuButton;  }
}
