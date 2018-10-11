package org.lds.cm.content.automation.tests;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.lds.cm.content.automation.service.QALockUnlockService;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class QALockUnlockTest {
	
	//set your user id here. You can find this next to your name in Content Central -> Manage Users
			static String user_id = "81";
			
	@Test
	public static void testBulkLock() throws SQLException, IOException {
		
		QALockUnlockService.bulkUnlock("97905_000", user_id);
	}

	@Test
	public static void lockUnlock() throws SQLException, IOException {
		
		
		//Initiates strings of 5 random files to lock, and 5 to unlock.
		ArrayList<String> toUnlock = QALockUnlockService.filesToUnlock();
		ArrayList<String> toLock = QALockUnlockService.filesToLock();
		
		//Gets current date and time to compare later
		String sysdateQuery = "select to_date(To_Char(Sysdate, 'DD-MON-YY HH24:MI'), 'DD-MON-YY HH24:MI') from dual";
		ResultSet date = JDBCUtils.getResultSet(sysdateQuery);
		date.next();
		String sysdate = date.getString("TO_DATE(TO_CHAR(SYSDATE,'DD-MON-YYHH24:MI'),'DD-MON-YYHH24:MI')");
		
		System.out.println("\n");

		/*For every document id in the lock array, checks if document exists with checkForDocID and if it does, it calls document lock.
		 If it doesn't, the loop will skip to next document id and tell you it was not found*/
		for(String doc_id: toLock) {
			if(QALockUnlockService.checkForDocID(doc_id)) {
				QALockUnlockService.documentLock(doc_id, user_id);
			}
			else {
				System.out.println(doc_id + "was not found");
				continue;
			}
			
			/*After document has been locked, this query pulls up the document row
			in the DB with two extra columns - the new dates in a easier format to compare */
			String checkQuery = "select m.*, to_date(To_Char(status_change_date, 'DD-MON-YY HH24:MI'), 'DD-MON-YY HH24:MI'), "
					+ "to_date(To_Char(modified_date, 'DD-MON-YY HH24:MI'), 'DD-MON-YY HH24:MI') from document m where document_id = " + doc_id;
			ResultSet rs = JDBCUtils.getResultSet(checkQuery);
			rs.next();
			
			//The next five lines get the value in each column of the document's row that should have been updated by documentLock
			String status = rs.getString("DOCUMENT_STATE_ID");
			String statusDate = rs.getString("TO_DATE(TO_CHAR(STATUS_CHANGE_DATE,'DD-MON-YYHH24:MI'),'DD-MON-YYHH24:MI')");
			String modifiedDate = rs.getString("TO_DATE(TO_CHAR(MODIFIED_DATE,'DD-MON-YYHH24:MI'),'DD-MON-YYHH24:MI')");
			String modifiedUserID = rs.getString("MODIFIED_BY_APP_USER_ID");
			String statusUserID = rs.getString("STATUS_APP_USER_ID");
			
			/*Asserts to make sure the value of each column in the document has been changed correctly 
			(status set to "LOCKED", status and modified dates set to current date and time, modified and status 
			user id's are set to user id set at the beginning of this class)*/
			Assert.assertEquals(status, "2");
			Assert.assertEquals(statusDate, sysdate);
			Assert.assertEquals(modifiedDate,  sysdate);
			Assert.assertEquals(modifiedUserID, user_id);
			Assert.assertEquals(statusUserID, user_id);	
		}

		System.out.println("\n");
		
		
		/*For every document id in the unlock array, checks if document exists with checkForDocID and if it does, it calls document unlock.
		 If it doesn't, the loop will skip to next document id and tell you it was not found*/
		for(String doc_id: toUnlock) {
			if(QALockUnlockService.checkForDocID(doc_id)) {
				QALockUnlockService.documentUnlock(doc_id, user_id);
			}
			else {
				System.out.println(doc_id + "was not found");
				continue;
			}
			
			/*After document has been unlocked, this query pulls up the document row
			in the DB with two extra columns - the new dates in a easier format to compare */
			String checkQuery = "select m.*, to_date(To_Char(status_change_date, 'DD-MON-YY HH24:MI'), 'DD-MON-YY HH24:MI'), "
					+ "to_date(To_Char(modified_date, 'DD-MON-YY HH24:MI'), 'DD-MON-YY HH24:MI') from document m where document_id = " + doc_id;
			ResultSet rs = JDBCUtils.getResultSet(checkQuery);
			rs.next();
			
			//The next five lines get the value in each column of the document's row that should have been updated by documentUnlock
			String status = rs.getString("DOCUMENT_STATE_ID");
			String statusDate = rs.getString("TO_DATE(TO_CHAR(STATUS_CHANGE_DATE,'DD-MON-YYHH24:MI'),'DD-MON-YYHH24:MI')");
			String modifiedDate = rs.getString("TO_DATE(TO_CHAR(MODIFIED_DATE,'DD-MON-YYHH24:MI'),'DD-MON-YYHH24:MI')");
			String modifiedUserID = rs.getString("MODIFIED_BY_APP_USER_ID");
			String statusUserID = rs.getString("STATUS_APP_USER_ID");
			
			/*Asserts to make sure the value of each column in the document has been changed correctly 
			(status set to "VALIDATED", status and modified dates set to current date and time, modified and status 
			user id's are set to user id set at the beginning of this method)*/
			Assert.assertEquals(status, "3");
			Assert.assertEquals(statusDate, sysdate);
			Assert.assertEquals(modifiedDate,  sysdate);
			Assert.assertEquals(modifiedUserID, user_id);
			Assert.assertEquals(statusUserID, user_id);
		}	
		
		System.out.println("\n");
		
	}	
	
}