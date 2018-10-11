package org.lds.cm.content.automation.tests;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.lds.cm.content.automation.service.QAManifestService;
import org.testng.annotations.Test;

public class QAManifestTest {
	
	@Test
	public void ManifestFileIdTest() throws ParseException, ClientProtocolException, IOException, SQLException {
		System.out.println("In ManifestFileIdTest");
		System.out.println("Test the service that takes a file ID, and all associated files.");
	
			// Returns XML showing all files for that match that file ID (first two segments).
				
			String fileId = "PD00009501_173_000";

			QAManifestService.returnAllFileIDs(fileId);
		      			
	}

}
