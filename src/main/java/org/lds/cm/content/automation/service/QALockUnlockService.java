package org.lds.cm.content.automation.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.lds.cm.content.automation.util.JDBCUtils;

public class QALockUnlockService {

	/*This method locks each document associated with document id given, 
	and updates status and modified user id to user id given*/
	public static void documentLock(String doc_id, String user_id) throws SQLException, IOException {
		//gets current status of document 
		String checkStatus = "SELECT DOCUMENT_STATE_ID FROM DOCUMENT WHERE DOCUMENT_ID = " + "'" + doc_id + "'";
		ResultSet rs = JDBCUtils.getResultSet(checkStatus);
		rs.next();
		String status = rs.getString("DOCUMENT_STATE_ID");
		rs.close();
		
		/*Makes sure the status is either validated or complete so that it can be locked. 
		  If so, document is locked, dates and users updated then prints out that document has been locked*/
		if(status.equals("3")) {
			String sql = "UPDATE DOCUMENT SET DOCUMENT_STATE_ID = 2, STATUS_CHANGE_DATE = sysdate, "
					+ "MODIFIED_DATE = sysdate, MODIFIED_BY_APP_USER_ID = " + user_id
					+ ", STATUS_APP_USER_ID = " + user_id + " WHERE DOCUMENT_ID = " + "'" + doc_id + "'";
			JDBCUtils.getResultSet(sql);
			System.out.println("Document " + doc_id + " has been locked \n");
		}
	}
	
	
	/*This method unlocks each document associated with document id given, 
	and updates status and modified user id to user id given*/
	public static void documentUnlock(String doc_id, String user_id) throws SQLException, IOException {
		String checkStatus = "SELECT DOCUMENT_STATE_ID FROM DOCUMENT WHERE DOCUMENT_ID = " + "'" + doc_id + "'";
		ResultSet rs = JDBCUtils.getResultSet(checkStatus);
		rs.next();
		String status = rs.getString("DOCUMENT_STATE_ID");
		rs.close();
		
		/*Makes sure the status is LOCKED so that it can be unlocked. 
		  If so, document is unlocked, dates and users updated then prints out that document has been unlocked*/
		if(status.equals("2")) {
			String sql = "UPDATE DOCUMENT SET DOCUMENT_STATE_ID = 3, STATUS_CHANGE_DATE = sysdate, " 
					+ "MODIFIED_DATE = sysdate, MODIFIED_BY_APP_USER_ID = " + user_id
					+ ", STATUS_APP_USER_ID = " + user_id + " WHERE DOCUMENT_ID = " + "'" + doc_id + "'";;
			JDBCUtils.getResultSet(sql);
			System.out.println("Document " + doc_id + " has been unlocked \n");
		}
	}
	
	
	/*takes in file id excluding last three digits and will lock all lockable documents associated with that file id*/
	public static void bulkLock(String fileId, String user_id) throws SQLException, IOException {
		
		String getDocIds = "SELECT * FROM DOCUMENT WHERE FILE_ID like '" + fileId + "%' and DOCUMENT_STATE_ID = 3";
		ResultSet rs = JDBCUtils.getResultSet(getDocIds);
		rs.next();
		
		while(rs.next()) {
			documentLock(rs.getString("DOCUMENT_ID"), user_id);
		} 
	}
	
	/*takes in file id excluding last three digits and will unlock all unlockable documents associated with that file id*/
	public static void bulkUnlock(String fileId, String user_id) throws SQLException, IOException {

		String getDocIds = "SELECT * FROM DOCUMENT WHERE FILE_ID like '" + fileId + "%' and DOCUMENT_STATE_ID = 2";
		ResultSet rs = JDBCUtils.getResultSet(getDocIds);
		rs.next();
		
		while(rs.next()) {
			documentUnlock(rs.getString("DOCUMENT_ID"), user_id);
		} 
	}
	
	
	/*This method does a query for the document associated with the doc id
	given and if it exists returns true, if not, it returns false*/
	public static Boolean checkForDocID(String doc_id) throws SQLException {
		String query = "SELECT DOCUMENT_ID FROM DOCUMENT WHERE DOCUMENT_ID = " + "'" + doc_id + "'";
		ResultSet rs = JDBCUtils.getResultSet(query);
		if(rs.next()) {
			rs.close();
			return true;
		}
		else {
			rs.close();
			return false;
		}	
	}
	
	
	//This method randomly generates an array of 5 doc id's of documents that can be locked
	public static ArrayList<String>  filesToLock() throws SQLException {
		ArrayList<String> docIDs = new ArrayList<>();
		Random rand = new Random();
		
		//Queries validated and complete documents because these are the ones that can be locked
		String countRowsQuery = "SELECT COUNT(Document_ID) FROM Document WHERE DOCUMENT_STATE_ID = 3"; 
		ResultSet rs1 = JDBCUtils.getResultSet(countRowsQuery);
		rs1.next();
		
		//Finds how many documents of this type there are so the random number doesn't go out of range
		int upperBound = rs1.getInt("COUNT(DOCUMENT_ID)");
		rs1.close();
		
		//real upper bound is -6 because the second number is 6 above the random number (including only documents) and this helps to avoid going out of bounds 
		int randomNumber = rand.nextInt(upperBound - 6);
		int secondNumber = randomNumber + 6;
		
		//This query will pull up rows of 5 documents between the row numbers set randomly above
		String sampleDocsQuery = "SELECT * FROM (select m.*, rownum r from document m WHERE DOCUMENT_STATE_ID = 3)"
				+ " where r >" + randomNumber + "and r < " + secondNumber;
		ResultSet rs2 = JDBCUtils.getResultSet(sampleDocsQuery);
		
		//adds each document id to the array and returns the array
		while(rs2.next()) {
			docIDs.add(rs2.getString("DOCUMENT_ID"));
		}
		rs2.close();
		
		
		return docIDs;
	}
	

	//This method randomly generates an array of 5 doc id's of documents that can be unlocked
	public static ArrayList<String> filesToUnlock() throws SQLException {
		ArrayList<String> docIDs = new ArrayList<>();
		Random rand = new Random();
		
		//Queries validated and complete documents because these are the ones that can be unlocked
		String countRowsQuery = "SELECT COUNT(Document_ID) FROM Document WHERE DOCUMENT_STATE_ID = 2"; 
		ResultSet rs1 = JDBCUtils.getResultSet(countRowsQuery);
		rs1.next();
		
		//Finds how many documents of this type there are so the random number doesn't go out of range
		int upperBound = rs1.getInt("COUNT(DOCUMENT_ID)");
		rs1.close();
		
		//real upper bound is -6 because the second number is 6 above the random number (including only five numbers) and this helps to avoid going out of bounds
		int randomNumber = rand.nextInt(upperBound - 6);
		int secondNumber = randomNumber + 6;
		
		//This query will pull up rows of 5 documents between the row numbers set randomly above
		String query = "SELECT * FROM (select m.*, rownum r from document m WHERE DOCUMENT_STATE_ID = 2)"
				+ " where r >" + randomNumber + "and r < " + secondNumber;
		ResultSet rs = JDBCUtils.getResultSet(query);
		
		//adds each document id to the array and returns the array
		while(rs.next()) {
			docIDs.add(rs.getString("DOCUMENT_ID"));
		}
		rs.close();
		
		
		return docIDs;
	}
	
}
