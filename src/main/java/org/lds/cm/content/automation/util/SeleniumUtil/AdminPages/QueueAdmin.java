package org.lds.cm.content.automation.util.SeleniumUtil.AdminPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class QueueAdmin {

    public static void goToPage(WebDriver driver) {
        System.out.println("\nGoing to Queue Admin page\n");
        WebElement page = driver.findElement(By.xpath("//*[@id=\"mainNavQueueAdmin\"]"));
        page.click();
    }

    public static void stopQueue(WebDriver driver, String name) {
        System.out.println("\nSelecting queue to stop\n");
        List<WebElement> table  = driver.findElements(By.xpath("/html/body/div/div/div[2]/div[2]/div/table/tbody/tr"));
        int length  = table.size();

        for (int i = 1; i <= length; i++) {
            WebElement row = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/table/tbody/tr[" + i + "]/td[1]"));
            String queue_name = row.getText();

            if (queue_name.equals(name)) {
                WebElement stop = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[2]/div/table/tbody/tr[" + i + "]/td[7]/span[1]"));
                stop.click();
            }
        }
    }
}
