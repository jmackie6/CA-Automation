package org.lds.cm.content.automation.tests.endpoints.Scriptures;

import java.io.IOException;

import org.apache.http.ParseException;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.NetUtils;
import org.lds.cm.content.automation.util.XMLUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.NodeList;

public class ScriptureRefTest {
	@Test (groups = { "endpoints" })
	public void generateScriptureWs() throws ParseException, IOException, IllegalArgumentException
	{
		String checkScriptureRef = checkScriptureEndpoint("/ws/v1");
		Assert.assertTrue(checkScriptureRef.contains("error=\"false\"" + " changed=\"true\""), checkScriptureRef);
	}

	@Test (groups = { "endpoints" })
	public void generateScriptureServ() throws ParseException, IOException, IllegalArgumentException
	{
		String checkScriptureRef = checkScriptureEndpoint("/services/api");
		Assert.assertTrue(checkScriptureRef.contains("error=\"false\"" + " changed=\"true\""), checkScriptureRef);
	}

	public static String checkScriptureEndpoint(String type) throws ParseException, IOException, IllegalArgumentException
	{
		System.out.println("\n**** In checkScriptureEndpoint ****\n");
		//https://publish-test.ldschurch.org/content_automation/ws/v1/generateScriptureRefs?source=I was reading 1 Nephi 1:11 the other day.
		
		//String fileId = "I was reading 1 Nephi 1:11 the other day. Try an incorrect one 5 Nephi 7:47, how bout a third? try Mosiah 3:16";
		String fileId = "For all of us, our personal revelations reflect the pattern of revelation received by prophets, as recounted in the "
				+" scriptures. Adam and Eve called upon the name of the Lord and received personal revelation, including knowledge of the Savior.6 "
				+" [See  Moses 5:4–11 Enoch, Abraham, and Moses sought for the welfare of their people and were given marvelous revelations recorded "
				+" in the Pearl of Great Price.7 [See  Genesis 18:23–33  Exodus 3:1–3  Exodus 32:31–33  Moses 1:1–2, 24  Moses 6:26–37  Moses 7:2–4  Abraham 1:1–2, 15–19 "
				+" Elijah’s personal revelation came through the still, small voice;8 [See  1 Kings 19:11–12 Daniel’s came in a dream.9 [See  Daniel 2:16–20 "
				+" Peter’s personal revelation gave him a testimony that Jesus is the Christ.10 [See  Matthew 16:15–17 Lehi and Nephi received revelations "
				+" about the Savior and the plan of salvation, and virtually all of the Bible and Book of Mormon prophets received revelations to warn, teach, "
				+" strengthen, and comfort them and their people.11 [See  1 Nephi 2:16, SEP  1 Nephi 11:1–2 for additional examples, see  Mosiah 3:1–4  Alma 43:23  "
				+" Helaman 7,  Helaman 8,  Helaman 10:2–4, SEP 3 Nephi 1:10–13  Mormon 8:34–35  Ether 3:1–6, 13–14, 25, Doctrine and Covenants 1:11, 4 Nephi 170:470";

		fileId = fileId.replaceAll(" ", "%20"); 
		
		String  epString = Constants.baseURL + type + "/generateScriptureRefs?source=" + fileId;
		
		String xml = NetUtils.getHTML(epString);
		System.out.println("XML: " + xml);
		
		String name = "//@changed";
		
		NodeList file_node = XMLUtils.getNodeListFromXpath(xml, name, null);

		for (int i = 0; i < file_node.getLength(); i++) 
		{
			String nameString = file_node.item(i).getNodeValue();	
			System.out.println("Changed: "  + nameString);
		}
		
		String error = "//@error";
		
		NodeList file_node2 = XMLUtils.getNodeListFromXpath(xml, error, null);

		for (int i = 0; i < file_node2.getLength(); i++) 
		{
			String nameString = file_node2.item(i).getNodeValue();	
			System.out.println("Error: "  + nameString);
		}
		
		String aclass = "//@class";
		
		NodeList file_node3 = XMLUtils.getNodeListFromXpath(xml, aclass, null);

		for (int i = 0; i < file_node3.getLength(); i++) 
		{
			String nameString = file_node3.item(i).getNodeValue();	
			//System.out.println("Class: "  + nameString);
		}
		
		String href = "//@href";
		
		NodeList file_node4 = XMLUtils.getNodeListFromXpath(xml, href, null);

		for (int i = 0; i < file_node4.getLength(); i++) 
		{
			String nameString = file_node4.item(i).getNodeValue();	
			System.out.println("Href: "  + nameString);
		}

		return xml;
			
	}
	
}
