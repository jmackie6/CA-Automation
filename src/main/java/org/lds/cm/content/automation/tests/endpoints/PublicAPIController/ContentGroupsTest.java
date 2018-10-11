package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ContentGroupsTest {


	@BeforeClass (alwaysRun = true)
	public void setUp() throws SQLException, InterruptedException
	{	APIRules.fixAPIRule("ws/v1/contentGroups");	}

/////////////////// Passing Test Cases ///////////////////////////
	@Test (groups = { "endpoints" })
	public void checkContentGroupsWs() throws Exception
	{
		checkContentGroupIDsResponse("/ws/v1");
		checkContentGroupsResponse("/ws/v1");
	}

	@Test (groups = { "endpoints" })
	public void checkContentGroupsServ() throws Exception
	{
		checkContentGroupIDsResponse("/services/api");
		checkContentGroupsResponse("/services/api");
	}

	// Checks the ws/v1/contentGroups API endpoint that it contains only active Content Group IDs
	public static void checkContentGroupIDsResponse(String type) throws ParseException, IOException, SQLException {
		//System.out.println("**** In ContentGroupsTest ****\n");
		//System.out.println("Test: checkContentGroupsIDsResponse");
		//System.out.println("Description: Test all the Content Group IDs to make sure they are active content groups");
		ResultSet rs = JDBCUtils.getResultSet("select content_group_id, group_name from content_group where active_flag = 1");
			
			// Get array of content groups from endpoint
			JSONObject contentGroupsJSON = NetUtils.getJson(Constants.baseURL + type + "/contentGroups");
			JSONArray contentGroupsJSONArray = (JSONArray) contentGroupsJSON.get("ContentGroups");
			
			// Populate list of content group IDs from endpoint
			List<String> epContentGroupIDs = new ArrayList<>();
			for (int i = 0; i < contentGroupsJSONArray.size(); i++) {
				JSONObject epContentGroup = (JSONObject) contentGroupsJSONArray.get(i);
				String epContentGroupID = epContentGroup.get("id").toString();
				epContentGroupIDs.add(epContentGroupID);
			}

			// Populate list of content Group IDs from DB
			List<String> dbContentGroupIDs = new ArrayList<>();
			while (rs.next()) {
				String dbContentGroupID = rs.getString(1);
				dbContentGroupIDs.add(dbContentGroupID);
			}
			
			// Check that no deactivated groups are listed in endpoint response
			List<String> failedEpContentGroupIDs = new ArrayList<>();
			for (String epContentGroupID: epContentGroupIDs) {
				if (!dbContentGroupIDs.contains(epContentGroupID)) {
					failedEpContentGroupIDs.add(epContentGroupID);
				}
				//else { System.out.println("Content Group ID " + epContentGroupID + " is active"); }
			}

			Assert.assertTrue(failedEpContentGroupIDs.size() == 0, "Failed Endpoint Content Group IDs: " + failedEpContentGroupIDs);
	}
	
	// Checks the ws/v1/contentGroups API endpoint response for an exact data match with the database results
	public static void checkContentGroupsResponse (String type) throws ParseException, IOException, SQLException {
//		System.out.println("Test: checkContentGroupsResponse");
//		System.out.println("Description: Test the Content Groups returned by the endpoint if they match the database");
//		System.out.println();

		ResultSet rs = JDBCUtils.getResultSet("select content_group_id, group_name from content_group where active_flag = 1");
		// Get array of content groups from endpoint
		JSONObject contentGroupsJSON = NetUtils.getJson(Constants.baseURL + type + "/contentGroups");
		JSONArray contentGroupsJSONArray = (JSONArray) contentGroupsJSON.get("ContentGroups");

		// Populate Content groups map with endpoint data
		Map<String, String> epContentGroupsMap = new HashMap<>();
		for (int i = 0; i < contentGroupsJSONArray.size(); i++) {
			JSONObject contentGroup = (JSONObject) contentGroupsJSONArray.get(i);
			String contentGroupID = contentGroup.get("id").toString();
			String contentGroupName = contentGroup.get("name").toString();
			epContentGroupsMap.put(contentGroupID, contentGroupName);
		}

		// Populate Content groups map with database data
		Map<String, String> dbContentGroupsMap = new HashMap<>();
		while (rs.next()) {
			String dbContentGroupID = rs.getString(1);
			String dbContentGroupName = rs.getString(2);
			dbContentGroupsMap.put(dbContentGroupID, dbContentGroupName);
		}

		// Compare that the id,name couple matches between the database and the endpoint
		Assert.assertEquals(epContentGroupsMap, dbContentGroupsMap, "Database and endpoint results do not match");

	}

	@AfterMethod(alwaysRun = true)
	public void cleanup()	{	JDBCUtils.closeUp();	}
}