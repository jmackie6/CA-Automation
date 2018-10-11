package org.lds.cm.content.automation.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.lds.cm.content.automation.model.QAHtml5Document;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.XMLUtils;
import org.testng.Assert;
import org.w3c.dom.NodeList;

public class QAManifestService {
	
	public static void returnAllFileIDs(String fileId) throws SQLException, IOException {
		// Returns XML showing all files that match that file ID (first two segments).
					// https://publish-test.ldschurch.org/content_automation/ws/v1/getAllMatchingFileIDs?fileId=00001_000_jeremiah
					//String fileId = "PD00009501_173_000";

					String epString = Constants.epManifest + fileId;
					String xml = NetUtils.getHTML(epString);
					
							String name = "//@id";
					
							NodeList file_node = XMLUtils.getNodeListFromXpath(xml, name, null);
							int count = 0;

							for (int i = 0; i < file_node.getLength(); i++) 
							{
								String nameString = file_node.item(i).getNodeValue();
								//System.out.println("File: "  + nameString);
								QAHtml5Document dbName =  QADocumentService.fromFileId(nameString);
								System.out.println("dbName: "  + dbName.getFileId());
								Assert.assertEquals(dbName.getFileId(), nameString);
								count++;
								
							}  
							System.out.println("\n" + count + " Files processed\n");
							Assert.assertEquals(getDocumentCount(fileId), count);
							return;
							
		                    // any missing from the database, possible that service inserted any or left any out
		                    // check what Apache has in the commons to compare list of strings
		                    
		                    // select fileID from doc where filename = 'manifest.html' instead of hard-coded
		                    // or hard-code 15-20 random file IDs, or use random sample method   
}
	
	public static int getDocumentCount(String fileId) {
		String string = fileId;
		String[] parts = string.split("_");
		String part1 = parts[0]; 
		String part2 = parts[1]; 
		
		String newString = part1 + "_" + part2;
		
		String sql = "select COUNT(*) AS count from document where file_id like '%" + newString + "%' ";
		int documentCount = 0;
		
		try {
			ResultSet rs = JDBCUtils.getResultSet(sql);
			
			if (rs != null) {
				while(rs.next()) {
					documentCount = rs.getInt("count");
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return documentCount;
	}

}
