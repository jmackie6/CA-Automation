package org.lds.cm.content.automation.tests.endpoints.LanguageAPIController;

import oracle.jdbc.driver.Const;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.lds.cm.content.automation.util.NetUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ForeignNumeralTest {

	
	@Test(groups= { "endpoints" })
	public void checkForeignNumeral () {
		System.out.println("\n**** In checkForeignNumerals ****\n");
		
		String [][][] numerals = {
			{{"0","០"},{"1","១"},{"2","២"},{"3","៣"},{"4","៤"},{"5","៥"},{"6","៦"},{"7","៧"},{"8","៨"},{"9","៩"},{"10","១០"},{"456","៤៥៦"},		// khm - Cambodian
				{"9999999999999999999","Invalid number passed"}},	
			{{"0","०"},{"1","१"},{"2","२"},{"3","३"},{"4","४"},{"5","५"},{"6","६"},{"7","७"},{"8","८"},{"9","९"},{"10","१०"},{"456","४५६"}},		// nep - Nepali			
			{{"0","๐"},{"1","๑"},{"2","๒"},{"3","๓"},{"4","๔"},{"5","๕"},{"6","๖"},{"7","๗"},{"8","๘"},{"9","๙"},{"10","๑๐"},{"456","๔๕๖"}},		// tha - Thai
			{{"0","٠"},{"1","١"},{"2","٢"},{"3","٣"},{"4","٤"},{"5","٥"},{"6","٦"},{"7","٧"},{"8","٨"},{"9","٩"},{"10","١٠"},{"456","٤٥٦"}    	// ara - Arabic
			}, 			
			{{"0","໐"},{"1","໑"},{"2","໒"},{"3","໓"},{"4","໔"},{"5","໕"},{"6","໖"},{"7","໗"},{"8","໘"},{"9","໙"},{"10","໑໐"},{"456","໔໕໖"}},		// lao - Lao			
			{{"0","۰"},{"1","۱"},{"2","۲"},{"3","۳"},{"4","۴"},{"5","۵"},{"6","۶"},{"7","۷"},{"8","۸"},{"9","۹"},{"10","۱۰"},{"456","۴۵۶"}},	// pes - Persian
			{{"1","Ա"},{"2","Բ"},{"3","Գ"},{"4","Դ"},{"5","Ե"},{"6","Զ"},{"7","Է"},{"8","Ը"},{"9","Թ"},{"10","Ժ"},  							// hye - Armenian
				{"20","Ի"},{"30","Լ"},{"40","Խ"},{"50","Ծ"},{"60","Կ"},{"70","Հ"},{"80","Ձ"},{"90","Ղ"},{"100","Ճ"},{"15","ԺԵ"},{"456","ՆԾԶ"}},
			{{"0","Number must be greater than 0"},{"1","፩"},{"2","፪"},{"3","፫"},{"4","፬"},{"5","፭"},{"6","፮"},{"7","፯"},{"8","፰"},{"9","፱"},		// amh - Amharic
				{"10","፲"},{"20","፳"},{"30","፴"},{"40","፵"},{"50","፶"},{"60","፷"},{"70","፸"},{"80","፹"},{"90","፺"},
				{"100","፻"},{"10000","፼"},{"12","፲፪"},{"123","፻፳፫"},{"1234","፲፪፻፴፬"},{"200","፪፻"},{"1100","፲፩፻"},{"10100","፼፻"},
				{"9999999999","Number too large"}}		
		};											
		
		String langEnum [] = {"khm","nep","tha","ara","lao","pes","hye","amh"};

		try {
			// For each language with special characters
			for (int i = 0; i < numerals.length; i++) {
				
				// Display the language being tested
				System.out.println("\n" + langEnum[i]);
				
				// For each of the number-numeral pairs 
				for (int j = 0; j < numerals[i].length; j++) {
					String apiNumeral = NetUtils.getHTML(Constants.baseURL + "/ws/v1/language/getForeignNumber?number="+ numerals[i][j][0] + "&lang=" + langEnum[i]);
					System.out.println(apiNumeral + " : " + numerals[i][j][1]);
					Assert.assertEquals(apiNumeral, numerals[i][j][1]);
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\n**** checkForeignNumerals complete****\n");
	}	
	
	//https://publish-dev.ldschurch.org/content_automation/ws/v1/language/getForeignNumber?number=11000&lang=amh	
}
