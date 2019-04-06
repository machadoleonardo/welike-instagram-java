package br.com.welike.instagram.service;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrapingService {

    public boolean exists(WebDriver driver, String xpath) {
        return driver.findElements(By.xpath(xpath)). size() != 0;
    }

    public void waitVisibility(WebDriverWait wait, String xpath) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    private void setSmallResolution(WebDriver driver) {
        Dimension dimension = new Dimension(300, 500);
        driver.manage().window().setSize(dimension);
    }

    private void scrollToMaxBotton(WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");
    }
}
