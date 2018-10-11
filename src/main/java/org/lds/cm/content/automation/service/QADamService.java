package org.lds.cm.content.automation.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lds.cm.content.automation.util.JDBCUtils;

public class QADamService {

	
	/*This will set dam failure for every documents by document id for each doc id put into 
	/content_automation-qa/src/main/java/org/lds/cm/content/automation/service/Documents/DamFailureDoc.txt*/
	public static void setDamFailureFromList() throws SQLException {
		String path = "./src/main/java/org/lds/cm/content/automation/service/Documents/DamFailureDoc.txt"; 
		
		try {
			FileInputStream file = new FileInputStream(path);
			Scanner scanner = new Scanner(file);
			String listItem;
			
			while(scanner.hasNextLine()) {
				listItem = scanner.nextLine();
				listItem = listItem.trim();

				if(StringUtils.isNotBlank(listItem) && NumberUtils.isNumber(listItem)) {
     				
					String sql = "update content_publish set status = 'DAM_FAILURE', DAM_ID = null where document_id in " + listItem
							+ "and content_version = (select max(content_version) from content_publish where document_id = " + listItem + ")";
					JDBCUtils.getResultSet(sql);
					System.out.println(listItem);
					String sql2 = "commit";
					JDBCUtils.getResultSet(sql2);
				}
			}
			scanner.close();
			file.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
