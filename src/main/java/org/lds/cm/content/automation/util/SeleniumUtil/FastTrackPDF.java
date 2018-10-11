package org.lds.cm.content.automation.util.SeleniumUtil;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.*;

import java.util.List;

public class FastTrackPDF {

	public static void moveAllToFrontMatter (WebDriver driver) {
		System.out.print("\nMoving all documents to front matter\n");
		WebElement move_all = driver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[4]"));
		move_all.click();
	}

	public static void allToSourceFromFrontMatter (WebDriver driver) {
		System.out.println("\nMoving all documents from front matter back to source\n");
		//													 	  /html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[1]
		WebElement move_front_back = driver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[1]"));
		move_front_back.click();
	}

	public static void selectDocuments (WebDriver driver, String[] documents) {
		System.out.println("\nSelecting Document For FastTrack PDF\n");

		List<WebElement> document_list = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[4]/div/div[1]/div/ul/li")));
		int doc_count = document_list.size();

		for (int i = 1; i <= doc_count; i++) {
			WebElement doc_name = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[1]/div/ul/li[" + i + "]")));
			String doc_info = doc_name.getText();

			for (int j = 0; j < documents.length; j++) {
				if (doc_info.contains(documents[j])) {
					doc_name.click();
				}
			}
		}
	}

	public static void selectedDocsToFrontMatter (WebDriver driver) {
		System.out.println("\nMoving Selected Documents To Front Matter\n");
		WebElement selected_docs_to_front = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[3]/strong/i")));
		selected_docs_to_front.click();
	}

	public static void selectedDocsFromFrontToSource (WebDriver driver) {
		System.out.println("\nMoving Selected Documents From Front To Source\n");
		WebElement selected_docs_from_front = driver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[2]"));
		selected_docs_from_front.click();
	}

	public static void selectedDocsToBodyMatter (WebDriver driver) {
		System.out.println("\nMoving Selected Documents To Body Matter\n");
		WebElement selected_docs_to_body = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[7]/strong/i")));
		selected_docs_to_body.click();
	}

	public static void selectedDocsFromBodyToSource (WebDriver driver) {
		System.out.println("\nMoving Selected Documents From Body To Source\n");
		WebElement selected_docs_from_body = driver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[6]"));
		selected_docs_from_body.click();
	}

	public static void allDocsToBody (WebDriver driver) {
		System.out.println("\nMoving All Documents To Body\n");
		WebElement docs_to_body = driver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[8]"));
		docs_to_body.click();
	}

	public static void allDocsFromBodyToSource (WebDriver driver) {
		System.out.println("\nMoving All Documents From Body To Source\n");
		//														 /html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[5]
		WebElement body_to_source = driver.findElement(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[2]/div[3]/div/div[2]/div/button[5]"));
		body_to_source.click();
	}

	public static void selectCSS (WebDriver driver, String css) throws InterruptedException {
		System.out.println("\nSelecting CSS Type\n");

		WebElement dropdown = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"cssStyleDropdown\"]/select")));
		dropdown.click();

		List<WebElement> css_list = (new WebDriverWait(driver, 60))
				.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"cssStyleDropdown\"]/select/option")));

		int css_count = css_list.size();

		for (int i = 1; i <= css_count; i++) {

			WebElement css_type = (new WebDriverWait(driver, 30))
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"cssStyleDropdown\"]/select/option[" + i + "]")));
			String name = css_type.getText();

			if (name.equals(css)) {
				css_type.click();
			}
		}
	}

	public static void languageToCopyFrom (WebDriver driver, String language) {
		System.out.println("\nSelecting Langauge To Copy From\n");

		WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"defaultLanguageSelect\"]"));
		dropdown.click();

		boolean language_found = true;

		List<WebElement> language_list = driver.findElements(By.xpath("//*[@id=\"defaultLanguageSelect\"]/option"));
		int language_count = language_list.size();

		for (int i = 1; i <= language_count; i++) {
			WebElement lang_row = driver.findElement(By.xpath("//*[@id=\"defaultLanguageSelect\"]/option[" + i + "]"));
			String lang_type = lang_row.getText();

			if (lang_type.equals(language)) {
				lang_row.click();
			} else {
				language_found = false;
			}
		}
		if (!language_found) {
			System.out.println("\nLanguage Not Found\n");
		}
	}

	public static void languageToApplyTo (WebDriver driver, String language) {
		System.out.println("\nSelecting Language To Apply To\n");

		boolean language_found = true;

		List<WebElement> apply_to = driver.findElements(By.xpath("//*[@id=\"copyToLanguagesSelect\"]/option"));
		int count = apply_to.size();

		for (int i = 1; i <= count; i++) {
			WebElement lang = driver.findElement(By.xpath("//*[@id=\"copyToLanguagesSelect\"]/option[" + i + "]"));
			String text = lang.getText();

			if (text.equals(language)) {
				lang.click();
			} else {
				language_found = false;
			}
		}
		if (!language_found) {
			System.out.println("\nLanguage Not Found\n");
		}
	}

	public static void confirmCreation (WebDriver driver) {
		System.out.println("\nCreating Fast Track PDF\n");
		WebElement create = (new WebDriverWait(driver, 30))
				.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/print-folder-component/div/div[3]/button[2]")));
		create.click();
	}

	public static void cancel (WebDriver driver) {
		System.out.println("\nCancelling Fast Track PDF creation\n");
		WebElement cancel = driver.findElement(By.xpath("//*[@id=\"printModal\"]/div/div/div[3]/button[1]"));
		cancel.click();
	}

	public static void exitIcon (WebDriver driver) {
		System.out.println("\nExiting FastTrack PDF Modal\n");
		WebElement exit_icon = driver.findElement(By.xpath("//*[@id=\"printModal\"]/div/div/div[1]/div/div[2]/button/i"));
		exit_icon.click();
	}
}
