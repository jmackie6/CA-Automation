package org.lds.cm.content.automation.util.SeleniumUtil;

import com.sun.istack.internal.NotNull;
import org.lds.cm.content.automation.util.BrowserUtils;
import org.openqa.selenium.*;

import java.awt.*;
import java.util.List;

import junit.framework.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Dashboard {

	private static java.util.Random random = new java.util.Random();

	public static void clickActionsButton(WebDriver driver, String filename) throws InterruptedException {
		List<WebElement> rows = driver.findElements(By.tagName("tr"));

		for (int i = 0; i < rows.size(); i++) {
			if (rows.get(i).getText().contains(filename)) {
				List<WebElement> columns = rows.get(i).findElements(By.tagName("td"));
				for (int j = 0; j < columns.size(); j++) {
					if (columns.get(j).getText().contains("Actions")) {
						WebElement actions = columns.get(j);
						actions.click();
						Thread.sleep(2000);
					}
				}
			}
		}
	}

	public static void clickGearIcon (WebDriver driver) {

		WebElement gearIcon = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"admin-menu--id\"]/a")));

		gearIcon.click();
	}

	public static void reportsTab (WebDriver driver) {
		WebElement reports = driver.findElement(By.xpath("//*[@id=\"mainNavReportsButton\"]"));
		reports.click();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void goHome (WebDriver driver) {

		WebElement home_button = driver.findElement(By.xpath("//*[@id=\"dashboard-id\"]"));
		home_button.click();
	}

	public static void expandAll (WebDriver driver){
		System.out.println("Expand all button\n ");
		WebElement expandAll = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[1]/button[2]"));
		expandAll.click();
	}

	public static void collapseAll (WebDriver driver){
		System.out.println("Expand all button\n ");
		WebElement collapseAll = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[1]/button[3]"));

		collapseAll.click();
	}


	/**
	 *
	 * @param driver
	 * @param pageSize - just the number  25, 50, 75, 100, 150, 200, 300
	 */
	public static void selectingPageSize (WebDriver driver, String pageSize){
		System.out.println("Results per page \n ");

		WebElement resultsPerPage = (new WebDriverWait(driver, 10))
			.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"pageSize\"]")));
		Select rpp  = new Select(resultsPerPage);

		String value = "Show " + pageSize + " per page";
		rpp.selectByVisibleText(value);
	}

	public static void pageNumber (WebDriver driver, String newPageNumber ) {
		System.out.println("Go to specific page Number\n ");

		WebElement page_Number = (new WebDriverWait(driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[3]/ul/li[" + newPageNumber + "]/a")));

		if (page_Number.isDisplayed()) {
			System.out.println("page is visible and clickable");
			page_Number.click();
		}
		else {
			System.out.println("page is not visible and not clickable");
			Assert.fail();
		}
	}

	public static void goToPreviousPage(WebDriver driver) {

		System.out.println("Go to next page Number\n ");

		WebElement previousPage = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[3]/ul/li[2]/a"));
		if (previousPage.isDisplayed()) {
			System.out.println("previous page button is visible and clickable");
			previousPage.click();
		}
		else {
			System.out.println("previous page button is not visible and not clickable");
			Assert.fail();
		}
		previousPage.click();
	}

	public static void goToPreviousPageSection (WebDriver driver) {

		System.out.println("Go to next page Number\n ");

		WebElement previousPageSection = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[3]/ul/li[1]/a"));
		if (previousPageSection.isDisplayed()) {
			System.out.println("previous page section button is visible and clickable");
			previousPageSection.click();
		}
		else {
			System.out.println("previous page section button is not visible and not clickable");
			Assert.fail();
		}
		previousPageSection.click();

	}

	public static void goToNextPage (WebDriver driver){

		WebElement pageSelection = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[3]/ul"));
		List<WebElement> count = pageSelection.findElements(By.tagName("li"));
		System.out.println(count.size());

		int pageIndex = count.size() - 1;

		WebElement nextPage = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[3]/ul/li[" + pageIndex + "]/a"));
      	nextPage.click();

	}

	public static void goToLastPageSection (WebDriver driver){

		WebElement pageSelection = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[3]/ul"));
		List<WebElement> count = pageSelection.findElements(By.tagName("li"));
		System.out.println(count.size());

		WebElement nextPageSelection = driver.findElement(By.xpath("//*[@id=\"dashboardCtrl\"]/doc-tree/div[2]/div[1]/div[3]/ul/li[" + count.size() + "]/a"));
		nextPageSelection.click();

	}

	public static void downloadRandomResult (WebDriver driver)throws InterruptedException {

		java.util.Random random = new java.util.Random();
		int r = random.nextInt(4) + 23;
		//*[@id="file-tree-grid"]/table/tbody/tr[4]/td[3]/div/a/i
		WebElement downloadRandomButton = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + r + "]/td[3]/div/a/i"));
		downloadRandomButton.click();
		Thread.sleep(3000);

	}

	public static void randomActionsButton (WebDriver driver) throws InterruptedException {

		java.util.Random random = new java.util.Random();
		int r = random.nextInt(4) +23;
		WebElement downloadRandomButton = (new WebDriverWait(driver, 10))
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + r + "]/td[4]/div/span/button")));
		downloadRandomButton.click();
		Thread.sleep(2000);
	}

	public static void randomPreviewPage (WebDriver driver) throws InterruptedException {

		java.util.Random random = new java.util.Random();
		int r = random.nextInt(4) + 23;
		//*[@id="file-tree-grid"]/table/tbody/tr[4]/td[3]/div/a/i
		WebElement downloadRandomButton = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + r + "]/td[1]/span/span/a"));
		downloadRandomButton.click();
		Thread.sleep(5000);
		//*[@id="file-tree-grid"]/table/tbody/tr[9]/td[1]/span/span/a

	}

	public static void verifyDownload (WebDriver driver, String downloadPath){


	}



	public static void lastDocActionButton (WebDriver driver) throws InterruptedException {
		List<WebElement> table = (new WebDriverWait(driver, 60))
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr")));

		for (int i = 1; i <= table.size(); i++) {
			if (i == table.size()) {
				WebElement action = (new WebDriverWait(driver, 60))
						.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + i + "]/td[4]/div/span/button")));

				action.click();
			}
		}
	}
	

	public static void previewSpecificFile (WebDriver driver, String filename) throws AWTException {

		WebElement doc_name = driver.findElement(By.linkText(filename));

		((JavascriptExecutor)driver).executeScript("window.scrollTo(0, " + doc_name.getLocation().y + ")");
		doc_name.click();
	}

	public static void downloadSpecificFile(WebDriver driver, String filename){

		System.out.println(filename);
		filename = "  " + filename;
		System.out.println(filename);
		WebElement table = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody"));
		List<WebElement> rowResults = table.findElements(By.tagName("tr"));
		System.out.println("number of rows: " + rowResults.size());

		int row_num,col_num;
		row_num=1;
		for (WebElement trRow : rowResults)
		{
			WebElement filenameLink = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + row_num + "]/td[1]/span/span/a"));
			if (filenameLink.getText().equals(filename)) {
				{
					System.out.println("INFO: filename found");
					BrowserUtils.sleep(1000);
					//option.click(); // click the desired option
					WebElement downloadButton = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + row_num + "]/td[3]/div/a/i"));
					downloadButton.click();
					BrowserUtils.sleep(1000);
					break;
				}
			}
			row_num++;
		}

	}

	public static void randomCheckBox(@NotNull WebDriver driver) {
		WebElement documentsTable = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody"));
		List<WebElement> documentsTableElements = documentsTable.findElements(By.tagName("tr"));

		//Since the first row of the documentsTableElements never has a checkbox, this variable assignment is structured to always return rows 2 to the last row number
		int randomRowNumber = random.nextInt(documentsTableElements.size() - 1) + 2;

		WebElement checkBox = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody/tr[" + randomRowNumber + "]/td[1]/span/span/input"));
		checkBox.click();
		if (!checkBox.isSelected()) {
			Assert.fail();
		} else {
			System.out.println("The checkbox in row " + randomRowNumber + " was successfully checked");
		}
	}

	// need to finish TODO
	public static void rootCheckBox(@NotNull WebDriver driver) {
//		WebElement documentsTable = driver.findElement(By.xpath("//*[@id=\"file-tree-grid\"]/table/tbody"));
//		List<WebElement> documentsTableElements = documentsTable.findElements(By.tagName("tr"));

	}

	public static void openBulkOperationsMenu(@NotNull WebDriver driver) {

		WebElement bulkOperationsButton = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div[1]/div[2]/doc-tree/div[2]/div[1]/div[1]/div/button[1]")));
		//For some reason, clicking the Bulk Operations button does not work when scrolling is necessary to view it in the browser, so the sendKeys method is a workaround
		bulkOperationsButton.sendKeys(Keys.ENTER);
	}

}