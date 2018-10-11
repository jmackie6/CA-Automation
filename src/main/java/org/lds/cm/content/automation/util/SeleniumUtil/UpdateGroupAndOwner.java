package org.lds.cm.content.automation.util.SeleniumUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class UpdateGroupAndOwner {

    // work in progress

    public static void selectContentGroup(WebDriver driver, String group) {
        System.out.println("\nSelecting Content Group to reassign to\n");

        List<WebElement> dropdown = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[1]/div/div/group-owner/div/div[2]/div/div/div[1]/select/option")));
        int count = dropdown.size();

        for (int i = 1; i <= count; i++) {
            WebElement content_group = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/group-owner/div/div[2]/div/div/div[1]/select/option[" + i + "]")));
            String name = content_group.getText();

            if (name.equals(group)) {
                content_group.click();
            }
        }
    }

    public static void selectOwner(WebDriver driver, String owner) {
        System.out.println("\nSelecting Owner to reassign to\n");
        WebElement element = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"singleSelect\"]")));
        element.click();

        List<WebElement> dropdown = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id=\"singleSelect\"]/option")));
        int count = dropdown.size();

        for (int i = 1; i <= count; i++) {
            WebElement assigned_owner = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"singleSelect\"]/option[" + i + "]")));
            String name = assigned_owner.getText();

            if (name.equals(owner)) {
                assigned_owner.click();
            }
        }
    }

    public static void saveAndClose(WebDriver driver) {
        System.out.println("\nSaving changes to group and owner");
        WebElement save = (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div/div/group-owner/div/div[3]/button")));
        save.click();
    }
}
