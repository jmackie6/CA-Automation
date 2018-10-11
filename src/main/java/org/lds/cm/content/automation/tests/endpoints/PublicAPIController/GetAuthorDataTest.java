package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.ErrorUtils;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.springframework.util.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class GetAuthorDataTest {

	private enum DateType { valid, badLength, InvalidDate }
	private static final String queryByName = "select * from mdmr_twp.mdm_lis_assignment where " +
			"preferred_given_name=? and preferred_surname=?";

	//couldn't find how to add a null to a prepared statement hence the couple of queries
	private static final String CompleteQuery = "select * from mdmr_twp.mdm_lis_assignment " +
			"where group_nm=? and group_id=? and position_nm=? and seniority_number=?";
	private static final String CompleteQuery2 = "select * from mdmr_twp.mdm_lis_assignment " +
			"where group_nm=? and group_id=? and position_nm=? and seniority_number is null";


	@BeforeClass (alwaysRun = true)
	public void setUp() throws SQLException, InterruptedException
	{	APIRules.fixAPIRule("ws/v1/getAuthorData");	}

	@Test (groups = { "endpoints" })
	public void checkAuthorDataValidWs() throws ParserConfigurationException, SQLException, IOException, SAXException
	{	getTestResultsValid("/ws/v1");	}

	@Test (groups = { "endpoints" })
	public void checkAuthorDataValidServ()  throws ParserConfigurationException, SQLException, IOException, SAXException
	{	getTestResultsValid("/services/api");	}


	@Test (groups = { "endpoints" })
	public void checkAuthorDataBadLengthWs()	throws IOException, SQLException
	{		getTestResultsBad(DateType.badLength, "/ws/v1");	}

	@Test (groups = { "endpoints" })
	public void checkAuthorDataBadLengthServ()	throws IOException, SQLException
	{		getTestResultsBad(DateType.badLength, "/services/api");	}

	@Test (groups = { "endpoints" })
	public void checkAuthorDataBadDateWs()	throws IOException, SQLException
	{		getTestResultsBad(DateType.InvalidDate, "/ws/v1");	}

	@Test (groups = { "endpoints" })
	public void checkAuthorDataBadDateServ()	throws IOException, SQLException
	{		getTestResultsBad(DateType.InvalidDate, "/services/api");	}

	@Test (groups = { "endpoints" })
	public void checkAuthorDataBadUserWs()	throws SQLException
	{	getTestResultsBadUser("Suzzy", "Something", "/ws/v1");	}

	@Test (groups = { "endpoints" })
	public void checkAuthorDataBadUserServ() throws SQLException
	{ getTestResultsBadUser("Suzzy", "Something", "/ws/v1");	}


	private void getTestResultsValid(String type)
			throws ParserConfigurationException, SQLException, IOException, SAXException
	{
		ArrayList<String> errors = new ArrayList<String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document xmlDoc = null;
		ArrayList<AuthorData> DatabaseAuthors = new ArrayList<AuthorData>();

		ResultSet rs = JDBCUtils.getRandomRows("mdmr_twp.mdm_lis_assignment", Constants.numOfTestsToRun);
		while (rs.next()) {
			String givenname = rs.getString("preferred_given_name");
			String surname = rs.getString("preferred_surname");
			String startDate = rs.getString("begin_dt");
			String endDate = rs.getString("end_dt");
			DatabaseAuthors.add(new AuthorData(givenname, surname, startDate, endDate));
		}
		rs.close();

		for (AuthorData ad : DatabaseAuthors) {
			if (ad.notNull()) {
				String name = ad.GivenName + " " + ad.surname;
				name = name.replace(" ", "%20");
				name = name.replace("%20%20", "%20"); //sometimes there are double spaces for no reason...
				String url = Constants.baseURL + type + "/getAuthorData?authorName=" + name + "&eventDate=" + ad.dateInRange();

				String response = NetUtils.getHTML(url);

				InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
				xmlDoc = builder.parse(is);

				String authName = xmlDoc.getElementsByTagName("author-name").item(0).getTextContent();
				String authRole = xmlDoc.getElementsByTagName("author-role").item(0).getTextContent();
				String authSeniority = xmlDoc.getElementsByTagName("author-seniority").item(0).getTextContent();
				String authRank = xmlDoc.getElementsByTagName("author-rank").item(0).getTextContent();
				String authTitle = xmlDoc.getElementsByTagName("author-title").item(0).getTextContent();

				ArrayList<String> fillInData = new ArrayList<>();
				fillInData.add(authRole);
				fillInData.add(authRank);
				fillInData.add(authTitle);

				ResultSet rs2 = null;
				//authSeniority is sent back as 0 if it is null in the database
				if (authSeniority.compareTo("0") == 0) {
					rs2 = JDBCUtils.getResultSet(CompleteQuery2, fillInData);
				}
				else {
					fillInData.add(authSeniority);
					rs2 = JDBCUtils.getResultSet(CompleteQuery, fillInData);
				}

				boolean correct = false;
				String DatabasePulledName = "";
				while (rs2.next()) {
					DatabasePulledName = rs2.getString("preferred_given_name") + rs2.getString("preferred_surname");

					//authName comes from the response with black diamonds for special letters (accents etc....)
					//set both to low encryption so that all strange stuff comes back as regular ?'s
					DatabasePulledName = new String(DatabasePulledName.getBytes("ASCII"));
					authName = new String(authName.getBytes("ASCII"));
					if (DatabasePulledName.compareTo(authName) == 0)
						correct = true;
				}
				rs2.close();
				is.close();

				if (!correct)
					errors.add("html response - " + response + "\nName pulled from database - " + DatabasePulledName);
			}
		}
		Assert.isTrue(errors.size() == 0, ErrorUtils.stringify(errors));
	}

	private void getTestResultsBad(DateType dt, String type)
			throws IOException, SQLException
	{
		ArrayList<String> errors = new ArrayList<String>();
		ArrayList<AuthorData> DatabaseAuthors = new ArrayList<AuthorData>();

		ResultSet rs = JDBCUtils.getRandomRows("mdmr_twp.mdm_lis_assignment", Constants.numOfTestsToRun);
		while (rs.next()) {
			String givenname = rs.getString("preferred_given_name");
			String surname = rs.getString("preferred_surname");
			String startDate = rs.getString("begin_dt");
			String endDate = rs.getString("end_dt");
			DatabaseAuthors.add(new AuthorData(givenname, surname, startDate, endDate));
		}
		rs.close();

		for (AuthorData ad : DatabaseAuthors) {
			StringBuilder sb = new StringBuilder(Constants.baseURL + type + "/getAuthorData?authorName=");
			if (ad.notNull()) {
				String name = ad.GivenName + " " + ad.surname;
				name = name.replace(" ", "%20");
				name = name.replace("%20%20", "%20"); //sometimes there are double spaces for no reason...
				sb.append(name + "&eventDate=");
				if (dt == DateType.InvalidDate)
					sb.append("17480250");
				else //(dt == DateType.badLength)
					sb.append("2015011");

				String response = NetUtils.getHTML(sb.toString());

				if (dt == DateType.badLength) {
					if (response.compareTo("<authors>Invalid eventDate.  Format YYYYMMDD required.</authors>") != 0)
						errors.add("wrong error message returned for " + sb.toString());
				} else //(dt == DateType.InvalidDate)
				{
					if (response.compareTo("<authors>null</authors>") != 0)
						errors.add("data was returned when it shouldn't " + sb.toString());
				}

			}
		}
		Assert.isTrue(errors.size() == 0, "Not all calls failed \n" + ErrorUtils.stringify(errors));
	}

	private void getTestResultsBadUser(String givenName, String surname, String type) throws SQLException
	{
		ArrayList<String> fillInData = new ArrayList<>();
		fillInData.add(givenName);
		fillInData.add(surname);
		ResultSet rs = JDBCUtils.getResultSet(queryByName, fillInData);
		if (rs.next())
			Assert.isTrue(1==2, "Database returned a user..." + givenName + " " + surname);
		rs.close();

		ArrayList<String> errors = new ArrayList<>();

		String url = "";
		try {
			String name = givenName + " " +surname;
			name = name.replace(" ", "%20");
			url = Constants.baseURL + type + "/getAuthorData?authorName=" + name + "&eventDate=201510101";

			String response = NetUtils.getHTML(url);

			if(response.compareTo("<authors>null</authors>") != 0)
				errors.add( "data was returned when it shouldn't " + url+ "\n" + response);

		} catch (Exception e) {
			errors.add(" url-" + url + "\nRecieved" + e.getMessage());
		}
		Assert.isTrue(errors.size() == 0, "Not all calls failed \n" + ErrorUtils.stringify(errors));
	}


	@AfterMethod(alwaysRun=true)
	public void cleanup()
	{	JDBCUtils.closeUp();	}


	private static class AuthorData
	{
		protected String GivenName = "", surname = "", Role = "", seniority = "", rank = "", title = "", startDate = "", endDate = "";

		//database setup
		protected AuthorData(String gn, String sn,  String r, String s, String ra, String t, String sd, String ed)
		{
			GivenName = gn;
			surname = sn;
			Role = r;
			if(s == null)
				seniority = "0";
			else
				seniority = s;
			rank = ra;
			title = t;
			startDate = sd;
			endDate = ed;
		}

		protected AuthorData(String gn, String sn, String sd, String ed)
		{
			GivenName = gn;
			surname = sn;
			startDate = sd;
			endDate = ed;
		}

		protected boolean comparison(AuthorData ad2)
		{
			String Name = GivenName + surname;
			String SecondName = ad2.GivenName + ad2.surname;

			if(Name.compareTo(SecondName) != 0 || ad2.Role.compareTo(Role) != 0)
				return false;
			if(ad2.seniority.compareTo(seniority) != 0 || ad2.rank.compareTo(rank) != 0)
				return false;
			if(ad2.title.compareTo(title) != 0)
				return false;

			//start and end date don't need to match because response data won't have them, but database pulled data will
			return true;
		}

		//Find the difference between the start and end date, divide by 2 and add to startDate.
		//If something doesn't parse correctly, return the startDate
		protected String dateInRange()
		{
			try {
				int x = Integer.parseInt(startDate);
				int y = 0;
				if (endDate != null) {
					y = Integer.parseInt(endDate);
				}

				x += Math.abs((x-y)/2);
				return "" + x;
			}
			catch(NumberFormatException e)
			{
				return startDate;
			}
		}

		public String toString()
		{
			return GivenName + " " + surname + " " + Role + " " + seniority + " " + rank + " " + title + " " + startDate + " " + endDate;
		}

		public boolean notNull()
		{
			if(GivenName == null || surname == null)
				return false;
			return true;
		}
	}
}
