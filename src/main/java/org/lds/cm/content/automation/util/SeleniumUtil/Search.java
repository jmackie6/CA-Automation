package org.lds.cm.content.automation.util.SeleniumUtil;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Search {
	
	public static void search (WebDriver driver, String searchTerm) {


		WebElement searchBar = (new WebDriverWait(driver, 20))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"searchBar\"]")));

		searchBar.clear();
		searchBar.sendKeys(searchTerm);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebElement searchButton = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[1]")));
		searchButton.click();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void searchButton (WebDriver driver) {
		WebElement searchButton = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[1]/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[1]/div/button[1]")));

		searchButton.click();
	}

	public static void searchUri(WebDriver driver){
		WebElement file_uri_radio_button = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[1]/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[2]/input")));

		file_uri_radio_button.click();

		BrowserUtils.sleep(5000);

		if(file_uri_radio_button .isSelected()){
			System.out.println("uri radio button is selected on dashboard");
		}
		else {
			System.out.println("Unable to select uri radio button on dashboard\n");
			Assert.fail();
		}
	}

	public static void searchFileId(WebDriver driver) {
		WebElement file_id_radio_button = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[3]")));
		file_id_radio_button.click();
	}

	public static void searchFileName(WebDriver driver){
		WebElement file_name_radio_button = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[1]/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[4]")));
		file_name_radio_button.click();

		BrowserUtils.sleep(5000);

		if(file_name_radio_button.isSelected()){
			System.out.println("File_name radio button selected on dashboard");
		}
		else {
			Assert.fail("Unable to select file_name radio button on dashboard");
		}
	}

	public static void searchPath(WebDriver driver){
		WebElement file_path_radio_button = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[1]/div[2]/doc-search/div[1]/div[1]/div[1]/div[1]/div[2]/div/label[5]")));
		file_path_radio_button.click();
		BrowserUtils.sleep(5000);
	}

	public static void selectContentGroup(WebDriver driver, String contentGroup){

		System.out.println("INFO: Selecting content group dropdown");
		WebElement select_content_group = driver.findElement(By.xpath("//*[@id=\"group_chosen\"]/a/span"));

		select_content_group.click();

		BrowserUtils.sleep(1000);

		System.out.println("INFO: finding element with the list of content groups");

		BrowserUtils.sleep(1000);

		WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"group_chosen\"]/div/ul"));
		System.out.println("INFO: iterating through each content group");
		List<WebElement> options = dropdown.findElements(By.tagName("li"));
		Boolean contentExist = false;
		for (WebElement option : options)
		{
			if (option.getText().equals(contentGroup))
			{
				System.out.println("INFO: content group found and selected");
				contentExist = true;
				BrowserUtils.sleep(1000);

				option.click(); // click the desired option
				break;
			}
		}

		if (!contentExist){
			System.out.println("ERROR: Content group does not exist or the content group passed in was misspelled\nFirst letter is capitalized and same applies if there is a second word");
			Assert.fail();
		}
		BrowserUtils.sleep(1000);

	}

	public static void selectingContentGroupBySearching (WebDriver driver, String contentGroup){
		System.out.println("INFO: Selecting contentGroup dropdown");
		WebElement select_content_group_dropdown = driver.findElement(By.xpath("//*[@id=\"group_chosen\"]/a/span"));
		select_content_group_dropdown.click();
		BrowserUtils.sleep(1000);


		WebElement input_users = driver.findElement(By.xpath("//*[@id=\"group_chosen\"]/div/div/input"));
		input_users.clear();
		input_users.sendKeys(contentGroup);
		BrowserUtils.sleep(1000);


		System.out.println("INFO: Pushing enter");

		input_users.sendKeys(Keys.ENTER);
		BrowserUtils.sleep(1000);

	}

	public static void deleteSelectedContentGroup(WebDriver driver){

		BrowserUtils.sleep(1000);

//		WebElement home_button = driver.findElement(By.xpath("//*[@id=\"homeButton\"]/a"));
//		home_button.click();

		BrowserUtils.sleep(5000);

		WebElement deleteContentGroupDashboard = driver.findElement(By.xpath("//*[@id=\"group_chosen\"]/a/abbr"));
		deleteContentGroupDashboard.click();

		BrowserUtils.sleep(1000);

	}

	public static void selectUser(WebDriver driver, String user){
		System.out.println("\nSelecting user on dashboard\n");
		System.out.println("INFO: Checking for a content group selected");

		WebElement groupSelected = driver.findElement(By.xpath("//*[@id=\"group_chosen\"]/a/abbr"));
		WebElement home_button = driver.findElement(By.xpath("//*[@id=\"homeButton\"]/a"));
		home_button.click();

		BrowserUtils.sleep(6000);


		try {
			driver.findElement(By.xpath("//*[@id=\"group_chosen\"]/a/abbr"));
		} catch (NoSuchElementException e) {
			System.out.println("ERROR: Content group not selected");
			Assert.fail();
		}

		if (groupSelected != null){

			System.out.println("INFO: working");
		}
		else {
			System.out.println("ERROR: Content group not selected");
			Assert.fail();
		}

		BrowserUtils.sleep(1000);


		System.out.println("INFO: Selecting user dropdown");
		WebElement select_users = driver.findElement(By.xpath("//*[@id=\"user_chosen\"]/a/span"));
		select_users.click();
		BrowserUtils.sleep(1000);


		System.out.println("INFO: finding element with the list of users");
		BrowserUtils.sleep(1000);


		WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"user_chosen\"]/div/ul"));
		System.out.println("INFO: iterating through each user");
		List<WebElement> userOptions = dropdown.findElements(By.tagName("li"));
		Boolean userExist = false;
		for (WebElement option : userOptions)
		{
			if (option.getText().equals(user))
			{
				System.out.println("INFO: user found and selected");
				userExist = true;
				BrowserUtils.sleep(1000);

				option.click(); // click the desired option
				break;
			}
		}

		if (!userExist){
			System.out.println("ERROR: user does not exist or the user passed in was misspelled\nFirst letter is capitalized and same applies if there is a last name");
			Assert.fail();
		}

		BrowserUtils.sleep(1000);

	}

	public static void selectUserBySearching(WebDriver driver, String user) {
		System.out.println("\nSelecting user by searching on dashboard\n");
		System.out.println("INFO: Checking for a content group selected");

		WebElement groupSelected = driver.findElement(By.xpath("//*[@id=\"group_chosen\"]/a/abbr"));
		WebElement home_button = driver.findElement(By.xpath("//*[@id=\"homeButton\"]/a"));
		home_button.click();

		BrowserUtils.sleep(1000);


//		try {
//			driver.findElement(By.xpath("//*[@id=\"group_chosen\"]/a/abbr"));
//		} catch (NoSuchElementException e) {
//			System.out.println("ERROR: Content group not selected");
//			Assert.fail();
//		}

		if (groupSelected != null){

			System.out.println("INFO: Content group selected");
		}
		else {
			System.out.println("ERROR: Content group not selected");
			Assert.fail();
		}

		BrowserUtils.sleep(1000);


		System.out.println("INFO: Selecting user dropdown");
		WebElement select_users_dropdown = driver.findElement(By.xpath("//*[@id=\"user_chosen\"]/a/span"));
		select_users_dropdown.click();
		BrowserUtils.sleep(1000);


		WebElement input_users = driver.findElement(By.xpath("//*[@id=\"user_chosen\"]/div/div/input"));
		input_users.clear();
		input_users.sendKeys(user);
		BrowserUtils.sleep(1000);


		System.out.println("INFO: Pushing enter");

		input_users.sendKeys(Keys.ENTER);
		BrowserUtils.sleep(1000);

	}

	public static void deleteSelectedUser(WebDriver driver){

		System.out.println("\nDeleting the selected user from the dashboard search\n");
		BrowserUtils.sleep(1000);


		WebElement deleteUserDashboard = driver.findElement(By.xpath("//*[@id=\"user_chosen\"]/a/abbr"));
		deleteUserDashboard.click();

		BrowserUtils.sleep(1000);

	}

	public static void searchLanguageTyping(WebDriver driver, String language){

		System.out.println("\nSearch by language\n");
		WebElement LanguageSearch = driver.findElement(By.xpath("//*[@id=\"languages_chosen\"]/ul/li/input"));
		LanguageSearch.clear();
		System.out.println("INFO: typing in language given");
		LanguageSearch.sendKeys(language);

		System.out.println("INFO: Pushing enter");

		LanguageSearch.sendKeys(Keys.ENTER);
	}

	public static void searchLanguageSelect(WebDriver driver, String language){

		System.out.println("\nSearch by language by selecting from dropdown and not typing language\n");
		WebElement languageSearch = driver.findElement(By.xpath("//*[@id=\"languages_chosen\"]/ul/li/input"));
		languageSearch.click();

		WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"languages_chosen\"]/div/ul"));
		System.out.println("INFO: iterating through each language");
		List<WebElement> languageOptions = dropdown.findElements(By.tagName("li"));
		Boolean langExist = false;
		for (WebElement option : languageOptions)
		{
			if (option.getText().equals(language))
			{
				System.out.println("INFO: language found and selected");
				langExist = true;
				BrowserUtils.sleep(1000);

				option.click(); // click the desired option
				break;
			}
		}

		if (!langExist){
			System.out.println("ERROR: Language must contain three letter code like this Navajo (nav) or the language was misplelled");
			Assert.fail();
		} else {
			System.out.println("INFO: Successful with selecting tha language given");
		}

		BrowserUtils.sleep(1000);

	}

	public static void deleteLanguage(WebDriver driver){
//		//*[@id="languages_chosen"]/ul/li[1]/a
//		WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"languages_chosen\"]/ul/li[1]"));
//		System.out.println("INFO: iterating through each language");
//		List<WebElement> languageOptions = dropdown.findElements(By.tagName("a"));
//		Boolean langExist = false;
//		for (WebElement option : languageOptions)
//		{
//			if (option.getText().equals(language))
//			{
//				System.out.println("INFO: language found and selected");
//				langExist = true;
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//
//					e.printStackTrace();
//				}
//				option.click(); // click the desired option
//				break;
//			}
//		}
	}

	public static void totalFilter (WebDriver driver){
		System.out.println("clicking the total filter ");
		WebElement total = driver.findElement(By.xpath("//*[@id=\"search-directive-quick-stats\"]/li[1]/a"));
		total.click();
	}

	public static void addedFilter (WebDriver driver){
		System.out.println("clicking the added filter ");
		WebElement added = driver.findElement(By.xpath("//*[@id=\"search-directive-quick-stats\"]/li[2]/a"));
		added.click();
	}

	public static void lockedFilter (WebDriver driver){
		System.out.println("clicking the locked filter ");
		WebElement locked = driver.findElement(By.xpath("//*[@id=\"search-directive-quick-stats\"]/li[3]/a"));
		locked.click();
	}

	public static void validatedFilter (WebDriver driver){
		System.out.println("clicking the validated filter ");
		WebElement validated = driver.findElement(By.xpath("//*[@id=\"search-directive-quick-stats\"]/li[4]/a"));
		validated.click();
	}

	public static void refreshButton(WebDriver driver){
		System.out.println("refresh button\n ");
		WebElement refresh = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/div/button[1]")));
		if (refresh.isDisplayed()) {
			System.out.println("Refresh button is visible and clickable");
			refresh.click();
		}
		else {
			System.out.println("Refresh button is not visible and not clickable");
			Assert.fail();
		}

	}

	public static void clearSearch (WebDriver driver){
		System.out.println("clear search button\n ");
		WebElement clearSearch = driver.findElement(By.xpath("/html/body/div/div/div[2]/doc-search/div[1]/div[1]/div[1]/div[2]/div/div/button[2]"));

		if (clearSearch.isDisplayed()) {
			System.out.println("Clear Search button is visible and clickable");
			clearSearch.click();
		}
		else {
			System.out.println("Clear Search button is not visible and not clickable");
			Assert.fail();
		}

	}


}
