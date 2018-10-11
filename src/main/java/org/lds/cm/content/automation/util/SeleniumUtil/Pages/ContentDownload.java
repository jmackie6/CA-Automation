package org.lds.cm.content.automation.util.SeleniumUtil.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ContentDownload {
    public static void goToPage (WebDriver driver) throws InterruptedException{
        System.out.println("\nContent Download page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavContentDownload\"]"));
        page.click();
        Thread.sleep(1000);
    }

    public static void download (WebDriver driver, int docRow) {
        System.out.println("\nSelecting document to download\n");

        List<WebElement> content = driver.findElements(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/table/tbody/tr"));
        int length = content.size();

        for (int i = 1; i <= length; i++) {
            if (docRow == i) {
                WebElement download = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/table/tbody/tr[" + i + "]/td[4]/div"));
                download.click();
                WebElement doc = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/table/tbody/tr[" + i + "]/td[1]"));
                String name = doc.getText();
                System.out.println("\nDownloading " + name + "\n");
            }
        }
    }
}
