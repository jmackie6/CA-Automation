package org.lds.cm.content.automation.util.SeleniumUtil;

import org.junit.Assert;
import org.lds.cm.content.automation.util.Constants.Constants;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.awt.*;
import java.awt.event.InputEvent;

public class PreTestSetup {

	private static Robot robot;

//	private static boolean systemMacOs = System.getProperty("os.name").contains("Mac");
//
//	public static WebDriver setupChromeDriver() throws InterruptedException{
//		Toolkit toolkit = Toolkit.getDefaultToolkit();
//		int width = (int) toolkit.getScreenSize().getWidth();
//		int height = (int) toolkit.getScreenSize().getHeight();
//		WebDriver driver = getChromeDriver();
//		driver.manage().window().setSize(new Dimension(width, height));
//		driver.manage().window().maximize();
//
//		driver.get(Constants.baseURL);
//		Thread.sleep(2000);
//		if(driver.getTitle().equals("Sign in")){
//			System.out.println("Successfully got to sign in page");
//		}else{
//			System.out.println("Didn't get to sign in page");
//			Assert.fail();
//		}
//
//		return driver;
//
//
//	}
//
//	public static WebDriver setupFirefoxDriver() throws InterruptedException{
//		Toolkit toolkit = Toolkit.getDefaultToolkit();
//		int width = (int) toolkit.getScreenSize().getWidth();
//		int height = (int) toolkit.getScreenSize().getHeight();
//		WebDriver driver = getFirefoxDriver();
//		driver.manage().window().setSize(new Dimension(width, height));
//		driver.manage().window().maximize();
//
//		driver.get(Constants.baseURL);
//		Thread.sleep(2000);
//		if(driver.getTitle().equals("Sign in")){
//			System.out.println("Successfully got to sign in page");
//		}else{
//			System.out.println("Didn't get to sign in page");
//			Assert.fail();
//		}
//
//		return driver;
//
//	}

	public static WebDriver setup() throws InterruptedException {
		/**If there is an unkown error with the chrome driver try updating it.
		 *
		 * Open up iTerm
		 *  type in "cd Downloads"
		 *  type in "npm install chromedriver"
		 *     //may need to be update chromedriver
		 */

		//String safariDriverPath = "/usr/bin/safaridriver";
		String firefoxDriverPath = "C:\\geckodriver-v0.18.0-win64\\geckodriver.exe";

		System.setProperty("webdriver.gecko.driver", firefoxDriverPath);
		String chromePath = getChromeDriverPath();
		System.setProperty("webdriver.chrome.driver", chromePath);
		//System.setProperty("webdriver.safari.driver" , safariDriverPath);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int width = (int) toolkit.getScreenSize().getWidth();
		int height = (int) toolkit.getScreenSize().getHeight();

		WebDriver driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(width, height));
		driver.manage().window().maximize();

		driver.get(Constants.baseURL);
		Thread.sleep(2000);
		if (driver.getTitle().equals("Sign in")) {
			System.out.println("successfully got to sign in page");
		} else {
			System.out.println("unsuccessful getting to sign in page");
			Assert.fail();
		}
//		driver.get("https://publish-stage.ldschurch.org)";
//		System.out.println("successfully opened CC-Stage");
// 		driver.get("https://publish-dev.ldschurch.org");
// 		System.out.println("successfully opened CC-Dev");

		return driver;
	}

	public static String getChromeDriverPath(){
		String path;
		if (System.getProperty("os.name").contains("Mac")){
			path = System.getProperty("user.home") + "/Documents/chromedriver";
		} else {
			path = "./src/main/resources/drivers/chromedriver.exe";
		}

		return path;
	}

//	public static String getFirefoxDriverPath(){
//
//		if (systemMacOs){
//			return "./src/main/resources/drivers/firefox_mac_driver/geckodriver";
//		} else {
//			return "./src/main/resources/drivers/firefox_win_driver/geckodriver.exe";
//		}
//
//
//	}
//
//	public static String getChromeDriverPath(){
//
//		if (systemMacOs){
//			return "./src/main/resources/drivers/chrome_mac_driver/chromedriver";
//		} else {
//			return "./src/main/resources/drivers/chrome_win_driver/chromedriver.exe";
//		}
//
//	}
//
//	public static WebDriver getChromeDriver(){
//		if(systemMacOs){
//			System.setProperty("webdriver.chrome.driver", getChromeDriverPath());
//			return new ChromeDriver();
//		}else{
//			System.setProperty("webdriver.chrome.driver", getChromeDriverPath());
//			return new ChromeDriver();
//		}
//	}
//
//	public static WebDriver getFirefoxDriver(){
//		if(systemMacOs){
//			System.setProperty("webdriver.gecko.driver", getFirefoxDriverPath());
//			return new FirefoxDriver();
//		}else{
//			System.setProperty("webdriver.gecko.driver", getFirefoxDriverPath());
//			return new FirefoxDriver();
//		}
//	}

	public static Robot getRobotInstance()
	{
		try {
			if (robot == null)
				robot = new Robot();
		} catch(Exception e){}

		return robot;
	}

	public static void mouseMove(WebElement element)
	{
		getRobotInstance().mouseMove(element.getLocation().getX(), element.getLocation().getY());
		getRobotInstance().mousePress(InputEvent.BUTTON1_DOWN_MASK);
		getRobotInstance().mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
}