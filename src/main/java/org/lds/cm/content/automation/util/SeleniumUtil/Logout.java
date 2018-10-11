package org.lds.cm.content.automation.util.SeleniumUtil;

import junit.framework.Assert;
import org.lds.cm.content.automation.util.Constants.MenuButtonConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Logout {

    public static void logout (WebDriver driver){
        System.out.println("Attempting to Logout Now");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //changed id from mainNavMenuButton -> NavigationMenu1
        WebElement main_menu_button = driver.findElement(By.xpath(MenuButtonConstants.MainMenuButtonXpath));
        main_menu_button.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //WebElement logout_button = driver.findElement(By.id("logout-form));
        WebElement logout_button = (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.elementToBeClickable(By.id("logout-form")));
        logout_button.click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        driver.navigate().refresh();

        String expectedTitle = "Sign in";

        try {
            Assert.assertEquals(expectedTitle, driver.getTitle());
            System.out.println("On" + driver.getTitle() + "page");
            System.out.println("Successful Logout");
        } catch(Throwable pageNavigationError) {
            System.out.println("Should be on" + expectedTitle);
            System.out.println("On" + driver.getTitle() + "page");
            System.out.println("Unsuccessful Logout");
            Assert.fail();
        }
    }

}
