package org.lds.cm.content.automation.util.Constants;

/** The xpath names of the various buttons on the main menu
 *  The User Management and Group Management pages will change based on whether you have the ICS_Admin role
 *         or the manager role (admin page ids are kept track by array position, not static id)
 *      To make up for this there are two methods at the bottom that will determine the button name based off
 *          of role name.
 */

public class MenuButtonConstants
{
    public static final String HomeButtonXpath = "//*[@id=\"dashboard-id\"]";

    public static final String UploadButtonXpath = "//*[@id=\"transform-id\"]";

    //reports menu page
    public static final String ReportMenuButtonXpath = "//*[@id=\"reports-id\"]";
    public static final String ContentReportXpath= "//*[@id=\"content-report-id\"]";
    public static final String PublishHistoryXpath = "//*[@id=\"publish-history-id\"]";
    public static final String ValidationReportXpath = "//*[@id=\"validation-report-id\"]";
    public static final String DatabaseReportXpath = "//*[@id=\"db-preview-report-id\"]";

    //main menu pages
    public static final String MainMenuButtonXpath = "//*[@id=\"admin-menu--id\"]";
    public static final String SettingsButtonXpath = "//*[@id=\"settings--id\"]";
    public static final String LinkSearchButtonXpath = "//*[@id=\"link-search--id\"]";
    public static final String ScriptureLinkButtonXpath = "//*[@id=\"scripture-ref-builder--id\"]";
    public static final String ContentDownloadMenuXpath = "//*[@id=\"content-download--id\"]";
    public static final String DownloadPDFMenuXpath = "//*[@id=\"download-pdf--id\"]";
    public static final String HelpResourcesMenuXpath = "//*[@id=\"help-resources--id\"]";
    public static final String AuthorBuilderButtonXpath = "//*[@id=\"author--id\"]";
    //admin pages
    public static final String AdminBoxPlacement = "//*[@id=\"ADMINISTRATION-BOX\"]";
    public static final String APIRulesMenuXpath = "//*[@id=\"api-rules--id\"]";
    public static final String AppConfigMenuXpath = "//*[@id=\"application-configuration--id\"]";
    public static final String BackgroundProcessesMenuXpath = "//*[@id=\"background-processes--id\"]";
    public static final String BrightcoveAccountsMenuXpath = "//*[@id=\"brightcove-accounts--id\"]";
    public static final String BrightcoveVideoCacheMenuXpath = "//*[@id=\"brightcove-video-cache--id\"]";
    public static final String ContentTemplatesMenuXpath = "//*[@id=\"content-templates--id\"]";
    public static final String ContentTypeManagementMenuXpath = "//*[@id=\"content-type-management--id\"]";
    public static final String CreateManifestsAndShellFilesMenuXpath = "//*[@id=\"create-manifests-and-shell-files--id\"]";
    public static final String ManageCssMenuXpath = "//*[@id=\"css-management--id\"]";
    public static final String ManageSecurityMenuXpath = "//*[@id=\"doc-security-management--id\"]";
    public static final String ManageGroupsMenuXpath = "//*[@id=\"group-management--id\"]";
    public static final String PrintJavaScriptMenuXpath = "//*[@id=\"javascript-management--id\"]";
    public static final String ManageCoverArtMenuXpath = "//*[@id=\"manage-cover-art--id\"]";
    public static final String MarkupValidationMenuXpath = "//*[@id=\"markup-validation-rules--id\"]";
    public static final String MediaXMLMenuXpath = "//*[@id=\"media-xml-management--id\"]";
    public static final String OxygenStyleSheets = "//*[@id=\"oxygen-style-sheets--id\"]";
    public static final String QueueAdminMenuXpath = "//*[@id=\"queue-admin--id\"]";
    public static final String ManageRolesMenuXpath = "//*[@id=\"role-management--id\"]";
    public static final String UserConfigMenuXpath = "//*[@id=\"user-configuration-properties--id\"]";
    public static final String ManageUserMenuXpath = "//*[@id=\"user-management--id\"]";
    public static final String ManageTransformsMenuXpath = "//*[@id=\"xslt-management--id\"]";
    public static final String logoff = "//*[@id=\"logout-form\"]";

    public static final String PathToContentReportSearchBox = "//*[@id=\"searchBar\"]";
    public static final String PathToContentReportSearchBtn = "/html/body/div/content-report/div[2]/div[2]/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[1]";
}