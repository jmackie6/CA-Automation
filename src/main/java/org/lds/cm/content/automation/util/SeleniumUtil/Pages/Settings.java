package org.lds.cm.content.automation.util.SeleniumUtil.Pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import junit.framework.Assert;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.support.ui.Select;

import javax.lang.model.element.Element;

public class Settings {

    public static void settingsPage (WebDriver driver) {

        WebElement settingsPage = driver.findElement(By.xpath("//*[@id=\"liAdmin\"]/ul/li[2]/a"));
        settingsPage.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //pass in the driver the setting number so the first in the list would be 1 and so forth
    public static void checkSetting (WebDriver driver, int settingNumber) throws InterruptedException {

        //settingNumber = settingNumber - 1;
        WebElement checkSetting = driver.findElement(By.xpath("//html/body/div/div/div[2]/div[2]/div/div[2]/div[" + settingNumber + "]/div[1]/input"));

        checkSetting.click();
        Thread.sleep(1000);

    }



}
