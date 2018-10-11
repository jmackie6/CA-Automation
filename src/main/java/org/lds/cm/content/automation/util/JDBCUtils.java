package org.lds.cm.content.automation.util;

import org.apache.http.NameValuePair;
import org.lds.cm.content.automation.enums.DocumentSource;
import org.lds.cm.content.automation.model.ReturnObj;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import java.io.*;
import java.sql.*;
import java.util.*;


public class JDBCUtils {
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private static PreparedStatement ps = null;
	private static ReturnObj returnObj = new ReturnObj();

	private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * Opens a connection only if it is not open yet
	 */
	public static Connection getConnection() throws SQLException {
		if (conn == null) {
			System.out.println("DB: " + Constants.dbUsername + "@" + Constants.dbUrl);
			conn = DriverManager.getConnection(Constants.dbUrl, Constants.dbUsername, Constants.dbPassword);
		}
		if(conn.isClosed())
		{
			conn = DriverManager.getConnection(Constants.dbUrl, Constants.dbUsername, Constants.dbPassword);
		}

		return conn;
	}


	public static void setUpECConnection() throws SQLException{
		if(!conn.isClosed())
			conn.close();
		if(conn == null){
			conn = DriverManager.getConnection(Constants.ECdbUrl, Constants.ECdbUsername, Constants.ECdbPassword);
		}
		if(conn.isClosed())
		{
			conn = DriverManager.getConnection(Constants.ECdbUrl, Constants.ECdbUsername, Constants.ECdbPassword);
		}
	}

	 /**
	 * Returns the result set of a query, returns null if not a 'select...' query
	 * @return 
	 * @throws SQLException 
	 */	
	public static ResultSet getResultSet(String query) throws SQLException {
        conn = getConnection();
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();

        return rs;
	}



	public static ResultSet getResultSet(String query, ArrayList<String> fillIn) throws SQLException {
		conn = getConnection();
		ps = conn.prepareStatement(query);
		//conn = DriverManager.getConnection(Constants.dbUrl, Constants.dbUsername, Constants.dbPassword);
		int index = 1;
		for (String currentString : fillIn){
			ps.setString(index++, currentString);
		}
		System.out.println(ps.toString());
		rs = ps.executeQuery();
		return rs;
	}





	public static ReturnObj getResultSet(String preparedQuery, ArrayList<String> fillInData, Boolean isWild) throws SQLException {
		conn = getConnection();
		conn.createStatement();
		ps = conn.prepareStatement(preparedQuery);
		System.out.println(ps.toString());

		for(int x = 0; x < fillInData.size(); x++) {

			if(fillInData.get(x).compareTo("null") == 0)
				ps.setNull((x+1), Types.INTEGER);
			else if(isWild)
				ps.setString((x + 1), fillInData.get(x) + "%");
			else
				ps.setString((x + 1), fillInData.get(x));
		}

		// SELECT / UPDATE
		if(preparedQuery.toLowerCase().contains("select")) {
			rs = ps.executeQuery();
			returnObj.resultSet = rs;
		}
		else if(preparedQuery.toLowerCase().contains("update") || preparedQuery.toLowerCase().contains("delete") ){
			returnObj.intVal = ps.executeUpdate();
		}

		return returnObj;
	}

	public static String getTestUserID() throws SQLException{
		String sqlString = "SELECT au.APP_USER_ID FROM APP_USER au WHERE UPPER(au.PREFERRED_NAME) LIKE '%TEST%'";
		final ResultSet rs = getResultSet(sqlString);
		if(rs.next()){
			return rs.getBigDecimal("APP_USER_ID").toString();
		}

		return null;
	}


	public static int executeUpdate(String preparedQuery, ArrayList<String> fillInData) throws SQLException, InterruptedException, IllegalArgumentException, IndexOutOfBoundsException {
		conn = getConnection();
		conn.createStatement();
		ps = conn.prepareStatement(preparedQuery);
		for(int x = 0; x < fillInData.size(); x++)
			ps.setString((x+1), fillInData.get(x));
		int update = ps.executeUpdate();
		return update;
	}


	/**
	 *
	 *
	 * NamedParameterJDBCTemplate example
	 *
	 *
	 *
	 *
	 */

	public static ResultSet namedJDBCStatementTemplate(String sqlQuery, Map<String, String> params){

		//NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate();
		return null;
	}




	/**
	 * Used for inserting, updating, or deleting records
	 * @param update
	 * @throws SQLException
	 * @return Number of impacted rows or 0 if update threw SQLException
	 */
	public static int executeUpdate(String update) throws SQLException {
		conn = getConnection();
		int updateNum = conn.createStatement().executeUpdate(update);
		return updateNum;
	}

	public static String clobToString(Clob data) throws IOException, SQLException {
		StringBuilder sb = new StringBuilder();
		try(Reader reader = data.getCharacterStream()) {
			try(BufferedReader br = new BufferedReader(reader)) {
				String line;
				while(null != (line = br.readLine())){
					sb.append(line);
				}
			}
		}
		return sb.toString();
	}

	//return the number of rows in a table in a database
	public static int getCount(String table)
	{
		String query = "select count(*) as NumOfRows from table";
		int x = 0;
		try
		{
			ResultSet rs = JDBCUtils.getResultSet(query);
			if(rs.next())
				x = rs.getInt("NumOfRows");
			rs.close();
		}
		catch(Exception e){}

		return x;
	}

	/**
	 * @param table - name of the table to query
	 * @param amount - the number of rows that you would like back
	 * @return
	 */
	public static ResultSet getRandomRows(String table, int amount)
	{
		String query = "select * from (select * from " + table + " order by dbms_random.value) where rownum <= " + amount;
		try {
			return getResultSet(query);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public static String blobToString(Blob data) throws IOException, SQLException, ClassNotFoundException {
		return new String(data.getBytes(1, (int) data.length()));
	}

	public static void closeUp()
	{
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch(SQLException e){}

		try {
			if (ps != null) {
				ps.close();
				ps = null;
			}
		} catch(SQLException e){}

		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		}catch(SQLException e){}

		try{
			if(rs != null)
				rs.close();
		}catch(SQLException e){}
	}
}

