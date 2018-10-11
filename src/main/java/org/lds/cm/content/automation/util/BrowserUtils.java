package org.lds.cm.content.automation.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class BrowserUtils
{

    public static ArrayList<String> errorLogCheck(WebDriver driver)
    {
        LogEntries logs = driver.manage().logs().get(LogType.BROWSER);

        Iterator it = logs.iterator();
        ArrayList<String> errorStrings = new ArrayList<String>();
        while(it.hasNext())
        {
            String error = it.next().toString();
            if(error.contains("[SEVERE]"))
                errorStrings.add(error);
        }
        return errorStrings;
    }

    public static void sleep(int miliseconds)
    {
        try  {  Thread.sleep(miliseconds);     }
        catch(InterruptedException e){}
    }

}
