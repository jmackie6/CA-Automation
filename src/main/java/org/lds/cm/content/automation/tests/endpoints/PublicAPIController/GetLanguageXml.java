package org.lds.cm.content.automation.tests.endpoints.PublicAPIController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.lds.cm.content.automation.model.CustomException;
import org.lds.cm.content.automation.model.GetLanguageXmlModel;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.JDBCUtils;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.SeleniumUtil.AdminPages.APIRules;
import org.springframework.util.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetLanguageXml {

	private ArrayList<String> commonLanguages = new ArrayList<String>();
	SoftAssert sa = new SoftAssert();


	/**Get the top languages that have more than 10,000 documents applied towards them or
	 * the top 20 from that list.
	 *
	 */
	@BeforeClass (alwaysRun=true)
	public void setUp() throws SQLException, InterruptedException
	{
		APIRules.fixAPIRule("ws/v1/getLanguageXml");
		if(commonLanguages.size() > 0)
			return;
		ResultSet rs = JDBCUtils.getResultSet("select language_id, l.lang_name, c from\n" +
				"  ( select language_id, count(document_id) as c\n" +
					"from document \n" +
				"    group by language_id \n" +
				"    having count(document_id) > 10000)\n" +
				"    natural join language l where language_id = language_code\n" +
				"    order by c desc");
		int counter = 0;
		while(rs.next() && counter <= 20)
		{
				commonLanguages.add(rs.getString("lang_name"));
				counter ++;
		}
		rs.close();
	}

	/**Grab all of the top languages and see if they work correctly*/
	@Test (groups = { "endpoints" })
	public void checkUsualLanguagesWs()	throws SQLException, IOException
	{	checkUsualLanguages("/ws/v1");	}

	@Test (groups = { "endpoints" })
	public void checkUsualLanguagesServ() throws SQLException, IOException
	{	checkUsualLanguages("/services/api");	}

	private void checkUsualLanguages(String type) throws SQLException, IOException, IllegalArgumentException
	{
		//Can get around findBugs for following ResultSet by making multiple calls to the database instead of one
		StringBuilder sb = new StringBuilder("select * from language where lang_name='English'");
		for(String lang : commonLanguages)
			sb.append(" or lang_name='" + lang + "'");
		//System.out.println(sb.toString());

		ArrayList<String> dblanguages = new ArrayList<String>();

		ResultSet rs = JDBCUtils.getResultSet(sb.toString());
		StringBuilder url = new StringBuilder(Constants.baseURL + type + "/getLanguageXml?isoCodes=");
		while (rs.next()) {
			dblanguages.add(rs.getString("lang_name"));
			url.append(rs.getInt("language_code") + ",");
		}
		rs.close();
		int length = url.toString().length();
		String response = NetUtils.getHTML(url.toString().substring(0, length - 1)); //have to remove the trailing ,

		Assert.isTrue(dblanguages.size() == commonLanguages.size(), "Didn't grab all languages from the database.  Only grabbed "
				+ dblanguages.size());

		Assert.isTrue(response != null, "Html call returned null");
		Assert.isTrue(response.length() != 0, "Html response length is 0");

		/** Lazy and didn't do xml parsing, but spliting instead.  so there are a few round about cut ups*/
		String[] languageDetails = response.split("/>");
		ArrayList<String> languageList = new ArrayList<String>();
		languageList.add(languageDetails[0].substring(20));
		for(int i = 1; i < languageDetails.length; i++)
			languageList.add(languageDetails[i].substring(9));
		languageList.remove(languageList.size() - 1);  //last item is the 'es>' from the ending tag

		//sort all of the lists so that it is an easy straight across comparison
		java.util.Collections.sort(languageList);
		java.util.Collections.sort(dblanguages);
		java.util.Collections.sort(commonLanguages);

		Assert.isTrue(languageList.size() == dblanguages.size(), "size of response list and size of database list don't match\n"
			+ "Response=" + languageList.size() + "\tDatabase=" + dblanguages.size());
		Assert.isTrue(commonLanguages.size() == dblanguages.size(), "size of predefined language list and database list don't match");

		for(int i = 0; i < commonLanguages.size(); i++)
		{
			Assert.isTrue(commonLanguages.get(i).compareTo(dblanguages.get(i)) == 0);
			Assert.isTrue(languageList.get(i).contains(commonLanguages.get(i)));
		}
	}


	//Check Some Languages that are randomly selected from the database
	//Check them in one call
	@Test (groups = { "endpoints" })
	public void checkSomeLanguagesWs() throws SQLException, IOException
	{ checkRandomLanguages("/ws/v1", Constants.numOfTestsToRun);}

	@Test (groups = { "endpoints" })
	public void checkSomeLanguagesServ() throws SQLException, IOException
	{ checkRandomLanguages("/services/api", Constants.numOfTestsToRun);}

	private void checkRandomLanguages(String type, int amount) throws SQLException, IOException
	{
		ArrayList<GetLanguageXmlModel> languageModels = new ArrayList<GetLanguageXmlModel>();
		StringBuilder url = new StringBuilder();

		ResultSet rs = JDBCUtils.getRandomRows("language", amount);
		url.append(Constants.baseURL + type + "/getLanguageXml?isoCodes=");
		while (rs.next()) {
			String name = rs.getString("lang_name");
			String iso1 = rs.getString("iso_lang_cd_part1");
			String iso3 = rs.getString("iso_lang_cd_part3");
			String lang_code = rs.getString("language_code");
			languageModels.add(new GetLanguageXmlModel(name, iso1, iso3, lang_code));

			url.append(lang_code + ",");
		}
		rs.close();
		int length = url.toString().length();
		String response = NetUtils.getHTML(url.toString().substring(0, length - 1)); //have to remove the trailing ,

		Assert.isTrue(response != null, "Html call returned null");
		Assert.isTrue(response.length() != 0, "Html response length is 0 " + url);

		String[] languageDetails = response.split("/>");
		ArrayList<String> languageList = new ArrayList<String>();
		languageList.add(languageDetails[0].substring(20));
		for(int i = 1; i < languageDetails.length - 1; i++)
			languageList.add(languageDetails[i].substring(9));

		ArrayList<GetLanguageXmlModel> ResponseModels = new ArrayList<GetLanguageXmlModel>();
		for(String l : languageList)
		{
			String[] pieces = l.split("=");
			try {
				ResponseModels.add(new GetLanguageXmlModel(pieces[1].substring(1, pieces[1].length() - 6),
						pieces[2].substring(1, pieces[2].length() - 6), pieces[3].substring(1, pieces[3].length() - 6),
						pieces[4].substring(1, pieces[4].length() - 9)));
				//System.out.println(ResponseModels.get(ResponseModels.size() - 1).toString());
			}
			catch(IndexOutOfBoundsException e)
			{
				System.out.println("IndexOutOfBoundsException thrown for " + l);
			}
		}

		for(int x = 0; x < languageModels.size(); x++)
		{
			for(int j =0; j < ResponseModels.size(); j++)
			{
				if(languageModels.get(x).comparison(ResponseModels.get(j)))
				{
					languageModels.remove(x);
					x--;
					ResponseModels.remove(j);
					j = 1000; // just break it from this inner for loop
				}
			}
		}

		Assert.isTrue(ResponseModels.size() == 0, "html response returned too many languages"+ response + "\n" + stringify(languageModels) + "\n " + stringify(ResponseModels));

	}



	//Check A Single Random Language that is randomly selected from the database
	@Test (groups = { "endpoints" })
	public void checkALanguageWs()	throws SQLException, IOException
	{ checkRandomLanguages("/ws/v1", 1);}

	@Test (groups = { "endpoints" })
	public void checkALanguageServ() throws SQLException, IOException
	{ checkRandomLanguages("/services/api", 1);}


	//Check A predetermined bad language,  using code 170000
	@Test (groups = { "endpoints" })
	public void checkALanguageWsBad() throws IOException	{ checkBadLanguages("/ws/v1");}

	@Test (groups = { "endpoints" })
	public void checkALanguageServBad() throws IOException	{ checkBadLanguages("/services/api");}

	private void checkBadLanguages(String type) throws IOException
	{
		String url = Constants.baseURL + type + "/getLanguageXml?isoCodes=170000";
		String response = NetUtils.getHTML(url);
		Assert.isTrue(response.compareTo("<languages></languages>") == 0, "response had too much information " + response);
	}

	private String stringify(ArrayList<GetLanguageXmlModel> models)
	{
		StringBuilder sb = new StringBuilder("\n");
		for(GetLanguageXmlModel m : models)
			sb.append(m.toString() + "\n");
		return sb.toString();
	}

	@AfterMethod(alwaysRun=true)
	private void cleanUp()	{ JDBCUtils.closeUp(); }
}