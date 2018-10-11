package deprecated;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

//import org.lds.cm.content.automation.util.UploadTransformUtil;

public class OpenEndpointTest {
	
	/**
	 * Test the end point that retrieves a file based on the batch guid. This only works if one file at a time is transformed because multiple
	 * files transformed at the same time will each have the same batch guid.
	 * 
	 * I think this could be changed to implement the transform end point for one file at a time, and then try to retrieve the file.
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws ParseException 
	 */
//	@Test
	public void testPreviewFile() throws SQLException, ParseException, ClientProtocolException, IOException {
		System.out.println();
		System.out.println("Test: testPreviewFile");
		System.out.println("Test: Make sure the preview file end point returns the correct file with the correct css.");
		System.out.println("ML HOST: " + Constants.mlPreviewHost);
		
		List<String> failedCases = new ArrayList<>();
		
		// get result set for successful transforms
		String query = "select * from process_log where status = 'SUCCESSFUL' and process_name = 'TRANSFORM' order by MODIFIED_DATE desc";
		ResultSet rs = JDBCUtils.getResultSet(query);
		
		// loop through batch_guid and get files with end point
		for (int i = 0; i < 100; i++) {
			rs.next();
			String uriFromDB = rs.getString("NAVIGATION_URI");
			//String fileNameFromDB = rs.getString("FILE_NAME");
			String batch_guid = rs.getString("BATCH_GUID");
			System.out.println("Testing file: " + uriFromDB);
			
			// get html from endpoint
			String htmlString = NetUtils.getHTML(Constants.epPreviewFileByBatchGuid + batch_guid);
			Pattern pattern = Pattern.compile("data-uri=\"(.*?)\" ");
			Matcher matcher = pattern.matcher(htmlString);
			if (matcher.find()) {
				String uriFromFile = matcher.group(1);
				//System.out.println(", URI: " + uriFromFile);
				
				// compare uri from result set with uri in file
				if (uriFromDB != null) {
					if (uriFromDB.contains(uriFromFile)) {
						System.out.println(uriFromDB);
					} else {
						failedCases.add("URI from DB: " + uriFromDB + " did not match URI from file: " + uriFromFile);
					}
				}
				
				// also check that correct css is present
			} else {
				System.out.println("Could not find URI");
				// TODO: more error handling here
			}
		}
		
		Assert.assertTrue(failedCases.isEmpty(), "Failed cases: " + failedCases);
	}
	
	/**
	 * test the transformed file with endpoints to see that they work and return the correct file
	 * the json passed in is the response from the transform endpoint containing Validation errors
	 * @param json
	 */
	public static void preview(JSONObject json) {
		if (json.get("transformSuccess").equals("true")) {
			
		}
	}
	
	/**
	 * update this method
	 * @throws org.json.simple.parser.ParseException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public static void testTransform() throws ClientProtocolException, IOException, org.json.simple.parser.ParseException {
		System.out.println();
		System.out.println("Test method: testTransform");
		System.out.println("Test: Test the transform endpoint.");
		System.out.println("ML HOST: " + Constants.mlPublishHost);
		
		List<File> filePaths = new ArrayList<File>();
		
		for (File file : filePaths) {
//			QATransformationResult result = UploadTransformUtil.transformFile(file);
//			preview(result);
		}
	}
}
