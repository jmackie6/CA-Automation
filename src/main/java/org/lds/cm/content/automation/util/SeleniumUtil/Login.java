package org.lds.cm.content.automation.util.SeleniumUtil;

import org.lds.cm.content.automation.model.UserModels.UserBaseClass;
import org.lds.cm.content.automation.util.SetupTests;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.testng.Assert;

import java.sql.SQLException;

import static org.lds.cm.content.automation.util.LogUtils.*;

public class Login {
    private static final Logger LOG = getLogger();

    public static void login(WebDriver driver, String username, String password) throws SQLException{
        WebElement usernameTextBox = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("IDToken1")));
        usernameTextBox.clear();
        usernameTextBox.sendKeys(username);

        WebElement passwordTextBox = driver.findElement(By.id("IDToken2"));
        passwordTextBox.clear();
        passwordTextBox.sendKeys(password);

        WebElement signInButton = driver.findElement(By.id("login-submit-button"));
        signInButton.submit();

        String expectedTitle = "Content Central";

        try {
            Assert.assertEquals(expectedTitle, driver.getTitle());
            LOG.info("{} On Correct Page", driver.getTitle());
        } catch (Throwable pageNavigationError) {
            try {
                WebElement element = (new WebDriverWait (driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"details\"]/h1/font")));
                Assert.assertEquals("Access Denied", element.getText());
            } catch (Exception e) {
                LOG.error("Expected Title: {}\nRecieved Title: {}\nNot on the right page after logging on", expectedTitle, driver.getTitle());
                Assert.fail();
            }
        }

        UserBaseClass user = UserBaseClass.createUser(username);
    }
}
